package com.rey.itrustapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import java.util.List;

import com.rey.itrustapplication.R;
import com.rey.itrustapplication.helperclasses.VideoItem;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder>{

    private final List<VideoItem> videoItemList;
    private final Context context;
    private final Lifecycle lifecycle;

    public VideosAdapter(Context context, List<VideoItem> videoItemList, Lifecycle lifecycle) {
        this.videoItemList = videoItemList;
        this.context = context;
        this.lifecycle = lifecycle;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VideoViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_container_video, parent, false)
        );

    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.getLifeCycle(lifecycle);
        holder.setVideoData(videoItemList.get(position));


    }

    @Override
    public int getItemCount() {
        return videoItemList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        static YouTubePlayerView youTubePlayerView;
        TextView textVideoTitle, textVideoDescription;
        ProgressBar videoProgressBar;

        Lifecycle lifecycle;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);
            videoProgressBar = itemView.findViewById(R.id.videoProgressBar);

        }

        void getLifeCycle(Lifecycle lifecycle) {
            this.lifecycle = lifecycle;
        }

        void setVideoData(VideoItem videoData) {

            textVideoTitle.setText(videoData.videoDatePosted);
            textVideoDescription.setText(videoData.videoCaption);
            videoProgressBar.setVisibility(View.VISIBLE);

            lifecycle.addObserver(youTubePlayerView);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoId = videoData.videoURL;
                    youTubePlayer.cueVideo(videoId, 0);
                    videoProgressBar.setVisibility(View.INVISIBLE);
                }


            });

            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> Toast.makeText(videoProgressBar.getContext(), "Your video is ready!", Toast.LENGTH_SHORT).show());

        }

        public static void releaseVideoPlayer() {

            youTubePlayerView.release();
        }

    }

}
