package com.KGRJJ.kgrjj_android_20192020.Event_related_content;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;


import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ProductViewHolder>{

    private Context mCtxEvent;
    private List<EventDataObject> eventobjList;

    public EventAdapter(Context mCtx, List<EventDataObject> eventobjList) {
        this.mCtxEvent = mCtx;
        this.eventobjList = eventobjList;
    }

    @NonNull
    @Override
    public EventAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtxEvent);
        View view = inflater.inflate(R.layout.list_layout, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ProductViewHolder holder, int position) {
        EventDataObject eventDataObject = eventobjList.get(position);
        holder.textViewTitle.setText(eventDataObject.getTitle());
        holder.textViewDescription.setText(eventDataObject.getDescription());
        holder.textViewDate.setText(eventDataObject.getDate().toString());
        holder.textViewTime.setText(eventDataObject.getTime().toString());
        holder.location = eventDataObject.getLocation();
        GoogleMap thisMap = holder.mapViewLocation;
        if(thisMap!=null){
            thisMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(holder.location.getLatitude(),holder.location.getLongitude())));
        }
        //holder.textViewLocation

    }

    @Override
    public int getItemCount() {
        return eventobjList.size();
    }



    class ProductViewHolder extends RecyclerView.ViewHolder  implements OnMapReadyCallback {

        TextView textViewTitle, textViewDescription, textViewDate, textViewTime;
        GeoPoint location;
        GoogleMap mapViewLocation;
        MapView map;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDesc);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);


            map = itemView.findViewById(R.id.eventMap);
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
