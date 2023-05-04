package com.rey.itrustapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.util.Objects;

public class LoginSignupActivity extends AppCompatActivity {

    Button loginBtn;
    TextView signUpAction, loginUserForgotPassword;
    ImageView backButton;

    private TextInputLayout username, password;
    private ProgressBar progressBar;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
            .getReference("RegularUsers");

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_signup);

        signUpAction = findViewById(R.id.signUpAction);
        loginBtn = findViewById(R.id.loginBtn);

        progressBar = findViewById(R.id.progressBarLogin);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginUserForgotPassword = findViewById(R.id.loginUserForgotPassword);

        sessionManager = new SessionManager(LoginSignupActivity.this);


        signUpAction.setOnClickListener(view -> startActivity(new Intent(LoginSignupActivity.this, SignUp.class)));

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(view -> startActivity(new Intent(LoginSignupActivity.this, MainActivity.class)));

        loginUserForgotPassword.setOnClickListener(event -> startActivity(new Intent(LoginSignupActivity.this, UserForgotPassword.class)));

        loginBtn.setOnClickListener(view -> {
            String cloneUsername = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
            String clonePassword = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

            if (cloneUsername.equals("")) {
                username.getEditText().setError("Paki enter po ng username");
                return;
            }

            if (clonePassword.equals("")) {
                password.getEditText().setError("Paki enter po ng password");
                return;
            }

            if (clonePassword.length() < 6) {
                password.getEditText().setError("Hindi pwede ang 6 pababa na karakter bilang password");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);

            Query query = databaseReference.orderByChild("fullName").equalTo(cloneUsername);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        username.setError(null);
                        username.setErrorEnabled(false);

                        String _password = snapshot.child(cloneUsername).child("password").getValue(String.class);

                        if (password.getEditText().getText().toString().trim().equals(_password)) {
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);

                            updateToken(cloneUsername);
                            sessionManager.setLogin(true);
                            sessionManager.setFullName(cloneUsername);
                            new AdminSessionManager(getApplicationContext()).adminClearSession();

                            startActivity(new Intent(getApplicationContext(), MainUserUi.class));
                            finish();
                        } else {
                            password.setError("Incorrect password");
                            password.requestFocus();
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);

                        }
                    } else {
                        username.setError("Full Name is not yet registered");
                        username.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void updateToken(String fullNameUser) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                .getReference("RegularUsers").child(fullNameUser);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final String token = task.getResult();
                        Log.d("FCM", "token user: " + token);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseReference.child("token").setValue(token);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginSignupActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

    }

}