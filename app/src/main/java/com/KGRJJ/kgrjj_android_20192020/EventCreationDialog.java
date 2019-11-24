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

import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.Timestamp;

import java.time.LocalDateTime;
import java.util.Calendar;

public class EventCreationDialog extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {


    private TextView mTitle;
    private TextView mDescription;
    private Button mButton;
    private Button mDate;
    private Button mTime;
    private TextView mDateText;
    private TextView mTimeText;
    private EventCreation eventCreation;
    private GoogleMap mMap;
    private Location mLocation;
    private boolean datePicked;
    private boolean timePicked;
    public static Marker currentMaker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dialog_event_creation);
        datePicked = false;
        timePicked = false;
        com.KGRJJ.kgrjj_android_20192020.utilities.Date date;
        final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
        mTitle = findViewById(R.id.EVENT_TITLE);
        mDescription = findViewById(R.id.EVENT_DESCRIPTION);
        mDate = findViewById(R.id.EVETNT_DATE);
        mTime = findViewById(R.id.EVENT_TIME);
        mDateText = findViewById(R.id.EVENT_DATE_TEXT);
        mTimeText = findViewById(R.id.EVENT_TIME_TEXT);
        mButton = findViewById(R.id.CREATE_EVENT_BTN);
        eventCreation = new EventCreation(db,user);
        mDate.setOnClickListener(view ->{
            CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                    .setOnDateSetListener((dialog, year, monthOfYear, dayOfMonth) ->{ monthOfYear = monthOfYear+1;
                            mDateText.setText(dayOfMonth+"/"+monthOfYear+"/"+year);})
                    .setFirstDayOfWeek(Calendar.SUNDAY)
                    .setPreselectedDate(LocalDateTime.now().getYear(),
                            LocalDateTime.now().getMonthValue(),LocalDateTime.now().getDayOfMonth())
                    .setDoneText("Date selected")
                    .setThemeLight();
            cdp.show(getSupportFragmentManager(),FRAG_TAG_DATE_PICKER);
            datePicked = true;
        });
        mTime.setOnClickListener(view ->{
            RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                    .setOnTimeSetListener((dialog, hourOfDay, minute) -> mTimeText.setText(hourOfDay+":"+minute))
                    .setStartTime(10, 10)
                    .setDoneText("Yay")
                    .setCancelText("Nop")
                    .setThemeDark();
            rtpd.show(getSupportFragmentManager(), "fragment_time_picker_name");
            timePicked = true;
        });
        mButton.setEnabled(false);

        mButton.setOnClickListener(v1 -> {
            String[] mydate = mDateText.getText().toString().split("/");
            Date date1 = new Date(Integer.valueOf(mydate[0]),Integer.valueOf(mydate[1]),Integer.valueOf(mydate[2]));
            String[] myTime = mTimeText.getText().toString().split(":");
            Time time = new Time(Integer.valueOf(myTime[0]),Integer.valueOf(myTime[1]));
            Toast.makeText(getApplicationContext(),"Event Created",Toast.LENGTH_SHORT).show();
            eventCreation.CreateEvent(mTitle.getText().toString(),
                    mDescription.getText().toString(),date1,time,mLocation
            );
            mTitle.clearComposingText();
            mDescription.clearComposingText();

            mButton.setEnabled(false);
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //Small widgets for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.setOnMapLongClickListener(latLng -> {
            mMap.clear();
            // TODO: Change this marker to user profileImage
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            if(currentMaker !=null){
                currentMaker.setPosition(latLng);
                currentMaker = mMap.addMarker(markerOptions);
            }else{
                currentMaker = mMap.addMarker(markerOptions);
            }

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

    }

    private void CheckFields(){
        if(!mTitle.getText().toString().isEmpty()&&
            !mDescription.getText().toString().isEmpty() &&
            datePicked && timePicked &&
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
