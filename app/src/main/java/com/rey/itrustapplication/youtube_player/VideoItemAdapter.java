package com.rey.itrustapplication.youtube_player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.rey.itrustapplication.R;

import java.util.ArrayList;

public class VideoItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_CONTENT_DESCRIPTION = 0;
    private static final int VIEW_TYPE_SUGGESTED_VIDEO = 1;
    private final ArrayList<VideoItemModel> itemModels;
    private final Context context;
    private String cdTitle, cdDesc;
    private final YouTubePlayer youTubePlayerView;

    public void setContentDescriptionData(String cdTitle, String cdDesc) {
        this.cdTitle = cdTitle;
        this.cdDesc = cdDesc;
    }

    public VideoItemAdapter(ArrayList<VideoItemModel> itemModels, Context context, YouTubePlayer youTubePlayerView) {
        this.itemModels = itemModels;
        this.context = context;
        this.youTubePlayerView = youTubePlayerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CONTENT_DESCRIPTION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_description_video_item,parent, false);
            return new ContentDescriptionViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_video_item,parent, false);
            return new SuggestedViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_CONTENT_DESCRIPTION;
        } else {
            return VIEW_TYPE_SUGGESTED_VIDEO;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentDescriptionViewHolder) {
            ContentDescriptionViewHolder viewHolder = (ContentDescriptionViewHolder) holder;
            viewHolder.description_video_item_content.setText(cdDesc);
            viewHolder.title_video_item_content.setText(cdTitle);

        } else {
            SuggestedViewHolder viewHolder = (SuggestedViewHolder) holder;
            VideoItemModel model = itemModels.get(position);
            viewHolder.title_suggested_video.setText(model.getTitle());

            holder.itemView.setOnClickListener(view -> {
                //Load new video and remove the selected video to the recyclerview

                youTubePlayerView.loadVideo(model.getThumbnailUrl(), 0f);
            });

            final String thumbnailUrl = "https://img.youtube.com/vi/" + model.getThumbnailUrl() + "/0.jpg";

            Glide.with(context).load(thumbnailUrl).into(viewHolder.thumbnail_suggested_video);
        }
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    private static class SuggestedViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail_suggested_video;
        TextView title_suggested_video;

        public SuggestedViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail_suggested_video = itemView.findViewById(R.id.thumbnail_suggested_video_item);
            title_suggested_video = itemView.findViewById(R.id.title_suggested_video_item);
        }
    }

    private static class ContentDescriptionViewHolder extends RecyclerView.ViewHolder {

        TextView title_video_item_content, description_video_item_content;

        public ContentDescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            title_video_item_content = itemView.findViewById(R.id.title_video_item_content);
            description_video_item_content = itemView.findViewById(R.id.description_video_item_content);
        }
    }

}
