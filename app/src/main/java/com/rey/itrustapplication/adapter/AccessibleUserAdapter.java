package com.rey.itrustapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.helperclasses.AccessibleUserHelperClass;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.util.List;

public class AccessibleUserAdapter extends RecyclerView.Adapter<AccessibleUserAdapter.AccessibleUserViewHolder>{

    private final Context context;
    private List<AccessibleUserHelperClass> accessibleUserHelperClassList;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();

    public AccessibleUserAdapter(Context context, List<AccessibleUserHelperClass> accessibleUserHelperClassList) {
        this.context = context;
        this.accessibleUserHelperClassList = accessibleUserHelperClassList;
    }

    public void setFilteredList(List<AccessibleUserHelperClass> filteredList) {
        this.accessibleUserHelperClassList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccessibleUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accessible_user_item, parent, false);

        return new AccessibleUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccessibleUserViewHolder holder, int position) {

        AccessibleUserHelperClass accessibleUserHelperClass = accessibleUserHelperClassList.get(position);

        holder.privilegeUser.setText(accessibleUserHelperClass.getFullName());
        holder.dateAdded.setText(accessibleUserHelperClass.getDateAdded());
        holder.deleteUser.setOnClickListener(view -> deleteUser(holder.getAdapterPosition(), accessibleUserHelperClass.getFullName()));

    }

    private void deleteUser(int adapterPosition, String fullName) {

        deleteRegisteredUser(fullName);

        Query query = databaseReference.child("AccessibleUsers").orderByChild("fullName").equalTo(fullName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        dataSnapshot.getRef().removeValue();
                    accessibleUserHelperClassList.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    Toast.makeText(context, "You have successfully removed " + fullName, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void deleteRegisteredUser(String fullName) {
        Toast.makeText(context, "First Name: " + fullName, Toast.LENGTH_SHORT).show();
        Query query = databaseReference.child("RegularUsers").orderByChild("fullName").equalTo(fullName);

        SessionManager sessionManager = new SessionManager(context.getApplicationContext());
        sessionManager.setFullName("");
        sessionManager.setLogin(false);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        dataSnapshot.getRef().removeValue();

                    Toast.makeText(context, "The account of the user is successfully deleted also", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return accessibleUserHelperClassList.size();
    }

    public static class AccessibleUserViewHolder extends RecyclerView.ViewHolder {

        TextView privilegeUser, dateAdded, deleteUser;

        public AccessibleUserViewHolder(@NonNull View itemView) {
            super(itemView);
            privilegeUser = itemView.findViewById(R.id.privilegeUser);
            dateAdded = itemView.findViewById(R.id.dateAdded);
            deleteUser = itemView.findViewById(R.id.deleteUser);
        }
    }

}
