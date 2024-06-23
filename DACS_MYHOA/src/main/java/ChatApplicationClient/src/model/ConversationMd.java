package ChatApplicationClient.src.model;

import java.sql.Timestamp;
import java.util.List;

public class ConversationMd {
    private int id;
    private String name;
    private long createdAt;

    public ConversationMd(int id, String name, long createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}