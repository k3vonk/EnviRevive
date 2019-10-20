package com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.MainActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;


public class UserProfileActivity extends BaseActivity implements View.OnClickListener {

    protected static final int CAPTURE_IMAGE_ATIVITY_REQUEST_CODE = 0;
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
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(
                "gs://kgrjj-android-2019.appspot.com/images");

        profile_name = findViewById(R.id.profile_username);
        profile_rank = findViewById(R.id.profile_rank);
        profile_city_country = findViewById(R.id.profile_city_country);
        profile_points = findViewById(R.id.profile_points);
        mProfileImage = findViewById(R.id.profile_portrait_image);
        mCoverPhoto = findViewById(R.id.coverPhoto);
        user = mAuth.getCurrentUser();
        if (user != null) {
            getFullName(user);
            getRank(user);
            getCityCountry(user);
            getPoints(user);
            getProfileImage(user);
        }
        findViewById(R.id.profile_sign_out).setOnClickListener(this);
        findViewById(R.id.changeImage).setOnClickListener(this);
        mProfileImage.setOnClickListener(this);

        Glide
                .with(this)
                .load(R.mipmap.glenda)
                .centerCrop()
                .into(mCoverPhoto);

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
                        profile_points.setText(
                                documentSnapshot.getDouble("Points").toString());
                    }
                });
    }

    protected void getProfileImage(FirebaseUser user) {
//        StorageReference profileRef = mStorageRef.child(user.getUid()+"/profileImage.jpg");
//        final long ONE_MEGABYTE = 1024 *1024;
//        profileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
//           Bitmap bmp =  BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//           mProfileImage.setImageBitmap(bmp);
//        });
        StorageReference profileRef = mStorageRef
                .child(user.getUid() + "/profileImage.png");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Glide
                .with(getApplicationContext())
                .load(uri)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(mProfileImage));
        //mProfileImage.setRotation((float)90.0);

//        String imageUrl = "gs://kgrjj-android-2019.appspot.com/images/"+
//                user.getUid()+"profileImage.png";

    }

    private void uploadToFirebase(Bitmap bmp) {
        StorageReference profileRef = mStorageRef.child(user.getUid() + "/profileImage.png");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getApplicationContext(), "Uploaded image", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Failed upload", Toast.LENGTH_SHORT).show();
        });


    }

    private void takePhoto() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ATIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ATIVITY_REQUEST_CODE) {
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri
                    );
                    mProfileImage.setImageBitmap(thumbnail);
                    Glide
                            .with(getApplicationContext())
                            .load(thumbnail)
                            .apply(RequestOptions.centerCropTransform())
                            .apply(RequestOptions.circleCropTransform())
                            .into(mProfileImage);
                    //mProfileImage.setRotation((float) 270);
                    uploadToFirebase(thumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                Bundle extras = data.getExtras();
//                Bitmap bmp = (Bitmap) extras.get("data");
//
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.profile_sign_out) {
            mAuth.signOut();
            Intent myIntent = new Intent(this, MainActivity.class);
            Toast.makeText(this, "Starting main activity", Toast.LENGTH_SHORT).show();
            startActivity(myIntent);
        }
        if (i == R.id.changeImage) {
            int REQUEST_CODE = 1;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            takePhoto();
        }
    }


}
