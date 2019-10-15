package com.KGRJJ.kgrjj_android_20192020.Data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FirestoreDocumentModel {

    public HashMap<String,Object> addDataToHashMap(String Name, String Username, String City,
                                               String Country){
        HashMap<String,Object> UserData = new HashMap<>();

        String [] arr;
        arr = Name.split(" ", 2);
        String FName = arr[0];
        String LName = arr[1];
        String ImageRef = "";
        String ProfileImageRef = "";
        String Rank = "Fresh";
        int Points = 0;
        List<String> EventsAttended = new ArrayList<>();
        List<String> subscribedEvents = new ArrayList<>();
        List<String> Accolades = new ArrayList<>();
        UserData.put("FName",FName);
        UserData.put("LName",LName);
        UserData.put("username",Username);
        UserData.put("City",City);
        UserData.put("Country",Country);
        UserData.put("ImageRef",ImageRef);
        UserData.put("ProfileImageRef",ProfileImageRef);
        UserData.put("Rank",Rank);
        UserData.put("EventsAttended",EventsAttended);
        UserData.put("subscribedEvents",subscribedEvents);
        UserData.put("Accolades",Accolades);
        UserData.put("Points",Points);
        return UserData;
    }

}
