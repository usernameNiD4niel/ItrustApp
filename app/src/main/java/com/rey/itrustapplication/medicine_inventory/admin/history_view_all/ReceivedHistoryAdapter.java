package com.rey.itrustapplication.medicine_inventory.admin.history_view_all;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;

import java.util.ArrayList;

public class ReceivedHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<HistoryViewModel> historyViewModels;

    public ReceivedHistoryAdapter(ArrayList<HistoryViewModel> historyViewModels) {
        this.historyViewModels = historyViewModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_history_view_all_header, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_receive_history_view_all_item, parent, false);
        }
        return new ViewHolderItem(view);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            return;
        }

        ViewHolderItem viewHolderItem = (ViewHolderItem) holder;

        viewHolderItem.bind(historyViewModels.get(position-1));

    }

    @Override
    public int getItemCount() {
        return historyViewModels.size()+1;
    }

    static class ViewHolderItem extends RecyclerView.ViewHolder {

        TextView patientName, medicineName, dateReceived, quantity, reason;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patient_name_receive_inventory_medicine_item);
            medicineName = itemView.findViewById(R.id.medicine_name_text_receive_history);
            dateReceived = itemView.findViewById(R.id.date_received_history_view);
            quantity = itemView.findViewById(R.id.quantity_text_receive_history);
            reason = itemView.findViewById(R.id.reason_text_receive_history);

        }

        void bind(HistoryViewModel historyViewModel) {
            String medicineNameText = historyViewModel.getMedicineName();
            String quantityNameText = historyViewModel.getQuantity();

            patientName.setText(historyViewModel.getPatientName());
            medicineName.setText(medicineNameText.endsWith(",") ? medicineNameText.substring(0, medicineNameText.length()-1) : medicineNameText);
            dateReceived.setText(historyViewModel.getDateReceive());
            quantity.setText(quantityNameText.endsWith(",") ? quantityNameText.substring(0, quantityNameText.length()-1) : quantityNameText);
            reason.setText(historyViewModel.getReason());
        }
    }

}
