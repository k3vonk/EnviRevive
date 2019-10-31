package com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.KGRJJ.kgrjj_android_20192020.Authentication.LoginActivity;
import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.Data.Image_Upload;
import com.KGRJJ.kgrjj_android_20192020.EventCreationDialog;
import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventCreation;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class UserProfileActivity extends BaseActivity implements View.OnClickListener {


    private int i, j;
    private int max_points;
    //private ImageView image;
    private static Uri imageUri;
    //END FIREBASE SPECIFIC VARIABLES
    Bitmap thumbnail;
    //START FIREBASE SPECIFIC VARIABLES/
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    //END USER PROFILE VARIABLES
    //USER PROFILE PAGE VARIABLES
    private TextView profile_name, profile_rank, profile_city_country, profile_points;



    /* method that connects to firestore and finds the user with the same ID as the currently signed
        in user. "Username" string is then assigned the value found form the database.
     */
    private ImageView mProfileImage,mCoverPhoto;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(
                "gs://kgrjj-android-2019.appspot.com/images");

        profile_name = findViewById(R.id.profile_username);
        profile_rank = findViewById(R.id.profile_rank);
        profile_city_country = findViewById(R.id.profile_city_country);
        profile_points = findViewById(R.id.profile_points);
        mProfileImage = findViewById(R.id.profile_portrait_image);
        mCoverPhoto = findViewById(R.id.cover);
        user = mAuth.getCurrentUser();
        if (user != null) {
            getFullName(user);
            getRank(user);
            getCityCountry(user);
            getPoints(user);
            getProfileImage(user);
        }
        findViewById(R.id.profile_sign_out).setOnClickListener(this);
        findViewById(R.id.change_image).setOnClickListener(this);

        //findViewById(R.id.CreateEventBTN).setOnClickListener(this);
        mProfileImage.setOnClickListener(this);


    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_user_profile;
    }

    private void getFullName(FirebaseUser user) {


        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        profile_name.setText(String.format("%s %s",
                                documentSnapshot.getString("FName"),
                                documentSnapshot.getString("LName")));
                    }
                });
    }

    private void getRank(FirebaseUser user) {


        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        profile_rank.setText(documentSnapshot.getString("Rank"));
                    }
                });
    }

    private void getCityCountry(FirebaseUser user) {


        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        profile_city_country.setText(String.format("%s,%s",
                                documentSnapshot.getString("City"),
                                documentSnapshot.getString("Country")));
                    }
                });
    }

    protected void getPoints(FirebaseUser user) {


        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //profile_points.setText("0");
                        TextViewAnimation(0, documentSnapshot.getDouble("Points").intValue(), profile_points);
                    }

                });
    }
    private void TextViewAnimation(int curr,int max, TextView points) {


        i = curr;
        j = max;
        new Thread(() -> {
            while(i < j){
                try{
                    Thread.sleep(10);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                //j = max_points;
                points.post(() -> points.setText("" + i));
                i++;
            }
        }).start();


    }

    protected void getProfileImage(FirebaseUser user) {
        StorageReference profileRef = mStorageRef
                .child(user.getUid() + "/profileImage.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri ->
                Glide
                        .with(getApplicationContext())
                        .load(uri)
                        //.apply(RequestOptions.overrideOf(400,400))
                        .apply(RequestOptions.centerCropTransform())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mProfileImage));
//        String path = Environment.getExternalStorageDirectory().toString()+"/"+user.getUid()+"_images";
//
//        File imgFile = new  File(path+user.getUid()+".jpg");
//
//        if(imgFile.exists()) {
//            Glide
//                    .with(getApplicationContext())
//                    .load(imgFile)
//                    .apply(RequestOptions.centerCropTransform())
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(mProfileImage);
//        }
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
            takePhoto(true);
        }
//        if(i == R.id.CreateEventBTN){
//            EventCreationDialog dialog = new EventCreationDialog(UserProfileActivity.this,db,user);
//            dialog.show();
//        }


    }


}
