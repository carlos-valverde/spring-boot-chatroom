package com.example.springbootchatroom.model;


/**
 * WebSocket message model
 */
public class Message {

    private String user;
    private String content;

    public Message (){}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String message) {
        this.content = content;
    }

    // TODO: add message model.
}