package com.rey.itrustapplication.helperclasses;

public class UserMagpakonsultaHelperClass {

    String fullName, status, day, dateRequested, amOrPm;
    int hour, minute;

    public UserMagpakonsultaHelperClass(String fullName, String day, int hour, int minute, String status, String dateRequested, String amOrPm) {
        this.fullName = fullName;
        this.hour = hour;
        this.minute = minute;
        this.status = status;
        this.day = day;
        this.dateRequested = dateRequested;
        this.amOrPm = amOrPm;
    }

    public String getAmOrPm() {
        return amOrPm;
    }

    public String getDateRequested() {
        return dateRequested;
    }

    public String getDay() {
        return day;
    }

    public String getFullName() {
        return fullName;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getStatus() {
        return status;
    }
}
