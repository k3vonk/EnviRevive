package com.KGRJJ.kgrjj_android_20192020.Event_related_content;

import android.location.Location;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.model.value.NumberValue;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class EventDisplayActivity extends BaseActivity {

    private List<EventDataObject> events;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_event_display);

        events = new ArrayList<>();


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        getEvents();


    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_event_display;
    }

    private void getEvents(){
        db.collection("Events").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                HashMap<String, Long> tempDat3 = (HashMap<String, Long>) doc.get("Date");
                HashMap<String, Long> temptime = (HashMap<String, Long>) doc.get("Time");
                Time time = new Time(Math.toIntExact(temptime.get("hour")),Math.toIntExact(temptime.get("minute")));
                Date date = new Date(Math.toIntExact(tempDat3.get("year")),Math.toIntExact(tempDat3.get("month")),Math.toIntExact(tempDat3.get("day")));
                EventDataObject EDO =
                        new EventDataObject(
                                (String) doc.get("Title"),
                                (String) doc.get("Description"),
                                date,
                                time,
                                (GeoPoint) doc.get("Location"),
                                (ArrayList<String>) doc.get("Images"));
                events.add(EDO);
            }
            eventAdapter = new EventAdapter(getApplicationContext(), events);
            recyclerView.setAdapter(eventAdapter);
        });
    }
}
