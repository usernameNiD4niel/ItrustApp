package com.rey.itrustapplication.helperclasses;

public class ListOfUserHelperClass {

    String fullName, purok, numberOfChats, sender;

    public ListOfUserHelperClass() {
    }

    public ListOfUserHelperClass(String fullName, String purok, String numberOfChats, String sender) {
        this.fullName = fullName;
        this.purok = purok;
        this.numberOfChats = numberOfChats;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPurok() {
        return purok;
    }

    public String getNumberOfChats() {
        return numberOfChats;
    }
}
