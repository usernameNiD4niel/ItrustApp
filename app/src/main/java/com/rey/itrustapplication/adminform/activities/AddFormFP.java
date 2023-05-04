package com.rey.itrustapplication.adminform.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.adminform.adapter.FormLayoutAdapter;
import com.rey.itrustapplication.adminform.model.FamilyPlanningModel;
import com.rey.itrustapplication.adminform.model.SideAInsider;
import com.rey.itrustapplication.databinding.ActivityAddFormFpBinding;

public class AddFormFP extends AppCompatActivity {

    ActivityAddFormFpBinding binding;
    private FormLayoutAdapter formLayoutAdapter;
    private ValueEventListener valueEventListener;
    private SideAInsider sideAInsider;
    private DatabaseReference databaseReference;
    private FamilyPlanningModel familyPlanningModel;
    private boolean isAddingExistingRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFormFpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String patientName = getIntent().getStringExtra("patient_name");
        String currentDate = getIntent().getStringExtra("current_date");
        boolean isPreviewing = getIntent().getBooleanExtra("isPreviewing", false);
        isAddingExistingRecord = getIntent().getBooleanExtra("isAddingExistingRecord", false);
        familyPlanningModel = (FamilyPlanningModel) getIntent().getSerializableExtra("model");

        binding.backButtonAddFormFp.setOnClickListener(view -> onBackPressed());

        if (isPreviewing) {
            patientName = patientName.trim();
            binding.progressBarAddFormFp.setVisibility(View.VISIBLE);
            binding.parentContainerAddForm.setClickable(false);
            binding.parentContainerAddForm.setFocusable(false);
            sideAInsider = new SideAInsider();
            databaseReference = FirebaseDatabase.getInstance().getReference("Forms/FamilyPlanning/SideA/" + patientName + "/" + currentDate);
            loadMoreSideA();
            return;
        }

        formLayoutAdapter = new FormLayoutAdapter(sideAInsider, familyPlanningModel, isAddingExistingRecord, this);
        binding.recyclerViewAddFormFp.setAdapter(formLayoutAdapter);
        binding.recyclerViewAddFormFp.setHasFixedSize(true);
        binding.recyclerViewAddFormFp.setItemViewCacheSize(5);



    }

    @Override
    protected void onStop() {
        if (databaseReference != null)
            databaseReference.removeEventListener(valueEventListener);
        super.onStop();
    }

    private void loadMoreSideA() {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                if (data.exists()) {
                    Toast.makeText(AddFormFP.this, "Exist", Toast.LENGTH_SHORT).show();
                    if (data.hasChild("planning_to_have_children"))
                        sideAInsider.setPlanningToHaveChildren(data.child("planning_to_have_children").getValue(Boolean.class) + "");
                    if (data.hasChild("address"))
                        sideAInsider.setAddress(data.child("address").getValue(String.class));
                    if (data.hasChild("contact_number"))
                        sideAInsider.setContactNumber(data.child("contact_number").getValue(String.class));
                    if (data.hasChild("civil_status"))
                        sideAInsider.setCivilStatus(data.child("civil_status").getValue(String.class));
                    if (data.hasChild("religion"))
                        sideAInsider.setReligion(data.child("religion").getValue(String.class));
                    if (data.hasChild("last_name_spouse"))
                        sideAInsider.setLastNameSpouse(data.child("last_name_spouse").getValue(String.class));
                    if (data.hasChild("given_name_spouse"))
                        sideAInsider.setGivenNameSpouse(data.child("given_name_spouse").getValue(String.class));
                    if (data.hasChild("contact_number_spouse"))
                        sideAInsider.setContactNumberSpouse(data.child("contact_number_spouse").getValue(String.class));
                    if (data.hasChild("mi"))
                        sideAInsider.setMiddleInitial(data.child("mi").getValue(String.class));
                    if (data.hasChild("civil_status_spouse"))
                        sideAInsider.setCivilStatusSpouse(data.child("civil_status_spouse").getValue(String.class));
                    if (data.hasChild("religion_spouse"))
                        sideAInsider.setReligionSpouse(data.child("religion_spouse").getValue(String.class));
                    if (data.hasChild("no_of_children"))
                        sideAInsider.setNoOfChildren(data.child("no_of_children").getValue(String.class));
                    if (data.hasChild("average_monthly_income"))
                        sideAInsider.setAverageMonthlyIncome(data.child("average_monthly_income").getValue(String.class));

                    if (data.hasChild("new_acceptor"))
                        sideAInsider.setNewAcceptor(data.child("new_acceptor").getValue(Boolean.class) + "");
                    if (data.hasChild("reason_for_family_planning"))
                        sideAInsider.setReasonForFamilyPlanning(data.child("reason_for_family_planning").getValue(String.class));
                    if (data.hasChild("drop_or_restart_reason"))
                        sideAInsider.setDropOrRestartReason(data.child("drop_or_restart_reason").getValue(String.class));
                    if (data.hasChild("iud"))
                        sideAInsider.setIud(data.child("iud").getValue(String.class));
                    if (data.hasChild("current_user"))
                        sideAInsider.setCurrentUser(data.child("current_user").getValue(Boolean.class) + "");
                    if (data.hasChild("changing_method"))
                        sideAInsider.setChangingMethod(data.child("changing_method").getValue(Boolean.class) + "");
                    if (data.hasChild("changing_clinic"))
                        sideAInsider.setChangingClinic(data.child("changing_clinic").getValue(Boolean.class) + "");
                    if (data.hasChild("coc"))
                        sideAInsider.setCoc(data.child("coc").getValue(Boolean.class) + "");
                    if (data.hasChild("pop"))
                        sideAInsider.setPop(data.child("pop").getValue(Boolean.class) + "");
                    if (data.hasChild("injectable"))
                        sideAInsider.setInjectable(data.child("injectable").getValue(Boolean.class) + "");
                    if (data.hasChild("implant"))
                        sideAInsider.setImplant(data.child("implant").getValue(Boolean.class) + "");
                    if (data.hasChild("lam"))
                        sideAInsider.setLam(data.child("lam").getValue(Boolean.class) + "");
                    if (data.hasChild("condom"))
                        sideAInsider.setCondom(data.child("condom").getValue(Boolean.class) + "");
                    if (data.hasChild("others_method_currently_used"))
                        sideAInsider.setOthersMethodCurrentlyUsed(data.child("others_method_currently_used").getValue(String.class));
                    if (data.hasChild("bom"))
                        sideAInsider.setBom(data.child("bom").getValue(Boolean.class) + "");
                    if (data.hasChild("bbt"))
                        sideAInsider.setBbt(data.child("bbt").getValue(Boolean.class) + "");
                    if (data.hasChild("stm"))
                        sideAInsider.setStm(data.child("stm").getValue(Boolean.class) + "");
                    if (data.hasChild("sdm"))
                        sideAInsider.setSdm(data.child("sdm").getValue(Boolean.class) + "");
                    if (data.hasChild("severe_headaches_migraine"))
                        sideAInsider.setSevereHeadachesMigraine(data.child("severe_headaches_migraine").getValue(String.class));
                    if (data.hasChild("history_if_stroke_heart_attack_hypertension"))
                        sideAInsider.setHistoryIfHeartAttack(data.child("history_if_stroke_heart_attack_hypertension").getValue(String.class));
                    if (data.hasChild("non_traumatic_hematoma_frequent_bruising_or_gum_bleeding"))
                        sideAInsider.setNonTraumaticHematoma(data.child("non_traumatic_hematoma_frequent_bruising_or_gum_bleeding").getValue(String.class));
                    if (data.hasChild("current_or_history_of_breast_cancer_breast_mass"))
                        sideAInsider.setCurrentOrHistory(data.child("current_or_history_of_breast_cancer_breast_mass").getValue(String.class));
                    if (data.hasChild("severe_chest_pain"))
                        sideAInsider.setSevereChestPain(data.child("severe_chest_pain").getValue(String.class));
                    if (data.hasChild("cough_for_more_than_14_days"))
                        sideAInsider.setCoughForMoreThan(data.child("cough_for_more_than_14_days").getValue(String.class));
                    if (data.hasChild("jaundice"))
                        sideAInsider.setJaundice(data.child("jaundice").getValue(String.class));
                    if (data.hasChild("unexplained_vaginal_bleeding"))
                        sideAInsider.setUnexplainedVaginalBleeding(data.child("unexplained_vaginal_bleeding").getValue(String.class));
                    if (data.hasChild("abnormal_vaginal_discharge"))
                        sideAInsider.setAbnormalVaginalDischarge(data.child("abnormal_vaginal_discharge").getValue(String.class));
                    if (data.hasChild("intake_of_phenobarbital_anti_seizure_or_rifampicin_anti_tb"))
                        sideAInsider.setIntakeOfPhenobarbital(data.child("intake_of_phenobarbital_anti_seizure_or_rifampicin_anti_tb").getValue(String.class));
                    if (data.hasChild("is_the_client_a_smoker"))
                        sideAInsider.setIsTheClientASmoker(data.child("is_the_client_a_smoker").getValue(String.class));
                    if (data.hasChild("with_disability"))
                        sideAInsider.setWithDisability(data.child("with_disability").getValue(String.class));
                    if (data.hasChild("weight"))
                        sideAInsider.setWeight(data.child("weight").getValue(String.class));
                    if (data.hasChild("height"))
                        sideAInsider.setHeight(data.child("height").getValue(String.class));
                    if (data.hasChild("blood_pressure"))
                        sideAInsider.setBloodPressure(data.child("blood_pressure").getValue(String.class));
                    if (data.hasChild("pulse_rate"))
                        sideAInsider.setPulseRate(data.child("pulse_rate").getValue(String.class));
                    if (data.hasChild("skin"))
                        sideAInsider.setSkin(data.child("skin").getValue(String.class));
                    if (data.hasChild("conjunctiva"))
                        sideAInsider.setConjunctiva(data.child("conjunctiva").getValue(String.class));
                    if (data.hasChild("neck"))
                        sideAInsider.setNeck(data.child("neck").getValue(String.class));
                    if (data.hasChild("breast"))
                        sideAInsider.setBreast(data.child("breast").getValue(String.class));
                    if (data.hasChild("abdomen"))
                        sideAInsider.setAbdomen(data.child("abdomen").getValue(String.class));
                    if (data.hasChild("extremities"))
                        sideAInsider.setExtremities(data.child("extremities").getValue(String.class));
                    if (data.hasChild("pelvic_examination"))
                        sideAInsider.setPelvicExamination(data.child("pelvic_examination").getValue(String.class));
                    if (data.hasChild("pelvic_examination_cervical_abnormalities"))
                        sideAInsider.setPelvicExaminationCervicalAbnormalities(data.child("pelvic_examination_cervical_abnormalities").getValue(String.class));
                    if (data.hasChild("pelvic_examination_cervical_consistency"))
                        sideAInsider.setPelvicExaminationCervicalConsistency(data.child("pelvic_examination_cervical_consistency").getValue(String.class));
                    if (data.hasChild("pelvic_examination_uterine_position"))
                        sideAInsider.setPelvicExaminationUterinePosition(data.child("pelvic_examination_uterine_position").getValue(String.class));
                    if (data.hasChild("uterine_depth_cm"))
                        sideAInsider.setUterineDepthCm(data.child("uterine_depth_cm").getValue(String.class));
                    if (data.hasChild("type_of_last_delivery"))
                        sideAInsider.setTypeOfLastDelivery(data.child("type_of_last_delivery").getValue(String.class));
                    if (data.hasChild("number_of_pregnancy"))
                        sideAInsider.setNumberOfPregnancy(data.child("number_of_pregnancy").getValue(String.class));
                    if (data.hasChild("number_of_pregnancy_full_term"))
                        sideAInsider.setNumberOfPregnancyFullTerm(data.child("number_of_pregnancy_full_term").getValue(String.class));
                    if (data.hasChild("number_of_pregnancy_premature"))
                        sideAInsider.setNumberOfPregnancyPremature(data.child("number_of_pregnancy_premature").getValue(String.class));
                    if (data.hasChild("number_of_pregnancy_abortion"))
                        sideAInsider.setNumberOfPregnancyAbortion(data.child("number_of_pregnancy_abortion").getValue(String.class));
                    if (data.hasChild("number_of_pregnant_living_children"))
                        sideAInsider.setNumberOfPregnantLivingChildren(data.child("number_of_pregnant_living_children").getValue(String.class));
                    if (data.hasChild("date_of_last_delivery"))
                        sideAInsider.setDateOfLastDelivery(data.child("date_of_last_delivery").getValue(String.class));
                    if (data.hasChild("last_of_menstrual_period"))
                        sideAInsider.setLastOfMenstrualPeriod(data.child("last_of_menstrual_period").getValue(String.class));
                    if (data.hasChild("previous_menstrual_period"))
                        sideAInsider.setPreviousMenstrualPeriod(data.child("previous_menstrual_period").getValue(String.class));
                    if (data.hasChild("dysmenorrhea"))
                        sideAInsider.setDysmenorrhea(data.child("dysmenorrhea").getValue(Boolean.class) + "");
                    if (data.hasChild("hydatidiform"))
                        sideAInsider.setHydatidiform(data.child("hydatidiform").getValue(Boolean.class) + "");
                    if (data.hasChild("history_of_ectopic_pregnancy"))
                        sideAInsider.setHistoryOfEctopicPregnancy(data.child("history_of_ectopic_pregnancy").getValue(Boolean.class) + "");
                    if (data.hasChild("menstrual_flow"))
                        sideAInsider.setMenstrualFlow(data.child("menstrual_flow").getValue(String.class));
                    if (data.hasChild("unpleasant_relationship_with_partner"))
                        sideAInsider.setUnpleasantRelationshipWithPartner(data.child("unpleasant_relationship_with_partner").getValue(String.class));
                    if (data.hasChild("partner_does_not_approve_of_the_visit_to_fp_clinic"))
                        sideAInsider.setPartnerDoesNotApproveOfTheVisitToFpClinic(data.child("partner_does_not_approve_of_the_visit_to_fp_clinic").getValue(String.class));
                    if (data.hasChild("history_of_domestic_violence_or_vaw"))
                        sideAInsider.setHistoryOfDomesticViolenceOrVaw(data.child("history_of_domestic_violence_or_vaw").getValue(String.class));
                    if (data.hasChild("referred_to"))
                        sideAInsider.setReferredTo(data.child("referred_to").getValue(String.class));
                    if (data.hasChild("abnormal_discharge_from_the_genital_area"))
                        sideAInsider.setAbnormalDischargeFromTheGenitalArea(data.child("abnormal_discharge_from_the_genital_area").getValue(String.class));
                    if (data.hasChild("abnormal_discharge_from_the_genital_area_yes"))
                        sideAInsider.setAbnormalDischargeFromTheGenitalAreaYes(data.child("abnormal_discharge_from_the_genital_area_yes").getValue(String.class));
                    if (data.hasChild("score_or_ulcers_in_the_genital_area"))
                        sideAInsider.setScoreOrUlcersInTheGenitalArea(data.child("score_or_ulcers_in_the_genital_area").getValue(String.class));
                    if (data.hasChild("pain_or_burning_sensation_in_the_genital_area"))
                        sideAInsider.setPainOrBurningSensationInTheGenitalArea(data.child("pain_or_burning_sensation_in_the_genital_area").getValue(String.class));
                    if (data.hasChild("history_of_treatment_for_sexually_transmitted_infections"))
                        sideAInsider.setHistoryOfTreatmentForSexuallyTransmittedInfections(data.child("history_of_treatment_for_sexually_transmitted_infections").getValue(String.class));
                    if (data.hasChild("hiv_aids_pelvic_inflammatory_disease"))
                        sideAInsider.setHivAidsPelvicInflammatoryDisease(data.child("hiv_aids_pelvic_inflammatory_disease").getValue(String.class));

                    runOnUiThread(() -> {
                        
                        if (binding.progressBarAddFormFp.getVisibility() == View.VISIBLE) {
                            binding.progressBarAddFormFp.setVisibility(View.GONE);
                            binding.parentContainerAddForm.setClickable(true);
                            binding.parentContainerAddForm.setFocusable(true);
                        }

                        formLayoutAdapter = new FormLayoutAdapter(sideAInsider, familyPlanningModel, isAddingExistingRecord, AddFormFP.this);
                        binding.recyclerViewAddFormFp.setAdapter(formLayoutAdapter);
                        binding.recyclerViewAddFormFp.setHasFixedSize(true);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddFormFP.this, "Database error, " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.addListenerForSingleValueEvent(valueEventListener);

    }
}