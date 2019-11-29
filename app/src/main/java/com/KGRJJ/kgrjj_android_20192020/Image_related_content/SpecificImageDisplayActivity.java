package com.KGRJJ.kgrjj_android_20192020.Image_related_content;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SpecificImageDisplayActivity extends BaseActivity {

    private List<ImageDataObject> images;
    private List<ImageDataObject> specificImages;

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude",0);
        double longitude = intent.getDoubleExtra("longitude",0);
        GeoPoint geo = new GeoPoint(latitude,longitude);
        images = new ArrayList<>();
        specificImages = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        getImages(geo);
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_image_display;
    }

    private void getImages(GeoPoint geo){

        db.collection("Images").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                GeoPoint geoPoint = (GeoPoint) doc.get("Location");
                String URL = (String) doc.get("URL");
                ImageDataObject IDO =
                        new ImageDataObject(
                                geoPoint,       //geopoint
                                URL);  //ImageView

                images.add(IDO);
            }
            float[] distance = new float[1];
            for(ImageDataObject ido : images){
                Location.distanceBetween(ido.getGeoPoint().getLatitude(),ido.getGeoPoint().getLongitude()
                ,geo.getLatitude(),geo.getLongitude(),distance);
                if(distance[0]<50){
                    specificImages.add(ido);
                }
            }
            imageAdapter = new ImageAdapter(getApplicationContext(), specificImages);
            recyclerView.setAdapter(imageAdapter);
        });
    }
}
