package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.rey.itrustapplication.medicine_inventory.user.BarangayMedicine;
import com.rey.itrustapplication.sessionmanager.SessionManager;


public class UserMoreFragment extends Fragment {

    private SessionManager sessionManager;
    private AlertDialog alertDialog;

    public UserMoreFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewContainer = inflater.inflate(R.layout.fragment_user_more, container, false);

        final Button logout = viewContainer.findViewById(R.id.logoutButton);
        final Button profileButton = viewContainer.findViewById(R.id.profileButton);
        final Button helpButton = viewContainer.findViewById(R.id.tulongButton);
        final Button medicineInventoryButton = viewContainer.findViewById(R.id.medicineInventoryButton);

        sessionManager = new SessionManager(viewContainer.getContext());

        logout.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Logout as User");
            builder.setMessage("Are you sure you want to log out your account?");
            builder.setPositiveButton("Yes", (dialog, which) -> setAvailabilityToOffline(sessionManager.getFullName()));

            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

            alertDialog = builder.create();
            alertDialog.show();


        });

        medicineInventoryButton.setOnClickListener(view -> startActivity(new Intent(viewContainer.getContext(), BarangayMedicine.class)));

        profileButton.setOnClickListener(view -> startActivity(new Intent(viewContainer.getContext(), UserProfileActivity.class)));

        helpButton.setOnClickListener(view -> startActivity(new Intent(viewContainer.getContext(), UserMoreHelpFragment.class)));

        viewContainer.findViewById(R.id.aboutButton).setOnClickListener(view -> startActivity(new Intent(viewContainer.getContext(), UserAboutUs.class)));

        viewContainer.findViewById(R.id.konsultaButton).setOnClickListener(view -> startActivity(new Intent(viewContainer.getContext(), UserConsult.class)));

        return viewContainer;
    }

    private void setAvailabilityToOffline(String currentUser) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(firebaseInstance);
        DatabaseReference databaseReference = firebaseDatabase.getReference("RegularUsers").child(currentUser + "/availability");

        databaseReference.setValue("Offline").addOnSuccessListener(success -> deleteExistingUserToken());

    }

    private void deleteExistingUserToken() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                .getReference("RegularUsers/" + sessionManager.getFullName() + "/token");

        databaseReference.removeValue().addOnCompleteListener(aVoid -> {
            if (aVoid.isSuccessful()) {
                sessionManager.signOutUser();
                Toast.makeText(getContext(), "You have successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
                requireActivity().finish();

                alertDialog.dismiss();
            }
        });
    }
}

