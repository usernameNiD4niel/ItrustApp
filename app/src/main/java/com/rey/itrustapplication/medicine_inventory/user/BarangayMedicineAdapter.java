package com.rey.itrustapplication.medicine_inventory.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;

import java.util.ArrayList;

public class BarangayMedicineAdapter extends RecyclerView.Adapter<BarangayMedicineAdapter.ViewHolderClass> {

    private ArrayList<BarangayMedicineModel> barangayMedicineModels;
    private final Context context;

    public BarangayMedicineAdapter(ArrayList<BarangayMedicineModel> barangayMedicineModels, Context context) {
        this.barangayMedicineModels = barangayMedicineModels;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateTheView(ArrayList<BarangayMedicineModel> models) {
        this.barangayMedicineModels = models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.barangay_medicine_item, parent, false);
        return new ViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        holder.bind(barangayMedicineModels.get(position), context);

        holder.itemView.setOnClickListener(view -> {
            BarangayMedicineModel clickedModel = barangayMedicineModels.get(position);

            // Create the intent to start the next activity
            Intent intent = new Intent(context, BarangayMedicineItemActivity.class);
            intent.putExtra("medicine_name", clickedModel.getMedicineName());
            intent.putExtra("medicine_description", clickedModel.getMedicineDescription());
            intent.putExtra("medicine_stock", clickedModel.getStock());

            // Start the activity with animation
            // Get the shared element's view and transition name
            Pair<View, String> pair1 = Pair.create(holder.medicineName, ViewCompat.getTransitionName(holder.medicineName));
            Pair<View, String> pair2 = Pair.create(holder.medicineDescription, ViewCompat.getTransitionName(holder.medicineDescription));
            Pair<View, String> pair3 = Pair.create(holder.medicineStock, ViewCompat.getTransitionName(holder.medicineStock));

            @SuppressWarnings("unchecked")
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, pair1, pair2, pair3);

            context.startActivity(intent, optionsCompat.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return barangayMedicineModels.size();
    }

    public static class ViewHolderClass extends RecyclerView.ViewHolder {

        TextView medicineName, medicineDescription, medicineStock;
        View itemView;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            medicineName = itemView.findViewById(R.id.medicine_name_barangay_medicine_item);
            medicineDescription = itemView.findViewById(R.id.description_barangay_medicine_item);
            medicineStock = itemView.findViewById(R.id.stock_barangay_medicine_item);

        }

        public void bind(BarangayMedicineModel barangayMedicineModel, Context context) {
            medicineName.setText(barangayMedicineModel.getMedicineName());

            final String medicineDescriptionText = barangayMedicineModel.getMedicineDescription();

            if (medicineDescriptionText.trim().length() >= 35) {
                String concatenatedText = medicineDescriptionText.trim().substring(0, 35) + "...";
                medicineDescription.setText(concatenatedText);
            } else {
                medicineDescription.setText(barangayMedicineModel.getMedicineDescription());
            }
            medicineStock.setText(barangayMedicineModel.getStock());

            itemView.setOnClickListener(view -> {
                //Load new activity
                Toast.makeText(context, "Selected Item: " + medicineName.getText().toString(), Toast.LENGTH_SHORT).show();
            });
        }


    }

}
