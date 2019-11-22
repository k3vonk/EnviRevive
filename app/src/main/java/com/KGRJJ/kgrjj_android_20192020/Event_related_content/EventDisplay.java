package com.KGRJJ.kgrjj_android_20192020.Event_related_content;

import android.location.Location;
import android.os.Bundle;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;

public class EventDisplay extends BaseActivity {


    private HashSet<EventDataObject> events = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);
        getEvents();
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_event_display;
    }

    private void getEvents(){
        db.collection("Events").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                EventDataObject EDO =
                        new EventDataObject(
                                (String) doc.get("Title"),
                                (String) doc.get("Description"),
                                (Date) doc.get("Date"),
                                (Time) doc.get("Time"),
                                (Location) doc.get("Location"),
                                (ArrayList<String>) doc.get("Images"));
                events.add(EDO);
            }
        });
    }
}
