package com.rey.itrustapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rey.itrustapplication.database.AdminDatabase;

import java.util.LinkedList;
import java.util.Objects;

public class AdminRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout buongPangalan, tirahan, username, password, passcode;
    Button signup;
    TextView login;
    ProgressBar progressBar;

    final Handler handler = new Handler();
    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
    DatabaseReference databaseReference;
    AdminDatabase adminDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

//        Hooks
        buongPangalan = findViewById(R.id.buongPangalan);
        tirahan = findViewById(R.id.tirahan);
        username = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        passcode = findViewById(R.id.passcode);

        signup = findViewById(R.id.signUp);
        signup.setOnClickListener(this);
        login = findViewById(R.id.already_have_account);
        login.setOnClickListener(this);

        progressBar = findViewById(R.id.progressbar);

        findViewById(R.id.backButtonAdminRegister).setOnClickListener(view -> finish());

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.already_have_account:
                startActivity(new Intent(this, AdminLoginActivity.class));
                break;
            case R.id.signUp:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String fullName = Objects.requireNonNull(buongPangalan.getEditText()).getText().toString().trim();
        String homeAddress = Objects.requireNonNull(tirahan.getEditText()).getText().toString().trim();
        String user = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
        String passwordString = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
        String passcodeString = Objects.requireNonNull(passcode.getEditText()).getText().toString().trim();

        if (fullName.isEmpty()) {
            buongPangalan.getEditText().setError("Full name is required");
            buongPangalan.requestFocus();
            return;
        }

        if (homeAddress.isEmpty()) {
            tirahan.getEditText().setError("Hindi pwedeng blangko ang tirahan");
            tirahan.requestFocus();
            return;
        }

        if (user.isEmpty()) {
            username.getEditText().setError("Hindi pwedeng blangko ang username");
            username.requestFocus();
            return;
        }

//        Check if the current user is already exist

        if (passwordString.isEmpty()) {
            password.getEditText().setError("Hindi pwedeng blangko ang password");
            password.requestFocus();
            return;
        }

        if (passwordString.length() < 6) {
            password.getEditText().setError("Hindi pwede ang 6 pababa na karakter bilang password");
            password.requestFocus();
            return;
        }

        if (passcodeString.isEmpty()) {
            passcode.getEditText().setError("Hindi pwedeng blangko ang passcode");
            passcode.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        signup.setVisibility(View.INVISIBLE);

        verifyUsername(fullName, homeAddress, user, passwordString, passcodeString);

    }

    private void verifyUsername(String fullName, String homeAddress, String user, String passwordString, String passcodeString) {
        databaseReference = firebaseDatabase.getReference("Admin");
        adminDatabase  = new AdminDatabase(AdminRegisterActivity.this, databaseReference);
        final LinkedList<Boolean> isAlreadyMember = new LinkedList<>();
        isAlreadyMember.add(false);

        adminDatabase.getRegisteredUsername(user, username.getEditText(), progressBar, signup, isAlreadyMember);

        handler.postDelayed(() -> {
            if (!isAlreadyMember.get(0)) {
                setupForAccountCreation(fullName, homeAddress, user, passwordString, passcodeString);
            }
        },1000);
    }

    private void setupForAccountCreation(String fullNameText, String homeAddressText, String usernameText, String passwordText, String passCodeText) {
        databaseReference = firebaseDatabase.getReference("Admin");
        adminDatabase = new AdminDatabase(AdminRegisterActivity.this, databaseReference);

        databaseReference.child("barangay_code").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String barangayCode = snapshot.getValue(String.class);

                    String token = FirebaseMessaging.getInstance().getToken().getResult();
                    
                    if (token.isEmpty()) {
                        Toast.makeText(AdminRegisterActivity.this, "Empty token", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (passCodeText.equals(barangayCode)) {
                        adminDatabase.createAdminAccount(fullNameText, homeAddressText, usernameText, passwordText, progressBar, signup, true, token);
                    } else {
                        //Get the DOH Code
                        databaseReference.child("doh_code").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    final String dohCode = dataSnapshot.getValue(String.class);
                                    if (passCodeText.equals(dohCode)) {
                                        adminDatabase.createAdminAccount(fullNameText, homeAddressText, usernameText, passwordText, progressBar, signup, false, token);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        signup.setVisibility(View.VISIBLE);

                                        Objects.requireNonNull(passcode.getEditText()).setError("You have entered incorrect passcode");
                                        passcode.getEditText().requestFocus();
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    signup.setVisibility(View.VISIBLE);

                                    Objects.requireNonNull(passcode.getEditText()).setError("You have entered incorrect passcode");
                                    passcode.getEditText().requestFocus();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}