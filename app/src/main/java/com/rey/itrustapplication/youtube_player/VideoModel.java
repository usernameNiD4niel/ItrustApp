package com.rey.itrustapplication.youtube_player;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class VideoModel implements Parcelable {

    private final String videoTitle;
    private final String videoDescription;
    private final String videoUrl;
    private final String videoDatePosted;
    private final String videoCategory;

    public VideoModel(String videoUrl, String videoTitle, String videoDescription, String videoDatePosted, String videoCategory) {
        this.videoUrl = videoUrl;
        this.videoTitle = videoTitle;
        this.videoDatePosted = videoDatePosted;
        this.videoDescription = videoDescription;
        this.videoCategory = videoCategory;
    }

    protected VideoModel(Parcel in) {
        videoTitle = in.readString();
        videoDescription = in.readString();
        videoUrl = in.readString();
        videoDatePosted = in.readString();
        videoCategory = in.readString();
    }

    public static final Creator<VideoModel> CREATOR = new Creator<VideoModel>() {
        @Override
        public VideoModel createFromParcel(Parcel in) {
            return new VideoModel(in);
        }

        @Override
        public VideoModel[] newArray(int size) {
            return new VideoModel[size];
        }
    };

    public String getVideoDescription() {
        return videoDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoCategory() {
        return videoCategory;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getVideoDatePosted() {
        return videoDatePosted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(videoTitle);
        parcel.writeString(videoDescription);
        parcel.writeString(videoUrl);
        parcel.writeString(videoDatePosted);
        parcel.writeString(videoCategory);
    }
}
