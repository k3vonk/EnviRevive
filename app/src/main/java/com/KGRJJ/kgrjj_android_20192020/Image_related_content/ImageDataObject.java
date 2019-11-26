package com.KGRJJ.kgrjj_android_20192020.Image_related_content;

import android.view.View;
import android.widget.ImageView;

import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class ImageDataObject {

    private ImageView imageURL;
    private GeoPoint geoPoint;


    public ImageDataObject(GeoPoint geoPoint, ImageView imageURL) {
        this.imageURL = imageURL;
        this.geoPoint = geoPoint;
    }

    public ImageView getImageURL() {
        return imageURL;
    }
    public GeoPoint getGeoPoint() {
        return geoPoint;
    }


}
