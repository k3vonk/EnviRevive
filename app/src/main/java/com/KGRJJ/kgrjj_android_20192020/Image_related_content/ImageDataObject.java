package com.KGRJJ.kgrjj_android_20192020.Image_related_content;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class ImageDataObject {

    private String image;
    private GeoPoint geoPoint;


    public ImageDataObject(GeoPoint geoPoint, String image) {
        this.image = image;
        this.geoPoint = geoPoint;
    }

    public String  getImage() {
        return image;
    }
    public GeoPoint getGeoPoint() {
        return geoPoint;
    }


}
