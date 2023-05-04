package com.rey.itrustapplication.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.helperclasses.ListOfUserHelperClass;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminListOfUserAdapter extends RecyclerView.Adapter<AdminListOfUserAdapter.AdminListOfUserHolder> {

    private final ArrayList<ListOfUserHelperClass> listOfUserHelperClasses;
    private final Context context;
    private final OnAdminClickListener onAdminClickListener;

    public AdminListOfUserAdapter(ArrayList<ListOfUserHelperClass> listOfUserHelperClasses, Context context,
                                  OnAdminClickListener onAdminClickListener) {
        this.listOfUserHelperClasses = listOfUserHelperClasses;
        this.context = context;
        this.onAdminClickListener = onAdminClickListener;
    }

    public interface OnAdminClickListener {
        void onUserClicked(int position);
    }

    @NonNull
    @Override
    public AdminListOfUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.admin_list_row, parent, false);

        return new AdminListOfUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminListOfUserHolder holder, int position) {
        ListOfUserHelperClass userHelperClass = listOfUserHelperClasses.get(position);
        String fullName = userHelperClass.getFullName();

        holder.fullNameUserList.setText(fullName);
        holder.purokUserList.setText(userHelperClass.getPurok());

        updateUserProfile(holder.usersProfile, fullName);

        if (!userHelperClass.getNumberOfChats().equals("online"))
            holder.numberOfChats.setVisibility(View.GONE);

        final String sender = userHelperClass.getSender();

        if ("Barangay Alawihao RHU".equals(sender)) {
            if (new SessionManager(context).getLogin()) {
                holder.adminListRowParent.setBackgroundColor(context.getResources().getColor(R.color.dark_color_100));
                return;
            }
            holder.adminListRowParent.setBackgroundColor(context.getResources().getColor(R.color.background__));
            Log.d("Daniel", "first if: " + sender);
            return;
        }
        if (new SessionManager(context).getFullName().equals(sender)){
            Log.d("Daniel", "second if sender: " + sender);
            holder.adminListRowParent.setBackgroundColor(context.getResources().getColor(R.color.background__));
            return;
        }

        holder.adminListRowParent.setBackgroundColor(context.getResources().getColor(R.color.dark_color_100));

    }

    private void updateUserProfile(CircleImageView usersProfile, String fullName) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String profile = snapshot.child(fullName).getValue(String.class);

                    if (profile == null) {
                        Drawable drawable = AppCompatResources.getDrawable(context, R.drawable.user_avatar_1);
                        usersProfile.setImageDrawable(drawable);
                        return;
                    }
                    Drawable drawable = AppCompatResources.getDrawable(context, Integer.parseInt(profile));
                    usersProfile.setImageDrawable(drawable);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Cannot retreive profile of the user", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listOfUserHelperClasses.size();
    }

    class AdminListOfUserHolder extends RecyclerView.ViewHolder {

        LinearLayout adminListRowParent;
        TextView fullNameUserList, purokUserList;
        ImageView numberOfChats;
        CircleImageView usersProfile;

        public AdminListOfUserHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(click -> onAdminClickListener.onUserClicked(getBindingAdapterPosition()));
            adminListRowParent = itemView.findViewById(R.id.adminListRowParent);
            fullNameUserList = itemView.findViewById(R.id.fullNameUserList);
            purokUserList = itemView.findViewById(R.id.purokUserList);
            numberOfChats = itemView.findViewById(R.id.numberOfChats);
            usersProfile = itemView.findViewById(R.id.usersProfile);
        }
    }

}
