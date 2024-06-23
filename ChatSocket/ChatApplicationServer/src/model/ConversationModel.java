package model;

import java.sql.Timestamp;
import java.util.List;

public class ConversationModel {
    private Integer id;
    private String name;
    private Timestamp createdAt;
    private List<Integer> memberIds;

    // Constructors, getters, and setters
    public ConversationModel(List<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public ConversationModel(int id, String name, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public ConversationModel(Integer id, String name, Timestamp createdAt, List<Integer> memberIds) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.memberIds = memberIds;
    }

    public ConversationModel(String name, List<Integer> memberIds) {
        this.name = name;
        this.memberIds = memberIds;
    }

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}