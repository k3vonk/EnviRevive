package com.KGRJJ.kgrjj_android_20192020.Image_related_content;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoLocation;
import org.imperiumlabs.geofirestore.GeoQuery;

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
        //image = BitmapFactory.decodeResource(getResources(),R.mipmap.glenda);
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
                String URL = (String) doc.get("URL");
                Log.i("TESTING",URL.toString());
                ImageDataObject IDO =
                        new ImageDataObject(
                                geoPoint,       //geopoint
                                URL);  //ImageView

                images.add(IDO);

            }
            imageAdapter = new ImageAdapter(getApplicationContext(), images);
            recyclerView.setAdapter(imageAdapter);
        });
    }
}
