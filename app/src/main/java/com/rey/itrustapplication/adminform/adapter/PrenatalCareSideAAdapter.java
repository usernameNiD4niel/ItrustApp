package com.rey.itrustapplication.adminform.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adminform.activities.ViewMoreSideAPrenatalCare;
import com.rey.itrustapplication.adminform.model.PrenatalCareSideAModel;
import com.rey.itrustapplication.adminform.popup.AddNewRecordPrompt;
import com.rey.itrustapplication.databinding.SideAItemPrenatalCareBinding;

import java.util.List;

public class PrenatalCareSideAAdapter extends RecyclerView.Adapter<PrenatalCareSideAAdapter.SideAViewHolder> {

    private final List<PrenatalCareSideAModel> prenatalCareSideAModels;

    public PrenatalCareSideAAdapter(List<PrenatalCareSideAModel> prenatalCareSideAModels) {
        this.prenatalCareSideAModels = prenatalCareSideAModels;
    }

    @NonNull
    @Override
    public SideAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_a_item_prenatal_care, parent, false);
        return new SideAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SideAViewHolder holder, int position) {
        holder.bind(prenatalCareSideAModels.get(position));
    }

    @Override
    public int getItemCount() {
        return prenatalCareSideAModels.size();
    }

    static class SideAViewHolder extends RecyclerView.ViewHolder {

        SideAItemPrenatalCareBinding binding;
        private PrenatalCareSideAModel prenatalCareSideAModel;

        public SideAViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SideAItemPrenatalCareBinding.bind(itemView);
            itemView.setOnClickListener(this::createAPopUp);
        }

        private void createAPopUp(View view) {
            Intent intent = new Intent(view.getContext(), ViewMoreSideAPrenatalCare.class);
            intent.putExtra("node_name", prenatalCareSideAModel.getNodeKey());
            intent.putExtra("client_name", prenatalCareSideAModel.getPatientName());
            AddNewRecordPrompt.showPopUp(
                    view.getContext(),
                    "Choose what data do you want to view or update the Prenatal Care Form",
                    "Side A",
                    "Side B",
                    (dialogInterface, i) -> {
                        intent.putExtra("type", "SideA");
                        view.getContext().startActivity(intent);
                    },
                    (dialogInterface, i) -> {
                        intent.putExtra("type", "SideB");
                        view.getContext().startActivity(intent);
                    });
        }

        void bind(PrenatalCareSideAModel prenatalCareSideAModel) {
            this.prenatalCareSideAModel = prenatalCareSideAModel;
            binding.dateAddedTextPrenatalCareItem.setText(prenatalCareSideAModel.getCurrentDateAdded());
            binding.clientNameTextPrenatalCareItem.setText(prenatalCareSideAModel.getPatientName());
            binding.timeAddedTextPrenatalCareItem.setText(prenatalCareSideAModel.getCurrentTimeAdded());
        }
    }

}
