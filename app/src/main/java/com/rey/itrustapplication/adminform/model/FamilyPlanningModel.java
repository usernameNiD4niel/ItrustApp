package com.rey.itrustapplication.adminform.model;

import java.io.Serializable;

public class FamilyPlanningModel implements Serializable {

    private String clientId, philId, nhts, fourPs, givenName, lastName, middleName, age, dateOfBirth, educAttain, occupation;

    public FamilyPlanningModel() {

    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setPhilId(String philId) {
        this.philId = philId;
    }

    public void setNhts(String nhts) {
        this.nhts = nhts;
    }

    public void setFourPs(String fourPs) {
        this.fourPs = fourPs;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEducAttain(String educAttain) {
        this.educAttain = educAttain;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getClientId() {
        return clientId;
    }

    public String getPhilId() {
        return philId;
    }

    public String getNhts() {
        return nhts;
    }

    public String getFourPs() {
        return fourPs;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getAge() {
        return age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEducAttain() {
        return educAttain;
    }

    public String getOccupation() {
        return occupation;
    }
}
