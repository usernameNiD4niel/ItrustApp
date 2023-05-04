package com.rey.itrustapplication.adapter;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.helperclasses.AdminPendingHelperClass;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdminPendingStatusAdapter extends RecyclerView.Adapter<AdminPendingStatusAdapter.PendingItemViewHolder> {

    private final ArrayList<AdminPendingHelperClass> adminPendingHelperClasses;
    private final Context context;

    public AdminPendingStatusAdapter(Context context, ArrayList<AdminPendingHelperClass> adminPendingHelperClasses) {
        this.context = context;
        this.adminPendingHelperClasses = adminPendingHelperClasses;
    }

    @NonNull
    @Override
    public PendingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_consult_item, parent, false);
        return new PendingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingItemViewHolder holder, int position) {
        AdminPendingHelperClass pendingHelperClass = adminPendingHelperClasses.get(position);

        holder.bind(pendingHelperClass, this, adminPendingHelperClasses);

    }

    @Override
    public int getItemCount() {
        return this.adminPendingHelperClasses.size();
    }

    public static class PendingItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView scheduleOfConsultation, patientName;
        ImageView approvePatient, declinePatient;
        ProgressBar pendingConsultItemProgressBar;
        private AdminPendingHelperClass adminPendingHelperClass;
        private Context context;
        AdminPendingStatusAdapter adminPendingStatusAdapter;
        ArrayList<AdminPendingHelperClass> adminPendingHelperClasses;

        public PendingItemViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleOfConsultation = itemView.findViewById(R.id.scheduleOfConsultation);
            patientName = itemView.findViewById(R.id.patientName);
            approvePatient = itemView.findViewById(R.id.approvePatient);
            declinePatient = itemView.findViewById(R.id.declinePatient);
            pendingConsultItemProgressBar = itemView.findViewById(R.id.pendingConsultItemProgressBar);

            approvePatient.setOnClickListener(this);
            declinePatient.setOnClickListener(this);

        }

        void bind(AdminPendingHelperClass adminPendingHelperClass, AdminPendingStatusAdapter adminPendingStatusAdapter, ArrayList<AdminPendingHelperClass> adminPendingHelperClasses) {
            this.adminPendingHelperClass = adminPendingHelperClass;
            this.adminPendingStatusAdapter = adminPendingStatusAdapter;
            this.adminPendingHelperClasses = adminPendingHelperClasses;
            scheduleOfConsultation.setText(adminPendingHelperClass.getSchedule());
            patientName.setText(adminPendingHelperClass.getFullName());

        }

        @Override
        public void onClick(View view) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            this.context = view.getContext();

            AdminPendingHelperClass helperClass = new AdminPendingHelperClass(adminPendingHelperClass.getFullName(),adminPendingHelperClass.getDate_requested(),adminPendingHelperClass.getSchedule());
            startProgressBar(true);

            if (view.getId() == approvePatient.getId()) {

                databaseReference.child("ApproveRequest").push().setValue(helperClass).addOnSuccessListener(success ->
                        databaseReference.child("PendingRequest").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        String key = dataSnapshot.getKey();
                                        if (adminPendingHelperClass.getFullName().equals(dataSnapshot.child("fullName").getValue(String.class))) {
                                            databaseReference.child("PendingRequest/" + key).removeValue().addOnSuccessListener(
                                                    success2 -> updateUsersStatus(adminPendingHelperClass.getFullName(), "Approved Request", databaseReference)
                                            );
                                            return;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }));
//                        databaseReference.child("PendingRequest/" + key).removeValue().addOnSuccessListener(success2 ->
//                                updateUsersStatus(adminPendingHelperClass.getFullName(), "Approved Request", databaseReference)));

            } else if (view.getId() == declinePatient.getId()) {

                databaseReference.child("DeclineRequest").push().setValue(helperClass).addOnSuccessListener(success ->
                        databaseReference.child("PendingRequest").push().removeValue().addOnSuccessListener(success2 ->
                                updateUsersStatus(adminPendingHelperClass.getFullName(), "Declined Request", databaseReference)));

            }
        }

        private void startProgressBar(boolean isLoading) {
            if (isLoading) {
                pendingConsultItemProgressBar.setVisibility(View.VISIBLE);
                approvePatient.setVisibility(View.GONE);
                declinePatient.setVisibility(View.GONE);
            } else {
                pendingConsultItemProgressBar.setVisibility(View.GONE);
                approvePatient.setVisibility(View.VISIBLE);
                declinePatient.setVisibility(View.VISIBLE);
            }
        }

        private void updateUsersStatus(String patientName, String status, DatabaseReference databaseReference) {

            HashMap<String, Object> updatedStatus = new HashMap<>();
            updatedStatus.put("status", status);

            final String statusRequest = (status.equals("Approved Request")) ? "approve" : "declined";

            databaseReference.child("RegularUsers/" + patientName).updateChildren(updatedStatus).addOnSuccessListener(success ->
                pushToActivityLog(statusRequest, patientName));

        }

        private void pushToActivityLog(String type, String patientName) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("ActivityLogs");

            final String currentUserAdmin = new AdminSessionManager(context).getUsernameSession();

            if (type.equalsIgnoreCase("approve")) type = currentUserAdmin + " approved " + patientName + " for consultation check up";
            else type = currentUserAdmin + " declined " + patientName + " for consultation check up";

            Calendar calendar = Calendar.getInstance();
            final String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

            Date dateDate = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh-mm-ss", Locale.getDefault());
            final String time = simpleDateFormat.format(dateDate);

            Map<String, Object> adminDataLog = new HashMap<>();
            adminDataLog.put("title", type);
            adminDataLog.put("date", date);
            adminDataLog.put("time", time);

            databaseReference.push().setValue(adminDataLog).addOnSuccessListener(success -> {

                startProgressBar(false);

                adminPendingHelperClasses.remove(getBindingAdapterPosition());
                adminPendingStatusAdapter.notifyItemRemoved(getBindingAdapterPosition());
            });

        }

    }

}
