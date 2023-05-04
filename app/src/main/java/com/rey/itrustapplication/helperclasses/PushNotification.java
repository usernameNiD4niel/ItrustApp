package com.rey.itrustapplication.helperclasses;

public class PushNotification {

    private NotificationModel notificationModel;
    private String to;

    public PushNotification(NotificationModel notificationModel, String to) {
        this.notificationModel = notificationModel;
        this.to = to;
    }

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

    public void setNotificationModel(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
