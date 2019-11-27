package com.KGRJJ.kgrjj_android_20192020.Event_related_content;


import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The EventDisplayActivity activity displays a list of events
 * which the users can view on a scrolling recycler structure and have the
 * ability to register to.
 * @author Ga Jun Young, Jackie Ju, Joiedel Agustin, Kiowa Daly, Rebecca Lobo
 * @since 27-11-2019
 */
public class EventDisplayActivity extends BaseActivity {

    private List<EventDataObject> events;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    /**
     * retrieves the event data from Firebase and calls upon EventDataObject to construct
     * an event object containing this data. The objects are added to an array list
     * which is processed by the Adapter and displayed on the Recycler View
     */
    private void getEvents(){
        db.collection("Events").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
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
                                (ArrayList<String>) doc.get("Images"));
                events.add(EDO);
            }
            eventAdapter = new EventAdapter(getApplicationContext(), events);
            recyclerView.setAdapter(eventAdapter);
        });
    }
}
