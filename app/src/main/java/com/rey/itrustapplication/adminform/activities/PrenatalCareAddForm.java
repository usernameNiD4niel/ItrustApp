package com.rey.itrustapplication.adminform.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.ActivityPrenatalCareAddFormBinding;
import com.rey.itrustapplication.databinding.FamilyHistoryBottomSheetBinding;
import com.rey.itrustapplication.databinding.FamilyPlanningHistoryBottomSheetBinding;
import com.rey.itrustapplication.databinding.ObstetricalHistoryBottomSheetBinding;
import com.rey.itrustapplication.databinding.PastHealthHistoryBottomSheetBinding;
import com.rey.itrustapplication.databinding.PersonalInformationBottomSheetBinding;
import com.rey.itrustapplication.databinding.PhysicalExaminationBottomSheetBinding;
import com.rey.itrustapplication.databinding.ReviewOfSystemBottomSheetBinding;
import com.rey.itrustapplication.databinding.SocialHistoryBottomSheetBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PrenatalCareAddForm extends AppCompatActivity {

    ActivityPrenatalCareAddFormBinding binding;
    private Map<String, Object> prenatalObjectData;
    private String clientFullName = "";

    //ROS
    private String heentSelected = "", chestOrHeartSelected =  "", abdomenSelected =  "", genitalSelected =  "", extremitiesSelected =  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrenatalCareAddFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prenatalObjectData = new HashMap<>();

        binding.saveAndExitPcAddForm.setOnClickListener(view -> saveDataToTheDatabase(true));

        binding.goToSideBPcAddFormButton.setOnClickListener(view -> saveDataToTheDatabase(false));

        binding.card1PcAdd.setOnClickListener(view -> {
            binding.progressIndicatorPcAddForm.setVisibility(View.VISIBLE);
            showPersonalInformationBottomSheet();
        });

        binding.card2PcAdd.setOnClickListener(view -> {
            binding.progressIndicatorPcAddForm.setVisibility(View.VISIBLE);
            showReviewOfSystemBottomSheet();
        });

        binding.card3PcAdd.setOnClickListener(view -> {
            binding.progressIndicatorPcAddForm.setVisibility(View.VISIBLE);
            showFamilyPlanningBottomSheet();
        });

        binding.card5PcAdd.setOnClickListener(view -> {
            binding.progressIndicatorPcAddForm.setVisibility(View.VISIBLE);
            showSocialHistoryBottomSheet();
        });

        binding.card4PcAdd.setOnClickListener(view -> {
            binding.progressIndicatorPcAddForm.setVisibility(View.VISIBLE);
            showPastHealthHistoryBottomSheet();
        });

        binding.card6PcAdd.setOnClickListener(view -> {
            binding.progressIndicatorPcAddForm.setVisibility(View.VISIBLE);
            showObstetricalHistory();
        });

        binding.card7PcAdd.setOnClickListener(view -> {
            binding.progressIndicatorPcAddForm.setVisibility(View.VISIBLE);
            showPhysicalExaminationBottomSheet();
        });

        binding.card8PcAdd.setOnClickListener(view -> {
            binding.progressIndicatorPcAddForm.setVisibility(View.VISIBLE);
            showFamilyPlanningHistoryBottomSheet();
        });
    }

    private void showFamilyPlanningHistoryBottomSheet() {
        binding.card8PcAdd.setClickable(false);
        FamilyPlanningHistoryBottomSheetBinding familyPlanningHistoryBottomSheetBinding = FamilyPlanningHistoryBottomSheetBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrenatalCareAddForm.this);
        bottomSheetDialog.setContentView(familyPlanningHistoryBottomSheetBinding.getRoot());

        setDefaultFieldsToPFamilyPlanningHistory(familyPlanningHistoryBottomSheetBinding);

        bottomSheetDialog.setOnDismissListener(dialogInterface ->  binding.card8PcAdd.setClickable(true));

        bottomSheetDialog.setOnShowListener(dialogInterface -> binding.progressIndicatorPcAddForm.setVisibility(View.GONE));
        bottomSheetDialog.show();
        familyPlanningHistoryBottomSheetBinding.saveRecordsButtonFphBottomSheet.setOnClickListener(view -> validateUserInputFamilyPlanningHistory(familyPlanningHistoryBottomSheetBinding, bottomSheetDialog));
    }

    private void validateUserInputFamilyPlanningHistory(FamilyPlanningHistoryBottomSheetBinding familyPlanningHistoryBottomSheetBinding, BottomSheetDialog bottomSheetDialog) {

        pushDataToMap("family_planning_history_previously_used_method",familyPlanningHistoryBottomSheetBinding.et1FphBottomSheetPc.getText().toString());
        pushDataToMap("family_planning_history_duration",familyPlanningHistoryBottomSheetBinding.et2FphBottomSheetPc.getText().toString());

        //1st dose
        pushDataToMap("family_planning_history_first_dose_vaccine", familyPlanningHistoryBottomSheetBinding.firstDoseVaccineFphPc.getText().toString());
        pushDataToMap("family_planning_history_first_dose_date", familyPlanningHistoryBottomSheetBinding.firstDoseDateFphPc.getText().toString());
        pushDataToMap("family_planning_history_first_dose_remarks", familyPlanningHistoryBottomSheetBinding.firstDoseRemarksFphPc.getText().toString());

        //2nd dose
        pushDataToMap("family_planning_history_second_dose_vaccine", familyPlanningHistoryBottomSheetBinding.secondDoseVaccineFphPc.getText().toString());
        pushDataToMap("family_planning_history_second_dose_date", familyPlanningHistoryBottomSheetBinding.secondDoseDateFphPc.getText().toString());
        pushDataToMap("family_planning_history_second_dose_remarks", familyPlanningHistoryBottomSheetBinding.secondDoseRemarksFphPc.getText().toString());

        //3rd dose
        pushDataToMap("family_planning_history_booster_one_vaccine", familyPlanningHistoryBottomSheetBinding.thirdDoseVaccineFphPc.getText().toString());
        pushDataToMap("family_planning_history_booster_one_date", familyPlanningHistoryBottomSheetBinding.thirdDoseDateFphPc.getText().toString());
        pushDataToMap("family_planning_history_booster_one_remarks", familyPlanningHistoryBottomSheetBinding.thirdDoseRemarksFphPc.getText().toString());

        //4th dose
        pushDataToMap("family_planning_history_booster_two_vaccine", familyPlanningHistoryBottomSheetBinding.fourthDoseVaccineFphPc.getText().toString());
        pushDataToMap("family_planning_history_booster_two_date", familyPlanningHistoryBottomSheetBinding.fourthDoseDateFphPc.getText().toString());
        pushDataToMap("family_planning_history_booster_two_remarks", familyPlanningHistoryBottomSheetBinding.fourthDoseRemarksFphPc.getText().toString());

        bottomSheetDialog.dismiss();
        binding.card8PcAdd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_check_24, 0);
        binding.card8PcAdd.setTypeface(binding.card8PcAdd.getTypeface(), Typeface.BOLD);

    }

    private void setDefaultFieldsToPFamilyPlanningHistory(FamilyPlanningHistoryBottomSheetBinding familyPlanningHistoryBottomSheetBinding) {
        if (!prenatalObjectData.isEmpty()) {
            if (prenatalObjectData.containsKey("family_planning_history_previously_used_method")) {
                familyPlanningHistoryBottomSheetBinding.et1FphBottomSheetPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_previously_used_method")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_duration")) {
                familyPlanningHistoryBottomSheetBinding.et2FphBottomSheetPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_duration")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_first_dose_vaccine")) {
                familyPlanningHistoryBottomSheetBinding.firstDoseVaccineFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_first_dose_vaccine")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_first_dose_date")) {
                familyPlanningHistoryBottomSheetBinding.firstDoseDateFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_first_dose_date")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_first_dose_remarks")) {
                familyPlanningHistoryBottomSheetBinding.firstDoseRemarksFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_first_dose_remarks")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_second_dose_vaccine")) {
                familyPlanningHistoryBottomSheetBinding.secondDoseVaccineFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_second_dose_vaccine")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_second_dose_date")) {
                familyPlanningHistoryBottomSheetBinding.secondDoseDateFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_second_dose_date")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_second_dose_remarks")) {
                familyPlanningHistoryBottomSheetBinding.secondDoseRemarksFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_second_dose_remarks")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_booster_one_vaccine")) {
                familyPlanningHistoryBottomSheetBinding.thirdDoseVaccineFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_booster_one_vaccine")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_booster_one_date")) {
                familyPlanningHistoryBottomSheetBinding.thirdDoseDateFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_booster_one_date")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_booster_one_remarks")) {
                familyPlanningHistoryBottomSheetBinding.thirdDoseRemarksFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_booster_one_remarks")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_booster_two_vaccine")) {
                familyPlanningHistoryBottomSheetBinding.fourthDoseVaccineFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_booster_two_vaccine")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_booster_two_date")) {
                familyPlanningHistoryBottomSheetBinding.fourthDoseDateFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_booster_two_date")));
            }

            if (prenatalObjectData.containsKey("family_planning_history_booster_two_remarks")) {
                familyPlanningHistoryBottomSheetBinding.fourthDoseRemarksFphPc.setText(String.valueOf(prenatalObjectData.get("family_planning_history_booster_two_remarks")));
            }
        }
    }

    private void showPhysicalExaminationBottomSheet() {
        binding.card7PcAdd.setClickable(false);
        PhysicalExaminationBottomSheetBinding physicalExaminationBottomSheetBinding = PhysicalExaminationBottomSheetBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrenatalCareAddForm.this);
        bottomSheetDialog.setContentView(physicalExaminationBottomSheetBinding.getRoot());

        setDefaultFieldsToPhysicalExamination(physicalExaminationBottomSheetBinding);

        bottomSheetDialog.setOnDismissListener(dialogInterface ->  binding.card7PcAdd.setClickable(true));

        bottomSheetDialog.setOnShowListener(dialogInterface -> binding.progressIndicatorPcAddForm.setVisibility(View.GONE));
        bottomSheetDialog.show();
        physicalExaminationBottomSheetBinding.saveRecordsButtonPhhBottomSheet.setOnClickListener(view -> validateUserInputPhysicalExamination(physicalExaminationBottomSheetBinding, bottomSheetDialog));
    }

    private void validateUserInputPhysicalExamination(PhysicalExaminationBottomSheetBinding physicalExaminationBottomSheetBinding, BottomSheetDialog bottomSheetDialog) {

        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check22PeBottomSheetPc.isChecked() &&
                !physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.etOthersPePc.getText().toString().isEmpty()) {
            physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.etOthersPePc.setError("Specify the vaginal examination...");
            physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.etOthersPePc.requestFocus();
            return;
        }

        pushDataToMap("vital_signs_blood_pressure", physicalExaminationBottomSheetBinding.et1PePc.getText().toString());
        pushDataToMap("vital_signs_weight", physicalExaminationBottomSheetBinding.et2PePc.getText().toString());
        pushDataToMap("vital_signs_pulse", physicalExaminationBottomSheetBinding.et3PePc.getText().toString());
        pushDataToMap("vital_signs_height", physicalExaminationBottomSheetBinding.et4PePc.getText().toString());
        pushDataToMap("vital_signs_muac", physicalExaminationBottomSheetBinding.et5PePc.getText().toString());
        pushDataToMap("vital_signs_bmi", physicalExaminationBottomSheetBinding.et6PePc.getText().toString());

        String conjunctivaSelected = "";

        if (physicalExaminationBottomSheetBinding.check1PeBottomSheetPc.isChecked()) conjunctivaSelected = ", Pale";
        if (physicalExaminationBottomSheetBinding.check2PeBottomSheetPc.isChecked()) conjunctivaSelected = ", Yellowish";

        if (!conjunctivaSelected.isEmpty())
            pushDataToMap("conjunctiva", conjunctivaSelected.substring(2));

        String neckSelected = "";

        if (physicalExaminationBottomSheetBinding.check3PeBottomSheetPc.isChecked()) neckSelected = ", Enlarge Thyroid";
        if (physicalExaminationBottomSheetBinding.check4PeBottomSheetPc.isChecked()) neckSelected = ", Enlarge Lymph Node";

        if (!neckSelected.isEmpty()) pushDataToMap("neck", neckSelected.substring(2));

        if (physicalExaminationBottomSheetBinding.check5PeBottomSheetPc.isChecked()) pushDataToMap("breast_mass", "true");

        if (physicalExaminationBottomSheetBinding.check6PeBottomSheetPc.isChecked()) pushDataToMap("breast_right_breast", "true");

        if (physicalExaminationBottomSheetBinding.check7PeBottomSheetPc.isChecked()) pushDataToMap("breast_left_breast", "true");

        if (physicalExaminationBottomSheetBinding.check8PeBottomSheetPc.isChecked()) pushDataToMap("breast_nipple_discharge", "true");

        if (physicalExaminationBottomSheetBinding.check9PeBottomSheetPc.isChecked()) pushDataToMap("breast_skin_orange_peel_or_dimpling", "true");

        if (physicalExaminationBottomSheetBinding.check10PeBottomSheetPc.isChecked()) pushDataToMap("breast_enlarged_axillary_lymph_nodes", "true");

        //Thorax
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check11PeBottomSheetPc.isChecked()) pushDataToMap("thorax_abnormal_heart_sound_cardiac_rate", "true");
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check12PeBottomSheetPc.isChecked()) pushDataToMap("thorax_abnormal_breath_sound_respiratory_rate", "true");

        //Abdomen
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check13PeBottomSheetPc.isChecked()) pushDataToMap("abdomen_enlarge_liver", "true");
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check14PeBottomSheetPc.isChecked()) pushDataToMap("abdomen_mass", "true");
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check15PeBottomSheetPc.isChecked()) pushDataToMap("abdomen_tenderness", "true");

        //Vaginal
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check16PeBottomSheetPc.isChecked()) pushDataToMap("vaginal_bleeding", "true");
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check17PeBottomSheetPc.isChecked()) pushDataToMap("vaginal_cysts_mass", "true");
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check18PeBottomSheetPc.isChecked()) pushDataToMap("vaginal_warts", "true");
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check19PeBottomSheetPc.isChecked()) pushDataToMap("vaginal_discharges", "true");
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check20PeBottomSheetPc.isChecked()) pushDataToMap("vaginal_scars", "true");
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check21PeBottomSheetPc.isChecked()) pushDataToMap("vaginal_lacerations", "true");
        //22 - occupied
        pushDataToMap("others", physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.etOthersPePc.getText().toString());

        //Extremities
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check23PeBottomSheetPc.isChecked()) pushDataToMap("extremities_edema", "true");
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check24PeBottomSheetPc.isChecked()) pushDataToMap("extremities_varicosities", "true");
        if (physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check25PeBottomSheetPc.isChecked()) pushDataToMap("extremities_pain_on_forced_dorsiflexion", "true");
        pushDataToMap("extremities_tt_status", physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.ttStatusEtPePc.getText().toString());

        //Impression Diagnosis
        pushDataToMap("impression_diagnosis_lmp", physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.lmpEtPePc.getText().toString());
        pushDataToMap("impression_diagnosis_edc", physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.edcEtPePc.getText().toString());
        pushDataToMap("impression_diagnosis_g", physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.gEtPePc.getText().toString());
        pushDataToMap("impression_diagnosis_p", physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.pEtPePc.getText().toString());
        pushDataToMap("impression_diagnosis_tpal", physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.tpalEtPePc.getText().toString());

        bottomSheetDialog.dismiss();
        binding.card7PcAdd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_check_24, 0);
        binding.card7PcAdd.setTypeface(binding.card7PcAdd.getTypeface(), Typeface.BOLD);
    }

    private void setDefaultFieldsToPhysicalExamination(PhysicalExaminationBottomSheetBinding physicalExaminationBottomSheetBinding) {

        if (!prenatalObjectData.isEmpty()) {
            if (prenatalObjectData.containsKey("vital_signs_blood_pressure")) physicalExaminationBottomSheetBinding.et1PePc.setText(String.valueOf(prenatalObjectData.get("vital_signs_blood_pressure")));
            if (prenatalObjectData.containsKey("vital_signs_weight")) physicalExaminationBottomSheetBinding.et2PePc.setText(String.valueOf(prenatalObjectData.get("vital_signs_weight")));
            if (prenatalObjectData.containsKey("vital_signs_weight")) physicalExaminationBottomSheetBinding.et3PePc.setText(String.valueOf(prenatalObjectData.get("vital_signs_weight")));
            if (prenatalObjectData.containsKey("vital_signs_height")) physicalExaminationBottomSheetBinding.et4PePc.setText(String.valueOf(prenatalObjectData.get("vital_signs_height")));
            if (prenatalObjectData.containsKey("vital_signs_muac")) physicalExaminationBottomSheetBinding.et5PePc.setText(String.valueOf(prenatalObjectData.get("vital_signs_muac")));
            if (prenatalObjectData.containsKey("vital_signs_bmi")) physicalExaminationBottomSheetBinding.et6PePc.setText(String.valueOf(prenatalObjectData.get("vital_signs_bmi")));

            if (prenatalObjectData.containsKey("conjunctiva")) {
                if (String.valueOf(prenatalObjectData.get("conjunctiva")).contains("Pale")) physicalExaminationBottomSheetBinding.check1PeBottomSheetPc.setChecked(true);
                if (String.valueOf(prenatalObjectData.get("conjunctiva")).contains("Yellowish")) physicalExaminationBottomSheetBinding.check2PeBottomSheetPc.setChecked(true);
            }

            if (prenatalObjectData.containsKey("neck")) {
                if (String.valueOf(prenatalObjectData.get("neck")).contains("Enlarge Thyroid")) physicalExaminationBottomSheetBinding.check3PeBottomSheetPc.setChecked(true);
                if (String.valueOf(prenatalObjectData.get("neck")).contains("Enlarge Lymph Node")) physicalExaminationBottomSheetBinding.check4PeBottomSheetPc.setChecked(true);
            }

            if (prenatalObjectData.containsKey("breast_mass")) physicalExaminationBottomSheetBinding.check5PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("breast_right_breast")) physicalExaminationBottomSheetBinding.check6PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("breast_left_breast")) physicalExaminationBottomSheetBinding.check7PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("breast_nipple_discharge")) physicalExaminationBottomSheetBinding.check8PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("breast_skin_orange_peel_or_dimpling")) physicalExaminationBottomSheetBinding.check9PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("breast_enlarged_axillary_lymph_nodes")) physicalExaminationBottomSheetBinding.check10PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("thorax_abnormal_heart_sound_cardiac_rate")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check11PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("thorax_abnormal_breath_sound_respiratory_rate")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check12PeBottomSheetPc.setChecked(true);

            if (prenatalObjectData.containsKey("abdomen_enlarge_liver")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check13PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("abdomen_mass")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check14PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("abdomen_tenderness")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check15PeBottomSheetPc.setChecked(true);

            if (prenatalObjectData.containsKey("vaginal_bleeding")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check16PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("vaginal_cysts_mass")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check17PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("vaginal_warts")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check18PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("vaginal_discharges")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check19PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("vaginal_scars")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check20PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("vaginal_lacerations")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check21PeBottomSheetPc.setChecked(true);

            if (prenatalObjectData.containsKey("others")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.etOthersPePc.setText(String.valueOf(prenatalObjectData.get("others")));

            if (prenatalObjectData.containsKey("extremities_edema")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check23PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("extremities_varicosities")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check24PeBottomSheetPc.setChecked(true);
            if (prenatalObjectData.containsKey("extremities_pain_on_forced_dorsiflexion")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.check25PeBottomSheetPc.setChecked(true);

            if (prenatalObjectData.containsKey("extremities_tt_status")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.ttStatusEtPePc.setText(String.valueOf(prenatalObjectData.get("extremities_tt_status")));

            if (prenatalObjectData.containsKey("impression_diagnosis_lmp")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.lmpEtPePc.setText(String.valueOf(prenatalObjectData.get("impression_diagnosis_lmp")));
            if (prenatalObjectData.containsKey("impression_diagnosis_edc")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.edcEtPePc.setText(String.valueOf(prenatalObjectData.get("impression_diagnosis_edc")));
            if (prenatalObjectData.containsKey("impression_diagnosis_g")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.gEtPePc.setText(String.valueOf(prenatalObjectData.get("impression_diagnosis_g")));
            if (prenatalObjectData.containsKey("impression_diagnosis_p")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.pEtPePc.setText(String.valueOf(prenatalObjectData.get("impression_diagnosis_p")));
            if (prenatalObjectData.containsKey("impression_diagnosis_tpal")) physicalExaminationBottomSheetBinding.part2PeBottomSheetPc.tpalEtPePc.setText(String.valueOf(prenatalObjectData.get("impression_diagnosis_tpal")));

        }

    }

    private void showPastHealthHistoryBottomSheet() {
        binding.card4PcAdd.setClickable(false);
        PastHealthHistoryBottomSheetBinding bottomSheetBinding = PastHealthHistoryBottomSheetBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrenatalCareAddForm.this);
        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());

        setDefaultFieldsToPHH(bottomSheetBinding);

        bottomSheetDialog.setOnDismissListener(dialogInterface -> binding.card4PcAdd.setClickable(true));

        bottomSheetDialog.setOnShowListener(dialogInterface -> binding.progressIndicatorPcAddForm.setVisibility(View.GONE));
        bottomSheetDialog.show();
        bottomSheetBinding.saveRecordsButtonPhhBottomSheet.setOnClickListener(view -> validateUserInputPHH(bottomSheetBinding, bottomSheetDialog));

    }

    private void validateUserInputPHH(PastHealthHistoryBottomSheetBinding bottomSheetBinding, BottomSheetDialog bottomSheetDialog) {

        if (bottomSheetBinding.pastHealthHistory1Check.isChecked())
            pushDataToMap("allergies", "true");
        if (bottomSheetBinding.pastHealthHistory2Check.isChecked())
            pushDataToMap("drug_intake_anti_tb_anti_diabetic_anti_convulsant", "true");
        if (bottomSheetBinding.pastHealthHistory3Check.isChecked())
            pushDataToMap("bleeding_tendencies_nose_gums_etc", "true");
        if (bottomSheetBinding.pastHealthHistory4Check.isChecked())
            pushDataToMap("anemia", "true");
        if (bottomSheetBinding.pastHealthHistory5Check.isChecked())
            pushDataToMap("diabetes", "true");
        if (bottomSheetBinding.pastHealthHistory6Check.isChecked())
            pushDataToMap("itching_or_sores_in_or_around_the_vagina", "true");
        if (bottomSheetBinding.pastHealthHistory7Check.isChecked())
            pushDataToMap("pain_or_burning_sensation_on_urination", "true");

        bottomSheetDialog.dismiss();
        binding.card4PcAdd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_check_24, 0);
        binding.card4PcAdd.setTypeface(binding.card4PcAdd.getTypeface(), Typeface.BOLD);

    }

    private void setDefaultFieldsToPHH(PastHealthHistoryBottomSheetBinding bottomSheetBinding) {
        if (!prenatalObjectData.isEmpty()) {
            if (prenatalObjectData.containsKey("allergies"))
                bottomSheetBinding.pastHealthHistory1Check.setChecked(true);
            if (prenatalObjectData.containsKey("drug_intake_anti_tb_anti_diabetic_anti_convulsant"))
                bottomSheetBinding.pastHealthHistory2Check.setChecked(true);
            if (prenatalObjectData.containsKey("bleeding_tendencies_nose_gums_etc"))
                bottomSheetBinding.pastHealthHistory3Check.setChecked(true);
            if (prenatalObjectData.containsKey("anemia"))
                bottomSheetBinding.pastHealthHistory4Check.setChecked(true);
            if (prenatalObjectData.containsKey("diabetes"))
                bottomSheetBinding.pastHealthHistory5Check.setChecked(true);
            if (prenatalObjectData.containsKey("itching_or_sores_in_or_around_the_vagina"))
                bottomSheetBinding.pastHealthHistory6Check.setChecked(true);
            if (prenatalObjectData.containsKey("pain_or_burning_sensation_on_urination"))
                bottomSheetBinding.pastHealthHistory7Check.setChecked(true);
        }
    }

    private void saveDataToTheDatabase(boolean isSaveAndExit) {

        if (clientFullName.isEmpty()) {
            Toast.makeText(this, "Please click the Personal Information and fill out the fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (prenatalObjectData.isEmpty()) {
            Toast.makeText(this, "Please fill out the form first before saving it to the database.", Toast.LENGTH_SHORT).show();
            return;
        }

        String nameNode = clientFullName.replaceAll(" ", "_");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", new Locale("fil", "PH"));
        String currentDate = simpleDateFormat.format(new Date());

        SimpleDateFormat time = new SimpleDateFormat("h:mma", Locale.getDefault());
        String currentTime = time.format(new Date());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Forms/PrenatalCare");

        String key = databaseReference.push().getKey();

        prenatalObjectData.put("date_added", currentDate);
        prenatalObjectData.put("time_added", currentTime);
        prenatalObjectData.put("key", key);


        Map<String, Object> highData = new HashMap<>();
        highData.put("node_name", nameNode);
        highData.put("current_date_added", currentDate);
        highData.put("current_time_added", currentTime);
        highData.put("client_name", clientFullName);

        databaseReference.child(nameNode).setValue(highData).addOnSuccessListener(success ->
            databaseReference.child(nameNode).child("SideA/" + key).setValue(prenatalObjectData).addOnSuccessListener(succ -> {
                Toast.makeText(this, "You have successfully added " + clientFullName + " records", Toast.LENGTH_LONG).show();
                prenatalObjectData.clear();
                clientFullName = "";
                if (isSaveAndExit) {
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), ViewMoreSideBPrenatalCare.class);
                    //Forms/PrenatalCare/$node_name/SideA,SideB/push()
                    intent.putExtra("node_name", nameNode);
                    startActivity(intent);
                }
            })
        );
    }

    private void showReviewOfSystemBottomSheet() {

        binding.card2PcAdd.setClickable(false);
        ReviewOfSystemBottomSheetBinding reviewOfSystemObject = ReviewOfSystemBottomSheetBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrenatalCareAddForm.this);
        bottomSheetDialog.setContentView(reviewOfSystemObject.getRoot());

        setDefaultFieldsToROS(reviewOfSystemObject);

        bottomSheetDialog.setOnDismissListener(dialogInterface -> {
            binding.card2PcAdd.setClickable(true);
            heentSelected = "";
            chestOrHeartSelected = "";
            abdomenSelected = "";
            genitalSelected = "";
            extremitiesSelected = "";
        });
        bottomSheetDialog.setOnShowListener(dialogInterface -> binding.progressIndicatorPcAddForm.setVisibility(View.GONE));
        bottomSheetDialog.show();
        reviewOfSystemObject.rosP2BottomSheet.saveRecordsButtonRosBottomSheet.setOnClickListener(view -> validateUserInputROS(reviewOfSystemObject, bottomSheetDialog));

    }

    private void validateUserInputROS(ReviewOfSystemBottomSheetBinding reviewOfSystemObject, BottomSheetDialog bottomSheetDialog) {

        Resources resources = getResources();

        if (reviewOfSystemObject.heent1Check.isChecked()) heentSelected += ", " + resources.getString(R.string.epilepsy_convulsions_seizures);
        if (reviewOfSystemObject.heent2Check.isChecked()) heentSelected += ", " + resources.getString(R.string.severe_headache_dizziness);
        if (reviewOfSystemObject.heent3Check.isChecked()) heentSelected += ", " + resources.getString(R.string.visual_disturbance_blurring_of_vision);
        if (reviewOfSystemObject.heent4Check.isChecked()) heentSelected += ", " + resources.getString(R.string.yellowish_conjunctiva);
        if (reviewOfSystemObject.heent5Check.isChecked()) heentSelected += ", " + resources.getString(R.string.enlarge_thyroid);

        if (reviewOfSystemObject.chestHear1Check.isChecked()) chestOrHeartSelected += ", " + resources.getString(R.string.severe_chest_pain);
        if (reviewOfSystemObject.chestHear2Check.isChecked()) chestOrHeartSelected += ", " + resources.getString(R.string.shortness_of_breath_and_easy_fatigability);
        if (reviewOfSystemObject.chestHear3Check.isChecked()) chestOrHeartSelected += ", " + resources.getString(R.string.breast_and_axillary_masses);
        if (reviewOfSystemObject.chestHear4Check.isChecked()) chestOrHeartSelected += ", " + resources.getString(R.string.nipple_discharges_specify_if_blood_or_pus);

        if (reviewOfSystemObject.abdomen1Check.isChecked()) abdomenSelected += ", " + resources.getString(R.string.mass_in_the_abdomen);
        if (reviewOfSystemObject.abdomen2Check.isChecked()) abdomenSelected += ", " + resources.getString(R.string.history_of_gall_bladder_disease);
        if (reviewOfSystemObject.abdomen3Check.isChecked()) abdomenSelected += ", " + resources.getString(R.string.history_of_liver_disease);

        if (reviewOfSystemObject.genital1Check.isChecked()) genitalSelected += ", " + resources.getString(R.string.vaginal_discharge);
        if (reviewOfSystemObject.genital2Check.isChecked()) genitalSelected += ", " + resources.getString(R.string.intermenstrual_bleeding);
        if (reviewOfSystemObject.genital3Check.isChecked()) genitalSelected += ", " + resources.getString(R.string.postcoital_bleeding);
        if (reviewOfSystemObject.genital4Check.isChecked()) genitalSelected += ", " + resources.getString(R.string.mass_in_the_uterus);

        if (reviewOfSystemObject.rosP2BottomSheet.extremities1Check.isChecked()) extremitiesSelected += ", " + resources.getString(R.string.severe_varicosities);
        if (reviewOfSystemObject.rosP2BottomSheet.extremities2Check.isChecked()) extremitiesSelected += ", " + resources.getString(R.string.swelling_or_severe_pain_in_the_legs_not_related_to_injuries);

        if (heentSelected.length() > 1) heentSelected = heentSelected.substring(2);
        if (chestOrHeartSelected.length() > 1) chestOrHeartSelected = chestOrHeartSelected.substring(2);
        if (abdomenSelected.length() > 1) abdomenSelected = abdomenSelected.substring(2);
        if (genitalSelected.length() > 1) genitalSelected = genitalSelected.substring(2);
        if (extremitiesSelected.length() > 1) extremitiesSelected = extremitiesSelected.substring(2);

        String skin = reviewOfSystemObject.rosP2BottomSheet.skin1Check.isChecked() ? "Yellowish" : "";

        pushDataToMap("heent", heentSelected.trim());
        pushDataToMap("chest_or_heart", chestOrHeartSelected.trim());
        pushDataToMap("abdomen", abdomenSelected.trim());
        pushDataToMap("genital", genitalSelected.trim());
        pushDataToMap("extremities", extremitiesSelected.trim());
        pushDataToMap("skin", skin);

        bottomSheetDialog.dismiss();

        binding.card2PcAdd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_check_24, 0);
        binding.card2PcAdd.setTypeface(binding.card2PcAdd.getTypeface(), Typeface.BOLD);

    }

    private void setDefaultFieldsToROS(ReviewOfSystemBottomSheetBinding reviewOfSystemObject) {
        Resources resources = getResources();

        if (prenatalObjectData.size() > 2) {
            if (prenatalObjectData.containsKey("heent")) {
                if (String.valueOf(prenatalObjectData.get("heent")).contains(resources.getString(R.string.epilepsy_convulsions_seizures))) {
                    reviewOfSystemObject.heent1Check.setChecked(true);
                }
                if (String.valueOf(prenatalObjectData.get("heent")).contains(resources.getString(R.string.severe_headache_dizziness))) {
                    reviewOfSystemObject.heent2Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("heent")).contains(resources.getString(R.string.visual_disturbance_blurring_of_vision))) {
                    reviewOfSystemObject.heent3Check.setChecked(true);
                }
                if (String.valueOf(prenatalObjectData.get("heent")).contains(resources.getString(R.string.yellowish_conjunctiva))) {
                    reviewOfSystemObject.heent4Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("heent")).contains(resources.getString(R.string.enlarge_thyroid))) {
                    reviewOfSystemObject.heent5Check.setChecked(true);
                }

            }

            if (prenatalObjectData.containsKey("chest_or_heart")) {

                if (String.valueOf(prenatalObjectData.get("chest_or_heart")).contains(resources.getString(R.string.severe_chest_pain))) {
                    reviewOfSystemObject.chestHear1Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("chest_or_heart")).contains(resources.getString(R.string.shortness_of_breath_and_easy_fatigability))) {
                    reviewOfSystemObject.chestHear2Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("chest_or_heart")).contains(resources.getString(R.string.breast_and_axillary_masses))) {
                    reviewOfSystemObject.chestHear3Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("chest_or_heart")).contains(resources.getString(R.string.nipple_discharges_specify_if_blood_or_pus))) {
                    reviewOfSystemObject.chestHear4Check.setChecked(true);
                }

            }

            if (prenatalObjectData.containsKey("abdomen")) {

                if (String.valueOf(prenatalObjectData.get("abdomen")).contains(resources.getString(R.string.mass_in_the_abdomen))) {
                    reviewOfSystemObject.abdomen1Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("abdomen")).contains(resources.getString(R.string.history_of_gall_bladder_disease))) {
                    reviewOfSystemObject.abdomen2Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("abdomen")).contains(resources.getString(R.string.history_of_liver_disease))) {
                    reviewOfSystemObject.abdomen3Check.setChecked(true);
                }

            }

            if (prenatalObjectData.containsKey("genital")) {

                if (String.valueOf(prenatalObjectData.get("genital")).contains(resources.getString(R.string.vaginal_discharge))) {
                    reviewOfSystemObject.genital1Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("genital")).contains(resources.getString(R.string.intermenstrual_bleeding))) {
                    reviewOfSystemObject.genital2Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("genital")).contains(resources.getString(R.string.postcoital_bleeding))) {
                    reviewOfSystemObject.genital3Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("genital")).contains(resources.getString(R.string.mass_in_the_uterus))) {
                    reviewOfSystemObject.genital4Check.setChecked(true);
                }


            }

            if (prenatalObjectData.containsKey("extremities")) {

                if (String.valueOf(prenatalObjectData.get("extremities")).contains(resources.getString(R.string.severe_varicosities))) {
                    reviewOfSystemObject.rosP2BottomSheet.extremities1Check.setChecked(true);
                }

                if (String.valueOf(prenatalObjectData.get("extremities")).contains(resources.getString(R.string.swelling_or_severe_pain_in_the_legs_not_related_to_injuries))) {
                    reviewOfSystemObject.rosP2BottomSheet.extremities2Check.setChecked(true);
                }

            }

            if (prenatalObjectData.containsKey("skin")) reviewOfSystemObject.rosP2BottomSheet.skin1Check.setChecked(true);
        }
    }

    private void setDefaultFields(PersonalInformationBottomSheetBinding personalInformationBottomSheetBinding) {
        if (!prenatalObjectData.isEmpty()) {
            if (prenatalObjectData.containsKey("given_name")) personalInformationBottomSheetBinding.givenNameEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("given_name")));
            if (prenatalObjectData.containsKey("last_name")) personalInformationBottomSheetBinding.lastNameEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("last_name")));
            if (prenatalObjectData.containsKey("mi")) personalInformationBottomSheetBinding.middleInitialEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("mi")));
            if (prenatalObjectData.containsKey("age")) personalInformationBottomSheetBinding.ageEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("age")));
            if (prenatalObjectData.containsKey("date_of_birth")) personalInformationBottomSheetBinding.dobEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("date_of_birth")));
            if (prenatalObjectData.containsKey("highest_educ")) personalInformationBottomSheetBinding.heEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("highest_educ")));
            if (prenatalObjectData.containsKey("occupation")) personalInformationBottomSheetBinding.occupationEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("occupation")));
            if (prenatalObjectData.containsKey("spouse_last_name")) personalInformationBottomSheetBinding.spouseLastNameEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("spouse_last_name")));
            if (prenatalObjectData.containsKey("spouse_given_name")) personalInformationBottomSheetBinding.spouseGivenNameEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("spouse_given_name")));
            if (prenatalObjectData.containsKey("spouse_mi")) personalInformationBottomSheetBinding.spouseMiddleInitialEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("spouse_mi")));
            if (prenatalObjectData.containsKey("spouse_age")) personalInformationBottomSheetBinding.spouseAgeEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("spouse_age")));
            if (prenatalObjectData.containsKey("spouse_date_of_birth")) personalInformationBottomSheetBinding.spouseDobEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("spouse_date_of_birth")));
            if (prenatalObjectData.containsKey("spouse_highest_educ")) personalInformationBottomSheetBinding.spouseHeEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("spouse_highest_educ")));
            if (prenatalObjectData.containsKey("spouse_occupation")) personalInformationBottomSheetBinding.spouseOccupationEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("spouse_occupation")));
            if (prenatalObjectData.containsKey("purok")) personalInformationBottomSheetBinding.piP2BottomSheet.purokEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("purok")));
            if (prenatalObjectData.containsKey("barangay")) personalInformationBottomSheetBinding.piP2BottomSheet.barangayEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("barangay")));
            if (prenatalObjectData.containsKey("municipality")) personalInformationBottomSheetBinding.piP2BottomSheet.municipalityEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("municipality")));
            if (prenatalObjectData.containsKey("province")) personalInformationBottomSheetBinding.piP2BottomSheet.provinceEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("province")));
            if (prenatalObjectData.containsKey("average_monthly_salary")) personalInformationBottomSheetBinding.piP2BottomSheet.amiEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("average_monthly_salary")));
            if (prenatalObjectData.containsKey("contact_number")) personalInformationBottomSheetBinding.piP2BottomSheet.contactNumberEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("contact_number")));
            if (prenatalObjectData.containsKey("phic_cat")) personalInformationBottomSheetBinding.piP2BottomSheet.phicCatEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("phic_cat")));
            if (prenatalObjectData.containsKey("number_of_living_children")) personalInformationBottomSheetBinding.piP2BottomSheet.nolcEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("number_of_living_children")));
            if (prenatalObjectData.containsKey("phil_health")) personalInformationBottomSheetBinding.piP2BottomSheet.philHealthEtPiBottomSheet.setText(String.valueOf(prenatalObjectData.get("phil_health")));

            String birthPlan = String.valueOf(prenatalObjectData.get("birth_plan"));

            switch (birthPlan) {
                case "Hospital":
                    personalInformationBottomSheetBinding.piP2BottomSheet.hospitalPiPc.setChecked(true);
                    break;
                case "Rhu":
                    personalInformationBottomSheetBinding.piP2BottomSheet.rhuPiPc.setChecked(true);
                    break;
                case "Lying-in Clinic":
                    personalInformationBottomSheetBinding.piP2BottomSheet.lyingInClinicPiPc.setChecked(true);
                    break;
            }


        }
    }

    private void showPersonalInformationBottomSheet() {

        binding.card1PcAdd.setClickable(false);
        PersonalInformationBottomSheetBinding personalInformationBottomSheetBinding = PersonalInformationBottomSheetBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrenatalCareAddForm.this);
        bottomSheetDialog.setContentView(personalInformationBottomSheetBinding.getRoot());
        setDefaultFields(personalInformationBottomSheetBinding);

        bottomSheetDialog.setOnDismissListener(dialogInterface -> binding.card1PcAdd.setClickable(true));
        bottomSheetDialog.setOnShowListener(dialogInterface -> binding.progressIndicatorPcAddForm.setVisibility(View.GONE));
        bottomSheetDialog.show();

        personalInformationBottomSheetBinding.saveRecordsButtonPiBottomSheet.setOnClickListener(view -> validateUserInputPersonalInformation(personalInformationBottomSheetBinding, bottomSheetDialog));

    }

    private void showFamilyPlanningBottomSheet() {

        binding.card3PcAdd.setClickable(false);
        FamilyHistoryBottomSheetBinding familyHistoryBottomSheetBinding = FamilyHistoryBottomSheetBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrenatalCareAddForm.this);
        bottomSheetDialog.setContentView(familyHistoryBottomSheetBinding.getRoot());

        setDefaultFieldsToFamilyHistory(familyHistoryBottomSheetBinding);

        bottomSheetDialog.setOnDismissListener(dialogInterface -> binding.card3PcAdd.setClickable(true));
        bottomSheetDialog.setOnShowListener(dialogInterface -> binding.progressIndicatorPcAddForm.setVisibility(View.GONE));
        bottomSheetDialog.show();

        familyHistoryBottomSheetBinding.saveRecordsButtonFhBottomSheet.setOnClickListener(view -> validateUserFamilyHistory(familyHistoryBottomSheetBinding, bottomSheetDialog));
    }

    private void validateUserFamilyHistory(FamilyHistoryBottomSheetBinding familyHistoryBottomSheetBinding, BottomSheetDialog bottomSheetDialog) {
        String familyHistory = "";

        if (familyHistoryBottomSheetBinding.familyHistory1Check.isChecked()) familyHistory += ", " + "CVA (stroke)";
        if (familyHistoryBottomSheetBinding.familyHistory2Check.isChecked()) familyHistory += ", " + "Hypertension";
        if (familyHistoryBottomSheetBinding.familyHistory3Check.isChecked()) familyHistory += ", " + "Asthma";
        if (familyHistoryBottomSheetBinding.familyHistory4Check.isChecked()) familyHistory += ", " + "Heart Disease";
        if (familyHistoryBottomSheetBinding.familyHistory5Check.isChecked()) familyHistory += ", " + "Diabetes";

        if (familyHistory.length() > 1) familyHistory = familyHistory.substring(2);

        pushDataToMap("family_history", familyHistory);

        bottomSheetDialog.dismiss();
        binding.card3PcAdd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_check_24, 0);
        binding.card3PcAdd.setTypeface(binding.card3PcAdd.getTypeface(), Typeface.BOLD);
    }

    private void setDefaultFieldsToFamilyHistory(FamilyHistoryBottomSheetBinding familyHistoryBottomSheetBinding) {
        if (!prenatalObjectData.isEmpty() && prenatalObjectData.containsKey("family_history")) {
            if (String.valueOf(prenatalObjectData.get("family_history")).contains("CVA (stroke)"))
                familyHistoryBottomSheetBinding.familyHistory1Check.setChecked(true);
            if (String.valueOf(prenatalObjectData.get("family_history")).contains("Hypertension"))
                familyHistoryBottomSheetBinding.familyHistory2Check.setChecked(true);
            if (String.valueOf(prenatalObjectData.get("family_history")).contains("Asthma"))
                familyHistoryBottomSheetBinding.familyHistory3Check.setChecked(true);
            if (String.valueOf(prenatalObjectData.get("family_history")).contains("Heart Disease"))
                familyHistoryBottomSheetBinding.familyHistory4Check.setChecked(true);
            if (String.valueOf(prenatalObjectData.get("family_history")).contains("Diabetes"))
                familyHistoryBottomSheetBinding.familyHistory5Check.setChecked(true);
        }
    }

    private void validateUserInputPersonalInformation(PersonalInformationBottomSheetBinding personalInformationBottomSheetBinding, BottomSheetDialog bottomSheetDialog) {

        final String lastName = personalInformationBottomSheetBinding.lastNameEtPiBottomSheet.getEditableText().toString();
        final String givenName = personalInformationBottomSheetBinding.givenNameEtPiBottomSheet.getEditableText().toString();
        final String mi = personalInformationBottomSheetBinding.middleInitialEtPiBottomSheet.getEditableText().toString();
        final String dob = personalInformationBottomSheetBinding.dobEtPiBottomSheet.getEditableText().toString();
        final String highestEduc = personalInformationBottomSheetBinding.heEtPiBottomSheet.getEditableText().toString();
        final String age = personalInformationBottomSheetBinding.ageEtPiBottomSheet.getEditableText().toString();
        final String occupation = personalInformationBottomSheetBinding.occupationEtPiBottomSheet.getEditableText().toString();

        //Spouse
        final String lastNameSpouse = personalInformationBottomSheetBinding.spouseLastNameEtPiBottomSheet.getEditableText().toString();
        final String givenNameSpouse = personalInformationBottomSheetBinding.spouseGivenNameEtPiBottomSheet.getEditableText().toString();
        final String miSpouse = personalInformationBottomSheetBinding.spouseMiddleInitialEtPiBottomSheet.getEditableText().toString();
        final String dobSpouse = personalInformationBottomSheetBinding.spouseDobEtPiBottomSheet.getEditableText().toString();
        final String highestEducSpouse = personalInformationBottomSheetBinding.spouseHeEtPiBottomSheet.getEditableText().toString();
        final String ageSpouse = personalInformationBottomSheetBinding.spouseAgeEtPiBottomSheet.getEditableText().toString();
        final String occupationSpouse = personalInformationBottomSheetBinding.spouseOccupationEtPiBottomSheet.getEditableText().toString();

        //Address
        final String purok = personalInformationBottomSheetBinding.piP2BottomSheet.purokEtPiBottomSheet.getEditableText().toString();
        final String barangay = personalInformationBottomSheetBinding.piP2BottomSheet.barangayEtPiBottomSheet.getEditableText().toString();
        final String municipality = personalInformationBottomSheetBinding.piP2BottomSheet.municipalityEtPiBottomSheet.getEditableText().toString();
        final String province = personalInformationBottomSheetBinding.piP2BottomSheet.provinceEtPiBottomSheet.getEditableText().toString();

        //Personal
        final String averageMonthlyIncome = personalInformationBottomSheetBinding.piP2BottomSheet.amiEtPiBottomSheet.getEditableText().toString();
        final String contactNumber = personalInformationBottomSheetBinding.piP2BottomSheet.contactNumberEtPiBottomSheet.getEditableText().toString();
        final String phicCat = personalInformationBottomSheetBinding.piP2BottomSheet.phicCatEtPiBottomSheet.getEditableText().toString();
        final String numberOfLivingChildren = personalInformationBottomSheetBinding.piP2BottomSheet.nolcEtPiBottomSheet.getEditableText().toString();
        final String philHealth = personalInformationBottomSheetBinding.piP2BottomSheet.philHealthEtPiBottomSheet.getEditableText().toString();

        if (lastName.isEmpty()) {
            personalInformationBottomSheetBinding.lastNamePersonalInfoPc.setError("Last Name is required, please fill out this field correctly");
            personalInformationBottomSheetBinding.lastNamePersonalInfoPc.requestFocus();
            return;
        }

        if (personalInformationBottomSheetBinding.lastNamePersonalInfoPc.isErrorEnabled()) {
            personalInformationBottomSheetBinding.lastNamePersonalInfoPc.setError("");
            personalInformationBottomSheetBinding.lastNamePersonalInfoPc.setErrorEnabled(false);
        }

        if (givenName.isEmpty()) {
            personalInformationBottomSheetBinding.givenNamePersonalInfoPc.setError("Last Name is required, please fill out this field correctly");
            personalInformationBottomSheetBinding.givenNamePersonalInfoPc.requestFocus();
            return;
        }

        if (personalInformationBottomSheetBinding.givenNamePersonalInfoPc.isErrorEnabled()) {
            personalInformationBottomSheetBinding.givenNamePersonalInfoPc.setError("");
            personalInformationBottomSheetBinding.givenNamePersonalInfoPc.setErrorEnabled(false);
        }

        String birthPlan = "";
        if (personalInformationBottomSheetBinding.piP2BottomSheet.lyingInClinicPiPc.isChecked()) birthPlan += ", " + "Lying-in Clinic";
        if (personalInformationBottomSheetBinding.piP2BottomSheet.hospitalPiPc.isChecked()) birthPlan += ", " + "Hospital";
        if (personalInformationBottomSheetBinding.piP2BottomSheet.rhuPiPc.isChecked()) birthPlan += ", " + "Rhu";

        if (!birthPlan.isEmpty()) {
            birthPlan = birthPlan.substring(2);
        }

        clientFullName = givenName + " " + mi + " " + lastName;

        pushDataToMap("given_name", givenName);
        pushDataToMap("last_name", lastName);
        pushDataToMap("mi", mi);
        pushDataToMap("age", age);
        pushDataToMap("date_of_birth", dob);
        pushDataToMap("highest_educ", highestEduc);
        pushDataToMap("occupation", occupation);

        pushDataToMap("spouse_last_name", lastNameSpouse);
        pushDataToMap("spouse_given_name", givenNameSpouse);
        pushDataToMap("spouse_mi", miSpouse);
        pushDataToMap("spouse_age", ageSpouse);
        pushDataToMap("spouse_date_of_birth", dobSpouse);
        pushDataToMap("spouse_highest_educ", highestEducSpouse);
        pushDataToMap("spouse_occupation", occupationSpouse);

        pushDataToMap("purok", purok);
        pushDataToMap("barangay", barangay);
        pushDataToMap("municipality", municipality);
        pushDataToMap("province", province);

        pushDataToMap("average_monthly_salary", averageMonthlyIncome);
        pushDataToMap("contact_number", contactNumber);
        pushDataToMap("phic_cat", phicCat);
        pushDataToMap("number_of_living_children", numberOfLivingChildren);
        pushDataToMap("phil_health", philHealth);

        pushDataToMap("birth_plan", birthPlan);


        bottomSheetDialog.dismiss();
        binding.card1PcAdd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_check_24, 0);
        binding.card1PcAdd.setTypeface(binding.card1PcAdd.getTypeface(), Typeface.BOLD);
    }

    private void pushDataToMap(String key, String data) {
        if (!data.isEmpty()) {
            prenatalObjectData.put(key, data);
        }
    }

    private void showSocialHistoryBottomSheet() {

        binding.card5PcAdd.setClickable(false);
        SocialHistoryBottomSheetBinding bottomSheetBinding = SocialHistoryBottomSheetBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrenatalCareAddForm.this);
        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());

        setDefaultFieldsToSocialHistory(bottomSheetBinding);

        bottomSheetDialog.setOnDismissListener(dialogInterface -> {
            binding.card5PcAdd.setClickable(true);
            bottomSheetBinding.socialHistory1Check.setOnCheckedChangeListener(null);
            bottomSheetBinding.socialHistory2Check.setOnCheckedChangeListener(null);
        });
        bottomSheetDialog.setOnShowListener(dialogInterface -> binding.progressIndicatorPcAddForm.setVisibility(View.GONE));
        bottomSheetDialog.show();
        bottomSheetBinding.saveRecordsButtonShBottomSheet.setOnClickListener(view -> validateUserSocialHistory(bottomSheetBinding, bottomSheetDialog));

        bottomSheetBinding.socialHistory1Check.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                bottomSheetBinding.et1ShPc.requestFocus();
            }
        });

        bottomSheetBinding.socialHistory2Check.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                bottomSheetBinding.et2ShPc.requestFocus();
            }
        });
    }

    private void validateUserSocialHistory(SocialHistoryBottomSheetBinding bottomSheetBinding, BottomSheetDialog bottomSheetDialog) {

        if (bottomSheetBinding.socialHistory1Check.isChecked()) {
            pushDataToMap("smoking", "true");
            pushDataToMap("stick_per_day", bottomSheetBinding.et1ShPc.getText().toString());
        }

        if (bottomSheetBinding.socialHistory2Check.isChecked()) {
            pushDataToMap("alcohol_beverage", "true");
            String amountPerDay = bottomSheetBinding.et2ShPc.getText().toString();
            pushDataToMap("amount_per_day", amountPerDay);
        }

        if (bottomSheetBinding.socialHistory3Check.isChecked())
            prenatalObjectData.put("obesity", "true");
        if (bottomSheetBinding.socialHistory4Check.isChecked())
            prenatalObjectData.put("history_of_domestic_violence_or_vaw", "true");
        if (bottomSheetBinding.socialHistory5Check.isChecked())
            prenatalObjectData.put("unpleasant_relationship_with_partner", "true");
        if (bottomSheetBinding.socialHistory6Check.isChecked())
            prenatalObjectData.put("treated_stis_in_the_past", "true");

        bottomSheetDialog.dismiss();
        binding.card5PcAdd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_check_24, 0);
        binding.card5PcAdd.setTypeface(binding.card5PcAdd.getTypeface(), Typeface.BOLD);
    }

    private void setDefaultFieldsToSocialHistory(SocialHistoryBottomSheetBinding bottomSheetBinding) {

        if (!prenatalObjectData.isEmpty()) {
            if (prenatalObjectData.containsKey("smoking")) bottomSheetBinding.socialHistory1Check.setChecked(true);
            if (prenatalObjectData.containsKey("stick_per_day")) bottomSheetBinding.et1ShPc.setText(String.valueOf(prenatalObjectData.get("stick_per_day")));
            if (prenatalObjectData.containsKey("alcohol_beverage")) bottomSheetBinding.socialHistory2Check.setChecked(true);
            if (prenatalObjectData.containsKey("amount_per_day")) bottomSheetBinding.et2ShPc.setText(String.valueOf(prenatalObjectData.get("amount_per_day")));
            if (prenatalObjectData.containsKey("obesity")) bottomSheetBinding.socialHistory3Check.setChecked(true);
            if (prenatalObjectData.containsKey("history_of_domestic_violence_or_vaw")) bottomSheetBinding.socialHistory4Check.setChecked(true);
            if (prenatalObjectData.containsKey("unpleasant_relationship_with_partner")) bottomSheetBinding.socialHistory5Check.setChecked(true);
            if (prenatalObjectData.containsKey("treated_stis_in_the_past")) bottomSheetBinding.socialHistory6Check.setChecked(true);

        }

    }

    private void showObstetricalHistory() {

        binding.card5PcAdd.setClickable(false);
        ObstetricalHistoryBottomSheetBinding bottomSheetBinding = ObstetricalHistoryBottomSheetBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrenatalCareAddForm.this);
        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());

        setDefaultFieldsToObstetricalHistory(bottomSheetBinding);

        bottomSheetDialog.setOnDismissListener(dialogInterface -> binding.card5PcAdd.setClickable(true));
        bottomSheetDialog.setOnShowListener(dialogInterface -> binding.progressIndicatorPcAddForm.setVisibility(View.GONE));
        bottomSheetDialog.show();
        bottomSheetBinding.saveRecordsButtonOhBottomSheet.setOnClickListener(view -> validateUserObstetricalHistory(bottomSheetBinding, bottomSheetDialog));
    }

    private void validateUserObstetricalHistory(ObstetricalHistoryBottomSheetBinding bottomSheetBinding, BottomSheetDialog bottomSheetDialog) {
        pushDataToMap("number_of_pregnancies_full_term", bottomSheetBinding.nopFullTermEtOhBottomSheet.getText().toString());
        pushDataToMap("number_of_pregnancies_premature", bottomSheetBinding.nopPrematureEtOhBottomSheet.getText().toString());
        pushDataToMap("number_of_pregnancies_abortion", bottomSheetBinding.nopAbortionEtOhBottomSheet.getText().toString());
        pushDataToMap("number_of_pregnancies_living", bottomSheetBinding.nopLivingEtOhBottomSheet.getText().toString());

        if (bottomSheetBinding.obstetricalHistory1Check.isChecked())
            pushDataToMap("history_of_ectopic_pregnancy", "true");
        if (bottomSheetBinding.obstetricalHistory2Check.isChecked())
            pushDataToMap("hydatidiform_mole_with_the_last_12_months", "true");

        pushDataToMap("history_of_previous_delivery_date_of_last_delivery", bottomSheetBinding.doldEtOhBottomSheet.getText().toString());
        pushDataToMap("history_of_previous_delivery_type_of_last_delivery", bottomSheetBinding.toldEtOhBottomSheet.getText().toString());
        pushDataToMap("history_of_previous_delivery_birth_attendant_in_last_delivery", bottomSheetBinding.baildEtOhBottomSheet.getText().toString());

        pushDataToMap("menstrual_history_last_menstrual_period", bottomSheetBinding.lmpEtOhBottomSheet.getText().toString());
        pushDataToMap("menstrual_history_period_menstrual_previous", bottomSheetBinding.pmpEtOhBottomSheet.getText().toString());
        pushDataToMap("menstrual_history_duration_of_menstrual_bleeding", bottomSheetBinding.dombEtOhBottomSheet.getText().toString());
        pushDataToMap("menstrual_history_character_of_menstrual_bleeding_of_pads", bottomSheetBinding.combopEtOhBottomSheet.getText().toString());

        bottomSheetDialog.dismiss();
        binding.card6PcAdd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_check_24, 0);
        binding.card6PcAdd.setTypeface(binding.card6PcAdd.getTypeface(), Typeface.BOLD);

    }

    private void setDefaultFieldsToObstetricalHistory(ObstetricalHistoryBottomSheetBinding bottomSheetBinding) {
        if (!prenatalObjectData.isEmpty()) {
            if (prenatalObjectData.containsKey("number_of_pregnancies_full_term")) {
                bottomSheetBinding.nopFullTermEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("number_of_pregnancies_full_term")));
            }
            if (prenatalObjectData.containsKey("number_of_pregnancies_premature")) {
                bottomSheetBinding.nopPrematureEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("number_of_pregnancies_premature")));
            }

            if (prenatalObjectData.containsKey("number_of_pregnancies_abortion")) {
                bottomSheetBinding.nopAbortionEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("number_of_pregnancies_abortion")));
            }

            if (prenatalObjectData.containsKey("number_of_pregnancies_living")) {
                bottomSheetBinding.nopLivingEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("number_of_pregnancies_living")));
            }

            if (prenatalObjectData.containsKey("history_of_ectopic_pregnancy")) {
                bottomSheetBinding.obstetricalHistory1Check.setChecked(true);
            }

            if (prenatalObjectData.containsKey("hydatidiform_mole_with_the_last_12_months")) {
                bottomSheetBinding.obstetricalHistory2Check.setChecked(true);
            }

            if (prenatalObjectData.containsKey("history_of_previous_delivery_date_of_last_delivery")) {
                bottomSheetBinding.doldEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("history_of_previous_delivery_date_of_last_delivery")));
            }

            if (prenatalObjectData.containsKey("history_of_previous_delivery_type_of_last_delivery")) {
                bottomSheetBinding.toldEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("history_of_previous_delivery_type_of_last_delivery")));
            }

            if (prenatalObjectData.containsKey("history_of_previous_delivery_birth_attendant_in_last_delivery")) {
                bottomSheetBinding.baildEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("history_of_previous_delivery_birth_attendant_in_last_delivery")));
            }

            if (prenatalObjectData.containsKey("menstrual_history_last_menstrual_period")) {
                bottomSheetBinding.lmpEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("menstrual_history_last_menstrual_period")));
            }

            if (prenatalObjectData.containsKey("menstrual_history_period_menstrual_previous")) {
                bottomSheetBinding.pmpEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("menstrual_history_period_menstrual_previous")));
            }

            if (prenatalObjectData.containsKey("menstrual_history_duration_of_menstrual_bleeding")) {
                bottomSheetBinding.dombEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("menstrual_history_duration_of_menstrual_bleeding")));
            }

            if (prenatalObjectData.containsKey("menstrual_history_character_of_menstrual_bleeding_of_pads")) {
                bottomSheetBinding.combopEtOhBottomSheet.setText(String.valueOf(prenatalObjectData.get("menstrual_history_character_of_menstrual_bleeding_of_pads")));
            }
        }
    }

}