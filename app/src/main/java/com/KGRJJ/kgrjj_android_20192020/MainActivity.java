package com.KGRJJ.kgrjj_android_20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.KGRJJ.kgrjj_android_20192020.Authentication.LoginActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRegisterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRegisterButton = findViewById(R.id.signUpBtn_homeScreen);

        mRegisterButton.setOnClickListener(this);
    }


    private void LaunchRegistration(){
        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.signUpBtn_homeScreen){
            LaunchRegistration();
        }
    }
}
