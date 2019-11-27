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
import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventAdapter;
import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventDataObject;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The UserProfile activity produces a screen that displays relevant account details of the user
 * currently signed in. Including profile image, Username, Points, and subscribed events
 *
 *
 * @author Ga Jun Young, Jackie Ju, Joiedel Agustin, Kiowa Daly, Rebecca Lobo
 * @since 07-10-2019
 */
public class UserProfileActivity extends BaseActivity implements View.OnClickListener {

    //USER PROFILE PAGE VARIABLES
    private TextView profile_name, profile_rank, profile_city_country, profile_points;
    protected  ImageView mProfileImage;
    protected ArrayList<EventDataObject> my_events;
    private RecyclerView recyclerView;

    private EventAdapter eventAdapter;
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
        my_events = new ArrayList<>();
        user = mAuth.getCurrentUser();
        getUserData(user);

        //getRegisteredEvents(user);
        profile_name.setText(fullname);
        profile_rank.setText(Rank);
        profile_city_country.setText(Country);
        profile_points.setText(Points);
        //Log.i("TESTING","PROFILEIMAGE CODE"+profileImage.getDensity());
        mProfileImage.setImageBitmap(profileImage);

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
        GetRegisteredEvents();
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_user_profile;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.change_image) {
            int REQUEST_CODE = 1;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            takePhoto(true,false);
        }
    }

    private void GetRegisteredEvents(){

        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        ArrayList<DocumentReference> subscribed =
                                (ArrayList<DocumentReference>) documentSnapshot.get("subscribedEvents");

                        for(DocumentReference ref : subscribed){

                            ref.get().addOnSuccessListener(doc -> {

                                String id = doc.getId();
                                HashMap<String, Long> tempDat3 = (HashMap<String, Long>) doc.get("Date");
                                HashMap<String, Long> temptime = (HashMap<String, Long>) doc.get("Time");
                                Time time = new Time(Math.toIntExact(temptime.get("hour")),Math.toIntExact(temptime.get("minute")));
                                Date date = new Date(Math.toIntExact(tempDat3.get("year")),Math.toIntExact(tempDat3.get("month")),Math.toIntExact(tempDat3.get("day")));
                                EventDataObject EDO =
                                        new EventDataObject(id,
                                                (String) doc.get("Title"),
                                                (String) doc.get("Description"),
                                                date,
                                                time,
                                                (GeoPoint) doc.get("Location"),
                                                (ArrayList<String>) doc.get("Images"),
                                                (ArrayList<String>) doc.get("Attendees"));
                                my_events.add(EDO);
                                Log.i("TESTING",my_events.toString());
                                eventAdapter = new EventAdapter(getApplicationContext(),my_events);
                                recyclerView.setAdapter(eventAdapter);
                            });
                        }
                    }

                });

    }

}
