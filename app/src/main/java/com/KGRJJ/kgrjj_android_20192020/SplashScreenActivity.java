package com.KGRJJ.kgrjj_android_20192020;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.KGRJJ.kgrjj_android_20192020.Authentication.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView mSplashLogo;
    private static int msplashTimeOut=5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //mSplashLogo= findViewById(R.id.logo);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        },msplashTimeOut);

        //Animation mAnim = AnimationUtils.loadAnimation(this, R.anim.splashscreenanimation);
        //mSplashLogo.startAnimation(mAnim);
    }
}
