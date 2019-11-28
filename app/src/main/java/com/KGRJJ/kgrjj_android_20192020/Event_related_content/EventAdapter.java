package com.KGRJJ.kgrjj_android_20192020.Event_related_content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import java.util.List;

/**
 * The EventAdapter activity produces allows the contents
 * of Recycler cards to be filled with the latest event
 * content - works in tandem with the activity "EventDisplayActivity"
 * @author Ga Jun Young, Jackie Ju, Joiedel Agustin, Kiowa Daly, Rebecca Lobo
 * @since 27-11-2019
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ProductViewHolder>{

    private Context mCtxEvent;
    private List<EventDataObject> eventobjList;

    public EventAdapter(Context mCtx, List<EventDataObject> eventobjList) {
        this.mCtxEvent = mCtx;
        this.eventobjList = eventobjList;
    }


    /**
     * encasulation of the list of events inside a Product Holder.
     */
    @NonNull
    @Override
    public EventAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtxEvent);
        View view = inflater.inflate(R.layout.event_list_layout, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    /**
     * retrieving event object data and binding it to the Product Holder
     * capability to register to the events held within each card.
     * Google Map API allows users to see the event geo point.
     */
    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ProductViewHolder holder, int position) {
        EventDataObject eventDataObject = eventobjList.get(position);
        holder.textViewTitle.setText(eventDataObject.getTitle());
        holder.textViewDescription.setText(eventDataObject.getDescription());
        holder.textViewDate.setText(eventDataObject.getDate().toString());
        holder.textViewTime.setText(eventDataObject.getTime().toString());
        holder.location = eventDataObject.getLocation();
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.un_sub.setEnabled(false);
        holder.un_sub.setVisibility(View.INVISIBLE);
        if(eventDataObject.getRegisteredUsers().contains(user)){
            holder.button.setVisibility(View.INVISIBLE);
            holder.button.setEnabled(false);
            holder.un_sub.setEnabled(true);
            holder.un_sub.setVisibility(View.VISIBLE);
        }
        holder.un_sub.setOnClickListener(v -> {
            String event = eventDataObject.getID();
            UnRegisterToEvent(event);
            holder.un_sub.setEnabled(false);
            holder.un_sub.setText("No longer Registered");
            Toast.makeText(mCtxEvent,"Registered to event",Toast.LENGTH_SHORT).show();
        });

        holder.button.setOnClickListener(v -> {
            String event = eventDataObject.getID();
           RegisterToEvent(event);
           holder.button.setEnabled(false);
           holder.button.setText("Already Registered");

            Toast.makeText(mCtxEvent,"Registered to event",Toast.LENGTH_SHORT).show();
        });

        GoogleMap thisMap = holder.mapViewLocation;
        if(thisMap!=null){
            thisMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(holder.location.getLatitude(),holder.location.getLongitude())));
        }
    }

    @Override
    public int getItemCount() {
        return eventobjList.size();
    }


    /**
     * functionality to register to the events held within each card.
     * register status is held within Firebase
     */
    public void RegisterToEvent(String event){
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference eventRef =
                FirebaseFirestore.getInstance().collection("Events").document(event);

                eventRef.update("Attendees",FieldValue.arrayUnion(user));
        FirebaseFirestore.getInstance().collection("user").document(user)
                .update("subscribedEvents",FieldValue.arrayUnion(eventRef));
    }
    public void UnRegisterToEvent(String event){
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference eventRef =
                FirebaseFirestore.getInstance().collection("Events").document(event);

        eventRef.update("Attendees",FieldValue.arrayRemove(user));
        FirebaseFirestore.getInstance().collection("user").document(user)
                .update("subscribedEvents",FieldValue.arrayRemove(eventRef));
        this.notifyDataSetChanged();
    }
    /**
     * initialising the skeleton structure of each card in the Recycler Product Holder
     * stylised using the 'event_list_layout' resource layout file.
     */
    class ProductViewHolder extends RecyclerView.ViewHolder  implements OnMapReadyCallback {

        TextView textViewTitle, textViewDescription, textViewDate, textViewTime;
        GeoPoint location;
        Button button,un_sub;
        GoogleMap mapViewLocation;
        MapView map;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDesc);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            button = itemView.findViewById(R.id.button);
            un_sub = itemView.findViewById(R.id.unregister);

            map = itemView.findViewById(R.id.imageMap);
            if(map!=null){
                map.onCreate(null);
                map.onResume();
                map.getMapAsync(this);
            }

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(mCtxEvent);
            mapViewLocation = googleMap;
            mapViewLocation.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mapViewLocation.getUiSettings().setZoomControlsEnabled(true);
            mapViewLocation.getUiSettings().setZoomGesturesEnabled(false);
            mapViewLocation.getUiSettings().setCompassEnabled(false);

            //Place current location marker
            com.google.android.gms.maps.model.LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            // TODO: Change this marker to user profileImage
            MarkerOptions markerOptions = new MarkerOptions();

            //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(profileImage));
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mapViewLocation.addMarker(markerOptions);

            //move map camera & animation
            //TODO: add a boundary here so it doesnt change positions
            mapViewLocation.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mapViewLocation.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
    }

}
