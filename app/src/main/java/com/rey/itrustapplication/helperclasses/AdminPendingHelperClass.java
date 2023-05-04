package com.rey.itrustapplication.helperclasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AdminPendingHelperClass {


    String fullName, schedule, date_requested;

    public AdminPendingHelperClass(String fullName, String date_requested, String schedule) {
        this.fullName = fullName;
        this.date_requested = date_requested;
        this.schedule = schedule;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDate_requested() {
        return date_requested;
    }

    public String getSchedule() {
        return schedule;
    }
}
