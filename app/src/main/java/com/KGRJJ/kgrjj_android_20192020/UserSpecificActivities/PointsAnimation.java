package com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.KGRJJ.kgrjj_android_20192020.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PointsAnimation extends AppCompatActivity {


    //String max_points;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);



        //I NEED TO FIX THIS TO SET THE MAXIMUM POINTS TO BE THE CURRENT USER'S POINTS

   }


}
