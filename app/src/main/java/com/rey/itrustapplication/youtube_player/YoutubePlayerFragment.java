package com.rey.itrustapplication.youtube_player;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.FragmentYoutubePlayerBinding;
import com.rey.itrustapplication.youtube_player.database.UserInteraction;
import com.rey.itrustapplication.youtube_player.models.UserInteractionModel;

import java.util.ArrayList;
import java.util.List;

public class YoutubePlayerFragment extends Fragment {

    private ArrayList<VideoModel> videoModels = new ArrayList<>();

    private final FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance(firebaseInstance);
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();

    private YoutubePlayerAdapter youtubePlayerAdapter;

    private UserInteraction userInteraction;

    private ValueEventListener valueEventListener;

    private FragmentYoutubePlayerBinding binding;

    private final ArrayList<String> categoryList = new ArrayList<>();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("date_source", videoModels);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            videoModels = savedInstanceState.getParcelableArrayList("data_source");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentYoutubePlayerBinding.inflate(inflater, container, false);

        userInteraction = new UserInteraction(requireContext());

        categoryList.add("Menstruation and menstrual disorders");
        categoryList.add("Menopause");
        categoryList.add("Contraception");
        categoryList.add("Fertility and infertility");
        categoryList.add("Postpartum Care");
        categoryList.add("Labor and Delivery");
        categoryList.add("Sexual health and sexually transmitted infections");
        categoryList.add("Reproductive Rights and Advocacy");
        categoryList.add("Gynecological Health");
        categoryList.add("Family Planning");
        categoryList.add("Nutrition and Diet during Pregnancy");
        categoryList.add("Exercise and Physical Activity during Pregnancy");

        Menu menu = binding.videoToolbar.getMenu();
        MenuItem menuItem1 = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) menuItem1.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.recyclerviewYoutubePlayerFragment.setLayoutManager(new LinearLayoutManager(requireContext()));
        youtubePlayerAdapter = new YoutubePlayerAdapter(videoModels);
        binding.recyclerviewYoutubePlayerFragment.setAdapter(youtubePlayerAdapter);
        if (videoModels.isEmpty()) {
            populateRecyclerView();
        }
        return binding.getRoot();

    }

    private void implementListener() {

        valueEventListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("YT", "onDataChange: outer exist");

                    //$yt_link
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        //VideoRecommendation/$category/$yt_link/...
                        if (dataSnapshot.exists()) {

                            Log.d("YT", "onDataChange: inner exist");

                            //videoUrl, videoTitle, videoDescription, videoDatePosted, videoCategory
                            final String videoUrl = dataSnapshot.child("videoUrl").getValue(String.class);

                            final String videoTitle = dataSnapshot.child("videoTitle").getValue(String.class);
                            final String videoDescription = dataSnapshot.child("videoDescription").getValue(String.class);
                            final String videoDatePosted = dataSnapshot.child("videoDatePosted").getValue(String.class);
                            final String videoCategory = dataSnapshot.child("videoCategory").getValue(String.class);

                            videoModels.add(new VideoModel(videoUrl, videoTitle, videoDescription, videoDatePosted, videoCategory));
                            requireActivity().runOnUiThread(() -> youtubePlayerAdapter.notifyItemInserted(videoModels.size()-1));
                        } else {
                            if (binding.noVideoFoundTvYtFragment.getVisibility() == View.GONE)
                                binding.noVideoFoundTvYtFragment.setVisibility(View.VISIBLE);
                            if (!videoModels.isEmpty()) {
                                videoModels.clear();
                                youtubePlayerAdapter.notifyDataSetChanged();
                            }
                        }

                    }

                    databaseReference.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error because, " +error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void populateRecyclerView() {

        List<UserInteractionModel> databaseData = userInteraction.getAllUserInteractions();


        if (!databaseData.isEmpty()) {
            implementListener();
            Log.d("YT", "onDataChange: database data is not empty");

            for (UserInteractionModel model : databaseData) {

                String categoryPreferred = model.getCategory();

                categoryList.remove(categoryPreferred);

                databaseReference.child("VideoRecommendation").child(categoryPreferred).addListenerForSingleValueEvent(valueEventListener);
            }

            for (String categoryItem : categoryList) {
                databaseReference.child("VideoRecommendation").child(categoryItem).addListenerForSingleValueEvent(valueEventListener);
            }

            return;
        }

        Log.d("YT", "onDataChange: database data is empty");

        int currentSize = videoModels.size();

        databaseReference.child("VideoRecommendation").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("YT", "onDataChange: IF exist 1");
                    //categories
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        //$yt_link
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            //VideoRecommendation/$category/$yt_link/...
                            if (dataSnapshot1.exists()) {
                                //videoUrl, videoTitle, videoDescription, videoDatePosted, videoCategory
                                final String videoUrl = dataSnapshot1.child("videoUrl").getValue(String.class);
                                final String videoTitle = dataSnapshot1.child("videoTitle").getValue(String.class);
                                final String videoDescription = dataSnapshot1.child("videoDescription").getValue(String.class);
                                final String videoDatePosted = dataSnapshot1.child("videoDatePosted").getValue(String.class);
                                final String videoCategory = dataSnapshot1.child("videoCategory").getValue(String.class);

                                Log.d("YT", "onDataChange: IF exist 2 : " + videoUrl);
                                videoModels.add(new VideoModel(videoUrl, videoTitle, videoDescription, videoDatePosted, videoCategory));
                                youtubePlayerAdapter.notifyItemInserted(videoModels.size()-1);
                            }
                        }

                    }

                    if (currentSize != videoModels.size()) {
                        Log.d("YT", "onDataChange: notify adapter: " + currentSize + " : " + videoModels.size());
//                        youtubePlayerAdapter.notifyItemRangeInserted(currentSize, videoModels.size());
                    }
                } else {
                    binding.noVideoFoundTvYtFragment.setVisibility(View.VISIBLE);
                    Log.d("YT", "onDataChange: doesn't exist");
                    if (!videoModels.isEmpty()) {
                        videoModels.clear();
                        youtubePlayerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStop() {
        if (valueEventListener != null)
            databaseReference.removeEventListener(valueEventListener);
        Log.d("YT", "onStop: STOP");
        super.onStop();
    }
}