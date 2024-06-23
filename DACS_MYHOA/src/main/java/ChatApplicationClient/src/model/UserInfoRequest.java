package model;

import java.io.Serializable;

public class UserInfoRequest implements Serializable {
    private int userId;

    public UserInfoRequest(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}