package com.KGRJJ.kgrjj_android_20192020;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.KGRJJ.kgrjj_android_20192020.Authentication.LoginActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class SplashScreenActivity extends AppCompatActivity {

    private LottieAnimationView mSplashLogo;
    private static int msplashTimeOut=5000;
    private TextView appName;
    private Animation fadein;
    protected FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mSplashLogo = findViewById(R.id.animation);
        appName = findViewById(R.id.appTitle);
        fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_in);
        mSplashLogo.startAnimation(fadein);
        appName.startAnimation(fadein);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        },msplashTimeOut);


    }

}
