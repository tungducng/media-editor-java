package com.media_editor.tufng.dto;

import com.media_editor.tufng.model.User;

public class LoginResponse {

    private User user;
    private String token;

    // Getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
