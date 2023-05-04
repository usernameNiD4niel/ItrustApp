package com.rey.itrustapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.rey.itrustapplication.database.UserDatabase;
import com.rey.itrustapplication.doh.DOHMainActivity;
import com.rey.itrustapplication.helperclasses.UtilityClass;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Button getStartedBtn, adminButton;
    FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, Object> remoteObject = new HashMap<>();
        remoteObject.put(UtilityClass.VERSION, BuildConfig.VERSION_CODE);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();

        firebaseRemoteConfig.setConfigSettingsAsync(firebaseRemoteConfigSettings);
        firebaseRemoteConfig.setDefaultsAsync(remoteObject);

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(runnable -> {
            if (runnable.isSuccessful()) {
                displayDialogBox();
            }
        });

        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getStartedBtn = findViewById(R.id.getStartedBtn);
        adminButton = findViewById(R.id.adminButton);

        adminButton.setOnClickListener(view -> adminAction());

        getStartedBtn.setOnClickListener(view -> loadNewActivity());

    }

    private void displayDialogBox() {

        long version_code = firebaseRemoteConfig.getLong(UtilityClass.VERSION);
        String version_name = firebaseRemoteConfig.getString(UtilityClass.VERSION_NAME);

        if (version_code > BuildConfig.VERSION_CODE && !version_name.equals(BuildConfig.VERSION_NAME)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Version: " + version_name +"\n\n" + firebaseRemoteConfig.getString(UtilityClass.WHATS_NEW))
                    .setCancelable(false)
                    .setIcon(AppCompatResources.getDrawable(getApplicationContext(), R.mipmap.ic_launcher_round))
                    .setTitle(firebaseRemoteConfig.getString(UtilityClass.UPDATE_TITLE))
                    .setPositiveButton("Update", (dialog, id) -> {
                        // Open a browser or show a custom dialog to direct the user to download the latest version of the app
                        downloadTheUpdatedCode();
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @SuppressLint("Range")
    private void downloadTheUpdatedCode() {

        final String apk_name = firebaseRemoteConfig.getString(UtilityClass.APK_NAME);
        String apkUrl = firebaseRemoteConfig.getString(UtilityClass.APK_URL);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(firebaseRemoteConfig.getString(UtilityClass.UPDATE_TITLE));
        request.setDescription(firebaseRemoteConfig.getString(UtilityClass.WHATS_NEW));
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "itrust.apk");
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, apk_name);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(firebaseRemoteConfig.getString(UtilityClass.UPDATE_TITLE));
        progressDialog.setMessage(firebaseRemoteConfig.getString(UtilityClass.WHATS_NEW));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.show();


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long receivedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (receivedDownloadId == downloadId) {

                    // Prompt the user to install the downloaded APK file
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);

                    Uri apkUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",
                            new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apk_name));

                    progressDialog.dismiss();

                    installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(installIntent);
                    Toast.makeText(context, "You have successfully updated the App.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        new Thread(() -> {
            boolean downloading = true;
            while (downloading) {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadId);

                Cursor cursor = downloadManager.query(q);
                cursor.moveToFirst();

                @SuppressLint("Range") int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                @SuppressLint("Range") int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;
                }

                final int progress = (int) ((bytes_downloaded * 100L) / bytes_total);
                runOnUiThread(() -> progressDialog.setProgress(progress));

                cursor.close();
            }
        }).start();

    }

    private void loadNewActivity() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegularUsers");
        final SessionManager sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.getLogin()) {
            new UserDatabase(getApplicationContext(), databaseReference).fetchUserCode(this);
        } else {
            Intent loginSignUpActivity = new Intent(MainActivity.this,LoginSignupActivity.class);
            startActivity(loginSignUpActivity);
        }


    }

    private void adminAction() {
        final AdminSessionManager adminSessionManager = new AdminSessionManager(getApplicationContext());
        if (adminSessionManager.getAdminLogin()) {
            startActivity(new Intent(this, AdminDashboardActivity.class));
            finish();
            return;
        } else if (adminSessionManager.isDoh()) {
            startActivity(new Intent(this, DOHMainActivity.class));
            finish();
            return;
        }
        startActivity(new Intent(getApplicationContext(), AdminLoginActivity.class));
    }

}




