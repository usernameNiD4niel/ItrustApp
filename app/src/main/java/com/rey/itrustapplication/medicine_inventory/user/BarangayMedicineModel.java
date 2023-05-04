package com.rey.itrustapplication.medicine_inventory.user;

public class BarangayMedicineModel {

    String medicineName, medicineDescription, stock;

    public BarangayMedicineModel(String medicineName, String medicineDescription, String stock) {
        this.medicineName = medicineName;
        this.medicineDescription = medicineDescription;
        this.stock = stock;
    }

    public String getMedicineDescription() {
        return medicineDescription;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getStock() {
        return stock;
    }
}
