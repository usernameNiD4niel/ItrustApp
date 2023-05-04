package com.rey.itrustapplication.medicine_inventory.admin.medicine_view_all;

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
import com.rey.itrustapplication.databinding.ActivityMedicineHistoryBinding;
import com.rey.itrustapplication.medicine_inventory.admin.InventoryMedicine;

import java.util.ArrayList;

public class MedicineHistory extends AppCompatActivity {

    ActivityMedicineHistoryBinding binding;
    MedicineHistoryAdapter medicineHistoryAdapter;
    ArrayList<MedicineHistoryModel> medicineHistoryModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicineHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        medicineHistoryModels = new ArrayList<>();

        binding.backButtonMedicineHistory.setOnClickListener(view -> {
            startActivity(new Intent(MedicineHistory.this, InventoryMedicine.class));
            finish();
        });

        medicineHistoryAdapter = new MedicineHistoryAdapter(medicineHistoryModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        binding.recyclerViewMedicineHistory.setItemViewCacheSize(20);
        binding.recyclerViewMedicineHistory.setHasFixedSize(true);
        binding.recyclerViewMedicineHistory.setAdapter(medicineHistoryAdapter);
        binding.recyclerViewMedicineHistory.setLayoutManager(linearLayoutManager);

        populateRecyclerView();

    }

    private void populateRecyclerView() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Medicines");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (binding.noDataFoundMedicineHistory.getVisibility() == View.VISIBLE)
                        binding.noDataFoundMedicineHistory.setVisibility(View.GONE);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String medicine_name = dataSnapshot.child("medicine_name").getValue(String.class);
                        final String medicine_stock = dataSnapshot.child("medicine_stock").getValue(String.class);
                        final String medicine_description = dataSnapshot.child("medicine_description").getValue(String.class);
                        final String how_to_use = dataSnapshot.child("how_to_use").getValue(String.class);
                        final String when_to_use = dataSnapshot.child("when_to_use").getValue(String.class);

                        runOnUiThread(() -> {
                            medicineHistoryModels.add(new MedicineHistoryModel(medicine_name, medicine_stock, medicine_description, how_to_use, when_to_use));
                            medicineHistoryAdapter.notifyItemRangeChanged(medicineHistoryModels.size()-1, 1);
                        });
                    }
                } else {
                    binding.noDataFoundMedicineHistory.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}