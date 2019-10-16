package com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;

import com.KGRJJ.kgrjj_android_20192020.MainActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;


public class UserProfileActivity extends BaseActivity implements View.OnClickListener{

    //START FIREBASE SPECIFIC VARIABLES/
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    //END FIREBASE SPECIFIC VARIABLES

    //USER PROFILE PAGE VARIABLES
    private TextView profile_name, profile_rank, profile_city_country, profile_points;
    private ImageView mProfileImage;
    private ImageView image;

    private FirebaseUser user;
    private Uri mProfileImageURI = null;
    //END USER PROFILE VARIABLES

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        profile_name = findViewById(R.id.profile_username);
        profile_rank = findViewById(R.id.profile_rank);
        profile_city_country = findViewById(R.id.profile_city_country);
        profile_points = findViewById(R.id.profile_points);
        mProfileImage = findViewById(R.id.profile_portrait_image);
        user = mAuth.getCurrentUser();
        getFullName(user);
        getRank(user);
        getCityCountry(user);
        getPoints(user);

        findViewById(R.id.profile_sign_out).setOnClickListener(this);
        findViewById(R.id.changeImage).setOnClickListener(this);
        mProfileImage.setOnClickListener(this);



    }


    /* method that connects to firestore and finds the user with the same ID as the currently signed
        in user. "Username" string is then assigned the value found form the database.
     */

    private void getFullName(FirebaseUser user) {


        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                       profile_name.setText(String.format("%s %s",
                               documentSnapshot.getString("FName"),
                               documentSnapshot.getString("LName")));
                    }
                });
    }
    private void getRank(FirebaseUser user) {


        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        profile_rank.setText(documentSnapshot.getString("Rank"));
                    }
                });
    }
    private void getCityCountry(FirebaseUser user) {


        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        profile_city_country.setText(String.format("%s,%s",
                                documentSnapshot.getString("City"),
                                documentSnapshot.getString("Country")));
                    }
                });
    }
    protected void getPoints(FirebaseUser user) {


        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        profile_points.setText(documentSnapshot.getDouble("Points").toString());
                    }
                });
    }
    protected static final int CAPTURE_IMAGE_ATIVITY_REQUEST_CODE = 0;
    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mProfileImageURI = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName(),
                new File(Environment.getExternalStorageDirectory(),
                "fname_"+ System.currentTimeMillis() +".jpg"));

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mProfileImageURI);
        startActivityForResult(intent,CAPTURE_IMAGE_ATIVITY_REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(resultCode==RESULT_OK){
            if(requestCode == CAPTURE_IMAGE_ATIVITY_REQUEST_CODE){
                Bundle extras = data.getExtras();
                Log.e("URI",mProfileImageURI.toString());
                Bitmap bmp = (Bitmap) extras.get("data");
                mProfileImage.setImageBitmap(bmp);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.profile_sign_out) {
            mAuth.signOut();
            Intent myIntent = new Intent(this, MainActivity.class);
            Toast.makeText(this,"Starting main activity",Toast.LENGTH_SHORT);
            startActivity(myIntent);
        }
        if(i == R.id.changeImage){
            takePhoto();
        }
    }


}
