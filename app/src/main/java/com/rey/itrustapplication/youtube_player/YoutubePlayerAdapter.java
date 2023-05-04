package com.rey.itrustapplication.youtube_player;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.YoutubeVideoItemBinding;

import java.util.ArrayList;

public class YoutubePlayerAdapter extends RecyclerView.Adapter<YoutubePlayerAdapter.MyViewHolder> {

    private final ArrayList<VideoModel> videoModels;

    public YoutubePlayerAdapter(ArrayList<VideoModel> videoModels) {
        this.videoModels = videoModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_video_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(videoModels.get(position));
    }

    @Override
    public int getItemCount() {
        return videoModels.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        YoutubeVideoItemBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = YoutubeVideoItemBinding.bind(itemView);
            this.itemView = itemView;

        }

        void bind(VideoModel videoModel) {
            String caption = (videoModel.getVideoDescription().length() >= 25) ? videoModel.getVideoDescription().substring(0,25) + "..." : videoModel.getVideoDescription();
            binding.videoDataCaption.setText(caption);
            binding.videoDataDate.setText(videoModel.getVideoDatePosted());
            binding.videoDataTitle.setText(videoModel.getVideoTitle());

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), VideoItemActivity.class);
                intent.putExtra("url", videoModel.getVideoUrl());
                intent.putExtra("description", videoModel.getVideoDescription());
                intent.putExtra("title", videoModel.getVideoTitle());
                intent.putExtra("category", videoModel.getVideoCategory());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) view.getContext(),
                        view,
                        "transitionName");
                view.getContext().startActivity(intent, options.toBundle());
                ((Activity) view.getContext()).overridePendingTransition(R.anim.enter, R.anim.exit);
            });

            final String thumbnailUrl = "https://img.youtube.com/vi/" + videoModel.getVideoUrl() + "/0.jpg";

            Glide.with(itemView.getContext()).load(thumbnailUrl).into(binding.thumbnailYoutubeVideoImg);
        }

    }

}




