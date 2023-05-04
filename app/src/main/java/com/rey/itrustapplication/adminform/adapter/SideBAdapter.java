package com.rey.itrustapplication.adminform.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adminform.activities.SideBAddFormFP;
import com.rey.itrustapplication.adminform.model.SideBModel;
import com.rey.itrustapplication.databinding.SideBItemBinding;

import java.util.ArrayList;

public class SideBAdapter extends RecyclerView.Adapter<SideBAdapter.ViewHolderSideB> {

    private final ArrayList<SideBModel> sideBModelArrayList;

    private Context context;

    public SideBAdapter(ArrayList<SideBModel> sideBModelArrayList) {
        this.sideBModelArrayList = sideBModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolderSideB onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        return new ViewHolderSideB(LayoutInflater.from(context).inflate(R.layout.side_b_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSideB holder, int position) {
        holder.bind(sideBModelArrayList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return sideBModelArrayList.size();
    }


    static class ViewHolderSideB extends RecyclerView.ViewHolder implements View.OnClickListener {

        SideBItemBinding binding;
        private SideBModel sideBModel;
        private Context context;

        public ViewHolderSideB(@NonNull View itemView) {
            super(itemView);
            binding = SideBItemBinding.bind(itemView);
            itemView.setOnClickListener(this);
        }

        void bind(SideBModel sideBModel, Context context) {
            this.sideBModel = sideBModel;
            this.context = context;
            binding.patientNameTextSideBItem.setText(sideBModel.getPatientName());
            binding.dateOfVisitTextSideBItem.setText(sideBModel.getDateOfVisit());
            binding.followupDateVisitTextSideBItem.setText(sideBModel.getFollowUpDateVisit());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, SideBAddFormFP.class);
            intent.putExtra("patient_name", sideBModel.getPatientName());
            intent.putExtra("isPreviewing", true);

            context.startActivity(intent);
        }
    }

}
