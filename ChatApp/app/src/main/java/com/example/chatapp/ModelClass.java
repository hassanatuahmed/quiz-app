package com.example.chatapp;

public class ModelClass {
    String message;
    String from,messageId;

    public ModelClass() {
    }

    public ModelClass(String message, String from, String messageId) {
        this.message = message;
        this.from = from;
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
