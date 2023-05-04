package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOffline;
import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOnline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rey.itrustapplication.databinding.ActivityUserAboutUsBinding;
import com.rey.itrustapplication.sessionmanager.SessionManager;

public class UserAboutUs extends AppCompatActivity {

    ActivityUserAboutUsBinding activityUserAboutUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserAboutUsBinding = ActivityUserAboutUsBinding.inflate(getLayoutInflater());
        setContentView(activityUserAboutUsBinding.getRoot());

        activityUserAboutUsBinding.backButtonAboutApp.setOnClickListener(view -> finish());

    }


    @Override
    public void onDestroy() {
        setAvailabilityToOffline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        setAvailabilityToOnline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        super.onStart();
    }

    @Override
    public void onResume() {
        setAvailabilityToOnline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        super.onResume();
    }

    @Override
    public void onStop() {
        setAvailabilityToOffline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        super.onStop();
    }

}