package com.rey.itrustapplication.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.OnboardingOne;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.UserPasscode;
import com.rey.itrustapplication.dialog.UserChangePasswordDialog;
import com.rey.itrustapplication.helperclasses.UserCreateAccount;
import com.rey.itrustapplication.helperclasses.UserMagpakonsultaHelperClass;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;
import com.rey.itrustapplication.sessionmanager.SessionManager;
import com.rey.itrustapplication.UserPasscodeSignUp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class UserDatabase {

    DatabaseReference databaseReference;
    private final SessionManager sessionManager;
    private final Context context;

    public UserDatabase(Context context, DatabaseReference databaseReference){
        this.databaseReference = databaseReference;
        this.context = context;
        sessionManager = new SessionManager(context.getApplicationContext());
    }

    public void getUserInformation(TextView fullname, TextView headerName, TextView purok, TextView phoneNumber, TextView gender) {

        final String _fullName = sessionManager.getFullName();

        Query query = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                .getReference("RegularUsers").orderByChild("fullName").equalTo(_fullName);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String phoneNumberText = snapshot.child(_fullName).child("phoneNumber").getValue(String.class);
                    String purokText = snapshot.child(_fullName).child("purok").getValue(String.class);
                    String genderText = snapshot.child(_fullName).child("gender").getValue(String.class);

                    phoneNumber.setText(phoneNumberText);
                    fullname.setText(_fullName);
                    purok.setText(purokText);
                    gender.setText(genderText);
                    headerName.setText(_fullName);
                } else {
                    Toast.makeText(context, "Data doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUserPassword(String newPassword, UserChangePasswordDialog userChangePasswordDialog) {

        HashMap<String, Object> newPasswordObject = new HashMap<>();
        newPasswordObject.put("password", newPassword);

        databaseReference.child(sessionManager.getFullName()).child("password")
                .updateChildren(newPasswordObject).addOnCompleteListener(task -> {
                    userChangePasswordDialog.dismiss();
                    Toast.makeText(context, "You have successfully updated your new password", Toast.LENGTH_SHORT).show();
                });
    }

    public void updatePreferences(ArrayList<String> preferencesList) {

        HashMap<String, Object> updatedPreferences = new HashMap<>();
        updatedPreferences.put("answer_one", preferencesList.get(0));
        updatedPreferences.put("answer_two", preferencesList.get(1));
        updatedPreferences.put("answer_three", preferencesList.get(2));
        updatedPreferences.put("answer_four", preferencesList.get(3));
        updatedPreferences.put("answer_five", preferencesList.get(4));

        databaseReference.child(sessionManager.getFullName()).child("user_preferences")
                .updateChildren(updatedPreferences).addOnCompleteListener(task -> {
            Toast.makeText(context, "You have successfully updated your preferences", Toast.LENGTH_SHORT).show();
        });
    }

    public void addPreferences(ArrayList<String> preferenceList) {

        databaseReference.child("RegularUsers").child(sessionManager.getFullName()).child("user_preferences")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        databaseReference.child("RegularUsers").child(sessionManager.getFullName()).child("user_preferences")
                                .child("answer_one").setValue(preferenceList.get(0));

                        databaseReference.child("RegularUsers").child(sessionManager.getFullName()).child("user_preferences")
                                .child("answer_two").setValue(preferenceList.get(1));

                        databaseReference.child("RegularUsers").child(sessionManager.getFullName()).child("user_preferences")
                                .child("answer_three").setValue(preferenceList.get(2));

                        databaseReference.child("RegularUsers").child(sessionManager.getFullName()).child("user_preferences")
                                .child("answer_four").setValue(preferenceList.get(3));

                        databaseReference.child("RegularUsers").child(sessionManager.getFullName()).child("user_preferences")
                                .child("answer_five").setValue(preferenceList.get(4));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void createUserPinCode(String pincode, String gender) {

        databaseReference.child(sessionManager.getFullName()).child("pincode").setValue(pincode)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "You have successfully created your pin", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, OnboardingOne.class);
                        context.startActivity(intent);
                    }
                });
    }

    public void hasUserPreferences(LinkedList<Boolean> isAlreadyAnswer) {

        databaseReference.child(sessionManager.getFullName()).child("user_preferences").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                isAlreadyAnswer.set(0, true);
            } else {
                isAlreadyAnswer.set(0, false);
            }
        }).addOnFailureListener(failed -> isAlreadyAnswer.set(0, false));

    }

    public void checkIfUserHasAccount(String fullName, LinkedList<Boolean> hasAnAccount) {

        databaseReference.child(fullName).get().addOnSuccessListener(runnable -> {
            if (runnable.exists()) {
                hasAnAccount.set(0, true);
            }else {
                hasAnAccount.set(0, false);
            }
        });

    }

    public void createUserAccount(String fullName, String purok, String phoneNumber, String gender, String birthday, String password, String token) {

        UserCreateAccount userCreateAccount = new UserCreateAccount(fullName, birthday, purok, phoneNumber, gender, password, token);

        databaseReference.child(fullName).setValue(userCreateAccount).addOnCompleteListener(task -> {

            sessionManager.setLogin(true);
            sessionManager.setFullName(fullName);
            new AdminSessionManager(context).adminClearSession();

            context.startActivity(new Intent(context, UserPasscodeSignUp.class));
            Toast.makeText(context, "You have successfully created your account!", Toast.LENGTH_SHORT).show();
        });

    }

    public void fetchUserCode(Activity activity) {

        databaseReference.child(sessionManager.getFullName()).child("pincode").get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        context.startActivity(new Intent(context, UserPasscode.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        activity.finish();
                    } else {
                        context.startActivity(new Intent(context, UserPasscodeSignUp.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        activity.finish();
                    }
                });

    }

    public void getUserPinCode(LinkedList<String> getPassCode) {

        databaseReference.child(sessionManager.getFullName()).child("pincode").get()
            .addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    getPassCode.set(0, dataSnapshot.getValue(String.class));
                } else {
                    getPassCode.set(0, "none");

                }
            }).addOnFailureListener(failed -> getPassCode.set(0,"none"));

    }

    public void getPasswordOfCurrentUser(String password, LinkedList<Boolean> isCorrectPassword) {
        databaseReference.child(sessionManager.getFullName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("password")) {
                            if (password.equals(snapshot.child("password").getValue(String.class)))
                                isCorrectPassword.set(0, true);
                            else {
                                isCorrectPassword.set(0, false);
                            }
                        } else {
                            Toast.makeText(context, "Mali ang child, getPasswordOfCurrentUser", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void addMagpakonsultaStatus(UserMagpakonsultaHelperClass userMagpakonsultaHelperClass,
                                       Button requestButton, ProgressBar progressBar) {

        databaseReference.child(sessionManager.getFullName()).setValue(userMagpakonsultaHelperClass)
                .addOnCompleteListener(task -> {

                    requestButton.setText(R.string.pending_request);
                    requestButton.setTextColor(context.getResources().getColor(R.color.pending_request_color));
                    requestButton.setBackgroundColor(context.getResources().getColor(R.color.transparent_color));
                    requestButton.setEnabled(false);
                    requestButton.setVisibility(View.VISIBLE);

                    progressBar.setVisibility(View.GONE);
                });

    }

    public void checkIfRequestIsExpired(Button userRequest, String day, int hour, int minutes, String path, ProgressBar progressBar,
                                        String status) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
        final DatabaseReference reference = firebaseDatabase.getReference(path);

        reference.child(sessionManager.getFullName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final int _hour = Integer.parseInt(snapshot.child("hour").getValue(Long.class).toString());
                    final int _minutes = Integer.parseInt(snapshot.child("minute").getValue(Long.class).toString());
                    final String _day = snapshot.child("day").getValue(String.class);

//                    if (!day.equals(_day) || _hour > hour || _minutes > minutes) {
//                        setButtonToNormalState(userRequest, progressBar);
////                        removeMagpakonsultaStatus(status, firebaseDatabase);
//                        return;
//                    }

                    switch (path) {
                        case "PendingRequest":
                            userRequest.setText(context.getResources().getText(R.string.pending_request));
                            userRequest.setTextColor(context.getResources().getColor(R.color.pending_request_color));
                            userRequest.setBackgroundColor(context.getResources().getColor(R.color.transparent_color));
                            userRequest.setEnabled(false);
                            break;
                        case "ApproveRequest":
                            userRequest.setText(context.getResources().getText(R.string.approved_request));
                            userRequest.setTextColor(context.getResources().getColor(R.color.successful_color));
                            userRequest.setBackgroundColor(context.getResources().getColor(R.color.transparent_color));
                            userRequest.setEnabled(false);
                            break;
                        case "DeclineRequest":
                            userRequest.setText(context.getResources().getText(R.string.declined_request));
                            userRequest.setTextColor(context.getResources().getColor(R.color.error_color));
                            userRequest.setBackgroundColor(context.getResources().getColor(R.color.transparent_color));
                            userRequest.setEnabled(false);
                            break;
                    }
                }
                progressBar.setVisibility(View.GONE);
                userRequest.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setButtonToNormalState(Button userRequest, ProgressBar progressBar) {

        userRequest.setText(context.getResources().getText(R.string.magsend_ng_request));
        userRequest.setTextColor(context.getResources().getColor(R.color.white));
        userRequest.setBackgroundColor(context.getResources().getColor(R.color.main_color));
        userRequest.setEnabled(true);
        userRequest.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.GONE);

    }

    public void getMagpakonsultaStatus(Button userRequest, String day, int hour, int minute, ProgressBar progressBar) {

        databaseReference.child(sessionManager.getFullName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("status")) {
                    final String status = snapshot.child("status").getValue(String.class);
                    if ("Pending Request".equals(status)) {

                        checkIfRequestIsExpired(userRequest, day, hour, minute, "PendingRequest", progressBar, status);

                    } else if ("Approve Request".equals(status)) {

                        checkIfRequestIsExpired(userRequest, day, hour, minute, "ApproveRequest", progressBar, status);

                    } else if ("Decline Request".equals(status)) {

                        checkIfRequestIsExpired(userRequest, day, hour, minute, "DeclineRequest", progressBar, status);
                    }
                } else {
//                    Kapag ang user ay walang request, babalik sa normal state si button
                    setButtonToNormalState(userRequest, progressBar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void validateFullNameIfAccessible(String fullName, String birthday, String gender, LinkedList<Boolean> isAccessible) {

        databaseReference.child(fullName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String _fullName = snapshot.child("fullName").getValue(String.class);
                    final String _birthday = snapshot.child("birthday").getValue(String.class);
                    final String _gender = snapshot.child("gender").getValue(String.class);

                    if (fullName.equals(_fullName)) {

                        if (birthday.equals(_birthday)) {

                            if (gender.equals(_gender)) {
                                isAccessible.set(0, true);
                                return;

                            } else {
                                Toast.makeText(context, "Incorrect gender", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Incorrect birthday", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, "Incorrect full name", Toast.LENGTH_SHORT).show();
                    }
                    isAccessible.set(0, false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*databaseReference.child(fullName).child("fullName").get()
            .addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    if (fullName.equals(dataSnapshot.getValue(String.class))) {
                        isAccessible.set(0, true);
                    } else {
                        isAccessible.set(0, false);
                    }
                } else {
                    isAccessible.set(0, false);
                }
            });*/
    }

}
