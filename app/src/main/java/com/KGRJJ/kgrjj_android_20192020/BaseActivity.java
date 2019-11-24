package com.KGRJJ.kgrjj_android_20192020;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.KGRJJ.kgrjj_android_20192020.Authentication.LoginActivity;
import com.KGRJJ.kgrjj_android_20192020.Data.Image_Upload;
import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventDisplayActivity;
import com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities.UserProfileActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cleveroad.sy.cyclemenuwidget.CycleMenuWidget;
import com.cleveroad.sy.cyclemenuwidget.OnMenuItemClickListener;
import com.cleveroad.sy.cyclemenuwidget.OnStateChangedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class BaseActivity extends AppCompatActivity {

    protected CycleMenuWidget cycleMenuWidget;

    protected Location mLastLocation;
    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected LocationRequest mLocationRequest;
    protected ArrayList<LatLng> list;
    protected static final int CAPTURE_IMAGE_ATIVITY_REQUEST_CODE = 0;
    protected static final int RESULT_OK = -1;

    public static Bitmap thumbnail;

    protected static Uri imageUri;
    public static String fullname;
    public static String Rank;
    public static String Country;
    public static String Points;
    public  static Bitmap profileImage;
    protected  Image_Upload image_upload;
    protected  FirebaseFirestore db;
    protected static StorageReference mStorageReference;
    protected static FirebaseUser user;
    protected static FirebaseAuth mAuth;
    private static boolean isInProfile;
    private static boolean isInReg;
    protected static final int REQUEST_PERMISSION_LOCATION_KEY = 99;
    public static final String MAP_TAG = "ENVIVE_MAP_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceID());

        //Instantiation
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        //Requesting location
        mLocationRequest = new LocationRequest();

        //Frequency settings
        mLocationRequest.setInterval(2 * 60 * 1000); //Every 2 minutes
        mLocationRequest.setFastestInterval(2 * 60 * 1000);

        //Accuracy settings
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //If permission is granted...
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

        } else { //Request permission...
            checkLocationPermissions();
        }


        cycleMenuWidget = findViewById(R.id.itemCycleMenuWidget);
        cycleMenuWidget.setMenuRes(R.menu.wheel_menu);


        cycleMenuWidget.setStateChangeListener(
                new OnStateChangedListener() {
                    @Override
                    public void onStateChanged(CycleMenuWidget.STATE state) {

                    }

                    @Override
                    public void onOpenComplete() {

                    }

                    @Override
                    public void onCloseComplete() {

                    }
                }
        );

        cycleMenuWidget.setOnMenuItemClickListener(
                new OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(View view, int itemPosition) {
                        switch (itemPosition) {
                            case 0:
                                Intent myIntentMap = new Intent(getApplicationContext(), MapsActivity.class);
                                startActivity(myIntentMap);
                                break;
                            case 1:
                                Intent myIntentProfile = new Intent(getApplicationContext(), UserProfileActivity.class);
                                startActivity(myIntentProfile);
                                break;
                            case 2:
                                Intent myIntentEventsList = new Intent(getApplicationContext(), EventDisplayActivity.class);
                                startActivity(myIntentEventsList);
                                break;
                            case 3:
                                takePhoto(false,false);
                                break;
                            case 4:
                                Intent myIntentEventCreate = new Intent(getApplicationContext(), EventCreationDialog.class);
                                startActivity(myIntentEventCreate);
                                break;
                            case 5:
                                mAuth.signOut();
                                user = null;
                                Intent myIntentLogout = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(myIntentLogout);
                                break;
                        }
                    }

                    @Override
                    public void onMenuItemLongClick(View view, int itemPosition) {
                        switch (itemPosition) {
                            case 0:
                                Toast.makeText(getApplicationContext(), "Go to the Map", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getApplicationContext(), "View your profile", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getApplicationContext(), "See a List of Events", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(getApplicationContext(), "Take a photo", Toast.LENGTH_SHORT).show();

                                break;
                            case 4:
                                Toast.makeText(getApplicationContext(), "Create an Event", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }
        );



        cycleMenuWidget.setStateSaveListener(
                (itemPosition, lastItemAngleShift) -> {

                }
        );
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                "gs://kgrjj-android-2019.appspot.com/images");
        image_upload = new Image_Upload(db,mStorageReference,getApplicationContext());
    }

    protected void takePhoto(boolean PI,boolean Reg) {

        isInProfile = PI;
        isInReg = Reg;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_ATIVITY_REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ATIVITY_REQUEST_CODE) {
                thumbnail  = (Bitmap) data.getExtras().get("data");

                Log.i("TESTING",""+thumbnail.getGenerationId());
                if (isInProfile) {
                    image_upload.UplaodProfileImage(thumbnail,user);
                } else if (isInReg) {
                    Log.i("TESTING", "image taken from reg");
                    ImageView m = findViewById(R.id.takePhoto);
                    Glide
                            .with(getApplicationContext())
                            .load(thumbnail)
                            //.apply(RequestOptions.overrideOf(400,400))
                            .apply(RequestOptions.centerCropTransform())
                            .apply(RequestOptions.circleCropTransform())
                            .into(m);
                } else {
                    image_upload.UploadImage(thumbnail, mLastLocation,user);
                }
            }
        }
    }
    /**
     * Check for application permission to access location
     */
    protected void checkLocationPermissions() {

        //If there is no permission...
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION_KEY);
        } else { //else granted...
            Log.d(MAP_TAG, "getLocation: permissions granted");
        }
    }

    /**
     * Handle requests based on requestCode
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION_KEY:

                //If the request is granted...
                if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) && (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION))) {
                    //If permission location is granted...
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        // mMap.setMyLocationEnabled(true);
                    } else { //Permission denied...
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
        }
    }

    /**
     * Callback function to obtain new location and store the old one.
     * Update any additional features in regards to this new location
     */

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();

            if (!locationList.isEmpty()) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude()); //Log message for newest location
                mLastLocation = location;
            }
        }
    };


    protected abstract int getLayoutResourceID();

    public void getUserData(FirebaseUser user) {
        Log.i("TESTING","cloud function called");

        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        fullname = (String.format("%s %s",
                                documentSnapshot.getString("FName"),
                                documentSnapshot.getString("LName")));
                        Log.i("TESTING",fullname);
                        Rank = (documentSnapshot.getString("Rank"));
                        Log.i("TESTING",Rank);
                        Country = (String.format("%s,%s",
                                documentSnapshot.getString("City"),
                                documentSnapshot.getString("Country")));
                        Log.i("TESTING",Country);
                        Points =  String.valueOf(documentSnapshot.getDouble("Points").intValue());
                        Log.i("TESTING",Points);

                        StorageReference profileRef = mStorageReference
                                .child(user.getUid() + "/profileImage.png");

                        profileRef.getDownloadUrl()
                                .addOnSuccessListener(uri-> {
                                    Glide.with(getApplicationContext())
                                            .asBitmap()
                                            .load(uri)
                                            .into(new CustomTarget<Bitmap>(){

                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    profileImage = resource;
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                                }
                                            });
                                });
                        //Glide






                        //Log.i("TESTING",profileImage);

                    }
                });
        Toast.makeText(getApplicationContext(),"Pulled values from cloud",Toast.LENGTH_LONG).show();
    }

}
