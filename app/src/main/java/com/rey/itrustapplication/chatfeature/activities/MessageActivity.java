package com.rey.itrustapplication.chatfeature.activities;

import static com.rey.itrustapplication.helperclasses.UtilityClass.BASE_URL;
import static com.rey.itrustapplication.helperclasses.UtilityClass.CONTENT_TYPE;
import static com.rey.itrustapplication.helperclasses.UtilityClass.SERVER_KEY;
import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOnline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adapter.MessagesAdapter;
import com.rey.itrustapplication.databinding.ActivityMessageBinding;
import com.rey.itrustapplication.helperclasses.MessagesModel;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    ActivityMessageBinding binding;
    private String fullName, chatRoomId, senderName;

    private MessagesAdapter messagesAdapter;

    private ArrayList<MessagesModel> messagesModelArrayList;

    private String __path, __username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messagesModelArrayList = new ArrayList<>();

        final SessionManager sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.getLogin()) {
            __path = "RegularUsers";
            __username = sessionManager.getFullName();
        } else {
            __path = "Admin";
            __username = new AdminSessionManager(getApplicationContext()).getUsernameSession();
        }

        senderName = getIntent().getStringExtra("senderName");
        fullName = getIntent().getStringExtra("fullName");
        String selectedProfile = getIntent().getStringExtra("selected_profile");

//        updateUserStatus();

        messagesAdapter = new MessagesAdapter(getApplicationContext(), messagesModelArrayList, selectedProfile);
        binding.recyclerViewMessage.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerViewMessage.setItemViewCacheSize(10);
        binding.recyclerViewMessage.setHasFixedSize(true);
        binding.recyclerViewMessage.setNestedScrollingEnabled(true);
        binding.recyclerViewMessage.setAdapter(messagesAdapter);
        binding.recyclerViewMessage.setItemAnimator(new DefaultItemAnimator());

        binding.userFullNameMessage.setText(fullName);

        binding.backButtonMessageActivity.setOnClickListener(click -> {
            startActivity(new Intent(MessageActivity.this, AdminListOfUsers.class));
            finish();
        });

        binding.sendMessageFab.setOnClickListener(click -> requestToNotifyUser());

        if (senderName.equals("Barangay Alawihao RHU")) {
            chatRoomId = "Admin" + fullName;
        } else {
            chatRoomId = "Admin" + senderName;
        }

        attachMessageListener(chatRoomId);

        if (new SessionManager(getApplicationContext()).getLogin()) {
            updateUserStatus("Admin");
        } else {
            updateUserStatus("RegularUsers");
        }

        binding.chatInfoButton.setOnClickListener(view ->
                startActivity(new Intent(MessageActivity.this, ChatFeatureUserInfo.class)
                        .putExtra("chatRoomId", chatRoomId)
                        .putExtra("chattingTo", fullName) ));

    }

    private void updateUserStatus(String path) {

        final String _name = fullName.equals("Barangay Alawihao RHU") ? "Midwife" : fullName;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                .getReference(path + "/" + _name);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("availability")) {
                            if ("online".equalsIgnoreCase(snapshot.child("availability").getValue(String.class))) {
                                binding.activeStatus.setText(getText(R.string.online));
                                binding.activeStatus.setBackgroundColor(getResources().getColor(R.color.successful_color));
                                binding.activeStatus.setTextColor(getResources().getColor(R.color.white));
                                return;
                            }
                            binding.activeStatus.setText(getText(R.string.offline));
                            binding.activeStatus.setBackgroundColor(getResources().getColor(R.color.dark_color));
                            binding.activeStatus.setTextColor(getResources().getColor(R.color.white));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void requestToNotifyUser() {

        final String _name = fullName.equals("Barangay Alawihao RHU") ? "Midwife" : fullName;

        if (!new SessionManager(this).getLogin()){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                    .getReference("RegularUsers").child(_name);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final String token = snapshot.child("token").getValue(String.class);
                    Log.d("FCM", "Token request: " + token);
                    formatRequest(token);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                    .getReference("Admin").child(_name);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final String token = snapshot.child("token").getValue(String.class);
                    Log.d("FCM", "Token request: " + token);
                    formatRequest(token);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Data Cancelled.", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void formatRequest(String token) {
        JSONObject to = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("title", fullName);
            data.put("message", binding.messageContent.getText().toString());
            data.put("senderName", senderName);

            to.put("to",token);
            to.put("data", data);

            sendNotification(to);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(JSONObject to) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                response -> Toast.makeText(this, "Message was sent", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "This user is not logged in yet", Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "key=" + SERVER_KEY);
                header.put("Content-Type", CONTENT_TYPE);
                return header;
            }

            @Override
            public byte[] getBody() {
                return to.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return CONTENT_TYPE;
            }
        };

        SingletonRequest.getInstance(this).getRequestQueue().add(stringRequest);

//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.POST,
//                BASE_URL,
//                to,
//                response -> Log.d("FCM", "send notification: " + response),
//                error -> Toast.makeText(getApplicationContext(), "Can't send a message " + error.getMessage(), Toast.LENGTH_SHORT).show()){
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> header = new HashMap<>();
//                header.put("Authorization", "key="+SERVER_KEY);
//                header.put("Content-Type", CONTENT_TYPE);
//                return header;
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return CONTENT_TYPE;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        request.setRetryPolicy(new DefaultRetryPolicy(30000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(request);

        DatabaseReference data = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                .getReference("messages/" + chatRoomId);
        data.push().setValue(new MessagesModel(senderName, fullName, binding.messageContent.getText().toString()))
                        .addOnSuccessListener(success -> {
                            binding.messageContent.setText("");
                            messagesModelArrayList.add(new MessagesModel(senderName, fullName, binding.messageContent.getText().toString()));
                            messagesAdapter.notifyItemInserted(messagesModelArrayList.size()-1);

                        }).addOnFailureListener(failed -> Toast.makeText(this, "Can't send a message because: " + failed.getMessage(), Toast.LENGTH_SHORT).show());

//        updateLastMessageNode();

    }

    private void attachMessageListener(String chatRoomId) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                .getReference("messages/" + chatRoomId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    messagesModelArrayList.add(dataSnapshot.getValue(MessagesModel.class));
                }
                messagesAdapter.notifyItemInserted(messagesModelArrayList.size()-1);
                binding.recyclerViewMessage.scrollToPosition(messagesModelArrayList.size()-1);
                binding.recyclerViewMessage.setVisibility(View.VISIBLE);
                binding.progressBarMessage.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database Error, check your internet or restart the app", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MessageActivity.this, AdminListOfUsers.class));
        finish();
    }

    @Override
    public void onResume() {
        setAvailabilityToOnline(__username, __path);
        super.onResume();
    }

}