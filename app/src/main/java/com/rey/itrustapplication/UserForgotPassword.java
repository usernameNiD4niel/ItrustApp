package com.rey.itrustapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.databinding.ActivityUserForgotPsswordBinding;

import java.util.Objects;

public class UserForgotPassword extends AppCompatActivity {

    ActivityUserForgotPsswordBinding binding;
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserForgotPsswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.userForgotPasswordBack.setOnClickListener(event -> finish());

        binding.changePassUserForgotPassword.setOnClickListener(event -> {

            final String fullName = Objects.requireNonNull(binding.fullNameUserForgotPassword.getEditText()).getText().toString();
            final String pinCode = Objects.requireNonNull(binding.pinCodeUserForgotPassword.getEditText()).getText().toString();
            final String password = Objects.requireNonNull(binding.newPasswordUserForgotPassword.getEditText()).getText().toString();
            final String retypePassword = Objects.requireNonNull(binding.reTypePasswordUserForgotPassword.getEditText()).getText().toString();

            if (isValidUserInput(fullName, pinCode, password, retypePassword)) {
                setProgressLoad(true);
                checkIfCredentialIsCorrect(fullName, pinCode, password);
            }
        });
    }

    private void checkIfCredentialIsCorrect(String fullName, String pinCode, String password) {

        databaseReference.child("RegularUsers").child(fullName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String code = snapshot.child("pincode").getValue(String.class);

                    if (pinCode.equals(code)) {
                        databaseReference.child("RegularUsers").child(fullName).child("password").setValue(password).addOnSuccessListener(aVoid -> {

                            if (binding.pinCodeUserForgotPassword.isErrorEnabled()) {
                                binding.pinCodeUserForgotPassword.setError(null);
                                binding.pinCodeUserForgotPassword.setErrorEnabled(false);
                            }

                           clearUserInputs();
                            Toast.makeText(UserForgotPassword.this, "You have successfully updated your password", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        binding.pinCodeUserForgotPassword.setError("incorrect pin code");
                        binding.pinCodeUserForgotPassword.requestFocus();
                        setProgressLoad(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearUserInputs() {
        Objects.requireNonNull(binding.fullNameUserForgotPassword.getEditText()).setText("");
        Objects.requireNonNull(binding.pinCodeUserForgotPassword.getEditText()).setText("");
        Objects.requireNonNull(binding.newPasswordUserForgotPassword.getEditText()).setText("");
        Objects.requireNonNull(binding.reTypePasswordUserForgotPassword.getEditText()).setText("");
        setProgressLoad(false);
    }

    private void setProgressLoad(boolean isLoading) {
        if (isLoading) {
            binding.progressBarUserForgotPassword.setVisibility(View.VISIBLE);
            binding.changePassUserForgotPassword.setVisibility(View.INVISIBLE);
        } else {
            binding.progressBarUserForgotPassword.setVisibility(View.GONE);
            binding.changePassUserForgotPassword.setVisibility(View.VISIBLE);
        }
    }

    private boolean isValidUserInput(String fullName, String pinCode, String password, String retypedPassword) {
        if (fullName.isEmpty() || fullName.length() <= 4) {
            binding.fullNameUserForgotPassword.setError("Invalid full name");
            binding.fullNameUserForgotPassword.requestFocus();
            return false;
        }

        if (pinCode.length() != 4) {
            if (binding.fullNameUserForgotPassword.isErrorEnabled()) {
                binding.fullNameUserForgotPassword.setError(null);
                binding.fullNameUserForgotPassword.setErrorEnabled(false);
            }
            binding.pinCodeUserForgotPassword.setError("pin code should be 4 numbers");
            binding.pinCodeUserForgotPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            if (binding.fullNameUserForgotPassword.isErrorEnabled()) {
                binding.fullNameUserForgotPassword.setError(null);
                binding.fullNameUserForgotPassword.setErrorEnabled(false);
            }
            if (binding.pinCodeUserForgotPassword.isErrorEnabled()) {
                binding.pinCodeUserForgotPassword.setError(null);
                binding.pinCodeUserForgotPassword.setErrorEnabled(false);
            }
            binding.newPasswordUserForgotPassword.setError("Password must be at least 6 characters");
            binding.newPasswordUserForgotPassword.requestFocus();
            return false;
        }

        if (!password.equals(retypedPassword)) {
            if (binding.fullNameUserForgotPassword.isErrorEnabled()) {
                binding.fullNameUserForgotPassword.setError(null);
                binding.fullNameUserForgotPassword.setErrorEnabled(false);
            }
            if (binding.pinCodeUserForgotPassword.isErrorEnabled()) {
                binding.pinCodeUserForgotPassword.setError(null);
                binding.pinCodeUserForgotPassword.setErrorEnabled(false);
            }
            if (binding.newPasswordUserForgotPassword.isErrorEnabled()) {
                binding.newPasswordUserForgotPassword.setError(null);
                binding.newPasswordUserForgotPassword.setErrorEnabled(false);
            }

            binding.reTypePasswordUserForgotPassword.setError("password and retype password doesn't match");
            binding.reTypePasswordUserForgotPassword.requestFocus();
            return false;
        }

        if (binding.reTypePasswordUserForgotPassword.isErrorEnabled()) {
            binding.reTypePasswordUserForgotPassword.setError(null);
            binding.reTypePasswordUserForgotPassword.setErrorEnabled(false);
        }

        return true;
    }
}