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
import com.rey.itrustapplication.adapter.AdminDeclineStatusAdapter;
import com.rey.itrustapplication.helperclasses.AdminPendingHelperClass;

import java.util.ArrayList;

public class AdminConsultationDeclinedFragment extends Fragment {

    private ArrayList<AdminPendingHelperClass> adminDeclineArrayList;
    private ProgressBar progressBar;
    private TextView noDataFound;
    private RecyclerView recyclerView;
    private AdminDeclineStatusAdapter adapter;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_consultation_declined, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBarDecline);
        noDataFound = view.findViewById(R.id.noDataFoundDecline);

        if (progressBar.getVisibility() == View.GONE)
            progressBar.setVisibility(View.VISIBLE);

        adminDeclineArrayList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerViewDecline);

        initializeData();

    }

    private void initializeData() {

        final DatabaseReference databaseReference = firebaseDatabase.getReference("DeclineRequest");

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

                        adminDeclineArrayList.add(new AdminPendingHelperClass(fullName, dateRequested, schedule));

                        if (adapter != null) {
                            adapter.notifyItemInserted(adminDeclineArrayList.size()-1);
                        }
                    }

                    if (recyclerView.getVisibility() == View.GONE) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setItemViewCacheSize(20);
                        recyclerView.setNestedScrollingEnabled(false);

                        adapter = new AdminDeclineStatusAdapter(getContext(), adminDeclineArrayList);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    if (adapter == null) return;

                    if (adapter.getItemCount() != adminDeclineArrayList.size()) {
                        final int count = adminDeclineArrayList.size();
                        adminDeclineArrayList.clear();
                        adapter.notifyItemRangeRemoved(adminDeclineArrayList.size(), count);
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