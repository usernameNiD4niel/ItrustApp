package com.rey.itrustapplication.adminform.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.ActivitySideBaddFormFpBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SideBAddFormFP extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ActivitySideBaddFormFpBinding binding;
    private boolean isAlreadyCleared;
    private String patientName;
    private boolean isPreviewing;
    private String questionOne, questionTwo, questionThree, questionFour, questionFive, questionSix;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Forms/FamilyPlanning");
    private ValueEventListener valueEventListener;
    private DatabaseReference dbRef;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySideBaddFormFpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Database Structure
        //FamilyPlanning > SideA > $user > SideB > ...

        patientName = getIntent().getStringExtra("patient_name");

        isPreviewing = getIntent().getBooleanExtra("isPreviewing", false);

        addListenerToCheckBox();

        if (isPreviewing) {
            binding.addRecordsSideB.setText(getResources().getString(R.string.update_text));
            fillAllThePrompt();
        }

        isAlreadyCleared = false;

        binding.dovTextSideB.setText(getCurrentDate());
        binding.dofuvTextSideB.setText(getCurrentDate());

        binding.addRecordsSideB.setOnClickListener(view -> validateUserInput());

        binding.backButtonAddFormFpSideB.setOnClickListener(view -> onBackPressed());
    }

    private void fillAllThePrompt() {

        dbRef = databaseReference.child("SideA/" + patientName + "/SideB");
                
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                if (data.exists()) {

                    for (DataSnapshot snapshot : data.getChildren()) {

                        final String date_of_visit = snapshot.child("date_of_visit").getValue(String.class);
                        final String medical_findings = snapshot.child("medical_findings").getValue(String.class);
                        final String method_accept = snapshot.child("method_accept").getValue(String.class);
                        final String name_of_service_provider = snapshot.child("name_of_service_provider").getValue(String.class);
                        final String date_of_follow_up_visit = snapshot.child("date_of_follow_up_visit").getValue(String.class);

                        final String question_one = snapshot.child("_1_did_you_have_a_baby_less_than_six_6_months_ago_are_you_fully_or_nearly_fully_breastfeeding_and_have_you_had_no_menstrual_period_since_then").getValue(String.class);
                        final String question_two = snapshot.child("_2_have_you_abstained_from_sexual_intercourse_since_your_last_menstrual_period_or_delivery").getValue(String.class);
                        final String question_three = snapshot.child("_3_have_you_had_a_baby_in_the_last_four_4_weeks").getValue(String.class);
                        final String question_four = snapshot.child("_4_did_your_last_menstrual_period_start_within_the_past_seven_7_days").getValue(String.class);
                        final String question_five = snapshot.child("_5_have_you_had_a_miscarriage_or_abortion_in_the_last_seven_7_days").getValue(String.class);
                        final String question_six = snapshot.child("_6_have_you_been_using_a_reliable_contraceptive_method_consistency_and_correctly").getValue(String.class);

                        binding.dovTextSideB.setText(date_of_visit);
                        binding.medicalFindingsTextSideB.setText(medical_findings);
                        binding.methodAcceptedTextSideB.setText(method_accept);
                        binding.nospTextSideB.setText(name_of_service_provider);
                        binding.dofuvTextSideB.setText(date_of_follow_up_visit);

                        if ("yes".equals(question_one)) {
                            binding.questionOneYesSideB.setChecked(true);
                        } else {
                            binding.questionOneNoSideB.setChecked(true);
                        }

                        if ("yes".equals(question_two)) {
                            binding.questionTwoYesSideB.setChecked(true);
                        } else {
                            binding.questionTwoNoSideB.setChecked(true);
                        }

                        if ("yes".equals(question_three)) {
                            binding.questionThreeYesSideB.setChecked(true);
                        } else {
                            binding.questionThreeNoSideB.setChecked(true);
                        }

                        if ("yes".equals(question_four)) {
                            binding.questionFourYesSideB.setChecked(true);
                        } else {
                            binding.questionFourNoSideB.setChecked(true);
                        }

                        if ("yes".equals(question_five)) {
                            binding.questionFiveYesSideB.setChecked(true);
                        } else {
                            binding.questionFiveNoSideB.setChecked(true);
                        }

                        if ("yes".equals(question_six)) {
                            binding.questionSixYesSideB.setChecked(true);
                        } else {
                            binding.questionSixNoSideB.setChecked(true);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SideBAddFormFP.this, "Error, retrieving data", Toast.LENGTH_SHORT).show();
            }
        };
        
        dbRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private void validateUserInput() {
        String dateOfVisit = binding.dovTextSideB.getEditableText().toString();
        String medicalFindings = binding.medicalFindingsTextSideB.getEditableText().toString();
        String methodAccept = binding.methodAcceptedTextSideB.getEditableText().toString();
        String nameOfServiceProvider = binding.nospTextSideB.getEditableText().toString();
        String dateOfFollowUpVisit = binding.dofuvTextSideB.getEditableText().toString();

        Toast.makeText(this, "Date of Visit: " + dateOfVisit, Toast.LENGTH_LONG).show();

        if (dateOfVisit.isEmpty()) {
            binding.dovSideB.setError("Date of Visit cannot be empty.");
            binding.dovSideB.requestFocus();
            return;
        }

        if (binding.dovSideB.isErrorEnabled()) {
            binding.dovSideB.setError("");
            binding.dovSideB.setErrorEnabled(false);
        }

        if (medicalFindings.isEmpty()) {
            binding.medicalFindingsSideB.setError("Medical findings cannot be empty.");
            binding.medicalFindingsSideB.requestFocus();
            return;
        }

        if (binding.medicalFindingsSideB.isErrorEnabled()) {
            binding.medicalFindingsSideB.setErrorEnabled(false);
            binding.medicalFindingsSideB.setError("");
        }

        if (methodAccept.isEmpty()) {
            binding.methodAcceptedSideB.setError("Method Accepted cannot be empty.");
            binding.methodAcceptedSideB.requestFocus();
            return;
        }

        if (binding.methodAcceptedSideB.isErrorEnabled()) {
            binding.methodAcceptedSideB.setErrorEnabled(false);
            binding.methodAcceptedSideB.setError("");
        }

        if (nameOfServiceProvider.isEmpty()) {
            binding.nospSideB.setError("Name of service provider cannot be empty.");
            binding.nospSideB.requestFocus();
            return;
        }

        if (binding.nospSideB.isErrorEnabled()) {
            binding.nospSideB.setErrorEnabled(false);
            binding.nospSideB.setError("");
        }

        if (dateOfFollowUpVisit.isEmpty()) {
            binding.dofuvSideB.setError("Date of follow-up visit cannot me empty");
            binding.dofuvSideB.requestFocus();
            return;
        }

        if (binding.dofuvSideB.isErrorEnabled()) {
            binding.dofuvSideB.setErrorEnabled(false);
            binding.dofuvSideB.setError("");
        }

        if (questionOne.isEmpty() || questionTwo.isEmpty() || questionThree.isEmpty() || questionFour.isEmpty() || questionFive.isEmpty() || questionSix.isEmpty()) {
            Toast.makeText(this, "Please fill all the checkboxes correctly.", Toast.LENGTH_SHORT).show();
            return;
        }

        pushDataToSideBNode(dateOfVisit, medicalFindings, methodAccept, nameOfServiceProvider, dateOfFollowUpVisit);

    }

    private void pushDataToSideBNode(String dateOfVisit, String medicalFindings, String methodAccept, String nameOfServiceProvider,
                                     String dateOfFollowUpVisit) {
        //Forms > FamilyPlanning > SideA > $user > SideB > ...
        final String dateKey = dateOfVisit.replaceAll("[^a-zA-Z0-9]", "_");
        Map<String, Object> sideBObject = new HashMap<>();
        sideBObject.put("date_of_visit", dateOfVisit);
        sideBObject.put("date_key", dateKey);
        sideBObject.put("medical_findings", medicalFindings);
        sideBObject.put("method_accept", methodAccept);
        sideBObject.put("name_of_service_provider", nameOfServiceProvider);
        sideBObject.put("date_of_follow_up_visit", dateOfFollowUpVisit);
        sideBObject.put("_1_did_you_have_a_baby_less_than_six_6_months_ago_are_you_fully_or_nearly_fully_breastfeeding_and_have_you_had_no_menstrual_period_since_then", questionOne);
        sideBObject.put("_2_have_you_abstained_from_sexual_intercourse_since_your_last_menstrual_period_or_delivery", questionTwo);
        sideBObject.put("_3_have_you_had_a_baby_in_the_last_four_4_weeks", questionThree);
        sideBObject.put("_4_did_your_last_menstrual_period_start_within_the_past_seven_7_days", questionFour);
        sideBObject.put("_5_have_you_had_a_miscarriage_or_abortion_in_the_last_seven_7_days", questionFive);
        sideBObject.put("_6_have_you_been_using_a_reliable_contraceptive_method_consistency_and_correctly", questionSix);

        databaseReference.child("SideA/" + patientName + "/SideB/" + dateKey).setValue(sideBObject)
                .addOnSuccessListener(success -> makeTheDefault(patientName))
                .addOnFailureListener(failed -> Toast.makeText(this, failed.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void makeTheDefault(String patientName) {

        if (isPreviewing) {
            Toast.makeText(this, "You have successfully updated " + patientName + "'s record", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You have successfully added a records for " + patientName, Toast.LENGTH_LONG).show();
        }
        finish();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", new Locale("fil", "PH"));
        return dateFormat.format(new Date());
    }

    private void addListenerToCheckBox() {
        binding.questionOneYesSideB.setOnCheckedChangeListener(this);
        binding.questionOneNoSideB.setOnCheckedChangeListener(this);

        binding.questionTwoYesSideB.setOnCheckedChangeListener(this);
        binding.questionTwoNoSideB.setOnCheckedChangeListener(this);

        binding.questionThreeYesSideB.setOnCheckedChangeListener(this);
        binding.questionThreeNoSideB.setOnCheckedChangeListener(this);

        binding.questionFourYesSideB.setOnCheckedChangeListener(this);
        binding.questionFourNoSideB.setOnCheckedChangeListener(this);

        binding.questionFiveYesSideB.setOnCheckedChangeListener(this);
        binding.questionFiveNoSideB.setOnCheckedChangeListener(this);

        binding.questionSixYesSideB.setOnCheckedChangeListener(this);
        binding.questionSixNoSideB.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onStop() {
        if (dbRef != null)
            dbRef.removeEventListener(valueEventListener);

        if (!isAlreadyCleared) {
            clearData();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isAlreadyCleared) {
            clearData();
        }
    }

    private void clearData() {
        isAlreadyCleared = true;
        binding.questionOneYesSideB.setOnCheckedChangeListener(null);
        binding.questionOneNoSideB.setOnCheckedChangeListener(null);

        binding.questionTwoYesSideB.setOnCheckedChangeListener(null);
        binding.questionTwoNoSideB.setOnCheckedChangeListener(null);

        binding.questionThreeYesSideB.setOnCheckedChangeListener(null);
        binding.questionThreeNoSideB.setOnCheckedChangeListener(null);

        binding.questionFourYesSideB.setOnCheckedChangeListener(null);
        binding.questionFourNoSideB.setOnCheckedChangeListener(null);

        binding.questionFiveYesSideB.setOnCheckedChangeListener(null);
        binding.questionFiveNoSideB.setOnCheckedChangeListener(null);

        binding.questionSixYesSideB.setOnCheckedChangeListener(null);
        binding.questionSixNoSideB.setOnCheckedChangeListener(null);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) return;

        if (compoundButton.getId() == binding.questionOneYesSideB.getId()) {
            binding.questionOneNoSideB.setChecked(false);
            questionOne = "yes";
            return;
        } else if (compoundButton.getId() == binding.questionOneNoSideB.getId()) {
            binding.questionOneYesSideB.setChecked(false);
            questionOne = "no";
            return;
        }

        if (compoundButton.getId() == binding.questionTwoYesSideB.getId()) {
            binding.questionTwoNoSideB.setChecked(false);
            questionTwo = "yes";
            return;
        } else if (binding.questionTwoNoSideB.getId() == compoundButton.getId()) {
            binding.questionTwoYesSideB.setChecked(false);
            questionTwo = "no";
            return;
        }

        if (compoundButton.getId() == binding.questionThreeYesSideB.getId()) {
            binding.questionThreeNoSideB.setChecked(false);
            questionThree = "yes";
            return;
        } else if (binding.questionThreeNoSideB.getId() == compoundButton.getId()) {
            binding.questionThreeYesSideB.setChecked(false);
            questionThree = "no";
            return;
        }

        if (compoundButton.getId() == binding.questionFourYesSideB.getId()) {
            binding.questionFourNoSideB.setChecked(false);
            questionFour = "yes";
            return;
        } else if (binding.questionFourNoSideB.getId() == compoundButton.getId()) {
            binding.questionFourYesSideB.setChecked(false);
            questionFour = "no";
            return;
        }

        if (compoundButton.getId() == binding.questionFiveYesSideB.getId()) {
            binding.questionFiveNoSideB.setChecked(false);
            questionFive = "yes";
            return;
        } else if (binding.questionFiveNoSideB.getId() == compoundButton.getId()) {
            binding.questionFiveYesSideB.setChecked(false);
            questionFive = "no";
            return;
        }

        if (compoundButton.getId() == binding.questionSixYesSideB.getId()) {
            binding.questionSixNoSideB.setChecked(false);
            questionSix = "yes";
        } else {
            binding.questionSixYesSideB.setChecked(false);
            questionSix = "no";
        }
    }
}