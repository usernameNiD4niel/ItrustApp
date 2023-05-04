package com.rey.itrustapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.View;

import com.rey.itrustapplication.databinding.ActivityUserMoreHelpFragmentBinding;

public class UserMoreHelpFragment extends AppCompatActivity {

    ActivityUserMoreHelpFragmentBinding activityUserMoreHelpFragmentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserMoreHelpFragmentBinding = ActivityUserMoreHelpFragmentBinding.inflate(getLayoutInflater());
        setContentView(activityUserMoreHelpFragmentBinding.getRoot());

        activityUserMoreHelpFragmentBinding.layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        activityUserMoreHelpFragmentBinding.layout2.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        activityUserMoreHelpFragmentBinding.layout3.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        activityUserMoreHelpFragmentBinding.layout4.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        activityUserMoreHelpFragmentBinding.layout5.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        activityUserMoreHelpFragmentBinding.cardViewContainer.setOnClickListener(view -> {
            int visibility = (activityUserMoreHelpFragmentBinding.answerOne.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

            TransitionManager.beginDelayedTransition(activityUserMoreHelpFragmentBinding.layout, new AutoTransition());

            activityUserMoreHelpFragmentBinding.answerOne.setVisibility(visibility);
        });

        activityUserMoreHelpFragmentBinding.cardViewContainer2.setOnClickListener(view -> {
            int visibility = (activityUserMoreHelpFragmentBinding.answerTwo.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

            TransitionManager.beginDelayedTransition(activityUserMoreHelpFragmentBinding.layout2, new AutoTransition());

            activityUserMoreHelpFragmentBinding.answerTwo.setVisibility(visibility);
        });

        activityUserMoreHelpFragmentBinding.cardViewContainer3.setOnClickListener(view -> {
            int visibility = (activityUserMoreHelpFragmentBinding.answerThree.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

            TransitionManager.beginDelayedTransition(activityUserMoreHelpFragmentBinding.layout3, new AutoTransition());

            activityUserMoreHelpFragmentBinding.answerThree.setVisibility(visibility);
        });

        activityUserMoreHelpFragmentBinding.cardViewContainer4.setOnClickListener(view -> {
            int visibility = (activityUserMoreHelpFragmentBinding.answerFour.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

            TransitionManager.beginDelayedTransition(activityUserMoreHelpFragmentBinding.layout4, new AutoTransition());

            activityUserMoreHelpFragmentBinding.answerFour.setVisibility(visibility);
        });

        activityUserMoreHelpFragmentBinding.cardViewContainer5.setOnClickListener(view -> {
            int visibility = (activityUserMoreHelpFragmentBinding.answerFive.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

            TransitionManager.beginDelayedTransition(activityUserMoreHelpFragmentBinding.layout5, new AutoTransition());

            activityUserMoreHelpFragmentBinding.answerFive.setVisibility(visibility);
        });

        activityUserMoreHelpFragmentBinding.backbuttonHelpApp.setOnClickListener(view -> finish());
    }


}