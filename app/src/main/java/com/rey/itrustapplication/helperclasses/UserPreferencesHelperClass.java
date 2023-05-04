package com.rey.itrustapplication.helperclasses;

public class UserPreferencesHelperClass {

    public String answer_one;
    public String answer_two;
    public String answer_three;
    public String answer_four;
    public String answer_five;
    
    public UserPreferencesHelperClass(){}

    public UserPreferencesHelperClass(String answer_one, String answer_two, String answer_three, String answer_four, String answer_five) {
        this.answer_one = answer_one;
        this.answer_two = answer_two;
        this.answer_three = answer_three;
        this.answer_four = answer_four;
        this.answer_five = answer_five;
    }

    public String getAnswer_one() {
        return answer_one;
    }

    public String getAnswer_two() {
        return answer_two;
    }

    public String getAnswer_three() {
        return answer_three;
    }

    public String getAnswer_four() {
        return answer_four;
    }

    public String getAnswer_five() {
        return answer_five;
    }
}
