package com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventAdapter;
import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventDataObject;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

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

    //RecyclerView variables
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        profile_name = findViewById(R.id.profile_username);
        profile_rank = findViewById(R.id.profile_rank);
        profile_city_country = findViewById(R.id.profile_city_country);
        profile_points = findViewById(R.id.profile_points);
        mProfileImage = findViewById(R.id.profile_portrait_image);
        my_events = new ArrayList<>();


        user = mAuth.getCurrentUser();   //ensure we have the correct user

        getUserData(user);  //retrieve necessary data. Must come before the following
        profile_name.setText(fullname);
        profile_rank.setText(Rank);
        profile_city_country.setText(Country);
        profile_points.setText(Points);
        mProfileImage.setImageBitmap(profileImage);


        //load profile images into image view using Glide
        Glide.with(getApplicationContext())
                .load(profileImage)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(mProfileImage);


        //Add listeners to each button
        findViewById(R.id.change_image).setOnClickListener(this);
        mProfileImage.setOnClickListener(this);


        recyclerView = findViewById(R.id.profileRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        GetRegisteredEvents(); //Retrieves event data and inserts it into recyclerview
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

    /*
            Method to retrieve all the events the user is currently subscribed to.
            Insert events into RecyclerView to be displayed
     */
    private void GetRegisteredEvents(){

        db.collection("user").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        ArrayList<DocumentReference> subscribed =
                                (ArrayList<DocumentReference>) documentSnapshot.get("subscribedEvents");

                        for(DocumentReference ref : subscribed){

                            ref.get().addOnSuccessListener(doc -> {

                                String id = doc.getId();

                                HashMap<String, Long> temp_date = (HashMap<String, Long>) doc.get("Date");
                                HashMap<String, Long> temp_time = (HashMap<String, Long>) doc.get("Time");
                                Time time = new Time(Math.toIntExact(temp_time.get("hour")),
                                        Math.toIntExact(temp_time.get("minute")));

                                Date date = new Date(Math.toIntExact(temp_date.get("year")),
                                        Math.toIntExact(temp_date.get("month")),
                                        Math.toIntExact(temp_date.get("day")));
                                //create a EventDataObject to be used by the eventAdapter
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
