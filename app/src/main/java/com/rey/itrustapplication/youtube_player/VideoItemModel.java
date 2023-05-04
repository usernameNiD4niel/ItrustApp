package com.rey.itrustapplication.youtube_player;

public class VideoItemModel {

    private final String thumbnailUrl, title;

    public VideoItemModel(String thumbnailUrl, String title) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }
}
