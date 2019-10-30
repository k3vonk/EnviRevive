package com.KGRJJ.kgrjj_android_20192020.Event_related_content;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class EventCreation {
    private FirebaseFirestore db;
    private FirebaseUser user;

    public EventCreation(FirebaseFirestore db, FirebaseUser user){
        this.db = db;
        this.user = user;
    }
    public void CreateEvent(String Title,String description,String date,String Time){
        HashMap<String,Object> map = new HashMap<>();
        map.put("Title",Title);
        map.put("Description",description);
        map.put("Date",date);
        map.put("Time",Time);
        ArrayList<String> myArray = new ArrayList<>();
        myArray.add(user.getUid());
        map.put("Attendees",myArray);

        db.collection("Events").add(map).addOnSuccessListener(aVoid->
                Log.i("Test","Correctly added event")
        );
    }
}
