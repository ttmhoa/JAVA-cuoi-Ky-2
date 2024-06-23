package ChatApplicationClient.src.MessageController;

import java.io.Serializable;

public class SendMessageRequest implements Serializable {
    private int conversationId;
    private int senderId;
    private String message;

    public SendMessageRequest(int conversationId, int senderId, String message) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.message = message;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}