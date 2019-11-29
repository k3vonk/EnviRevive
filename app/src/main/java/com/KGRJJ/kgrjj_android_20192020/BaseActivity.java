package com.KGRJJ.kgrjj_android_20192020;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.FileProvider;

import com.KGRJJ.kgrjj_android_20192020.Authentication.LoginActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Random;
/**
 * The BaseActivity houses any code that is reusable in multiple activities
 *
 * @author Ga Jun Young, Jackie Ju, Joiedel Agustin, Kiowa Daly, Rebecca Lobo
 * @since 07-10-2019
 */

public abstract class BaseActivity extends AppCompatActivity {

    //Error Message Tag
    private static final String TAG = BaseActivity.class.getSimpleName();

    //Map & Location Variables
    protected static final int REQUEST_PERMISSION_LOCATION_KEY = 99;
    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;

    //Image Variables
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    protected  static final int REQUEST_ANALYSIS = 70;
    protected static final int RESULT_OK = -1;
    public static Bitmap profileImage;
    protected static String mostRecentPhotoPath;

    //FireBase/Internal Storage & Image Info Variables
    protected static final int REQUEST_PERMISSION_STORAGE_KEY = 10;
    protected static FirebaseUser user;
    protected static FirebaseAuth mAuth;
    protected static StorageReference mStorageReference;
    protected FirebaseFirestore db;
    protected ArrayList<LatLng> list;
    public static String fullname;
    public static String Rank;
    public static String Country;
    public static String Points;

    //Cycle Hamburger menu
    private static boolean isInProfile;
    private static boolean isInReg;
    protected CycleMenuWidget cycleMenuWidget;

    /**
     * Setup location tracker, wheel widget, FireBase and Internal storage
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceID());

        setupLocationTracker(); //instantiate location tracking and carry out various tasks

        //TODO: clean up cycleMenuWidget to several functions
        cycleMenuWidget = findViewById(R.id.itemCycleMenuWidget);
        cycleMenuWidget.setMenuRes(R.menu.wheel_menu);
        if(getLayoutResourceID() == R.layout.activity_login ||
                getLayoutResourceID() == R.layout.activity_registration ||
                    getLayoutResourceID()==R.layout.activity_image_analysis_screen ||
                        getLayoutResourceID()==R.layout.activity_image_analysis_screen){
            cycleMenuWidget.setEnabled(false);
            cycleMenuWidget.setVisibility(View.INVISIBLE);
        }
        else{
            cycleMenuWidget.setEnabled(true);
            cycleMenuWidget.setVisibility(View.VISIBLE);
        }

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
                                finish();
                                break;
                            case 1:
                                if(getLayoutResourceID() == R.layout.activity_user_profile) {
                                    break;
                                }else{
                                    getUserData(user);
                                    Intent myIntentProfile = new Intent(getApplicationContext(), UserProfileActivity.class);
                                    startActivity(myIntentProfile);
                                    finish();
                                    break;
                                }

                            case 2:
                                Intent myIntentEventsList = new Intent(getApplicationContext(), EventDisplayActivity.class);
                                startActivity(myIntentEventsList);
                                finish();
                                break;
                            case 3:
                                Intent myIntentImagesList = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                                startActivity(myIntentImagesList);
                                finish();
                                break;
                            case 4:
                                takePhoto(false, false);
                                break;
                            case 5:
                                Intent myIntentEventCreate = new Intent(getApplicationContext(), EventCreationDialog.class);
                                startActivity(myIntentEventCreate);
                                finish();
                                break;
                            case 6:
                                mAuth.signOut();
                                user = null;
                                Intent myIntentLogout = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(myIntentLogout);
                                finish();
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

        //Initialise FireBase related variables
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                "gs://kgrjj-android-2019.appspot.com/images");
    }


    //====================== IMAGE TAKEN ===========================//

    /**
     * Take a photo using phone's hardware
     * @param PI true if in profile activity
     * @param Reg true if in registration activity
     */
    protected void takePhoto(boolean PI, boolean Reg) {
        isInProfile = PI;
        isInReg = Reg;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                galleryAddPic();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("takePhoto", "Error occured creating file" + ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.KGRJJ.kgrjj_android_20192020.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    /**
     * Upload personal profile image onto FireBase and into user's account
     * @param bmp image of user
     * @param user users personal FireBase
     */
    public void UploadProfileImage(Bitmap bmp, FirebaseUser user) throws IOException {

        StorageReference profileRef = mStorageReference.child(user.getUid() + "/profileImage.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileRef.putBytes(data);

        uploadTask.addOnSuccessListener(taskSnapshot -> Toast.makeText(getApplicationContext(), "Uploaded image", Toast.LENGTH_SHORT)
                .show())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed upload", Toast.LENGTH_SHORT).show());
    }

    /**
            Method called only after image analysis has completed.
            Current location, URL of image and analysis results are store in a
            FireBase FireStore Image Collection.
     */
    public void UploadImage(Bitmap bmp,Location location,HashMap<String,Float> results){
        String imagename = new Random().nextInt(10000) + 0 + "_" + location + ".jpg";
        StorageReference profileRef = mStorageReference.child("images/" + imagename);

        //Compress bitmap into byte Array to be uploaded using upload Task
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getApplicationContext(), "Uploaded image", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Failed upload", Toast.LENGTH_SHORT).show();
        });

        //Store Location/URL/results of analysis into a new Document in the Image Collection
        HashMap<String, Object> map = new HashMap<>();
        map.put("Location", new GeoPoint(location.getLatitude(), location.getLongitude()));
        map.put("URL", imagename);
        map.put("AnalysisResults",results);
        db.collection("Images").add(map);

    }

    /*
            Method from https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a

            Fixes rotation issue with taking a photo
     */
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    //endregion

    //region Location Permission Content


    /**
     * createImageFile create a collision-resistant file name
     * Reference: https://developer.android.com/training/camera/photobasics.html
     * @return unique file name
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mostRecentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Add photo to a set gallery database
     * Reference: https://developer.android.com/training/camera/photobasics.html
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Log.i("GALLERYADDPIC", "Photopath " + mostRecentPhotoPath);
        File f = new File(mostRecentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    /**
     * Bitmap transformation by rotation
     */
    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * Bitmap transformation by flipping
     */
    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //============================================ FIREBASE ==================================================================//
    protected abstract int getLayoutResourceID();

    /**
            Method to retrieve user data from Firebase Firestore
     */
    public void getUserData(FirebaseUser user) {
        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        fullname = (String.format("%s %s",
                                documentSnapshot.getString("FName"),
                                documentSnapshot.getString("LName")));
                        Rank = (documentSnapshot.getString("Rank"));
                        Country = (String.format("%s,%s",
                                documentSnapshot.getString("City"),
                                documentSnapshot.getString("Country")));
                        Points = String.valueOf(documentSnapshot.getDouble("Points").intValue());
                        StorageReference profileRef = mStorageReference
                                .child(user.getUid() + "/profileImage.jpg");
                        profileRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    Glide.with(getApplicationContext())
                                            .asBitmap()
                                            .apply(RequestOptions.centerCropTransform())
                                            .apply(RequestOptions.circleCropTransform())                               .load(uri)
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
                    }
                });
        Toast.makeText(getApplicationContext(), "Pulled user values from cloud", Toast.LENGTH_LONG).show();
        Log.i("FirebaseResults", "cloud function called and retrieved user info");
    }


    //======================== LOCATION FINDER =============================//
    /**
     * Instantiate location tracker
     */
    private void setupLocationTracker(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        //Requesting location
        mLocationRequest = new LocationRequest();

        //Frequency settings
        mLocationRequest.setInterval(2 * 60 * 1000); //Every 2 minutes
        mLocationRequest.setFastestInterval(2 * 60 * 1000);

        //Accuracy settings
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        onLocationPermissionTask();
    }

    /**
     * Carry out various location tasks based on permission
     */
    private void onLocationPermissionTask(){
        //If permission is granted...
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } else { //Request permission...
            checkLocationPermissions();
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
            Log.d(TAG, "getLocation: permissions granted");
        }
    }


    /**
     * Callback function to obtain new location and store the old one.
     * Update any additional features in regards to this new location
     * Reference: https://developers.google.com/maps/documentation/android-sdk/start
     */
    final LocationCallback mLocationCallback = new LocationCallback() {
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

    /**
     * Handle requests based on requestCode
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION_LOCATION_KEY) {//If the request is granted...
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) && (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION))) {
                //If permission location is granted...
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                } else { //Permission denied...
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
        }else if(requestCode == REQUEST_PERMISSION_STORAGE_KEY){
            if((grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)&&(permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE))){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Accessed", Toast.LENGTH_LONG).show();
                } else { //Permission denied...
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //================================  ACTIVITY RESULTS =====================================//
    /**
     * Based on parameters, carry out different tasks after an activity
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("Baseactivity", "What is my most recent phot path" + mostRecentPhotoPath);
        if(resultCode == 69 && requestCode == REQUEST_ANALYSIS){
            Bitmap bmp = BitmapFactory.decodeFile(mostRecentPhotoPath);
            HashMap<String,Float> results = (HashMap<String,Float>) data.getSerializableExtra("Results");
            UploadImage(bmp,mLastLocation,results);
            if(results != null) {
                Log.i("BaseActivity", " Image Uploaded");
            }
            else
                Log.i("BaseActivity"," Image not null 1");

        }
        else if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                Log.i("BASEACTIVITY", "string path" + mostRecentPhotoPath);
                Bitmap bmp = BitmapFactory.decodeFile(mostRecentPhotoPath);

                if(bmp == null)
                    Log.i("BASEACTIVITY", "IS NULL");
                else
                    Log.i("BASEACTIVITIY", "IS NOT NULL");
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

                        UploadProfileImage(bmp ,user);
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
                    try {
                        //analyse the image with new intent and return then values
                        Intent imageAnalysisScreen = new Intent(getApplicationContext(), ImageAnalysisScreen.class);
                        imageAnalysisScreen.putExtra("image", mostRecentPhotoPath);
                        startActivityForResult(imageAnalysisScreen,REQUEST_ANALYSIS);
                    }catch (Exception e){
                        Log.e(TAG, "Bitmap of image does not exist " + e.getMessage());
                    }
                }
            }
        }
    }
}

