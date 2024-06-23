package ChatApplicationClient.src.MessageController;

import java.sql.Timestamp;

public class ChatMessage {
    private int id;
    private int conversationId;
    private int senderId;
    private String content;
    private Timestamp timestamp;

    // Constructors, getters and setters

    public ChatMessage(int id, int conversationId, int senderId, String content, Timestamp timestamp) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public ChatMessage(int conversationId, int senderId, String content, Timestamp timestamp) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }
// Getters and setters...

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}