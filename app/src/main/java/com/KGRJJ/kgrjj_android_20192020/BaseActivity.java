package com.KGRJJ.kgrjj_android_20192020;


import android.Manifest;

import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.KGRJJ.kgrjj_android_20192020.Authentication.LoginActivity;
import com.KGRJJ.kgrjj_android_20192020.Authentication.PackageManagerUtils;
import com.KGRJJ.kgrjj_android_20192020.Data.Image_Upload;
import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventDisplayActivity;
import com.KGRJJ.kgrjj_android_20192020.Image_related_content.ImageDisplayActivity;
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
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.LatLng;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.ref.WeakReference;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public abstract class BaseActivity extends AppCompatActivity {

    public static final String MAP_TAG = "ENVIVE_MAP_TAG";
    protected static final int CAPTURE_IMAGE_ATIVITY_REQUEST_CODE = 0;
    protected static final int RESULT_OK = -1;
    protected static final int REQUEST_PERMISSION_LOCATION_KEY = 99;
    public static Bitmap thumbnail;
    public static String fullname;
    public static String Rank;
    public static String Country;
    public static String Points;
    public static Bitmap profileImage;
    protected static Uri imageUri;
    protected static StorageReference mStorageReference;
    protected static FirebaseUser user;
    protected static FirebaseAuth mAuth;
    private static boolean isInProfile;
    private static boolean isInReg;
    protected CycleMenuWidget cycleMenuWidget;
    protected String mostRecentPhotoPath;
    protected Location mLastLocation;
    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected LocationRequest mLocationRequest;
    protected ArrayList<LatLng> list;
    protected Image_Upload image_upload;
    protected FirebaseFirestore db;
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

    private static final String CLOUD_VISION_API_KEY = "AIzaSyAtV-bOc020EN9DQSxbnTbG_8NYnHBa33M";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;


    private static final String TAG = BaseActivity.class.getSimpleName();
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    private TextView mImageDetails;

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
                                Intent myIntentImagesList = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                                startActivity(myIntentImagesList);
                                break;
                            case 4:
                                takePhoto(false, false);
                                break;
                            case 5:
                                Intent myIntentEventCreate = new Intent(getApplicationContext(), EventCreationDialog.class);
                                startActivity(myIntentEventCreate);
                                break;
                            case 6:
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
                                Toast.makeText(getApplicationContext(), "View your Profile", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getApplicationContext(), "See a List of Events", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(getApplicationContext(), "See a List of Images", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(getApplicationContext(), "Take a Photo", Toast.LENGTH_SHORT).show();

                                break;
                            case 5:
                                Toast.makeText(getApplicationContext(), "Create an Event", Toast.LENGTH_SHORT).show();

                                break;
                            case 6:
                                Toast.makeText(getApplicationContext(), "Sign Out", Toast.LENGTH_SHORT).show();
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
        image_upload = new Image_Upload(db, mStorageReference, getApplicationContext());
    }

    //region Photo Related Content
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "PNG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageName,  /* prefix */
                    ".png",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        mostRecentPhotoPath = image.getAbsolutePath();
        Log.i("TESTING", mostRecentPhotoPath);
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mostRecentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    protected void takePhoto(boolean PI, boolean Reg) {


        isInProfile = PI;
        isInReg = Reg;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFIle = createImageFile();
        if (photoFIle != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.KGRJJ.kgrjj_android_20192020.provider", photoFIle);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, CAPTURE_IMAGE_ATIVITY_REQUEST_CODE);
        }

    }












    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ATIVITY_REQUEST_CODE) {
                //thumbnail  = (Bitmap) data.getExtras().get("data");
                galleryAddPic();

                Bitmap bmp = BitmapFactory.decodeFile(mostRecentPhotoPath);
                if (isInProfile) {
                    ImageView m = findViewById(R.id.profile_portrait_image);
                    Glide
                            .with(getApplicationContext())
                            .load(mostRecentPhotoPath)
                            //.apply(RequestOptions.overrideOf(400,400))
                            .apply(RequestOptions.centerCropTransform())
                            .apply(RequestOptions.circleCropTransform())
                            .into(m);
                    try {

                        UploadProfileImage(bmp, user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else if (isInReg) {
                    ImageView m = findViewById(R.id.takePhoto);
                    Glide
                            .with(getApplicationContext())
                            .load(mostRecentPhotoPath)
                            //.apply(RequestOptions.overrideOf(400,400))
                            .apply(RequestOptions.centerCropTransform())
                            .apply(RequestOptions.circleCropTransform())
                            .into(m);
                } else {

                    UploadImage(bmp, mLastLocation, user);

                    //Load bitmap and garbage collection
                    try {
                        //Write file
                        String filename = mostRecentPhotoPath;
                        FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        //Cleanup
                        stream.close();
                        thumbnail.recycle();

                        Intent imageAnalysisScreen = new Intent(getApplicationContext(), ImageAnalysisScreen.class);
                        imageAnalysisScreen.putExtra("image", filename);
                        startActivity(imageAnalysisScreen);
                    }catch (Exception e){
                        Log.e(TAG, "Bitmap of image does not exist " + e.getMessage());
                    }
                }
            }
        }
    }

    public void UploadProfileImage(Bitmap bmp, FirebaseUser user) throws IOException {
        StorageReference profileRef = mStorageReference.child(user.getUid() + "/profileImage.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileRef.putBytes(data);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getApplicationContext(), "Uploaded image", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Failed upload", Toast.LENGTH_SHORT).show();
        });


    }
    //endregion
    //region Location Permission Content


    String currFilePath;
    public void UploadImage(Bitmap bmp,Location location,FirebaseUser user){


        String imagename = new Random().nextInt(10000) + 0 + "_" + location + ".jpg";
        StorageReference profileRef = mStorageReference.child("images/" + imagename);

        String url = "gs://kgrjj-android-2019.appspot.com/images/";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getApplicationContext(), "Uploaded image", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Failed upload", Toast.LENGTH_SHORT).show();
        });

        HashMap<String, Object> map = new HashMap<>();
        map.put("Location", new GeoPoint(location.getLatitude(), location.getLongitude()));
        map.put("URL", imagename);

        db.collection("Images").add(map);

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


    //endregion

    /**
     * Callback function to obtain new location and store the old one.
     * Update any additional features in regards to this new location
     */


//endregion

    protected abstract int getLayoutResourceID();

    public void getRegisteredEvents(){

    }

    public void getUserData(FirebaseUser user) {
        Log.i("TESTING", "cloud function called");

        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        fullname = (String.format("%s %s",
                                documentSnapshot.getString("FName"),
                                documentSnapshot.getString("LName")));
                        Log.i("TESTING", fullname);
                        Rank = (documentSnapshot.getString("Rank"));
                        Log.i("TESTING", Rank);
                        Country = (String.format("%s,%s",
                                documentSnapshot.getString("City"),
                                documentSnapshot.getString("Country")));
                        Log.i("TESTING", Country);
                        Points = String.valueOf(documentSnapshot.getDouble("Points").intValue());
                        Log.i("TESTING", Points);

                        StorageReference profileRef = mStorageReference
                                .child(user.getUid() + "/profileImage.jpg");

                        profileRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    Glide.with(getApplicationContext())
                                            .asBitmap()
                                            .load(uri)
                                            .into(new CustomTarget<Bitmap>() {

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
        Toast.makeText(getApplicationContext(), "Pulled values from cloud", Toast.LENGTH_LONG).show();
    }


}
