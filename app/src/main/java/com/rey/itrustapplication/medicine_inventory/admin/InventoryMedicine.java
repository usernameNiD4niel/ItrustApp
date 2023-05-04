package com.rey.itrustapplication.medicine_inventory.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.AdminDrawerBaseActivity;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.ActivityInventoryMedicineBinding;
import com.rey.itrustapplication.medicine_inventory.admin.medicine_view_all.MedicineHistory;

public class InventoryMedicine extends AdminDrawerBaseActivity {

    ActivityInventoryMedicineBinding binding;
    ValueEventListener fetchMedicineRecords, fetchReceiveHistory;
    DatabaseReference fetchMedicineRecordsDB, fetchReceiveHistoryDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventoryMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        allocateActivityTitle("Medicine Inventory | Overview");

        fetchMedicineRecords();
        fetchReceivedHistory();

        binding.viewAllRecentAddedOverviewInventoryMedicine.setOnClickListener(view -> {
            startActivity(new Intent(InventoryMedicine.this, MedicineHistory.class));
            finish();
        });
        binding.viewAllReceivedHistoryOverviewInventoryMedicine.setOnClickListener(view -> {
            startActivity(new Intent(InventoryMedicine.this, ReceiveHistoryViewAll.class));
            finish();
        });

        binding.updateTextviewInventoryMedicineActivity.setOnClickListener(view -> {
            startActivity(new Intent(InventoryMedicine.this, UpdateInventoryMedicine.class));
            finish();
        });
        binding.addTextviewInventoryMedicineActivity.setOnClickListener(view -> {
            startActivity(new Intent(InventoryMedicine.this, AddInventoryMedicine.class));
            finish();
        });
        binding.receivedTextviewInventoryMedicineActivity.setOnClickListener(view -> {
            startActivity(new Intent(InventoryMedicine.this, ReceivedInventoryMedicine.class));
            finish();
        });
        binding.removeTextviewInventoryMedicineActivity.setOnClickListener(view -> {
            startActivity(new Intent(InventoryMedicine.this, RemoveInventoryMedicine.class));
            finish();
        });


    }

    private void fetchReceivedHistory() {
        fetchReceiveHistoryDB = FirebaseDatabase.getInstance().getReference("ClaimedMedicines");
        int[] counter = { 0 };
        fetchReceiveHistory = fetchReceiveHistoryDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    final  int childCount = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));

                    final String placeHolder = getResources().getString(R.string.no_data_found);

                    if (childCount == 1) {

                        binding.medicineText2InventoryMedicineActivity.setText(placeHolder);
                        binding.patientNameOverviewInventoryMedicine2.setText(placeHolder);
                        binding.dateReceivedInventoryMedicine2.setText(placeHolder);

                        binding.medicineText3InventoryMedicineActivity.setText(placeHolder);
                        binding.patientNameOverviewInventoryMedicine3.setText(placeHolder);
                        binding.dateReceivedInventoryMedicine3.setText(placeHolder);

                    } else if (childCount == 2) {

                        binding.medicineText3InventoryMedicineActivity.setText(placeHolder);
                        binding.patientNameOverviewInventoryMedicine3.setText(placeHolder);
                        binding.dateReceivedInventoryMedicine3.setText(placeHolder);

                    }

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String patient_name = dataSnapshot.child("patient_name").getValue(String.class);
                        String medicine_name = dataSnapshot.child("medicine_name").getValue(String.class);
                        final String date_received = dataSnapshot.child("date_received").getValue(String.class);

                        if (medicine_name == null) {
                            Toast.makeText(InventoryMedicine.this, "No medicine added", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (medicine_name.endsWith(",")) {
                            medicine_name = medicine_name.substring(0, medicine_name.length()-1);
                        }

                        binding.medicineTextInventoryMedicineActivity.setText(medicine_name);
                        binding.patientNameOverviewInventoryMedicine.setText(patient_name);
                        binding.dateReceivedInventoryMedicineActivity.setText(date_received);

                        if (counter[0] == 1) {
                            binding.medicineText2InventoryMedicineActivity.setText(medicine_name);
                            binding.patientNameOverviewInventoryMedicine2.setText(patient_name);
                            binding.dateReceivedInventoryMedicine2.setText(date_received);
                        } else if (counter[0] == 2){
                            binding.medicineText3InventoryMedicineActivity.setText(medicine_name);
                            binding.patientNameOverviewInventoryMedicine3.setText(patient_name);
                            binding.dateReceivedInventoryMedicine3.setText(date_received);
                            break;
                        }

                        counter[0]++;
                    }
                } else {
                    final String placeHolder = getResources().getString(R.string.no_data_found);

                    binding.medicineTextInventoryMedicineActivity.setText(placeHolder);
                    binding.patientNameOverviewInventoryMedicine.setText(placeHolder);
                    binding.dateReceivedInventoryMedicineActivity.setText(placeHolder);

                    binding.medicineText2InventoryMedicineActivity.setText(placeHolder);
                    binding.patientNameOverviewInventoryMedicine2.setText(placeHolder);
                    binding.dateReceivedInventoryMedicine2.setText(placeHolder);

                    binding.medicineText3InventoryMedicineActivity.setText(placeHolder);
                    binding.patientNameOverviewInventoryMedicine3.setText(placeHolder);
                    binding.dateReceivedInventoryMedicine3.setText(placeHolder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStop() {
        fetchMedicineRecordsDB.removeEventListener(fetchMedicineRecords);
        fetchReceiveHistoryDB.removeEventListener(fetchReceiveHistory);
        super.onStop();
    }

    private void fetchMedicineRecords() {
        fetchMedicineRecordsDB = FirebaseDatabase.getInstance().getReference("Medicines");

        int[] counter = { 0 };

        fetchMedicineRecords = fetchMedicineRecordsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    final long childCount = snapshot.getChildrenCount();
                    binding.numberOfMedicineInventoryMedicine.setText(String.valueOf(childCount));

                    final String placeHolder = getResources().getString(R.string.no_data_found);

                    if (childCount == 1) {

                        binding.recentAddedMedicineTextInventoryMedicineActivity2.setText(placeHolder);
                        binding.descriptionOverviewInventoryMedicine2.setText(placeHolder);
                        binding.recentAddedDateReceivedInventoryMedicineActivity2.setText(placeHolder);

                        binding.recentAddedMedicineTextInventoryMedicineActivity3.setText(placeHolder);
                        binding.descriptionOverviewInventoryMedicine3.setText(placeHolder);
                        binding.recentAddedDateReceivedInventoryMedicineActivity3.setText(placeHolder);

                    } else if (childCount == 2) {

                        binding.recentAddedMedicineTextInventoryMedicineActivity3.setText(placeHolder);
                        binding.descriptionOverviewInventoryMedicine3.setText(placeHolder);
                        binding.recentAddedDateReceivedInventoryMedicineActivity3.setText(placeHolder);

                    }

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        final String medicine_name = dataSnapshot.child("medicine_name").getValue(String.class);
                        final String medicine_stock = dataSnapshot.child("medicine_stock").getValue(String.class);
                        final String medicine_description = dataSnapshot.child("medicine_description").getValue(String.class);

                        if (medicine_description == null) return;

                        String medDesc = (medicine_description.length() > 35) ? "Description: " + medicine_description.substring(0, 25) + "..." : medicine_description;

                        if (medDesc.trim().isEmpty()) {
                            medDesc = "No Description Provided";
                        }

                        if (counter[0] == 0) {
                            binding.recentAddedMedicineTextInventoryMedicineActivity.setText(medicine_name);
                            binding.descriptionOverviewInventoryMedicine.setText(medDesc);
                            binding.recentAddedDateReceivedInventoryMedicineActivity.setText(medicine_stock);
                        } else if (counter[0] == 1) {
                            binding.recentAddedMedicineTextInventoryMedicineActivity2.setText(medicine_name);
                            binding.descriptionOverviewInventoryMedicine2.setText(medDesc);
                            binding.recentAddedDateReceivedInventoryMedicineActivity2.setText(medicine_stock);
                        } else {
                            binding.recentAddedMedicineTextInventoryMedicineActivity3.setText(medicine_name);
                            binding.descriptionOverviewInventoryMedicine3.setText(medDesc);
                            binding.recentAddedDateReceivedInventoryMedicineActivity3.setText(medicine_stock);
                            break;
                        }

                        counter[0]++;
                    }

                } else {
                    final String placeHolder = getResources().getString(R.string.no_data_found);

                    binding.recentAddedMedicineTextInventoryMedicineActivity.setText(placeHolder);
                    binding.descriptionOverviewInventoryMedicine.setText(placeHolder);
                    binding.recentAddedDateReceivedInventoryMedicineActivity.setText(placeHolder);

                    binding.recentAddedMedicineTextInventoryMedicineActivity2.setText(placeHolder);
                    binding.descriptionOverviewInventoryMedicine2.setText(placeHolder);
                    binding.recentAddedDateReceivedInventoryMedicineActivity2.setText(placeHolder);

                    binding.recentAddedMedicineTextInventoryMedicineActivity3.setText(placeHolder);
                    binding.descriptionOverviewInventoryMedicine3.setText(placeHolder);
                    binding.recentAddedDateReceivedInventoryMedicineActivity3.setText(placeHolder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}