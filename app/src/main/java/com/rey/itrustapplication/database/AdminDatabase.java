package com.rey.itrustapplication.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rey.itrustapplication.AdminDashboardActivity;
import com.rey.itrustapplication.AdminLoginActivity;
import com.rey.itrustapplication.databinding.ActivityAdminLoginBinding;
import com.rey.itrustapplication.doh.DOHMainActivity;
import com.rey.itrustapplication.helperclasses.AdminCreateAccount;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class AdminDatabase {

    private final Context context;
    private final DatabaseReference databaseReference;
    private final AdminSessionManager sessionManager;

    public AdminDatabase(Context context, DatabaseReference databaseReference) {
        this.context = context;
        this.databaseReference = databaseReference;

        sessionManager = new AdminSessionManager(context);
    }

    public void createAdminAccount(String fullName, String homeAddress, String username,
                                   String password, ProgressBar progressBar, Button signUpButton,
                                   boolean isAdmin, String token) {

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        AdminCreateAccount adminCreateAccount = new AdminCreateAccount(fullName, homeAddress, username, password, currentDate, token);

        String ifDoh;
        if (!isAdmin) {
            ifDoh = "DOH/";
        } else {
            ifDoh = "";
        }

        databaseReference.child(ifDoh + username).setValue(adminCreateAccount).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            signUpButton.setVisibility(View.VISIBLE);

            if (isAdmin) {
                sessionManager.setAdminLogin(true);
                sessionManager.setIsDoh(false);
            } else {
                sessionManager.setIsDoh(true);
                sessionManager.setAdminLogin(false);
            }
            sessionManager.setUsernameSession(username);
            new SessionManager(context).signOutUser();

            Toast.makeText(context, "You have successfully created your account!", Toast.LENGTH_SHORT).show();

            context.startActivity(new Intent(context, AdminLoginActivity.class));
            ((Activity)context).finish();
        }).addOnFailureListener(exception -> Toast.makeText(context, "Can't create your account because, " + exception.getMessage(), Toast.LENGTH_SHORT).show());

    }

    public void getBarangayPassCode(String barangayPasscode, ActivityAdminLoginBinding adminLoginBinding) {

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isCorrect = barangayPasscode.equals(dataSnapshot.getValue(String.class));
                    if (isCorrect) {
                        adminLoginBinding.passcodeAdminLogin.setError(null);
                        adminLoginBinding.passcodeAdminLogin.setErrorEnabled(false);
                        Log.d("TAG 2", "onDataChange: admin");
                        checkIfAuthorizeAdmin(adminLoginBinding.usernameAdminLogin, adminLoginBinding.passwordAdminLogin, true, adminLoginBinding);
                    } else {
                        databaseReference.child("doh_code").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    final String dohCode = snapshot.getValue(String.class);
                                    if (barangayPasscode.equals(dohCode)) {
                                        Log.d("TAG 2", "onDataChange: not admin but doh");
                                        adminLoginBinding.passcodeAdminLogin.setError(null);
                                        adminLoginBinding.passcodeAdminLogin.setErrorEnabled(false);
                                        checkIfAuthorizeAdmin(adminLoginBinding.usernameAdminLogin, adminLoginBinding.passwordAdminLogin, false, adminLoginBinding);
                                        return;
                                    }
                                }
                                adminLoginBinding.passcodeAdminLogin.setError("Incorrect Barangay Passcode");
                                adminLoginBinding.passcodeAdminLogin.requestFocus();
                                setLoadingProgress(adminLoginBinding);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                } else {
                    Toast.makeText(context, "Incorrect barangay passcode", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.child("barangay_code").addListenerForSingleValueEvent(eventListener);

    }

    private void setLoadingProgress(ActivityAdminLoginBinding adminLoginBinding) {
        adminLoginBinding.progressbarLogin.setVisibility(View.GONE);
        adminLoginBinding.login.setVisibility(View.VISIBLE);

    }

    public void getRegisteredUsername(String currentUser, EditText usernameField, ProgressBar progressBar, Button registerButton,
                                      LinkedList<Boolean> isAlreadyMember) {

        databaseReference.child(currentUser).child("fullName").get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        if (currentUser.equals(dataSnapshot.getValue(String.class))) {
                            usernameField.setError("This username is already taken");
                            usernameField.requestFocus();
                            progressBar.setVisibility(View.GONE);
                            registerButton.setVisibility(View.VISIBLE);
                            isAlreadyMember.set(0, true);
                        } else {
                            isAlreadyMember.set(0, false);
                        }
                    } else {
                        isAlreadyMember.set(0, false);
                    }
                });
    }

    public void checkIfAuthorizeAdmin(TextInputLayout username, TextInputLayout password, boolean isAdmin, ActivityAdminLoginBinding adminLoginBinding) {

        final EditText usernameEditText = username.getEditText();
        final EditText passwordEditText = password.getEditText();

        String ifDoh;

        if (!isAdmin) {
            ifDoh = "DOH/";
        } else {
            ifDoh = "";
        }

        if (usernameEditText == null || passwordEditText == null) {
            Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show();
            setLoadingProgress(adminLoginBinding);
            return;
        }

        final String _username = usernameEditText.getText().toString().trim();
        final String _password = passwordEditText.getText().toString().trim();

        Query query = databaseReference.child(ifDoh).orderByChild("username").equalTo(_username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.exists()) {
                     username.setError(null);
                     username.setErrorEnabled(false);

                     String _passwordText = snapshot.child(_username).child("password").getValue(String.class);

                     if (_password.equals(_passwordText)) {

                         FirebaseMessaging.getInstance().getToken()
                             .addOnCompleteListener(task -> {
                                 if (task.isSuccessful()) {
                                     final String token = task.getResult();
                                     updateToken(token, _username, isAdmin, adminLoginBinding);
                                 }
                             });

                     } else {
                         password.setError("Incorrect password");
                         password.requestFocus();
                         setLoadingProgress(adminLoginBinding);

                     }
                 } else {
                     username.setError("Username is not yet registered");
                     username.requestFocus();
                     setLoadingProgress(adminLoginBinding);
                 }
             }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error Database Admin, " + error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void updateToken(String token, String _username, boolean isAdmin, ActivityAdminLoginBinding adminLoginBinding) {

        if (token == null) {
            Toast.makeText(context, "can't receive notification", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                .getReference("Admin");
        databaseReference.child(_username).child("token").setValue(token).addOnCompleteListener(aVoid ->{
            if (isAdmin) {
                sessionManager.setAdminLogin(true);
                sessionManager.setIsDoh(false);
            } else {
                sessionManager.setIsDoh(true);
                sessionManager.setAdminLogin(false);
            }
            sessionManager.setUsernameSession(_username);

            new SessionManager(context).signOutUser();

            setLoadingProgress(adminLoginBinding);

            if (!isAdmin) {
                context.startActivity(new Intent(context, DOHMainActivity.class));
                ((Activity) context).finish();
                return;
            }
            context.startActivity(new Intent(context, AdminDashboardActivity.class));
            ((Activity) context).finish();
        });
    }

}