package com.rey.itrustapplication.medicine_inventory.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.AdminDrawerBaseActivity;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.ActivityRemoveInventoryMedicineBinding;

import java.util.ArrayList;

public class RemoveInventoryMedicine extends AdminDrawerBaseActivity implements View.OnClickListener {

    ActivityRemoveInventoryMedicineBinding binding;
    private String selectedMedicineName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRemoveInventoryMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Medicine Inventory | Delete");

        populateDropDown();

        binding.overviewTextviewInventoryMedicineActivityRemove.setOnClickListener(this);
        binding.addTextviewInventoryMedicineActivityRemove.setOnClickListener(this);
        binding.updateTextviewInventoryMedicineActivityRemove.setOnClickListener(this);
        binding.receivedTextviewInventoryMedicineActivityRemove.setOnClickListener(this);

        binding.removeButtonRemoveInventoryMedicine.setOnClickListener(view -> validateUserInputs());

    }

    private void validateUserInputs() {
        if (selectedMedicineName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please select medicine first", Toast.LENGTH_SHORT).show();
            return;
        }

//        barangay_passcode_remove_inventory_medicine
        if (binding.barangayPasscodeRemoveInventoryMedicine.getEditText() == null) return;

        String passCode = binding.barangayPasscodeRemoveInventoryMedicine.getEditText().getText().toString();

        if (passCode.isEmpty()) {
            binding.barangayPasscodeRemoveInventoryMedicine.setError("This field is empty, please enter passcode");
            return;
        }

        binding.progressBarRemoveInventoryMedicine.setVisibility(View.VISIBLE);
        binding.removeButtonRemoveInventoryMedicine.setVisibility(View.GONE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        ValueEventListener valueEventListener;

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (passCode.equals(snapshot.getValue(String.class))) {

                        if (binding.barangayPasscodeRemoveInventoryMedicine.isErrorEnabled()) {
                            binding.barangayPasscodeRemoveInventoryMedicine.setError("");
                            binding.barangayPasscodeRemoveInventoryMedicine.setErrorEnabled(false);
                        }

                        if (binding.progressBarRemoveInventoryMedicine.getVisibility() == View.VISIBLE) {
                            binding.progressBarRemoveInventoryMedicine.setVisibility(View.GONE);
                            binding.removeButtonRemoveInventoryMedicine.setVisibility(View.VISIBLE);
                        }

                        databaseReference.child("Medicines/" + selectedMedicineName).removeValue()
                                .addOnSuccessListener(succ -> resetInputs())
                                .addOnFailureListener(failed -> Toast.makeText(RemoveInventoryMedicine.this, "Failed, " + failed.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        if (binding.progressBarRemoveInventoryMedicine.getVisibility() == View.VISIBLE) {
                            binding.progressBarRemoveInventoryMedicine.setVisibility(View.GONE);
                            binding.removeButtonRemoveInventoryMedicine.setVisibility(View.VISIBLE);
                        }

                        binding.barangayPasscodeRemoveInventoryMedicine.setError("Barangay Passcode is incorrect");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.child("Admin/barangay_code").addListenerForSingleValueEvent(valueEventListener);

    }

    private void resetInputs() {
        selectedMedicineName = "";
        if (binding.barangayPasscodeRemoveInventoryMedicine.getEditText() != null)
            binding.barangayPasscodeRemoveInventoryMedicine.getEditText().setText("");
        binding.medicinesTextRemoveInventoryMedicine.setText("");

        populateDropDown();
    }

    private void populateDropDown() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Medicines");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (!binding.removeButtonRemoveInventoryMedicine.isEnabled())
                        binding.removeButtonRemoveInventoryMedicine.setEnabled(true);

                    final ArrayList<String> listOfMedicineNames = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String medicine_name = dataSnapshot.child("medicine_name").getValue(String.class);

                        listOfMedicineNames.add(medicine_name);
                    }

                    if (!listOfMedicineNames.isEmpty()) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.signup_gender_list,listOfMedicineNames);
                        binding.medicinesTextRemoveInventoryMedicine.setAdapter(arrayAdapter);

                        binding.medicinesTextRemoveInventoryMedicine.setOnItemClickListener((parent, view, position, id) -> selectedMedicineName = parent.getItemAtPosition(position).toString());
                    }
                } else {
                    binding.removeButtonRemoveInventoryMedicine.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RemoveInventoryMedicine.this, "Fetching data cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == binding.overviewTextviewInventoryMedicineActivityRemove.getId())
            startActivity(new Intent(RemoveInventoryMedicine.this, InventoryMedicine.class));
        else if (view.getId() == binding.addTextviewInventoryMedicineActivityRemove.getId())
            startActivity(new Intent(RemoveInventoryMedicine.this, AddInventoryMedicine.class));
        else if (view.getId() == binding.updateTextviewInventoryMedicineActivityRemove.getId())
            startActivity(new Intent(RemoveInventoryMedicine.this, UpdateInventoryMedicine.class));
        else
            startActivity(new Intent(RemoveInventoryMedicine.this, ReceivedInventoryMedicine.class));
        finish();
    }
}