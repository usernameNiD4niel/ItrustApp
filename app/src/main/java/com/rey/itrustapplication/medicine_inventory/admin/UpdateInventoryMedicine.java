package com.rey.itrustapplication.medicine_inventory.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.AdminDrawerBaseActivity;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.ActivityUpdateInventoryMedicineBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateInventoryMedicine extends AdminDrawerBaseActivity {

    ActivityUpdateInventoryMedicineBinding binding;
    private ArrayList<String> listOfMedicines;
    private String medicineSelected = "", medicineStockString = "", medicineDescriptionString = "", howToUseString = "", whenToUseString = "";
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Medicines");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateInventoryMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Inventory Medicine | Update");

        listOfMedicines = new ArrayList<>();

        fetchMedicines();

        binding.addTextviewInventoryMedicineActivityUpdate.setOnClickListener(view -> {
            startActivity(new Intent(UpdateInventoryMedicine.this, AddInventoryMedicine.class));
            finish();
        });
        binding.overviewTextviewInventoryMedicineActivityUpdate.setOnClickListener(view -> {
            startActivity(new Intent(UpdateInventoryMedicine.this, InventoryMedicine.class));
            finish();
        });
        binding.receivedTextviewInventoryMedicineActivityUpdate.setOnClickListener(view -> {
            startActivity(new Intent(UpdateInventoryMedicine.this, ReceivedInventoryMedicine.class));
            finish();
        });
        binding.removeTextviewInventoryMedicineActivityUpdate.setOnClickListener(view -> {
            startActivity(new Intent(UpdateInventoryMedicine.this, RemoveInventoryMedicine.class));
            finish();
        });

        binding.updateMedicineButtonUpdateInventoryMedicine.setOnClickListener(view -> validateInput());

    }

    private void validateInput() {
        if (medicineSelected.isEmpty()) {
            Toast.makeText(this, "Please select Medicine Name first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.stockUpdateInventoryMedicine.getEditText() == null) {
            Toast.makeText(this, "Validating stock...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.stockUpdateInventoryMedicine.getEditText().getText().toString().trim().length() == 0) {
            binding.stockUpdateInventoryMedicine.setError("Stock is required");
            binding.stockUpdateInventoryMedicine.requestFocus();
            return;
        }

        if (binding.descriptionUpdateInventoryMedicine.getEditText() != null) {
            medicineDescriptionString = binding.descriptionUpdateInventoryMedicine.getEditText().getText().toString();
        }

        if (binding.htuUpdateInventoryMedicine.getEditText() != null) {
            howToUseString = binding.htuUpdateInventoryMedicine.getEditText().getText().toString();
        }

        if (binding.wtuUpdateInventoryMedicine.getEditText() != null) {
            whenToUseString = binding.wtuUpdateInventoryMedicine.getEditText().getText().toString();
        }

        medicineStockString = binding.stockUpdateInventoryMedicine.getEditText().getText().toString();

        if (medicineStockString.isEmpty()) {
            binding.stockUpdateInventoryMedicine.setError("Enter a stock to " + medicineSelected);
            binding.stockUpdateInventoryMedicine.requestFocus();
        }

        if (binding.stockUpdateInventoryMedicine.isErrorEnabled()) {
            binding.stockUpdateInventoryMedicine.setError("");
            binding.stockUpdateInventoryMedicine.setErrorEnabled(false);
        }

        updateTheSelectedMedicine();

    }

    private void updateTheSelectedMedicine() {
        Map<String, Object> medicineObject = new HashMap<>();
        medicineObject.put("medicine_name", medicineSelected);
        medicineObject.put("medicine_stock", medicineStockString);
        medicineObject.put("medicine_description", medicineDescriptionString);
        medicineObject.put("how_to_use", howToUseString);
        medicineObject.put("when_to_use", whenToUseString);

        databaseReference.child(medicineSelected).updateChildren(medicineObject).addOnSuccessListener(success -> {
            resetUserInputs();
            Toast.makeText(this, "You have successfully updated the " + medicineSelected + " medicine", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(failed -> Toast.makeText(this, "Cannot update the " + medicineSelected + " please try again", Toast.LENGTH_SHORT).show());
    }

    private void resetUserInputs() {

        medicineStockString = "";
        medicineDescriptionString = "";
        medicineSelected = "";
        whenToUseString = "";
        howToUseString = "";

        if (binding.stockUpdateInventoryMedicine.getEditText() != null && !medicineStockString.isEmpty()) {
            binding.stockUpdateInventoryMedicine.getEditText().setText("");
        }

        if (binding.descriptionUpdateInventoryMedicine.getEditText() != null && !medicineDescriptionString.isEmpty()) {
            binding.descriptionUpdateInventoryMedicine.getEditText().setText("");
        }

        if (binding.wtuUpdateInventoryMedicine.getEditText() != null && !whenToUseString.isEmpty()) {
            binding.wtuUpdateInventoryMedicine.getEditText().setText("");
        }

        if (binding.htuUpdateInventoryMedicine.getEditText() != null && !howToUseString.isEmpty()) {
            binding.htuUpdateInventoryMedicine.getEditText().setText("");
        }

        binding.stockUpdateInventoryMedicine.requestFocus();
    }

    private void fetchMedicines() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String medicineName = dataSnapshot.child("medicine_name").getValue(String.class);

                        if (medicineName != null) {
                            listOfMedicines.add(medicineName);
                        }
                    }

                    if (listOfMedicines.size() > 0) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(UpdateInventoryMedicine.this, R.layout.signup_gender_list, listOfMedicines);
                        binding.medicineNameTextUpdateInventoryMedicine.setAdapter(arrayAdapter);

                        binding.medicineNameTextUpdateInventoryMedicine.setOnItemClickListener((parent, view, position, id) -> {
                            medicineSelected = parent.getItemAtPosition(position).toString();
                            retrieveAssociatedDataToMedicineSelected();
                        });
                    }
                } else {
                    Toast.makeText(UpdateInventoryMedicine.this, "No medicine found in the inventory, please add first", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateInventoryMedicine.this, "Fetching data was cancelled, " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveAssociatedDataToMedicineSelected() {
        databaseReference.child(medicineSelected).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Medicines > $medicine_name > medicine_name, medicine_stock, medicine_description, how_to_use, when_to_use
                    final String medicineStock = snapshot.child("medicine_stock").getValue(String.class);
                    final String medicineDescription = snapshot.child("medicine_description").getValue(String.class);
                    final String howToUse = snapshot.child("how_to_use").getValue(String.class);
                    final String whenToUse = snapshot.child("when_to_use").getValue(String.class);

                    if (binding.stockUpdateInventoryMedicine.getEditText() != null) {
                        binding.stockUpdateInventoryMedicine.getEditText().setText(medicineStock);
                    }

                    if (binding.descriptionUpdateInventoryMedicine.getEditText() != null) {
                        binding.descriptionUpdateInventoryMedicine.getEditText().setText(medicineDescription);
                    }

                    if (binding.wtuUpdateInventoryMedicine.getEditText() != null) {
                        binding.wtuUpdateInventoryMedicine.getEditText().setText(whenToUse);
                    }

                    if (binding.htuUpdateInventoryMedicine.getEditText() != null) {
                        binding.htuUpdateInventoryMedicine.getEditText().setText(howToUse);
                    }

                    binding.progressBarUpdateInventoryMedicine.setVisibility(View.GONE);
                    binding.updateMedicineButtonUpdateInventoryMedicine.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}