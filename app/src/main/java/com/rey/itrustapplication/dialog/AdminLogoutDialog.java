package com.rey.itrustapplication.dialog;

import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOffline;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.itrustapplication.AdminLoginActivity;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;

public class AdminLogoutDialog extends Dialog {

    private final AdminSessionManager adminSessionManager;

    public AdminLogoutDialog(@NonNull Context context, AdminSessionManager adminSessionManager) {
        super(context);
        this.adminSessionManager = adminSessionManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Admin Logout");
        setCancelable(false);
        setContentView(R.layout.admin_logout_alert);

        final Button logoutButton = findViewById(R.id.logoutButtonAdmin);
        final Button cancelButton = findViewById(R.id.cancelLogoutAdmin);

        cancelButton.setOnClickListener(view -> dismiss());

        logoutButton.setOnClickListener(view -> logoutTheAdmin());

    }

    private void logoutTheAdmin() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                        .getReference("Admin/" + adminSessionManager.getUsernameSession() + "/token");

        databaseReference.removeValue().addOnCompleteListener(aVoid -> {
            setAvailabilityToOffline(new AdminSessionManager(getContext()).getUsernameSession(), "Admin");
            adminSessionManager.setAdminLogin(false);
            adminSessionManager.setUsernameSession("");
            adminSessionManager.adminClearSession();
            dismiss();
            getContext().startActivity(new Intent(getContext(), AdminLoginActivity.class));
        });

    }
}
