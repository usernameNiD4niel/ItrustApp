package com.rey.itrustapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.adapter.AdminApproveStatusAdapter;
import com.rey.itrustapplication.helperclasses.AdminPendingHelperClass;

import java.util.ArrayList;

public class AdminConsultationApprovedFragment extends Fragment {

    private ArrayList<AdminPendingHelperClass> adminApproveHelperClassArrayList;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
    private TextView noDataFound;
    private RecyclerView recyclerViewPending;
    private ProgressBar progressBar;
    private AdminApproveStatusAdapter adminApproveStatusAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_consultation_approved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noDataFound = view.findViewById(R.id.noDataFoundApprove);
        progressBar = view.findViewById(R.id.progressBarApprove);
        recyclerViewPending = view.findViewById(R.id.recyclerViewApprove);

        adminApproveHelperClassArrayList = new ArrayList<>();

        dataInitialize();

    }

    private void dataInitialize() {

        final DatabaseReference databaseReference = firebaseDatabase.getReference("ApproveRequest");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (noDataFound.getVisibility() == View.VISIBLE)
                        noDataFound.setVisibility(View.GONE);

                    for (DataSnapshot pendingStatus : snapshot.getChildren()) {
                        final String fullName = pendingStatus.child("fullName").getValue(String.class);
                        final String schedule = pendingStatus.child("schedule").getValue(String.class);
                        final String dateRequested = pendingStatus.child("date_requested").getValue(String.class);

                        adminApproveHelperClassArrayList.add(new AdminPendingHelperClass(fullName, dateRequested, schedule));

                        if (adminApproveStatusAdapter != null) {
                            adminApproveStatusAdapter.notifyItemInserted(adminApproveHelperClassArrayList.size()-1);
                        }
                    }

                    if (recyclerViewPending.getVisibility() == View.GONE) {
                        recyclerViewPending.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewPending.setHasFixedSize(true);
                        recyclerViewPending.setItemViewCacheSize(20);
                        recyclerViewPending.setNestedScrollingEnabled(false);

                        adminApproveStatusAdapter = new AdminApproveStatusAdapter(getContext(), adminApproveHelperClassArrayList);
                        recyclerViewPending.setAdapter(adminApproveStatusAdapter);

                        progressBar.setVisibility(View.GONE);
                        recyclerViewPending.setVisibility(View.VISIBLE);
                    }
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    if (adminApproveStatusAdapter == null) return;

                    if (adminApproveStatusAdapter.getItemCount() != adminApproveHelperClassArrayList.size()) {
                        final int lastCountList = adminApproveHelperClassArrayList.size();
                        adminApproveHelperClassArrayList.clear();
                        adminApproveStatusAdapter.notifyItemRangeRemoved(0, lastCountList);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
                Toast.makeText(getContext(), "Errror: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}