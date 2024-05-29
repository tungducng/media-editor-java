package com.media_editor.tufng.model;

public class Session {
    private int userId;
    private String token;


    public int getUserId() {
        return userId;
    }

    public Session(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
