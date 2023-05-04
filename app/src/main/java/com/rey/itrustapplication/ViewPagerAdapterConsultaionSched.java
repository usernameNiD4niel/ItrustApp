package com.rey.itrustapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapterConsultaionSched extends FragmentStateAdapter {

    public ViewPagerAdapterConsultaionSched(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AdminConsultationPendingFragment();
            case 1:
                return new AdminConsultationApprovedFragment();
            case 2:
                return new AdminConsultationDeclinedFragment();
        }
        return new AdminConsultationPendingFragment();
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}