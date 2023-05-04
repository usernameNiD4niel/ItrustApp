package com.rey.itrustapplication.helperclasses;

public class NotificationModel {

    String message, title;

    public NotificationModel(String title, String body) {
        this.message = body;
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
