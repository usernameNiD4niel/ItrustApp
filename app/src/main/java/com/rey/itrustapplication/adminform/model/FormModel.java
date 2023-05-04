package com.rey.itrustapplication.adminform.model;

public class FormModel {

    private final String patientName, timeAdded, dateAdded, totalRecords;

    public FormModel(String patientName, String timeAdded, String dateAdded, String totalRecords) {
        this.patientName = patientName;
        this.timeAdded = timeAdded;
        this.dateAdded = dateAdded;
        this.totalRecords = totalRecords;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
