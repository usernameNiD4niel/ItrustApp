package com.rey.itrustapplication.youtube_player;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController;
import com.rey.itrustapplication.R;

import java.util.LinkedList;

public class FragmentHomePageAdapter extends Fragment {

    private final int position;
    private final LinkedList<VideoModel> videoModels;
    private YouTubePlayerView youTubePlayerView;
    private TextView caption, date;

    public FragmentHomePageAdapter(int position, LinkedList<VideoModel> videoModels) {
        // Required empty public constructor
        this.position = position;
        this.videoModels = videoModels;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page_adapter, container, false);

        caption = view.findViewById(R.id.video_data_caption_2);
        date = view.findViewById(R.id.video_data_date_2);
        youTubePlayerView = view.findViewById(R.id.youtube_video_id_2);
        getLifecycle().addObserver(youTubePlayerView);

        VideoModel model = videoModels.get(position);

        caption.setText(model.getVideoTitle());
        date.setText(model.getVideoDatePosted());

        YouTubePlayerListener youTubePlayerListener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(model.getVideoUrl(), 0f);
                DefaultPlayerUiController defaultPlayerUiController = new DefaultPlayerUiController(youTubePlayerView, youTubePlayer);
                youTubePlayerView.setCustomPlayerUi(defaultPlayerUiController.getRootView());
                defaultPlayerUiController.showVideoTitle(false);
                super.onReady(youTubePlayer);

            }

            @Override
            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
                Log.d("current second", "onCurrentSecond: " + second);
                super.onCurrentSecond(youTubePlayer, second);
            }

            @Override
            public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float duration) {
                Log.d("current second", "onCurrentSecond: " + duration);
                super.onVideoDuration(youTubePlayer, duration);
            }
        };

        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).ccLoadPolicy(0).ivLoadPolicy(1).build();
        youTubePlayerView.initialize(youTubePlayerListener, options);
        return view;
    }

    @Override
    public void onDestroy() {
        youTubePlayerView.release();
        date.setText(null);
        caption.setText(null);
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}