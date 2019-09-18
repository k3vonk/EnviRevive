package com.KGRJJ.kgrjj_android_20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.KGRJJ.kgrjj_android_20192020.Authentication.RegistrationActivity;

public class MainActivity extends AppCompatActivity {

    private Button mRegisterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRegisterButton = findViewById(R.id.signUpBtn_homeScreen);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                LaunchRegisteration();
            }
        });
    }


    private void LaunchRegisteration(){
        Intent myIntent = new Intent(this, RegistrationActivity.class);
        startActivity(myIntent);
    }
}
