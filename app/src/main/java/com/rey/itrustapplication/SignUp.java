package com.rey.itrustapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rey.itrustapplication.database.UserDatabase;
import com.rey.itrustapplication.databinding.ActivitySignupBinding;

import java.util.LinkedList;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    ActivitySignupBinding activitySignUpBinding;

    private final String[] genderList = new String[] {"Male", "Female"};

    private String genderSelected = "";
    private String fullname, purok, phoneNumber, password, birthDay;
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.signup_gender_list, genderList);
        activitySignUpBinding.gender.setAdapter(arrayAdapter);

        activitySignUpBinding.gender.setOnItemClickListener((parent, view, position, id) -> {
            genderSelected = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, genderSelected, Toast.LENGTH_SHORT).show();
        });

        activitySignUpBinding.backButton.setOnClickListener(view -> finish());

        activitySignUpBinding.usersAgreement.setOnClickListener(view -> {
            // load new activity that shows user agreement
        });

        activitySignUpBinding.sendLogin.setOnClickListener(view -> finish());

        activitySignUpBinding.checkBox.setOnClickListener(click -> isChecked = activitySignUpBinding.checkBox.isChecked());

        activitySignUpBinding.registerButton.setOnClickListener(view -> {

            fullname = Objects.requireNonNull(activitySignUpBinding.fullName.getEditText()).getText().toString();
            birthDay = Objects.requireNonNull(activitySignUpBinding.birthday.getEditText()).getText().toString();
            phoneNumber = Objects.requireNonNull(activitySignUpBinding.phoneNumber.getEditText()).getText().toString().trim();
            purok = Objects.requireNonNull(activitySignUpBinding.purok.getEditText()).getText().toString();
            password = Objects.requireNonNull(activitySignUpBinding.password.getEditText()).getText().toString();
            String confirmPassword = Objects.requireNonNull(activitySignUpBinding.confirmPassword.getEditText()).getText().toString();

            if (fullname.isEmpty() || phoneNumber.isEmpty() || purok.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || birthDay.isEmpty()) {
                Toast.makeText(this, "Please enter a correct credential.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phoneNumber.length() != 11 || (!phoneNumber.contains("09"))) {
                activitySignUpBinding.phoneNumber.getEditText().setError("Your phone number is not valid");
                activitySignUpBinding.progressBar.setVisibility(View.INVISIBLE);
                activitySignUpBinding.registerButton.setVisibility(View.VISIBLE);
                return;
            }

            if (password.length() < 6) {
                activitySignUpBinding.confirmPassword.setError("Only 6 characters and up of password is allowed");
                activitySignUpBinding.progressBar.setVisibility(View.INVISIBLE);
                activitySignUpBinding.registerButton.setVisibility(View.VISIBLE);
                return;
            }

            if (!(password.equals(confirmPassword))) {
                activitySignUpBinding.confirmPassword.setError("Your confirm password not match to your password");
                activitySignUpBinding.progressBar.setVisibility(View.INVISIBLE);
                activitySignUpBinding.registerButton.setVisibility(View.VISIBLE);
                return;
            }

            if (!isChecked) {
                activitySignUpBinding.progressBar.setVisibility(View.INVISIBLE);
                activitySignUpBinding.registerButton.setVisibility(View.VISIBLE);
                Toast.makeText(SignUp.this, "Please check users agreement", Toast.LENGTH_SHORT).show();
                return;
            }

            if (genderSelected.equals("")) {
                activitySignUpBinding.gender.setError("Select your gender");
                activitySignUpBinding.gender.requestFocus();
                return;
            }

            setLoadingProgress(true);
            removeErrorSet();

            validateIfAccessibleUser(fullname, birthDay);

        });
    }

    private void removeErrorSet() {

        activitySignUpBinding.fullName.setError(null);
        activitySignUpBinding.fullName.setErrorEnabled(false);

        activitySignUpBinding.birthday.setError(null);
        activitySignUpBinding.birthday.setErrorEnabled(false);

        activitySignUpBinding.phoneNumber.setError(null);
        activitySignUpBinding.phoneNumber.setErrorEnabled(false);

        activitySignUpBinding.purok.setError(null);
        activitySignUpBinding.purok.setErrorEnabled(false);

        activitySignUpBinding.password.setError(null);
        activitySignUpBinding.password.setErrorEnabled(false);

        activitySignUpBinding.confirmPassword.setError(null);
        activitySignUpBinding.confirmPassword.setErrorEnabled(false);

    }

    private void validateIfAccessibleUser(String fullName, String birthday) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                .getReference("AccessibleUsers");

        LinkedList<Boolean> isAccessible = new LinkedList<>();
        isAccessible.add(false);

        UserDatabase userDatabase = new UserDatabase(SignUp.this, databaseReference);
        userDatabase.validateFullNameIfAccessible(fullName, birthday, genderSelected, isAccessible);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (isAccessible.get(0)) {
                removeErrorSet();
                checkIfUserAlreadyHasAccount();
            }
            else {
                Toast.makeText(this, "Your credentials might not registered by the admins", Toast.LENGTH_SHORT).show();
                setLoadingProgress(false);
            }
        }, 1500);
    }

    private void checkIfUserAlreadyHasAccount() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                .getReference("RegularUsers");

        UserDatabase userDatabase = new UserDatabase(SignUp.this, databaseReference);

        Log.d("Daniel", "checkIfUserAlreadyHasAccount: checking...");
        final LinkedList<Boolean> hasAlreadyAnAccount = new LinkedList<>();
        hasAlreadyAnAccount.add(false);

        userDatabase.checkIfUserHasAccount(fullname, hasAlreadyAnAccount);

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            setLoadingProgress(false);

            if (hasAlreadyAnAccount.get(0)) {
                Objects.requireNonNull(activitySignUpBinding.fullName.getEditText()).setError("You have already registered your account");
                activitySignUpBinding.fullName.requestFocus();
            } else {

                FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Daniel", "checkIfUserAlreadyHasAccount: creating...");
                            userDatabase.createUserAccount(fullname, purok, phoneNumber, genderSelected, birthDay, password,task.getResult());
                        }
                    });
            }

        }, 1000);
    }

    private void setLoadingProgress(boolean hasToLoad) {
        if (hasToLoad) {
            activitySignUpBinding.progressBar.setVisibility(View.VISIBLE);
            activitySignUpBinding.registerButton.setVisibility(View.INVISIBLE);
        } else {
            activitySignUpBinding.progressBar.setVisibility(View.GONE);
            activitySignUpBinding.registerButton.setVisibility(View.VISIBLE);
        }
    }

}