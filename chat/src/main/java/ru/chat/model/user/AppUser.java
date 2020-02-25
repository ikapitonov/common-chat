package ru.chat.model.user;

public class AppUser {
    private int id;
    private String site;
    private String email;
    private String createdAt;
    private byte active;
    private String username;
    private String token;
    private byte validate;
    private String password;
    private String last_login;

    public AppUser(int id, String site, String email, String createdAt, byte active, String username, String token, byte validate, String password, String last_login) {
        this.id = id;
        this.site = site;
        this.email = email;
        this.createdAt = createdAt;
        this.active = active;
        this.username = username;
        this.token = token;
        this.validate = validate;
        this.password = password;
        this.last_login = last_login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public byte getActive() {
        return active;
    }

    public void setActive(byte active) {
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public byte getValidate() {
        return validate;
    }

    public void setValidate(byte validate) {
        this.validate = validate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }
}

