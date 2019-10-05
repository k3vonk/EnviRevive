package com.KGRJJ.kgrjj_android_20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.KGRJJ.kgrjj_android_20192020.Adapter.WheelSelectionAdapter;
import com.KGRJJ.kgrjj_android_20192020.Data.ImageData;

import java.util.ArrayList;
import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;

public class TestActivityWheel extends AppCompatActivity implements CursorWheelLayout.OnMenuSelectedListener{


    CursorWheelLayout wheel_image;
    List<ImageData> lstImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wheel);

        initViews();

        loadData();

        wheel_image.setOnMenuSelectedListener(this);
    }

    private void loadData(){
        for(int i=0;i<9;i++){
            lstImages = new ArrayList<>();
            lstImages.add(new ImageData(R.drawable.common_full_open_on_phone, "Phone"));
            lstImages.add(new ImageData(R.drawable.common_full_open_on_phone, "Phone"));
            lstImages.add(new ImageData(R.drawable.common_full_open_on_phone, "Phone"));
            lstImages.add(new ImageData(R.drawable.common_full_open_on_phone, "Phone"));
            lstImages.add(new ImageData(R.drawable.common_full_open_on_phone, "Phone"));
            WheelSelectionAdapter  mAdapter = new WheelSelectionAdapter(getBaseContext(), lstImages);
            wheel_image.setAdapter(mAdapter);

        }
    }

    private void initViews(){
        wheel_image = findViewById(R.id.wheel_image);
    }

    @Override
    public void onItemSelected(CursorWheelLayout parent, View view, int pos) {
        if(parent.getId() == R.id.wheel_image)
            Toast.makeText(getBaseContext(), "Selected: " + lstImages.get(pos).imageDescription, Toast.LENGTH_SHORT).show();
    }
}
