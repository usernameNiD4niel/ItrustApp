package com.rey.itrustapplication.adminform.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.ActivityViewMoreSideBprenatalCareBinding;
import com.rey.itrustapplication.databinding.TrimesterAbdominalExaminationBottomSheetBinding;
import com.rey.itrustapplication.databinding.VitalSignsBottomSheetBinding;

import java.util.HashMap;
import java.util.Map;

public class ViewMoreSideBPrenatalCare extends AppCompatActivity {

    ActivityViewMoreSideBprenatalCareBinding binding;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private Map<String, Object> sideBObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewMoreSideBprenatalCareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sideBObject = new HashMap<>();

        String nodeName = getIntent().getStringExtra("node_name");

        binding.backButtonViewMoreSideB.setOnClickListener(view -> onBackPressed());

        databaseReference = FirebaseDatabase.getInstance().getReference("Forms/PrenatalCare/"+ nodeName + "/SideB");

        binding.card1PcAddSideB.setOnClickListener(view -> showTAEBottomSheet());
        binding.card2PcAddSideB.setOnClickListener(view -> showVitalSignsBottomSheet());

        fetchDataToTheData();
    }

    private void showVitalSignsBottomSheet() {
        binding.card2PcAddSideB.setClickable(false);
        binding.progressBarSideBPc.setVisibility(View.VISIBLE);

        VitalSignsBottomSheetBinding bottomSheetBinding = VitalSignsBottomSheetBinding.inflate(getLayoutInflater());
        setDefaultFieldsToVitalSigns(bottomSheetBinding);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ViewMoreSideBPrenatalCare.this);
        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
        bottomSheetDialog.setOnDismissListener(dialogInterface -> binding.card2PcAddSideB.setClickable(true));
        bottomSheetDialog.setOnShowListener(dialogInterface -> binding.progressBarSideBPc.setVisibility(View.GONE));
        bottomSheetDialog.show();

        bottomSheetBinding.saveRecordsButtonVsBottomSheet.setOnClickListener(view -> validateUserInputVitalSigns(bottomSheetBinding, bottomSheetDialog));
    }

    private void validateUserInputVitalSigns(VitalSignsBottomSheetBinding bottomSheetBinding, BottomSheetDialog bottomSheetDialog) {

        //First Trimester - 1st Month
        pushDataToMap("first_trimester_1m_bp", bottomSheetBinding.m1DateVisit1stTrimesterEtVsPc.getText().toString());
        pushDataToMap("first_trimester_1m_weight", bottomSheetBinding.m1Aog1stTrimesterEtVsPc.getText().toString());
        pushDataToMap("first_trimester_1m_pulse_rate", bottomSheetBinding.m1FundicHeight1stTrimesterEtVsPc.getText().toString());
        pushDataToMap("first_trimester_1m_muac", bottomSheetBinding.m1Fht1stTrimesterEtVsPc.getText().toString());

        //First Trimester - 2nd Month
        pushDataToMap("first_trimester_2m_bp", bottomSheetBinding.m2DateVisit1stTrimesterEtVsPc.getText().toString());
        pushDataToMap("first_trimester_2m_weight", bottomSheetBinding.m2Aog1stTrimesterEtVsPc.getText().toString());
        pushDataToMap("first_trimester_2m_pulse_rate", bottomSheetBinding.m2FundicHeight1stTrimesterEtVsPc.getText().toString());
        pushDataToMap("first_trimester_2m_muac", bottomSheetBinding.m2Fht1stTrimesterEtVsPc.getText().toString());

        //First Trimester - 3rd Month
        pushDataToMap("first_trimester_3m_bp", bottomSheetBinding.m3DateVisit1stTrimesterEtVsPc.getText().toString());
        pushDataToMap("first_trimester_3m_weight", bottomSheetBinding.m3Aog1stTrimesterEtVsPc.getText().toString());
        pushDataToMap("first_trimester_3m_pulse_rate", bottomSheetBinding.m3FundicHeight1stTrimesterEtVsPc.getText().toString());
        pushDataToMap("first_trimester_3m_muac", bottomSheetBinding.m3Fht1stTrimesterEtVsPc.getText().toString());

        /*-----------------------------------------------------------------------*/

        //Second Trimester - 1st Month
        pushDataToMap("second_trimester_1m_bp", bottomSheetBinding.m1DateVisit2ndTrimesterEtVsPc.getText().toString());
        pushDataToMap("second_trimester_1m_weight", bottomSheetBinding.m1Aog2ndTrimesterEtVsPc.getText().toString());
        pushDataToMap("second_trimester_1m_pulse_rate", bottomSheetBinding.m1FundicHeight2ndTrimesterEtVsPc.getText().toString());
        pushDataToMap("second_trimester_1m_muac", bottomSheetBinding.m1Fht2ndTrimesterEtVsPc.getText().toString());

        //Second Trimester - 2nd Month
        pushDataToMap("second_trimester_2m_bp", bottomSheetBinding.m2DateVisit2ndTrimesterEtVsPc.getText().toString());
        pushDataToMap("second_trimester_2m_weight", bottomSheetBinding.m2Aog2ndTrimesterEtVsPc.getText().toString());
        pushDataToMap("second_trimester_2m_pulse_rate", bottomSheetBinding.m2FundicHeight2ndTrimesterEtVsPc.getText().toString());
        pushDataToMap("second_trimester_2m_muac", bottomSheetBinding.m2Fht2ndTrimesterEtVsPc.getText().toString());

        //Second Trimester - 3rd Month
        pushDataToMap("second_trimester_3m_bp", bottomSheetBinding.m3DateVisit2ndTrimesterEtVsPc.getText().toString());
        pushDataToMap("second_trimester_3m_weight", bottomSheetBinding.m3Aog2ndTrimesterEtVsPc.getText().toString());
        pushDataToMap("second_trimester_3m_pulse_rate", bottomSheetBinding.m3FundicHeight2ndTrimesterEtVsPc.getText().toString());
        pushDataToMap("second_trimester_3m_muac", bottomSheetBinding.m3Fht2ndTrimesterEtVsPc.getText().toString());

        /*-----------------------------------------------------------------------*/

        //Third Trimester - 1st Month
        pushDataToMap("third_trimester_1m_bp", bottomSheetBinding.m1DateVisit3rdTrimesterEtVsPc.getText().toString());
        pushDataToMap("third_trimester_1m_weight", bottomSheetBinding.m1Aog3rdTrimesterEtVsPc.getText().toString());
        pushDataToMap("third_trimester_1m_pulse_rate", bottomSheetBinding.m1FundicHeight3rdTrimesterEtVsPc.getText().toString());
        pushDataToMap("third_trimester_1m_muac", bottomSheetBinding.m1Fht3rdTrimesterEtVsPc.getText().toString());

        //Third Trimester - 2nd Month
        pushDataToMap("third_trimester_2m_bp", bottomSheetBinding.m2DateVisit3rdTrimesterEtVsPc.getText().toString());
        pushDataToMap("third_trimester_2m_weight", bottomSheetBinding.m2Aog3rdTrimesterEtVsPc.getText().toString());
        pushDataToMap("third_trimester_2m_pulse_rate", bottomSheetBinding.m2FundicHeight3rdTrimesterEtVsPc.getText().toString());
        pushDataToMap("third_trimester_2m_muac", bottomSheetBinding.m2Fht3rdTrimesterEtVsPc.getText().toString());

        //Third Trimester - 3rd Month
        pushDataToMap("third_trimester_3m_bp", bottomSheetBinding.m3DateVisit3rdTrimesterEtVsPc.getText().toString());
        pushDataToMap("third_trimester_3m_weight", bottomSheetBinding.m3Aog3rdTrimesterEtVsPc.getText().toString());
        pushDataToMap("third_trimester_3m_pulse_rate", bottomSheetBinding.m3FundicHeight3rdTrimesterEtVsPc.getText().toString());
        pushDataToMap("third_trimester_3m_muac", bottomSheetBinding.m3Fht3rdTrimesterEtVsPc.getText().toString());

        setCardIndicator(bottomSheetDialog, binding.card2PcAddSideB);
    }

    private void setDefaultFieldsToVitalSigns(VitalSignsBottomSheetBinding bottomSheetBinding) {

        if (!sideBObject.isEmpty()) {
            // First Trimester - Month 1
            if (sideBObject.containsKey("first_trimester_1m_bp")) {
                bottomSheetBinding.m1DateVisit1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_1m_bp")));
            }
            if (sideBObject.containsKey("first_trimester_1m_weight")) {
                bottomSheetBinding.m1Aog1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_1m_weight")));
            }
            if (sideBObject.containsKey("first_trimester_1m_pulse_rate")) {
                bottomSheetBinding.m1FundicHeight1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_1m_pulse_rate")));
            }
            if (sideBObject.containsKey("first_trimester_1m_muac")) {
                bottomSheetBinding.m1Fht1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_1m_muac")));
            }

            // First Trimester - Month 2
            if (sideBObject.containsKey("first_trimester_2m_bp")) {
                bottomSheetBinding.m2DateVisit1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_2m_bp")));
            }
            if (sideBObject.containsKey("first_trimester_2m_weight")) {
                bottomSheetBinding.m2Aog1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_2m_weight")));
            }
            if (sideBObject.containsKey("first_trimester_2m_pulse_rate")) {
                bottomSheetBinding.m2FundicHeight1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_2m_pulse_rate")));
            }
            if (sideBObject.containsKey("first_trimester_2m_muac")) {
                bottomSheetBinding.m2Fht1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_2m_muac")));
            }

            // First Trimester - Month 3
            if (sideBObject.containsKey("first_trimester_3m_bp")) {
                bottomSheetBinding.m3DateVisit1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_3m_bp")));
            }
            if (sideBObject.containsKey("first_trimester_3m_weight")) {
                bottomSheetBinding.m3Aog1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_3m_weight")));
            }
            if (sideBObject.containsKey("first_trimester_3m_pulse_rate")) {
                bottomSheetBinding.m3FundicHeight1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_3m_pulse_rate")));
            }
            if (sideBObject.containsKey("first_trimester_3m_muac")) {
                bottomSheetBinding.m3Fht1stTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("first_trimester_3m_muac")));
            }

            /*-------------------------------------------------------------------------------------------------------------------*/

            // Second Trimester - Month 1
            if (sideBObject.containsKey("second_trimester_1m_bp")) {
                bottomSheetBinding.m1DateVisit2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_1m_bp")));
            }
            if (sideBObject.containsKey("second_trimester_1m_weight")) {
                bottomSheetBinding.m1Aog2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_1m_weight")));
            }
            if (sideBObject.containsKey("second_trimester_1m_pulse_rate")) {
                bottomSheetBinding.m1FundicHeight2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_1m_pulse_rate")));
            }
            if (sideBObject.containsKey("second_trimester_1m_muac")) {
                bottomSheetBinding.m1Fht2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_1m_muac")));
            }

            // Second Trimester - Month 2
            if (sideBObject.containsKey("second_trimester_2m_bp")) {
                bottomSheetBinding.m2DateVisit2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_2m_bp")));
            }
            if (sideBObject.containsKey("second_trimester_2m_weight")) {
                bottomSheetBinding.m2Aog2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_2m_weight")));
            }
            if (sideBObject.containsKey("second_trimester_2m_pulse_rate")) {
                bottomSheetBinding.m2FundicHeight2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_2m_pulse_rate")));
            }
            if (sideBObject.containsKey("second_trimester_2m_muac")) {
                bottomSheetBinding.m2Fht2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_2m_muac")));
            }

            // Second Trimester - Month 3
            if (sideBObject.containsKey("second_trimester_3m_bp")) {
                bottomSheetBinding.m3DateVisit2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_3m_bp")));
            }
            if (sideBObject.containsKey("second_trimester_3m_weight")) {
                bottomSheetBinding.m3Aog2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_3m_weight")));
            }
            if (sideBObject.containsKey("second_trimester_3m_pulse_rate")) {
                bottomSheetBinding.m3FundicHeight2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_3m_pulse_rate")));
            }
            if (sideBObject.containsKey("second_trimester_3m_muac")) {
                bottomSheetBinding.m3Fht2ndTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("second_trimester_3m_muac")));
            }

            /*-------------------------------------------------------------------------------------------------------------------*/

            // Third Trimester - Month 1
            if (sideBObject.containsKey("third_trimester_1m_bp")) {
                bottomSheetBinding.m1DateVisit3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_1m_bp")));
            }
            if (sideBObject.containsKey("third_trimester_1m_weight")) {
                bottomSheetBinding.m1Aog3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_1m_weight")));
            }
            if (sideBObject.containsKey("third_trimester_1m_pulse_rate")) {
                bottomSheetBinding.m1FundicHeight3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_1m_pulse_rate")));
            }
            if (sideBObject.containsKey("third_trimester_1m_muac")) {
                bottomSheetBinding.m1Fht3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_1m_muac")));
            }

            // Third Trimester - Month 2
            if (sideBObject.containsKey("third_trimester_2m_bp")) {
                bottomSheetBinding.m2DateVisit3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_2m_bp")));
            }
            if (sideBObject.containsKey("third_trimester_2m_weight")) {
                bottomSheetBinding.m2Aog3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_2m_weight")));
            }
            if (sideBObject.containsKey("third_trimester_2m_pulse_rate")) {
                bottomSheetBinding.m2FundicHeight3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_2m_pulse_rate")));
            }
            if (sideBObject.containsKey("third_trimester_2m_muac")) {
                bottomSheetBinding.m2Fht3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_2m_muac")));
            }

            // Third Trimester - Month 3
            if (sideBObject.containsKey("third_trimester_3m_bp")) {
                bottomSheetBinding.m3DateVisit3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_3m_bp")));
            }
            if (sideBObject.containsKey("third_trimester_3m_weight")) {
                bottomSheetBinding.m3Aog3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_3m_weight")));
            }
            if (sideBObject.containsKey("third_trimester_3m_pulse_rate")) {
                bottomSheetBinding.m3FundicHeight3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_3m_pulse_rate")));
            }
            if (sideBObject.containsKey("third_trimester_3m_muac")) {
                bottomSheetBinding.m3Fht3rdTrimesterEtVsPc.setText(String.valueOf(sideBObject.get("third_trimester_3m_muac")));
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void showTAEBottomSheet() {

        binding.card1PcAddSideB.setClickable(false);
        binding.progressBarSideBPc.setVisibility(View.VISIBLE);

        TrimesterAbdominalExaminationBottomSheetBinding bottomSheetBinding = TrimesterAbdominalExaminationBottomSheetBinding.inflate(getLayoutInflater());

        setDefaultFieldsToTAE(bottomSheetBinding);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ViewMoreSideBPrenatalCare.this);
        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
        bottomSheetDialog.setOnDismissListener(dialogInterface -> binding.card1PcAddSideB.setClickable(true));
        bottomSheetDialog.setOnShowListener(dialogInterface -> binding.progressBarSideBPc.setVisibility(View.GONE));
        bottomSheetDialog.show();

        bottomSheetBinding.saveRecordsButtonTaeBottomSheet.setOnClickListener(view -> validateUserInputTAE(bottomSheetBinding, bottomSheetDialog));

    }

    private void setDefaultFieldsToTAE(TrimesterAbdominalExaminationBottomSheetBinding bottomSheetBinding) {
        if (!sideBObject.isEmpty()) {
            //First Trimester - 1st Month
            if (sideBObject.containsKey("first_trimester_1m_date_visited")) {
                bottomSheetBinding.m1DateVisit1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_1m_date_visited")));
            }
            if (sideBObject.containsKey("first_trimester_1m_aog")) {
                bottomSheetBinding.m1Aog1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_1m_aog")));
            }
            if (sideBObject.containsKey("first_trimester_1m_fundic_height_cm")) {
                bottomSheetBinding.m1FundicHeight1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_1m_fundic_height_cm")));
            }
            if (sideBObject.containsKey("first_trimester_1m_fetal_heart_tone")) {
                bottomSheetBinding.m1Fht1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_1m_fetal_heart_tone")));
            }

            //First Trimester - 2nd Month
            if (sideBObject.containsKey("first_trimester_2m_date_visited")) {
                bottomSheetBinding.m2DateVisit1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_2m_date_visited")));
            }
            if (sideBObject.containsKey("first_trimester_2m_aog")) {
                bottomSheetBinding.m2Aog1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_2m_aog")));
            }
            if (sideBObject.containsKey("first_trimester_2m_fundic_height_cm")) {
                bottomSheetBinding.m2FundicHeight1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_2m_fundic_height_cm")));
            }
            if (sideBObject.containsKey("first_trimester_2m_fetal_heart_tone")) {
                bottomSheetBinding.m2Fht1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_2m_fetal_heart_tone")));
            }

            //First Trimester - 3rd Month
            if (sideBObject.containsKey("first_trimester_3m_date_visited")) {
                bottomSheetBinding.m3DateVisit1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_3m_date_visited")));
            }
            if (sideBObject.containsKey("first_trimester_3m_aog")) {
                bottomSheetBinding.m3Aog1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_3m_aog")));
            }
            if (sideBObject.containsKey("first_trimester_3m_fundic_height_cm")) {
                bottomSheetBinding.m3FundicHeight1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_3m_fundic_height_cm")));
            }
            if (sideBObject.containsKey("first_trimester_3m_fetal_heart_tone")) {
                bottomSheetBinding.m3Fht1stTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("first_trimester_3m_fetal_heart_tone")));
            }

            /*------------------------------------------------------------------------------*/

            //Second Trimester - 1st Month
            if (sideBObject.containsKey("second_trimester_1m_date_visited")) {
                bottomSheetBinding.m1DateVisit2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_1m_date_visited")));
            }
            if (sideBObject.containsKey("second_trimester_1m_aog")) {
                bottomSheetBinding.m1Aog2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_1m_aog")));
            }
            if (sideBObject.containsKey("second_trimester_1m_fundic_height_cm")) {
                bottomSheetBinding.m1FundicHeight2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_1m_fundic_height_cm")));
            }
            if (sideBObject.containsKey("second_trimester_1m_fetal_heart_tone")) {
                bottomSheetBinding.m1Fht2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_1m_fetal_heart_tone")));
            }

            //Second Trimester - 2nd Month
            if (sideBObject.containsKey("second_trimester_2m_date_visited")) {
                bottomSheetBinding.m2DateVisit2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_2m_date_visited")));
            }
            if (sideBObject.containsKey("second_trimester_2m_aog")) {
                bottomSheetBinding.m2Aog2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_2m_aog")));
            }
            if (sideBObject.containsKey("second_trimester_2m_fundic_height_cm")) {
                bottomSheetBinding.m2FundicHeight2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_2m_fundic_height_cm")));
            }
            if (sideBObject.containsKey("second_trimester_2m_fetal_heart_tone")) {
                bottomSheetBinding.m2Fht2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_2m_fetal_heart_tone")));
            }

            //Second Trimester - 3rd Month
            if (sideBObject.containsKey("second_trimester_3m_date_visited")) {
                bottomSheetBinding.m3DateVisit2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_3m_date_visited")));
            }
            if (sideBObject.containsKey("second_trimester_3m_aog")) {
                bottomSheetBinding.m3Aog2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_3m_aog")));
            }
            if (sideBObject.containsKey("second_trimester_3m_fundic_height_cm")) {
                bottomSheetBinding.m3FundicHeight2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_3m_fundic_height_cm")));
            }
            if (sideBObject.containsKey("second_trimester_3m_fetal_heart_tone")) {
                bottomSheetBinding.m3Fht2ndTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_3m_fetal_heart_tone")));
            }

            /*------------------------------------------------------------------------------*/

            //Third Trimester - 1st Month
            if (sideBObject.containsKey("third_trimester_1m_date_visited")) {
                bottomSheetBinding.m1DateVisit3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_1m_date_visited")));
            }
            if (sideBObject.containsKey("third_trimester_1m_aog")) {
                bottomSheetBinding.m1Aog3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_1m_aog")));
            }
            if (sideBObject.containsKey("third_trimester_1m_fundic_height_cm")) {
                bottomSheetBinding.m1FundicHeight3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_1m_fundic_height_cm")));
            }
            if (sideBObject.containsKey("third_trimester_1m_fetal_heart_tone")) {
                bottomSheetBinding.m1Fht3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_1m_fetal_heart_tone")));
            }

            //Third Trimester - 2nd Month
            if (sideBObject.containsKey("third_trimester_2m_date_visited")) {
                bottomSheetBinding.m2DateVisit3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_2m_date_visited")));
            }
            if (sideBObject.containsKey("third_trimester_2m_aog")) {
                bottomSheetBinding.m2Aog3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_2m_aog")));
            }
            if (sideBObject.containsKey("third_trimester_2m_fundic_height_cm")) {
                bottomSheetBinding.m2FundicHeight3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_2m_fundic_height_cm")));
            }
            if (sideBObject.containsKey("third_trimester_2m_fetal_heart_tone")) {
                bottomSheetBinding.m2Fht3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("second_trimester_2m_fetal_heart_tone")));
            }

            //Third Trimester - 3rd Month
            if (sideBObject.containsKey("third_trimester_3m_date_visited")) {
                bottomSheetBinding.m3DateVisit3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_3m_date_visited")));
            }
            if (sideBObject.containsKey("third_trimester_3m_aog")) {
                bottomSheetBinding.m3Aog3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_3m_aog")));
            }
            if (sideBObject.containsKey("third_trimester_3m_fundic_height_cm")) {
                bottomSheetBinding.m3FundicHeight3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_3m_fundic_height_cm")));
            }
            if (sideBObject.containsKey("third_trimester_3m_fetal_heart_tone")) {
                bottomSheetBinding.m3Fht3rdTrimesterEtTaePc.setText(String.valueOf(sideBObject.get("third_trimester_3m_fetal_heart_tone")));
            }
        }
    }

    private void pushDataToMap(String key, String data) {
        if (!data.isEmpty()) {
            sideBObject.put(key, data);
        }
    }

    private void validateUserInputTAE(TrimesterAbdominalExaminationBottomSheetBinding bottomSheetBinding, BottomSheetDialog bottomSheetDialog) {

        //First Trimester - 1st Month
        pushDataToMap("first_trimester_1m_date_visited", bottomSheetBinding.m1DateVisit1stTrimesterEtTaePc.getText().toString());
        pushDataToMap("first_trimester_1m_aog", bottomSheetBinding.m1Aog1stTrimesterEtTaePc.getText().toString());
        pushDataToMap("first_trimester_1m_fundic_height_cm", bottomSheetBinding.m1FundicHeight1stTrimesterEtTaePc.getText().toString());
        pushDataToMap("first_trimester_1m_fetal_heart_tone", bottomSheetBinding.m1Fht1stTrimesterEtTaePc.getText().toString());

        //First Trimester - 2nd Month
        pushDataToMap("first_trimester_2m_date_visited", bottomSheetBinding.m2DateVisit1stTrimesterEtTaePc.getText().toString());
        pushDataToMap("first_trimester_2m_aog", bottomSheetBinding.m2Aog1stTrimesterEtTaePc.getText().toString());
        pushDataToMap("first_trimester_2m_fundic_height_cm", bottomSheetBinding.m2FundicHeight1stTrimesterEtTaePc.getText().toString());
        pushDataToMap("first_trimester_2m_fetal_heart_tone", bottomSheetBinding.m2Fht1stTrimesterEtTaePc.getText().toString());

        //First Trimester - 3rd Month
        pushDataToMap("first_trimester_3m_date_visited", bottomSheetBinding.m3DateVisit1stTrimesterEtTaePc.getText().toString());
        pushDataToMap("first_trimester_3m_aog", bottomSheetBinding.m3Aog1stTrimesterEtTaePc.getText().toString());
        pushDataToMap("first_trimester_3m_fundic_height_cm", bottomSheetBinding.m3FundicHeight1stTrimesterEtTaePc.getText().toString());
        pushDataToMap("first_trimester_3m_fetal_heart_tone", bottomSheetBinding.m3Fht1stTrimesterEtTaePc.getText().toString());

        /*-----------------------------------------------------------------------*/

        //Second Trimester - 1st Month
        pushDataToMap("second_trimester_1m_date_visited", bottomSheetBinding.m1DateVisit2ndTrimesterEtTaePc.getText().toString());
        pushDataToMap("second_trimester_1m_aog", bottomSheetBinding.m1Aog2ndTrimesterEtTaePc.getText().toString());
        pushDataToMap("second_trimester_1m_fundic_height_cm", bottomSheetBinding.m1FundicHeight2ndTrimesterEtTaePc.getText().toString());
        pushDataToMap("second_trimester_1m_fetal_heart_tone", bottomSheetBinding.m1Fht2ndTrimesterEtTaePc.getText().toString());

        //Second Trimester - 2nd Month
        pushDataToMap("second_trimester_2m_date_visited", bottomSheetBinding.m2DateVisit2ndTrimesterEtTaePc.getText().toString());
        pushDataToMap("second_trimester_2m_aog", bottomSheetBinding.m2Aog2ndTrimesterEtTaePc.getText().toString());
        pushDataToMap("second_trimester_2m_fundic_height_cm", bottomSheetBinding.m2FundicHeight2ndTrimesterEtTaePc.getText().toString());
        pushDataToMap("second_trimester_2m_fetal_heart_tone", bottomSheetBinding.m2Fht2ndTrimesterEtTaePc.getText().toString());

        //Second Trimester - 3rd Month
        pushDataToMap("second_trimester_3m_date_visited", bottomSheetBinding.m3DateVisit2ndTrimesterEtTaePc.getText().toString());
        pushDataToMap("second_trimester_3m_aog", bottomSheetBinding.m3Aog2ndTrimesterEtTaePc.getText().toString());
        pushDataToMap("second_trimester_3m_fundic_height_cm", bottomSheetBinding.m3FundicHeight2ndTrimesterEtTaePc.getText().toString());
        pushDataToMap("second_trimester_3m_fetal_heart_tone", bottomSheetBinding.m3Fht2ndTrimesterEtTaePc.getText().toString());

        /*-----------------------------------------------------------------------*/

        //Third Trimester - 1st Month
        pushDataToMap("third_trimester_1m_date_visited", bottomSheetBinding.m1DateVisit3rdTrimesterEtTaePc.getText().toString());
        pushDataToMap("third_trimester_1m_aog", bottomSheetBinding.m1Aog3rdTrimesterEtTaePc.getText().toString());
        pushDataToMap("third_trimester_1m_fundic_height_cm", bottomSheetBinding.m1FundicHeight3rdTrimesterEtTaePc.getText().toString());
        pushDataToMap("third_trimester_1m_fetal_heart_tone", bottomSheetBinding.m1Fht3rdTrimesterEtTaePc.getText().toString());

        //Third Trimester - 2nd Month
        pushDataToMap("third_trimester_2m_date_visited", bottomSheetBinding.m2DateVisit3rdTrimesterEtTaePc.getText().toString());
        pushDataToMap("third_trimester_2m_aog", bottomSheetBinding.m2Aog3rdTrimesterEtTaePc.getText().toString());
        pushDataToMap("third_trimester_2m_fundic_height_cm", bottomSheetBinding.m2FundicHeight3rdTrimesterEtTaePc.getText().toString());
        pushDataToMap("third_trimester_2m_fetal_heart_tone", bottomSheetBinding.m2Fht3rdTrimesterEtTaePc.getText().toString());

        //Third Trimester - 3rd Month
        pushDataToMap("third_trimester_3m_date_visited", bottomSheetBinding.m3DateVisit3rdTrimesterEtTaePc.getText().toString());
        pushDataToMap("third_trimester_3m_aog", bottomSheetBinding.m3Aog3rdTrimesterEtTaePc.getText().toString());
        pushDataToMap("third_trimester_3m_fundic_height_cm", bottomSheetBinding.m3FundicHeight3rdTrimesterEtTaePc.getText().toString());
        pushDataToMap("third_trimester_3m_fetal_heart_tone", bottomSheetBinding.m3Fht3rdTrimesterEtTaePc.getText().toString());

        setCardIndicator(bottomSheetDialog, binding.card1PcAddSideB);
    }

    private void setCardIndicator(BottomSheetDialog bottomSheetDialog, AppCompatButton cardPcAddSideB) {
        bottomSheetDialog.dismiss();
        cardPcAddSideB.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_check_24, 0);
        cardPcAddSideB.setTypeface(cardPcAddSideB.getTypeface(), Typeface.BOLD);
    }

    private void fetchDataToTheData() {

    }

    @Override
    protected void onStop() {
        if (databaseReference != null)
            databaseReference.removeEventListener(valueEventListener);
        super.onStop();
    }

}