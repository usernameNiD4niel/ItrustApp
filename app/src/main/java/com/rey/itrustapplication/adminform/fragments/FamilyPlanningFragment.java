package com.rey.itrustapplication.adminform.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.rey.itrustapplication.adminform.activities.AddFormFP;
import com.rey.itrustapplication.adminform.adapter.FormAdapter;
import com.rey.itrustapplication.adminform.adapter.SideBAdapter;
import com.rey.itrustapplication.adminform.model.FormModel;
import com.rey.itrustapplication.adminform.model.SideBModel;
import com.rey.itrustapplication.adminform.popup.PatientNameDropDownPopUp;
import com.rey.itrustapplication.databinding.FragmentFamilyPlanningBinding;

import java.util.ArrayList;

public class FamilyPlanningFragment extends Fragment {

    private FormAdapter formAdapter;
    private SideBAdapter sideBAdapter;
    private ArrayList<FormModel> formModels;
    private ArrayList<SideBModel> sideBModelArrayList;

    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;

    FragmentFamilyPlanningBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_family_planning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentFamilyPlanningBinding.bind(view);

        formModels = new ArrayList<>();
        sideBModelArrayList = new ArrayList<>();


        formAdapter = new FormAdapter(formModels);

        binding.recyclerviewFpFragment.setItemViewCacheSize(3);
        binding.recyclerviewFpFragment.setHasFixedSize(true);
        binding.recyclerviewFpFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewFpFragment.setAdapter(formAdapter);

        sideBAdapter = new SideBAdapter(sideBModelArrayList);

        binding.recyclerviewSideBFpFragment.setItemViewCacheSize(4);
        binding.recyclerviewSideBFpFragment.setHasFixedSize(true);
        binding.recyclerviewSideBFpFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewSideBFpFragment.setAdapter(sideBAdapter);

        binding.existingPatientFpRecordButton.setOnClickListener(view1 -> startActivity(new Intent(getContext(), AddFormFP.class).putExtra("isAddingExistingRecord", true)));

        binding.addFpRecordButton.setOnClickListener(view1 -> startActivity(new Intent(getContext(), AddFormFP.class)));

        Thread thread = new Thread(this::populateRecyclerViewSideA);
        thread.start();

        binding.addMoreHeaderLabelFp.setOnClickListener(view1 -> PatientNameDropDownPopUp.showPopup(getContext()));

    }

    @Override
    public void onStop() {
        if (databaseReference != null)
            databaseReference.removeEventListener(valueEventListener);
        super.onStop();

    }

    private void populateRecyclerViewSideA() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Forms/FamilyPlanning/SideA");
        //Forms > FamilyPlanning > SideA > $user > SideB > ...

        valueEventListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!formModels.isEmpty())
                    formModels.clear();

                if (!sideBModelArrayList.isEmpty())
                    sideBModelArrayList.clear();

                FragmentActivity fragmentActivity = getActivity();

                if (fragmentActivity == null) {
                    Toast.makeText(getContext(), "Please restart the app to fetch all the required data", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!snapshot.exists()) {
                    fragmentActivity.runOnUiThread(() -> {
                        if (formAdapter.getItemCount() > 0) {
                            formAdapter.notifyDataSetChanged();
                        }

                        if (binding.noRecordFoundSideAFp.getVisibility() == View.GONE)
                            binding.noRecordFoundSideAFp.setVisibility(View.VISIBLE);
                        if (binding.progressBarSideAFp.getVisibility() == View.VISIBLE)
                            binding.progressBarSideAFp.setVisibility(View.GONE);

                        if (binding.noRecordFoundSideBFp.getVisibility() == View.GONE)
                            binding.noRecordFoundSideBFp.setVisibility(View.VISIBLE);
                        if (binding.progressBarSideBFp.getVisibility() == View.VISIBLE)
                            binding.progressBarSideBFp.setVisibility(View.GONE);

                    });
                    return;
                }
                long recordsCount = snapshot.getChildrenCount();

                binding.headerSideAFpForm.fpHeaderRecordsFp.setText(String.valueOf(recordsCount));

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                    if (childSnapshot.exists()) {

                        final String patient_name = childSnapshot.child("patient_name").getValue(String.class);
                        final String time_added = childSnapshot.child("last_date_added").getValue(String.class);
                        final String date_added = childSnapshot.child("last_time_added").getValue(String.class);

                        final long sideACount = childSnapshot.getChildrenCount();

                        fragmentActivity.runOnUiThread(() -> {
                            formAdapter.notifyItemInserted(formModels.size() - 1);
                            if (binding.noRecordFoundSideAFp.getVisibility() == View.VISIBLE)
                                binding.noRecordFoundSideAFp.setVisibility(View.GONE);
                            if (binding.progressBarSideAFp.getVisibility() == View.VISIBLE)
                                binding.progressBarSideAFp.setVisibility(View.GONE);
                        });

                        databaseReference.child(patient_name + "/SideB").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot sideBSnapshot) {
                                if (!sideBSnapshot.exists()) {
                                    formModels.add(new FormModel(patient_name, time_added, date_added, String.valueOf(((int) sideACount) -14)));
                                    formAdapter.notifyItemInserted(formModels.size()-1);
                                    fragmentActivity.runOnUiThread(() -> {
                                        if (binding.progressBarSideBFp.getVisibility() == View.VISIBLE)
                                            binding.progressBarSideBFp.setVisibility(View.GONE);
                                        if (binding.noRecordFoundSideBFp.getVisibility() == View.GONE)
                                            binding.noRecordFoundSideBFp.setVisibility(View.VISIBLE);
                                        if (sideBAdapter.getItemCount() > 0) {
                                            sideBAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    return;
                                } else {
                                    formModels.add(new FormModel(patient_name, time_added, date_added, String.valueOf(((int) sideACount) -15)));
                                    formAdapter.notifyItemInserted(formModels.size()-1);
                                }

                                for (DataSnapshot dataSnapshot : sideBSnapshot.getChildren()) {
                                    if (dataSnapshot.hasChild("date_of_visit")) {
                                        final String date_of_visit = dataSnapshot.child("date_of_visit").getValue(String.class);
                                        final String date_of_follow_up_visit = dataSnapshot.child("date_of_follow_up_visit").getValue(String.class);

                                        sideBModelArrayList.add(new SideBModel(patient_name, date_of_visit, date_of_follow_up_visit));

                                        fragmentActivity.runOnUiThread(() -> {
                                            sideBAdapter.notifyItemInserted(sideBModelArrayList.size()-1);
                                            if (binding.noRecordFoundSideBFp.getVisibility() == View.VISIBLE)
                                                binding.noRecordFoundSideBFp.setVisibility(View.GONE);
                                            if (binding.progressBarSideBFp.getVisibility() == View.VISIBLE)
                                                binding.progressBarSideBFp.setVisibility(View.GONE);

                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(valueEventListener);

    }
}