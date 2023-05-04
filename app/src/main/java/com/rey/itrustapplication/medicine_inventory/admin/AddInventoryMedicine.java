package com.rey.itrustapplication.medicine_inventory.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.itrustapplication.AdminDrawerBaseActivity;
import com.rey.itrustapplication.databinding.ActivityAddInventoryMedicineBinding;

import java.util.HashMap;
import java.util.Map;

public class AddInventoryMedicine extends AdminDrawerBaseActivity {

    ActivityAddInventoryMedicineBinding binding;

    private String medicineName = "", medicineStock = "", description = "", whenToUse = "", howToUse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddInventoryMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        allocateActivityTitle("Medicine Inventory | Add");

        binding.updateTextviewInventoryMedicineActivityAdd.setOnClickListener(view -> {
            startActivity(new Intent(AddInventoryMedicine.this, UpdateInventoryMedicine.class));
            finish();
        });
        binding.overviewTextviewInventoryMedicineActivityAdd.setOnClickListener(view -> {
            startActivity(new Intent(AddInventoryMedicine.this, InventoryMedicine.class));
            finish();
        });
        binding.receivedTextviewInventoryMedicineActivityAdd.setOnClickListener(view -> {
            startActivity(new Intent(AddInventoryMedicine.this, ReceivedInventoryMedicine.class));
            finish();
        });

        binding.removeTextviewInventoryMedicineActivityAdd.setOnClickListener(view -> {
            startActivity(new Intent(AddInventoryMedicine.this, RemoveInventoryMedicine.class));
            finish();
        });

        binding.addMedicineButtonAddInventoryMedicine.setOnClickListener(view -> validateUserInput());
    }

    private void validateUserInput() {

        if (binding.medicineNameAddInventoryMedicine.getEditText() == null ||
                binding.stockAddInventoryMedicine.getEditText() == null) {
            Toast.makeText(this, "All fields are required to fill up", Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.descriptionAddInventoryMedicine.getEditText() != null) {
            description = binding.descriptionAddInventoryMedicine.getEditText().getText().toString() + "";
        }

        if (binding.htuAddInventoryMedicine.getEditText() != null) {
            howToUse = binding.htuAddInventoryMedicine.getEditText().getText().toString() + "";
        }

        if (binding.wtuAddInventoryMedicine.getEditText() != null) {
            whenToUse = binding.wtuAddInventoryMedicine.getEditText().getText().toString() + "";
        }

        medicineName = binding.medicineNameAddInventoryMedicine.getEditText().getText().toString();
        medicineStock = binding.stockAddInventoryMedicine.getEditText().getText().toString();

        if (medicineName.isEmpty()) {
            binding.medicineNameAddInventoryMedicine.setError("Please enter a medicine name");
            binding.medicineNameAddInventoryMedicine.requestFocus();
            return;
        }

        if (medicineStock.isEmpty()) {
            binding.stockAddInventoryMedicine.setError("Please enter how many medicine claimed");
            binding.stockAddInventoryMedicine.requestFocus();
            return;
        }

        if (binding.medicineNameAddInventoryMedicine.isErrorEnabled()) {
            binding.medicineNameAddInventoryMedicine.setErrorEnabled(false);
            binding.medicineNameAddInventoryMedicine.setError("");
        }

        if (binding.stockAddInventoryMedicine.isErrorEnabled()) {
            binding.stockAddInventoryMedicine.setErrorEnabled(false);
            binding.stockAddInventoryMedicine.setError("");
        }

        // Medicines > $medicine_name > medicine_name, medicine_stock, medicine_description, how_to_use, when_to_use
        pushDataToDatabase();

    }

    private void pushDataToDatabase() {

        Map<String, Object> medicineObject = new HashMap<>();
        medicineObject.put("medicine_name", medicineName);
        medicineObject.put("medicine_stock", medicineStock);
        medicineObject.put("medicine_description", description);
        medicineObject.put("how_to_use", howToUse);
        medicineObject.put("when_to_use", whenToUse);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Medicines");

        databaseReference.child(medicineName).setValue(medicineObject).addOnSuccessListener(success -> {
            resetInputs();
            Toast.makeText(this, "New medicine added to the database", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(failed -> Toast.makeText(this, "Cannot add medicine, because " + failed.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void resetInputs() {

        if (binding.medicineNameAddInventoryMedicine.getEditText() != null)
            binding.medicineNameAddInventoryMedicine.getEditText().setText("");

        if (binding.stockAddInventoryMedicine.getEditText() != null)
            binding.stockAddInventoryMedicine.getEditText().setText("");

        if (binding.descriptionAddInventoryMedicine.getEditText() != null && !description.isEmpty())
            binding.descriptionAddInventoryMedicine.getEditText().setText("");

        if (binding.htuAddInventoryMedicine.getEditText() != null && !howToUse.isEmpty())
            binding.htuAddInventoryMedicine.getEditText().setText("");

        if (binding.wtuAddInventoryMedicine.getEditText() != null && !whenToUse.isEmpty())
            binding.wtuAddInventoryMedicine.getEditText().setText("");

        medicineName = "";
        medicineStock = "";
        description = "";
        howToUse = "";
        whenToUse = "";

        binding.medicineNameAddInventoryMedicine.requestFocus();

    }
}