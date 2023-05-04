package com.rey.itrustapplication.youtube_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.ActivityVideoItemBinding;
import com.rey.itrustapplication.youtube_player.database.UserInteraction;

import java.util.ArrayList;

public class VideoItemActivity extends AppCompatActivity {

    private ActivityVideoItemBinding binding;
    private VideoItemAdapter videoItemAdapter;
    private ArrayList<VideoItemModel> modelArrayList;
    public YouTubePlayer player;
    private String url, description, title, category;
    private YouTubePlayerListener listener;
    private UserInteraction userInteraction;
    private int points;
    private float previousTime = 0f;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.enter, R.anim.exit);

        url = getIntent().getStringExtra("url");
        description = getIntent().getStringExtra("description");
        title = getIntent().getStringExtra("title");
        category = getIntent().getStringExtra("category");

        modelArrayList = new ArrayList<>();
        userInteraction = new UserInteraction(getApplicationContext());

        databaseReference = FirebaseDatabase.getInstance().getReference("VideoRecommendation/" + category);

        loadTheVideo();

        binding.backButtonVideoItem.setOnClickListener(view -> onBackPressed());

    }

    @SuppressLint("NotifyDataSetChanged")
    private void populateRecyclerView() {

        //use the category here for fetching video user data

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final String video_url = snapshot.child("videoUrl").getValue(String.class);
                        final String video_description = snapshot.child("videoDescription").getValue(String.class);

                        if (video_url != null && !video_url.equals(url)) {
                            modelArrayList.add(new VideoItemModel(video_url, video_description));
                        }
                    }

                    if (modelArrayList.isEmpty()) {
                        String placeHolder = "No Video yet about " + category + " or similar to " + title;
                        binding.noDataFoundVideoActivity.setText(placeHolder);
                        binding.noDataFoundVideoActivity.setVisibility(View.VISIBLE);
                        return;
                    }
                    videoItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(valueEventListener);
/*
        final String dummyUrl1 = "IRtMe76kmXY"; //
        final String dummyUrl2 = "x2wGX5LFuOc"; //
        final String dummyUrl3 = "_nMdn6EI6WA"; //
        final String dummyUrl4 = "TfN9-SamIss"; //
        final String dummyUrl5 = "y7BbYKkFwCw"; //

        modelArrayList.add(new VideoItemModel(dummyUrl1, "Menstruation and Pregnancy"));
        modelArrayList.add(new VideoItemModel(dummyUrl2, "Menstrual Cycle and Pregnancy Myths Busted by an Ob/Gyn | Stanford"));
        modelArrayList.add(new VideoItemModel(dummyUrl3, "What are the Signs and Symptoms of Menopause?"));
        modelArrayList.add(new VideoItemModel(dummyUrl4, "Pharmacology - Menstrual Cycle and Hormonal Contraceptives"));
        modelArrayList.add(new VideoItemModel(dummyUrl5, "TV Patrol: Pamamahagi ng Contraceptives sa Health Centers, itutuloy na"));

        videoItemAdapter.notifyDataSetChanged();*/

        // VideoRecommendation/$url/video_url : str, video_description : str, date_posted : str, categories : menstruation,family planning

    }

    @Override
    protected void onStop() {
        super.onStop();
        userInteraction.insertInteraction(points, category);
        databaseReference.removeEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.ytPlayerVideoItem.removeYouTubePlayerListener(listener);
        binding.ytPlayerVideoItem.release();
    }

    private void invokeRecyclerViewAdapter() {
        videoItemAdapter = new VideoItemAdapter(modelArrayList, getApplicationContext(), player);
        videoItemAdapter.setContentDescriptionData(title, description);
        binding.recyclerViewVideoItem.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerViewVideoItem.setAdapter(videoItemAdapter);
        binding.recyclerViewVideoItem.setHasFixedSize(true);

    }

    private void loadTheVideo() {

        // [0] = playing
        // [1] = ended
        // [2] = seek
        boolean[] flags = { true, true, true };

        listener = new AbstractYouTubePlayerListener() {

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);

                switch (state) {
                    case PLAYING:
                        if(flags[0]) {
                            points += 3;
                        }
                        flags[0] = false;
                        break;
                    case ENDED:
                        if (flags[1]) {
                            points += 10;
                        }
                        flags[1] = false;
                }
            }

            @Override
            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
                if (Math.abs(second - previousTime) > 1f) {
                    if (flags[2]) {
                        points += 5;
                    }
                    flags[2] = false;
                }
                previousTime = second;
            }

            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // using pre-made custom ui
                youTubePlayer.loadVideo(url, 0f);
                VideoItemActivity.this.player = youTubePlayer;
                DefaultPlayerUiController defaultPlayerUiController = new DefaultPlayerUiController(binding.ytPlayerVideoItem, youTubePlayer);
                binding.ytPlayerVideoItem.setCustomPlayerUi(defaultPlayerUiController.getRootView());
                defaultPlayerUiController.showVideoTitle(false);
                binding.progressBarVideoItem.setVisibility(View.GONE);
                invokeRecyclerViewAdapter();
                populateRecyclerView();
            }
        };

        binding.ytPlayerVideoItem.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setFullscreen(true);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setFullscreen(false);
            }
        });

// disable iframe ui
        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).ccLoadPolicy(0).ivLoadPolicy(1).build();
        binding.ytPlayerVideoItem.initialize(listener, options);
    }

    private void setFullscreen(boolean fullscreen) {
        if (fullscreen) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

}