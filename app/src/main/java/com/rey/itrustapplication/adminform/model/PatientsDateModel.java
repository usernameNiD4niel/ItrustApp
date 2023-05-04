package com.rey.itrustapplication.adminform.model;

public class PatientsDateModel {

    private final String date, time, dateKey;

    public PatientsDateModel(String date, String time, String dateKey) {
        this.date = date;
        this.time = time;
        this.dateKey = dateKey;
    }

    public String getDateKey() {
        return dateKey;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

}
