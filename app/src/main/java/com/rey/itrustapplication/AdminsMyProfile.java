package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOffline;
import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOnline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.databinding.ActivityAdminsMyProfileBinding;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;

public class AdminsMyProfile extends AppCompatActivity {

    ActivityAdminsMyProfileBinding activityAdminsMyProfileBinding;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
    private final DatabaseReference databaseReference = firebaseDatabase.getReference("Admin");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminsMyProfileBinding = ActivityAdminsMyProfileBinding.inflate(getLayoutInflater());
        setContentView(activityAdminsMyProfileBinding.getRoot());

        getAdminData();
    }

    private void getAdminData() {

        final AdminSessionManager adminSessionManager = new AdminSessionManager(AdminsMyProfile.this);

        databaseReference.child(adminSessionManager.getUsernameSession()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("fullName")) {

                    final String _fullName = snapshot.child("fullName").getValue(String.class);
                    final String _accountCreated = snapshot.child("accountCreated").getValue(String.class);
                    final String _username = snapshot.child("username").getValue(String.class);
                    final String _homeAddress = snapshot.child("homeAddress").getValue(String.class);

                    activityAdminsMyProfileBinding.adminNameTextContent.setText(_fullName);
                    activityAdminsMyProfileBinding.adminUsernameTextContent.setText(_username);
                    activityAdminsMyProfileBinding.profileAdminUsername.setText(_username);
                    activityAdminsMyProfileBinding.adminTirahanTextContent.setText(_homeAddress);
                    activityAdminsMyProfileBinding.adminAccountCreatedTextContent.setText(_accountCreated);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        setAvailabilityToOnline(new AdminSessionManager(getApplicationContext()).getUsernameSession(), "Admin");
        super.onResume();
    }

    @Override
    public void onStop() {
        setAvailabilityToOffline(new AdminSessionManager(getApplicationContext()).getUsernameSession(), "Admin");
        super.onStop();
    }

}