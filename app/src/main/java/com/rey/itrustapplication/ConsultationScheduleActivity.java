package com.rey.itrustapplication;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.databinding.ActivityConsultationScheduleBinding;

public class ConsultationScheduleActivity extends AdminDrawerBaseActivity {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            ActivityConsultationScheduleBinding binding;
    private String daySelected = "";
    private String barangayCode = "", to = "", from = "";

    private final String[] DAYS = new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Consultation Schedule");

        DAYS[0] = getResources().getString(R.string.monday);
        DAYS[1] = getResources().getString(R.string.tuesday);
        DAYS[2] = getResources().getString(R.string.wednesday);
        DAYS[3] = getResources().getString(R.string.thursday);
        DAYS[4] = getResources().getString(R.string.friday);
        DAYS[5] = getResources().getString(R.string.saturday);
        DAYS[6] = getResources().getString(R.string.sunday);

        populateDropdownMenu();
        binding.updateScheduleConsultationSchedule.setOnClickListener(view -> validateUserInput());
    }

    private void validateUserInput() {

        if (daySelected.isEmpty()) {
            binding.daysTextConsultationSchedule.setError("Please select days first you want to update");
            binding.daysTextConsultationSchedule.requestFocus();
            return;
        }

        if (binding.hoursFromConsultationSchedule.getEditText()!=null) {

            from = binding.hoursFromConsultationSchedule.getEditText().getText().toString();

            if (from.trim().isEmpty()) {
                binding.hoursFromConsultationSchedule.setError("This field is required, please enter starting hours of schedule for " + daySelected);
                binding.hoursFromConsultationSchedule.requestFocus();
                return;
            }
        }

        if (binding.hoursToConsultationSchedule.getEditText()!=null) {

            to = binding.hoursToConsultationSchedule.getEditText().getText().toString();

            if (to.trim().isEmpty()) {
                binding.hoursToConsultationSchedule.setError("This field is required, please enter ending hours of schedule for " + daySelected);
                binding.hoursToConsultationSchedule.requestFocus();
                return;
            }
        }

        if (binding.barangayPasscodeConsultationSchedule.getEditText() != null) {

            barangayCode = binding.barangayPasscodeConsultationSchedule.getEditText().getText().toString();

            if (barangayCode.trim().isEmpty()) {
                binding.barangayPasscodeConsultationSchedule.setError("Enter the barangay code to verify the changes");
                binding.barangayPasscodeConsultationSchedule.requestFocus();
                return;
            }
        }
        if (binding.hoursFromConsultationSchedule.isErrorEnabled()) {
            binding.hoursFromConsultationSchedule.setError("");
            binding.hoursFromConsultationSchedule.setErrorEnabled(false);
        }

        if (binding.hoursToConsultationSchedule.isErrorEnabled()) {
            binding.hoursToConsultationSchedule.setError("");
            binding.hoursToConsultationSchedule.setErrorEnabled(false);
        }

        if (binding.barangayPasscodeConsultationSchedule.isErrorEnabled()) {
            binding.barangayPasscodeConsultationSchedule.setError("");
            binding.barangayPasscodeConsultationSchedule.setErrorEnabled(false);
        }

        getBarangayCode();

    }

    private void getBarangayCode() {

        final String concatenatedSchedule = from + " - " + to;

        databaseReference.child("Admin/barangay_code").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String barangay_code = snapshot.getValue(String.class);
                    if (barangayCode.equals(barangay_code)) {
                        databaseReference.child("Admin/consultation_schedule/" + daySelected.toLowerCase()).setValue(concatenatedSchedule)
                                .addOnSuccessListener(success -> resetUserInputs())
                                .addOnFailureListener(failed -> Toast.makeText(ConsultationScheduleActivity.this, "Cannot update the consultation schedule for " + daySelected, Toast.LENGTH_SHORT).show());
                    } else {
                        binding.barangayPasscodeConsultationSchedule.setError("The barangay code doesn't match to the original barangay code");
                        binding.barangayPasscodeConsultationSchedule.requestFocus();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void resetUserInputs() {
        binding.daysTextConsultationSchedule.setText("");

        if (binding.hoursFromConsultationSchedule.getEditText() != null)
            binding.hoursFromConsultationSchedule.getEditText().setText("");

        if (binding.hoursToConsultationSchedule.getEditText() != null)
            binding.hoursToConsultationSchedule.getEditText().setText("");

        daySelected = "";
        to = "";
        from = "";

        binding.daysTextConsultationSchedule.requestFocus();

        Toast.makeText(getApplicationContext(), "You have successfully updated the " + daySelected + " schedule", Toast.LENGTH_SHORT).show();
    }

    private void populateDropdownMenu() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.signup_gender_list, DAYS);
        binding.daysTextConsultationSchedule.setAdapter(arrayAdapter);
        binding.daysTextConsultationSchedule.setOnItemClickListener((parent, view, position, id) -> {
            daySelected = parent.getItemAtPosition(position).toString();

            databaseReference.child("Admin/consultation_schedule/" + daySelected.toLowerCase()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String day_selected = snapshot.getValue(String.class);

                        if (day_selected == null) return;

                        final String[] schedule = day_selected.split("-");

                        if (schedule.length < 2) {
                            if (binding.hoursToConsultationSchedule.getEditText() != null)
                                binding.hoursToConsultationSchedule.getEditText().setText(day_selected);
                            return;
                        }

                        if (binding.hoursFromConsultationSchedule.getEditText() != null)
                            binding.hoursFromConsultationSchedule.getEditText().setText(schedule[0]);
                        if (binding.hoursToConsultationSchedule.getEditText() != null)
                            binding.hoursToConsultationSchedule.getEditText().setText(schedule[1]);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }
}