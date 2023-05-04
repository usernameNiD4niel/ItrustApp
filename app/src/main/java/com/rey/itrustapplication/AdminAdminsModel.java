package com.rey.itrustapplication;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AdminAdminsModel {

    String name, username, accountCreated;
    int profile;

    public AdminAdminsModel() {
    }

    public AdminAdminsModel(String name, String username, String accountCreated, int profile) {
        this.name = name;
        this.username = username;
        this.accountCreated = accountCreated;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getAccountCreated() {
        return accountCreated;
    }

    public int getProfile() {
        return profile;
    }
}
