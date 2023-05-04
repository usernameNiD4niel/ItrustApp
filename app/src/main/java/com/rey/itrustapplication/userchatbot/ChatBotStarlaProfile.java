package com.rey.itrustapplication.userchatbot;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.chatfeature.activities.AdminListOfUsers;
import com.rey.itrustapplication.databinding.ActivityChatBotStarlaProfileBinding;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.util.LinkedList;

public class ChatBotStarlaProfile extends AppCompatActivity {

    ActivityChatBotStarlaProfileBinding binding;

    private LinkedList<ChatBotStarlaProfileModel> chatBotStarlaProfileModelLinkedList;
    private ChatBotProfileAdapter chatBotProfileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBotStarlaProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButtonsUserChatBot.setOnClickListener(back -> finish());

        chatBotStarlaProfileModelLinkedList = new LinkedList<>();

        populateRecyclerView();

    }


    private void populateRecyclerView() {
        chatBotStarlaProfileModelLinkedList.clear();

        final SessionManager sessionManager = new SessionManager(this);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("RegularUsers");

        databaseReference.child(sessionManager.getFullName()).child("saved_answer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String __answer = dataSnapshot.child("answer").getValue(String.class);
                        final String __question = dataSnapshot.child("question").getValue(String.class);

                        chatBotStarlaProfileModelLinkedList.add(new ChatBotStarlaProfileModel(__answer, __question));
                    }
                }

                if (chatBotStarlaProfileModelLinkedList.size() == 0) {
                    binding.noDataFoundChatBotStarlaProfile.setVisibility(View.VISIBLE);
                    binding.progressBarChatBotStarlaProfile.setVisibility(View.GONE);
                    return;
                }

                binding.noDataFoundChatBotStarlaProfile.setVisibility(View.GONE);
                chatBotProfileAdapter = new ChatBotProfileAdapter(ChatBotStarlaProfile.this, chatBotStarlaProfileModelLinkedList);
                binding.recyclerViewChatBotProfile.setAdapter(chatBotProfileAdapter);
                binding.recyclerViewChatBotProfile.setLayoutManager(new LinearLayoutManager(ChatBotStarlaProfile.this));
                binding.recyclerViewChatBotProfile.setHasFixedSize(true);
                binding.recyclerViewChatBotProfile.setVisibility(View.VISIBLE);
                binding.progressBarChatBotStarlaProfile.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.viewDarnaMessageProfile.setOnClickListener(view -> startActivity(new Intent(ChatBotStarlaProfile.this, AdminListOfUsers.class)));
    }
}