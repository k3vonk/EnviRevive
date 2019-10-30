package com.KGRJJ.kgrjj_android_20192020.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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
    public void UploadImage(Bitmap bmp){

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
}
