package com.KGRJJ.kgrjj_android_20192020.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.KGRJJ.kgrjj_android_20192020.MainActivity;
import com.KGRJJ.kgrjj_android_20192020.R;

public class RegistrationActivity extends AppCompatActivity {


    private Button mHomeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mHomeButton = findViewById(R.id.homeBtn_registerScreen);

        mHomeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                launchHomeScreen();

            }
        });
    }

    private void launchHomeScreen(){
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
