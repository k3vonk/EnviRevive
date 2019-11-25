package com.KGRJJ.kgrjj_android_20192020.Image_related_content;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventAdapter;
import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventDataObject;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageDisplayActivity extends BaseActivity {

    private List<ImageDataObject> images;

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        images = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        getImages();
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_image_display;
    }

    private void getImages(){
        db.collection("Images").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                GeoPoint geoPoint = (GeoPoint) doc.get("Location");
                ImageView imageViewurl = findViewById(R.id.imageImage);


                Glide.with(getApplicationContext())
                        .load(doc.get("URL"))
                        .into(imageViewurl);    //imageview

                ImageDataObject IDO =
                        new ImageDataObject(
                                geoPoint,       //geopoint
                                imageViewurl);  //ImageView

                images.add(IDO);
            }
            imageAdapter = new ImageAdapter(getApplicationContext(), images);
            recyclerView.setAdapter(imageAdapter);
        });
    }
}
