package com.rey.itrustapplication.adminform.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adminform.activities.PrenatalCareAddForm;
import com.rey.itrustapplication.adminform.model.PatientsDateModel;
import com.rey.itrustapplication.databinding.PatientsDateItemBinding;

import java.util.List;

public class PrenatalCareDateAdapter extends RecyclerView.Adapter<PrenatalCareDateAdapter.PrenatalCareViewHolder> {

    private final List<PatientsDateModel> models;
    private final String nodeName, type;

    public PrenatalCareDateAdapter(List<PatientsDateModel> models, String nodeName, String type) {
        this.models = models;
        this.nodeName = nodeName;
        this.type = type;
    }

    @NonNull
    @Override
    public PrenatalCareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrenatalCareViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.patients_date_item, parent, false), nodeName, type);
    }

    @Override
    public void onBindViewHolder(@NonNull PrenatalCareViewHolder holder, int position) {
        holder.bind(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    static class PrenatalCareViewHolder extends RecyclerView.ViewHolder {

        PatientsDateItemBinding binding;
        private PatientsDateModel patientsDateModel;
        private final String nodeName, type;

        public PrenatalCareViewHolder(@NonNull View itemView, String nodeName, String type) {
            super(itemView);
            binding = PatientsDateItemBinding.bind(itemView);
            this.nodeName = nodeName;
            this.type = type;
            itemView.setOnClickListener(this::dateActionListener);

        }

        private void dateActionListener(View view) {
            Intent intent = new Intent(view.getContext(), PrenatalCareAddForm.class);
            intent.putExtra("type_key", patientsDateModel.getDateKey());
            intent.putExtra("node_name", nodeName);
            intent.putExtra("type", type);
            //Forms/PrenatalCare/node_name/type/type_key
            view.getContext().startActivity(intent);
        }

        void bind(PatientsDateModel patientsDateModel) {
            this.patientsDateModel = patientsDateModel;
            binding.timePatientsDateItem.setText(patientsDateModel.getTime());
            binding.datePatientsDateItem.setText(patientsDateModel.getDate());
        }
    }

}
