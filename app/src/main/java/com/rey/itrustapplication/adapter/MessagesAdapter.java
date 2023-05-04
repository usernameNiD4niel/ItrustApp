package com.rey.itrustapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.itrustapplication.R;
import com.rey.itrustapplication.helperclasses.MessagesModel;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private final Context context;
    private final ArrayList<MessagesModel> arrayList;

    private final String selectedProfile;

    public MessagesAdapter(Context context, ArrayList<MessagesModel> arrayList, String selectedProfile) {
        this.context = context;
        this.arrayList = arrayList;
        this.selectedProfile = selectedProfile;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.messages_row_layout, parent, false);
            return new MessagesUserHolder(view);

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.message_admin_item, parent, false);
            return new MessageAdminHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        SessionManager sessionManager = new SessionManager(context);

        if (sessionManager.getLogin()) {
            if (arrayList.get(position).getSender().equals(sessionManager.getFullName())) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (arrayList.get(position).getSender().equals("Barangay Alawihao RHU")) {
                return 0;
            } else {
                return 1;
            }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessagesModel messagesModel = arrayList.get(position);
        SessionManager sessionManager = new SessionManager(context);

        if (sessionManager.getLogin()) {

            if (messagesModel.getSender().equals(sessionManager.getFullName())) {
                MessagesUserHolder messagesUserHolder = new MessagesUserHolder(holder.itemView);
                messagesUserHolder.chatMessageUser.setText(messagesModel.getContent());
                messagesUserHolder.profileImageChatUser.setImageDrawable(AppCompatResources.getDrawable(context, Integer.parseInt(selectedProfile)));
            } else {
                MessageAdminHolder messageAdminHolder = new MessageAdminHolder(holder.itemView);
                messageAdminHolder.chatMessageAdmin.setText(messagesModel.getContent());
                messageAdminHolder.profileImageChatAdmin.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.darna_final));
            }
        } else {

            if (messagesModel.getSender().equals("Barangay Alawihao RHU")) {
                MessagesUserHolder messagesUserHolder = new MessagesUserHolder(holder.itemView);
                messagesUserHolder.chatMessageUser.setText(messagesModel.getContent());
                messagesUserHolder.profileImageChatUser.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.darna_final));
            } else {
                MessageAdminHolder messageAdminHolder = new MessageAdminHolder(holder.itemView);
                messageAdminHolder.chatMessageAdmin.setText(messagesModel.getContent());
                messageAdminHolder.profileImageChatAdmin.setImageDrawable(AppCompatResources.getDrawable(context, Integer.parseInt(selectedProfile)));
            }
        }

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class MessagesUserHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImageChatUser;
        TextView chatMessageUser;
        View itemView;

        public MessagesUserHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            profileImageChatUser = itemView.findViewById(R.id.smallProfileImage);
            chatMessageUser = itemView.findViewById(R.id.textMessageChat);
        }
    }

    static class MessageAdminHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImageChatAdmin;
        TextView chatMessageAdmin;
        View itemView;

        public MessageAdminHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            profileImageChatAdmin = itemView.findViewById(R.id.smallProfileImageAdmin);
            chatMessageAdmin = itemView.findViewById(R.id.textMessageChatAdmin);
        }
    }

}
