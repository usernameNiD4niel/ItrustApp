package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOffline;
import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOnline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.common.base.CharMatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.database.UserDatabase;
import com.rey.itrustapplication.databinding.ActivityUserConsultMagpakunsultaBinding;
import com.rey.itrustapplication.helperclasses.UserMagpakonsultaHelperClass;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class UserConsultMagpakunsulta extends AppCompatActivity {

    private ActivityUserConsultMagpakunsultaBinding activityUserConsultMagpakunsultaBinding;
    private String daySelected = "";
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://fir-thesis-15f90-default-rtdb.firebaseio.com/");
    private final DatabaseReference databaseReference = firebaseDatabase.getReference("PendingRequest");
    private UserDatabase userDatabase;
    private SessionManager sessionManager;
    private int hour = 0, minute = 0;
    private LinkedList<String> newDaysList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserConsultMagpakunsultaBinding = ActivityUserConsultMagpakunsultaBinding.inflate(getLayoutInflater());
        setContentView(activityUserConsultMagpakunsultaBinding.getRoot());

        userDatabase = new UserDatabase(UserConsultMagpakunsulta.this, databaseReference);
        sessionManager = new SessionManager(UserConsultMagpakunsulta.this);


        populateSpinner();
        setUpTimePicker();
        activityUserConsultMagpakunsultaBinding.backButtonConsultation.setOnClickListener(view -> finish());

        activityUserConsultMagpakunsultaBinding.sendRequestBtn.setOnClickListener(view -> validateInputs());

    }

    private void validateInputs() {
        if (daySelected.equals("")) {
            activityUserConsultMagpakunsultaBinding.availableDate.setError("pumili ng araw ng konsulta");
            activityUserConsultMagpakunsultaBinding.availableDate.requestFocus();
            return;
        }

        for (String days : newDaysList) {
            if (!days.contains("Not Available")){
                final String[] extractedDaysList = days.split("-");

                String startingTime = CharMatcher.inRange('0', '9').retainFrom(extractedDaysList[0]);

                if (hour == 0) {
                    Toast.makeText(this, "The RHU is still sleeping!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // For four digit (12:11...)
                if (startingTime.length() == 4) {
                    //Hour
                    final int firstTwoDigits = Integer.parseInt(startingTime.substring(0,2));

                    if (firstTwoDigits > hour) {
                        Toast.makeText(this, "Your time is out of the range!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (firstTwoDigits == hour){
                        // Minutes
                        final int lastTwoDigits = Integer.parseInt(startingTime.substring(3));
                        if (lastTwoDigits > minute){
                            Toast.makeText(this, "Your time is out of the range!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else if (startingTime.length() == 3) {
                    // For three digits (9:10...) admin
                    final int firstDigit = Integer.parseInt(startingTime.substring(0,1));
                    if (hour > firstDigit) {
                        Toast.makeText(this, "Your time is out of the range!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (hour == firstDigit) {
                        final int lastTwoDigits = Integer.parseInt(startingTime.substring(1));
                        if (lastTwoDigits > minute){
                            Toast.makeText(this, "Your time is out of the range!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }


                final boolean hapon = days.contains("pm");

                String endingTime = CharMatcher.inRange('0', '9').retainFrom(extractedDaysList[1]);
                // For four digit (12:11...)
                if (endingTime.length() == 4) {
                    //Hour
                    int firstTwoDigits = Integer.parseInt(endingTime.substring(0,2));

                    firstTwoDigits = hapon ? firstTwoDigits+12 : firstTwoDigits;

                    if (firstTwoDigits > hour) {
                        Toast.makeText(this, "Your time is out of the range!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (firstTwoDigits == hour){
                        // Minutes
                        final int lastTwoDigits = Integer.parseInt(endingTime.substring(3));
                        if (lastTwoDigits > minute){
                            Toast.makeText(this, "Your time is out of the range!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else if (endingTime.length() == 3) {
                    // For three digits (9:10...) admin
                    int firstDigit = Integer.parseInt(endingTime.substring(0,1));

                    firstDigit = hapon ? firstDigit+12 : firstDigit;

                    if (hour > firstDigit) {
                        Toast.makeText(this, "Your time is out of the range!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (hour == firstDigit) {
                        final int lastTwoDigits = Integer.parseInt(endingTime.substring(1));
                        if (lastTwoDigits > minute){
                            Toast.makeText(this, "Your time is out of the range!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }
        }

        activityUserConsultMagpakunsultaBinding.sendRequestBtn.setVisibility(View.INVISIBLE);
        activityUserConsultMagpakunsultaBinding.progressBarMagpakonsulta.setVisibility(View.VISIBLE);

        appendStatusToCurrentUser();

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        String amOrPm;

        amOrPm = hour > 12 ? "pm" : "am";

        UserMagpakonsultaHelperClass magpakonsultaHelperClass =
                new UserMagpakonsultaHelperClass(sessionManager.getFullName(), daySelected, hour, minute,
                        "Pending Request", currentDate, amOrPm);

        Toast.makeText(this, "am or pm: " + amOrPm, Toast.LENGTH_SHORT).show();
        Log.d("FCM", "am or pm: " + amOrPm);

        userDatabase.addMagpakonsultaStatus(magpakonsultaHelperClass, activityUserConsultMagpakunsultaBinding.sendRequestBtn,
                activityUserConsultMagpakunsultaBinding.progressBarMagpakonsulta);

    }

    private void appendStatusToCurrentUser() {

        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child("RegularUsers").child(sessionManager.getFullName()).child("status")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reference.child("RegularUsers").child(sessionManager.getFullName()).child("status").setValue("Pending Request");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void setUpTimePicker() {
        activityUserConsultMagpakunsultaBinding.timePicker.setIs24HourView(false); // used to display AM/PM mode
        // perform set on time changed listener event
        activityUserConsultMagpakunsultaBinding.timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            Toast.makeText(getApplicationContext(), hourOfDay + " " + minute, Toast.LENGTH_SHORT).show();
            hour = hourOfDay;
            this.minute = minute;
        });
    }

    private void populateSpinner() {

        DatabaseReference reference = firebaseDatabase.getReference("Admin");

        LinkedList<String> listOfDays = new LinkedList<>();

        reference.child("consultation_schedule")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("monday")) {
                            listOfDays.add("MONDAY || " + snapshot.child("monday").getValue(String.class));
                            listOfDays.add("TUESDAY || " + snapshot.child("tuesday").getValue(String.class));
                            listOfDays.add("WEDNESDAY || " + snapshot.child("wednesday").getValue(String.class));
                            listOfDays.add("THURSDAY || " + snapshot.child("thursday").getValue(String.class));
                            listOfDays.add("FRIDAY || " + snapshot.child("friday").getValue(String.class));
                            listOfDays.add("SATURDAY || " + snapshot.child("saturday").getValue(String.class));
                            listOfDays.add("SUNDAY || " + snapshot.child("sunday").getValue(String.class));
                            loadDropDownItem(listOfDays);
                            newDaysList = listOfDays;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadDropDownItem(LinkedList<String> listOfDays) {

        final LinkedList<String> filteredList = new LinkedList<>();

        for (String filterList : listOfDays)
            if (!filterList.contains("Not Available"))
                filteredList.add(filterList);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.signup_gender_list, filteredList);

        activityUserConsultMagpakunsultaBinding.availableDate.setAdapter(spinnerArrayAdapter);

        activityUserConsultMagpakunsultaBinding.availableDate.setOnItemClickListener((adapterView, view, i, l) -> {
            daySelected = adapterView.getItemAtPosition(i).toString();
            if (daySelected.contains("MONDAY")) daySelected = "MONDAY";
            else if (daySelected.contains("TUESDAY")) daySelected = "TUESDAY";
            else if (daySelected.contains("WEDNESDAY")) daySelected = "WEDNESDAY";
            else if (daySelected.contains("THURSDAY")) daySelected = "THURSDAY";
            else if (daySelected.contains("FRIDAY")) daySelected = "FRIDAY";
            else if (daySelected.contains("SATURDAY")) daySelected = "SATURDAY";
            else if (daySelected.contains("SUNDAY")) daySelected = "SUNDAY";
        });
    }

    @Override
    protected void onStart() {

        setAvailabilityToOnline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        activityUserConsultMagpakunsultaBinding.sendRequestBtn.setVisibility(View.INVISIBLE);
        activityUserConsultMagpakunsultaBinding.progressBarMagpakonsulta.setVisibility(View.VISIBLE);

//        check muna natin kung expired na yung request ni user

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh-mm-ss", Locale.getDefault());
        String time = simpleDateFormat.format(date);
        int hour = Integer.parseInt(time.substring(0,2));
        int minute = Integer.parseInt(time.substring(3,5));
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String currentDay;

        switch (day) {
            case Calendar.MONDAY:
                currentDay = "MONDAY";
                break;
            case Calendar.TUESDAY:
                currentDay = "TUESDAY";
                break;
            case Calendar.WEDNESDAY:
                currentDay = "WEDNESDAY";
                break;
            case Calendar.THURSDAY:
                currentDay = "THURSDAY";
                break;
            case Calendar.FRIDAY:
                currentDay = "FRIDAY";
                break;
            case Calendar.SATURDAY:
                currentDay = "SATURDAY";
                break;
            default:
                currentDay = "SUNDAY";
                break;
        }

        System.out.println("Day: " + currentDay + " Hour: " + hour + " Minute: " + minute);
        Toast.makeText(this, "Day: " + currentDay + " Hour: " + hour + " Minute: " + minute, Toast.LENGTH_SHORT).show();

        DatabaseReference reference = firebaseDatabase.getReference("RegularUsers");
        UserDatabase database = new UserDatabase(UserConsultMagpakunsulta.this, reference);

        database.getMagpakonsultaStatus(activityUserConsultMagpakunsultaBinding.sendRequestBtn, currentDay, hour, minute,
                activityUserConsultMagpakunsultaBinding.progressBarMagpakonsulta);

        super.onStart();
    }


    @Override
    public void onDestroy() {
        setAvailabilityToOffline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        setAvailabilityToOnline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        super.onResume();
    }

    @Override
    public void onStop() {
        setAvailabilityToOffline(new SessionManager(getApplicationContext()).getFullName(), "RegularUsers");
        super.onStop();
    }

}