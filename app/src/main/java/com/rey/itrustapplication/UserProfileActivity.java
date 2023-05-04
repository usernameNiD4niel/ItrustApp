package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOffline;
import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOnline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.database.UserDatabase;
import com.rey.itrustapplication.databinding.ActivityUserProfileBinding;
import com.rey.itrustapplication.dialog.UserChangePasswordDialog;
import com.rey.itrustapplication.sessionmanager.SessionManager;
import com.rey.itrustapplication.user_avatar.ChangeProfileDialogFragment;


public class UserProfileActivity extends AppCompatActivity {

    ActivityUserProfileBinding activityUserProfileBinding;
    private final DatabaseReference databaseReference = FirebaseDatabase
            .getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
            .getReference("RegularUsers");

    private boolean accountDeleted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUserProfileBinding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activityUserProfileBinding.backButton.setOnClickListener(view -> finish());

        activityUserProfileBinding.deleteAccount.setOnClickListener(event -> verifyToDelete());

        UserDatabase userDatabase = new UserDatabase(UserProfileActivity.this, databaseReference);
        userDatabase.getUserInformation(
                activityUserProfileBinding.fullName,
                activityUserProfileBinding.userFullname,
                activityUserProfileBinding.purok,
                activityUserProfileBinding.phoneNumber,
                activityUserProfileBinding.gender
        );

        activityUserProfileBinding.changePassword.setOnClickListener(view -> updateUserInformation(userDatabase));

        activityUserProfileBinding.profilePicture.setOnClickListener(view ->
                new ChangeProfileDialogFragment(getApplicationContext(), activityUserProfileBinding.profilePicture)
                        .show(getSupportFragmentManager(), "ChangeProfileDialog"));

        displayNewProfilePicture();

        activityUserProfileBinding.swipeActivityUserProfile.setOnRefreshListener(this::displayNewProfilePicture);
    }


    private void displayNewProfilePicture() {
        final String currentUser = new SessionManager(getApplicationContext()).getFullName();

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("UserProfile");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String userProfile = snapshot.child(currentUser).getValue(String.class);
                    if (userProfile == null) {
                        Toast.makeText(UserProfileActivity.this, "No profile fetch", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Drawable vectorDrawable = AppCompatResources.getDrawable(getApplicationContext(), Integer.parseInt(userProfile));

                    activityUserProfileBinding.profilePicture.setImageDrawable(vectorDrawable);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Fetching profile cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        if (activityUserProfileBinding.swipeActivityUserProfile.isRefreshing()) {
            activityUserProfileBinding.swipeActivityUserProfile.setRefreshing(false);
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        displayNewProfilePicture();
    }

    private void verifyToDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attempting to Delete");
        builder.setMessage("Are you sure you want to delete this account? \nIf you delete this account only your conversation with admin will be keep.");
        builder.setPositiveButton("Yes", (dialog, which) -> deleteCurrentUserAccount());

        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteCurrentUserAccount() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/").getReference("RegularUsers");
        databaseReference.child(new SessionManager(this).getFullName()).removeValue().addOnSuccessListener(task -> {
            accountDeleted = true;
            new SessionManager(this).signOutUser();
            startActivity(new Intent(UserProfileActivity.this, LoginSignupActivity.class));
            finish();
            Toast.makeText(this, "Your account is successfully delete!", Toast.LENGTH_SHORT).show();
        });

        Toast.makeText(this, "Deleting your account...", Toast.LENGTH_SHORT).show();
    }

    private void updateUserInformation(UserDatabase userDatabase) {

        UserChangePasswordDialog userChangePasswordDialog = new UserChangePasswordDialog(UserProfileActivity.this, userDatabase);
        userChangePasswordDialog.setCancelable(false);
        userChangePasswordDialog.show();
    }


    @Override
    public void onDestroy() {
        if (!accountDeleted) {
            setAvailabilityToOffline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        setAvailabilityToOnline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        super.onStart();
    }

}