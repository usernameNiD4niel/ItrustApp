package com.rey.itrustapplication;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.databinding.ActivityAdminVideoRecommendationBinding;
import com.rey.itrustapplication.youtube_player.VideoModel;
import com.rey.itrustapplication.youtube_player.YoutubePlayerDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AdminVideoRecommendation extends AdminDrawerBaseActivity {

    ActivityAdminVideoRecommendationBinding binding;
    private final String[] categories = {
            "Menstruation and menstrual disorders",
            "Menopause",
            "Contraception",
            "Fertility and infertility",
            "Postpartum Care",
            "Labor and Delivery",
            "Sexual health and sexually transmitted infections",
            "Reproductive Rights and Advocacy",
            "Gynecological Health",
            "Family Planning",
            "Nutrition and Diet during Pregnancy",
            "Exercise and Physical Activity during Pregnancy"
    };

    private String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminVideoRecommendationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Video Recommendation");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.signup_gender_list, categories);
        binding.videoCategoriesRecommendationText.setAdapter(arrayAdapter);

        binding.videoDatePostedRecommendationText.setText(getCurrentDate());

        binding.recommendVideoButton.setOnClickListener(view -> validateUserInput());

        binding.videoCategoriesRecommendationText.setOnItemClickListener((parent, view, position, id) ->
                selectedCategory = parent.getItemAtPosition(position).toString());
    }

    private String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("en","PH"));
        Calendar calendar = Calendar.getInstance();

        return simpleDateFormat.format(calendar.getTime());

    }

    private void resetInputs() {
        binding.descriptionVideoText.setText("");
        binding.ytLinkVideoRecommendationText.setText("");
        binding.videoTitleRecommendationText.setText("");
        binding.videoTitleRecommendationText.requestFocus();
    }

    private void validateUserInput() {

        final String captionText = Objects.requireNonNull(binding.descriptionVideoText.getText()).toString();
        final String titleText = Objects.requireNonNull(binding.videoTitleRecommendationText.getText()).toString();
        String ytLinkText = Objects.requireNonNull(binding.ytLinkVideoRecommendationText.getText()).toString();

        if (ytLinkText.isEmpty()) {
            binding.ytLinkVideoRecommendation.setError("This field cannot be empty");
            binding.ytLinkVideoRecommendationText.requestFocus();
            return;
        }

        if (captionText.isEmpty()) {
            binding.descriptionVideoRecommendation.setError("This field cannot be empty");
            binding.descriptionVideoText.requestFocus();
            return;
        }

        if (ytLinkText.length() < 27) {
            binding.ytLinkVideoRecommendation.setError("Please enter a valid Youtube Link");
            binding.ytLinkVideoRecommendationText.requestFocus();
            return;
        }

        if (titleText.isEmpty()) {
            binding.videoTitleRecommendation.setError("Video title cannot be empty");
            binding.videoTitleRecommendationText.requestFocus();
            return;
        }

        if (selectedCategory.isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.ytLinkVideoRecommendation.isErrorEnabled()) {
            binding.ytLinkVideoRecommendation.setErrorEnabled(false);
            binding.ytLinkVideoRecommendation.setError(null);
        }

        if (binding.videoTitleRecommendation.isErrorEnabled()) {
            binding.videoTitleRecommendation.setErrorEnabled(false);
            binding.videoTitleRecommendation.setError(null);
        }

        if (binding.descriptionVideoRecommendation.isErrorEnabled()) {
            binding.descriptionVideoRecommendation.setErrorEnabled(false);
            binding.descriptionVideoRecommendation.setError(null);
        }

        ytLinkText = ytLinkText.trim().substring(ytLinkText.length()-11);

        isAlreadyExistVideo(ytLinkText, captionText, titleText);

    }

    private void isAlreadyExistVideo(String ytLinkText, String captionText, String titleText) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("VideoRecommendation/" + ytLinkText);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.ytLinkVideoRecommendation.setError("This video is already added to the Video Recommendation");
                    binding.ytLinkVideoRecommendationText.requestFocus();
                } else {
                    if (binding.ytLinkVideoRecommendation.isErrorEnabled()) {
                        binding.ytLinkVideoRecommendation.setErrorEnabled(false);
                        binding.ytLinkVideoRecommendation.setError(null);

                    }
                    pushToDatabase(ytLinkText, captionText, getCurrentDate(), titleText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminVideoRecommendation.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void pushToDatabase(String videoUrl, String videoCaption, String videoDatePosted, String titleText) {

        VideoModel videoModel = new VideoModel(videoUrl, titleText,videoCaption, videoDatePosted, selectedCategory);

        Toast.makeText(this, "Pushing data: " + videoUrl, Toast.LENGTH_SHORT).show();

        YoutubePlayerDatabase youtubePlayerDatabase = new YoutubePlayerDatabase(this);
        youtubePlayerDatabase.addVideoData(videoModel);
        resetInputs();
    }

}