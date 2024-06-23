package org.example.dacs_myhoa;


import ChatApplicationClient.src.MessageController.ClientMessageProcessor;
import ChatApplicationClient.src.model.ConversationMd;
import ChatApplicationClient.src.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;


import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.DatePicker;
import model.ConversationModel;
import model.MessageModel;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;
import java.util.Date;
import static org.example.dacs_myhoa.loginController.userId;
import static org.example.dacs_myhoa.loginController.userNameLogin;

public class Room extends Thread implements Initializable {
    @FXML
    private Button btn_send;
    @FXML
    private TextArea msgRoom;
    @FXML
    private Button close;
    @FXML
    private Button profile;
    @FXML
    private AnchorPane panehome;
    @FXML
    private AnchorPane profileanchor;
    @FXML
    private TextField nickname;
    @FXML
    private TextField phonenumber;
    @FXML
    private ImageView imgprofile;
    @FXML
    private DatePicker date;
    @FXML
    private TextField searchhere;
    @FXML
    private ListView<ConversationModel> listviewUser;
    @FXML
    private VBox listuser;
    @FXML
    private TextField msgField;
    @FXML
    private Label emailProfile;
    @FXML
    private Label nicknameProfile;
    @FXML
    private VBox messId;
    @FXML
    private Label namechat;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    private ResultSet rs;
    Date currentDate = new Date();
    private  ConversationModel selectedConversation;

    Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
    private  ClientMessageProcessor messageProcessor;

    public void connection(){
        try{
            socket = new Socket("localhost", 12345);
            messageProcessor = new ClientMessageProcessor(socket);
            messageProcessor.connect(userId,DataUser.username);
            System.out.println("Connected to Chat Server.");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void searchConversationer() {
        listviewUser.getItems().clear();
        String keywork = searchhere.getText();
        if(!keywork.isEmpty()){
            messageProcessor.searchConversation(keywork,listviewUser);
        }
    }
    @FXML
    public void getItemUser() {
        ConversationModel selectedConversation = listviewUser.getSelectionModel().getSelectedItem();
        if (selectedConversation != null && selectedConversation.getId() != null) {
            int id = selectedConversation.getId();
            listviewUser.setOnMouseClicked( mouseEvent -> {
                    messageProcessor.getMessage(id, messId);
                    String name= selectedConversation.getName();
                    namechat.setText(name);
                });
            sendMessageRoom(id);
        }
    }

//@FXML
//public void createConversationRoom() {
//    List<Integer> memberIds = new ArrayList<>();
//    memberIds.add(userId); // Thêm userId vào memberIds
//    String conversationName = DataUser.username;
//    // Lấy conversation được chọn từ listviewUser
//    ConversationModel selectedConversation = listviewUser.getSelectionModel().getSelectedItem();
//
//    if (selectedConversation != null) {
//        conversationName += selectedConversation.getName();
//
//        // Kiểm tra và thêm các memberIds từ selectedConversation
//        List<Integer> selectedMemberIds = selectedConversation.getMemberIds();
//        if (selectedMemberIds != null) {
//            for (Integer id : selectedMemberIds) {
//                memberIds.add(id);
//                System.err.println("here"+id);
//            }
//        }
//    }
//
//    // Gọi messageProcessor.createConversation() với đối tượng ConversationModel mới
//    ConversationModel conversationModel = new ConversationModel(null, conversationName, memberIds);
//    messageProcessor.createConversation(conversationModel);
//}



    public void sendMessageRoom(int conversationId) {
        String content = msgField.getText();
        MessageModel cm = new MessageModel(conversationId, userId, content, currentTimestamp);
        messageProcessor.sendMessage(cm, messId);
        msgField.clear();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection();
        if (messageProcessor != null) {
             messageProcessor.getConversation(userId, listviewUser);
        } else {
            System.out.println("messer null");
        }

    }

    private void loadProfileData() {
        String sql = "SELECT username, email " +
                "FROM users " +
                "WHERE id = ?";
        try (Connection conn = new INDEXJDBC().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                String nickName = rs.getString("username");
                String email = rs.getString("email");
                nicknameProfile.setText(nickName);
                emailProfile.setText(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private double x;
    private double y;

    public void closebtaction() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to preview?");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.get().equals(ButtonType.OK)) {
                close.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("DashBoard.Css").toExternalForm());
                stage.initStyle(StageStyle.TRANSPARENT);
                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getScreenX();
                    y = event.getScreenY();
                });
                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                    stage.setOpacity(.8);

                });
                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });
                stage.setScene(scene);
                stage.show();


            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sliderProfile(){
        profileanchor.setVisible(true);
        panehome.setVisible(false);
        loadProfileData();
    }
    public void sliderHome(){
        profileanchor.setVisible(false);
        panehome.setVisible(true);
    }


}
//    public List<User> getAllUsers() {
//        List<User> users = new ArrayList<>();
//        try {
//            PreparedStatement statement = con.prepareStatement("SELECT * FROM dbo.users");
//            ResultSet resultSet = statement.executeQuery();
//                while (resultSet.next()) {
//                    int id = resultSet.getInt("id");
//                    String username = resultSet.getString("username");
//                    String password = resultSet.getString("password");
//                    String email = resultSet.getString("email");
//                    Timestamp createdAt = resultSet.getTimestamp("created_at");
//
//                    User user = new User(id, username, password, email, createdAt);
//                    users.add(user);
//                }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return users;
//    }