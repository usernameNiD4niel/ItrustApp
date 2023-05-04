package com.rey.itrustapplication.youtube_player.models;

public class UserInteractionModel {

    private final String category;
    private final int points;

    public UserInteractionModel(String category, int points) {
        this.category = category;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public String getCategory() {
        return category;
    }
}
