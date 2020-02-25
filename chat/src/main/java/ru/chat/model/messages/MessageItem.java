package ru.chat.model.messages;

public class MessageItem {
    private long id;
    private String username;
    private byte admin;
    private String message;
    private String date;
    private int parent_id;

    public MessageItem () {

    }

    public MessageItem(long id, String username, byte admin, String message, String date, int parent_id) {
        this.id = id;
        this.username = username;
        this.admin = admin;
        this.message = message;
        this.date = date;
        this.parent_id = parent_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte getAdmin() {
        return admin;
    }

    public void setAdmin(byte admin) {
        this.admin = admin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }
}
