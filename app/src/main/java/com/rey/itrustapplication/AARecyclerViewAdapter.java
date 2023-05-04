package com.rey.itrustapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AARecyclerViewAdapter extends RecyclerView.Adapter<AARecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<AdminAdminsModel> adminAdminsModels;

    public AARecyclerViewAdapter(Context context, ArrayList<AdminAdminsModel> adminAdminsModels) {
        this.context = context;
        this.adminAdminsModels = adminAdminsModels;
    }

    @NonNull
    @Override
    public AARecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.admins_recycler_row, parent, false);

        return new AARecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AARecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.adminNameFull.setText(adminAdminsModels.get(position).getName());
        holder.adminUsername.setText(adminAdminsModels.get(position).getUsername());
        holder.adminCreatedAccount.setText(adminAdminsModels.get(position).getAccountCreated());
        holder.adminImageView.setImageResource(adminAdminsModels.get(position).getProfile());
    }

    @Override
    public int getItemCount() {
        return adminAdminsModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView adminImageView;
        TextView adminNameFull, adminUsername, adminCreatedAccount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            adminImageView = itemView.findViewById(R.id.adminImageView);
            adminNameFull = itemView.findViewById(R.id.admin_name_full);
            adminUsername = itemView.findViewById(R.id.admin_username);
            adminCreatedAccount = itemView.findViewById(R.id.admin_created_account);
        }
    }
}
