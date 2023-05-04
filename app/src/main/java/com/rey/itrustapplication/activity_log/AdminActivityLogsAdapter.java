package com.rey.itrustapplication.activity_log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;

import java.util.LinkedList;

public class AdminActivityLogsAdapter extends RecyclerView.Adapter<AdminActivityLogsAdapter.AdminActivityLogsHolder> {

    private final Context context;
    private final LinkedList<AdminLogsModel> adminLogsModels;

    public AdminActivityLogsAdapter(Context context, LinkedList<AdminLogsModel> adminLogsModels) {
        this.context = context;
        this.adminLogsModels = adminLogsModels;
    }

    @NonNull
    @Override
    public AdminActivityLogsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_logs_item, parent, false);
        return new AdminActivityLogsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminActivityLogsHolder holder, int position) {
        AdminLogsModel adminLogsModel = adminLogsModels.get(position);

        holder.activityLogTitle.setText(adminLogsModel.getLogsTitle());
        holder.activityLogDate.setText(adminLogsModel.getLogsDate());
        holder.activityLogTime.setText(adminLogsModel.getLogsTime());

    }

    @Override
    public int getItemCount() {
        return adminLogsModels.size();
    }

    static class AdminActivityLogsHolder extends RecyclerView.ViewHolder {

        TextView activityLogTitle, activityLogDate, activityLogTime;

        public AdminActivityLogsHolder(@NonNull View itemView) {
            super(itemView);
            activityLogTitle = itemView.findViewById(R.id.activityLogTitle);
            activityLogDate = itemView.findViewById(R.id.activityLogDate);
            activityLogTime = itemView.findViewById(R.id.activityLogTime);
        }
    }

}
