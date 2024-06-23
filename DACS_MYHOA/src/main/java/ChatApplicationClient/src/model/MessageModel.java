package model;

import java.sql.Timestamp;

public class MessageModel {
    private int id;
    private int conversationId;
    private int senderId;
    private String content;
    private Timestamp timestamp;
    private String senderName;
    // Constructors, getters and setters
//hihi
    public MessageModel(int id, int conversationId, String senderName, String content) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public MessageModel(int id, int conversationId, int senderId, String content, Timestamp timestamp) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public MessageModel(int conversationId, int senderId, String content, Timestamp timestamp) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public MessageModel() {

    }


    // Getters and setters...

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

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