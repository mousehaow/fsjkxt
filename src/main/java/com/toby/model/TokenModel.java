package com.toby.model;

public class TokenModel {

    private String userId;

    private String token;

    private User user;

    public TokenModel(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public TokenModel(String token) {
        this.token = token;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
