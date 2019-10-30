package com.KGRJJ.kgrjj_android_20192020;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.transition.*;
import android.transition.TransitionInflater;
import android.widget.Toast;

import com.KGRJJ.kgrjj_android_20192020.Authentication.RegistrationActivity;
import com.KGRJJ.kgrjj_android_20192020.Wheel.TestActivityWheel3;
import com.cleveroad.sy.cyclemenuwidget.CycleMenuWidget;
import com.cleveroad.sy.cyclemenuwidget.OnMenuItemClickListener;
import com.cleveroad.sy.cyclemenuwidget.OnStateChangedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseActivity extends AppCompatActivity {

    protected CycleMenuWidget cycleMenuWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceID());

        FirebaseAuth.AuthStateListener mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            assert user != null;
            Toast.makeText(getApplicationContext(), "Signed in user => " + user.getUid(), Toast.LENGTH_SHORT)
                    .show();
        };
        cycleMenuWidget = findViewById(R.id.itemCycleMenuWidget);
        cycleMenuWidget.setMenuRes(R.menu.wheel_menu);


        cycleMenuWidget.setStateChangeListener(
                new OnStateChangedListener() {
                    @Override
                    public void onStateChanged(CycleMenuWidget.STATE state) {

                    }

                    @Override
                    public void onOpenComplete() {

                    }

                    @Override
                    public void onCloseComplete() {

                    }
                }
        );

        cycleMenuWidget.setOnMenuItemClickListener(
                new OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(View view, int itemPosition) {
                        switch (itemPosition) {
                            case 0:
                                Intent myIntentProfile = new Intent(getApplicationContext(), RegistrationActivity.class);
                                startActivity(myIntentProfile);
                                break;
                            case 1:
                                Toast.makeText(getApplicationContext(), "Profile button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getApplicationContext(), "Search button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(getApplicationContext(), "Plus button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(getApplicationContext(), "Profile2 button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onMenuItemLongClick(View view, int itemPosition) {
                        switch (itemPosition) {
                            case 0:
                                Toast.makeText(getApplicationContext(), "Home Button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getApplicationContext(), "Profile button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getApplicationContext(), "Search button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(getApplicationContext(), "Plus button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(getApplicationContext(), "Profile2 button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }
        );



        cycleMenuWidget.setStateSaveListener(
                (itemPosition, lastItemAngleShift) -> {

                }
        );

    }
    protected abstract int getLayoutResourceID();

    }
    private void setupWindowAnimations(){
        Transition slide = TransitionInflater
                .from(getApplicationContext())
                .inflateTransition(R.transition.activity_fade);
        getWindow().setEnterTransition(slide);
        getWindow().setExitTransition(slide);
    }
}
