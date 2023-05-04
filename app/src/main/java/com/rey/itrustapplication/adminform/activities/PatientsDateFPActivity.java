package com.rey.itrustapplication.adminform.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.adminform.adapter.PatientsDateAdapter;

import com.rey.itrustapplication.adminform.model.FamilyPlanningModel;
import com.rey.itrustapplication.adminform.model.PatientsDateModel;
import com.rey.itrustapplication.databinding.ActivityPatientsDateFpactivityBinding;

import java.util.ArrayList;
import java.util.List;

public class PatientsDateFPActivity extends AppCompatActivity {

    ActivityPatientsDateFpactivityBinding binding;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private List<PatientsDateModel> patientsDateModelList;
    private PatientsDateAdapter patientsDateAdapter;
    private String patientName;

    private FamilyPlanningModel familyPlanningModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientsDateFpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        patientsDateModelList = new ArrayList<>();
        familyPlanningModel = new FamilyPlanningModel();

        patientName = getIntent().getStringExtra("patient_name");

        if (patientName.isEmpty()) {
            binding.noDataFoundPatientsDateActivity.setVisibility(View.VISIBLE);
            binding.progressBarPatientsDateActivity.setVisibility(View.GONE);
            Toast.makeText(this, "Please go back to this page, you might have a poor internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.patientNameDateActivity.setText(patientName);

        binding.backButtonPatientsDate.setOnClickListener(view -> onBackPressed());

        databaseReference = FirebaseDatabase.getInstance().getReference("Forms/FamilyPlanning/SideA/" + patientName);

        fetchPatientsExistingRecords();

    }

    private void fetchPatientsExistingRecords() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot childSnapshot) {

                if (childSnapshot.exists()) {

                    if (childSnapshot.hasChild("client_id"))
                        familyPlanningModel.setClientId(childSnapshot.child("client_id").getValue(String.class));
                    if (childSnapshot.hasChild("phil_id"))
                        familyPlanningModel.setPhilId(childSnapshot.child("phil_id").getValue(String.class));
                    if (childSnapshot.hasChild("nhts"))
                        familyPlanningModel.setNhts(childSnapshot.child("nhts").getValue(Boolean.class) + "");
                    if (childSnapshot.hasChild("four_Ps"))
                        familyPlanningModel.setFourPs(childSnapshot.child("four_Ps").getValue(Boolean.class) + "");
                    if (childSnapshot.hasChild("given_name"))
                        familyPlanningModel.setGivenName(childSnapshot.child("given_name").getValue(String.class));
                    if (childSnapshot.hasChild("last_name"))
                        familyPlanningModel.setLastName(childSnapshot.child("last_name").getValue(String.class));
                    if (childSnapshot.hasChild("mi"))
                        familyPlanningModel.setMiddleName(childSnapshot.child("mi").getValue(String.class));
                    if (childSnapshot.hasChild("age"))
                        familyPlanningModel.setAge(childSnapshot.child("age").getValue(String.class));
                    if (childSnapshot.hasChild("date_of_birth"))
                        familyPlanningModel.setDateOfBirth(childSnapshot.child("date_of_birth").getValue(String.class));
                    if (childSnapshot.hasChild("educ_attain"))
                        familyPlanningModel.setEducAttain(childSnapshot.child("educ_attain").getValue(String.class));
                    if (childSnapshot.hasChild("occupation"))
                        familyPlanningModel.setOccupation(childSnapshot.child("occupation").getValue(String.class));

                    for (DataSnapshot snapshot : childSnapshot.getChildren()) {

                        if (snapshot.hasChild("current_date")) {
                            final String current_date = snapshot.child("current_date").getValue(String.class);
                            final String current_time = snapshot.child("current_time").getValue(String.class);
                            final String date_key = snapshot.child("date_key").getValue(String.class);

                            patientsDateModelList.add(new PatientsDateModel(current_date, current_time, date_key));
                        }
                    }

                    patientsDateAdapter = new PatientsDateAdapter(patientsDateModelList, patientName, PatientsDateFPActivity.this, familyPlanningModel);
                    binding.recyclerviewPatientNameActivity.setItemViewCacheSize(5);
                    binding.recyclerviewPatientNameActivity.setHasFixedSize(true);
                    binding.recyclerviewPatientNameActivity.setAdapter(patientsDateAdapter);

                } else {
                    Log.d("DB", "onDataChange: Doesn't Exist");
                    binding.noDataFoundPatientsDateActivity.setVisibility(View.VISIBLE);
                }

                if (binding.progressBarPatientsDateActivity.getVisibility() == View.VISIBLE)
                    binding.progressBarPatientsDateActivity.setVisibility(View.GONE);
//                patientsDateAdapter.notifyItemRangeInserted(positionStart, patientsDateModelList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PatientsDateFPActivity.this, "Error, " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    protected void onStop() {
        if (databaseReference != null)
            databaseReference.removeEventListener(valueEventListener);
        super.onStop();
    }
}