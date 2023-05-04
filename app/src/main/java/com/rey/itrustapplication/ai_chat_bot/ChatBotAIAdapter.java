package com.rey.itrustapplication.ai_chat_bot;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.rey.itrustapplication.UserProfileActivity;
import com.rey.itrustapplication.sessionmanager.SessionManager;
import com.rey.itrustapplication.userchatbot.ChatBotStarlaProfile;

import java.util.HashMap;
import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatBotAIAdapter extends RecyclerView.Adapter<ChatBotAIAdapter.ChatBotViewHolder>{

    private final Context context;
    private final LinkedList<ChatBotModelClass> chatBotModelClasses;

    public ChatBotAIAdapter(Context context, LinkedList<ChatBotModelClass> chatBotModelClasses) {
        this.context = context;
        this.chatBotModelClasses = chatBotModelClasses;
    }

    @NonNull
    @Override
    public ChatBotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return new ChatBotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBotViewHolder holder, int position) {
        ChatBotModelClass botModelClass = chatBotModelClasses.get(position);
        holder.messageChat.setText(botModelClass.getMessageChat());

        if (!botModelClass.getIsUser()) {
            holder.iconChatBotAi.setOnClickListener(view -> context.startActivity(new Intent(context, ChatBotStarlaProfile.class)));
            holder.messageChat.setOnLongClickListener(view -> {
                final String botMessage = botModelClass.getMessageChat();

                if (botMessage.equals("Hello, ako si starla paano ako sayo makakatulong?")) return false;

                if (botMessage.equals("I am very sorry but your asking is beyond of my knowledge." +
                        " \nIf you want, you can chat the Midwife by typing MIDWIFE \nIf you want to chat with me that I can answer here's a few tips" +
                        "\n- Be specific to your question." +
                        "\n- As much as possible when you're asking add a question (?) at the end." +
                        "\n- Make it English or Tagalog only" +
                        "\n- If I still can't answer you're query, make you're question to English if possible"))
                    return false;

                final String userMessage = chatBotModelClasses.get(position-1).getMessageChat();

                savedDataToDatabase(userMessage, botMessage, holder.messageChat);
                return true;
            });
        } else {
            updateUserProfile(holder.iconChatBotAi);
            holder.iconChatBotAi.setOnClickListener(view -> context.startActivity(new Intent(context, UserProfileActivity.class)));
        }
    }

    private void updateUserProfile(CircleImageView iconChatBotAi) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String drawableHash = snapshot.child(new SessionManager(context).getFullName()).getValue(String.class);
                    if (drawableHash == null) {
                        return;
                    }
                    Drawable drawable = AppCompatResources.getDrawable(context, Integer.parseInt(drawableHash));
                    iconChatBotAi.setImageDrawable(drawable);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void savedDataToDatabase(String userMessage, String botMessage, TextView messageTV) {
        //Save data to database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/")
                .getReference("RegularUsers");

        final String currentUser = new SessionManager(context).getFullName();

        HashMap<String, Object> userDataSaved = new HashMap<>();
        userDataSaved.put("answer", botMessage);
        userDataSaved.put("question", userMessage);

        databaseReference.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    databaseReference.child(currentUser + "/saved_answer/" + userMessage).setValue(userDataSaved).addOnCompleteListener(aVoid -> {
                        ColorStateList stateList = ColorStateList.valueOf(context.getResources().getColor(R.color.ngilo));
                        messageTV.setBackgroundTintList(stateList);
                        messageTV.setTextColor(context.getResources().getColor(R.color.black));
                    }).addOnFailureListener(failed -> Toast.makeText(context, "Mabagal ang iyong internet: " + failed, Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Nacancel ang pagsave ng data...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatBotModelClasses.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatBotModelClass message = chatBotModelClasses.get(position);
        if (message.getIsUser()) {
            return R.layout.chat_bot_ai_user;
        } else {
            return R.layout.chat_bot_ai_bot;
        }
    }

    public static class ChatBotViewHolder extends RecyclerView.ViewHolder {
        TextView messageChat;
        CircleImageView iconChatBotAi;

        public ChatBotViewHolder(@NonNull View itemView) {
            super(itemView);
            messageChat = itemView.findViewById(R.id.chat_bot_ai_message);
            iconChatBotAi = itemView.findViewById(R.id.icon_chat_bot_ai);
        }
    }

}
