package com.rey.itrustapplication.adminform.model;

public class PrenatalCareSideAModel {

    private final String patientName, currentDateAdded, currentTimeAdded, nodeKey;

    public PrenatalCareSideAModel(String patientName, String currentDateAdded, String currentTimeAdded, String nodeKey) {
        this.patientName = patientName;
        this.currentDateAdded = currentDateAdded;
        this.currentTimeAdded = currentTimeAdded;
        this.nodeKey = nodeKey;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public String getCurrentTimeAdded() {
        return currentTimeAdded;
    }

    public String getCurrentDateAdded() {
        return currentDateAdded;
    }

    public String getPatientName() {
        return patientName;
    }

}
