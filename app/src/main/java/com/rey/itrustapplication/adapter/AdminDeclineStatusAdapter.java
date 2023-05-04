package com.rey.itrustapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;
import com.rey.itrustapplication.helperclasses.AdminPendingHelperClass;

import java.util.ArrayList;

public class AdminDeclineStatusAdapter extends RecyclerView.Adapter<AdminDeclineStatusAdapter.AdminDeclineViewHolder>{

    private final Context context;
    private final ArrayList<AdminPendingHelperClass> adminDeclineArrayList;

    public AdminDeclineStatusAdapter(Context context, ArrayList<AdminPendingHelperClass> adminDeclineArrayList) {
        this.context = context;
        this.adminDeclineArrayList = adminDeclineArrayList;
    }

    @NonNull
    @Override
    public AdminDeclineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.decline_consult_item, parent, false);
        return new AdminDeclineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminDeclineViewHolder holder, int position) {
        AdminPendingHelperClass declineHelperClass = adminDeclineArrayList.get(position);

        holder.bind(declineHelperClass);

    }

    @Override
    public int getItemCount() {
        return adminDeclineArrayList.size();
    }

    public static class AdminDeclineViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleOfConsultation, patientName;

        public AdminDeclineViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleOfConsultation = itemView.findViewById(R.id.scheduleOfConsultation);
            patientName = itemView.findViewById(R.id.patientName);
        }

        void bind(AdminPendingHelperClass declineHelperClass) {
            scheduleOfConsultation.setText(declineHelperClass.getSchedule());
            patientName.setText(declineHelperClass.getFullName());

        }
    }

}
