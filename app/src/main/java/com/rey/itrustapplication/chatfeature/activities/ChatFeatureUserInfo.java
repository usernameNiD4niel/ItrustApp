package com.rey.itrustapplication.chatfeature.activities;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.ActivityChatFeatureUserInfoBinding;
import com.rey.itrustapplication.printfeature.Common;
import com.rey.itrustapplication.printfeature.PdfDocumentAdapter;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFeatureUserInfo extends AppCompatActivity {

    ActivityChatFeatureUserInfoBinding binding;
    private Document document;

    private String chatRoomId, chattingTo;

    private CircleImageView userProfileChatFeature;
    private LinearLayout personalInfoParent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatFeatureUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButtonChatFeatureUserInfo.setOnClickListener(back -> finish());

        userProfileChatFeature = findViewById(R.id.userProfileChatFeature);
        personalInfoParent = findViewById(R.id.personal_information_cfui);
        progressBar = findViewById(R.id.progress_bar_chat_feature_user_info);

        chatRoomId = getIntent().getStringExtra("chatRoomId");
        chattingTo = getIntent().getStringExtra("chattingTo");

        if (new SessionManager(this).getLogin()) {
            Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.darna_final);
            userProfileChatFeature.setImageDrawable(drawable);

            binding.chattingToName.setText(getString(R.string.jane_de_leon));
            binding.chattingToContactInfo.setText(getString(R.string._0987_654_3210));
            binding.chattingToLocation.setText(getString(R.string.purok_4_itong_address_ay_sample_lamang_at_ito_y_papalitan_din));
            binding.chattingToEmailInfo.setText(getString(R.string.barangayalawihaoofficial_gmail_com));
            progressBar.setVisibility(View.GONE);
            personalInfoParent.setVisibility(View.VISIBLE);

        } else {
            updateUserProfile();
            getUserGender();
        }

        binding.chattingToBannerTop.setText(chattingTo);

        createPermission();
    }

    private void updateUserProfile() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String drawableHash = snapshot.child(chattingTo).getValue(String.class);
                    if (drawableHash == null) {
                        return;
                    }
                    Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), Integer.parseInt(drawableHash));
                    userProfileChatFeature.setImageDrawable(drawable);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatFeatureUserInfo.this, "Check your internet and try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserGender() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("RegularUsers");
        databaseReference.child(chattingTo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    final String fullName = snapshot.child("fullName").getValue(String.class);
                    final String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    final String address = snapshot.child("purok").getValue(String.class) + ", Barangay Alawihao Daet Camarines Norte";

                    final String userEmail = "Unavailable";

                    binding.chattingToName.setText(fullName);
                    binding.chattingToContactInfo.setText(phoneNumber);
                    binding.chattingToLocation.setText(address);
                    binding.chattingToEmailInfo.setText(userEmail);
                }
                progressBar.setVisibility(View.GONE);
                personalInfoParent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatFeatureUserInfo.this, "Check your internet and try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPermission() {
        createPDFFile(Common.getAppPath(ChatFeatureUserInfo.this) + "conversation.pdf");

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.d("Daniel", "onPermissionGranted: granted");
                            Log.d("Daniel", "onPermissionGranted: not null");
                            binding.downloadConversationButton.setOnClickListener(view -> {
                                Log.d("Daniel", "onPermissionGranted: button clicked");
                                document.close();
                                Toast.makeText(ChatFeatureUserInfo.this, "Successfully created snapshot of convo!", Toast.LENGTH_SHORT).show();
                                printPDF();
                            });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    private void addLineSeparator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int alignCenter, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(alignCenter);
        document.add(paragraph);
    }

    private void createPDFFile(String path) {
        if (new File(path).exists())
            new File(path).delete();

        try {
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));

            document.open();
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("ITrustDev");
            document.addCreator("Daniel Rey");

//            Ow shit not good
            formatApprovedRequest(document);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void formatApprovedRequest(Document document) throws DocumentException, IOException {
        BaseColor colorAccent = new BaseColor(79,102,216,255);
        float fontSize = 16.0f;

        BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

        Font titleFont = new Font(
                fontName,
                30.0f,
                Font.NORMAL,
                BaseColor.BLACK
        );
        addNewItem(document, "Convo with " + chattingTo, Element.ALIGN_CENTER, titleFont);

        Font orderNumberFont = new Font(
                fontName,
                fontSize,
                Font.NORMAL,
                colorAccent
        );

        Font contentFont = new Font(
                fontName,
                fontSize,
                Font.BOLD,
                colorAccent
        );

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");

        final DatabaseReference databaseReferenceApprovedRequest = firebaseDatabase.getReference();
        databaseReferenceApprovedRequest.child("messages").child(chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    try {
                        addNewItem(document, "No Conversation Record", Element.ALIGN_CENTER, orderNumberFont);
                        addLineSeparator(document);
                        return;
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }

                for (DataSnapshot residenceData : snapshot.getChildren()) {

                    if (residenceData.hasChild("content")) {

                        final String content = residenceData.child("content").getValue(String.class);
                        final String sender = residenceData.child("sender").getValue(String.class);

                        try {
                            if (sender == null) return;


                            if (!sender.equals(chattingTo)) {
                                addNewItem(document, "Sender: " + sender, Element.ALIGN_RIGHT, orderNumberFont);
                                addNewItem(document, "\""+content+"\"", Element.ALIGN_RIGHT, contentFont);
                            } else {
                                addNewItem(document, "Sender: " + sender, Element.ALIGN_LEFT, orderNumberFont);
                                addNewItem(document, "\""+content+"\"", Element.ALIGN_LEFT, contentFont);
                            }
                            addLineSeparator(document);

                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(ChatFeatureUserInfo.this, Common.getAppPath(ChatFeatureUserInfo.this) + "conversation.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());

        }catch (Exception e) {
            Log.e("Daniel", "" + e.getMessage());
        }
    }

}