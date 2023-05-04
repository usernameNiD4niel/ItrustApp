package com.rey.itrustapplication.adminform.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adminform.activities.PatientsDateFPActivity;
import com.rey.itrustapplication.adminform.model.FormModel;
import com.rey.itrustapplication.databinding.FormReyclerviewItemBinding;

import java.util.ArrayList;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormContentViewHolder> {

    private final ArrayList<FormModel> formModels;

    public FormAdapter(ArrayList<FormModel> formModels) {
        this.formModels = formModels;
    }

    @NonNull
    @Override
    public FormAdapter.FormContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_reyclerview_item, parent, false);
        return new FormContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormAdapter.FormContentViewHolder holder, int position) {

        holder.bind(formModels.get(position));
    }

    @Override
    public int getItemCount() {
        return formModels.size();
    }

    public static class FormContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FormReyclerviewItemBinding binding;
        private FormModel formModel;

        public FormContentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FormReyclerviewItemBinding.bind(itemView);
            itemView.setOnClickListener(this);
        }

        void bind(FormModel formModel) {
            this.formModel = formModel;
            binding.patientNameTextFpItem.setText(formModel.getPatientName());
            binding.recentlyTimeAddedTextFpItem.setText(formModel.getTimeAdded());
            binding.recentlyDateAddedTextFpItem.setText(formModel.getDateAdded());
            binding.totalRecordsCreatedTextFpItem.setText(formModel.getTotalRecords());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PatientsDateFPActivity.class);
            intent.putExtra("patient_name", formModel.getPatientName());
            view.getContext().startActivity(intent);

            Toast.makeText(view.getContext(), "Patient " + formModel.getPatientName() + " is previewing!", Toast.LENGTH_SHORT).show();
        }
    }

}
