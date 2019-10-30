package com.KGRJJ.kgrjj_android_20192020.Wheel;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.KGRJJ.kgrjj_android_20192020.R;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

public class TestActivityWheel2 extends AppCompatActivity {

    CircleMenu circleMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wheel_2);
        circleMenu = findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.icon_menu, R.mipmap.icon_cancel);
        circleMenu.addSubMenu(Color.parseColor("#258CFF"), R.mipmap.icon_home)
                .addSubMenu(Color.parseColor("#30A400"), R.mipmap.icon_search)
                .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.icon_notify)
                .addSubMenu(Color.parseColor("#8A39FF"), R.mipmap.icon_setting)
                .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.icon_gps);

        circleMenu.setOnMenuSelectedListener(i -> {
            switch (i) {
                case 0:
                    Toast.makeText(TestActivityWheel2.this, "Home Button Clicked", Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    Toast.makeText(TestActivityWheel2.this, "Search button Clicked", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(TestActivityWheel2.this, "Notify button Clicked", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(TestActivityWheel2.this, "Settings button Clicked", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(TestActivityWheel2.this, "GPS button Clicked", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        circleMenu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {
            @Override
            public void onMenuOpened() {
                Toast.makeText(TestActivityWheel2.this, "Menu Opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuClosed() {
                Toast.makeText(TestActivityWheel2.this, "Menu Closed", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void onBackPressed() {
        if (circleMenu.isOpened())
            circleMenu.closeMenu();
        else
            finish();
    }


}
