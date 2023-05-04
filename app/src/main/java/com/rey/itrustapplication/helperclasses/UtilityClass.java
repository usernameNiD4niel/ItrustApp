package com.rey.itrustapplication.helperclasses;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class UtilityClass {

    public static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String SERVER_KEY = "AAAA2bbUbJw:APA91bF1Dpfncv40gTbibl1fCcGWdfu0gZ_3o8w-znypkkTnw4AAN2Ny8QnoN2yeOgaiSpiDPO3djIslhqCShRrOEwQ-gzCeNyWHN26GPUnzi6q3PSNGsELtsnMdTMBZ-XSGnQsNuM7S";
    public static final String CONTENT_TYPE = "application/json";

    public static final String CHANNEL_ID = "channel_id";

    //Remote Config Keys
    public static final String VERSION = "version";
    public static final String VERSION_NAME = "version_name";
    public static final String UPDATE_TITLE = "title";
    public static final String WHATS_NEW = "whats_new";
    public static final String APK_NAME = "apk_name_with_extension";
    public static final String APK_URL = "url";

    public static final String firebaseInstance = "https://fir-thesis-15f90-default-rtdb.firebaseio.com/";

    public static void setAvailabilityToOffline(String currentUser, String path) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(firebaseInstance);
        DatabaseReference databaseReference = firebaseDatabase.getReference(path).child(currentUser);

        Map<String, Object> map = new HashMap<>();
        map.put("availability", "offline");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void setAvailabilityToOnline(String currentUser, String path) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(firebaseInstance);
        DatabaseReference databaseReference = firebaseDatabase.getReference(path).child(currentUser);

        Map<String, Object> map = new HashMap<>();
        map.put("availability", "online");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.updateChildren(map);
                Log.d("Daniel", "online");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
