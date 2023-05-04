package com.rey.itrustapplication.helperclasses;

public class UserCreateAccount {

    String fullName, purok, phoneNumber, gender, password, token, birthday;

    public UserCreateAccount(){}

    public UserCreateAccount(String fullName, String birthday, String purok, String phoneNumber, String gender, String password, String token) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.purok = purok;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.password = password;
        this.token = token;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPurok() {
        return purok;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }
}
