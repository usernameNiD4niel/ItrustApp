package com.rey.itrustapplication.residence_profile;

public class ResidenceProfileModel {

    String name, birthday, phoneNumber, purok, gender;

    public ResidenceProfileModel(String name, String birthday, String phoneNumber, String purok, String gender) {
        this.name = name;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.purok = purok;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPurok() {
        return purok;
    }

    public String getGender() {
        return gender;
    }
}
