package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOnline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserPasscode extends AppCompatActivity implements View.OnClickListener {

    ProgressBar passcodeProgressBar;
    TextView textOnePasscode, textTwoPasscode, textThreePasscode, textFourPasscode, enterPinCode;
    AppCompatButton oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton, sevenButton, eightButton, nineButton, zeroButton;
    private String pinCode = "";
    CircleImageView remove, profile;
    private int currentPosition = 0;
    private DatabaseReference databaseReference;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_passcode);

        textOnePasscode = findViewById(R.id.text_one_passcode);
        textTwoPasscode = findViewById(R.id.text_two_passcode);
        textThreePasscode = findViewById(R.id.text_three_passcode);
        textFourPasscode = findViewById(R.id.text_four_passcode);
        enterPinCode = findViewById(R.id.enter_pin_passcode);

        oneButton = findViewById(R.id.one_button);
        twoButton = findViewById(R.id.two_button);
        threeButton = findViewById(R.id.three_button);
        fourButton = findViewById(R.id.four_button);
        fiveButton = findViewById(R.id.five_button);
        sixButton = findViewById(R.id.six_button);
        sevenButton = findViewById(R.id.seven_button);
        eightButton = findViewById(R.id.eight_button);
        nineButton = findViewById(R.id.nine_button);
        zeroButton = findViewById(R.id.zero_button);

        remove = findViewById(R.id.remove_button_passcode);
        profile = findViewById(R.id.user_profile_passcode);

        oneButton.setOnClickListener(this);
        twoButton.setOnClickListener(this);
        threeButton.setOnClickListener(this);
        fourButton.setOnClickListener(this);
        fiveButton.setOnClickListener(this);
        sixButton.setOnClickListener(this);
        sevenButton.setOnClickListener(this);
        eightButton.setOnClickListener(this);
        nineButton.setOnClickListener(this);
        zeroButton.setOnClickListener(this);
        remove.setOnClickListener(this);

        passcodeProgressBar = findViewById(R.id.passcodeProgressBar);

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("RegularUsers");
        sessionManager = new SessionManager(this);

        if (sessionManager.getProfileUser().equals("")) {
            getUserProfile(sessionManager.getFullName());
        } else {
            Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(),
                    Integer.parseInt(sessionManager.getProfileUser()));
            profile.setImageDrawable(drawable);
        }

        final TextView forgotPinCodeUserPasscode = findViewById(R.id.forgotPinCodeUserPasscode);

        forgotPinCodeUserPasscode.setOnClickListener(view -> startActivity(new Intent(UserPasscode.this, ForgotPinCode.class)));

    }

    private void getUserProfile(String fullName) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserProfile");
        reference.child(fullName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String dp = snapshot.getValue(String.class);
                    if (dp == null) return;
                    Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), Integer.parseInt(dp));
                    profile.setImageDrawable(drawable);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        setAvailabilityToOnline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        super.onStart();
    }


    @Override
    public void onClick(View view) {
        
        if (currentPosition == 4) {
            Toast.makeText(this, "We're still validating your request", Toast.LENGTH_SHORT).show();
            return;
        }

        if (view.getId() == oneButton.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 1;
        } else if (view.getId() == twoButton.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 2;
        }  else if (view.getId() == threeButton.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 3;
        }  else if (view.getId() == fourButton.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 4;
        }  else if (view.getId() == fiveButton.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 5;
        }  else if (view.getId() == sixButton.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 6;
        }  else if (view.getId() == sevenButton.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 7;
        }  else if (view.getId() == eightButton.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 8;
        }  else if (view.getId() == nineButton.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 9;
        } else if (view.getId() == remove.getId()) {
            if (currentPosition <= 0) return;
            pinCode = (currentPosition == 1) ? "" : pinCode.substring(0, pinCode.length()-1);
            currentPosition--;
            removeTheTint();
        }

        if (currentPosition == 4) {

            passcodeProgressBar.setVisibility(View.VISIBLE);

            databaseReference.child(sessionManager.getFullName()).child("pincode").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        final String pin = snapshot.getValue(String.class);
                        if (pinCode.equals(pin)) {
                            databaseReference.child(sessionManager.getFullName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    passcodeProgressBar.setVisibility(View.GONE);
                                    if (snapshot.hasChild("users_preferences")) {
                                        startActivity(new Intent(UserPasscode.this, MainUserUi.class));
                                    } else {
                                        Intent intent = new Intent(UserPasscode.this, OnboardingOne.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            pinCode = "";
                            passcodeProgressBar.setVisibility(View.GONE);
                            currentPosition = 0;
                            resetTextViewBg();
                            final String pinText = "Invalid pin code, please try again";
                            enterPinCode.setText(pinText);
                            Toast.makeText(UserPasscode.this, "Incorrect passcode", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //check the passcode if valid
        }
    }

    private void resetTextViewBg() {
        textOnePasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
        textTwoPasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
        textThreePasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
        textFourPasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
    }

    private void removeTheTint() {
        switch (currentPosition) {
            case 0:
                if (textOnePasscode.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)))
                    textOnePasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
                break;
            case 1:
                if (textTwoPasscode.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)))
                    textTwoPasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
                break;
            case 2:
                if (textThreePasscode.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)))
                    textThreePasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
                break;
            case 3:
                if (textFourPasscode.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)))
                    textFourPasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
                break;
        }
    }

    private void updateTheTint() {

        switch (currentPosition) {
            case 0:
                if (textOnePasscode.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__))) {
                    textOnePasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)));
                }
                break;
            case 1:
                if (textTwoPasscode.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)))
                    textTwoPasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)));
                break;
            case 2:
                if (textThreePasscode.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)))
                    textThreePasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)));
                break;
            default:
                if (textFourPasscode.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)))
                    textFourPasscode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)));
                break;
        }
    }
}