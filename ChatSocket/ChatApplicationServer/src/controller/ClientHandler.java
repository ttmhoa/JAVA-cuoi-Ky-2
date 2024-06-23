//package controller;
//
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ClientHandler extends Thread {
//
//    private static Map<String, PrintWriter> clientWriters = new HashMap<>();
//    private Socket socket;
//    private BufferedReader reader;
//    private PrintWriter writer;
//
//    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
//        try {
//            this.socket = socket;
//            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.writer = new PrintWriter(socket.getOutputStream(), true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void run() {
//        try {
//            String msg;
//            while ((msg = reader.readLine()) != null) {
//                if (msg.equalsIgnoreCase( "exit")) {
//                    break;
//                }
//                for (ClientHandler cl : clients) {
//                    cl.writer.println(msg);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                reader.close();
//                writer.close();
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//}