package com.rey.itrustapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.itrustapplication.database.AdminDatabase;
import com.rey.itrustapplication.databinding.ActivityAdminLoginBinding;
import java.util.Objects;

public class AdminLoginActivity extends AppCompatActivity {

    public ActivityAdminLoginBinding adminLoginBinding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminLoginBinding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(adminLoginBinding.getRoot());

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Admin");

        adminLoginBinding.login.setOnClickListener(view -> setValidation());

        adminLoginBinding.signup.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), AdminRegisterActivity.class)));

        adminLoginBinding.backButtonAdminLogin.setOnClickListener(view ->
                startActivity(new Intent(AdminLoginActivity.this, MainActivity.class)));

    }


    private void setValidation() {
        if (Objects.requireNonNull(adminLoginBinding.usernameAdminLogin.getEditText()).getText().toString().isEmpty()) {
            adminLoginBinding.usernameAdminLogin.getEditText().setError("Username field cannot be empty");
            adminLoginBinding.usernameAdminLogin.requestFocus();
            return;
        }

        if (Objects.requireNonNull(adminLoginBinding.passwordAdminLogin.getEditText()).getText().toString().isEmpty()) {
            adminLoginBinding.passwordAdminLogin.getEditText().setError("Password field cannot be empty");
            adminLoginBinding.passwordAdminLogin.requestFocus();
            return;
        }

        if (Objects.requireNonNull(adminLoginBinding.passcodeAdminLogin.getEditText()).getText().toString().isEmpty()) {
            adminLoginBinding.passcodeAdminLogin.getEditText().setError("PassCode cannot be empty");
            adminLoginBinding.passcodeAdminLogin.requestFocus();
            return;
        }

        loadingProgress(true);

        checkIfLegit();
    }

    public void loadingProgress(boolean isLoading) {
        if (isLoading) {
            adminLoginBinding.progressbarLogin.setVisibility(View.VISIBLE);
            adminLoginBinding.login.setVisibility(View.INVISIBLE);
        } else {
            adminLoginBinding.progressbarLogin.setVisibility(View.GONE);
            adminLoginBinding.login.setVisibility(View.VISIBLE);
        }
    }

    private void checkIfLegit() {

        AdminDatabase adminDatabase = new AdminDatabase(AdminLoginActivity.this, databaseReference);

        final EditText editableText = adminLoginBinding.passcodeAdminLogin.getEditText();
        if (editableText == null) return;

        final String barangayPasscode = editableText.getText().toString();

        Log.d("TAG 2", "checkIfLegit: barangay pass code : " + barangayPasscode);

        adminDatabase.getBarangayPassCode(barangayPasscode, adminLoginBinding);

    }


}