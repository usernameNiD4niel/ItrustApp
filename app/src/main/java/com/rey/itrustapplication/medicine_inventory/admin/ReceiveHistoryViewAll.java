package com.rey.itrustapplication.medicine_inventory.admin;

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
import com.rey.itrustapplication.databinding.ActivityReceiveHistoryViewAllBinding;
import com.rey.itrustapplication.medicine_inventory.admin.history_view_all.HistoryViewModel;
import com.rey.itrustapplication.medicine_inventory.admin.history_view_all.ReceivedHistoryAdapter;

import java.util.ArrayList;

public class ReceiveHistoryViewAll extends AppCompatActivity {

    ActivityReceiveHistoryViewAllBinding binding;
    private ArrayList<HistoryViewModel> historyViewModels;
    private ReceivedHistoryAdapter receivedHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiveHistoryViewAllBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        historyViewModels = new ArrayList<>();

        binding.backButtonReceiveHistoryViewAll.setOnClickListener(view -> {
            startActivity(new Intent(ReceiveHistoryViewAll.this, InventoryMedicine.class));
            finish();
        });

        receivedHistoryAdapter = new ReceivedHistoryAdapter(historyViewModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        binding.recyclerViewReceiveHistory.setItemViewCacheSize(20);
        binding.recyclerViewReceiveHistory.setHasFixedSize(true);
        binding.recyclerViewReceiveHistory.setAdapter(receivedHistoryAdapter);
        binding.recyclerViewReceiveHistory.setLayoutManager(linearLayoutManager);

        populateRecyclerView();
    }

    private void populateRecyclerView() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ClaimedMedicines");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (binding.noDataFoundReceiveHistory.getVisibility() == View.VISIBLE)
                        binding.noDataFoundReceiveHistory.setVisibility(View.GONE);

                    if (binding.progressContainerReceiveHistory.getVisibility() == View.VISIBLE)
                        binding.progressContainerReceiveHistory.setVisibility(View.GONE);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        final String medicine_name = dataSnapshot.child("medicine_name").getValue(String.class);
                        final String patient_name = dataSnapshot.child("patient_name").getValue(String.class);
                        final String date_received = dataSnapshot.child("date_received").getValue(String.class);
                        final String quantity = dataSnapshot.child("quantity").getValue(String.class);
                        final String reason = dataSnapshot.child("reason").getValue(String.class);

                        runOnUiThread(() -> {
                            historyViewModels.add(new HistoryViewModel(patient_name, medicine_name, quantity, reason, date_received));
                            receivedHistoryAdapter.notifyItemRangeChanged(historyViewModels.size() - 1, 1);
                        });

                    }

                } else {
                    binding.noDataFoundReceiveHistory.setVisibility(View.VISIBLE);
                    binding.progressContainerReceiveHistory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}