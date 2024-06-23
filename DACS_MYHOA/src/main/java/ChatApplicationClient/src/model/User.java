package ChatApplicationClient.src.model;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class User  implements Initializable {
    private int id;
    private String userName;
    private String password;
    private Timestamp createdAt;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }



    public User(int id, String username, String password,String email, Timestamp createdAt) {
        this.id = id;
        this.userName = username;
        this.password = password;
        this.createdAt = createdAt;
        this.email = email;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}