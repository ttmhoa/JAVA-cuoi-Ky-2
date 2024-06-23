package service;

import entity.ConversationMember;
import entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConversationMemberService {

    private final Connection con;

    public ConversationMemberService() {
        ConnectionDB obj = new ConnectionDB();
        this.con = obj.getConnection();
    }

    public void createConversationMember(ConversationMember model) {
        try {
            String CREATE_CONVERSATION_MEMBER = "INSERT INTO conversation_members (conversation_id, user_id, joined_at) VALUES (?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(CREATE_CONVERSATION_MEMBER);
            statement.setInt(1, model.getConversationId());
            statement.setInt(2, model.getUserId());
            statement.setTimestamp(3, model.getJoinedAt());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ConversationMember getConversationMember(int conversationId, int userId) {
        try {
            String GET_CONVERSATION_MEMBER = "SELECT * FROM conversation_members WHERE conversation_id = ? AND user_id = ?";
            PreparedStatement statement = con.prepareStatement(GET_CONVERSATION_MEMBER);
            statement.setInt(1, conversationId);
            statement.setInt(2, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Timestamp joinedAt = rs.getTimestamp("joined_at");
                return new ConversationMember(conversationId, userId, joinedAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateConversationMember(ConversationMember model) {
        try {
            String UPDATE_CONVERSATION_MEMBER = "UPDATE conversation_members SET joined_at = ? WHERE conversation_id = ? AND user_id = ?";
            PreparedStatement statement = con.prepareStatement(UPDATE_CONVERSATION_MEMBER);
            statement.setTimestamp(1, model.getJoinedAt());
            statement.setInt(2, model.getConversationId());
            statement.setInt(3, model.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteConversationMember(int conversationId, int userId) {
        try {
            String DELETE_CONVERSATION_MEMBER = "DELETE FROM conversation_members WHERE conversation_id = ? AND user_id = ?";
            PreparedStatement statement = con.prepareStatement(DELETE_CONVERSATION_MEMBER);
            statement.setInt(1, conversationId);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getMembersByConversation(int conversationId) {
        List<Integer> memberIds = new ArrayList<>();
        try {
            String GET_MEMBERS_BY_CONVERSATION = "SELECT * FROM conversation_members WHERE conversation_id = ?";
            PreparedStatement statement = con.prepareStatement(GET_MEMBERS_BY_CONVERSATION);
            statement.setInt(1, conversationId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                memberIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberIds;
    }

    public void addMemberToConversation(int conversationId, int userId, Timestamp joinedAt) {
        try {
            String ADD_MEMBER_TO_CONVERSATION = "INSERT INTO conversation_members (conversation_id, user_id, joined_at) VALUES (?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(ADD_MEMBER_TO_CONVERSATION);
            statement.setInt(1, conversationId);
            statement.setInt(2, userId);
            statement.setTimestamp(3, joinedAt);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
