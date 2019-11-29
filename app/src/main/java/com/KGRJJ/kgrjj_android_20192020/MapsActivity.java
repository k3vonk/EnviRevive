package com.KGRJJ.kgrjj_android_20192020;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.KGRJJ.kgrjj_android_20192020.Adapter.LabelAdapter;
import com.KGRJJ.kgrjj_android_20192020.Image_related_content.SpecificImageDisplayActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The MapsActivity allows the tracking of users and display Google Maps
 *
 * @author Ga Jun Young, Jackie Ju, Joiedel Agustin, Kiowa Daly, Rebecca Lobo
 * @since 26-11-2019
 */
public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    //Map variables
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;

    Location mLastLocation;

    //HeatMap variables
    HeatmapTileProvider mHeatMapTileProvider;
    TileOverlay mTileOverlay;

    //Time
    Calendar time;
    private static final int REQUEST_PERMISSION_LOCATION_KEY = 99;


    /**
     * Instantiate Google Maps
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Map Loaded or Reloaded", Toast.LENGTH_SHORT).show();

        time = Calendar.getInstance();


        //Instantiation
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Recreate this activity on button back press.
     */
    @Override
    public void onBackPressed() {
        this.recreate();
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_maps;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * <p>
     * Reference: https://developers.google.com/maps/documentation/android-sdk/start
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Change Map based on time
        if(time.get(Calendar.HOUR_OF_DAY) >= 19 || time.get(Calendar.HOUR_OF_DAY) < 6) {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.mp_night_style));

                if (!success) {
                    Log.i("MapActivity", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("MapActivity", "Can't find style. Error: ", e);
            }
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Small widgets for the map
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        //Requesting location
        mLocationRequest = new LocationRequest();

        //Frequency settings
        mLocationRequest.setInterval(2 * 60 * 1000); //Every 2 minutes
        mLocationRequest.setFastestInterval(2 * 60 * 1000);

        //Accuracy settings
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //If permission is granted...
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        } else { //Request permission...
            checkLocationPermissions();
        }

        //TODO: heatmap update
        addHeatMap();
    }


    /**
     * Update map when permission is given
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_LOCATION_KEY) {//If the request is granted...
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) && (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION))) {
                //If permission location is granted...
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                } else { //Permission denied...
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Callback function to obtain new location and store the old one.
     * Update any additional features in regards to this new location
     */
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();

            if (!locationList.isEmpty()) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude()); //Log message for newest location
                mLastLocation = location;

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                // TODO: Change this marker to user profile
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera & animation
                //TODO: add a boundary here so it doesn't change positions
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

            }
        }
    };

    /**
     * Add HeatMap items to display new features on the map
     */
    private void addHeatMap() {
        readItems();
    }

    /**
     * Acquires various images from FireBase and adds it to the HeatMap
     */
    private void readItems() {
        ArrayList<WeightedLatLng> cleanList = new ArrayList<>();
        ArrayList<LatLng> cleanLatlng = new ArrayList<>();
        ArrayList<Circle> cleanCircle = new ArrayList<>();
        ArrayList<WeightedLatLng> dirtyList = new ArrayList<>();
        ArrayList<LatLng> dirtyLatlng = new ArrayList<>();
        ArrayList<Circle> dirtyCircle = new ArrayList<>();

        db.collection("Images").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null) {

                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    GeoPoint location = (GeoPoint) doc.get("Location");
                    WeightedLatLng weightedLatLng;
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    HashMap<String,Double> results = (HashMap<String, Double>) doc.get("AnalysisResults");

                    double pollution = 0.10000000f;
                    for(Map.Entry<String,Double> entry : results.entrySet()){

                        boolean result = Arrays.stream(LabelAdapter.candidates).anyMatch(entry.getKey()::equals);
                        if(result){
                            if(entry.getValue()>pollution){
                                pollution = entry.getValue();
                            }
                        }
                    }
                    // store the dirty points information into list
                    if(pollution!=0.1f){
                        double weight = 0.5 + pollution/2;
                        weightedLatLng = new WeightedLatLng(latLng,weight);
                        dirtyList.add(weightedLatLng);
                        dirtyLatlng.add(latLng);

                    }
                    // store the clean points information into list
                    else{
                        weightedLatLng = new WeightedLatLng(latLng,0.1);
                        cleanList.add(weightedLatLng);
                        cleanLatlng.add(latLng);
                    }

                }
            }
            // show clean points hearmap by default
            if (cleanList.size()!=0) {
                for(LatLng latLng : cleanLatlng){
                    CircleOptions circleOptions = new CircleOptions()
                            .center(latLng)
                            .strokeColor(Color.TRANSPARENT)
                            .clickable(true)
                            .radius(100); // In meters

                    // Get back the mutable Circle
                    Circle circle = mMap.addCircle(circleOptions);
                    cleanCircle.add(circle);
                }
                // Set gradient
                int[] colors = {
                        Color.rgb(0,255,0), // green
                        Color.rgb(0,255,0)  // red
                };

                // Set star point
                float[] startPoints = {
                        0.2f, 0.3f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                // Create a heat map tile provider, passing it the latlngs of the police stations
                mHeatMapTileProvider = new HeatmapTileProvider.Builder()
                        .weightedData(cleanList)
                        .gradient(gradient)
                        .opacity(0.7)
                        .build();

                // Add a tile overlay to the map, using the heat map tile provider.
                mTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mHeatMapTileProvider));
            }
            // change heatmaps
            Button button = findViewById(R.id.shift_heatmaps);
            button.setText("CLEAN");
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(button.getText().equals("CLEAN")){
                        mTileOverlay.remove();
                        button.setText("DIRTY");
                        Log.i("dirty" ,dirtyList.size()+"");
                        // delete circles for clean points
                        if(cleanCircle.size()!=0){
                            for(Circle circle : cleanCircle){
                                circle.remove();
                            }
                        }
                        if (dirtyList.size()!=0) {
                            // draw circles for dirty points
                            for(LatLng latLng : dirtyLatlng){
                                CircleOptions circleOptions = new CircleOptions()
                                        .center(latLng)
                                        .strokeColor(Color.TRANSPARENT)
                                        .clickable(true)
                                        .radius(100); // In meters

                                // Get back the mutable Circle
                                Circle circle = mMap.addCircle(circleOptions);
                            }
                            // reset settings for dirty points heatmap
                            // Set gradient
                            int[] colors = {
                                    Color.rgb(255,128,0), // green
                                    Color.rgb(255,0,0)  // red
                            };

                            // Set star point
                            float[] startPoints = {
                                    0.1f, 0.2f
                            };

                            Gradient gradient = new Gradient(colors, startPoints);
                            mHeatMapTileProvider.setGradient(gradient);
                            mHeatMapTileProvider.setWeightedData(dirtyList);
                            mHeatMapTileProvider.setOpacity(1);
                            mHeatMapTileProvider.setRadius(20);
                            mTileOverlay.clearTileCache();
                            mTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mHeatMapTileProvider));
                        }
                    }
                    else{
                        button.setText("CLEAN");
                        // remove dirty points heatmap
                        mTileOverlay.remove();
                        // remove circles for dirty points
                        if(dirtyCircle.size()!=0){
                            for(Circle circle : dirtyCircle){
                                circle.remove();
                            }
                        }
                        if (cleanList.size()!=0) {
                            // draw circles for clean points
                            for(LatLng latLng : cleanLatlng){
                                CircleOptions circleOptions = new CircleOptions()
                                        .center(latLng)
                                        .strokeColor(Color.TRANSPARENT)
                                        .clickable(true)
                                        .radius(100); // In meters

                                // Get back the mutable Circle
                                Circle circle = mMap.addCircle(circleOptions);
                            }
                            // reset settings for clean points heatmap
                            // Set gradient
                            int[] colors = {
                                    Color.rgb(0,255,0), // green
                                    Color.rgb(0,255,0)  // red
                            };

                            // Set star point
                            float[] startPoints = {
                                    0.2f, 0.3f
                            };

                            Gradient gradient = new Gradient(colors, startPoints);
                            mHeatMapTileProvider.setGradient(gradient);
                            mHeatMapTileProvider.setWeightedData(cleanList);
                            mHeatMapTileProvider.setRadius(10);
                            mHeatMapTileProvider.setOpacity(0.7);
                            mTileOverlay.clearTileCache();
                            mTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mHeatMapTileProvider));
                        }
                    }

                }
            });
            // show images corresponding to the geographic location of the image
            mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {

                @Override
                public void onCircleClick(Circle circle) {
                    Intent myIntent = new Intent(MapsActivity.this, SpecificImageDisplayActivity.class);
                    myIntent.putExtra("latitude",circle.getCenter().latitude);
                    myIntent.putExtra("longitude",circle.getCenter().longitude);
                    startActivity(myIntent);

                }
            });
        });

    }

    //TODO: different states
    @Override
    public void onPause() {
        super.onPause();

        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }
}
