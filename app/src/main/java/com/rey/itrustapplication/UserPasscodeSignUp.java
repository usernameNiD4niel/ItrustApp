package com.rey.itrustapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rey.itrustapplication.databinding.ActivityUserPasscodeSignUpBinding;

public class UserPasscodeSignUp extends AppCompatActivity implements View.OnClickListener {

    ActivityUserPasscodeSignUpBinding activityUserPasscodeSignUpBinding;

    private int currentPosition = 0;
    private String pinCode = "", retypedPin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserPasscodeSignUpBinding = ActivityUserPasscodeSignUpBinding.inflate(getLayoutInflater());
        setContentView(activityUserPasscodeSignUpBinding.getRoot());

    }

    @Override
    public void onClick(View view) {

        if (currentPosition == 4) {
            Toast.makeText(this, "We're still validating your request", Toast.LENGTH_SHORT).show();
            return;
        }

        if (view.getId() == activityUserPasscodeSignUpBinding.oneButtonSignup.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 1;
        } else if (view.getId() == activityUserPasscodeSignUpBinding.twoButtonSignup.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 2;
        }  else if (view.getId() == activityUserPasscodeSignUpBinding.threeButtonSignup.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 3;
        }  else if (view.getId() == activityUserPasscodeSignUpBinding.fourButtonSignup.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 4;
        }  else if (view.getId() == activityUserPasscodeSignUpBinding.fiveButtonSignup.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 5;
        }  else if (view.getId() == activityUserPasscodeSignUpBinding.sixButtonSignup.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 6;
        }  else if (view.getId() == activityUserPasscodeSignUpBinding.sevenButtonSignup.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 7;
        }  else if (view.getId() == activityUserPasscodeSignUpBinding.eightButtonSignup.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 8;
        }  else if (view.getId() == activityUserPasscodeSignUpBinding.nineButtonSignup.getId()) {
            updateTheTint();
            currentPosition++;
            pinCode += "" + 9;
        } else if (view.getId() == activityUserPasscodeSignUpBinding.removeButtonPasscodeSignup.getId()) {
            if (currentPosition == 0) return;
            currentPosition--;
            removeTheTint();
            pinCode = (currentPosition == 1) ? "" : pinCode.substring(0, pinCode.length()-1);
        }

        if (currentPosition == 4) {

            if (retypedPin.isEmpty()) {
                final String retypeYourPin = "Retype your pin code";
                activityUserPasscodeSignUpBinding.enterPinPasscodeSignup.setText(retypeYourPin);
                retypedPin = pinCode;
                resetTextViewBg();
                pinCode = "";
                currentPosition = 0;
            } else {
                if (retypedPin.equals(pinCode)) {
                    Intent intent = new Intent(UserPasscodeSignUp.this, OnboardingOne.class);
                    startActivity(intent);
                    finish();
                } else {
                    final String incorrect = "Passcode does not match please try again";
                    activityUserPasscodeSignUpBinding.enterPinPasscodeSignup.setText(incorrect);
                    retypedPin = pinCode;
                    resetTextViewBg();
                    pinCode = "";
                    currentPosition = 0;
                }
            }

        }
    }

    private void resetTextViewBg() {
        activityUserPasscodeSignUpBinding.textOnePasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
        activityUserPasscodeSignUpBinding.textTwoPasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
        activityUserPasscodeSignUpBinding.textThreePasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
        activityUserPasscodeSignUpBinding.textFourPasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
    }

    private void removeTheTint() {
        switch (currentPosition) {
            case 0:
                if (activityUserPasscodeSignUpBinding.textOnePasscodeSignup.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)))
                    activityUserPasscodeSignUpBinding.textOnePasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
                break;
            case 1:
                if (activityUserPasscodeSignUpBinding.textTwoPasscodeSignup.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)))
                    activityUserPasscodeSignUpBinding.textTwoPasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
                break;
            case 2:
                if (activityUserPasscodeSignUpBinding.textThreePasscodeSignup.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)))
                    activityUserPasscodeSignUpBinding.textThreePasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
                break;
            case 3:
                if (activityUserPasscodeSignUpBinding.textFourPasscodeSignup.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)))
                    activityUserPasscodeSignUpBinding.textFourPasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)));
                break;
        }
    }

    private void updateTheTint() {

        switch (currentPosition) {
            case 0:
                if (activityUserPasscodeSignUpBinding.textOnePasscodeSignup.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__))) {
                    activityUserPasscodeSignUpBinding.textOnePasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)));
                    Toast.makeText(this, "current white going to blue", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if (activityUserPasscodeSignUpBinding.textTwoPasscodeSignup.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)))
                    activityUserPasscodeSignUpBinding.textTwoPasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)));
                break;
            case 2:
                if (activityUserPasscodeSignUpBinding.textThreePasscodeSignup.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)))
                    activityUserPasscodeSignUpBinding.textThreePasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)));
                break;
            default:
                if (activityUserPasscodeSignUpBinding.textFourPasscodeSignup.getBackgroundTintList() == ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.dark_shadow__)))
                    activityUserPasscodeSignUpBinding.textFourPasscodeSignup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.main_color)));
                break;
        }
    }
}