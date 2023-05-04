package com.rey.itrustapplication.adminform.popup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.rey.itrustapplication.R;

public class AddNewRecordPrompt {

    public static void showPopUp(Context context, String message, String t1, String t2, DialogInterface.OnClickListener positiveButton, DialogInterface.OnClickListener negativeButton) {

        AlertDialog alertDialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Adding Record");
        builder.setIcon(R.drawable.record);
        builder.setMessage(message);
        builder.setPositiveButton(t1, positiveButton);

        builder.setNegativeButton(t2, negativeButton);

        alertDialog = builder.create();
        alertDialog.show();

    }

}
