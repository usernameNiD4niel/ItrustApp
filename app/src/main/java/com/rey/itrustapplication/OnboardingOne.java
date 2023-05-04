package com.rey.itrustapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.rey.itrustapplication.databinding.ActivityOnboardingOneBinding;

import android.view.View;
import android.view.WindowManager;

public class OnboardingOne extends AppCompatActivity implements View.OnClickListener {

    ActivityOnboardingOneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingOneBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        binding.menstruationBtnOnboarding.setOnClickListener(this);
        binding.menopauseBtnOnboarding.setOnClickListener(this);
        binding.contraceptionBtnOnboarding.setOnClickListener(this);
        binding.fertilityBtnOnboarding.setOnClickListener(this);
        binding.postpartumBtnOnboarding.setOnClickListener(this);
        binding.laborBtnOnboarding.setOnClickListener(this);
        binding.sexualBtnOnboarding.setOnClickListener(this);
        binding.reproductiveRightsBtnOnboarding.setOnClickListener(this);
        binding.gynecologicalBtnOnboarding.setOnClickListener(this);
        binding.familyPlanningBtnOnboarding.setOnClickListener(this);
        binding.nutritionBtnOnboarding.setOnClickListener(this);
        binding.exerciseBtnOnboarding.setOnClickListener(this);

        binding.watchVideosOnboarding.setOnClickListener(view -> {
            startActivity(new Intent(OnboardingOne.this, MainUserUi.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding.menstruationBtnOnboarding.setOnClickListener(null);
        binding.menopauseBtnOnboarding.setOnClickListener(null);
        binding.contraceptionBtnOnboarding.setOnClickListener(null);
        binding.fertilityBtnOnboarding.setOnClickListener(null);
        binding.postpartumBtnOnboarding.setOnClickListener(null);
        binding.laborBtnOnboarding.setOnClickListener(null);
        binding.sexualBtnOnboarding.setOnClickListener(null);
        binding.reproductiveRightsBtnOnboarding.setOnClickListener(null);
        binding.gynecologicalBtnOnboarding.setOnClickListener(null);
        binding.familyPlanningBtnOnboarding.setOnClickListener(null);
        binding.nutritionBtnOnboarding.setOnClickListener(null);
        binding.exerciseBtnOnboarding.setOnClickListener(null);

        binding.watchVideosOnboarding.setOnClickListener(null);
        binding = null;
    }

    @Override
    public void onClick(View view) {

        AppCompatButton button = (AppCompatButton) view;

        Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.normal_state_btn_rounded);

        if (button.getBackground() == drawable) {
            Drawable drawable2 = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_button_bg);
            button.setBackground(drawable2);
            button.setTextColor(getResources().getColor(R.color.main_color));
            return;
        }

        button.setBackground(drawable);
        button.setTextColor(getResources().getColor(R.color.white));
    }
}