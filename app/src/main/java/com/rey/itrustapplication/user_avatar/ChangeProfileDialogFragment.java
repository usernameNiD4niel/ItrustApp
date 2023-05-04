package com.rey.itrustapplication.user_avatar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;
import androidx.transition.Slide;
import androidx.transition.Transition;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.itrustapplication.R;
import com.rey.itrustapplication.sessionmanager.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeProfileDialogFragment extends DialogFragment {

    CircleImageView predefinedAvatarProfile1, predefinedAvatarProfile2, predefinedAvatarProfile3, predefinedAvatarProfile4,
    predefinedAvatarProfile5, predefinedAvatarProfile6, predefinedAvatarProfile7, currentProfile;

    Button save, cancel;

    private final Context context;

    private String currentUser;

    private int iconIndicator = 0, selectedImage = 0;
    private final CircleImageView profile;

    public ChangeProfileDialogFragment(Context context, CircleImageView profile) {
        this.context = context;
        this.profile = profile;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);

        View view = inflater.inflate(R.layout.change_profile_dialog_fragment, container, false);

        save = view.findViewById(R.id.save_change_profile);
        cancel = view.findViewById(R.id.cancel_change_profile);

        predefinedAvatarProfile1 = view.findViewById(R.id.predefine_avatar_profile_1);
        predefinedAvatarProfile2 = view.findViewById(R.id.predefine_avatar_profile_2);
        predefinedAvatarProfile3 = view.findViewById(R.id.predefine_avatar_profile_3);
        predefinedAvatarProfile4 = view.findViewById(R.id.predefine_avatar_profile_4);
        predefinedAvatarProfile5 = view.findViewById(R.id.predefine_avatar_profile_5);
        predefinedAvatarProfile6 = view.findViewById(R.id.predefine_avatar_profile_6);
        predefinedAvatarProfile7 = view.findViewById(R.id.predefine_avatar_profile_7);

        currentProfile = view.findViewById(R.id.circle_image_view);

        currentUser = new SessionManager(context).getFullName();

        loadPreviousProfile();

        eventsInsidePopup();
        return view;
    }

    private void loadPreviousProfile() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String drawableHash = snapshot.child(currentUser).getValue(String.class);
                    if (drawableHash == null) {
                        return;
                    }

                    if (R.drawable.user_avatar_1 == Integer.parseInt(drawableHash)) {
                        return;
                    }

                    Drawable vectorDrawable = AppCompatResources.getDrawable(context, Integer.parseInt(drawableHash));
                    currentProfile.setImageDrawable(vectorDrawable);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Cannot load your profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eventsInsidePopup() {
        cancel.setOnClickListener(view -> dismiss());
        save.setOnClickListener(view -> {
            if (selectedImage != 0) {
                storeImagesToStorage(selectedImage);
                Drawable drawable = AppCompatResources.getDrawable(view.getContext(), selectedImage);
                profile.setImageDrawable(drawable);
            } else {
                Toast.makeText(getContext(), "No Image Selected Yet", Toast.LENGTH_SHORT).show();
            }
        });

        predefinedAvatarProfile1.setOnClickListener(view -> {
            conditionActiveProfileRemove();
            iconIndicator = 1;
            selectedImage = R.drawable.avatar__1_;
            setSelectedProfileActive(predefinedAvatarProfile1);
        });

        predefinedAvatarProfile2.setOnClickListener(view -> {
            conditionActiveProfileRemove();
            iconIndicator = 2;
            selectedImage = R.drawable.avatar__2_;
            setSelectedProfileActive(predefinedAvatarProfile2);
        });

        predefinedAvatarProfile3.setOnClickListener(view -> {
            conditionActiveProfileRemove();
            iconIndicator = 3;
            selectedImage = R.drawable.avatar__3_;
            setSelectedProfileActive(predefinedAvatarProfile3);
        });

        predefinedAvatarProfile4.setOnClickListener(view -> {
            conditionActiveProfileRemove();
            iconIndicator = 4;
            selectedImage = R.drawable.avatar__4_;
            setSelectedProfileActive(predefinedAvatarProfile4);
        });

        predefinedAvatarProfile5.setOnClickListener(view -> {
            conditionActiveProfileRemove();
            iconIndicator = 5;
            selectedImage = R.drawable.avatar__5_;
            setSelectedProfileActive(predefinedAvatarProfile5);
        });

        predefinedAvatarProfile6.setOnClickListener(view -> {
            conditionActiveProfileRemove();
            iconIndicator = 6;
            selectedImage = R.drawable.avatar__6_;
            setSelectedProfileActive(predefinedAvatarProfile6);
        });

        predefinedAvatarProfile7.setOnClickListener(view -> {
            conditionActiveProfileRemove();
            iconIndicator = 7;
            selectedImage = R.drawable.avatar__7_;
            setSelectedProfileActive(predefinedAvatarProfile7);
        });

    }

    private void storeImagesToStorage(int drawablePath) {

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("UserProfile");
        databaseRef.child(currentUser).setValue(drawablePath + "")
                .addOnSuccessListener(success -> dismiss())
                .addOnFailureListener(failure -> Toast.makeText(context, "Error adding profile", Toast.LENGTH_SHORT).show());

    }

    private void conditionActiveProfileRemove() {
        switch (iconIndicator) {
            case 1:
                removeActiveProfileIndicator(predefinedAvatarProfile1);
                break;
            case 2:
                removeActiveProfileIndicator(predefinedAvatarProfile2);
                break;
            case 3:
                removeActiveProfileIndicator(predefinedAvatarProfile3);
                break;
            case 4:
                removeActiveProfileIndicator(predefinedAvatarProfile4);
                break;
            case 5:
                removeActiveProfileIndicator(predefinedAvatarProfile5);
                break;
            case 6:
                removeActiveProfileIndicator(predefinedAvatarProfile6);
                break;
            case 7:
                removeActiveProfileIndicator(predefinedAvatarProfile7);
                break;
        }
    }

    private void removeActiveProfileIndicator(CircleImageView circleImageView) {
        circleImageView.setBorderColor(Color.TRANSPARENT);
        circleImageView.setBorderWidth(0);
    }

    private void setSelectedProfileActive(CircleImageView circleImageView) {
        circleImageView.setBorderColor(getResources().getColor(R.color.main_color));
        circleImageView.setBorderWidth(5);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Dialog);
        Transition enterTransition = new Slide(Gravity.BOTTOM);
        enterTransition.setDuration(400);
        setEnterTransition(enterTransition);
        Transition exitTransition = new Slide(Gravity.BOTTOM);
        exitTransition.setDuration(400);
        setExitTransition(exitTransition);
    }

}
