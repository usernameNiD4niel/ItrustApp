package com.rey.itrustapplication.sessionmanager;

import android.content.Context;
import android.content.SharedPreferences;


public class SessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private final String FULLNAMEKEY = "FULL_NAME";
    private final String LOGINKEY = "KEY_LOGIN";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("AppKey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setLogin(boolean isLogin) {
        editor.putBoolean(LOGINKEY, isLogin);
        editor.commit();
    }


    public boolean getLogin() {
        return sharedPreferences.getBoolean(LOGINKEY, false);
    }

    public void setFullName(String fullName) {
        editor.putString(FULLNAMEKEY, fullName);
        editor.commit();
    }

    public String getFullName() {return sharedPreferences.getString(FULLNAMEKEY, "");}

    public void signOutUser() {
        editor.clear();
        editor.commit();
    }

    public void setUserToken(String token) {
        editor.putString("TOKEN", token);
        editor.commit();
    }

    public String getToken() {
        return sharedPreferences.getString("TOKEN", "");
    }


    public String getProfileUser() {
        return sharedPreferences.getString("HASH", "");
    }

}
