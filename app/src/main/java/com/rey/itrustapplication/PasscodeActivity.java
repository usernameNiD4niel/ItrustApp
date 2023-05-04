package com.rey.itrustapplication;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.itrustapplication.databinding.ActivityPasscodeBinding;

import java.util.HashMap;
import java.util.Objects;

public class PasscodeActivity extends AdminDrawerBaseActivity {

    ActivityPasscodeBinding activityPasscodeBinding;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
    final DatabaseReference databaseReference = firebaseDatabase.getReference("Admin");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPasscodeBinding = ActivityPasscodeBinding.inflate(getLayoutInflater());
        setContentView(activityPasscodeBinding.getRoot());
        allocateActivityTitle("Passcode");

        activityPasscodeBinding.updateCodeBtn.setOnClickListener(view -> validateUserInputs());
    }

    private void validateUserInputs() {

        String currentCode = Objects.requireNonNull(activityPasscodeBinding.currentCode.getEditText()).getText().toString().trim();
        String newCode = Objects.requireNonNull(activityPasscodeBinding.updatedCode.getEditText()).getText().toString().trim();

        if (currentCode.isEmpty()) {
            activityPasscodeBinding.currentCode.setError("Please enter your current barangay passcode");
            activityPasscodeBinding.currentCode.requestFocus();

            activityPasscodeBinding.updateCodeBtn.setError(null);
            activityPasscodeBinding.updatedCode.setErrorEnabled(false);
            return;
        }

        if (newCode.isEmpty()) {
            activityPasscodeBinding.currentCode.setError(null);
            activityPasscodeBinding.currentCode.setErrorEnabled(false);

            activityPasscodeBinding.updatedCode.setError("Please enter your new barangay passcode");
            activityPasscodeBinding.updatedCode.requestFocus();

            return;
        }

        checIfCurrentCodeIsCorrect(currentCode, newCode);

    }

    private void checIfCurrentCodeIsCorrect(String currentCode, String newCode) {

        databaseReference.child("barangay_code").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                if (currentCode.equals(dataSnapshot.getValue(String.class)))
                    updateBarangayPasscode(newCode);
                else {
                    activityPasscodeBinding.currentCode.setError("Incorrect barangay passcode");
                    activityPasscodeBinding.currentCode.requestFocus();
                }
            }
        });

    }

    private void updateBarangayPasscode(String newCode) {

        HashMap<String, Object> newBarangayCodeObject = new HashMap<>();
        newBarangayCodeObject.put("barangay_code", newCode);

        databaseReference.updateChildren(newBarangayCodeObject)
                .addOnCompleteListener(task -> {
                    Objects.requireNonNull(activityPasscodeBinding.currentCode.getEditText()).setText("");
                    Objects.requireNonNull(activityPasscodeBinding.updatedCode.getEditText()).setText("");

                    activityPasscodeBinding.currentCode.setError(null);
                    activityPasscodeBinding.currentCode.setErrorEnabled(false);

                    activityPasscodeBinding.updateCodeBtn.setError(null);
                    activityPasscodeBinding.updatedCode.setErrorEnabled(false);

                    activityPasscodeBinding.currentCode.requestFocus();
                    Toast.makeText(this, "You have successfully updated Barangay Passcode", Toast.LENGTH_SHORT).show();
                });
    }
}