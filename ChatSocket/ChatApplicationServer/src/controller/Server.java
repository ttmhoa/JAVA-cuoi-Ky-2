package controller;

import com.google.gson.Gson;
import entity.ConversationMember;
import model.*;
import service.ConversationMemberService;
import service.ConversationService;
import service.MessageService;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static final Map<Integer, Socket> clientSockets = new HashMap<>();

    public static Map<Integer, Socket> getClientSockets() {
        return clientSockets;
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostName());
                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
        {
            while (true) {
                String messageFromClient = reader.readLine();
                System.out.println("Message from client: " + messageFromClient);

                try {
                    Gson gson = new Gson();
                    Request request = gson.fromJson(messageFromClient, Request.class);
                    if(request == null) continue;

                    switch (request.getEvent()) {
                        case "connect": // đầu tiên
                            handleConnect(request);
                            break;
                        case "getConversation": // get data cho cột bên trái, với trường hợp chưa từng nv với ai thì nó sẽ nulll
                            handleGetConversation(request);
                            break;
                        case "getMessage": // khi clịck vào 1 item ở bên trái
                            handleGetMessage(request);
                            break;
                        case "sendMessage": // khi ấn nút gửi
                            handleSendMessage(request);
                            break;
                        case "createConversation": // lúc tạo mới chat với 1 người chưa từng nhắn tin
                            handleCreateConversation(request);
                            break;
                        case "searchConversation": // search ở ô bên trái
                            handleSearchConversation(request);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            System.out.println("Client disconnected: " + socket.getInetAddress().getHostName());
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private void handleConnect(Request request) {
        Gson gson = new Gson();
        UserInfoRequest userInfoRequest = gson.fromJson(gson.toJson(request.getData()), UserInfoRequest.class);
        Server.getClientSockets().put(userInfoRequest.getUserId(), socket);
        System.out.println("User connected: " + userInfoRequest.getUserId());
    }

    private void handleSendMessage(Request request) {
        Gson gson = new Gson();
        MessageModel messageModel = gson.fromJson(gson.toJson(request.getData()), MessageModel.class);

        MessageService messageService = new MessageService();
        messageService.createMessage(messageModel);

        ConversationMemberService conversationMemberService = new ConversationMemberService();

        var receiverIds = conversationMemberService.getMembersByConversation(messageModel.getConversationId());

        for (int receiverId : receiverIds) {
                Socket receiverSocket = Server.getClientSockets().get(receiverId);

                if (receiverSocket != null) {
                    var messages = messageService.getMessagesByConversation(messageModel.getConversationId());
                    sendResponseToClient(gson.toJson(messages), receiverSocket);

                    System.out.println("Message sent to user: " + receiverId);
                } else {
                    System.out.println("User " + receiverId + " is not connected.");
                }
        }
    }

    private void handleGetConversation(Request request) {
        Gson gson = new Gson();
        int userId = gson.fromJson(gson.toJson(request.getData()), Integer.class);

        ConversationService conversationService = new ConversationService();
        List<ConversationModel> conversations = conversationService.getConversationsByUserId(userId);

        sendResponseToClient(gson.toJson(conversations), this.socket);
    }
    private void handleGetMessage(Request request) {
        Gson gson = new Gson();
        int conversationId = gson.fromJson(gson.toJson(request.getData()), Integer.class);

        MessageService messageService = new MessageService();
        List<MessageModel> messages = messageService.getMessagesByConversation(conversationId);

        for (MessageModel message : messages) {
            System.out.println("Message: " + gson.toJson(message));
        }

        sendResponseToClient(gson.toJson(messages), this.socket);
    }

    private void handleCreateConversation(Request request) {
        Gson gson = new Gson();
        ConversationModel conversationModel = gson.fromJson(gson.toJson(request.getData()), ConversationModel.class);

        // 1. Create the conversation
        ConversationService conversationService = new ConversationService();
        conversationService.createConversation(conversationModel);

        int conversationId = conversationModel.getId(); // Get the ID of the newly created conversation

        // 2. Add members to the conversatnion
        ConversationMemberService conversationMemberService = new ConversationMemberService();
        for (int memberId : conversationModel.getMemberIds()) {
            var model = new ConversationMember(conversationId, memberId, new Timestamp((new Date().getTime())));
            conversationMemberService.createConversationMember(model);
        }

        // 3. Send response to client (if needed)
        String jsonResponse = "Conversation created successfully";
        sendResponseToClient(jsonResponse, this.socket);

        System.out.println("CreateConversation event handled successfully");
    }

    private void handleSearchConversation(Request request) {
        Gson gson = new Gson();
        String keyword = gson.fromJson(gson.toJson(request.getData()), String.class);

        ConversationService conversationService = new ConversationService();
        List<ConversationModel> conversations = conversationService.searchConversations(keyword);

        String jsonResponse = gson.toJson(conversations);
        sendResponseToClient(jsonResponse, this.socket);
    }

//    private void handleJoinConversation(Request request) {
//        Gson gson = new Gson();
//        ConversationMember member = gson.fromJson(gson.toJson(request.getData()), ConversationMember.class);
//
//        ConversationMemberService memberService = new ConversationMemberService();
//        memberService.addMemberToConversation(member.getConversationId(), member.getUserId(), member.getJoinedAt());
//
//        String jsonResponse = gson.toJson("Member added to conversation successfully");
//        sendResponseToClient(jsonResponse, this.socket);
//    }

    private void sendResponseToClient(String response, Socket clientSocket) {
        try {
            Gson gson = new Gson();
            PrintWriter clientOutput = new PrintWriter(clientSocket.getOutputStream(), true);
            var  responseData = new Response("response",response);
            clientOutput.println(gson.toJson(responseData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}