package com.rey.itrustapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.itrustapplication.sessionmanager.SessionManager;
import com.rey.itrustapplication.youtube_player.YoutubePlayerFragment;


public class MainUserUi extends AppCompatActivity {

    BottomNavigationView navigationBottom;

    YoutubePlayerFragment userHomeFragment = new YoutubePlayerFragment();
    UserChatbotFragment userChatbotFragment = new UserChatbotFragment();
    UserMoreFragment userMoreFragment = new UserMoreFragment();
    SessionManager sessionManager;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_ui);

        navigationBottom = findViewById(R.id.navigationBottom);

        sessionManager = new SessionManager(getApplicationContext());

        databaseReference = FirebaseDatabase.getInstance().getReference("RegularUsers/" + sessionManager.getFullName());

        getSupportFragmentManager().beginTransaction().replace(R.id.container, userHomeFragment).commit();

        navigationBottom.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.user_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, userHomeFragment).commit();
                return true;
            }

            if (item.getItemId() == R.id.user_chatbot) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, userChatbotFragment).commit();
                return true;
            }

            if (item.getItemId() == R.id.user_more) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, userMoreFragment).commit();
                return true;
            }

            return false;
        });

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout as User");
        builder.setMessage("Are you sure you want to log out your account?");
        builder.setPositiveButton("Yes", (dialog, which) ->
                databaseReference.child("availability").setValue("offline").addOnCompleteListener(runnable -> {
                    Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
                    deleteExistingUserToken();
        }));

        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteExistingUserToken() {

        databaseReference.child("token").removeValue().addOnCompleteListener(aVoid -> {
            if (aVoid.isSuccessful()) {
                sessionManager.signOutUser();
                startActivity(new Intent(MainUserUi.this, LoginSignupActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });
    }
}
