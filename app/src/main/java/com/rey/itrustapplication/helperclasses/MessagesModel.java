package com.rey.itrustapplication.helperclasses;

public class MessagesModel {

    private String sender, receiver, content;

    public MessagesModel() {
    }

    public MessagesModel(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }
}
