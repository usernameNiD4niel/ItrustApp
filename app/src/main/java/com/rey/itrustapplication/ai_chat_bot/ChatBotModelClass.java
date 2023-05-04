package com.rey.itrustapplication.ai_chat_bot;

public class ChatBotModelClass {

    private String messageChat;
    private boolean isUser;

    public ChatBotModelClass(String userMessage, boolean isUser) {
        this.messageChat = userMessage;
        this.isUser = isUser;
    }

    public boolean getIsUser() {
        return isUser;
    }

    public void setUser(boolean isUser) {
        this.isUser = isUser;
    }

    public String getMessageChat() {
        return messageChat;
    }

    public void setMessageChat(String messageChat) {
        this.messageChat = messageChat;
    }

}
