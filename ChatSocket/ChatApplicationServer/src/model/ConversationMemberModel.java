package model;

import java.sql.Timestamp;

public class ConversationMemberModel {
    private int conversationId;
    private int userId;
    private Timestamp joinedAt;

    // Constructors, getters, and setters
    public ConversationMemberModel() {}

    public ConversationMemberModel(int conversationId, int userId) {
        this.conversationId = conversationId;
        this.userId = userId;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }
}