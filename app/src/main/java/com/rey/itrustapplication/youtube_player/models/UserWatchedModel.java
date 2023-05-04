package com.rey.itrustapplication.youtube_player.models;

public class UserWatchedModel {

    private final String ytLink, title, category, description;

    public UserWatchedModel(String ytLink, String title, String category, String description) {
        this.ytLink = ytLink;
        this.title = title;
        this.category = category;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getYtLink() {
        return ytLink;
    }
}
