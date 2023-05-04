package com.rey.itrustapplication.helperclasses;

public class AdminCreateAccount {

    private String fullName;
    private String homeAddress;
    private String username;
    private String password;
    private String accountCreated;
    private String token;

    public AdminCreateAccount() {}

    public AdminCreateAccount(String fullName, String homeAddress, String username, String password, String accountCreated, String token) {
        this.fullName = fullName;
        this.homeAddress = homeAddress;
        this.username = username;
        this.password = password;
        this.accountCreated = accountCreated;
        this.token = token;
    }

    public String getFullName() {
        return fullName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getAccountCreated() {
        return accountCreated;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
