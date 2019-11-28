package com.KGRJJ.kgrjj_android_20192020.Event_related_content;

import android.location.Location;
import android.util.Log;
import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * EventCreation allows the users to create clean-up events and fill
 * it with details such as a geopoint location and a time, for example.
 * @author Ga Jun Young, Jackie Ju, Joiedel Agustin, Kiowa Daly, Rebecca Lobo
 * @since 27-11-2019
 */
public class EventCreation {

    private FirebaseFirestore db;
    private FirebaseUser user;

    public EventCreation(FirebaseFirestore db, FirebaseUser user){
        this.db = db;
        this.user = user;
    }
    public void CreateEvent(String Title, String description, Date date, Time time, Location location){
        //creates an empty hash map for the allocation of event data
        HashMap<String,Object> map = new HashMap<>();
        HashMap<String,Object> map2 = new HashMap<>();
        ArrayList<DocumentReference> value = new ArrayList<>();
        ArrayList<String> myArray = new ArrayList<>();
        ArrayList<String> imageArray = new ArrayList<>();

        //placing event data placeholders inside the hash map
        map.put("Title",Title);
        map.put("Description",description);
        map.put("Date",date);
        map.put("Time",time);
        map.put("Location",new GeoPoint(location.getLatitude(),location.getLongitude()));
        map.put("Attendees",myArray);
        map.put("Images",imageArray);

        myArray.add(user.getUid());

        //associating firebase event data with the hash map
        db.collection("Events").document(Title).set(map).addOnSuccessListener(aVoid->
                Log.i("Test","Correctly added event")
        );
        value.add(db.collection("Events").document(Title));
        db.collection("User").document(user.getUid()).set(map2, SetOptions.merge());
    }

}
