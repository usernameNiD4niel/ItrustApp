package com.rey.itrustapplication.medicine_inventory.admin;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.AdminDrawerBaseActivity;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.ActivityReceivedInventoryMedicineBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class ReceivedInventoryMedicine extends AdminDrawerBaseActivity {

    ActivityReceivedInventoryMedicineBinding binding;
    private String patientName = "", selectedMedicine = "", reason = "", gamot = "", bilang = "";
    private ArrayList<String> listOfMedicines, addedHolder;
    private ArrayList<Integer> listOfQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceivedInventoryMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Inventory Medicine | Received");

        listOfMedicines = new ArrayList<>();
        listOfQuantity = new ArrayList<>();
        addedHolder = new ArrayList<>();

        populateDropdownMenu();

        binding.addTextviewInventoryMedicineActivityReceive.setOnClickListener(view -> {
            startActivity(new Intent(ReceivedInventoryMedicine.this, AddInventoryMedicine.class));
            finish();
        });

        binding.updateTextviewInventoryMedicineActivityReceive.setOnClickListener(view -> {
            startActivity(new Intent(ReceivedInventoryMedicine.this, UpdateInventoryMedicine.class));
            finish();
        });

        binding.overviewTextviewInventoryMedicineActivityReceive.setOnClickListener(view -> {
            startActivity(new Intent(ReceivedInventoryMedicine.this, InventoryMedicine.class));
            finish();
        });

        binding.removeTextviewInventoryMedicineActivityReceive.setOnClickListener(view -> {
            startActivity(new Intent(ReceivedInventoryMedicine.this, RemoveInventoryMedicine.class));
            finish();
        });

        binding.addReceiveButtonReceiveInventoryMedicine.setOnClickListener(view -> validateUserInput());
        binding.addButtonReceiveInventoryMedicine.setOnClickListener(view -> validateQuantityInput());
        binding.clearTableReceiveInventoryMedicine.setOnClickListener(view -> {
            binding.quantityContentReceiveInventoryMedicine.removeAllViews();
            binding.medicineContentReceiveInventoryMedicine.removeAllViews();
            binding.quantityContentReceiveInventoryMedicine.addView(binding.quantityPlaceholderReceiveInventoryMedicine);
            binding.medicineContentReceiveInventoryMedicine.addView(binding.medicinesPlaceholderReceiveInventoryMedicine);
            gamot = "";
            bilang = "";
        });
    }

    private void validateQuantityInput() {
        if (binding.quantityReceiveInventoryMedicine.getEditText() == null) return;
        
        if (!addedHolder.isEmpty()) {
            for (String selectedItem : addedHolder) {
                if (selectedItem.equals(selectedMedicine)) {
                    Toast.makeText(this, "You cannot add duplicate medicine, clear the table and do it again", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        
        addedHolder.add(selectedMedicine);

        final int quantityText = Integer.parseInt(binding.quantityReceiveInventoryMedicine.getEditText().getText().toString());

        if (quantityText <= 0) {
            binding.quantityReceiveInventoryMedicine.setError("Quantity should be whole number and cannot be 0");
            binding.quantityReceiveInventoryMedicine.requestFocus();
            return;
        }

        if (selectedMedicine == null) {
            Toast.makeText(this, "Please select medicine name first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (listOfMedicines.isEmpty()) {
            Toast.makeText(this, "No medicine fount in the inventory", Toast.LENGTH_SHORT).show();
            return;
        }

        int index = listOfMedicines.indexOf(selectedMedicine);

        int position = listOfQuantity.get(index);

        if (quantityText > position) {
            binding.quantityReceiveInventoryMedicine.setError("The quantity should not exceed to " + position);
            binding.quantityReceiveInventoryMedicine.requestFocus();
            return;
        }

        if (binding.quantityReceiveInventoryMedicine.isErrorEnabled()) {
            binding.quantityReceiveInventoryMedicine.setError("");
            binding.quantityReceiveInventoryMedicine.setErrorEnabled(false);
        }

        if (binding.medicineContentReceiveInventoryMedicine.getChildAt(0) == binding.medicinesPlaceholderReceiveInventoryMedicine)
            binding.medicineContentReceiveInventoryMedicine.removeView(binding.medicinesPlaceholderReceiveInventoryMedicine);

        TextView textView = new TextView(ReceivedInventoryMedicine.this);
        textView.setTextColor(getResources().getColor(R.color.main_color));
        final String tvText = quantityText + "";
        textView.setText(tvText);

        binding.medicineContentReceiveInventoryMedicine.addView(textView);

        if (binding.quantityContentReceiveInventoryMedicine.getChildAt(0) == binding.quantityPlaceholderReceiveInventoryMedicine)
            binding.quantityContentReceiveInventoryMedicine.removeView(binding.quantityPlaceholderReceiveInventoryMedicine);

        TextView textView1 = new TextView(ReceivedInventoryMedicine.this);
        textView1.setText(selectedMedicine);
        textView1.setTextColor(getResources().getColor(R.color.main_color));

        binding.quantityContentReceiveInventoryMedicine.addView(textView1);

        binding.quantityReceiveInventoryMedicine.getEditText().setText("");
        binding.quantityReceiveInventoryMedicine.requestFocus();

        gamot += selectedMedicine + ",";

        bilang += quantityText + ",";

    }

    private void populateDropdownMenu() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Medicines");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (!binding.addReceiveButtonReceiveInventoryMedicine.isEnabled())
                        binding.addReceiveButtonReceiveInventoryMedicine.setEnabled(true);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String medicine_name = dataSnapshot.child("medicine_name").getValue(String.class);
                        final String medicine_stock = dataSnapshot.child("medicine_stock").getValue(String.class);

                        if (medicine_stock == null) {
                            break;
                        }

                        listOfMedicines.add(medicine_name);
                        listOfQuantity.add(Integer.parseInt(medicine_stock));
                    }

                    if (!listOfMedicines.isEmpty()) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ReceivedInventoryMedicine.this, R.layout.signup_gender_list, listOfMedicines);
                        binding.medicinesTextReceiveInventoryMedicine.setAdapter(arrayAdapter);

                        binding.medicinesTextReceiveInventoryMedicine.setOnItemClickListener((parent, view, position, id) ->
                            selectedMedicine = parent.getItemAtPosition(position).toString()
                        );
                    }
                } else {
                    Toast.makeText(ReceivedInventoryMedicine.this, "No medicine found in the inventory, please add medicine first", Toast.LENGTH_SHORT).show();
                    binding.addReceiveButtonReceiveInventoryMedicine.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReceivedInventoryMedicine.this, "Fetching data was cancelled, " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateUserInput() {
        if (binding.patientNameReceiveInventoryMedicine.getEditText() == null || binding.reasonReceiveInventoryMedicine.getEditText() == null) {
            return;
        }

        patientName = binding.patientNameReceiveInventoryMedicine.getEditText().getText().toString().trim();
        reason = binding.reasonReceiveInventoryMedicine.getEditText().getText().toString().trim();

        if (patientName.isEmpty()) {
            binding.patientNameReceiveInventoryMedicine.setError("Please enter patient name");
            binding.patientNameReceiveInventoryMedicine.requestFocus();
            return;
        }

        if (binding.patientNameReceiveInventoryMedicine.isErrorEnabled()) {
            binding.patientNameReceiveInventoryMedicine.setError("");
            binding.patientNameReceiveInventoryMedicine.setErrorEnabled(false);
        }

        if (binding.reasonReceiveInventoryMedicine.isErrorEnabled()) {
            binding.reasonReceiveInventoryMedicine.setError("");
            binding.reasonReceiveInventoryMedicine.setErrorEnabled(false);
        }

        if (gamot.isEmpty()) {
            Toast.makeText(this, "Please add atleast 1 quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        pushToReceivedMedicines();

    }

    private void pushToReceivedMedicines() {
        // ClaimedMedicines > $uid > patient_name, medicine_name (list), quantity (list), reason

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", new Locale("fil", "PH"));
        dateFormat.setTimeZone(TimeZone.getDefault());
        String currentDate = dateFormat.format(new Date());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> receivedObject = new HashMap<>();
        receivedObject.put("patient_name", patientName);
        receivedObject.put("medicine_name", (gamot.endsWith(",")) ? gamot.substring(0,gamot.length()-1) : gamot);
        receivedObject.put("date_received", currentDate);
        receivedObject.put("quantity", (bilang.endsWith(",")) ? bilang.substring(0,bilang.length()-1) : bilang);
        receivedObject.put("reason", reason);

        String[] gamotArray = gamot.split(",");
        String[] quantityArray = bilang.split(",");

        int[] counter = { 0 };

        databaseReference.child("ClaimedMedicines").push().setValue(receivedObject).addOnSuccessListener(success ->
                updateStock(counter, gamotArray, databaseReference, quantityArray)).addOnFailureListener(failed ->
                Toast.makeText(this, "Can't add a received because, " + failed.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateStock(int[] counter, String[] gamotArray, DatabaseReference databaseReference, String[] quantityArray) {

        int index = listOfMedicines.indexOf(gamotArray[counter[0]]);

        int position = listOfQuantity.get(index);
        
        final int finalStock = position - Integer.parseInt(quantityArray[counter[0]]);

        databaseReference.child("Medicines/" + gamotArray[counter[0]] + "/medicine_stock").setValue(String.valueOf(finalStock)).addOnSuccessListener(succ -> {
            counter[0]++;

            if (gamotArray.length == counter[0]) {
                resetInputsAndVariables();
                Toast.makeText(this, "You have successfully create a record for received", Toast.LENGTH_SHORT).show();
                return;
            }

            updateStock(counter, gamotArray, databaseReference, quantityArray);
        }).addOnFailureListener(failed -> Toast.makeText(this, "Cannot update the stock", Toast.LENGTH_SHORT).show());

    }

    private void resetInputsAndVariables() {

        if (binding.patientNameReceiveInventoryMedicine.getEditText() == null || binding.reasonReceiveInventoryMedicine.getEditText() == null ||
        binding.quantityReceiveInventoryMedicine.getEditText() == null) {
            return;
        }

        if (!patientName.isEmpty()) {
            binding.patientNameReceiveInventoryMedicine.getEditText().setText("");
        }

        if (!reason.isEmpty()) {
            binding.reasonReceiveInventoryMedicine.getEditText().setText("");
        }

        if (!binding.quantityReceiveInventoryMedicine.getEditText().getText().toString().trim().isEmpty()) {
            binding.quantityReceiveInventoryMedicine.getEditText().setText("");
        }

        gamot = "";
        bilang = "";
        reason = "";
        patientName = "";

        binding.patientNameReceiveInventoryMedicine.requestFocus();

        binding.medicineContentReceiveInventoryMedicine.removeAllViews();
        binding.medicineContentReceiveInventoryMedicine.addView(binding.medicinesPlaceholderReceiveInventoryMedicine);

        binding.quantityContentReceiveInventoryMedicine.removeAllViews();
        binding.quantityContentReceiveInventoryMedicine.addView(binding.quantityPlaceholderReceiveInventoryMedicine);
    }
}