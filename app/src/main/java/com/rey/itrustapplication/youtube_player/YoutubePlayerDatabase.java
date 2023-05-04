package com.rey.itrustapplication.youtube_player;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YoutubePlayerDatabase {
    private final Context context;
    private final FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance(firebaseInstance);
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();

    public YoutubePlayerDatabase(Context context) {
        this.context = context;
    }

    public void addVideoData(VideoModel videoModel) {

        databaseReference.child("VideoRecommendation/" + videoModel.getVideoCategory()).child(videoModel.getVideoUrl()).setValue(videoModel)
                .addOnSuccessListener(aVoid -> appendToActivityLogs())
                .addOnFailureListener(failure -> Toast.makeText(context, "Failed: " + failure, Toast.LENGTH_SHORT).show());

    }

    private void appendToActivityLogs() {

        final String currentUser = new AdminSessionManager(context).getUsernameSession();
        final String logMessage = currentUser + " added new video recommendation";

        Calendar calendar = Calendar.getInstance();
        final String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        Date dateDate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh-mm-ss", Locale.getDefault());
        final String time = simpleDateFormat.format(dateDate);

        Map<String, Object> adminDataLog = new HashMap<>();
        adminDataLog.put("title", logMessage);
        adminDataLog.put("date", date);
        adminDataLog.put("time", time);

        databaseReference.child("ActivityLogs").push().setValue(adminDataLog)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Successfully added new video", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(failure -> Toast.makeText(context, "Sucessfully added video but cannot add to log", Toast.LENGTH_SHORT).show());



    }


}
