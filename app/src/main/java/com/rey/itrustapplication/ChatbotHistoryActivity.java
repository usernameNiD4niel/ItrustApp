package com.rey.itrustapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.adapter.AccessibleUserAdapter;
import com.rey.itrustapplication.databinding.ActivityChatbotHistoryBinding;
import com.rey.itrustapplication.helperclasses.AccessibleUserHelperClass;

import java.util.ArrayList;
import java.util.List;

public class ChatbotHistoryActivity extends AdminDrawerBaseActivity {

    private List<AccessibleUserHelperClass> accessibleUserHelperClassList;
    private AccessibleUserAdapter adapter;

    private ActivityChatbotHistoryBinding binding;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatbotHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        allocateActivityTitle("Accessible User");

        addDataToRecyclerView();

        
        binding.addUser.setOnClickListener(view -> startActivity(new Intent(ChatbotHistoryActivity.this, AddAccessibleUser.class)));

        binding.searchView.clearFocus();
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        Handler handler = new Handler();

        handler.postDelayed(() -> {

            binding.recyclerViewAccessibleUsers.setHasFixedSize(true);
            binding.recyclerViewAccessibleUsers.setLayoutManager(new LinearLayoutManager(ChatbotHistoryActivity.this));

            adapter = new AccessibleUserAdapter(ChatbotHistoryActivity.this, accessibleUserHelperClassList);
            binding.recyclerViewAccessibleUsers.setAdapter(adapter);
            adapter.notifyItemChanged(adapter.getItemCount()+1);

        }, 1000);

    }

    private void addDataToRecyclerView() {

        binding.progressBarAddUserRecycler.setVisibility(View.VISIBLE);

        accessibleUserHelperClassList = new ArrayList<>();

        final DatabaseReference databaseReference = firebaseDatabase.getReference("AccessibleUsers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    binding.noDataFoundAddUserRecycler.setVisibility(View.GONE);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        final String dateAdded = dataSnapshot.child("dateAdded").getValue(String.class);

                        accessibleUserHelperClassList.add(new AccessibleUserHelperClass(fullName, dateAdded));

                        if (adapter != null) {
                            adapter.notifyItemInserted(accessibleUserHelperClassList.size()-1);
                        }
                    }

                    if (adapter == null) {
                        binding.recyclerViewAccessibleUsers.setNestedScrollingEnabled(false);
                        binding.recyclerViewAccessibleUsers.setItemViewCacheSize(20);
                        binding.recyclerViewAccessibleUsers.setHasFixedSize(true);
                        binding.recyclerViewAccessibleUsers.setLayoutManager(new LinearLayoutManager(ChatbotHistoryActivity.this));

                        adapter = new AccessibleUserAdapter(ChatbotHistoryActivity.this, accessibleUserHelperClassList);
                        binding.recyclerViewAccessibleUsers.setAdapter(adapter);
                        binding.recyclerViewAccessibleUsers.setVisibility(View.VISIBLE);
                        binding.progressBarAddUserRecycler.setVisibility(View.GONE);
                    }
                } else {
                    binding.progressBarAddUserRecycler.setVisibility(View.GONE);
                    binding.noDataFoundAddUserRecycler.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList(String text) {
        List<AccessibleUserHelperClass> filteredList = new ArrayList<>();

        for (AccessibleUserHelperClass newList : accessibleUserHelperClassList) {
            if (newList.getFullName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(newList);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }
    }


}