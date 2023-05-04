package com.rey.itrustapplication.adminform.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adminform.activities.PrenatalCareAddForm;
import com.rey.itrustapplication.adminform.adapter.PrenatalCareSideAAdapter;
import com.rey.itrustapplication.adminform.model.PrenatalCareSideAModel;
import com.rey.itrustapplication.databinding.FragmentPrenatalCareBinding;

import java.util.ArrayList;
import java.util.List;

public class PrenatalCareFragment extends Fragment {

    FragmentPrenatalCareBinding binding;
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;
    private List<PrenatalCareSideAModel> prenatalCareSideAModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPrenatalCareBinding.bind(inflater.inflate(R.layout.fragment_prenatal_care, container, false));
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        if (databaseReference != null) databaseReference.removeEventListener(valueEventListener);
        super.onStop();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prenatalCareSideAModels = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Forms/PrenatalCare");
        fetchDataFromSideADatabase();

        binding.addPrenatalCareRecordButton.setOnClickListener(view1 -> startActivity(new Intent(getContext(), PrenatalCareAddForm.class)));

//        binding.sideBIncludePrenatalCare.viewMoreHeaderSideBPrenatalCare.setOnClickListener(view1 -> startActivity(new Intent(getContext(), ViewMoreSideBPrenatalCare.class)));
    }

    private void fetchDataFromSideADatabase() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (binding.noDataFoundSideAPrenatalCare.getVisibility() == View.VISIBLE)
                        binding.noDataFoundSideAPrenatalCare.setVisibility(View.GONE);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.hasChild("client_name")) {
                            final String patient_name = dataSnapshot.child("client_name").getValue(String.class);
                            final String current_date_added = dataSnapshot.child("current_date_added").getValue(String.class);
                            final String current_time_added = dataSnapshot.child("current_time_added").getValue(String.class);
                            final String node_name = dataSnapshot.child("node_name").getValue(String.class);
                            prenatalCareSideAModels.add(new PrenatalCareSideAModel(patient_name, current_date_added, current_time_added, node_name));
                        }
                    }

                } else {
                    binding.noDataFoundSideAPrenatalCare.setVisibility(View.VISIBLE);
                    if (binding.progressBarSideAPrenatalCare.getVisibility() == View.VISIBLE)
                        binding.progressBarSideAPrenatalCare.setVisibility(View.GONE);
                    return;
                }

                if (binding.progressBarSideAPrenatalCare.getVisibility() == View.VISIBLE)
                    binding.progressBarSideAPrenatalCare.setVisibility(View.GONE);

                if (prenatalCareSideAModels.size() > 0) {
                    PrenatalCareSideAAdapter prenatalCareSideAAdapter = new PrenatalCareSideAAdapter(prenatalCareSideAModels);
                    binding.recyclerViewSideAPrenatalCare.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerViewSideAPrenatalCare.setHasFixedSize(true);
                    binding.recyclerViewSideAPrenatalCare.setAdapter(prenatalCareSideAAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error, " + error.toException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }
}