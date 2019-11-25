package com.KGRJJ.kgrjj_android_20192020.Event_related_content;

import android.location.Location;

import com.KGRJJ.kgrjj_android_20192020.utilities.Date;
import com.KGRJJ.kgrjj_android_20192020.utilities.Time;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class EventDataObject {

    private String Title;
    private String description;
    private Date date;
    private Time time;
    private GeoPoint location;



    private ArrayList<String> imageURLS;


    public EventDataObject(String title, String description, Date date, Time time, GeoPoint location, ArrayList<String> imageURLS) {
        Title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.imageURLS = imageURLS;

    }


    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public ArrayList<String> getImageURLS(){
        return imageURLS;
    }


}