package service;

import entity.Conversation;
import model.ConversationModel;
import model.MessageModel;
import service.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConversationService  {

    private final Connection con;
    private final String CREATE_CONVERSATION = "INSERT INTO conversations (name) VALUES (?)";
    private final String GET_CONVERSATION = "SELECT * FROM conversations WHERE id = ?";
    private final String UPDATE_CONVERSATION = "UPDATE conversations SET name = ? WHERE id = ?";
    private final String DELETE_CONVERSATION = "DELETE FROM conversations WHERE id = ?";
    private final String SEARCH_CONVERSATION = "SELECT * FROM conversations WHERE name LIKE ?";
    private final String SEARCH_CONVERSATION_user = "SELECT * FROM users WHERE username LIKE ?";
    private final String GET_CONVERSATIONS_BY_USER_ID = "SELECT c.id, c.name, c.created_at " +
            "FROM conversations c " +
            "JOIN conversation_members cm ON c.id = cm.conversation_id " +
            "WHERE cm.user_id = ?";

    public ConversationService() {
        ConnectionDB obj = new ConnectionDB();
        this.con = obj.getConnection();
    }

    public void createConversation(ConversationModel model) {
        try {
            PreparedStatement statement = con.prepareStatement(CREATE_CONVERSATION, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, model.getName());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int conversationId = generatedKeys.getInt(1);
                model.setId(conversationId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Conversation getConversation(int id) {
        try {
            PreparedStatement statement = con.prepareStatement(GET_CONVERSATION);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                Timestamp createdAt = rs.getTimestamp("created_at");
                return new Conversation(id, name, createdAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateConversation(Conversation model) {
        try {
            PreparedStatement statement = con.prepareStatement(UPDATE_CONVERSATION);
            statement.setString(1, model.getName());
            statement.setInt(2, model.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteConversation(int id) {
        try {
            PreparedStatement statement = con.prepareStatement(DELETE_CONVERSATION);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ConversationModel> searchConversations(String keyword) {
        List<ConversationModel> conversations = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement(SEARCH_CONVERSATION);
            statement.setString(1, "%" + keyword + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Timestamp createdAt = rs.getTimestamp("created_at");
                conversations.add(new ConversationModel(id, name, createdAt));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            List<Integer> usersId=new ArrayList<>();
            PreparedStatement statement = con.prepareStatement(SEARCH_CONVERSATION_user);
            statement.setString(1, "%" + keyword + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                usersId.add(rs.getInt("id"));
                String name = rs.getString("username");
                Timestamp createdAt = rs.getTimestamp("created_at");
                conversations.add(new ConversationModel(null, name, createdAt,usersId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversations;
    }

    public List<ConversationModel> getConversationsByUserId(int userId) {
        List<ConversationModel> conversations = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement(GET_CONVERSATIONS_BY_USER_ID)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Timestamp createdAt = rs.getTimestamp("created_at");
                ConversationModel conversation = new ConversationModel(id, name, createdAt);
                conversations.add(conversation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversations;
    }
}