package com.rey.itrustapplication.adminform;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rey.itrustapplication.adminform.fragments.BirthEmergencyPlanFragment;
import com.rey.itrustapplication.adminform.fragments.FamilyPlanningFragment;
import com.rey.itrustapplication.adminform.fragments.PrenatalCareFragment;

public class FragmentStateAdapterClass extends FragmentStateAdapter {

    public FragmentStateAdapterClass(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FamilyPlanningFragment();
            case 1:
                return new PrenatalCareFragment();
        }
        return new BirthEmergencyPlanFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
