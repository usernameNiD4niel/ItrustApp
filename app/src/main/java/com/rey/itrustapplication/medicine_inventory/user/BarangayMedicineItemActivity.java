package com.rey.itrustapplication.medicine_inventory.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.itrustapplication.R;

public class BarangayMedicineItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barangay_medicine_item);

        final String medicineName = getIntent().getStringExtra("medicine_name");
        final String medicineDescription = getIntent().getStringExtra("medicine_description");
        final String medicine_stock = getIntent().getStringExtra("medicine_stock");

        findViewById(R.id.back_button_barangay_medicine_item).setOnClickListener(view -> onBackPressed());

        TextView name = findViewById(R.id.medicine_name_brgy_item);
        name.setText(medicineName);

        TextView stock = findViewById(R.id.stock_text_bgry_item);
        final String stockText = "Stock: " + medicine_stock;
        stock.setText(stockText);

        TextView description = findViewById(R.id.description_text_content_bgry_medicine_item);
        description.setText(medicineDescription);

        ImageView imageView = findViewById(R.id.logo_brgy_medicine_item);

        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // Remove the listener to prevent infinite loops
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                // Start the transition
                startPostponedEnterTransition();
                return true;
            }
        });
    }
}