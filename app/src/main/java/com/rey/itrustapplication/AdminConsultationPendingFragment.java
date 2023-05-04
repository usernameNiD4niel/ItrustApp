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
import com.rey.itrustapplication.adapter.AdminPendingStatusAdapter;
import com.rey.itrustapplication.helperclasses.AdminPendingHelperClass;

import java.util.ArrayList;

public class AdminConsultationPendingFragment extends Fragment {

    private ArrayList<AdminPendingHelperClass> adminPendingHelperClassArrayList;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");

    private TextView noDataFound;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewPending;
    private AdminPendingStatusAdapter adminPendingStatusAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_admin_consultation_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.pendingConsultationProgress);
        noDataFound = view.findViewById(R.id.noDataFound);
        recyclerViewPending = view.findViewById(R.id.recyclerViewPending);

        dataInitialize();

    }

    private void dataInitialize() {

        adminPendingHelperClassArrayList = new ArrayList<>();

        final DatabaseReference databaseReference = firebaseDatabase.getReference("PendingRequest");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (noDataFound.getVisibility() == View.VISIBLE)
                        noDataFound.setVisibility(View.GONE);

                    for (DataSnapshot pendingStatus : snapshot.getChildren()) {
                        final String fullName = pendingStatus.child("fullName").getValue(String.class);
                        final String dateRequested = pendingStatus.child("date_requested").getValue(String.class);
                        final String schedule = pendingStatus.child("schedule").getValue(String.class);

                        adminPendingHelperClassArrayList.add(new AdminPendingHelperClass(fullName, dateRequested,schedule));

                        if (adminPendingStatusAdapter != null) {
                            adminPendingStatusAdapter.notifyItemInserted(adminPendingHelperClassArrayList.size()-1);
                        }

                    }

                    if (recyclerViewPending.getVisibility() == View.GONE) {

                        recyclerViewPending.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewPending.setHasFixedSize(true);
                        recyclerViewPending.setItemViewCacheSize(20);
                        recyclerViewPending.setNestedScrollingEnabled(false);

                        adminPendingStatusAdapter = new AdminPendingStatusAdapter(getContext(), adminPendingHelperClassArrayList);
                        recyclerViewPending.setAdapter(adminPendingStatusAdapter);
                        progressBar.setVisibility(View.GONE);
                        recyclerViewPending.setVisibility(View.VISIBLE);


                    }
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    if (adminPendingStatusAdapter == null) return;

                    if (adminPendingStatusAdapter.getItemCount() != adminPendingHelperClassArrayList.size()) {
                        final int lastCountList = adminPendingHelperClassArrayList.size();
                        adminPendingHelperClassArrayList.clear();
                        adminPendingStatusAdapter.notifyItemRangeRemoved(adminPendingHelperClassArrayList.size(), lastCountList);
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