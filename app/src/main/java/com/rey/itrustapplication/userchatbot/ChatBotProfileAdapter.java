package com.rey.itrustapplication.userchatbot;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.rey.itrustapplication.R;

import java.util.LinkedList;

public class ChatBotProfileAdapter extends RecyclerView.Adapter<ChatBotProfileAdapter.ChatBotProfileViewHolder>{

    private final Context context;
    private final LinkedList<ChatBotStarlaProfileModel> chatBotStarlaProfileModelLinkedList;

    public ChatBotProfileAdapter(Context context, LinkedList<ChatBotStarlaProfileModel> chatBotStarlaProfileModelLinkedList) {
        this.context = context;
        this.chatBotStarlaProfileModelLinkedList = chatBotStarlaProfileModelLinkedList;
    }

    @NonNull
    @Override
    public ChatBotProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_bot_profile_item, parent, false);
        return new ChatBotProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBotProfileViewHolder holder, int position) {
        ChatBotStarlaProfileModel model = chatBotStarlaProfileModelLinkedList.get(position);
        holder.questionChatBotStarlaProfile.setText(model.getQuestion());
        holder.answerChatBotStarlaItem.setText(model.getAnswer());
    }

    @Override
    public int getItemCount() {
        return chatBotStarlaProfileModelLinkedList.size();
    }

    static class ChatBotProfileViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        ConstraintLayout chatBotProfileItem;
        TextView answerChatBotStarlaItem, questionChatBotStarlaProfile;

        public ChatBotProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layoutChatBotItem);
            chatBotProfileItem = itemView.findViewById(R.id.chatBotProfileItem);
            answerChatBotStarlaItem = itemView.findViewById(R.id.answerChatBotStarlaItem);
            questionChatBotStarlaProfile = itemView.findViewById(R.id.questionChatBotStarlaProfile);

            layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

            chatBotProfileItem.setOnClickListener(view -> {
                int visibility = (answerChatBotStarlaItem.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;

                TransitionManager.beginDelayedTransition(layout, new AutoTransition());

                answerChatBotStarlaItem.setVisibility(visibility);
            });
        }
    }

}
