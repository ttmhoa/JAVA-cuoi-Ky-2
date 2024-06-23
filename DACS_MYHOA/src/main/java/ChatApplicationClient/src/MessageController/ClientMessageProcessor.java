package ChatApplicationClient.src.MessageController;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.ConversationModel;
import model.MessageModel;
import org.example.dacs_myhoa.Room;
import org.example.dacs_myhoa.loginController;


import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClientMessageProcessor {

    public Room rm =new Room();
    public final PrintWriter writer;
    public final BufferedReader reader;
    public final Gson gson;
    private String conversationName;

    public ClientMessageProcessor(Socket socket) throws IOException {
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.gson = new Gson();
    }

    public void connect(int userId, String username) {
        Request connectRequest = new Request("connect", new UserInfoRequest(userId, username));
        String jsonConnectRequest = gson.toJson(connectRequest);
        writer.println(jsonConnectRequest);
    }

    public void sendMessage(MessageModel chatMessage, VBox messId) {
        Request messageRequest = new Request("sendMessage", chatMessage);
        String jsonMessageRequest = gson.toJson(messageRequest);
        writer.println(jsonMessageRequest);
        receiveMessagessend(messId);
    }

    public void receiveMessagessend(VBox messId) {
        new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    LocalTime now = LocalTime.now();
                    System.out.println("Current Time: " + now);
                    handleMessagesend(message, messId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void handleMessagesend(String message,VBox messId) {
        System.out.println(message); // In ra nội dung JSON nhận được từ server
        try {
            var responseModel =this.gson.fromJson(message,Response.class );

            Type userListType = new TypeToken<List<MessageModel>>() {}.getType();
            List<MessageModel> messageModel = this.gson.fromJson(responseModel.getData().toString(), userListType);
            Platform.runLater(() -> {
                List<HBox> hBoxes = new ArrayList<>();
                for (MessageModel mess : messageModel) {
                    var isMe = loginController.userId == mess.getSenderId();
                    HBox anc = renderMessage(mess.getSenderName(),mess.getContent(), isMe);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            messId.getChildren().add(anc);
                            ScrollPane scrollPane = new ScrollPane(messId);
                            scrollPane.setFitToWidth(true);
                            scrollPane.setVvalue(1.0);
                        }
                    });
                }
            });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi giải nén JSON: " + e.getMessage());
        }
    }


    public void closeConnection() throws IOException {
        writer.close();
        reader.close();
    }

//    public void sendMessage() {
//        Gson gson = new Gson();
//        MessageModel messageModel = new MessageModel(1, 1, 1, "Hello from Client", null);
//        Request request = new Request("sendMessage", messageModel);
//        String jsonRequest = gson.toJson(request);
//        writer.println(jsonRequest);
//    }

    public void getMessage(int conversationId,VBox messId) {
        Gson gson = new Gson();
        Request request = new Request("getMessage", conversationId);

        String jsonRequest = gson.toJson(request);
        writer.println(jsonRequest);
        new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    handleMessageGet(message,messId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
    public void handleMessageGet(String message,VBox messId){
        System.out.println(message); // In ra nội dung JSON nhận được từ server
        try {
            var responseModel =this.gson.fromJson(message,Response.class );

            Type userListType = new TypeToken<List<MessageModel>>() {}.getType();
            List<MessageModel> messages = this.gson.fromJson(responseModel.getData().toString(), userListType);

            Platform.runLater(() -> {
                messId.getChildren().clear();
                for (MessageModel mess : messages) {
                    var isMe = loginController.userId == mess.getSenderId();
                    HBox anc = renderMessage(mess.getSenderName(),mess.getContent(), isMe);
                    Platform.runLater(() -> {
                        messId.getChildren().add(anc);
                    });
                }
            });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi giải nén JSON: " + e.getMessage());
        }
//        Gson gson = new Gson();
//        List<MessageModel> messageModel = gson.fromJson(message, new TypeToken<List<MessageModel>>() {}.getType());

    }

    public HBox renderMessage(String name,String message, Boolean isreceiver) {
        HBox hbox= new HBox();
        try {
            if(!message.isEmpty()){
                hbox.setAlignment(isreceiver ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                hbox.setPadding(new Insets(10,5,5,10));
                Text text =new Text(message);
                Label label= new Label( name +" ");
                TextFlow textFlow= new TextFlow(text);
                textFlow.setStyle("-fx-color: rgb(239,242,255);"+
                        "-fx-background-color: rgb(15,125,242);"+
                        "-fx-background-radius :20px;");
                textFlow.setPadding(new Insets(5,10,5,10));
                text.setFill(Color.color(0.934,0.945,0.996));
                if(!isreceiver){
                    hbox.getChildren().add(label);
                }
                hbox.getChildren().add(textFlow);
            }

        }catch (Exception e){
            System.out.println(e);
        }


        return hbox;
    }


    public void getConversation(int userId, ListView<ConversationModel> listviewUser) {
        Gson gson = new Gson();
        Request request = new Request("getConversation", userId);

        String jsonRequest = gson.toJson(request);
        writer.println(jsonRequest);

        new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    LocalTime now = LocalTime.now();
                    System.out.println("Current Time: " + now);
                    handleConversationGet(message, listviewUser);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleConversationGet(String message, ListView<ConversationModel> listviewUser) {
        System.out.println(message); // In ra nội dung JSON nhận được từ server
        try {
            var responseModel =this.gson.fromJson(message,Response.class );
            Type userListType = new TypeToken<List<ConversationModel>>() {}.getType();
            List<ConversationModel> users = this.gson.fromJson(responseModel.getData().toString(), userListType);//chuyen doi du lieu
            Platform.runLater(() -> {
                for(ConversationModel us: users){
                    listviewUser.getItems().add(us);
                }
            });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi giải nén JSON: " + e.getMessage());
        }
    }

    public void createConversation(ConversationModel conversationModel) {
        Gson gson = new Gson();
        // Create the Request
        Request request = new Request("createConversation", conversationModel);

        // Convert the Request to JSON and send it to the server
        String jsonRequest = gson.toJson(request);
        writer.println(jsonRequest);
    }


//    public void joinConversation() {
//        Gson gson = new Gson();
//        ConversationModel conversationModel = new ConversationModel(13, "Test Conversation");
//        Request request = new Request("joinConversation", conversationModel);
//
//        String jsonRequest = gson.toJson(request);
//        writer.println(jsonRequest);
//    }
//
    public void searchConversation(String keyword, ListView<ConversationModel> listviewUser) {
        Gson gson = new Gson();

        Request request = new Request("searchConversation", keyword);

        String jsonRequest = gson.toJson(request);
        writer.println(jsonRequest);
        new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    handleSearchConversationSGet(message,listviewUser);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void handleSearchConversationSGet(String message,ListView<ConversationModel> listviewUser) {
        System.out.println(message); // In ra nội dung JSON nhận được từ server
        try {

            List<Integer> memberIds = new ArrayList<>();
            var responseModel =this.gson.fromJson(message,Response.class );
            ConversationModel selectedConversation = listviewUser.getSelectionModel().getSelectedItem();
            Type userListType = new TypeToken<List<ConversationModel>>() {}.getType();
            List<ConversationModel> users = this.gson.fromJson(responseModel.getData().toString(), userListType);//chuyen doi du lieu
            Platform.runLater(() -> {
                for(ConversationModel us: users){
                    listviewUser.getItems().add(us);
                }
            });
            if (selectedConversation != null) {
                conversationName = selectedConversation.getName();

                // Kiểm tra và thêm các memberIds từ selectedConversation
                if (selectedConversation.getId() == null) {
                    List<Integer> selectedMemberIds = selectedConversation.getMemberIds();
                    for (Integer id : selectedMemberIds) {
                        memberIds.add(id);
                    }
                }
            }
            ConversationModel conversationModel = new ConversationModel(null, conversationName, memberIds);
            createConversation(conversationModel);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi giải nén JSON: " + e.getMessage());
        }
    }

}
