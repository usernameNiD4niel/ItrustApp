package com.rey.itrustapplication.helperclasses;

public class AccessibleUserHelperClass {

    public String fullName, dateAdded, birthday, gender;

    public AccessibleUserHelperClass(String fullName, String dateAdded) {
        this.fullName = fullName;
        this.dateAdded = dateAdded;
    }

    public AccessibleUserHelperClass(String dateAdded, String fullName, String birthday, String gender) {
        this.dateAdded = dateAdded;
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
