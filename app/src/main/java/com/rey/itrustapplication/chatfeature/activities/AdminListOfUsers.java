package com.rey.itrustapplication.chatfeature.activities;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;
import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOnline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adapter.AdminListOfUserAdapter;
import com.rey.itrustapplication.databinding.ActivityAdminListOfUsersBinding;
import com.rey.itrustapplication.helperclasses.ListOfUserHelperClass;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AdminListOfUsers extends AppCompatActivity {

    ActivityAdminListOfUsersBinding binding;
    private AdminListOfUserAdapter adminListOfUserAdapter;
    private ArrayList<ListOfUserHelperClass> listOfUserHelperClassArrayList;
    AdminListOfUserAdapter.OnAdminClickListener onAdminClickListener;

    private String senderName;

    private String path, __username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminListOfUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final SessionManager sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.getLogin()) {
            path = "RegularUsers";
            senderName = sessionManager.getFullName();
            __username = sessionManager.getFullName();
        } else {
            path = "Admin";
            senderName = "Barangay Alawihao RHU";
            __username = new AdminSessionManager(getApplicationContext()).getUsernameSession();
        }


        binding.backButtonAdminList.setOnClickListener(click -> onBackPressed());

        listOfUserHelperClassArrayList = new ArrayList<>();
        binding.swipeLayout.setOnRefreshListener(() -> {
            listOfUserHelperClassArrayList.clear();
            refreshData(sessionManager);
        });

        onAdminClickListener = (this::getUserProfile);

        adminListOfUserAdapter = new AdminListOfUserAdapter(listOfUserHelperClassArrayList, getApplicationContext(), onAdminClickListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerViewAdminList.setLayoutManager(layoutManager);
        binding.recyclerViewAdminList.setAdapter(adminListOfUserAdapter);
        binding.recyclerViewAdminList.setItemViewCacheSize(20);
        binding.recyclerViewAdminList.setHasFixedSize(true);
        binding.recyclerViewAdminList.setNestedScrollingEnabled(true);
        binding.recyclerViewAdminList.setItemAnimator(new DefaultItemAnimator());

        refreshData(sessionManager);

    }

    private void getUserProfile(int position) {
        binding.progressBarAdminListUsers.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Userprofile");
        final String __fullName = listOfUserHelperClassArrayList.get(position).getFullName();
        Intent messageIntent = new Intent(AdminListOfUsers.this, MessageActivity.class);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild(__fullName)) {
                    final String selectedHashProfile = snapshot.child(__fullName).getValue(String.class);
                    messageIntent.putExtra("selected_profile", selectedHashProfile);
                } else {
                    messageIntent.putExtra("selected_profile", R.drawable.user_avatar_1 + "");
                }

                messageIntent.putExtra("fullName", listOfUserHelperClassArrayList.get(position).getFullName());
                messageIntent.putExtra("senderName", senderName);
                startActivity(messageIntent);
                binding.progressBarAdminListUsers.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void noConversationMessage(String availability) {
        final String greetings = getResources().getString(R.string.greet) + " " + senderName;
        if (binding.progressBarAdminListUsers.getVisibility() == View.VISIBLE)
            binding.progressBarAdminListUsers.setVisibility(View.GONE);

        listOfUserHelperClassArrayList.add(new ListOfUserHelperClass("Barangay Alawihao RHU",greetings,availability,"user"));
        adminListOfUserAdapter.notifyDataSetChanged();
    }

//    Purpose: If may nag chat dapat iibaw yung last na nag chat
    private void refreshData(SessionManager sessionManager) {

        binding.progressBarAdminListUsers.setVisibility(View.VISIBLE);

        if (sessionManager.getLogin()) {
            setUpUserView();
        } else {
            setUpAdminView();
        }
    }

    private void setUpUserView() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("Admin/Midwife");
        final DatabaseReference messagesReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("messages");

        final String[] activeness = { "offline" };

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    activeness[0] = snapshot.child("availability").getValue(String.class);
                } else {
                    Toast.makeText(AdminListOfUsers.this, "non EXISTED", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: Connection Problem", Toast.LENGTH_SHORT).show();
            }
        });

        messagesReference.child("Admin" + senderName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    if (binding.swipeLayout.isRefreshing()) binding.swipeLayout.setRefreshing(false);
                    noConversationMessage(activeness[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagesReference.child("Admin" + senderName).limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Toast.makeText(AdminListOfUsers.this, "exists", Toast.LENGTH_SHORT).show();
                    final String uid = snapshot.getKey();
                    updateUserInfo(uid, activeness[0], "Barangay Alawihao RHU", senderName);
                    if (binding.swipeLayout.isRefreshing()) binding.swipeLayout.setRefreshing(false);

                } else {

                    Toast.makeText(AdminListOfUsers.this, "not existed", Toast.LENGTH_SHORT).show();
                    if (binding.swipeLayout.isRefreshing()) binding.swipeLayout.setRefreshing(false);
                    noConversationMessage(activeness[0]);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo(String uid, String activeness, String fullName, String query) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("messages/Admin" + query);
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String sender = snapshot.child("sender").getValue(String.class);
                    String content = snapshot.child("content").getValue(String.class);

                    if (content == null) return;

                    if (content.length() > 15)
                        content = content.substring(0, 15) + "...";

                    listOfUserHelperClassArrayList.add(new ListOfUserHelperClass(fullName,content,activeness,sender));
                    adminListOfUserAdapter.notifyDataSetChanged();
                    if (binding.swipeLayout.isRefreshing()) binding.swipeLayout.setRefreshing(false);
                    binding.progressBarAdminListUsers.setVisibility(View.GONE);

                } else {
                    noConversationMessage(activeness);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Connection Problem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpAdminView() {

        Set<String> uniqueNames = new HashSet<>();

        final DatabaseReference messagesReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("messages");

        FirebaseDatabase.getInstance(firebaseInstance).getReference("RegularUsers")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.hasChild("fullName")) {
                            final String _fullName = dataSnapshot.child("fullName").getValue(String.class);
                            final String availability = dataSnapshot.child("availability").getValue(String.class);

                            messagesReference.child("Admin" + _fullName).limitToLast(1).addChildEventListener(new ChildEventListener() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.exists()) {
                                        final String uid = snapshot.getKey();
                                        if (!uniqueNames.contains(uid)) {
                                            uniqueNames.add(uid);
                                            updateUserInfo(uid, availability, _fullName, _fullName);
                                        } else {
                                            binding.progressBarAdminListUsers.setVisibility(View.VISIBLE);
                                            listOfUserHelperClassArrayList.clear();
                                            adminListOfUserAdapter.notifyDataSetChanged();
                                            setUpAdminView();
                                        }

                                    } else {
                                        if (binding.swipeLayout.isRefreshing()) binding.swipeLayout.setRefreshing(false);
                                        noConversationMessage(availability);
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No user found.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Poor Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "You can't use the chat feature...", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        setAvailabilityToOnline(__username, path);
        super.onResume();
    }

}