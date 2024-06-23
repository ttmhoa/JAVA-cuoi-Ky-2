package ChatApplicationClient.src.MessageController;

import java.io.Serializable;

public class UserInfoRequest implements Serializable {
    private int userId;
    private String userName;
    public UserInfoRequest(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}