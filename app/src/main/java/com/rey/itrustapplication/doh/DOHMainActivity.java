package com.rey.itrustapplication.doh;

import android.os.Bundle;

import com.rey.itrustapplication.AdminDrawerBaseActivity;
import com.rey.itrustapplication.databinding.ActivityDohmainBinding;

public class DOHMainActivity extends AdminDrawerBaseActivity {

    ActivityDohmainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDohmainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Dashboard");
    }
}