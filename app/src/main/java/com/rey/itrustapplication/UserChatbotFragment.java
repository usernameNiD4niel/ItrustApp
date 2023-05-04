package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOnline;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rey.itrustapplication.ai_chat_bot.ChatBotAIActivity;
import com.rey.itrustapplication.sessionmanager.SessionManager;

public class UserChatbotFragment extends Fragment {

    Button talkToKatiwalaBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View newView = inflater.inflate(R.layout.fragment_user_chatbot, container, false);

        talkToKatiwalaBtn = newView.findViewById(R.id.talkToKatiwalaBtn);

        talkToKatiwalaBtn.setOnClickListener(view -> startActivity(new Intent(getActivity(), ChatBotAIActivity.class)));

        return newView;

    }

    @Override
    public void onResume() {
        setAvailabilityToOnline(new SessionManager(requireActivity()).getFullName(), "RegularUsers");
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}