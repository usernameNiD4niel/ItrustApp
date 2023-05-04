
package com.rey.itrustapplication;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.databinding.ActivityAdminDashboardBinding;
import com.rey.itrustapplication.residence_profile.ResidenceProfile;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;

import java.util.LinkedList;

public class AdminDashboardActivity extends AdminDrawerBaseActivity {

    ActivityAdminDashboardBinding activityAdminDashboardBinding;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();

    private final LinkedList<Float> rateList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminDashboardBinding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityAdminDashboardBinding.getRoot());
        allocateActivityTitle("Registered User");

        final String currentUser = new AdminSessionManager(this).getUsernameSession();
        activityAdminDashboardBinding.currentUserDashboard.setText(currentUser);

        setUpNumberOfRegisteredAccount("RegularUsers", activityAdminDashboardBinding.numberOfAccountsDashboardTitle);

        setUpNumberOfRegisteredAccount("ApproveRequest", activityAdminDashboardBinding.approvedScheduleDashboard);

        setUpNumberOfRegisteredAccount("Admin", activityAdminDashboardBinding.numberOfAdminDashboard);

        setUpNumberOfRegisteredAccount("QuestionRequested", activityAdminDashboardBinding.numberOfConsultationTitle);

        setUpNumberOfRegisteredAccount("DeclineRequest", activityAdminDashboardBinding.declineConsultationDashboard);

        setUpNumberOfRegisteredAccount("ApproveRequest", activityAdminDashboardBinding.approvedScheduleDashboard);

        setUpNumberOfRegisteredAccount("PendingRequest", activityAdminDashboardBinding.pendingRequestDashboard);

        setUpNumberOfClaimedMedicine();

        retrieveStarlaRate();

        activityAdminDashboardBinding.residenceProfileParent.setOnClickListener(view -> startActivity(new Intent(AdminDashboardActivity.this, ResidenceProfile.class)));

    }

    private void setUpNumberOfClaimedMedicine() {
        databaseReference.child("ClaimedMedicines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final long number_of_claimed_medicine = snapshot.getChildrenCount();

                    activityAdminDashboardBinding.numberOfConsultationTitle.setText(String.valueOf(number_of_claimed_medicine));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpNumberOfRegisteredAccount(String path, TextView textView) {
        databaseReference.child(path)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (path.equals("Admin")) {
                            textView.setText(String.valueOf((int) (snapshot.getChildrenCount() - 2)));
                            return;
                        }

                        if (path.equals("RegularUsers")) {
                            textView.setText(String.valueOf((int) (snapshot.getChildrenCount() - 1)));
                            return;
                        }
                        textView.setText(String.valueOf((int) snapshot.getChildrenCount()));
                    } else {
                        textView.setText("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void retrieveStarlaRate() {
        databaseReference.child("Rate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.hasChild("who_rate")) {
                            final String rate = dataSnapshot.child("rate").getValue(String.class);

                            if (rate == null) return;

                            switch (rate) {
                                case "one":
                                    rateList.add(1f);
                                    break;
                                case "two":
                                    rateList.add(2f);
                                    break;
                                case "three":
                                    rateList.add(3f);
                                    break;
                                case "four":
                                    rateList.add(4f);
                                    break;
                                default:
                                    rateList.add(5f);
                                    break;
                            }

                        }
                    }
                }
                if (rateList.size() == 0) {
                    activityAdminDashboardBinding.starlaRateDashBoard.setText("0");
                    return;
                }

                float sumFinal = 0f;

                for (Float sum : rateList) {
                    sumFinal += sum;
                }

                sumFinal = sumFinal/rateList.size();

                Log.d("Daniel", "onDataChange: sum is: " + sumFinal);
                final String _rating = String.valueOf(sumFinal).substring(0,3);
                Log.d("Daniel", "onDataChange: rating is: " + _rating);
                activityAdminDashboardBinding.starlaRateDashBoard.setText(_rating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}