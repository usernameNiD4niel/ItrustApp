package com.rey.itrustapplication.medicine_inventory.admin.history_view_all;

public class HistoryViewModel {

    private final String patientName;
    private final String medicineName;
    private final String quantity;
    private final String reason;
    private final String dateReceive;

    public HistoryViewModel(String patientName, String medicineName, String quantity, String reason, String dateReceive) {
        this.patientName = patientName;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.reason = reason;
        this.dateReceive = dateReceive;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getDateReceive() {
        return dateReceive;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getReason() {
        return reason;
    }
}
