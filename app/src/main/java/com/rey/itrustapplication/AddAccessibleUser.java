package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.itrustapplication.databinding.ActivityAddAccessibleUserBinding;
import com.rey.itrustapplication.helperclasses.AccessibleUserHelperClass;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddAccessibleUser extends AppCompatActivity {

    ActivityAddAccessibleUserBinding activityAddAccessibleUserBinding;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
    private final String[] genderList = new String[] {"Male", "Female"};
    
    private String genderSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddAccessibleUserBinding = ActivityAddAccessibleUserBinding.inflate(getLayoutInflater());
        setContentView(activityAddAccessibleUserBinding.getRoot());

        activityAddAccessibleUserBinding.backButtonAccessibleUserMain.setOnClickListener(view -> finish());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.signup_gender_list, genderList);
        activityAddAccessibleUserBinding.genderAccessible.setAdapter(arrayAdapter);

        activityAddAccessibleUserBinding.genderAccessible.setOnItemClickListener((parent, view, position, id) -> 
            genderSelected = parent.getItemAtPosition(position).toString()
        );

        activityAddAccessibleUserBinding.addNewUser.setOnClickListener(view ->
                validateUser(
                        Objects.requireNonNull(activityAddAccessibleUserBinding.newUser.getEditText()).getText().toString(),
                        Objects.requireNonNull(activityAddAccessibleUserBinding.usersBirthday.getEditText()).getText().toString()));
    }

    private void validateAge() {

        activityAddAccessibleUserBinding.newUser.setError(null);
        activityAddAccessibleUserBinding.newUser.setErrorEnabled(false);

        activityAddAccessibleUserBinding.usersBirthday.setError("Please enter a valid age");
        activityAddAccessibleUserBinding.usersBirthday.requestFocus();
    }

    private void validateUser(String newUserText, String birthday) {
        if (newUserText.isEmpty()) {
            activityAddAccessibleUserBinding.newUser.setError("You need to enter a user first");
            activityAddAccessibleUserBinding.newUser.requestFocus();
            return;
        }

        if (genderSelected.isEmpty()) {
            activityAddAccessibleUserBinding.newUser.setError(null);
            activityAddAccessibleUserBinding.newUser.setErrorEnabled(false);

            Toast.makeText(this, "Please choose users gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (birthday.isEmpty()) {
            validateAge();
            return;
        }

        pushToActivityLog();
        activityAddAccessibleUserBinding.progressBarAddUser.setVisibility(View.VISIBLE);
        activityAddAccessibleUserBinding.addNewUser.setVisibility(View.INVISIBLE);
        removeError();
        addNewUserToDatabase(newUserText, genderSelected, Objects.requireNonNull(activityAddAccessibleUserBinding.usersBirthday.getEditText()).getText().toString());
    }

    private void removeError() {
        activityAddAccessibleUserBinding.newUser.setError(null);
        activityAddAccessibleUserBinding.newUser.setErrorEnabled(false);

        activityAddAccessibleUserBinding.usersBirthday.setError(null);
        activityAddAccessibleUserBinding.usersBirthday.setErrorEnabled(false);
    }

    private void addNewUserToDatabase(String newUserText, String gender, String birthday) {
        final DatabaseReference databaseReference = firebaseDatabase.getReference("AccessibleUsers");

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        
        AccessibleUserHelperClass accessibleUserHelperClass = new AccessibleUserHelperClass(currentDate, newUserText, birthday, gender);
        
        databaseReference.child(newUserText).setValue(accessibleUserHelperClass)
            .addOnCompleteListener(task -> {
                Toast.makeText(this, "New User added", Toast.LENGTH_SHORT).show();
                clearUnderlyingText();
                activityAddAccessibleUserBinding.addNewUser.setVisibility(View.VISIBLE);
                activityAddAccessibleUserBinding.progressBarAddUser.setVisibility(View.GONE);
            });
                
    }

    private void clearUnderlyingText() {

        Objects.requireNonNull(activityAddAccessibleUserBinding.newUser.getEditText()).setText("");

        activityAddAccessibleUserBinding.genderAccessible.setSelected(false);

        Objects.requireNonNull(activityAddAccessibleUserBinding.usersBirthday.getEditText()).setText("");

        activityAddAccessibleUserBinding.newUser.requestFocus();

    }

    private void pushToActivityLog() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("ActivityLogs");

        final String currentUserAdmin = new AdminSessionManager(this).getUsernameSession();
        final String title = currentUserAdmin + " added " +
                Objects.requireNonNull(activityAddAccessibleUserBinding.newUser.getEditText()).getText().toString() +
                " as a new user";

        Calendar calendar = Calendar.getInstance();
        final String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        Date dateDate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh-mm-ss", Locale.getDefault());
        final String time = simpleDateFormat.format(dateDate);

        Map<String, Object> adminDataLog = new HashMap<>();
        adminDataLog.put("title", title);
        adminDataLog.put("date", date);
        adminDataLog.put("time", time);

        databaseReference.push().setValue(adminDataLog);
    }

}