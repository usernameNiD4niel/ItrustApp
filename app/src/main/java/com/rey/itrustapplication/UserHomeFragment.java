package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;
import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOffline;
import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOnline;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.adapter.VideosAdapter;
import com.rey.itrustapplication.helperclasses.VideoItem;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {

    private List<VideoItem> videoItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ViewPager2 videosPager = view.findViewById(R.id.videosViewPager);
        final ProgressBar progressBarUserHome = view.findViewById(R.id.progressBarUserHome);

        videoItemList = new ArrayList<>();

        populateList(videosPager, progressBarUserHome);
    }

    private void populateList(ViewPager2 viewPager2, ProgressBar progressBar) {
        videoItemList.clear();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("VideoRecommendation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.hasChild("category")) {
                        final String category = dataSnapshot.child("category").getValue(String.class);
                        if (category == null) return;

                        Log.d("Daniel", "onDataChange: category: " + category);

                        databaseReference.child(category).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    if (dataSnapshot1.hasChild("caption")) {
                                        final String videoDatePosted = dataSnapshot1.child("videoDatePosted").getValue(String.class);
                                        final String videoCaption = dataSnapshot1.child("videoCaption").getValue(String.class);
                                        final String videoUrl = dataSnapshot1.child("videoUrl").getValue(String.class);

                                        VideoItem videoItem = new VideoItem();
                                        videoItem.videoURL = videoUrl;
                                        videoItem.videoCaption = videoCaption;
                                        videoItem.videoDatePosted = videoDatePosted;

                                        videoItemList.add(videoItem);

                                        Log.d("Daniel", "onDataChange: video item list size: " + videoItemList.size());

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                new Handler().postDelayed(() -> {
                    viewPager2.setAdapter(new VideosAdapter(getActivity(), videoItemList, getLifecycle()));
                    viewPager2.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }, 700);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        VideosAdapter.VideoViewHolder.releaseVideoPlayer();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        setAvailabilityToOnline(new SessionManager(requireActivity()).getFullName(), "RegularUsers");
        super.onResume();
    }

    @Override
    public void onStop() {
        setAvailabilityToOffline(new SessionManager(requireActivity()).getFullName(), "RegularUsers");
        super.onStop();
    }
}