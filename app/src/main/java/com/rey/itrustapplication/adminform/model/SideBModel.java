package com.rey.itrustapplication.adminform.model;

public class SideBModel {

    private final String patientName;
    private final String dateOfVisit;
    private final String followUpDateVisit;

    public SideBModel(String patientName, String dateOfVisit, String followUpDateVisit) {
        this.patientName = patientName;
        this.dateOfVisit = dateOfVisit;
        this.followUpDateVisit = followUpDateVisit;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public String getFollowUpDateVisit() {
        return followUpDateVisit;
    }
}
