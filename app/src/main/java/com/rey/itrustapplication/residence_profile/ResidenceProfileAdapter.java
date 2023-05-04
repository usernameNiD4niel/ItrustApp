package com.rey.itrustapplication.residence_profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;

import java.util.LinkedList;
import java.util.List;

public class ResidenceProfileAdapter extends RecyclerView.Adapter<ResidenceProfileAdapter.ResidenceProfileHolder>{

    private final Context context;
    private LinkedList<ResidenceProfileModel> residenceProfileModels;

    public ResidenceProfileAdapter(Context context, LinkedList<ResidenceProfileModel> residenceProfileModels) {
        this.context = context;
        this.residenceProfileModels = residenceProfileModels;
    }

    @NonNull
    @Override
    public ResidenceProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.residence_profile_item, parent, false);
        return new ResidenceProfileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResidenceProfileHolder holder, int position) {
        ResidenceProfileModel model = residenceProfileModels.get(position);

        holder.nameTextResidenceItem.setText(model.getName());
        holder.phoneNumberTextResidenceItem.setText(model.getPhoneNumber());
        holder.birthdayTextResidenceItem.setText(model.getBirthday());
        holder.purokTextResidenceItem.setText(model.getPurok());

    }

    @Override
    public int getItemCount() {
        return residenceProfileModels.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(LinkedList<ResidenceProfileModel> filteredList) {

        this.residenceProfileModels = filteredList;
        notifyDataSetChanged();
    }

    static class ResidenceProfileHolder extends RecyclerView.ViewHolder {

        TextView nameTextResidenceItem, phoneNumberTextResidenceItem, birthdayTextResidenceItem, purokTextResidenceItem;

        public ResidenceProfileHolder(@NonNull View itemView) {
            super(itemView);
            nameTextResidenceItem = itemView.findViewById(R.id.nameTextResidenceItem);
            phoneNumberTextResidenceItem = itemView.findViewById(R.id.phoneNumberTextResidenceItem);
            birthdayTextResidenceItem = itemView.findViewById(R.id.birthdayTextResidenceItem);
            purokTextResidenceItem = itemView.findViewById(R.id.purokTextResidenceItem);
        }
    }

}
