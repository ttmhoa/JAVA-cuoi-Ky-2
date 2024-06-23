package service;

import entity.Message;
import model.MessageModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private final Connection con;
    private final String CREATE_MESSAGE = "INSERT INTO messages (conversation_id, sender_id, content, timestamp) VALUES (?, ?, ?, ?)";
    private final String GET_MESSAGE = "SELECT * FROM messages WHERE id = ?";
    private final String UPDATE_MESSAGE = "UPDATE messages SET content = ? WHERE id = ?";
    private final String DELETE_MESSAGE = "DELETE FROM messages WHERE id = ?";
    private final String GET_MESSAGES_BY_CONVERSATION = "SELECT \n" +
            "    m.id, \n" +
            "    m.conversation_id, \n" +
            "    m.sender_id, \n" +
            "    u.username AS sender_name,\n" +
            "    m.content, \n" +
            "    m.timestamp\n" +
            "FROM messages m\n" +
            "JOIN users u ON m.sender_id = u.id\n" +
            "WHERE m.conversation_id = ?\n" +
            "ORDER BY m.timestamp ASC;";

    public MessageService() {
        ConnectionDB obj = new ConnectionDB();
        this.con = obj.getConnection();
    }

    public void createMessage(MessageModel model) {
        try {
            PreparedStatement statement = con.prepareStatement(CREATE_MESSAGE);
            statement.setInt(1, model.getConversationId());
            statement.setInt(2, model.getSenderId());
            statement.setString(3, model.getContent());
            statement.setTimestamp(4, model.getTimestamp());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Message getMessage(int id) {
        try {
            PreparedStatement statement = con.prepareStatement(GET_MESSAGE);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int conversationId = rs.getInt("conversation_id");
                int senderId = rs.getInt("sender_id");
                String content = rs.getString("content");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                return new Message(id, conversationId, senderId, content, timestamp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateMessage(Message model) {
        try {
            PreparedStatement statement = con.prepareStatement(UPDATE_MESSAGE);
            statement.setString(1, model.getContent());
            statement.setInt(2, model.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(int id) {
        try {
            PreparedStatement statement = con.prepareStatement(DELETE_MESSAGE);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MessageModel> getMessagesByConversation(int conversationId) {
        List<MessageModel> messages = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement(GET_MESSAGES_BY_CONVERSATION);
            statement.setInt(1, conversationId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int senderId = rs.getInt("sender_id");
                String senderName = rs.getString("sender_name");
                String content = rs.getString("content");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                messages.add(new MessageModel(id, conversationId, senderId, senderName, content, timestamp));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
