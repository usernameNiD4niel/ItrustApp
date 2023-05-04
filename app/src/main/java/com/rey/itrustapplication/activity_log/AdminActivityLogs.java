package com.rey.itrustapplication.activity_log;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
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
import com.rey.itrustapplication.AdminDrawerBaseActivity;
import com.rey.itrustapplication.databinding.ActivityAdminLogsBinding;
import com.rey.itrustapplication.printfeature.Common;
import com.rey.itrustapplication.printfeature.PdfDocumentAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class AdminActivityLogs extends AdminDrawerBaseActivity {

    ActivityAdminLogsBinding binding;

    private AdminActivityLogsAdapter adapter;
    private LinkedList<AdminLogsModel> adminLogsModels;

    private Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLogsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Activity Log");

        createPrintPdf();

        adminLogsModels = new LinkedList<>();

        populateRecyclerView();

        binding.backButtonAdminLogs.setOnClickListener(view -> finish());

        new Handler().postDelayed(() -> {
            if (adminLogsModels.size() == 0) {
                binding.noDataFoundAdminLogs.setVisibility(View.VISIBLE);
                binding.progressBarAdminLogs.setVisibility(View.GONE);
                return;
            }

            adapter = new AdminActivityLogsAdapter(AdminActivityLogs.this, adminLogsModels);

            binding.recyclerViewActivityLogs.setAdapter(adapter);
            binding.recyclerViewActivityLogs.setLayoutManager(new LinearLayoutManager(AdminActivityLogs.this));
            binding.recyclerViewActivityLogs.setHasFixedSize(true);
            binding.recyclerViewActivityLogs.setVisibility(View.VISIBLE);

            binding.progressBarAdminLogs.setVisibility(View.GONE);
        }, 500);

    }

    private void createPrintPdf() {
        createPDFFile(Common.getAppPath(AdminActivityLogs.this) + "sample.pdf");

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        binding.printActivityLogs.setOnClickListener(view -> {
                            document.close();
                            Toast.makeText(AdminActivityLogs.this, "Preview PDF!", Toast.LENGTH_SHORT).show();
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
        float fontSize = 17.0f;

        BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

        Font titleFont = new Font(
                fontName,
                30.0f,
                Font.NORMAL,
                BaseColor.BLACK
        );
        addNewItem(document, "Admin Activities", Element.ALIGN_CENTER, titleFont);

        Font orderNumberFont = new Font(
                fontName,
                fontSize,
                Font.NORMAL,
                colorAccent
        );

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("ActivityLogs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    try {
                        addNewItem(document, "No Admin Activities Yet", Element.ALIGN_CENTER, orderNumberFont);
                        addLineSeparator(document);
                        return;
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }

                if (snapshot.exists()){
                    for (DataSnapshot adminData : snapshot.getChildren()) {

                        final String title = adminData.child("title").getValue(String.class);
                        final String date = adminData.child("date").getValue(String.class);
                        final String time = adminData.child("time").getValue(String.class);

                        try {
                            addNewItem(document, "Activity Title: " + title, Element.ALIGN_LEFT, orderNumberFont);
                            addNewItem(document, "Date: " + date, Element.ALIGN_LEFT, orderNumberFont);
                            addNewItem(document, "Time: " + time, Element.ALIGN_LEFT, orderNumberFont);

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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(AdminActivityLogs.this, Common.getAppPath(AdminActivityLogs.this) + "sample.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());

        }catch (Exception e) {
            Log.e("Daniel", "" + e.getMessage());
        }
    }

    private void populateRecyclerView() {

        adminLogsModels.clear();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("ActivityLogs");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String title = dataSnapshot.child("title").getValue(String.class);
                        final String date = dataSnapshot.child("date").getValue(String.class);
                        final String time = dataSnapshot.child("time").getValue(String.class);

                        adminLogsModels.add(new AdminLogsModel(title, time, date));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStop() {
        document.resetPageCount();
        document.close();
        super.onStop();
    }
}