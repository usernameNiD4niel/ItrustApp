package com.rey.itrustapplication.medicine_inventory.admin.medicine_view_all;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;

import java.util.ArrayList;

public class MedicineHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final ArrayList<MedicineHistoryModel> medicineHistoryModels;

    public MedicineHistoryAdapter(ArrayList<MedicineHistoryModel> medicineHistoryModels) {
        this.medicineHistoryModels = medicineHistoryModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolderHeaderMedicineHistory(LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_history_view_all_header, parent, false));
        }
        return new ViewHolderItemMedicineHistory(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_history_item, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((ViewHolderHeaderMedicineHistory) holder).bind();
            return;
        }

        ViewHolderItemMedicineHistory viewHolderItemMedicineHistory = (ViewHolderItemMedicineHistory) holder;
        viewHolderItemMedicineHistory.bind(medicineHistoryModels.get(position-1));

    }

    @Override
    public int getItemCount() {
        return medicineHistoryModels.size()+1;
    }


    static class ViewHolderItemMedicineHistory extends RecyclerView.ViewHolder {

        TextView medicineName, medicineStock, description, howToUse, whenToUse;

        public ViewHolderItemMedicineHistory(@NonNull View itemView) {
            super(itemView);

            medicineName = itemView.findViewById(R.id.medicine_name_text_medicine_history);
            medicineStock = itemView.findViewById(R.id.stock_text_medicine_history);
            description = itemView.findViewById(R.id.description_text_medicine_history);
            howToUse = itemView.findViewById(R.id.how_to_use_text_medicine_history);
            whenToUse = itemView.findViewById(R.id.when_to_use_text_medicine_history);

        }

        void bind(MedicineHistoryModel medicineHistoryModel) {
            medicineName.setText(medicineHistoryModel.getMedicineName());
            medicineStock.setText(medicineHistoryModel.getMedicineStock());
            description.setText(medicineHistoryModel.getDescription());
            howToUse.setText(medicineHistoryModel.getHowToUse());
            whenToUse.setText(medicineHistoryModel.getWhenToUse());
        }

    }

    static class ViewHolderHeaderMedicineHistory extends RecyclerView.ViewHolder {

        TextView titleHeader, contentHeader;

        public ViewHolderHeaderMedicineHistory(@NonNull View itemView) {
            super(itemView);
            titleHeader = itemView.findViewById(R.id.received_history_header_text);
            contentHeader = itemView.findViewById(R.id.received_history_header_content);
        }

        void bind() {
            final String titleHeaderText = "MEDICINE INFORMATION";
            final String contentHeaderText = "Information Medicine gives you full access to all the medicine added in this App.";
            if (!titleHeader.getText().toString().equals(titleHeaderText))
                titleHeader.setText(titleHeaderText);

            if (!contentHeader.getText().toString().equals(contentHeaderText))
                contentHeader.setText(contentHeaderText);
        }
    }

}
