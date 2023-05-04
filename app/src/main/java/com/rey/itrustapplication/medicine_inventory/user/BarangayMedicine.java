package com.rey.itrustapplication.medicine_inventory.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.databinding.ActivityBarangayMedicineBinding;

import java.util.ArrayList;

public class BarangayMedicine extends AppCompatActivity {

    ActivityBarangayMedicineBinding binding;

    private ArrayList<BarangayMedicineModel> barangayMedicineModels;

    private BarangayMedicineAdapter barangayMedicineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBarangayMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        barangayMedicineModels = new ArrayList<>();

        populateTheInventoryUser();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        barangayMedicineAdapter = new BarangayMedicineAdapter(barangayMedicineModels, BarangayMedicine.this);

        binding.recyclerViewBarangayMedicine.setHasFixedSize(true);
        binding.recyclerViewBarangayMedicine.setItemViewCacheSize(20);
        binding.recyclerViewBarangayMedicine.setLayoutManager(linearLayoutManager);
        binding.recyclerViewBarangayMedicine.setAdapter(barangayMedicineAdapter);

        Menu menu = binding.userMedicineInventoryToolbar.getMenu();

        binding.backButtonBarangayMedicine.setOnClickListener(view -> onBackPressed());

        MenuItem menuItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setQueryHint("Search Medicine");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<BarangayMedicineModel> arrayList = new ArrayList<>();
                for (BarangayMedicineModel model : barangayMedicineModels) {
                    if (model.getMedicineName().toLowerCase().trim().contains(s.trim().toLowerCase())) {
                        arrayList.add(model);
                    } else if (model.getMedicineDescription().toLowerCase().trim().contains(s.trim().toLowerCase())){
                        arrayList.add(model);
                    }
                }

                if (arrayList.isEmpty()) {
                    Toast.makeText(BarangayMedicine.this, "No Data Found.", Toast.LENGTH_SHORT).show();
                } else {
                    barangayMedicineAdapter.updateTheView(arrayList);
                }
                return true;
            }
        });
    }

    private void populateTheInventoryUser() {

        barangayMedicineModels.add(new BarangayMedicineModel("Aspirin", "Aspirin is a pain reliever and fever reducer that belongs to a class of drugs known as nonsteroidal anti-inflammatory drugs (NSAIDs). It works by inhibiting the production of certain chemicals in the body that cause pain, inflammation, and fever. Aspirin is commonly used to relieve headaches, toothaches, menstrual cramps, and other types of mild to moderate pain.", "10"));
        barangayMedicineModels.add(new BarangayMedicineModel("Lipitor", "Lipitor is a medication used to lower cholesterol levels in the blood. It works by blocking an enzyme in the liver that is responsible for producing cholesterol. Lipitor is used to reduce the risk of heart attack, stroke, and other cardiovascular diseases in people who have high levels of cholesterol.", "3"));
        barangayMedicineModels.add(new BarangayMedicineModel("Ventolin", "Ventolin is a medication used to treat asthma and other breathing disorders. It works by relaxing the muscles in the airways, allowing more air to flow in and out of the lungs. Ventolin is typically taken through an inhaler, and is often used as a \"rescue inhaler\" to quickly relieve symptoms of asthma attacks.", "19"));
        barangayMedicineModels.add(new BarangayMedicineModel("Prozac", "Prozac is a medication used to treat depression, obsessive-compulsive disorder, and other mental health conditions. It works by increasing the levels of certain chemicals in the brain that regulate mood and emotions. Prozac is often prescribed as a long-term treatment, and may take several weeks to begin working.", "22"));
        barangayMedicineModels.add(new BarangayMedicineModel("Tylenol", "Tylenol is a pain reliever and fever reducer that is commonly used to treat mild to moderate pain. Its active ingredient is acetaminophen, which works by reducing the production of prostaglandins, chemicals in the body that cause pain and fever. Tylenol is available in various forms, including tablets, capsules, and liquid formulations. It is often used to relieve headaches, menstrual cramps, and other types of pain that are not relieved by aspirin or other NSAIDs.", "19"));

        barangayMedicineModels.add(new BarangayMedicineModel("Aspirin", "Aspirin is a pain reliever and fever reducer that belongs to a class of drugs known as nonsteroidal anti-inflammatory drugs (NSAIDs). It works by inhibiting the production of certain chemicals in the body that cause pain, inflammation, and fever. Aspirin is commonly used to relieve headaches, toothaches, menstrual cramps, and other types of mild to moderate pain.", "10"));
        barangayMedicineModels.add(new BarangayMedicineModel("Lipitor", "Lipitor is a medication used to lower cholesterol levels in the blood. It works by blocking an enzyme in the liver that is responsible for producing cholesterol. Lipitor is used to reduce the risk of heart attack, stroke, and other cardiovascular diseases in people who have high levels of cholesterol.", "3"));
        barangayMedicineModels.add(new BarangayMedicineModel("Ventolin", "Ventolin is a medication used to treat asthma and other breathing disorders. It works by relaxing the muscles in the airways, allowing more air to flow in and out of the lungs. Ventolin is typically taken through an inhaler, and is often used as a \"rescue inhaler\" to quickly relieve symptoms of asthma attacks.", "19"));
        barangayMedicineModels.add(new BarangayMedicineModel("Prozac", "Prozac is a medication used to treat depression, obsessive-compulsive disorder, and other mental health conditions. It works by increasing the levels of certain chemicals in the brain that regulate mood and emotions. Prozac is often prescribed as a long-term treatment, and may take several weeks to begin working.", "22"));
        barangayMedicineModels.add(new BarangayMedicineModel("Tylenol", "Tylenol is a pain reliever and fever reducer that is commonly used to treat mild to moderate pain. Its active ingredient is acetaminophen, which works by reducing the production of prostaglandins, chemicals in the body that cause pain and fever. Tylenol is available in various forms, including tablets, capsules, and liquid formulations. It is often used to relieve headaches, menstrual cramps, and other types of pain that are not relieved by aspirin or other NSAIDs.", "19"));

        barangayMedicineModels.add(new BarangayMedicineModel("Aspirin", "Aspirin is a pain reliever and fever reducer that belongs to a class of drugs known as nonsteroidal anti-inflammatory drugs (NSAIDs). It works by inhibiting the production of certain chemicals in the body that cause pain, inflammation, and fever. Aspirin is commonly used to relieve headaches, toothaches, menstrual cramps, and other types of mild to moderate pain.", "10"));
        barangayMedicineModels.add(new BarangayMedicineModel("Lipitor", "Lipitor is a medication used to lower cholesterol levels in the blood. It works by blocking an enzyme in the liver that is responsible for producing cholesterol. Lipitor is used to reduce the risk of heart attack, stroke, and other cardiovascular diseases in people who have high levels of cholesterol.", "3"));
        barangayMedicineModels.add(new BarangayMedicineModel("Ventolin", "Ventolin is a medication used to treat asthma and other breathing disorders. It works by relaxing the muscles in the airways, allowing more air to flow in and out of the lungs. Ventolin is typically taken through an inhaler, and is often used as a \"rescue inhaler\" to quickly relieve symptoms of asthma attacks.", "19"));
        barangayMedicineModels.add(new BarangayMedicineModel("Prozac", "Prozac is a medication used to treat depression, obsessive-compulsive disorder, and other mental health conditions. It works by increasing the levels of certain chemicals in the brain that regulate mood and emotions. Prozac is often prescribed as a long-term treatment, and may take several weeks to begin working.", "22"));
        barangayMedicineModels.add(new BarangayMedicineModel("Tylenol", "Tylenol is a pain reliever and fever reducer that is commonly used to treat mild to moderate pain. Its active ingredient is acetaminophen, which works by reducing the production of prostaglandins, chemicals in the body that cause pain and fever. Tylenol is available in various forms, including tablets, capsules, and liquid formulations. It is often used to relieve headaches, menstrual cramps, and other types of pain that are not relieved by aspirin or other NSAIDs.", "19"));


    }
}