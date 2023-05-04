package com.rey.itrustapplication.residence_profile;

import static com.rey.itrustapplication.helperclasses.UtilityClass.firebaseInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.rey.itrustapplication.databinding.ActivityResidenceProfileBinding;
import com.rey.itrustapplication.printfeature.Common;
import com.rey.itrustapplication.printfeature.PdfDocumentAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class ResidenceProfile extends AppCompatActivity {

    ActivityResidenceProfileBinding binding;

    private LinkedList<ResidenceProfileModel> residenceProfileModels;
    private ResidenceProfileAdapter adapter;

    Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResidenceProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButtonResidenceProfile.setOnClickListener(view -> finish());

        residenceProfileModels = new LinkedList<>();

        populateRecyclerView();

        binding.searchView.clearFocus();
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        createPermission();

    }

    private void createPermission() {
        createPDFFile(Common.getAppPath(ResidenceProfile.this) + "residence_profile.pdf");

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        binding.printResidenceProfile.setOnClickListener(view -> {
                            document.close();
                            Toast.makeText(ResidenceProfile.this, "Successfully created pdf!", Toast.LENGTH_SHORT).show();
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
        
        if (new File(path).exists()) {
            if (new File(path).delete()) {
                Toast.makeText(this, "File was deleted... creating new file", Toast.LENGTH_SHORT).show();
            }
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
        addNewItem(document, "Registered Residence Credential", Element.ALIGN_CENTER, titleFont);

        Font orderNumberFont = new Font(
                fontName,
                fontSize,
                Font.NORMAL,
                colorAccent
        );

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");

        final DatabaseReference databaseReferenceApprovedRequest = firebaseDatabase.getReference();
        databaseReferenceApprovedRequest.child("RegularUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    try {
                        addNewItem(document, "No Resident Record Yet", Element.ALIGN_CENTER, orderNumberFont);
                        addLineSeparator(document);
                        return;
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }

                for (DataSnapshot residenceData : snapshot.getChildren()) {

                    if (residenceData.hasChild("fullName")) {

                        final String fullName = residenceData.child("fullName").getValue(String.class);
                        final String birthday = residenceData.child("birthday").getValue(String.class);
                        final String phoneNumber = residenceData.child("phoneNumber").getValue(String.class);
                        final String purok = residenceData.child("purok").getValue(String.class);
                        final String gender = residenceData.child("gender").getValue(String.class);

                        try {
                            addNewItem(document, "Name: " + fullName, Element.ALIGN_LEFT, orderNumberFont);
                            addNewItem(document, "Phone Number: " + phoneNumber, Element.ALIGN_LEFT, orderNumberFont);
                            addNewItem(document, "Birthday: " + birthday, Element.ALIGN_LEFT, orderNumberFont);
                            addNewItem(document, "Purok: " + purok, Element.ALIGN_LEFT, orderNumberFont);
                            addNewItem(document, "Gender: " + gender, Element.ALIGN_LEFT, orderNumberFont);

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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(ResidenceProfile.this, Common.getAppPath(ResidenceProfile.this) + "residence_profile.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());

        }catch (Exception e) {
            Log.e("Daniel", "" + e.getMessage());
        }
    }

    private void populateRecyclerView() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseInstance).getReference("RegularUsers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.hasChild("fullName")) {
                            final String name = dataSnapshot.child("fullName").getValue(String.class);
                            final String gender = dataSnapshot.child("gender").getValue(String.class);
                            final String birthday = dataSnapshot.child("birthday").getValue(String.class);
                            final String purok = dataSnapshot.child("purok").getValue(String.class);
                            final String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);

                            residenceProfileModels.add(new ResidenceProfileModel(name, birthday, phoneNumber, purok, gender));
                        }
                    }
                    new Handler().postDelayed(() -> {
                        adapter = new ResidenceProfileAdapter(ResidenceProfile.this, residenceProfileModels);
                        binding.recyclerViewResidenceProfile.setLayoutManager(new LinearLayoutManager(ResidenceProfile.this));
                        binding.recyclerViewResidenceProfile.setAdapter(adapter);
                        binding.recyclerViewResidenceProfile.setHasFixedSize(true);

                        binding.recyclerViewResidenceProfile.setVisibility(View.VISIBLE);

                        binding.progressBarResidenceProfile.setVisibility(View.GONE);

                    }, 700);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList(String text) {
        LinkedList<ResidenceProfileModel> filteredList = new LinkedList<>();

        for (ResidenceProfileModel newList : residenceProfileModels) {
            if (newList.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(newList);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }
    }

    @Override
    protected void onStop() {
        document.resetPageCount();
        document.close();
        super.onStop();
    }
}