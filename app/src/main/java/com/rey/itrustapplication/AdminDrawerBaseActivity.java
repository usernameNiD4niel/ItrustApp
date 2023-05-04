package com.rey.itrustapplication;

import static com.rey.itrustapplication.helperclasses.UtilityClass.setAvailabilityToOnline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.itrustapplication.activity_log.AdminActivityLogs;
import com.rey.itrustapplication.adminform.activities.FormActivity;
import com.rey.itrustapplication.chatfeature.activities.AdminListOfUsers;
import com.rey.itrustapplication.medicine_inventory.admin.InventoryMedicine;
import com.rey.itrustapplication.sessionmanager.AdminSessionManager;

public class AdminDrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    AdminSessionManager adminSessionManager;
    NavigationView navigationView;

    @SuppressLint("InflateParams")
    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_admin_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adminSessionManager = new AdminSessionManager(getApplicationContext());

        navigationView.inflateMenu(R.menu.main_drawer_menu);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.nav_registered_user:
                startActivity(new Intent(AdminDrawerBaseActivity.this, AdminDashboardActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_consultation_schedule:
                startActivity(new Intent(AdminDrawerBaseActivity.this, ConsultationScheduleActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_chatbot_history:
                startActivity(new Intent(AdminDrawerBaseActivity.this, ChatbotHistoryActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_admins:
                startActivity(new Intent(AdminDrawerBaseActivity.this, AdminsActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_passcode:
                if (adminSessionManager.getUsernameSession().equals("Midwife")) {
                    startActivity(new Intent(AdminDrawerBaseActivity.this, PasscodeActivity.class));
                    overridePendingTransition(0,0);
                } else {
                    Toast.makeText(this, "Only authorized person can enter", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_status_request:
                startActivity(new Intent(AdminDrawerBaseActivity.this, AdminConsultationScheduleActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_activity_logs:
                startActivity(new Intent(AdminDrawerBaseActivity.this, AdminActivityLogs.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_video_recommendation:
                startActivity(new Intent(AdminDrawerBaseActivity.this, AdminVideoRecommendation.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_logout:
                loadAlertBox();
                overridePendingTransition(0,0);
                break;
            case R.id.nav_chat:
                if (adminSessionManager.getUsernameSession().equals("Midwife")) {

                    startActivity(new Intent(AdminDrawerBaseActivity.this, AdminListOfUsers.class));
                    overridePendingTransition(0, 0);
                } else {
                    Toast.makeText(this, "Only authorized person can enter", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_starla:
                startActivity(new Intent(AdminDrawerBaseActivity.this, FormActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.medicine_inventory_admin:
                startActivity(new Intent(AdminDrawerBaseActivity.this, InventoryMedicine.class));
                overridePendingTransition(0,0);
                break;
            default:
                return false;
        }

        return true;
    }

    private void loadAlertBox() {

        AdminSessionManager adminSessionManager = new AdminSessionManager(AdminDrawerBaseActivity.this);

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminDrawerBaseActivity.this);
        builder.setTitle(R.string.logout_confirmation);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            dialog.dismiss();
            if (adminSessionManager.getUsernameSession().equals("Midwife")){
                FirebaseDatabase.getInstance().getReference("Admin/Midwife/token").removeValue();
                FirebaseDatabase.getInstance().getReference("Admin/Midwife/availability").setValue("offline");

            }
            adminSessionManager.adminClearSession();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0,0);
        });
        builder.setNegativeButton(R.string.no_prompt, (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            loadAlertBox();
        }
    }

    public void allocateActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }

    @Override
    public void onResume() {
        setAvailabilityToOnline(new AdminSessionManager(getApplicationContext()).getUsernameSession(), "Admin");
        super.onResume();
    }

}