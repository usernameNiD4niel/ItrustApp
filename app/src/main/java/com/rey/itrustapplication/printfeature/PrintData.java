package com.rey.itrustapplication.printfeature;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.widget.Button;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrintData extends AppCompatActivity {

    Button btn_create_pdf;
    Document document;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_data);
        btn_create_pdf = findViewById(R.id.btn_create_pdf);

        createPDFFile(Common.getAppPath(PrintData.this) + "test_pdf.pdf");

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        btn_create_pdf.setOnClickListener(view -> {
                            document.close();

                            Toast.makeText(PrintData.this, "Success!", Toast.LENGTH_SHORT).show();

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

    private void createPDFFile(String path) {
        if (new File(path).exists())
            if (new File(path).delete()) {
                Toast.makeText(this, "The file was deleted... creating new file", Toast.LENGTH_SHORT).show();
            }

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
        float fontSize = 18.0f;

        BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

        Font titleFont = new Font(
                fontName,
                30.0f,
                Font.NORMAL,
                BaseColor.BLACK
        );
        addNewItem(document, "Approve Request", Element.ALIGN_CENTER, titleFont);

        Font orderNumberFont = new Font(
                fontName,
                fontSize,
                Font.NORMAL,
                colorAccent
        );

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");

        final DatabaseReference databaseReferenceApprovedRequest = firebaseDatabase.getReference();
        databaseReferenceApprovedRequest.child("ApproveRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot pendingStatus : snapshot.getChildren()) {
                    final String fullName = pendingStatus.child("fullName").getValue(String.class);
                    final String day = pendingStatus.child("day").getValue(String.class);
                    final Long hour = pendingStatus.child("hour").getValue(Long.class);
                    final Long minute = pendingStatus.child("minute").getValue(Long.class);
                    final String amPm = pendingStatus.child("amOrPm").getValue(String.class);
                    final String dateRequested = pendingStatus.child("dateRequested").getValue(String.class);
                    final String dateDeclinedOrApproved = pendingStatus.child("dateDeclinedOrApproved").getValue(String.class);

                    final String desireTime = hour + ":" + minute + " " + amPm;
                    try {
                        addNewItem(document, "Resident Name: " + fullName, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Desire Schedule: " + day, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Day and Date Approve: " + dateDeclinedOrApproved, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Desire Time: " + desireTime, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Date Requested: " + dateRequested, Element.ALIGN_LEFT, orderNumberFont);

                        addLineSeparator(document);

                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatabaseReference databaseReferencePendingRequest = firebaseDatabase.getReference();
        databaseReferencePendingRequest.child("PendingRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    try {
                        addNewItem(document, "No Pending Request Record", Element.ALIGN_LEFT, orderNumberFont);
                        addLineSeparator(document);
                        return;
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    addNewItem(document, "Pending Request", Element.ALIGN_CENTER, titleFont);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                for (DataSnapshot pendingStatus : snapshot.getChildren()) {
                    final String fullName = pendingStatus.child("fullName").getValue(String.class);
                    final String day = pendingStatus.child("day").getValue(String.class);
                    final Long hour = pendingStatus.child("hour").getValue(Long.class);
                    final Long minute = pendingStatus.child("minute").getValue(Long.class);
                    final String amPm = pendingStatus.child("amOrPm").getValue(String.class);
                    final String dateRequested = pendingStatus.child("dateRequested").getValue(String.class);

                    final String desireTime = hour + ":" + minute + " " + amPm;
                    try {
                        addNewItem(document, "Resident Name: " + fullName, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Desire Schedule: " + day, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Desire Time: " + desireTime, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Date Requested: " + dateRequested, Element.ALIGN_LEFT, orderNumberFont);

                        addLineSeparator(document);

                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatabaseReference databaseReferenceDeclinedRequest = firebaseDatabase.getReference();
        databaseReferenceDeclinedRequest.child("DeclineRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    try {
                        addNewItem(document, "No Pending Request Record", Element.ALIGN_LEFT, orderNumberFont);
                        addLineSeparator(document);
                        return;
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    addNewItem(document, "Declined Request", Element.ALIGN_CENTER, titleFont);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                for (DataSnapshot pendingStatus : snapshot.getChildren()) {
                    final String fullName = pendingStatus.child("fullName").getValue(String.class);
                    final String day = pendingStatus.child("day").getValue(String.class);
                    final Long hour = pendingStatus.child("hour").getValue(Long.class);
                    final Long minute = pendingStatus.child("minute").getValue(Long.class);
                    final String amPm = pendingStatus.child("amOrPm").getValue(String.class);
                    final String dateRequested = pendingStatus.child("dateRequested").getValue(String.class);
                    final String dateDeclinedOrApproved = pendingStatus.child("dateDeclinedOrApproved").getValue(String.class);

                    final String desireTime = hour + ":" + minute + " " + amPm;
                    try {
                        addNewItem(document, "Resident Name: " + fullName, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Desire Schedule: " + day, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Day and Date Approve: " + dateDeclinedOrApproved, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Desire Time: " + desireTime, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Date Requested: " + dateRequested, Element.ALIGN_LEFT, orderNumberFont);

                        addLineSeparator(document);

                    } catch (DocumentException e) {
                        e.printStackTrace();
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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(PrintData.this, Common.getAppPath(PrintData.this) + "test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());

        }catch (Exception e) {
            Log.e("Daniel", "" + e.getMessage());
        }
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
}

/*
BaseColor colorAccent = new BaseColor(79,102,216,255);
            float fontSize = 12.0f;
            float valueFontSize = 18.0f;

            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

            Font titleFont = new Font(
                    fontName,
                    30.0f,
                    Font.NORMAL,
                    BaseColor.BLACK
            );
            addNewItem(document, "Approve Request", Element.ALIGN_CENTER, titleFont);

            Font orderNumberFont = new Font(
                    fontName,
                    fontSize,
                    Font.NORMAL,
                    colorAccent
            );
            addNewItem(document, "Order No:", Element.ALIGN_LEFT, orderNumberFont);
            // addNewItem(document, "Order No:", Element.ALIGN_LEFT, orderNumberFont);

            Font orderNumberValueFont = new Font(
                    fontName,
                    valueFontSize,
                    Font.NORMAL,
                    BaseColor.BLACK
            );
            addNewItem(document, "#717171", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeparator(document);

            addNewItem(document, "Order Date", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "10/4/2022", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeparator(document);

            addNewItem(document, "Account Name:", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "Daniel Rey", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSpace(document);

            addNewItem(document, "Product Detail", Element.ALIGN_CENTER, titleFont);
            addLineSeparator(document);

            addNewItemWithLeftAndRight(document, "Pizza 25", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont);

            addLineSeparator(document);

            addNewItemWithLeftAndRight(document, "Pizza 26", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont);

            addLineSeparator(document);

            addLineSpace(document);
            addLineSpace(document);

            addNewItemWithLeftAndRight(document, "Total", "24000.0", titleFont, orderNumberValueFont);

 */