package com.KGRJJ.kgrjj_android_20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;

public class TestActivityWheel2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wheel2);
    }
    FilterMenuLayout layout = findViewById(R.id.filter_menu);
    FilterMenu menu = new FilterMenu.Builder(getApplicationContext())
            .addItem(R.drawable.common_full_open_on_phone)
            .inflate(R.menu.wheel_menu)     //inflate  menu resource
            .attach(layout)
    .withListener(new FilterMenu.OnMenuChangeListener() {


        @Override
        public void onMenuItemClick(View view, int position) {
        }
        @Override
        public void onMenuCollapse() {
        }
        @Override
        public void onMenuExpand() {
        }
    })
            .build();
}
