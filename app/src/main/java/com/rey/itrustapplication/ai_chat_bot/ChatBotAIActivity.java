package com.rey.itrustapplication.ai_chat_bot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rey.itrustapplication.chatfeature.activities.AdminListOfUsers;
import com.rey.itrustapplication.databinding.ActivityChatBotAiactivityBinding;
import com.rey.itrustapplication.sessionmanager.SessionManager;
import com.rey.itrustapplication.userchatbot.ChatBotStarlaProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatBotAIActivity extends AppCompatActivity {

    ActivityChatBotAiactivityBinding binding;
    final LinkedList<ChatBotModelClass> chatBotModelClasses = new LinkedList<>();

    ChatBotAIAdapter chatBotAIAdapter;

    private Handler handler;
    private Runnable runnable;

    private SpeechRecognizer speechRecognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBotAiactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.etChatBotAi.requestFocus();


        chatBotAIAdapter = new ChatBotAIAdapter(this, chatBotModelClasses);
        binding.chatBotAiRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.chatBotAiRecyclerview.setAdapter(chatBotAIAdapter);
        populateRecyclerView();

        binding.chatBotAiBtn.setOnClickListener(view -> validateUserInput());

        binding.chatBotAiStarla.setOnClickListener(view -> startActivity(new Intent(ChatBotAIActivity.this, ChatBotStarlaProfile.class)));

        binding.chatBotAiBackButton.setOnClickListener(view -> finish());

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        setUpRecognizer();

        binding.micChatBotAi.setOnClickListener(view -> {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                return;
            }

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
            speechRecognizer.startListening(intent);
        });

        binding.etChatBotAi.setOnFocusChangeListener((view, b) -> {
            if (b) {
                binding.chatBotAiRecyclerview.smoothScrollToPosition(chatBotModelClasses.size()-1);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            // Check if the permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                // Start the speech recognition process
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
                speechRecognizer.startListening(intent);
            } else {
                // Permission was denied
                // Provide feedback to the user
                Toast.makeText(this, "You should allow to access the audio...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpRecognizer() {
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Toast.makeText(ChatBotAIActivity.this, "Please start speaking", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeginningOfSpeech() {
                
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                switch (i) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        // handle audio error
                        Toast.makeText(ChatBotAIActivity.this, "Audio Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        // handle client error
                        Toast.makeText(ChatBotAIActivity.this, "Client Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        // handle insufficient permissions error
                        Toast.makeText(ChatBotAIActivity.this, "Insufficient Permission Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        // handle network error
                        Toast.makeText(ChatBotAIActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        // handle network timeout error
                        Toast.makeText(ChatBotAIActivity.this, "Network Timeout Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        // handle no match error
                        Toast.makeText(ChatBotAIActivity.this, "NO Match Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        // handle recognizer busy error
                        Toast.makeText(ChatBotAIActivity.this, "Recognizer Busy Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        // handle server error
                        Toast.makeText(ChatBotAIActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        // handle speech timeout error
                        Toast.makeText(ChatBotAIActivity.this, "Speech Timeout Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED:
                        // handle speech timeout error
                        Toast.makeText(ChatBotAIActivity.this, "Language Not Supported Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE:
                        // handle speech timeout error
                        Toast.makeText(ChatBotAIActivity.this, "Language Unavailable Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_SERVER_DISCONNECTED:
                        // handle speech timeout error
                        Toast.makeText(ChatBotAIActivity.this, "Server Disconnected Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_TOO_MANY_REQUESTS:
                        // handle speech timeout error
                        Toast.makeText(ChatBotAIActivity.this, "Too Many Request Error", Toast.LENGTH_SHORT).show();
                        break;
                    case SpeechRecognizer.ERROR_CANNOT_CHECK_SUPPORT:
                        Toast.makeText(ChatBotAIActivity.this, "Error: Cannot Check Support", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onResults(Bundle bundle) {
                List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String result = results.get(0);
                binding.etChatBotAi.setText(result);
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String result = results.get(0);
                binding.etChatBotAi.setText(result);
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void typingIndicator() {
        handler = new Handler();
        binding.etChatBotAi.setText(null);

        runnable = () -> {
            if (binding.etChatBotAi.getText().toString().isEmpty())
                binding.etChatBotAi.setText(".");
            else if (binding.etChatBotAi.getText().toString().equals("."))
                binding.etChatBotAi.setText("..");
            else
                binding.etChatBotAi.setText("...");

            if (binding.etChatBotAi.getText().toString().equals("..."))
                binding.etChatBotAi.setText("");

            handler.postDelayed(runnable, 500);

        };
    }

    private void validateUserInput() {
        final String userInputText = binding.etChatBotAi.getText().toString();

        if(userInputText.isEmpty()) {
            Toast.makeText(this, "Invalid input...", Toast.LENGTH_SHORT).show();
            return;
        }

        chatBotModelClasses.add(new ChatBotModelClass(userInputText, true));
        chatBotAIAdapter.notifyItemInserted(chatBotModelClasses.size()-1);
        binding.chatBotAiRecyclerview.scrollToPosition(chatBotAIAdapter.getItemCount()-1);
        binding.etChatBotAi.setText("");

        typingIndicator();

        try {
            createRequestToOpenAI(userInputText);
        } catch (Exception e) {
            handler.removeCallbacks(runnable);
            loadProgress(false);
            Toast.makeText(this, "Catch," + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void populateRecyclerView() {
        chatBotModelClasses.add(new ChatBotModelClass("Hello, ako si starla paano ako sayo makakatulong?", false));
        chatBotAIAdapter.notifyItemInserted(chatBotModelClasses.size()-1);
    }

    private boolean isAskingIdentity(String userMessage) {
        final String []askingNames = {
                "ano ang iyong pangalangan",
                "pangalan mo",
                "whats your name",
                "what's your name",
                "anong name mo",
                "ano pangalan mo",
                "what is your name",
                "who are you",
                "who you",
                "ano ang tawag sayo",
                "tawag sayo"
        };

        for (String askingName : askingNames) {
            if (userMessage.toLowerCase().contains(askingName)) return true;
        }

        return false;
    }

    private boolean isAskingCreator(String userMessage) {
        final String[] userQuestion = {
                "who created you",
                "who made you",
                "who make you",
                "who programs you",
                "who program you",
                "who is the developer",
                "develops you",
                "sino ang gumawa sayo",
                "sino ang lumikha sayo",
                "sino ang gumawa ng app na to",
                "creates you",
                "create you",
                "sino nag gawa saimo",
                "naggawa sayo",
                "gumawa sayo"
        };

        for (String question : userQuestion)
            if (userMessage.toLowerCase().contains(question))
                return true;
        return false;
    }

    private void loadProgress(boolean isLoading) {
        if (isLoading) {
            binding.chatBotAiProgressBar.setVisibility(View.VISIBLE);
            binding.chatBotAiBtn.setVisibility(View.GONE);
        } else {
            binding.chatBotAiProgressBar.setVisibility(View.GONE);
            binding.chatBotAiBtn.setVisibility(View.VISIBLE);
        }
    }

    private void createRequestToOpenAI(String currentUserText) {

        loadProgress(true);
        handler.post(runnable);

        if (currentUserText.trim().equalsIgnoreCase("midwife")) {
            startActivity(new Intent(ChatBotAIActivity.this, AdminListOfUsers.class));

            chatBotModelClasses.add(new ChatBotModelClass("May maitutulong pa ba ako?", false));
            chatBotAIAdapter.notifyItemInserted(chatBotModelClasses.size()-1);
            binding.chatBotAiRecyclerview.scrollToPosition(chatBotAIAdapter.getItemCount()-1);
            handler.removeCallbacks(runnable);
            loadProgress(false);
            binding.etChatBotAi.setText("");
            return;
        }

        if (isAskingIdentity(currentUserText)) {
            chatBotModelClasses.add(new ChatBotModelClass("My name is Starla", false));
            chatBotAIAdapter.notifyItemInserted(chatBotModelClasses.size()-1);
            binding.chatBotAiRecyclerview.scrollToPosition(chatBotAIAdapter.getItemCount()-1);
            handler.removeCallbacks(runnable);
            loadProgress(false);
            binding.etChatBotAi.setText("");
            return;
        }

        if (isAskingCreator(currentUserText)) {
            chatBotModelClasses.add(new ChatBotModelClass(
                    "Ang gumawa sa akin ay ang grupo ng Itrust team. Ang grupong ito ay nagaaral sa mabini bilang isang computer science student at kasalukuyan silang 4th year student ngayong 2023.",
                    false));
            chatBotAIAdapter.notifyItemInserted(chatBotModelClasses.size()-1);
            binding.chatBotAiRecyclerview.scrollToPosition(chatBotAIAdapter.getItemCount()-1);
            handler.removeCallbacks(runnable);
            loadProgress(false);
            binding.etChatBotAi.setText("");
            return;
        }

        try {
            textDavinci(currentUserText);
//            experimentMethod(currentUserText);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void displayErrorMessage() {
        chatBotModelClasses.add(new ChatBotModelClass("I am very sorry but your asking is beyond of my knowledge." +
                " \nIf you want, you can chat the Midwife by typing MIDWIFE if you want to chat with me that I can answer here's a few tips" +
                "\n- Be specific to your question." +
                "\n- As much as possible when you're asking add a question (?) at the end." +
                "\n- Make it English or Tagalog only" +
                "\n- If I still can't answer you're query, make you're question to English if possible", false));
        chatBotAIAdapter.notifyItemInserted(chatBotModelClasses.size()-1);
        binding.chatBotAiRecyclerview.scrollToPosition(chatBotAIAdapter.getItemCount()-1);
        handler.removeCallbacks(runnable);
        binding.etChatBotAi.setText("");
        loadProgress(false);
    }

    private void textDavinci(String currentUserText) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.openai.com/v1/completions",
                response -> {
                    // Do something with the response
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray completions = jsonResponse.getJSONArray("choices");
                        if (completions.length() > 0) {
                            JSONObject firstCompletion = completions.getJSONObject(0);
                            String message = firstCompletion.getString("text");

                            chatBotModelClasses.add(new ChatBotModelClass(message.trim(), false));
                            chatBotAIAdapter.notifyItemInserted(chatBotModelClasses.size()-1);
                            binding.chatBotAiRecyclerview.scrollToPosition(chatBotAIAdapter.getItemCount()-1);
                            loadProgress(false);
                            handler.removeCallbacks(runnable);
                            binding.etChatBotAi.setText("");
                        } else {
                            Log.d("TAG", "createRequestToOpenAI: no completions");
                            displayErrorMessage();
                            System.out.println("Else: " + completions);
                        }

                    } catch (JSONException e) {
                        displayErrorMessage();
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    chatBotModelClasses.add(new ChatBotModelClass("I am very sorry but your asking is beyond to my knowledge." +
                            " \nIf you want, you can chat the Midwife by typing MIDWIFE", false));
                    chatBotAIAdapter.notifyItemInserted(chatBotModelClasses.size()-1);
                    binding.chatBotAiRecyclerview.scrollToPosition(chatBotAIAdapter.getItemCount()-1);
                    handler.removeCallbacks(runnable);
                    binding.etChatBotAi.setText(null);
                    loadProgress(false);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {

//                final String API_KEY_MAIN = "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";
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + "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");
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                // Create the request body as a JSON object
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("model", "text-davinci-003");
                    requestBody.put("prompt", currentUserText);
                    requestBody.put("max_tokens", 350);
                    requestBody.put("temperature", 0.1);
                    requestBody.put("user", new SessionManager(ChatBotAIActivity.this).getFullName()+"-itrust-app");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return requestBody.toString().getBytes();
            }
        };

        requestQueue.add(stringRequest);
    }
}