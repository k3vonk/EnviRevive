package com.KGRJJ.kgrjj_android_20192020.Event_related_content;

import android.location.Location;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EventDisplayActivity extends BaseActivity {

    private List<EventDataObject> events;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);

        events = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getEvents();

        eventAdapter = new EventAdapter(this, events);
        recyclerView.setAdapter(eventAdapter);
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
