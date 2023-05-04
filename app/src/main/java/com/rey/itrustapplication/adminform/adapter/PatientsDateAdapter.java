package com.rey.itrustapplication.adminform.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adminform.activities.AddFormFP;
import com.rey.itrustapplication.adminform.activities.PatientsDateFPActivity;
import com.rey.itrustapplication.adminform.model.FamilyPlanningModel;
import com.rey.itrustapplication.adminform.model.PatientsDateModel;
import com.rey.itrustapplication.databinding.PatientsDateItemBinding;

import java.util.List;

public class PatientsDateAdapter extends RecyclerView.Adapter<PatientsDateAdapter.PatientsDateViewHolder> {

    private final List<PatientsDateModel> patientsDateModelList;
    private final String patientName;
    private final PatientsDateFPActivity patientsDateFPActivity;
    private final FamilyPlanningModel familyPlanningModel;

    public PatientsDateAdapter(List<PatientsDateModel> patientsDateModelList, String patientName, PatientsDateFPActivity patientsDateFPActivity, FamilyPlanningModel familyPlanningModel) {
        this.patientsDateModelList = patientsDateModelList;
        this.patientName = patientName;
        this.patientsDateFPActivity = patientsDateFPActivity;
        this.familyPlanningModel = familyPlanningModel;
    }

    @NonNull
    @Override
    public PatientsDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PatientsDateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.patients_date_item, parent, false), familyPlanningModel);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsDateViewHolder holder, int position) {
        holder.bind(patientsDateModelList.get(position), patientName, patientsDateFPActivity);
    }

    @Override
    public int getItemCount() {
        return patientsDateModelList.size();
    }

    static class PatientsDateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        PatientsDateItemBinding binding;
        private String patientName, dateKey;
        private PatientsDateFPActivity patientsDateFPActivity;
        private final FamilyPlanningModel familyPlanningModel;

        public PatientsDateViewHolder(@NonNull View itemView, FamilyPlanningModel familyPlanningModel) {
            super(itemView);
            binding = PatientsDateItemBinding.bind(itemView);
            this.familyPlanningModel = familyPlanningModel;
            itemView.setOnClickListener(this);
        }

        void bind(PatientsDateModel patientsDateModel, String patientName, PatientsDateFPActivity patientsDateFPActivity) {
            this.patientName = patientName;
            this.patientsDateFPActivity = patientsDateFPActivity;
            dateKey = patientsDateModel.getDateKey();
            binding.datePatientsDateItem.setText(patientsDateModel.getDate());
            binding.timePatientsDateItem.setText(patientsDateModel.getTime());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), AddFormFP.class);
            intent.putExtra("patient_name", patientName);
            intent.putExtra("isPreviewing", true);
            intent.putExtra("model", familyPlanningModel);
            intent.putExtra("current_date", dateKey);

            view.getContext().startActivity(intent);
            patientsDateFPActivity.finish();

        }
    }

}
