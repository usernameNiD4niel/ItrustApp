package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.BASE_URL;
import static com.rey.itrustapplication.helperclasses.UtilityClass.CONTENT_TYPE;
import static com.rey.itrustapplication.helperclasses.UtilityClass.SERVER_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.rey.itrustapplication.databinding.ActivityUserConsultBinding;
import com.rey.itrustapplication.sessionmanager.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserConsult extends AppCompatActivity {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");

    private ActivityUserConsultBinding binding;

    private final LinkedList<String> availableSchedule = new LinkedList<>();

    private String selectedSchedule = "";
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserConsultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(view -> finish());

        SessionManager sessionManager = new SessionManager(this);

        currentUser = sessionManager.getFullName();

        makeRequestToAdmin();

        populateDropDown();

        binding.availableScheduleConsult.setOnItemClickListener((parent, view, position, id) -> selectedSchedule = parent.getItemAtPosition(position).toString());

        binding.requestUserConsultation.setOnClickListener(view -> createAPendingRequest());
    }

    private void shouldLoad(boolean isLoad) {
        if (isLoad) {
            binding.progressbarUserConsult.setVisibility(View.VISIBLE);
            binding.requestUserConsultation.setVisibility(View.INVISIBLE);
            return;
        }

        binding.progressbarUserConsult.setVisibility(View.GONE);
        binding.requestUserConsultation.setVisibility(View.VISIBLE);
    }

    private void createAPendingRequest() {
        // PendingRequest/name,date_requested

        shouldLoad(true);

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        Map<String, Object> pendingRequestObject = new HashMap<>();
        pendingRequestObject.put("fullName", currentUser);
        pendingRequestObject.put("schedule", selectedSchedule);
        pendingRequestObject.put("date_requested", currentDate);

        databaseReference.child("PendingRequest").push().setValue(pendingRequestObject).addOnSuccessListener(complete ->
                databaseReference.child("RegularUsers/" + currentUser + "/status").setValue("Pending Request").addOnSuccessListener(complete1 -> databaseReference.child("Admin/Midwife/token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            final String token = snapshot.getValue(String.class);
                            formatRequest(token);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                })).addOnFailureListener(failed2 -> {
                    Toast.makeText(this, failed2.getMessage(), Toast.LENGTH_SHORT).show();
                    shouldLoad(false);

        })).addOnFailureListener(failed -> {
            Toast.makeText(this, failed.getMessage(), Toast.LENGTH_SHORT).show();
            shouldLoad(false);
        });
    }

    private void formatRequest(String token) {
        JSONObject data = new JSONObject();
        JSONObject to = new JSONObject();
        try {
            data.put("schedule_sender", "consultation schedule");
            data.put("message", "Hey comeback, " + currentUser + " was requesting ");
            data.put("senderName", "Consultation Schedule");

            to.put("to", token);
            to.put("data", data);

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, to.toString());

            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(body)
                    .addHeader("Authorization", "key=" + SERVER_KEY)
                    .addHeader("Content-Type", CONTENT_TYPE)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.makeText(UserConsult.this, "Error occur because, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    shouldLoad(false);
                    binding.requestUserConsultation.setEnabled(false);
                    Toast.makeText(UserConsult.this, "The request has send to the barangay admin", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateDropDown() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("Admin/consultation_schedule");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.hasChild("monday")) {
                        final String monday = snapshot.child("monday").getValue(String.class);
                        availableSchedule.add("Monday | "+monday);
                    }

                    if (snapshot.hasChild("tuesday")) {
                        final String tuesday = snapshot.child("tuesday").getValue(String.class);
                        availableSchedule.add("Tuesday | "+tuesday);
                    }

                    if (snapshot.hasChild("wednesday")) {
                        final String wednesday = snapshot.child("wednesday").getValue(String.class);
                        availableSchedule.add("Wednesday | "+wednesday);
                    }

                    if (snapshot.hasChild("thursday")) {
                        final String thursday = snapshot.child("thursday").getValue(String.class);
                        availableSchedule.add("Thursday | "+thursday);
                    }

                    if (snapshot.hasChild("friday")) {
                        final String friday = snapshot.child("friday").getValue(String.class);
                        availableSchedule.add("Friday | "+friday);
                    }

                    if (snapshot.hasChild("saturday")) {
                        final String saturday = snapshot.child("saturday").getValue(String.class);
                        availableSchedule.add("Saturday | "+saturday);
                    }

                    if (snapshot.hasChild("sunday")) {
                        final String sunday = snapshot.child("sunday").getValue(String.class);
                        availableSchedule.add("Sunday | "+sunday);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.signup_gender_list, availableSchedule);
                    binding.availableScheduleConsult.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserConsult.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeRequestToAdmin() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("RegularUsers");

        databaseReference.child(currentUser + "/status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String status = snapshot.getValue(String.class);
                    binding.requestUserConsultation.setText(status);
                    binding.requestUserConsultation.setEnabled(false);
                } else {
                    if (!binding.requestUserConsultation.isEnabled()) {
                        binding.requestUserConsultation.setEnabled(true);
                        binding.requestUserConsultation.setText(getResources().getString(R.string.request));
                    }
                }
                binding.requestUserConsultation.setVisibility(View.VISIBLE);
                binding.progressbarUserConsult.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}