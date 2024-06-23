package entity;

import java.sql.Timestamp;

public class ConversationMember {
    private int conversationId;
    private int userId;
    private Timestamp joinedAt;

    // Constructors, getters, and setters
    public ConversationMember() {}

    public ConversationMember(int conversationId, int userId) {
        this.conversationId = conversationId;
        this.userId = userId;
    }

    public ConversationMember(int conversationId, int userId, Timestamp joinedAt) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.joinedAt = joinedAt;
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