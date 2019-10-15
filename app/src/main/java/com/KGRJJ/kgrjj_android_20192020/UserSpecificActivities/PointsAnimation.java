package com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.KGRJJ.kgrjj_android_20192020.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PointsAnimation extends AppCompatActivity {

    //private FirebaseUser user;
    //String max_points;
    private int i, j;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        TextView points = findViewById(R.id.profile_points);

        //I NEED TO FIX THIS TO SET THE MAXIMUM POINTS TO BE THE CURRENT USER'S POINTS
        TextViewAnimation(0, "100", points);
   }

    private void TextViewAnimation(int curr, String user_points, TextView points) {
        i = curr;
        j = Integer.parseInt(user_points);

        new Thread(() -> {
            while(i < j){
                try{
                    Thread.sleep(10);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                points.post(() -> points.setText("" + i));
                i++;
            }
        }).start();

    }
}
