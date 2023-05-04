package com.rey.itrustapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;
import com.rey.itrustapplication.helperclasses.AdminPendingHelperClass;

import java.util.ArrayList;

public class AdminApproveStatusAdapter extends RecyclerView.Adapter<AdminApproveStatusAdapter.ApproveStatusViewHolder> {

    private final  Context context;
    private final ArrayList<AdminPendingHelperClass> adminPendingHelperClassArrayList;

    public AdminApproveStatusAdapter(Context context, ArrayList<AdminPendingHelperClass> adminPendingHelperClassArrayList) {
        this.context = context;
        this.adminPendingHelperClassArrayList = adminPendingHelperClassArrayList;
    }

    @NonNull
    @Override
    public ApproveStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.approve_consult_item, parent, false);

        return new ApproveStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveStatusViewHolder holder, int position) {

        AdminPendingHelperClass approveHelperClass = adminPendingHelperClassArrayList.get(position);

        holder.bind(approveHelperClass);

    }

    @Override
    public int getItemCount() {
        return this.adminPendingHelperClassArrayList.size();
    }

    public static class ApproveStatusViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleOfConsultation, patientName;

        public ApproveStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleOfConsultation = itemView.findViewById(R.id.scheduleOfConsultation);
            patientName = itemView.findViewById(R.id.patientName);
        }

        void bind(AdminPendingHelperClass adminPendingHelperClass) {
            scheduleOfConsultation.setText(adminPendingHelperClass.getSchedule());
            patientName.setText(adminPendingHelperClass.getFullName());
        }
    }
}
