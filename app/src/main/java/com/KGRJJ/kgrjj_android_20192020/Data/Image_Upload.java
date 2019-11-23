package com.KGRJJ.kgrjj_android_20192020.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.DescriptorProtos;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Image_Upload {
    private FirebaseFirestore mDatabase;
    private StorageReference mStorageRef;
    private FirebaseUser user;
    private Context context;

    public Image_Upload(FirebaseFirestore db, StorageReference StorageRef, FirebaseUser user, Context context){
        this.mDatabase=db;
        this.mStorageRef=StorageRef;
        this.user = user;
        this.context = context;
    }
    public void UplaodProfileImage(Bitmap bmp){
        StorageReference profileRef = mStorageRef.child(user.getUid() + "/profileImage.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileRef.putBytes(data);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(context, "Uploaded image", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed upload", Toast.LENGTH_SHORT).show();
        });
    }
    public void UploadImage(Bitmap bmp,Location location){




        String imagename = new Random().nextInt(10000) + 0 + "_"+location;
        StorageReference profileRef = mStorageRef.child("images/"+imagename);

        String url = "gs://kgrjj-android-2019.appspot.com/images/";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(context, "Uploaded image", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed upload", Toast.LENGTH_SHORT).show();
        });
        HashMap<String,Object> map = new HashMap<>();
        map.put("Location",new GeoPoint(location.getLatitude(),location.getLongitude()));
        map.put("URL",url+imagename);
        mDatabase.collection("Images").add(map);
    }
}
