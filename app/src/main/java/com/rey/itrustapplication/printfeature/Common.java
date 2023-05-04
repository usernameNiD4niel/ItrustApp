package com.rey.itrustapplication.printfeature;

import android.content.Context;
import android.widget.Toast;

import com.rey.itrustapplication.R;

import java.io.File;

public class Common {

    public static String getAppPath(Context context) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator
        );

        if (!dir.exists()) if (dir.mkdir()) Toast.makeText(context, "New file was created", Toast.LENGTH_SHORT).show();

        return dir.getPath() + File.separator;
    }

}
