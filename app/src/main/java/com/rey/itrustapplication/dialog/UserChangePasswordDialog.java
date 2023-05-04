package com.rey.itrustapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.database.UserDatabase;

import java.util.LinkedList;
import java.util.Objects;

public class UserChangePasswordDialog extends Dialog {

    private final UserDatabase userDatabase;

    public UserChangePasswordDialog(@NonNull Context context, UserDatabase userDatabase) {
        super(context);
        this.userDatabase = userDatabase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Admin Logout");
        setContentView(R.layout.user_profile_changepassword_dialog);

        final TextInputLayout newPassword = findViewById(R.id.newPassword);
        final TextInputLayout currentPassword = findViewById(R.id.currentPassword);
        final Button changePasswordButton = findViewById(R.id.changePasswordButton);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final Button cancelButtonChangePass = findViewById(R.id.cancelButtonChangePass);

        cancelButtonChangePass.setOnClickListener(event -> dismiss());

        changePasswordButton.setOnClickListener(view -> validateUserInputs(newPassword, currentPassword, progressBar, changePasswordButton));

    }

    private void validateUserInputs(TextInputLayout newPassword, TextInputLayout currentPassword, ProgressBar progressBar, Button changeButton) {

        final String _newPassword = Objects.requireNonNull(newPassword.getEditText()).getText().toString().trim();
        final String _currentPassword = Objects.requireNonNull(currentPassword.getEditText()).getText().toString().trim();

        if (_newPassword.isEmpty() || _newPassword.length() < 6) {
            newPassword.setError("Hindi maaaring 6 karakter lamang ang password");
            newPassword.requestFocus();

            currentPassword.setError(null);
            currentPassword.setErrorEnabled(false);
            return;
        }

        if (_currentPassword.isEmpty()) {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);

            currentPassword.setError("Hindi maaring walang current password");
            currentPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        changeButton.setVisibility(View.INVISIBLE);


        checkIfCurrentPasswordCorrect(_newPassword, _currentPassword, currentPassword,  progressBar, changeButton);

        dismiss();

    }

    private void checkIfCurrentPasswordCorrect(String _newPassword, String _currentPassword, TextInputLayout currentPassword,
                                               ProgressBar progressBar, Button changeButton) {

        final LinkedList<Boolean> isCorrectPassword = new LinkedList<>();
        isCorrectPassword.add(false);

        userDatabase.getPasswordOfCurrentUser(_currentPassword, isCorrectPassword);

        if (isCorrectPassword.get(0)) {
            userDatabase.updateUserPassword(_newPassword, this);
            return;
        }

        currentPassword.setError("Mali ang iyong password");
        progressBar.setVisibility(View.GONE);
        changeButton.setVisibility(View.VISIBLE);
    }


}
