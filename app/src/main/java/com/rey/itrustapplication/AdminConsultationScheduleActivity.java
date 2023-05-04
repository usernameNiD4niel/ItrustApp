package com.rey.itrustapplication;

import androidx.annotation.NonNull;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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
import com.rey.itrustapplication.databinding.ActivityAdminConsultationScheduleBinding;
import com.rey.itrustapplication.printfeature.Common;
import com.rey.itrustapplication.printfeature.PdfDocumentAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AdminConsultationScheduleActivity extends AdminDrawerBaseActivity {

    Document document;

    private ActivityAdminConsultationScheduleBinding binding;

    DatabaseReference databaseReference;
    ValueEventListener valueEventListenerPending, valueEventListenerApprove, valueEventListenerDecline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminConsultationScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        allocateActivityTitle("Consultation Schedule");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        createPDFFile(Common.getAppPath(AdminConsultationScheduleActivity.this) + "consultation_schedule.pdf");

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        binding.printPdfButton.setOnClickListener(view -> {
                            document.close();
                            printPDF();
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(AdminConsultationScheduleActivity.this, "Transferring the document to PDF was denied.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        Toast.makeText(AdminConsultationScheduleActivity.this, "Please restart the app if you can't see the \"Allow\" prompt", Toast.LENGTH_SHORT).show();
                    }
                })
                .check();


        ViewPagerAdapterConsultaionSched viewPagerAdapterConsultaionSched = new ViewPagerAdapterConsultaionSched(this);
        binding.viewPager.setAdapter(viewPagerAdapterConsultaionSched);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Pending");
            } else if (position == 1) {
                tab.setText("Approved");
            } else {
                tab.setText("Declined");
            }

        }
        ).attach();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onResume() {
        if (document != null && document.getPageNumber() != 0) {
            document.resetPageCount();
            document.close();
        }
        super.onResume();
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
            if (!new File(path).delete()) {
                Toast.makeText(this, "Please try again, cannot refresh the data", Toast.LENGTH_SHORT).show();
                return;
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

        Font orderNumberFont = new Font(
                fontName,
                fontSize,
                Font.NORMAL,
                colorAccent
        );


        valueEventListenerApprove = databaseReference.child("ApproveRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    addNewItem(document, "Approve Request", Element.ALIGN_CENTER, titleFont);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                if (!snapshot.exists()) {
                    try {
                        addNewItem(document, "No Approve Request Record", Element.ALIGN_CENTER, orderNumberFont);
                        addLineSeparator(document);
                        return;
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }

                for (DataSnapshot pendingStatus : snapshot.getChildren()) {

                    final String fullName = pendingStatus.child("fullName").getValue(String.class);
                    final String schedule = pendingStatus.child("schedule").getValue(String.class);
                    final String date_requested = pendingStatus.child("date_requested").getValue(String.class);

                    try {
                        addNewItem(document, "Resident Name: " + fullName, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Desire Schedule: " + schedule, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Date Requested: " + date_requested, Element.ALIGN_LEFT, orderNumberFont);

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

        valueEventListenerPending = databaseReference.child("PendingRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    addNewItem(document, "Pending Request", Element.ALIGN_CENTER, titleFont);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                if (!snapshot.exists()) {
                    try {
                        addNewItem(document, "No Pending Request Record", Element.ALIGN_CENTER, orderNumberFont);
                        addLineSeparator(document);
                        return;
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
                for (DataSnapshot pendingStatus : snapshot.getChildren()) {

                    final String fullName = pendingStatus.child("fullName").getValue(String.class);
                    final String schedule = pendingStatus.child("schedule").getValue(String.class);
                    final String date_requested = pendingStatus.child("date_requested").getValue(String.class);

                    try {
                        addNewItem(document, "Resident Name: " + fullName, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Desire Schedule: " + schedule, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Date Requested: " + date_requested, Element.ALIGN_LEFT, orderNumberFont);

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

        valueEventListenerDecline = databaseReference.child("DeclineRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    addNewItem(document, "Declined Request", Element.ALIGN_CENTER, titleFont);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                if (!snapshot.exists()) {
                    try {
                        addNewItem(document, "No Declined Request Record", Element.ALIGN_CENTER, orderNumberFont);
                        addLineSeparator(document);
                        return;
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }

                for (DataSnapshot pendingStatus : snapshot.getChildren()) {

                    final String fullName = pendingStatus.child("fullName").getValue(String.class);
                    final String schedule = pendingStatus.child("schedule").getValue(String.class);
                    final String date_requested = pendingStatus.child("date_requested").getValue(String.class);

                    try {
                        addNewItem(document, "Resident Name: " + fullName, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Desire Schedule: " + schedule, Element.ALIGN_LEFT, orderNumberFont);
                        addNewItem(document, "Date Requested: " + date_requested, Element.ALIGN_LEFT, orderNumberFont);

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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(AdminConsultationScheduleActivity.this, Common.getAppPath(AdminConsultationScheduleActivity.this) + "consultation_schedule.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());

        }catch (Exception e) {
            Log.e("Daniel", "" + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListenerPending);
        databaseReference.removeEventListener(valueEventListenerApprove);
        databaseReference.removeEventListener(valueEventListenerDecline);
    }

    @Override
    protected void onStop() {
        document.resetPageCount();
        document.close();
        super.onStop();
    }

}