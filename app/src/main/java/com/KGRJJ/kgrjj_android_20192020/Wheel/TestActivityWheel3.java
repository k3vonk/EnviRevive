package com.KGRJJ.kgrjj_android_20192020.Wheel;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;
import com.KGRJJ.kgrjj_android_20192020.Authentication.RegistrationActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.cleveroad.sy.cyclemenuwidget.CycleMenuWidget;
import com.cleveroad.sy.cyclemenuwidget.OnMenuItemClickListener;
import com.cleveroad.sy.cyclemenuwidget.OnStateChangedListener;

/**
 * Final prototype for application navigation
 * Reference: https://github.com/Cleveroad/CycleMenu
 */
public class TestActivityWheel3 extends AppCompatActivity {

    CycleMenuWidget cycleMenuWidget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wheel_3);
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
                                Toast.makeText(TestActivityWheel3.this, "Profile button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(TestActivityWheel3.this, "Search button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(TestActivityWheel3.this, "Plus button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(TestActivityWheel3.this, "Profile2 button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onMenuItemLongClick(View view, int itemPosition) {
                        switch (itemPosition) {
                            case 0:
                                Toast.makeText(TestActivityWheel3.this, "Home Button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(TestActivityWheel3.this, "Profile button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(TestActivityWheel3.this, "Search button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(TestActivityWheel3.this, "Plus button Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(TestActivityWheel3.this, "Profile2 button Clicked", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wheel_menu, menu);
        return true;
    }
}
