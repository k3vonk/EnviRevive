package com.KGRJJ.kgrjj_android_20192020.Event_related_content;

import android.location.Location;
import android.util.Log;

import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import java.util.HashMap;

public class EventCreation {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private Date date;

    public EventCreation(FirebaseFirestore db, FirebaseUser user){
        this.db = db;
        this.user = user;
    }
    public void CreateEvent(String Title, String description, Date date, Time time, Location location){
        HashMap<String,Object> map = new HashMap<>();
        map.put("Title",Title);
        map.put("Description",description);
        map.put("Date",date);
        map.put("Time",time);
        map.put("Location",location);
        ArrayList<String> myArray = new ArrayList<>();
        myArray.add(user.getUid());
        map.put("Attendees",myArray);

        db.collection("Events").document(Title).set(map).addOnSuccessListener(aVoid->
                Log.i("Test","Correctly added event")
        );
    }
    public void addDate(Date date){
        this.date = date;
    }
}
