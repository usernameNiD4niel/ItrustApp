package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.databinding.ActivityUserOtpBinding;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.util.Objects;

public class ForgotPinCode extends AppCompatActivity {

    ActivityUserOtpBinding activityUserOtpBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserOtpBinding = ActivityUserOtpBinding.inflate(getLayoutInflater());
        setContentView(activityUserOtpBinding.getRoot());

        activityUserOtpBinding.backButtonForgotPinCode.setOnClickListener(back -> finish());

        activityUserOtpBinding.updatePinCodeButton.setOnClickListener(view -> validateUserInput());
    }

    private void validateUserInput() {
        final String currentPassword = Objects.requireNonNull(activityUserOtpBinding.currentPasswordForgotPinCode.getEditText()).getText().toString();
        final String newPin = Objects.requireNonNull(activityUserOtpBinding.newPinForgotPinCode.getEditText()).getText().toString();

        if (currentPassword.isEmpty()) {
            activityUserOtpBinding.currentPasswordForgotPinCode.setError("Please enter a valid Password");
            activityUserOtpBinding.currentPasswordForgotPinCode.requestFocus();
            return;
        }

        if (newPin.isEmpty()) {
            activityUserOtpBinding.newPinForgotPinCode.setError("Please enter a valid new Pin");
            activityUserOtpBinding.newPinForgotPinCode.requestFocus();
            return;
        }

        if (newPin.length() != 4) {
            activityUserOtpBinding.newPinForgotPinCode.setError("Pin code must be 4 digits only");
            activityUserOtpBinding.newPinForgotPinCode.requestFocus();
            return;
        }

        activityUserOtpBinding.currentPasswordForgotPinCode.setErrorEnabled(false);
        activityUserOtpBinding.currentPasswordForgotPinCode.setError(null);

        activityUserOtpBinding.newPinForgotPinCode.setErrorEnabled(false);
        activityUserOtpBinding.newPinForgotPinCode.setError(null);

        activityUserOtpBinding.progressBarForgotPin.setVisibility(View.VISIBLE);
        activityUserOtpBinding.updatePinCodeButton.setVisibility(View.INVISIBLE);

        validateDatabaseData(currentPassword, newPin);
    }

    private void validateDatabaseData(String currentPassword, String newPin) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("RegularUsers");

        final String currentUser = new SessionManager(this).getFullName();
        databaseReference.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String password_ = snapshot.child("password").getValue(String.class);
                    if (currentPassword.equals(password_))
                        databaseReference.child(currentUser).child("pincode").setValue(newPin).addOnSuccessListener(task ->
                                resetInputs());
                    else {
                        activityUserOtpBinding.currentPasswordForgotPinCode.setError("Incorrect password");
                        activityUserOtpBinding.currentPasswordForgotPinCode.requestFocus();

                        activityUserOtpBinding.updatePinCodeButton.setVisibility(View.VISIBLE);
                        activityUserOtpBinding.progressBarForgotPin.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void resetInputs() {
        Toast.makeText(ForgotPinCode.this, "You have successfully updated your pin code", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, UserPasscode.class));
        finish();
    }
}