package com.rey.itrustapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.rey.itrustapplication.databinding.ActivitySendEmailBinding;

import java.util.Objects;

public class SendEmailActivity extends AppCompatActivity {

    ActivitySendEmailBinding activitySendEmailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySendEmailBinding = ActivitySendEmailBinding.inflate(getLayoutInflater());
        setContentView(activitySendEmailBinding.getRoot());

        activitySendEmailBinding.backButtonSenEmail.setOnClickListener(view -> finish());

        activitySendEmailBinding.sendEmailButton.setOnClickListener(view -> validateUserInput());

        final String answer = getIntent().getStringExtra("message");
        final String question = getIntent().getStringExtra("question");

        final String message = "Hi admin, I have a problem which I can't understand the respond of starla, can you help me to understand it? \n"
                + "Here's my question: \"" + question + "\" \n\n" + "This is the respond I get: \"" + answer + "\"";

        Objects.requireNonNull(activitySendEmailBinding.subject.getEditText()).setText(getResources().getText(R.string.email_subject));
        Objects.requireNonNull(activitySendEmailBinding.message.getEditText()).setText(message);
    }

    private void validateUserInput() {
        final String usersEmail = Objects.requireNonNull(activitySendEmailBinding.receiverEmail.getEditText()).getText().toString().trim();
        final String subject = Objects.requireNonNull(activitySendEmailBinding.subject.getEditText()).getText().toString().trim();
        final String usersMessage = Objects.requireNonNull(activitySendEmailBinding.message.getEditText()).getText().toString().trim();
        
        if (usersEmail.isEmpty() || subject.isEmpty() || usersMessage.isEmpty()) {
            Toast.makeText(this, "Pakiusap, pakilamanan lahat ng field", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{usersEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, usersMessage);
        intent.setType("message/rfc822");

            startActivity(Intent.createChooser(intent, "Pumili kung saan gusto magsend"));

    }
}