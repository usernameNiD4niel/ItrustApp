package com.rey.itrustapplication.sessionmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessaging;

public class AdminSessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AdminSessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("AdminSession", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setAdminLogin(boolean isLogin) {
        editor.putBoolean("LOGGED_IN", isLogin);
        editor.commit();
    }

    public boolean getAdminLogin() {
        return sharedPreferences.getBoolean("LOGGED_IN", false);
    }

    public void setUsernameSession(String userName) {
        editor.putString("ADMIN_USER", userName);
        editor.commit();
    }

    public String getUsernameSession() {
        return sharedPreferences.getString("ADMIN_USER", "");
    }

    public void adminClearSession() {
        editor.clear();
        editor.commit();
    }

    public boolean isDoh() {
        return sharedPreferences.getBoolean("ADMIN_DOH", false);
    }

    public void setIsDoh(boolean isDoh) {
        editor.putBoolean("ADMIN_DOH", isDoh);
    }

}
