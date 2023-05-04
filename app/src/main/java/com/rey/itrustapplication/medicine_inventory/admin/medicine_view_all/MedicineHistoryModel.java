package com.rey.itrustapplication.medicine_inventory.admin.medicine_view_all;

public class MedicineHistoryModel {

    private final String medicineName, medicineStock, description, howToUse, whenToUse;

    public MedicineHistoryModel(String medicineName, String medicineStock, String description, String howToUse, String whenToUse) {
        this.medicineName = medicineName;
        this.medicineStock = medicineStock;
        this.description = description;
        this.howToUse = howToUse;
        this.whenToUse = whenToUse;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getMedicineStock() {
        return medicineStock;
    }

    public String getDescription() {
        return description;
    }

    public String getHowToUse() {
        return howToUse;
    }

    public String getWhenToUse() {
        return whenToUse;
    }
}
