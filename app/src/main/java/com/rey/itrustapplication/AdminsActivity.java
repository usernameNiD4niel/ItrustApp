package com.rey.itrustapplication;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.databinding.ActivityAdminsBinding;

import java.util.ArrayList;

public class AdminsActivity extends AdminDrawerBaseActivity {

    ArrayList<AdminAdminsModel> adminAdminsModels = new ArrayList<>();
    private AARecyclerViewAdapter adapter;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
    private ActivityAdminsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Admins");

        adminAdminsModels = new ArrayList<>();

        adapter = new AARecyclerViewAdapter(this, adminAdminsModels);
        binding.recyclerViewAdmins.setAdapter(adapter);
        binding.recyclerViewAdmins.setHasFixedSize(true);
        binding.recyclerViewAdmins.setLayoutManager(new LinearLayoutManager(AdminsActivity.this));

        setUpChatbotHistoryModels();

        binding.myProfileBtn.setOnClickListener(view ->
            startActivity(new Intent(AdminsActivity.this, AdminsMyProfile.class)));

    }

    @Override
    public void onResume() {
        if (adminAdminsModels.size() > 0) {
            adminAdminsModels.clear();
        }
        super.onResume();
    }

    private void setUpChatbotHistoryModels() {

        final DatabaseReference databaseReference = firebaseDatabase.getReference("Admin");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        if (dataSnapshot.hasChild("fullName")) {

                            final String fullName = dataSnapshot.child("fullName").getValue(String.class);
                            final String username = dataSnapshot.child("username").getValue(String.class);
                            final String accountCreated = dataSnapshot.child("accountCreated").getValue(String.class);

                            adminAdminsModels.add(new AdminAdminsModel(fullName, username, accountCreated, R.drawable.adminavatar));

                        }

                    }

                    adapter.notifyItemInserted(adminAdminsModels.size()-1);
                    Toast.makeText(AdminsActivity.this, "Number : " + adminAdminsModels.size(), Toast.LENGTH_SHORT).show();
                    binding.progressBarAdminAdmins.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}