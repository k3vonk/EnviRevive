package com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.Authentication.LoginActivity;
import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class UserProfileActivity extends BaseActivity implements View.OnClickListener {


    //private FirebaseAuth mAuth;//
    //END USER PROFILE VARIABLES
    //USER PROFILE PAGE VARIABLES
    private TextView profile_name, profile_rank, profile_city_country, profile_points;
    protected  ImageView mProfileImage;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                "gs://kgrjj-android-2019.appspot.com/images");

        profile_name = findViewById(R.id.profile_username);
        profile_rank = findViewById(R.id.profile_rank);
        profile_city_country = findViewById(R.id.profile_city_country);
        profile_points = findViewById(R.id.profile_points);
        /* method that connects to firestore and finds the user with the same ID as the currently signed
        in user. "Username" string is then assigned the value found form the database.
     */
        mProfileImage = findViewById(R.id.profile_portrait_image);

        user = mAuth.getCurrentUser();
        getUserData(user);
        getRegisteredEvents(user);
        profile_name.setText(fullname);
        profile_rank.setText(Rank);
        profile_city_country.setText(Country);
        profile_points.setText(Points);
        //Log.i("TESTING","PROFILEIMAGE CODE"+profileImage.getDensity());
        mProfileImage.setImageBitmap(profileImage);
        findViewById(R.id.profile_sign_out).setOnClickListener(this);
        findViewById(R.id.change_image).setOnClickListener(this);
        Glide.with(getApplicationContext())
                .load(profileImage)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(mProfileImage);
        //findViewById(R.id.CreateEventBTN).setOnClickListener(this);

        mProfileImage.setOnClickListener(this);
        recyclerView = findViewById(R.id.profileRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_user_profile;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.profile_sign_out) {
            mAuth.signOut();
            Intent myIntent = new Intent(this, LoginActivity.class);
            Toast.makeText(this, "Starting main activity", Toast.LENGTH_SHORT).show();
            startActivity(myIntent);
        }
        if (i == R.id.change_image) {
            int REQUEST_CODE = 1;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            takePhoto(true,false);
        }
    }


}
