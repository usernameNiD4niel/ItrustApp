package com.rey.itrustapplication.adminform.popup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adminform.activities.SideBAddFormFP;

import java.util.ArrayList;
import java.util.List;

public class PatientNameDropDownPopUp {

    private static String patientSelected = "";

    public static void showPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_name_pop_up, null);
        MaterialAutoCompleteTextView patientNameDropdownPopUp = view.findViewById(R.id.patient_name_dropdown_pop_up);

        builder.setView(view)
                .setCancelable(false)
                .setPositiveButton("Submit", ((dialogInterface, i) -> validatePatientSelected(context)))
                .setTitle("Patient's Name List")
                .setIcon(R.drawable.record_64px)
                .setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        fetchPatientNames(context, patientNameDropdownPopUp);
        patientNameDropdownPopUp.setOnItemClickListener((parent, view2, position, id) -> patientSelected = parent.getItemAtPosition(position).toString());

    }

    private static void validatePatientSelected(Context context) {
        if (patientSelected.isEmpty()) {
            Toast.makeText(context, "Please select patient name first", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(context, SideBAddFormFP.class);
        intent.putExtra("patient_name", patientSelected);
        intent.putExtra("isPreviewing", true);

        context.startActivity(intent);
    }

    private static void fetchPatientNames(Context context, MaterialAutoCompleteTextView patientNameDropdownPopUp) {
        List<String> patientNameList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Forms/FamilyPlanning/SideA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String patientName = dataSnapshot.child("patient_name").getValue(String.class);
                        patientNameList.add(patientName);
                    }
                } else {
                    Toast.makeText(context, "No patient name yet", Toast.LENGTH_SHORT).show();
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.signup_gender_list, patientNameList);
                patientNameDropdownPopUp.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error, " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
