package com.KGRJJ.kgrjj_android_20192020;

import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventCreation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventCreationDialog extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {


    private TextView mTitle;
    private TextView mDescription;
    private TextView mDate;
    private TextView mTime;
    private Button mButton;
    private EventCreation eventCreation;
    private GoogleMap mMap;
    private Location mLocation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dialog_event_creation);

        mTitle = findViewById(R.id.EVENT_TITLE);
        mDescription = findViewById(R.id.EVENT_DESCRIPTION);
        mDate = findViewById(R.id.EVENT_DATE);
        mTime = findViewById(R.id.EVENT_TIME);
        mButton = findViewById(R.id.CREATE_EVENT_BTN);
        eventCreation = new EventCreation(db,user);
        mButton.setEnabled(false);
        mButton.setOnClickListener(v1 -> {
            Toast.makeText(getApplicationContext(),"Event Created",Toast.LENGTH_SHORT).show();
            eventCreation.CreateEvent(mTitle.getText().toString(),
                    mDescription.getText().toString(),
                    mDate.getText().toString(),
                    mTime.getText().toString(),mLocation
            );
            mTitle.clearComposingText();
            mDescription.clearComposingText();
            mDate.clearComposingText();
            mTime.clearComposingText();
            mButton.setEnabled(false);
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Small widgets for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.setOnMapLongClickListener(latLng -> {

            // TODO: Change this marker to user profile
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions);
            mLocation = new Location("");
            mLocation.setLatitude(latLng.latitude);
            mLocation.setLongitude(latLng.longitude);
            CheckFields();
        });
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void CheckFields(){
        if(!mTitle.getText().toString().isEmpty()&&
            !mDescription.getText().toString().isEmpty() &&
            !mDate.getText().toString().isEmpty()&&
            !mTime.getText().toString().isEmpty()&&
            mLocation!=null){
            mButton.setEnabled(true);
        }
        else{
            mButton.setEnabled(false);
        }
    }
    @Override
    protected int getLayoutResourceID() {
        return R.layout.dialog_event_creation;
    }


    @Override
    public void onClick(View v) {

    }
}
