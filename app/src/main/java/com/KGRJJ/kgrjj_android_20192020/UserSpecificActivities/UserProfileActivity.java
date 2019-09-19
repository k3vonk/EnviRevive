package com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.KGRJJ.kgrjj_android_20192020.MainActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth mAuth;

    private TextView profile_name;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profile_name = findViewById(R.id.profile_username);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        profile_name.setText(user.getUid());
        findViewById(R.id.SignOutBtn_profile).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.SignOutBtn_profile) {
            mAuth.signOut();
            Intent myIntent = new Intent(this, MainActivity.class);
            Toast.makeText(this,"Starting main activity",Toast.LENGTH_SHORT);
            startActivity(myIntent);
        }
    }

}
