package com.rey.itrustapplication.adminform.activities;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayoutMediator;
import com.rey.itrustapplication.AdminDrawerBaseActivity;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.adminform.FragmentStateAdapterClass;
import com.rey.itrustapplication.databinding.ActivityFormBinding;

public class FormActivity extends AdminDrawerBaseActivity {

    ActivityFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Forms");

        FragmentStateAdapterClass fragmentStateAdapterClass = new FragmentStateAdapterClass(this);
        binding.viewPagerFormActivity.setAdapter(fragmentStateAdapterClass);

        new TabLayoutMediator(binding.tabLayoutFormActivity, binding.viewPagerFormActivity, (tab, position) -> {

            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.family);
                    tab.setText("FP Form");
                    break;
                case 1:
                    tab.setIcon(R.drawable.prenatal_care);
                    tab.setText("PC Form");
                    break;
                case 2:
                    tab.setIcon(R.drawable.baby);
                    tab.setText("BE Plan");
                    break;
            }
        }).attach();

    }
}