package com.rey.itrustapplication.adminform.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adminform.activities.AddFormFP;
import com.rey.itrustapplication.adminform.activities.SideBAddFormFP;
import com.rey.itrustapplication.adminform.model.FamilyPlanningModel;
import com.rey.itrustapplication.adminform.model.SideAInsider;
import com.rey.itrustapplication.adminform.popup.AddNewRecordPrompt;
import com.rey.itrustapplication.databinding.AddressOfClientLayoutBinding;
import com.rey.itrustapplication.databinding.ButtonLayoutAddFpBinding;
import com.rey.itrustapplication.databinding.ExistingPatientAddRecordsBinding;
import com.rey.itrustapplication.databinding.FpHeaderStartingItemBinding;
import com.rey.itrustapplication.databinding.MedicalHistoryFpBinding;
import com.rey.itrustapplication.databinding.NameOfClientLayoutBinding;
import com.rey.itrustapplication.databinding.ObstetricalHistoryFpBinding;
import com.rey.itrustapplication.databinding.PhysicalExaminationFpBinding;
import com.rey.itrustapplication.databinding.RfstiFpBinding;
import com.rey.itrustapplication.databinding.TypeOfClientLayoutBinding;
import com.rey.itrustapplication.databinding.VawFpBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FormLayoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HeaderFPViewHolder headerFPViewHolder;
    private NameOfClientFPViewHolder nameOfClientFPViewHolder;
    private AddressOfClientFPViewHolder addressOfClientFPViewHolder;
    private TypeOfClientFPViewHolder typeOfClientFPViewHolder;
    private MedicalHistoryFPViewHolder medicalHistoryFPViewHolder;
    private ObstetricalHistoryFPViewHolder obstetricalHistoryFPViewHolder;
    private VAWFPViewHolder vawfpViewHolder;
    private ExistingHeaderFpViewHolder existingHeaderFpViewHolder;
    private RFSTIFPViewHolder rfstifpViewHolder;
    private PhysicalExaminationFPViewHolder physicalExaminationFPViewHolder;

    private final SideAInsider sideAInsider;
    private final FamilyPlanningModel familyPlanningModel;

    private final AddFormFP addFormFP;

    private String selectedPatient = "";

    private final boolean isAddingExistingRecord;

    private final DatabaseReference databaseReference;

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else if (position == 1) {
            return 2;
        } else if ((position == 2)) {
            return 3;
        } else if (position == 3){
            return 4;
        } else if (position == 4){
            return 5;
        } else if (position == 5){
            return 6;
        } else if (position == 6){
            return 7;
        } else if (position == 7){
            return 8;
        } else if (position == 8){
            return 9;
        } else {
            return 10;
        }
    }

    public FormLayoutAdapter(SideAInsider sideAInsider, FamilyPlanningModel familyPlanningModel, boolean isAddingExistingRecord, AddFormFP addFormFP) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.sideAInsider = sideAInsider;
        this.familyPlanningModel = familyPlanningModel;
        this.isAddingExistingRecord = isAddingExistingRecord;
        this.addFormFP = addFormFP;
    }

    private void createNewRecords(boolean isLoadingSideB, View view, String fullName, DialogInterface dialogInterface) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", new Locale("fil", "PH"));
        String formattedDate = dateFormat.format(new Date());

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma", Locale.getDefault());
        String formattedTime = timeFormat.format(new Date());

        Map<String, Object> fpDataHeaders = new HashMap<>();
        fpDataHeaders.put("last_date_added", formattedDate);
        fpDataHeaders.put("last_time_added", formattedTime);

        Map<String, Object> fpData = new HashMap<>();
        fpData.put("current_date", formattedDate);
        fpData.put("current_time", formattedTime);

        headerFPViewHolder.bindAction(fpDataHeaders);

        addressOfClientFPViewHolder.bindAction(fpData);
        typeOfClientFPViewHolder.bindAction(fpData);
        medicalHistoryFPViewHolder.bindAction(fpData);
        physicalExaminationFPViewHolder.bindAction(fpData);
        obstetricalHistoryFPViewHolder.bindAction(fpData);
        vawfpViewHolder.bindAction(fpData);
        rfstifpViewHolder.bindAction(fpData);

        nameOfClientFPViewHolder.bindAction(fpDataHeaders);
        String key = databaseReference.push().getKey();
        Toast.makeText(view.getContext(), "Key: " + key, Toast.LENGTH_SHORT).show();
        String primaryKey = formattedDate.replaceAll("/", "_");
        fpDataHeaders.put("patient_name", fullName);
        fpDataHeaders.put(primaryKey,fpData);
        fpData.put("date_key", primaryKey);


        databaseReference.child("Forms/FamilyPlanning/SideA/" + fullName).setValue(fpDataHeaders).addOnSuccessListener(success -> addFormFP.runOnUiThread(() -> {
            vawfpViewHolder.clearData();
            medicalHistoryFPViewHolder.clearData();
            typeOfClientFPViewHolder.clearData();
            physicalExaminationFPViewHolder.clearData();
            rfstifpViewHolder.clearData();
            Toast.makeText(view.getContext(), "You have successfully added new record to the database!", Toast.LENGTH_SHORT).show();

            if (isLoadingSideB) {
                view.getContext().startActivity(new Intent(view.getContext(), SideBAddFormFP.class).putExtra("patient_name", fullName));
                addFormFP.finish();
                return;
            }
            if (dialogInterface != null)
                dialogInterface.dismiss();
            addFormFP.onBackPressed();
        })).addOnFailureListener(failed -> addFormFP.runOnUiThread(() -> Toast.makeText(view.getContext(), "Error because, " + failed.getMessage(), Toast.LENGTH_SHORT).show()));


    }

    private void updateUserRecord(boolean isLoadingSideB, View view, String fullName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", new Locale("fil", "PH"));
        String formattedDate = dateFormat.format(new Date());

        final String primaryKey = formattedDate.replaceAll("/", "_");

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma", Locale.getDefault());
        String formattedTime = timeFormat.format(new Date());

        Map<String, Object> fpData = new HashMap<>();
        fpData.put("current_date", formattedDate);
        fpData.put("current_time", formattedTime);
        fpData.put("date_key", primaryKey);

        addressOfClientFPViewHolder.bindAction(fpData);
        typeOfClientFPViewHolder.bindAction(fpData);
        medicalHistoryFPViewHolder.bindAction(fpData);
        physicalExaminationFPViewHolder.bindAction(fpData);
        obstetricalHistoryFPViewHolder.bindAction(fpData);
        vawfpViewHolder.bindAction(fpData);
        rfstifpViewHolder.bindAction(fpData);

        databaseReference.child("Forms/FamilyPlanning/SideA/" + fullName + "/" + primaryKey).setValue(fpData).addOnSuccessListener(success -> addFormFP.runOnUiThread(() -> {
            vawfpViewHolder.clearData();
            medicalHistoryFPViewHolder.clearData();
            typeOfClientFPViewHolder.clearData();
            physicalExaminationFPViewHolder.clearData();
            rfstifpViewHolder.clearData();

            Toast.makeText(view.getContext(), "You have successfully added new records to the database", Toast.LENGTH_SHORT).show();

            if (isLoadingSideB) {
                view.getContext().startActivity(new Intent(view.getContext(), SideBAddFormFP.class).putExtra("patient_name", fullName));
                addFormFP.finish();
                return;
            }

            addFormFP.onBackPressed();
        })).addOnFailureListener(failed -> addFormFP.runOnUiThread(() -> Toast.makeText(view.getContext(), "Error because, " + failed.getMessage(), Toast.LENGTH_SHORT).show()));


    }

    private void setProgressing(ProgressBar progressBar, View view, boolean shouldLoad) {
        if (shouldLoad)
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }
        else
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }
    }

    public void saveAddedFormFP(boolean isLoadingSideB, View view, ProgressBar progressBar) {

        LinkedList<String> fullName = new LinkedList<>();

        if (!isAddingExistingRecord) {
            if (!headerFPViewHolder.isValidUserInput(addFormFP)) {
                setProgressing(progressBar, view, false);
                return;
            }
            if (!nameOfClientFPViewHolder.isValidInput(fullName, addFormFP)) {
                setProgressing(progressBar, view, false);
                return;
            }
        }

        if (isAddingExistingRecord) {
            if (selectedPatient.isEmpty()) {
                existingHeaderFpViewHolder.setError(addFormFP);
                setProgressing(progressBar, view, false);
                return;
            }

            existingHeaderFpViewHolder.removeError(addFormFP);

            fullName.add(selectedPatient);
        }

        databaseReference.child("Forms/FamilyPlanning/SideA/" + fullName.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {

            final String concatenatedName = fullName.get(0).replaceAll("[^a-zA-Z0-9]","_");

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    createNewRecords(isLoadingSideB, view, concatenatedName, null);
                } else {

                    AddNewRecordPrompt.showPopUp(view.getContext(),
                            fullName.get(0) + " has already a record in this system, do you want to create a new records? or use the existing record?",
                            "Existing Record",
                            "New Record",
                            ((dialogInterface, i) -> updateUserRecord(isLoadingSideB, view, concatenatedName)),
                            ((dialogInterface, i) ->
                                AddNewRecordPrompt.showPopUp(view.getContext(),
                                        "Are you sure? Please note that the other record associated with this user will also be deleted?",
                                        "Delete Anyway",
                                        "Back",
                                        ((dialogInterface1, i1) -> {
                                            Toast.makeText(view.getContext(), "Creating new record", Toast.LENGTH_SHORT).show();
                                            createNewRecords(isLoadingSideB, view, concatenatedName, dialogInterface1);
                                        }),
                                        null)
                            ));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Error, " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        ButtonContainerViewHolder buttonContainerViewHolder;
        if (isAddingExistingRecord) {
            switch (viewType) {
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.existing_patient_add_records, parent, false);
                    existingHeaderFpViewHolder = new ExistingHeaderFpViewHolder(view);
                    return existingHeaderFpViewHolder;
                case 2:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_of_client_layout, parent, false);
                    addressOfClientFPViewHolder = new AddressOfClientFPViewHolder(view, sideAInsider);
                    return addressOfClientFPViewHolder;
                case 3:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_of_client_layout, parent, false);
                    typeOfClientFPViewHolder = new TypeOfClientFPViewHolder(view, sideAInsider);
                    return typeOfClientFPViewHolder;
                case 4:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_history_fp, parent, false);
                    medicalHistoryFPViewHolder = new MedicalHistoryFPViewHolder(view, sideAInsider);
                    return medicalHistoryFPViewHolder;
                case 5:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obstetrical_history_fp, parent, false);
                    obstetricalHistoryFPViewHolder = new ObstetricalHistoryFPViewHolder(view, sideAInsider);
                    return obstetricalHistoryFPViewHolder;
                case 6:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rfsti_fp, parent, false);
                    rfstifpViewHolder = new RFSTIFPViewHolder(view, sideAInsider);
                    return rfstifpViewHolder;
                case 7:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaw_fp, parent, false);
                    vawfpViewHolder = new VAWFPViewHolder(view, sideAInsider);
                    return vawfpViewHolder;
                case 8:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.physical_examination_fp, parent, false);
                    physicalExaminationFPViewHolder = new PhysicalExaminationFPViewHolder(view, sideAInsider);
                    return physicalExaminationFPViewHolder;
                default:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_layout_add_fp, parent, false);
                    buttonContainerViewHolder = new ButtonContainerViewHolder(view);
                    return buttonContainerViewHolder;

            }
        }

        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fp_header_starting_item, parent, false);
                headerFPViewHolder = new HeaderFPViewHolder(view, familyPlanningModel);
                return headerFPViewHolder;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.name_of_client_layout, parent, false);
                nameOfClientFPViewHolder = new NameOfClientFPViewHolder(view, familyPlanningModel);
                return nameOfClientFPViewHolder;
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_of_client_layout, parent, false);
                addressOfClientFPViewHolder = new AddressOfClientFPViewHolder(view, sideAInsider);
                return addressOfClientFPViewHolder;
            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_of_client_layout, parent, false);
                typeOfClientFPViewHolder = new TypeOfClientFPViewHolder(view, sideAInsider);
                return typeOfClientFPViewHolder;
            case 5:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_history_fp, parent, false);
                medicalHistoryFPViewHolder = new MedicalHistoryFPViewHolder(view, sideAInsider);
                return medicalHistoryFPViewHolder;
            case 6:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obstetrical_history_fp, parent, false);
                obstetricalHistoryFPViewHolder = new ObstetricalHistoryFPViewHolder(view, sideAInsider);
                return obstetricalHistoryFPViewHolder;
            case 7:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rfsti_fp, parent, false);
                rfstifpViewHolder = new RFSTIFPViewHolder(view, sideAInsider);
                return rfstifpViewHolder;
            case 8:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaw_fp, parent, false);
                vawfpViewHolder = new VAWFPViewHolder(view, sideAInsider);
                return vawfpViewHolder;
            case 9:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.physical_examination_fp, parent, false);
                physicalExaminationFPViewHolder = new PhysicalExaminationFPViewHolder(view, sideAInsider);
                return physicalExaminationFPViewHolder;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_layout_add_fp, parent, false);
                buttonContainerViewHolder = new ButtonContainerViewHolder(view);
                return buttonContainerViewHolder;
        }
    }

    private void settingUpToPushData(boolean shouldLoadSideB, ProgressBar progressBar, View view) {
        setProgressing(progressBar, view, true);
        saveAddedFormFP(shouldLoadSideB, view, progressBar);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position == 0 && isAddingExistingRecord) {
            ExistingHeaderFpViewHolder viewHolder = (ExistingHeaderFpViewHolder) holder;
            viewHolder.fetchPatientNames(addFormFP);
            viewHolder.materialAutoCompleteTextView.setOnItemClickListener((parent, view, pos, id) ->
                    selectedPatient = parent.getItemAtPosition(pos).toString());
        } else if (position == 1 && isAddingExistingRecord) {
            AddressOfClientFPViewHolder viewHolder = (AddressOfClientFPViewHolder) holder;
            viewHolder.bind();
        } else if (position == 3 && isAddingExistingRecord) {
            MedicalHistoryFPViewHolder viewHolder = (MedicalHistoryFPViewHolder) holder;
            viewHolder.bind();
        } else if (position == 8 && isAddingExistingRecord) {
            ButtonContainerViewHolder viewHolder = (ButtonContainerViewHolder) holder;
            viewHolder.binding.saveAndExitAddFormFp.setOnClickListener(view ->
                settingUpToPushData(false, viewHolder.binding.progressBarButtonAddFp, viewHolder.itemView)
            );
            viewHolder.binding.sideBButtonAddFormFp.setOnClickListener(view ->
                    settingUpToPushData(true, viewHolder.binding.progressBarButtonAddFp, viewHolder.itemView)
            );
        }

        if (position == 0 && !isAddingExistingRecord) {
            HeaderFPViewHolder viewHolder = (HeaderFPViewHolder) holder;
            viewHolder.bind();

        } else if (position == 2&& !isAddingExistingRecord) {
            AddressOfClientFPViewHolder viewHolder = (AddressOfClientFPViewHolder) holder;
            viewHolder.bind();
        } else if (position == 4 && !isAddingExistingRecord) {
            MedicalHistoryFPViewHolder viewHolder = (MedicalHistoryFPViewHolder) holder;
            viewHolder.bind();
        } else if (position == 9 && !isAddingExistingRecord) {
            ButtonContainerViewHolder viewHolder = (ButtonContainerViewHolder) holder;
            viewHolder.binding.saveAndExitAddFormFp.setOnClickListener(view ->
                    settingUpToPushData(false, viewHolder.binding.progressBarButtonAddFp, viewHolder.itemView)
            );
            viewHolder.binding.sideBButtonAddFormFp.setOnClickListener(view ->
                    settingUpToPushData(true, viewHolder.binding.progressBarButtonAddFp, viewHolder.itemView)
            );
        }
    }

    @Override
    public int getItemCount() {
        if (!isAddingExistingRecord)
            return 10;
        return 9;
    }

    static class ButtonContainerViewHolder extends RecyclerView.ViewHolder {

        ButtonLayoutAddFpBinding binding;

        public ButtonContainerViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ButtonLayoutAddFpBinding.bind(itemView);
        }
    }

    static class ExistingHeaderFpViewHolder extends RecyclerView.ViewHolder {

        ExistingPatientAddRecordsBinding binding;
        private ArrayAdapter<String> arrayAdapter;
        private final List<String> patientList;
        private final View itemView;
        private final MaterialAutoCompleteTextView materialAutoCompleteTextView;

        public ExistingHeaderFpViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ExistingPatientAddRecordsBinding.bind(itemView);
            patientList = new ArrayList<>();
            this.itemView = itemView;
            materialAutoCompleteTextView = binding.existingPatientDropDownAddRecord;
        }



        void fetchPatientNames(AddFormFP addFormFP) {
            FirebaseDatabase.getInstance().getReference("Forms/FamilyPlanning/SideA").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.hasChild("patient_name")) {
                                final String patient_name = dataSnapshot.child("patient_name").getValue(String.class);
                                patientList.add(patient_name);
                            }
                        }
                        arrayAdapter = new ArrayAdapter<>(itemView.getContext(), R.layout.signup_gender_list, patientList);
                        binding.existingPatientDropDownAddRecord.setAdapter(arrayAdapter);
                        binding.patientDropDownParentAddRecords.setVisibility(View.VISIBLE);
                        binding.progressBarExistingPatientAddRecords.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(addFormFP, "No patient name exist in the database, please add 1 first", Toast.LENGTH_SHORT).show();
                        addFormFP.onBackPressed();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void setError(AddFormFP addFormFP) {
            addFormFP.runOnUiThread(() -> {
                binding.patientDropDownParentAddRecords.setError("Please select patient name first");
                binding.patientDropDownParentAddRecords.requestFocus();
            });


        }

        public void removeError(AddFormFP addFormFP) {
            if (binding.patientDropDownParentAddRecords.isErrorEnabled()) {
                addFormFP.runOnUiThread(() -> {
                    binding.patientDropDownParentAddRecords.setError("");
                    binding.patientDropDownParentAddRecords.setErrorEnabled(false);
                });
            }
        }
    }

    static class HeaderFPViewHolder extends RecyclerView.ViewHolder {

        FpHeaderStartingItemBinding binding;
        boolean isNHTS = false, is4Ps = false;
        private boolean hasNhts = false, has4Ps = false;
        FamilyPlanningModel familyPlanningModel;
        private final View itemView;

        public HeaderFPViewHolder(@NonNull View itemView, FamilyPlanningModel familyPlanningModel) {
            super(itemView);
            binding = FpHeaderStartingItemBinding.bind(itemView);
            this.itemView = itemView;
            this.familyPlanningModel = familyPlanningModel;
        }

        public boolean isValidUserInput(AddFormFP addFormFP) {

            if (!hasNhts) {
                addFormFP.runOnUiThread(() ->
                    Toast.makeText(itemView.getContext(), "Please select NHTS, this filled is required", Toast.LENGTH_SHORT).show());
                return false;
            }
            if (!has4Ps) {
                addFormFP.runOnUiThread(() ->
                    Toast.makeText(itemView.getContext(), "Does the patient is a member of 4Ps?", Toast.LENGTH_SHORT).show());
                return false;
            }

            if (binding.clientIdAddFormFp.getEditableText().toString().isEmpty()) {
                addFormFP.runOnUiThread(() -> {
                    binding.clientIdLabelAddFormFp.setError("This filled is required");
                    binding.clientIdLabelAddFormFp.requestFocus();
                });
                return false;
            }

            if (binding.clientIdLabelAddFormFp.isErrorEnabled()) {
                addFormFP.runOnUiThread(() -> {
                    binding.clientIdLabelAddFormFp.setErrorEnabled(false);
                    binding.clientIdLabelAddFormFp.setError("");
                });
            }

            if (binding.philIdAddFormFp.getEditableText().toString().isEmpty()) {
                addFormFP.runOnUiThread(() -> {
                    binding.philIdLabelAddFormFp.setError("This filled is required");
                    binding.philIdLabelAddFormFp.requestFocus();
                });
                return false;
            }

            if (binding.philIdLabelAddFormFp.isErrorEnabled()) {
                addFormFP.runOnUiThread(() -> {
                binding.philIdLabelAddFormFp.setErrorEnabled(false);
                binding.philIdLabelAddFormFp.setError("");
                });
            }

            return true;
        }

        void setDefaultFields() {
            if (familyPlanningModel.getNhts() != null && Boolean.parseBoolean(familyPlanningModel.getNhts())) {
                binding.nhtsYesAddFormFp.setChecked(true);
            } else {
                binding.nhtsNoAddFormFp.setChecked(false);
            }

            if (familyPlanningModel.getFourPs() != null && Boolean.parseBoolean(familyPlanningModel.getFourPs())) {
                binding.fourPsYesAddFormFp.setChecked(true);
            } else {
                binding.fourPsNoAddFormFp.setChecked(false);
            }

            binding.clientIdAddFormFp.setText(familyPlanningModel.getClientId() == null ? "" : familyPlanningModel.getClientId().trim());
            binding.philIdAddFormFp.setText(familyPlanningModel.getPhilId() == null ? "" : familyPlanningModel.getPhilId().trim());

        }

        void bind() {

            if (familyPlanningModel != null) {
                setDefaultFields();

            }

            binding.nhtsYesAddFormFp.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    binding.nhtsNoAddFormFp.setChecked(false);
                    hasNhts = true;
                    isNHTS = true;
                }
            });

            binding.nhtsNoAddFormFp.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    binding.nhtsYesAddFormFp.setChecked(false);
                    hasNhts = true;
                    isNHTS = false;
                }
            });
            binding.fourPsYesAddFormFp.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    binding.fourPsNoAddFormFp.setChecked(false);
                    is4Ps = true;
                    has4Ps = true;
                }
            });

            binding.fourPsNoAddFormFp.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    binding.fourPsYesAddFormFp.setChecked(false);
                    is4Ps = false;
                    has4Ps = true;
                }

            });
        }

        void bindAction(Map<String, Object> fpData) {
            String client_id = binding.clientIdAddFormFp.getEditableText().toString();
            String phil_id = binding.philIdAddFormFp.getEditableText().toString();

            fpData.put("client_id", client_id.isEmpty() ? "" : client_id.trim());
            fpData.put("phil_id", phil_id.isEmpty() ? "" : phil_id.trim());
            fpData.put("nhts", isNHTS);
            fpData.put("four_Ps", is4Ps);

        }
    }

    static class NameOfClientFPViewHolder extends RecyclerView.ViewHolder {

        NameOfClientLayoutBinding binding;
        View itemView;
        private String MI = "", age = "", dob = "", givenName = "", lastName = "";
        private final FamilyPlanningModel familyPlanningModel;

        public NameOfClientFPViewHolder(@NonNull View itemView, FamilyPlanningModel familyPlanningModel) {
            super(itemView);
            this.itemView = itemView;
            binding = NameOfClientLayoutBinding.bind(itemView);
            this.familyPlanningModel = familyPlanningModel;
            if (familyPlanningModel != null) {
                setDefaultInputs();
            }
        }

        boolean isValidInput(LinkedList<String> fullName, AddFormFP addFormFP) {
            String lastName = binding.lastNameAddFormFp.getEditableText().toString().trim();
            String givenName = binding.givenNameAddFormFp.getEditableText().toString().trim();
            String MI = binding.miAddFormFp.getEditableText().toString().trim();
            String age = binding.ageAddFormFp.getEditableText().toString().trim();
            String dob = binding.dobAddFormFp.getEditableText().toString().trim();

            if (lastName.isEmpty()) {
                addFormFP.runOnUiThread(() -> {
                binding.lastNameAddFormFp.setError("Last name field is required");
                binding.lastNameAddFormFp.requestFocus();
                });
                return false;
            }

            if (givenName.isEmpty()) {
                addFormFP.runOnUiThread(() -> {
                binding.givenNameAddFormFp.setError("Given name field is required");
                binding.givenNameAddFormFp.requestFocus();
                });
                return false;
            }

            if (MI.isEmpty()) {
                addFormFP.runOnUiThread(() -> {
                    binding.miAddFormFp.setError("middle initial field is required");
                    binding.miAddFormFp.requestFocus();
                });
                return false;
            }

            if (age.isEmpty()) {
                addFormFP.runOnUiThread(() -> {
                    binding.ageAddFormFp.setError("age field is required");
                    binding.ageAddFormFp.requestFocus();
                });
                return false;
            }

            if (dob.isEmpty()) {
                addFormFP.runOnUiThread(() -> {
                    binding.dobAddFormFp.setError("date of birth field is required");
                    binding.dobAddFormFp.requestFocus();
                });
                return false;
            }

            addFormFP.runOnUiThread(() -> {
                if (binding.lastNameAddFormFp.getError() != null) binding.lastNameAddFormFp.setError("");
                if (binding.givenNameAddFormFp.getError() != null) binding.givenNameAddFormFp.setError("");
                if (binding.miAddFormFp.getError() != null) binding.miAddFormFp.setError("");
                if (binding.ageAddFormFp.getError() != null) binding.ageAddFormFp.setError("");
                if (binding.dobAddFormFp.getError() != null) binding.dobAddFormFp.setError("");
            });

            this.givenName = givenName;
            this.lastName = lastName;
            this.MI = MI;
            this.age = age;
            this.dob = dob;

            final String e = givenName.trim() + MI.trim() + lastName.trim();

            if (fullName.isEmpty()) {
                fullName.add(e);
            } else
                fullName.set(0, e);

            return true;

        }

        void bindAction(Map<String, Object> fpData) {
            String educAttain = binding.educAttAddFormFp.getEditableText().toString().trim();
            String occupation = binding.occupationAddFormFp.getEditableText().toString().trim();

            fpData.put("given_name", givenName);
            fpData.put("last_name", lastName);
            fpData.put("mi", MI);
            fpData.put("age", age);
            fpData.put("date_of_birth", dob);
            fpData.put("educ_attain", educAttain);
            fpData.put("occupation", occupation);
        }


        public void setDefaultInputs() {
            binding.givenNameAddFormFp.setText(familyPlanningModel.getGivenName() == null ? "" : familyPlanningModel.getGivenName().trim());
            binding.lastNameAddFormFp.setText(familyPlanningModel.getLastName() == null ? "" : familyPlanningModel.getLastName().trim());
            binding.miAddFormFp.setText(familyPlanningModel.getMiddleName() == null ? "" : familyPlanningModel.getMiddleName().trim());
            binding.dobAddFormFp.setText(familyPlanningModel.getDateOfBirth() == null ? "" : familyPlanningModel.getDateOfBirth().trim());
            binding.ageAddFormFp.setText(familyPlanningModel.getAge() == null ? "" : familyPlanningModel.getAge().trim());
            binding.educAttAddFormFp.setText(familyPlanningModel.getEducAttain() == null ? "" : familyPlanningModel.getEducAttain().trim());
            binding.occupationAddFormFp.setText(familyPlanningModel.getOccupation() == null ? "" : familyPlanningModel.getOccupation().trim());
        }
    }

    static class AddressOfClientFPViewHolder extends RecyclerView.ViewHolder {

        AddressOfClientLayoutBinding binding;
        private boolean isPlanningToHaveChildren = false;
        private final SideAInsider sideAInsider;

        public AddressOfClientFPViewHolder(@NonNull View itemView, SideAInsider sideAInsider) {
            super(itemView);
            binding = AddressOfClientLayoutBinding.bind(itemView);
            this.sideAInsider = sideAInsider;
            if (sideAInsider != null) {
                setDefaultInputsCheckBox();
            }
        }

        private void setDefaultInputsCheckBox() {
            binding.addressFieldAddFormFp.setText(sideAInsider.getAddress() == null ? "" : sideAInsider.getAddress().trim());
            binding.contactNumberAddFormFp.setText(sideAInsider.getContactNumber() == null ? "" : sideAInsider.getContactNumber().trim());
            binding.civilStatusAddFormFp.setText(sideAInsider.getCivilStatus() == null ? "" : sideAInsider.getCivilStatus().trim());
            binding.religionAddFormFp.setText(sideAInsider.getReligion() == null ? "" : sideAInsider.getReligion().trim());

            binding.lnSpouseAddFormFp.setText(sideAInsider.getLastNameSpouse() == null ? "" : sideAInsider.getLastNameSpouse().trim());
            binding.gnSpouseAddFormFp.setText(sideAInsider.getGivenNameSpouse() == null ? "" : sideAInsider.getGivenNameSpouse().trim());
            binding.miSpouseAddFormFp.setText(sideAInsider.getMiddleInitial() == null ? "" : sideAInsider.getMiddleInitial().trim());
            binding.contactSpouseAddFormFp.setText(sideAInsider.getContactNumberSpouse() == null ? "" : sideAInsider.getContactNumberSpouse().trim());
            binding.civilSpouseAddFormFp.setText(sideAInsider.getCivilStatusSpouse() == null ? "" : sideAInsider.getCivilStatusSpouse().trim());
            binding.religionSpouseAddFormFp.setText(sideAInsider.getReligionSpouse() == null ? "" : sideAInsider.getReligionSpouse().trim());
            binding.nolcAddFormFp.setText(sideAInsider.getNoOfChildren() == null ? "" : sideAInsider.getNoOfChildren().trim());
            binding.amicAddFormFp.setText(sideAInsider.getAverageMonthlyIncome() == null ? "" : sideAInsider.getAverageMonthlyIncome().trim());

            if (sideAInsider.getPlanningToHaveChildren() != null && Boolean.parseBoolean(sideAInsider.getPlanningToHaveChildren())) binding.pthmcYesAddFormFp.setChecked(true);
            else binding.pthmcNoAddFormFp.setChecked(true);

        }

        void bind() {
            binding.pthmcYesAddFormFp.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    binding.pthmcNoAddFormFp.setChecked(false);
                    isPlanningToHaveChildren = true;
                }
            });

            binding.pthmcNoAddFormFp.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    binding.pthmcYesAddFormFp.setChecked(false);
                    isPlanningToHaveChildren = false;
                }
            });
        }

        void bindAction(Map<String, Object> fpData) {
            final String address = binding.addressFieldAddFormFp.getEditableText().toString();
            final String contactNumber = binding.contactNumberAddFormFp.getEditableText().toString();
            final String civilStatus = binding.civilStatusAddFormFp.getEditableText().toString();
            final String religion = binding.religionAddFormFp.getEditableText().toString();

            //Spouse
            final String lastNameSpouse = binding.lnSpouseAddFormFp.getEditableText().toString();
            final String givenName = binding.gnSpouseAddFormFp.getEditableText().toString();
            final String MI = binding.miSpouseAddFormFp.getEditableText().toString();
            final String contactNumberSpouse = binding.contactSpouseAddFormFp.getEditableText().toString();
            final String civilStatusSpouse = binding.civilSpouseAddFormFp.getEditableText().toString();
            final String religionSpouse = binding.religionSpouseAddFormFp.getEditableText().toString();
            final String noOfChildren = binding.nolcAddFormFp.getEditableText().toString();

            final String averageMonthlySalary = binding.amicAddFormFp.getEditableText().toString();

            fpData.put("planning_to_have_children", isPlanningToHaveChildren);
            fpData.put("address", address);
            fpData.put("contact_number", contactNumber);
            fpData.put("civil_status", civilStatus);
            fpData.put("religion", religion);
            fpData.put("last_name_spouse", lastNameSpouse);
            fpData.put("given_name_spouse", givenName);
            fpData.put("mi", MI);
            fpData.put("contact_number_spouse", contactNumberSpouse);
            fpData.put("civil_status_spouse", civilStatusSpouse);
            fpData.put("religion_spouse", religionSpouse);
            fpData.put("no_of_children", noOfChildren);
            fpData.put("average_monthly_income", averageMonthlySalary);

        }

    }

    static class VAWFPViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        VawFpBinding binding;
        private final SideAInsider sideAInsider;
        private String questionOneAnswer, questionTwoAnswer, questionThreeAnswer, referredTo;

        public VAWFPViewHolder(@NonNull View itemView, SideAInsider sideAInsider) {
            super(itemView);
            binding = VawFpBinding.bind(itemView);
            this.sideAInsider = sideAInsider;
            binding.vawCheckYes1Fp.setOnCheckedChangeListener(this);
            binding.vawCheckNo1Fp.setOnCheckedChangeListener(this);
            binding.vawCheckYes2Fp.setOnCheckedChangeListener(this);
            binding.vawCheckNo2Fp.setOnCheckedChangeListener(this);
            binding.vawCheckNo3Fp.setOnCheckedChangeListener(this);
            binding.vawCheckYes3Fp.setOnCheckedChangeListener(this);
            binding.referredTpDswdVawFp.setOnCheckedChangeListener(this);
            binding.referredTpNgosVawFp.setOnCheckedChangeListener(this);
            binding.referredTpWcpuVawFp.setOnCheckedChangeListener(this);
            binding.referredTpOthersVawFp.setOnCheckedChangeListener(this);

            if (sideAInsider != null) {
                setDefaultInputsAndCheBoxes();
            }
        }

        private void setDefaultInputsAndCheBoxes() {
            if (sideAInsider.getUnpleasantRelationshipWithPartner().equals("yes")) binding.vawCheckYes1Fp.setChecked(true);
            else binding.vawCheckNo1Fp.setChecked(true);

            if (sideAInsider.getPartnerDoesNotApproveOfTheVisitToFpClinic().equals("yes")) binding.vawCheckYes2Fp.setChecked(true);
            else binding.vawCheckNo2Fp.setChecked(true);

            if (sideAInsider.getHistoryOfDomesticViolenceOrVaw().equals("yes")) binding.vawCheckYes3Fp.setChecked(true);
            else binding.vawCheckNo3Fp.setChecked(true);

            final String _referencedTo = sideAInsider.getReferredTo() == null ? "" : sideAInsider.getReferredTo();

            switch (_referencedTo) {
                case "DSWD":
                    binding.referredTpDswdVawFp.setChecked(true);
                    break;
                case "WCPU":
                    binding.referredTpWcpuVawFp.setChecked(true);
                    break;
                case "NGOs":
                    binding.referredTpNgosVawFp.setChecked(true);
                    break;
                default:
                    if (sideAInsider.getReferredTo() != null)
                        binding.referredTpOthersVawFp.setChecked(true);
                    if (!sideAInsider.getReferredTo().equals("Others"))
                        binding.othersEtVawFp.setText(sideAInsider.getReferredTo());
                    break;
            }

        }

        void clearData() {

            binding.vawCheckYes1Fp.setOnCheckedChangeListener(null);
            binding.vawCheckNo1Fp.setOnCheckedChangeListener(null);
            binding.vawCheckYes2Fp.setOnCheckedChangeListener(null);
            binding.vawCheckNo2Fp.setOnCheckedChangeListener(null);
            binding.vawCheckNo3Fp.setOnCheckedChangeListener(null);
            binding.vawCheckYes3Fp.setOnCheckedChangeListener(null);
            binding.referredTpDswdVawFp.setOnCheckedChangeListener(null);
            binding.referredTpNgosVawFp.setOnCheckedChangeListener(null);
            binding.referredTpWcpuVawFp.setOnCheckedChangeListener(null);
            binding.referredTpOthersVawFp.setOnCheckedChangeListener(null);

            if (binding.vawCheckYes1Fp.isChecked()) binding.vawCheckYes1Fp.setChecked(false);
            if (binding.vawCheckNo1Fp.isChecked()) binding.vawCheckNo1Fp.setChecked(false);

            if (binding.vawCheckYes2Fp.isChecked()) binding.vawCheckYes2Fp.setChecked(false);
            if (binding.vawCheckNo2Fp.isChecked()) binding.vawCheckNo2Fp.setChecked(false);

            if (binding.vawCheckNo3Fp.isChecked()) binding.vawCheckNo3Fp.setChecked(false);
            if (binding.vawCheckYes3Fp.isChecked()) binding.vawCheckYes3Fp.setChecked(false);

            if (binding.referredTpDswdVawFp.isChecked()) binding.referredTpDswdVawFp.setChecked(false);
            if (binding.referredTpNgosVawFp.isChecked()) binding.referredTpNgosVawFp.setChecked(false);
            if (binding.referredTpWcpuVawFp.isChecked()) binding.referredTpWcpuVawFp.setChecked(false);
            if (binding.referredTpOthersVawFp.isChecked()) binding.referredTpOthersVawFp.setChecked(false);

            if (!binding.othersEtVawFp.getText().toString().isEmpty())
                binding.othersEtVawFp.setText("");

            questionOneAnswer = "";
            questionTwoAnswer = "";
            questionThreeAnswer = "";
            referredTo = "";
        }

        void bindAction(Map<String, Object> fpData) {

            fpData.put("unpleasant_relationship_with_partner", questionOneAnswer);
            fpData.put("partner_does_not_approve_of_the_visit_to_fp_clinic", questionTwoAnswer);
            fpData.put("history_of_domestic_violence_or_vaw", questionThreeAnswer);
            fpData.put("referred_to", referredTo);

        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!b) return;

            if (binding.vawCheckYes1Fp.getId() == compoundButton.getId()) {
                questionOneAnswer = "yes";
                binding.vawCheckNo1Fp.setChecked(false);
            } else if (binding.vawCheckNo1Fp.getId() == compoundButton.getId()) {
                questionOneAnswer = "no";
                binding.vawCheckYes1Fp.setChecked(false);
            } else if (binding.vawCheckYes2Fp.getId() == compoundButton.getId()) {
                questionTwoAnswer = "yes";
                binding.vawCheckNo2Fp.setChecked(false);
            } else if (binding.vawCheckNo2Fp.getId() == compoundButton.getId()) {
                questionTwoAnswer = "no";
                binding.vawCheckYes2Fp.setChecked(false);
            } else if (binding.vawCheckYes3Fp.getId() == compoundButton.getId()) {
                questionThreeAnswer = "yes";
                binding.vawCheckNo3Fp.setChecked(false);
            } else if (binding.vawCheckNo3Fp.getId() == compoundButton.getId()){
                questionThreeAnswer = "no";
                binding.vawCheckYes3Fp.setChecked(false);
            } else if (binding.referredTpDswdVawFp.getId() == compoundButton.getId()) {
                referredTo = "DSWD";
                binding.referredTpWcpuVawFp.setChecked(false);
                binding.referredTpNgosVawFp.setChecked(false);
                binding.referredTpOthersVawFp.setChecked(false);
                binding.othersEtVawFp.setText("");
            } else if (binding.referredTpWcpuVawFp.getId() == compoundButton.getId()) {
                referredTo = "WCPU";
                binding.referredTpDswdVawFp.setChecked(false);
                binding.referredTpNgosVawFp.setChecked(false);
                binding.referredTpOthersVawFp.setChecked(false);
                binding.othersEtVawFp.setText("");
            } else if (binding.referredTpNgosVawFp.getId() == compoundButton.getId()) {
                referredTo = "NGOs";
                binding.referredTpDswdVawFp.setChecked(false);
                binding.referredTpWcpuVawFp.setChecked(false);
                binding.referredTpOthersVawFp.setChecked(false);
                binding.othersEtVawFp.setText("");
            } else {
                referredTo = binding.othersEtVawFp.getText().toString().trim().isEmpty() ? "Others" : binding.othersEtVawFp.getText().toString().trim();
                binding.referredTpDswdVawFp.setChecked(false);
                binding.referredTpWcpuVawFp.setChecked(false);
                binding.referredTpNgosVawFp.setChecked(false);
            }
        }
    }

    static class TypeOfClientFPViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        TypeOfClientLayoutBinding binding;
        private String reasonForFamilyPlanning = "", dropOutOrRestartReason = "", iudType = "", othersMCU = "";
        private final SideAInsider sideAInsider;

        public TypeOfClientFPViewHolder(@NonNull View itemView, SideAInsider sideAInsider) {
            super(itemView);
            binding = TypeOfClientLayoutBinding.bind(itemView);

            this.sideAInsider = sideAInsider;

            binding.reasonForFpSpacingTocLayout.setOnCheckedChangeListener(this);
            binding.reasonForFpLimitingTocLayout.setOnCheckedChangeListener(this);
            binding.reasonForFpOthersTocLayout.setOnCheckedChangeListener(this);

            binding.reasonMedicalConditionTocLayout.setOnCheckedChangeListener(this);
            binding.reasonSideEffectsTocLayout.setOnCheckedChangeListener(this);

            binding.mcuIntervalTocLayout.setOnCheckedChangeListener(this);
            binding.mcuPostPartumTocLayout.setOnCheckedChangeListener(this);

            if (sideAInsider != null) {
                setDefaultInputAndCheckBoxes();
            }
        }

        private void setDefaultInputAndCheckBoxes() {

            final String _reasonForFamilyPlanning = sideAInsider.getReasonForFamilyPlanning() == null ? "" : sideAInsider.getReasonForFamilyPlanning();

            if (_reasonForFamilyPlanning.equals("spacing")) {
                binding.reasonForFpSpacingTocLayout.setChecked(true);
            } else if (_reasonForFamilyPlanning.equals("limiting")) {
                binding.reasonForFpLimitingTocLayout.setChecked(true);
            } else if (!_reasonForFamilyPlanning.isEmpty()) {
                binding.reasonForFpOthersTocLayout.setChecked(true);
                binding.reasonForFpOthersEtTocLayout.setText(_reasonForFamilyPlanning);
            }

            final String _dropOrRestartReason = sideAInsider.getDropOrRestartReason() == null ? "" : sideAInsider.getDropOrRestartReason().trim();

            if (!_dropOrRestartReason.isEmpty()) {

                binding.restartCurrentUserTocLayout.setChecked(true);

                if (_dropOrRestartReason.equals("medical condition")) {
                    binding.reasonMedicalConditionTocLayout.setChecked(true);
                } else if (_dropOrRestartReason.equals("side effects")) {
                    binding.reasonSideEffectsTocLayout.setChecked(true);
                    binding.reasonEtTocLayout.setText(sideAInsider.getDropOrRestartReason());
                }
            }

            if (sideAInsider.getIud().equals("interval")) {
                binding.mcuIntervalTocLayout.setChecked(true);
            } else if (sideAInsider.getIud().equals("postpartum")) {
                binding.mcuPostPartumTocLayout.setChecked(true);
            }

            final String _othersCurrentlyUsed = sideAInsider.getOthersMethodCurrentlyUsed() == null ? "" : sideAInsider.getOthersMethodCurrentlyUsed().trim();

            if (!_othersCurrentlyUsed.isEmpty()) {
                binding.mcuOthersTocLayout.setChecked(true);
                if (_othersCurrentlyUsed.equals("others"))
                    binding.mcuOthersEtTocLayout.setText(_othersCurrentlyUsed);
            }

            if (sideAInsider.getNewAcceptor() != null && Boolean.parseBoolean(sideAInsider.getNewAcceptor())) binding.newAcceptorTocLayout.setChecked(true);

            if (sideAInsider.getCurrentUser() != null && Boolean.parseBoolean(sideAInsider.getCurrentUser())) binding.currentUserTocLayout.setChecked(true);
            if (sideAInsider.getChangingMethod() != null && Boolean.parseBoolean(sideAInsider.getChangingMethod())) binding.changingMethodCurrentUserTocLayout.setChecked(true);
            if (sideAInsider.getChangingClinic() != null && Boolean.parseBoolean(sideAInsider.getChangingClinic())) binding.changingClinicCurrentUserTocLayout.setChecked(true);

            if (sideAInsider.getCoc() != null && Boolean.parseBoolean(sideAInsider.getCoc())) binding.mcuCocTocLayout.setChecked(true);
            if (sideAInsider.getPop() != null && Boolean.parseBoolean(sideAInsider.getPop())) binding.mcuPopTocLayout.setChecked(true);
            if (sideAInsider.getInjectable() != null && Boolean.parseBoolean(sideAInsider.getInjectable())) binding.mcuInjectableTocLayout.setChecked(true);
            if (sideAInsider.getImplant() != null && Boolean.parseBoolean(sideAInsider.getImplant())) binding.mcuLamTocLayout.setChecked(true);
            if (sideAInsider.getCondom() != null && Boolean.parseBoolean(sideAInsider.getCondom())) binding.mcuCondomTocLayout.setChecked(true);
            if (sideAInsider.getBom() != null && Boolean.parseBoolean(sideAInsider.getBom())) binding.mcuBomTocLayout.setChecked(true);
            if (sideAInsider.getBbt() != null && Boolean.parseBoolean(sideAInsider.getBbt())) binding.mcuBbtTocLayout.setChecked(true);
            if (sideAInsider.getStm() != null && Boolean.parseBoolean(sideAInsider.getStm())) binding.mcuStmTocLayout.setChecked(true);
            if (sideAInsider.getSdm() != null && Boolean.parseBoolean(sideAInsider.getSdm())) binding.mcuSdmTocLayout.setChecked(true);

        }

        void bindAction(Map<String, Object> fpData) {

            final boolean isNewAcceptor = binding.newAcceptorTocLayout.isChecked();

            final boolean isCurrentUser = binding.currentUserTocLayout.isChecked();
            final boolean isChangingMethod = binding.changingMethodCurrentUserTocLayout.isChecked();
            final boolean isChangingClinic = binding.changingClinicCurrentUserTocLayout.isChecked();

            final boolean isCOC = binding.mcuCocTocLayout.isChecked();
            final boolean isPOP = binding.mcuPopTocLayout.isChecked();
            final boolean isInjectable = binding.mcuInjectableTocLayout.isChecked();
            final boolean isImplant = binding.mcuImplantTocLayout.isChecked();
            final boolean isLAM = binding.mcuLamTocLayout.isChecked();
            final boolean isCondom = binding.mcuCondomTocLayout.isChecked();
            final boolean isOthersMCU = binding.mcuPostPartumTocLayout.isChecked();

            othersMCU = binding.mcuOthersEtTocLayout.getText().toString();

            final boolean isBOM = binding.mcuBomTocLayout.isChecked();
            final boolean isBBT = binding.mcuBbtTocLayout.isChecked();
            final boolean isSTM = binding.mcuStmTocLayout.isChecked();
            final boolean isSDM = binding.mcuSdmTocLayout.isChecked();

            addOnlyNecessaryData(isNewAcceptor, "new_acceptor", fpData);

            addOnlyNecessaryData(true, "reason_for_family_planning", fpData);
            addOnlyNecessaryData(true, "drop_or_restart_reason", fpData);
            addOnlyNecessaryData(true, "iud", fpData);

            addOnlyNecessaryData(isCurrentUser, "current_user", fpData);
            addOnlyNecessaryData(isChangingMethod, "changing_method", fpData);
            addOnlyNecessaryData(isChangingClinic, "changing_clinic", fpData);

            addOnlyNecessaryData(isCOC, "coc", fpData);
            addOnlyNecessaryData(isPOP, "pop", fpData);
            addOnlyNecessaryData(isInjectable, "injectable", fpData);
            addOnlyNecessaryData(isImplant, "implant", fpData);
            addOnlyNecessaryData(isLAM, "lam", fpData);

            addOnlyNecessaryData(isCondom, "condom", fpData);
            addOnlyNecessaryData(isOthersMCU, "others_method_currently_used", fpData);
            addOnlyNecessaryData(isBOM, "bom", fpData);
            addOnlyNecessaryData(isBBT, "bbt", fpData);
            addOnlyNecessaryData(isSTM, "stm", fpData);
            addOnlyNecessaryData(isSDM, "sdm", fpData);


        }

        void addOnlyNecessaryData(boolean isNecessary, String node, Map<String, Object> fpData) {

            switch (node) {
                case "reason_for_family_planning":
                    fpData.put(node, reasonForFamilyPlanning);
                    return;
                case "drop_or_restart_reason":
                    fpData.put(node, dropOutOrRestartReason);
                    return;
                case "iud":
                    fpData.put(node, iudType);
                    return;
                case "others_method_currently_used":
                    fpData.put(node, othersMCU.isEmpty() ? "others" : othersMCU);
                    return;
            }

            if (isNecessary) {
                fpData.put(node, true);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if (!b) return;

            if (compoundButton.getId() == binding.reasonForFpSpacingTocLayout.getId()) {
                reasonForFamilyPlanning = "spacing";
                binding.reasonForFpLimitingTocLayout.setChecked(false);
                binding.reasonForFpOthersTocLayout.setChecked(false);

                if (!binding.newAcceptorTocLayout.isChecked())
                    binding.newAcceptorTocLayout.setChecked(true);

                if (!binding.reasonForFpOthersEtTocLayout.getText().toString().trim().isEmpty())
                    binding.reasonForFpOthersEtTocLayout.setText("");

            } else if (compoundButton.getId() == binding.reasonForFpLimitingTocLayout.getId()) {
                reasonForFamilyPlanning = "limiting";
                binding.reasonForFpSpacingTocLayout.setChecked(false);
                binding.reasonForFpOthersTocLayout.setChecked(false);

                if (!binding.reasonForFpOthersEtTocLayout.getText().toString().trim().isEmpty())
                    binding.reasonForFpOthersEtTocLayout.setText("");

            } else if (compoundButton.getId() == binding.reasonForFpOthersTocLayout.getId()) {
                reasonForFamilyPlanning = binding.reasonForFpOthersEtTocLayout.getText().toString().isEmpty() ? "others" : binding.reasonForFpOthersEtTocLayout.getText().toString().trim();
                binding.reasonForFpSpacingTocLayout.setChecked(false);
                binding.reasonForFpLimitingTocLayout.setChecked(false);

            } else if (compoundButton.getId() == binding.reasonMedicalConditionTocLayout.getId()) {
                dropOutOrRestartReason = "medical condition";
                binding.reasonSideEffectsTocLayout.setChecked(false);
                if (!binding.restartCurrentUserTocLayout.isChecked())
                    binding.restartCurrentUserTocLayout.setChecked(true);

            } else if (compoundButton.getId() == binding.reasonSideEffectsTocLayout.getId()) {
                dropOutOrRestartReason = binding.reasonEtTocLayout.getText().toString().trim().isEmpty() ? "side effects" : binding.reasonEtTocLayout.getText().toString().trim();
                binding.reasonMedicalConditionTocLayout.setChecked(false);
                if (!binding.restartCurrentUserTocLayout.isChecked())
                    binding.restartCurrentUserTocLayout.setChecked(true);

            } else if (compoundButton.getId() == binding.mcuIntervalTocLayout.getId()) {
                binding.mcuPostPartumTocLayout.setChecked(false);
                iudType = "interval";

            } else {
                binding.mcuIntervalTocLayout.setChecked(false);
                iudType = "postpartum";
            }
        }

        void clearData() {
            binding.reasonForFpSpacingTocLayout.setOnCheckedChangeListener(null);
            binding.reasonForFpLimitingTocLayout.setOnCheckedChangeListener(null);
            binding.reasonForFpOthersTocLayout.setOnCheckedChangeListener(null);

            binding.reasonMedicalConditionTocLayout.setOnCheckedChangeListener(null);
            binding.reasonSideEffectsTocLayout.setOnCheckedChangeListener(null);

            binding.mcuIntervalTocLayout.setOnCheckedChangeListener(null);
            binding.mcuPostPartumTocLayout.setOnCheckedChangeListener(null);
        }
    }

    static class MedicalHistoryFPViewHolder extends RecyclerView.ViewHolder {

        MedicalHistoryFpBinding binding;
        private final SideAInsider sideAInsider;

        private final String[] propertiesNode;

        public MedicalHistoryFPViewHolder(@NonNull View itemView, SideAInsider sideAInsider) {
            super(itemView);
            binding = MedicalHistoryFpBinding.bind(itemView);
            this.sideAInsider = sideAInsider;
            propertiesNode = new String[] {
                    "severe_headaches_migraine",
                    "history_if_stroke_heart_attack_hypertension",
                    "non_traumatic_hematoma_frequent_bruising_or_gum_bleeding",
                    "current_or_history_of_breast_cancer_breast_mass",
                    "severe_chest_pain",
                    "cough_for_more_than_14_days",
                    "jaundice",
                    "unexplained_vaginal_bleeding",
                    "abnormal_vaginal_discharge",
                    "intake_of_phenobarbital_anti_seizure_or_rifampicin_anti_tb",
                    "is_the_client_a_smoker",
                    "with_disability"
            };
            if (sideAInsider != null) {
                setDefaultInputAndCheckBoxes();
            }
        }

        private void setDefaultInputAndCheckBoxes() {

            if (sideAInsider.getSevereHeadachesMigraine().equals("yes")) binding.mh1Yes.setChecked(true);
            else binding.mh1No.setChecked(true);

            if (sideAInsider.getHistoryIfHeartAttack().equals("yes")) binding.mh2Yes.setChecked(true);
            else binding.mh2No.setChecked(true);

            if (sideAInsider.getNonTraumaticHematoma().equals("yes")) binding.mh3Yes.setChecked(true);
            else binding.mh3No.setChecked(true);

            if (sideAInsider.getCurrentOrHistory().equals("yes")) binding.mh4Yes.setChecked(true);
            else binding.mh4No.setChecked(true);

            if (sideAInsider.getSevereChestPain().equals("yes")) binding.mh5Yes.setChecked(true);
            else binding.mh5No.setChecked(true);

            if (sideAInsider.getCoughForMoreThan().equals("yes")) binding.mh6Yes.setChecked(true);
            else binding.mh6No.setChecked(true);

            if (sideAInsider.getJaundice().equals("yes")) binding.mh7Yes.setChecked(true);
            else binding.mh7No.setChecked(true);

            if (sideAInsider.getUnexplainedVaginalBleeding().equals("yes")) binding.mh8Yes.setChecked(true);
            else binding.mh8No.setChecked(true);

            if (sideAInsider.getAbnormalVaginalDischarge().equals("yes")) binding.mh9Yes.setChecked(true);
            else binding.mh9No.setChecked(true);

            if (sideAInsider.getIntakeOfPhenobarbital().equals("yes")) binding.mh10Yes.setChecked(true);
            else binding.mh10No.setChecked(true);

            if (sideAInsider.getIsTheClientASmoker().equals("yes")) binding.mh11Yes.setChecked(true);
            else binding.mh11No.setChecked(true);

            if (sideAInsider.getWithDisability().equals("no")) {
                binding.mh12No.setChecked(true);
            }
            else {
                binding.mh12Yes.setChecked(true);
                final String _withDisability = sideAInsider.getWithDisability() == null ? "" : sideAInsider.getWithDisability().trim();
                if (!_withDisability.equals("yes") && !_withDisability.isEmpty())
                    binding.withDisabilityFp.setText(_withDisability);
            }

        }

        void bind() {

            binding.mh1Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh1No.setChecked(false);}));
            binding.mh1No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh1Yes.setChecked(false);}));

            binding.mh2Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh2No.setChecked(false);}));
            binding.mh2No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh2Yes.setChecked(false);}));

            binding.mh3Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh3No.setChecked(false);}));
            binding.mh3No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh3Yes.setChecked(false);}));

            binding.mh4Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh4No.setChecked(false);}));
            binding.mh4No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh4Yes.setChecked(false);}));

            binding.mh5Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh5No.setChecked(false);}));
            binding.mh5No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh5Yes.setChecked(false);}));

            binding.mh6Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh6No.setChecked(false);}));
            binding.mh6No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh6Yes.setChecked(false);}));

            binding.mh7Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh7No.setChecked(false);}));
            binding.mh7No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh7Yes.setChecked(false);}));

            binding.mh8Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh8No.setChecked(false);}));
            binding.mh8No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh8Yes.setChecked(false);}));

            binding.mh9Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh9No.setChecked(false);}));
            binding.mh9No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh9Yes.setChecked(false);}));

            binding.mh10Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh10No.setChecked(false);}));
            binding.mh10No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh10Yes.setChecked(false);}));

            binding.mh11Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh11No.setChecked(false);}));
            binding.mh11No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh11Yes.setChecked(false);}));

            binding.mh12Yes.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh12No.setChecked(false);}));
            binding.mh12No.setOnCheckedChangeListener(((compoundButton, b) -> {if (b) binding.mh12Yes.setChecked(false);}));


        }

        void clearData() {
            binding.mh1Yes.setOnCheckedChangeListener(null);
            binding.mh1No.setOnCheckedChangeListener(null);

            binding.mh2Yes.setOnCheckedChangeListener(null);
            binding.mh2No.setOnCheckedChangeListener(null);

            binding.mh3Yes.setOnCheckedChangeListener(null);
            binding.mh3No.setOnCheckedChangeListener(null);

            binding.mh4Yes.setOnCheckedChangeListener(null);
            binding.mh4No.setOnCheckedChangeListener(null);

            binding.mh5Yes.setOnCheckedChangeListener(null);
            binding.mh5No.setOnCheckedChangeListener(null);

            binding.mh7Yes.setOnCheckedChangeListener(null);
            binding.mh7No.setOnCheckedChangeListener(null);

            binding.mh8Yes.setOnCheckedChangeListener(null);
            binding.mh8No.setOnCheckedChangeListener(null);

            binding.mh9Yes.setOnCheckedChangeListener(null);
            binding.mh9No.setOnCheckedChangeListener(null);

            binding.mh10Yes.setOnCheckedChangeListener(null);
            binding.mh10No.setOnCheckedChangeListener(null);

            binding.mh11Yes.setOnCheckedChangeListener(null);
            binding.mh11No.setOnCheckedChangeListener(null);

            binding.mh12Yes.setOnCheckedChangeListener(null);
            binding.mh12No.setOnCheckedChangeListener(null);

            binding.mh6Yes.setOnCheckedChangeListener(null);
            binding.mh6No.setOnCheckedChangeListener(null);
        }

        void bindAction(Map<String, Object> fpData) {

            dataToggle(fpData, binding.mh1Yes, 0);
            dataToggle(fpData, binding.mh2Yes, 1);
            dataToggle(fpData, binding.mh3Yes, 2);
            dataToggle(fpData, binding.mh4Yes, 3);
            dataToggle(fpData, binding.mh5Yes, 4);
            dataToggle(fpData, binding.mh6Yes, 5);
            dataToggle(fpData, binding.mh7Yes, 6);
            dataToggle(fpData, binding.mh8Yes, 7);
            dataToggle(fpData, binding.mh9Yes, 8);
            dataToggle(fpData, binding.mh10Yes, 9);
            dataToggle(fpData, binding.mh11Yes, 10);
            if (binding.mh12Yes.isChecked()) {
                fpData.put(propertiesNode[11], (binding.withDisabilityFp.getText().toString().isEmpty() ? "yes" : binding.withDisabilityFp.getText().toString()));
            }else {
                dataToggle(fpData, binding.mh12Yes, 11);
            }

        }

        void dataToggle(Map<String, Object> fpData, MaterialCheckBox checkBox, int index) {
            String VALUE_DATA_YES = "yes";
            String VALUE_DATA_NO = "no";
            if (checkBox.isChecked()) fpData.put(propertiesNode[index], VALUE_DATA_YES);
            else fpData.put(propertiesNode[index], VALUE_DATA_NO);

        }
    }

    static class ObstetricalHistoryFPViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        ObstetricalHistoryFpBinding binding;
        private boolean isVaginal = false;
        private String menstrualFlow = "";
        private final SideAInsider sideAInsider;

        public ObstetricalHistoryFPViewHolder(@NonNull View itemView, SideAInsider sideAInsider) {
            super(itemView);
            binding = ObstetricalHistoryFpBinding.bind(itemView);

            this.sideAInsider = sideAInsider;

            binding.toldVaginalFp.setOnCheckedChangeListener(this);
            binding.toldCsFp.setOnCheckedChangeListener(this);

            binding.scantyMfCheckFp.setOnCheckedChangeListener(this);
            binding.moderateMfCheckFp.setOnCheckedChangeListener(this);
            binding.heavyMfCheckFp.setOnCheckedChangeListener(this);

            if (sideAInsider != null) {
                setDefaultInputsAndCheckBoxes();
            }
        }

        private void setDefaultInputsAndCheckBoxes() {
            binding.nopEtFp.setText(sideAInsider.getNumberOfPregnancy() == null ? "" : sideAInsider.getNumberOfPregnancy());
            binding.nopFullTermEtFp.setText(sideAInsider.getNumberOfPregnancyFullTerm() == null ? "" : sideAInsider.getNumberOfPregnancyFullTerm());
            binding.nopPrematureEtFp.setText(sideAInsider.getNumberOfPregnancyPremature() == null ? "" : sideAInsider.getNumberOfPregnancyPremature());
            binding.nopAbortionEtFp.setText(sideAInsider.getNumberOfPregnancyAbortion() == null ? "" : sideAInsider.getNumberOfPregnancyAbortion());
            binding.nopLivingChildrenEtFp.setText(sideAInsider.getNumberOfPregnantLivingChildren() == null ? "" : sideAInsider.getNumberOfPregnantLivingChildren());
            binding.doldEtFp.setText(sideAInsider.getDateOfLastDelivery() == null ? "" : sideAInsider.getDateOfLastDelivery());
            if (sideAInsider.getTypeOfLastDelivery().equals("vaginal")) {
                binding.toldVaginalFp.setChecked(true);
            } else if (sideAInsider.getTypeOfLastDelivery().equals("cesarean section")) {
                binding.toldCsFp.setChecked(true);
            }

            binding.lmpEtFp.setText(sideAInsider.getLastOfMenstrualPeriod() == null ? "" : sideAInsider.getLastOfMenstrualPeriod());
            binding.pmpEtFp.setText(sideAInsider.getPreviousMenstrualPeriod() == null ? "" : sideAInsider.getPreviousMenstrualPeriod());

            final String _menstrualFlow = sideAInsider.getMenstrualFlow() == null ? "" : sideAInsider.getMenstrualFlow();

            switch (_menstrualFlow) {
                case "scanty (1–2 pads per day)":
                    binding.scantyMfCheckFp.setChecked(true);
                    break;
                case "heavy (>5 per pads day)":
                    binding.heavyMfCheckFp.setChecked(true);
                    break;
                case "moderate (3–5 pads per day)":
                    binding.moderateMfCheckFp.setChecked(true);
                    break;
            }

            final boolean _dysmenorrhea = sideAInsider.getDysmenorrhea() != null && Boolean.parseBoolean(sideAInsider.getDysmenorrhea());
            final boolean _hydatidiform = sideAInsider.getHydatidiform() != null && Boolean.parseBoolean(sideAInsider.getHydatidiform());
            final boolean _historyOfEctopicPregnancy = sideAInsider.getHistoryOfEctopicPregnancy() != null && Boolean.parseBoolean(sideAInsider.getHistoryOfEctopicPregnancy());

            if (_dysmenorrhea) binding.dysmenorrheaCheckFp.setChecked(true);
            if (_hydatidiform) binding.hydatidiformCheckFp.setChecked(true);
            if (_historyOfEctopicPregnancy) binding.historyOfEctopicPregnancyCheckFp.setChecked(true);
        }

        void bindAction(Map<String, Object> fpData) {
            final String numberOfPregnancies = binding.nopEtFp.getText().toString();
            final String nopFullTerm = binding.nopFullTermEtFp.getText().toString();
            final String nopPremature = binding.nopPrematureEtFp.getText().toString();
            final String nopAbortion = binding.nopAbortionEtFp.getText().toString();
            final String nopLivingChildren = binding.nopLivingChildrenEtFp.getText().toString();
            final String dateOfLastDelivery = binding.doldEtFp.getText().toString();
            final String lastOfMenstrualPeriod = binding.lmpEtFp.getText().toString();
            final String previousMenstrualPeriod = binding.pmpEtFp.getText().toString();

            final boolean isDysmenorrhea = binding.dysmenorrheaCheckFp.isChecked();
            final boolean isHydatidiform = binding.hydatidiformCheckFp.isChecked();
            final boolean isHistoryOfEctopicPregnancy = binding.historyOfEctopicPregnancyCheckFp.isChecked();

            if (isVaginal)
                fpData.put("type_of_last_delivery", "vaginal");
            else
                fpData.put("type_of_last_delivery", "cesarean section");

            if (!numberOfPregnancies.isEmpty())
                fpData.put("number_of_pregnancy", numberOfPregnancies);
            if (!nopFullTerm.isEmpty())
                fpData.put("number_of_pregnancy_full_term", nopFullTerm);
            if (!nopPremature.isEmpty())
                fpData.put("number_of_pregnancy_premature", nopPremature);
            if (!nopAbortion.isEmpty())
                fpData.put("number_of_pregnancy_abortion", nopAbortion);
            if (!nopLivingChildren.isEmpty())
                fpData.put("number_of_pregnant_living_children", nopLivingChildren);
            if (!dateOfLastDelivery.isEmpty())
                fpData.put("date_of_last_delivery", dateOfLastDelivery);
            if (!lastOfMenstrualPeriod.isEmpty())
                fpData.put("last_of_menstrual_period", lastOfMenstrualPeriod);
            if (!previousMenstrualPeriod.isEmpty())
                fpData.put("previous_menstrual_period", previousMenstrualPeriod);
            if (isDysmenorrhea)
                fpData.put("dysmenorrhea", true);
            if (isHydatidiform)
                fpData.put("hydatidiform", true);
            if (isHistoryOfEctopicPregnancy)
                fpData.put("history_of_ectopic_pregnancy", true);

            fpData.put("menstrual_flow", menstrualFlow);



        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!b) return;
            if (compoundButton.getId() == binding.toldVaginalFp.getId()) {
                if (binding.toldCsFp.isChecked())
                    binding.toldCsFp.setChecked(false);
                isVaginal = true;

            } else if (compoundButton.getId() == binding.toldCsFp.getId()){
                if (binding.toldVaginalFp.isChecked())
                    binding.toldVaginalFp.setChecked(false);
                isVaginal = false;

            } else if (compoundButton.getId() == binding.scantyMfCheckFp.getId()) {
                if (binding.moderateMfCheckFp.isChecked())
                    binding.moderateMfCheckFp.setChecked(false);
                if (binding.heavyMfCheckFp.isChecked())
                    binding.heavyMfCheckFp.setChecked(false);
                menstrualFlow = itemView.getResources().getString(R.string.scanty_1_2_pads_per_day);

            } else if (compoundButton.getId() == binding.moderateMfCheckFp.getId()) {
                if (binding.scantyMfCheckFp.isChecked())
                    binding.scantyMfCheckFp.setChecked(false);
                if (binding.heavyMfCheckFp.isChecked())
                    binding.heavyMfCheckFp.setChecked(false);
                menstrualFlow = itemView.getResources().getString(R.string.heavy_5_per_pads_day);

            } else if (compoundButton.getId() == binding.heavyMfCheckFp.getId()) {
                if (binding.scantyMfCheckFp.isChecked())
                    binding.scantyMfCheckFp.setChecked(false);
                if (binding.moderateMfCheckFp.isChecked())
                    binding.moderateMfCheckFp.setChecked(false);
                menstrualFlow = itemView.getResources().getString(R.string.moderate_3_5_pads_per_day);

            }
        }
    }

    static class RFSTIFPViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        RfstiFpBinding binding;
        private final SideAInsider sideAInsider;
        String questionOne = "", questionTwo = "", questionThree = "", questionFour = "", questionFive = "", questionOneYes = "";

        public RFSTIFPViewHolder(@NonNull View itemView, SideAInsider sideAInsider) {
            super(itemView);
            binding = RfstiFpBinding.bind(itemView);

            this.sideAInsider = sideAInsider;

            binding.rfsti1Yes.setOnCheckedChangeListener(this);
            binding.rfsti1No.setOnCheckedChangeListener(this);

            binding.rfsti2Yes.setOnCheckedChangeListener(this);
            binding.rfsti2No.setOnCheckedChangeListener(this);

            binding.rfsti3Yes.setOnCheckedChangeListener(this);
            binding.rfsti3No.setOnCheckedChangeListener(this);

            binding.rfsti4Yes.setOnCheckedChangeListener(this);
            binding.rfsti4No.setOnCheckedChangeListener(this);

            binding.rfsti5Yes.setOnCheckedChangeListener(this);
            binding.rfsti5No.setOnCheckedChangeListener(this);

            binding.rfsti1YesPenis.setOnCheckedChangeListener(this);
            binding.rfsti1YesVagina.setOnCheckedChangeListener(this);

            if (sideAInsider != null) {
                setDefaultInputsAndCheckBoxes();
            }

        }

        private void setDefaultInputsAndCheckBoxes() {
            if (sideAInsider.getAbnormalDischargeFromTheGenitalArea().equals("yes")) {
                binding.rfsti1Yes.setChecked(true);
                if (sideAInsider.getAbnormalDischargeFromTheGenitalAreaYes().equals("vagina")) {
                    binding.rfsti1YesVagina.setChecked(true);
                } else if (sideAInsider.getAbnormalDischargeFromTheGenitalAreaYes().equals("penis")){
                    binding.rfsti1YesPenis.setChecked(true);
                }
            } else {
                binding.rfsti1No.setChecked(true);
            }

            if (sideAInsider.getScoreOrUlcersInTheGenitalArea().equals("yes")) binding.rfsti2Yes.setChecked(true);
            else binding.rfsti2No.setChecked(true);

            if (sideAInsider.getPainOrBurningSensationInTheGenitalArea().equals("yes")) binding.rfsti3Yes.setChecked(true);
            else binding.rfsti3No.setChecked(true);

            if (sideAInsider.getHistoryOfTreatmentForSexuallyTransmittedInfections().equals("yes")) binding.rfsti4Yes.setChecked(true);
            else binding.rfsti4No.setChecked(true);

            if (sideAInsider.getHivAidsPelvicInflammatoryDisease().equals("yes")) binding.rfsti5Yes.setChecked(true);
            else binding.rfsti5No.setChecked(true);

        }

        void bindAction(Map<String, Object> fpData) {
            if (!questionOne.isEmpty())
                fpData.put("abnormal_discharge_from_the_genital_area", questionOne);
            if (!questionOneYes.isEmpty())
                fpData.put("abnormal_discharge_from_the_genital_area_yes", questionOneYes);
            if (!questionTwo.isEmpty())
                fpData.put("score_or_ulcers_in_the_genital_area", questionTwo);
            if (!questionThree.isEmpty())
                fpData.put("pain_or_burning_sensation_in_the_genital_area", questionThree);
            if (!questionFour.isEmpty())
                fpData.put("history_of_treatment_for_sexually_transmitted_infections", questionFour);
            if (!questionFive.isEmpty())
                fpData.put("hiv_aids_pelvic_inflammatory_disease", questionFive);
        }

        void clearData() {
            binding.rfsti1Yes.setOnCheckedChangeListener(null);
            binding.rfsti1No.setOnCheckedChangeListener(null);

            binding.rfsti2Yes.setOnCheckedChangeListener(null);
            binding.rfsti2No.setOnCheckedChangeListener(null);

            binding.rfsti3Yes.setOnCheckedChangeListener(null);
            binding.rfsti3No.setOnCheckedChangeListener(null);

            binding.rfsti4Yes.setOnCheckedChangeListener(null);
            binding.rfsti4No.setOnCheckedChangeListener(null);

            binding.rfsti5Yes.setOnCheckedChangeListener(null);
            binding.rfsti5No.setOnCheckedChangeListener(null);

            binding.rfsti1YesPenis.setOnCheckedChangeListener(null);
            binding.rfsti1YesVagina.setOnCheckedChangeListener(null);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!b) return;

            if (compoundButton.getId() == binding.rfsti1Yes.getId()) {
                binding.rfsti1No.setChecked(false);
                questionOne = "yes";
            } else if (compoundButton.getId() == binding.rfsti1No.getId()) {
                binding.rfsti1Yes.setChecked(false);
                questionOne = "no";
            } else if (compoundButton.getId() == binding.rfsti2Yes.getId()) {
                binding.rfsti2No.setChecked(false);
                questionTwo = "yes";
            } else if (compoundButton.getId() == binding.rfsti2No.getId()) {
                binding.rfsti2Yes.setChecked(false);
                questionTwo = "no";
            } else if (compoundButton.getId() == binding.rfsti3Yes.getId()) {
                binding.rfsti3No.setChecked(false);
                questionThree = "yes";
            } else if (compoundButton.getId() == binding.rfsti3No.getId()) {
                binding.rfsti3Yes.setChecked(false);
                questionThree = "no";
            } else if (compoundButton.getId() == binding.rfsti4Yes.getId()) {
                binding.rfsti4No.setChecked(false);
                questionFour = "yes";
            } else if (compoundButton.getId() == binding.rfsti4No.getId()) {
                binding.rfsti4Yes.setChecked(false);
                questionFour = "no";
            } else if (compoundButton.getId() == binding.rfsti5Yes.getId()) {
                binding.rfsti5No.setChecked(false);
                questionFive = "yes";
            } else if (compoundButton.getId() == binding.rfsti5No.getId()) {
                binding.rfsti5Yes.setChecked(false);
                questionFive = "no";
            } else if (compoundButton.getId() == binding.rfsti1YesPenis.getId()) {
                binding.rfsti1YesVagina.setChecked(false);
                questionOneYes = "penis";
                if (!binding.rfsti1Yes.isChecked())
                    binding.rfsti1Yes.setChecked(true);
            } else {
                binding.rfsti1YesPenis.setChecked(false);
                questionOneYes = "vagina";
                if (!binding.rfsti1Yes.isChecked())
                    binding.rfsti1Yes.setChecked(true);
            }
        }
    }

    static class PhysicalExaminationFPViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        PhysicalExaminationFpBinding binding;
        private final SideAInsider sideAInsider;
        private String skin = "", conjunctiva = "", neck = "", breast = "", abdomen = "", extremities = "",
                pelvicExaminationCervicalAbnormalities = "",pelvicExaminationCervicalConsistency = "", pelvicExaminationUterinePosition = "";

        public PhysicalExaminationFPViewHolder(@NonNull View itemView, SideAInsider sideAInsider) {
            super(itemView);
            binding = PhysicalExaminationFpBinding.bind(itemView);
            this.sideAInsider = sideAInsider;

            //Skin
            binding.normalCheckSkinPeFp.setOnCheckedChangeListener(this);
            binding.paleCheckSkinPeFp.setOnCheckedChangeListener(this);
            binding.yellowishCheckSkinPeFp.setOnCheckedChangeListener(this);
            binding.hematomaCheckSkinPeFp.setOnCheckedChangeListener(this);

            //Conjunctiva
            binding.normalCheckConjunctivaPeFp.setOnCheckedChangeListener(this);
            binding.paleCheckConjunctivaPeFp.setOnCheckedChangeListener(this);
            binding.yellowishCheckConjunctivaPeFp.setOnCheckedChangeListener(this);

            //Neck
            binding.neckMassCheckNeckPeFp.setOnCheckedChangeListener(this);
            binding.normalCheckNeckPeFp.setOnCheckedChangeListener(this);
            binding.elnCheckNeckPeFp.setOnCheckedChangeListener(this);

            //Breast
            binding.lefPeFp.normalBreastPeFp.setOnCheckedChangeListener(this);
            binding.lefPeFp.massBreastPeFp.setOnCheckedChangeListener(this);
            binding.lefPeFp.nippleDischargeBreastPeFp.setOnCheckedChangeListener(this);

            //Abdomen
            binding.lefPeFp.normalAbdomenPeFp.setOnCheckedChangeListener(this);
            binding.lefPeFp.abdominalMassAbdomenPeFp.setOnCheckedChangeListener(this);
            binding.lefPeFp.varicositiesAndominalPeFp.setOnCheckedChangeListener(this);

            //Extremities
            binding.normalCheckExtremitiesPeFp.setOnCheckedChangeListener(this);
            binding.edemaCheckExtremitiesPeFp.setOnCheckedChangeListener(this);
            binding.varicositiesCheckExtremitiesPeFp.setOnCheckedChangeListener(this);

            //Pelvic Examination >>> Cervical Abnormalities
            binding.wartsCaPelvicExaminationPeFp.setOnCheckedChangeListener(this);
            binding.pocCaPelvicExaminationPeFp.setOnCheckedChangeListener(this);
            binding.ioeCaPelvicExaminationPeFp.setOnCheckedChangeListener(this);
            binding.bdCaPelvicExaminationPeFp.setOnCheckedChangeListener(this);

            //Pelvic Examination >>> Cervical Consistency
            binding.firmCcPelvicExaminationPeFp.setOnCheckedChangeListener(this);
            binding.softCcPelvicExaminationPeFp.setOnCheckedChangeListener(this);

            //Pelvic Examination >>> Uterine Position
            binding.rightPeFp.midUpPelvicExaminationPeFp.setOnCheckedChangeListener(this);
            binding.rightPeFp.anteflexedUpPelvicExaminationPeFp.setOnCheckedChangeListener(this);
            binding.rightPeFp.retroflexedUpPelvicExaminationPeFp.setOnCheckedChangeListener(this);

            //Pelvic Examination
            binding.rightPeFp.udPelvicExaminationPeFp.setOnCheckedChangeListener(this);

            if (sideAInsider != null) {
                setDefaultInputsCheckBoxes();
            }

        }

        private void setDefaultInputsCheckBoxes() {
            binding.weightEtPeFp.setText(sideAInsider.getWeight() == null ? "" : sideAInsider.getWeight());
            binding.heightEtPeFp.setText(sideAInsider.getHeight() == null ? "" : sideAInsider.getHeight());
            binding.bloodPressureEtPeFp.setText(sideAInsider.getBloodPressure() == null ? "" : sideAInsider.getBloodPressure());
            binding.pulseRateEtPeFp.setText(sideAInsider.getPulseRate() == null ? "" : sideAInsider.getPulseRate());

            final String _skin = sideAInsider.getSkin() == null ? "" : sideAInsider.getSkin();
            final String _conjunctiva = sideAInsider.getConjunctiva() == null ? "" : sideAInsider.getConjunctiva();
            final String _neck = sideAInsider.getNeck() == null ? "" : sideAInsider.getNeck();
            final String _abdomen = sideAInsider.getAbdomen() == null ? "" : sideAInsider.getAbdomen();
            final String _breast = sideAInsider.getBreast() == null ? "" : sideAInsider.getBreast();
            final String _extremities = sideAInsider.getExtremities() == null ? "" : sideAInsider.getExtremities();
            final String _pelvicExamination = sideAInsider.getPelvicExamination() == null ? "" : sideAInsider.getPelvicExamination();

            switch (_skin) {
                case "normal":
                    binding.normalCheckSkinPeFp.setChecked(true);
                    break;
                case "pale":
                    binding.paleCheckSkinPeFp.setChecked(true);
                    break;
                case "yellowish":
                    binding.yellowishCheckSkinPeFp.setChecked(true);
                    break;
                case "hematoma":
                    binding.hematomaCheckSkinPeFp.setChecked(true);
                    break;
            }

            switch (_conjunctiva) {
                case "normal":
                    binding.normalCheckConjunctivaPeFp.setChecked(true);
                    break;
                case "pale":
                    binding.paleCheckConjunctivaPeFp.setChecked(true);
                    break;
                case "yellowish":
                    binding.yellowishCheckConjunctivaPeFp.setChecked(true);
                    break;
            }

            switch (_neck) {
                case "normal":
                    binding.normalCheckNeckPeFp.setChecked(true);
                    break;
                case "neck mass":
                    binding.neckMassCheckNeckPeFp.setChecked(true);
                    break;
                case "enlarge lymph node":
                    binding.elnCheckNeckPeFp.setChecked(true);
                    break;
            }

            switch (_breast) {
                case "normal":
                    binding.lefPeFp.normalBreastPeFp.setChecked(true);
                    break;
                case "mass":
                    binding.lefPeFp.massBreastPeFp.setChecked(true);
                    break;
                case "nipple discharge":
                    binding.lefPeFp.nippleDischargeBreastPeFp.setChecked(true);
                    break;
            }

            switch (_abdomen) {
                case "normal":
                    binding.lefPeFp.normalAbdomenPeFp.setChecked(true);
                    break;
                case "abdominal mass":
                    binding.lefPeFp.abdominalMassAbdomenPeFp.setChecked(true);
                    break;
                case "varicosities":
                    binding.lefPeFp.varicositiesAndominalPeFp.setChecked(true);
                    break;
            }

            switch (_extremities) {
                case "normal":
                    binding.normalCheckExtremitiesPeFp.setChecked(true);
                    break;
                case "edema":
                    binding.edemaCheckExtremitiesPeFp.setChecked(true);
                    break;
                case "varicosities":
                    binding.varicositiesCheckExtremitiesPeFp.setChecked(true);
                    break;
            }

            if (sideAInsider.getUterineDepthCm() != null) {
                binding.rightPeFp.udPelvicExaminationPeFp.setChecked(true);
                binding.rightPeFp.cmEtUdPelvicExaminationPeFp.setText(sideAInsider.getUterineDepthCm());
            }

            switch (_pelvicExamination) {
                case "normal":
                    binding.normalPelvicExaminationPeFp.setChecked(true);
                    break;
                case "mass":
                    binding.massPelvicExaminationPeFp.setChecked(true);
                    break;
                case "abnormal discharge":
                    binding.abnormalDischargePelvicExaminationPeFp.setChecked(true);
                    break;
                case "cervical abnormalities":
                    binding.caPelvicExaminationPeFp.setChecked(true);
                    break;
                case "cervical tenderness":
                    binding.rightPeFp.ctPelvicExaminationPeFp.setChecked(true);
                    break;
                case "adnexal mass/tenderness":
                    binding.rightPeFp.amtPelvicExaminationPeFp.setChecked(true);
                    break;
                case "cervical consistency":
                    binding.ccPelvicExaminationPeFp.setChecked(true);
                    break;
            }

        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if (!b) return;

            if (binding.normalCheckSkinPeFp.getId() == compoundButton.getId()) {
                skin = "normal";
                binding.paleCheckSkinPeFp.setChecked(false);
                binding.yellowishCheckSkinPeFp.setChecked(false);
                binding.hematomaCheckSkinPeFp.setChecked(false);
                return;
            }

            if (binding.paleCheckSkinPeFp.getId() == compoundButton.getId()) {
                skin = "pale";
                binding.normalCheckSkinPeFp.setChecked(false);
                binding.yellowishCheckSkinPeFp.setChecked(false);
                binding.hematomaCheckSkinPeFp.setChecked(false);
                return;
            }

            if (binding.yellowishCheckSkinPeFp.getId() == compoundButton.getId()) {
                skin = "yellowish";
                binding.normalCheckSkinPeFp.setChecked(false);
                binding.paleCheckSkinPeFp.setChecked(false);
                binding.hematomaCheckSkinPeFp.setChecked(false);
                return;
            }

            if (binding.hematomaCheckSkinPeFp.getId() == compoundButton.getId()) {
                skin = "hematoma";
                binding.normalCheckSkinPeFp.setChecked(false);
                binding.paleCheckSkinPeFp.setChecked(false);
                binding.yellowishCheckSkinPeFp.setChecked(false);
                return;
            }

            if (binding.normalCheckConjunctivaPeFp.getId() == compoundButton.getId()) {
                binding.paleCheckConjunctivaPeFp.setChecked(false);
                binding.yellowishCheckConjunctivaPeFp.setChecked(false);
                conjunctiva = "normal";
                return;
            }

            if (binding.paleCheckConjunctivaPeFp.getId() == compoundButton.getId()) {
                binding.normalCheckConjunctivaPeFp.setChecked(false);
                binding.yellowishCheckConjunctivaPeFp.setChecked(false);
                conjunctiva = "pale";
                return;
            }

            if (binding.yellowishCheckConjunctivaPeFp.getId() == compoundButton.getId()) {
                binding.normalCheckConjunctivaPeFp.setChecked(false);
                binding.paleCheckConjunctivaPeFp.setChecked(false);
                conjunctiva = "yellowish";
                return;
            }

            if (binding.neckMassCheckNeckPeFp.getId() == compoundButton.getId()) {
                neck = "neck mass";
                binding.normalCheckNeckPeFp.setChecked(false);
                binding.elnCheckNeckPeFp.setChecked(false);
                return;
            }

            if (binding.normalCheckNeckPeFp.getId() == compoundButton.getId()) {
                neck = "normal";
                binding.neckMassCheckNeckPeFp.setChecked(false);
                binding.elnCheckNeckPeFp.setChecked(false);
                return;
            }

            if (binding.elnCheckNeckPeFp.getId() == compoundButton.getId()) {
                neck = "enlarge lymph node";
                binding.neckMassCheckNeckPeFp.setChecked(false);
                binding.normalCheckNeckPeFp.setChecked(false);
                return;
            }

            if (binding.lefPeFp.normalBreastPeFp.getId() == compoundButton.getId()) {
                breast = "normal";
                binding.lefPeFp.massBreastPeFp.setChecked(false);
                binding.lefPeFp.nippleDischargeBreastPeFp.setChecked(false);
                return;
            }

            if (binding.lefPeFp.massBreastPeFp.getId() == compoundButton.getId()) {
                breast = "mass";
                binding.lefPeFp.normalBreastPeFp.setChecked(false);
                binding.lefPeFp.nippleDischargeBreastPeFp.setChecked(false);
                return;
            }

            if (binding.lefPeFp.nippleDischargeBreastPeFp.getId() == compoundButton.getId()) {
                breast = "nipple discharge";
                binding.lefPeFp.normalBreastPeFp.setChecked(false);
                binding.lefPeFp.massBreastPeFp.setChecked(false);
                return;
            }

            if (binding.lefPeFp.normalAbdomenPeFp.getId() == compoundButton.getId()) {
                abdomen = "normal";
                binding.lefPeFp.abdominalMassAbdomenPeFp.setChecked(false);
                binding.lefPeFp.varicositiesAndominalPeFp.setChecked(false);
                return;
            }

            if (binding.lefPeFp.abdominalMassAbdomenPeFp.getId() == compoundButton.getId()) {
                abdomen = "abdominal mass";
                binding.lefPeFp.normalAbdomenPeFp.setChecked(false);
                binding.lefPeFp.varicositiesAndominalPeFp.setChecked(false);
                return;
            }

            if (binding.lefPeFp.varicositiesAndominalPeFp.getId() == compoundButton.getId()) {
                abdomen = "varicosities";
                binding.lefPeFp.normalAbdomenPeFp.setChecked(false);
                binding.lefPeFp.abdominalMassAbdomenPeFp.setChecked(false);
                return;
            }

            if (binding.normalCheckExtremitiesPeFp.getId() == compoundButton.getId()) {
                extremities = "normal";
                binding.edemaCheckExtremitiesPeFp.setChecked(false);
                binding.varicositiesCheckExtremitiesPeFp.setChecked(false);
                return;
            }

            if (binding.edemaCheckExtremitiesPeFp.getId() == compoundButton.getId()) {
                extremities = "edema";
                binding.normalCheckExtremitiesPeFp.setChecked(false);
                binding.varicositiesCheckExtremitiesPeFp.setChecked(false);
                return;
            }

            if (binding.varicositiesCheckExtremitiesPeFp.getId() == compoundButton.getId()) {
                extremities = "varicosities";
                binding.normalCheckExtremitiesPeFp.setChecked(false);
                binding.edemaCheckExtremitiesPeFp.setChecked(false);
                return;
            }

            if (binding.wartsCaPelvicExaminationPeFp.getId() == compoundButton.getId()) {
                pelvicExaminationCervicalAbnormalities = "warts";

                binding.pocCaPelvicExaminationPeFp.setChecked(false);
                binding.ioeCaPelvicExaminationPeFp.setChecked(false);
                binding.bdCaPelvicExaminationPeFp.setChecked(false);
                return;
            }

            if (binding.pocCaPelvicExaminationPeFp.getId() == compoundButton.getId()) {
                pelvicExaminationCervicalAbnormalities = "polyp or cyst";

                binding.wartsCaPelvicExaminationPeFp.setChecked(false);
                binding.ioeCaPelvicExaminationPeFp.setChecked(false);
                binding.bdCaPelvicExaminationPeFp.setChecked(false);
                return;
            }

            if (binding.ioeCaPelvicExaminationPeFp.getId() == compoundButton.getId()) {
                pelvicExaminationCervicalAbnormalities = "inflammation or erosion";

                binding.wartsCaPelvicExaminationPeFp.setChecked(false);
                binding.pocCaPelvicExaminationPeFp.setChecked(false);
                binding.bdCaPelvicExaminationPeFp.setChecked(false);
                return;
            }

            if (binding.bdCaPelvicExaminationPeFp.getId() == compoundButton.getId()) {
                pelvicExaminationCervicalAbnormalities = "bloody discharge";

                binding.wartsCaPelvicExaminationPeFp.setChecked(false);
                binding.pocCaPelvicExaminationPeFp.setChecked(false);
                binding.ioeCaPelvicExaminationPeFp.setChecked(false);
                return;
            }

            if (binding.firmCcPelvicExaminationPeFp.getId() == compoundButton.getId()) {
                pelvicExaminationCervicalConsistency = "firm";
                binding.softCcPelvicExaminationPeFp.setChecked(false);
                return;
            }

            if (binding.softCcPelvicExaminationPeFp.getId() == compoundButton.getId()) {
                pelvicExaminationCervicalConsistency = "soft";
                binding.firmCcPelvicExaminationPeFp.setChecked(false);
                return;
            }

            if (binding.rightPeFp.midUpPelvicExaminationPeFp.getId() == compoundButton.getId()) {
                binding.rightPeFp.anteflexedUpPelvicExaminationPeFp.setChecked(false);
                binding.rightPeFp.retroflexedUpPelvicExaminationPeFp.setChecked(false);
                pelvicExaminationUterinePosition = "mid";
                return;
            }

            if (binding.rightPeFp.anteflexedUpPelvicExaminationPeFp.getId() == compoundButton.getId()) {
                binding.rightPeFp.midUpPelvicExaminationPeFp.setChecked(false);
                binding.rightPeFp.retroflexedUpPelvicExaminationPeFp.setChecked(false);
                pelvicExaminationUterinePosition = "anteflexed";
                return;
            }

            if (binding.rightPeFp.udPelvicExaminationPeFp.getId() == compoundButton.getId()) {
                binding.rightPeFp.cmEtUdPelvicExaminationPeFp.requestFocus();
                return;
            }

            binding.rightPeFp.midUpPelvicExaminationPeFp.setChecked(false);
            binding.rightPeFp.anteflexedUpPelvicExaminationPeFp.setChecked(false);
            pelvicExaminationUterinePosition = "retroflexed";

        }

        void bindAction(Map<String, Object> fpData) {
            if (!skin.isEmpty()) {
                fpData.put("skin", skin);
            }

            if (!conjunctiva.isEmpty()) {
                fpData.put("conjunctiva", conjunctiva);
            }

            if (!neck.isEmpty()) {
                fpData.put("neck", neck);
            }

            if (!breast.isEmpty()) {
                fpData.put("breast", breast);
            }

            if (!abdomen.isEmpty()) {
                fpData.put("abdomen", abdomen);
            }

            if (!extremities.isEmpty()) {
                fpData.put("extremities", extremities);
            }

            if (binding.normalPelvicExaminationPeFp.isChecked())
                fpData.put("pelvic_examination", "normal");
            else if (binding.massPelvicExaminationPeFp.isChecked())
                fpData.put("pelvic_examination", "mass");
            else if (binding.abnormalDischargePelvicExaminationPeFp.isChecked())
                fpData.put("pelvic_examination", "abnormal discharge");
            else if (binding.rightPeFp.ctPelvicExaminationPeFp.isChecked())
                fpData.put("pelvic_examination", "cervical tenderness");
            else if (binding.rightPeFp.amtPelvicExaminationPeFp.isChecked())
                fpData.put("pelvic_examination", "adnexal mass/tenderness");


             if (!pelvicExaminationCervicalAbnormalities.isEmpty()) {
                 fpData.put("pelvic_examination", "cervical abnormalities");
                 fpData.put("pelvic_examination_cervical_abnormalities", pelvicExaminationCervicalAbnormalities);
             }

             if (!pelvicExaminationCervicalConsistency.isEmpty()) {
                 fpData.put("pelvic_examination", "cervical consistency");
                 fpData.put("pelvic_examination_cervical_consistency", pelvicExaminationCervicalConsistency);
             }

             if (!pelvicExaminationUterinePosition.isEmpty()) {
                 fpData.put("pelvic_examination", "uterine position");
                 fpData.put("pelvic_examination_uterine_position", pelvicExaminationUterinePosition);
             }

             String cmEditText = binding.rightPeFp.cmEtUdPelvicExaminationPeFp.getText().toString().trim();

             if (!cmEditText.isEmpty()) {
                 fpData.put("uterine_depth_cm", cmEditText);
             }

             final String weight = binding.weightEtPeFp.getEditableText().toString();
             final String height = binding.heightEtPeFp.getEditableText().toString();
             final String bloodPressure = binding.bloodPressureEtPeFp.getEditableText().toString();
             final String pulseRate = binding.pulseRateEtPeFp.getEditableText().toString();

             if (!weight.isEmpty()) fpData.put("weight", weight);
             if (!height.isEmpty()) fpData.put("height", height);
             if (!bloodPressure.isEmpty()) fpData.put("blood_pressure", bloodPressure);
             if (!pulseRate.isEmpty()) fpData.put("pulse_rate", pulseRate);
        }

        void clearData() {
            //Skin
            binding.normalCheckSkinPeFp.setOnCheckedChangeListener(null);
            binding.paleCheckSkinPeFp.setOnCheckedChangeListener(null);
            binding.yellowishCheckSkinPeFp.setOnCheckedChangeListener(null);
            binding.hematomaCheckSkinPeFp.setOnCheckedChangeListener(null);

            //Conjunctiva
            binding.normalCheckConjunctivaPeFp.setOnCheckedChangeListener(null);
            binding.paleCheckConjunctivaPeFp.setOnCheckedChangeListener(null);
            binding.yellowishCheckConjunctivaPeFp.setOnCheckedChangeListener(null);

            //Neck
            binding.neckMassCheckNeckPeFp.setOnCheckedChangeListener(null);
            binding.normalCheckNeckPeFp.setOnCheckedChangeListener(null);
            binding.elnCheckNeckPeFp.setOnCheckedChangeListener(null);

            //Breast
            binding.lefPeFp.normalBreastPeFp.setOnCheckedChangeListener(null);
            binding.lefPeFp.massBreastPeFp.setOnCheckedChangeListener(null);
            binding.lefPeFp.nippleDischargeBreastPeFp.setOnCheckedChangeListener(null);

            //Abdomen
            binding.lefPeFp.normalAbdomenPeFp.setOnCheckedChangeListener(null);
            binding.lefPeFp.abdominalMassAbdomenPeFp.setOnCheckedChangeListener(null);
            binding.lefPeFp.varicositiesAndominalPeFp.setOnCheckedChangeListener(null);

            //Extremities
            binding.normalCheckExtremitiesPeFp.setOnCheckedChangeListener(null);
            binding.edemaCheckExtremitiesPeFp.setOnCheckedChangeListener(null);
            binding.varicositiesCheckExtremitiesPeFp.setOnCheckedChangeListener(null);

            //Pelvic Examination >>> Cervical Abnormalities
            binding.wartsCaPelvicExaminationPeFp.setOnCheckedChangeListener(null);
            binding.pocCaPelvicExaminationPeFp.setOnCheckedChangeListener(null);
            binding.ioeCaPelvicExaminationPeFp.setOnCheckedChangeListener(null);
            binding.bdCaPelvicExaminationPeFp.setOnCheckedChangeListener(null);

            //Pelvic Examination >>> Cervical Consistency
            binding.firmCcPelvicExaminationPeFp.setOnCheckedChangeListener(null);
            binding.softCcPelvicExaminationPeFp.setOnCheckedChangeListener(null);

            //Pelvic Examination >>> Uterine Position
            binding.rightPeFp.midUpPelvicExaminationPeFp.setOnCheckedChangeListener(null);
            binding.rightPeFp.anteflexedUpPelvicExaminationPeFp.setOnCheckedChangeListener(null);
            binding.rightPeFp.retroflexedUpPelvicExaminationPeFp.setOnCheckedChangeListener(null);

            //Pelvic Examination
            binding.rightPeFp.udPelvicExaminationPeFp.setOnCheckedChangeListener(null);
        }
    }

}
