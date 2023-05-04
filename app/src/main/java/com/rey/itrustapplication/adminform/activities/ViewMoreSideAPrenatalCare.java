package com.rey.itrustapplication.adminform.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.adminform.adapter.PrenatalCareDateAdapter;
import com.rey.itrustapplication.adminform.model.PatientsDateModel;
import com.rey.itrustapplication.databinding.ActivityViewMoreSideAprenatalCareBinding;

import java.util.ArrayList;
import java.util.List;

public class ViewMoreSideAPrenatalCare extends AppCompatActivity {

    ActivityViewMoreSideAprenatalCareBinding binding;
    private DatabaseReference databaseReference;
    private List<PatientsDateModel> models;
    private PrenatalCareDateAdapter adapter;
    private ValueEventListener valueEventListener;

    private String nodeName = "";
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewMoreSideAprenatalCareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String clientName = getIntent().getStringExtra("client_name");
        nodeName = getIntent().getStringExtra("node_name");
        type = getIntent().getStringExtra("type");

        if (!clientName.isEmpty()) {
            binding.patientNameViewMorePc.setText(clientName);
        }

        ProgressDialog progressDialog = ProgressDialog.show(this, "Getting Ready", "Fetching all the necessary data from the database", true);
        progressDialog.show();

        models = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Forms/PrenatalCare/" + nodeName);

        binding.backButtonViewMorePc.setOnClickListener(view -> onBackPressed());

        fetchDataFromTheDatabase(progressDialog);

    }

    @Override
    protected void onStop() {
        if (databaseReference != null)
            databaseReference.removeEventListener(valueEventListener);
        super.onStop();
    }

    private void fetchDataFromTheDatabase(ProgressDialog progressDialog) {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        final String date_added = dataSnapshot.child("date_added").getValue(String.class);
                        final String time_added = dataSnapshot.child("time_added").getValue(String.class);
                        final String key = dataSnapshot.child("key").getValue(String.class);
                        models.add(new PatientsDateModel(date_added, time_added, key));

                    }

                    if (!models.isEmpty()) {
                        adapter = new PrenatalCareDateAdapter(models, nodeName, type);
                        binding.recyclerviewViewMorePc.setHasFixedSize(true);
                        binding.recyclerviewViewMorePc.setItemAnimator(new DefaultItemAnimator());
                        binding.recyclerviewViewMorePc.setAdapter(adapter);
                    }

                } else {
                    binding.noDataFoundViewMorePc.setVisibility(View.VISIBLE);
                }
                runOnUiThread(progressDialog::dismiss);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewMoreSideAPrenatalCare.this, "Fetching data error, please check your internet connection or restart the app", Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.child(type).addListenerForSingleValueEvent(valueEventListener);
    }
}