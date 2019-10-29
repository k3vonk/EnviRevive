package com.KGRJJ.kgrjj_android_20192020.Services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class UserProfileDataService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UserProfileDataService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(
                "gs://kgrjj-android-2019.appspot.com/images");;
        Bundle extra = intent.getExtras();
        String userID = extra.getString("ID");
        StorageReference profileRef = mStorageRef
                .child(userID + "/profileImage.png");
        profileRef.getDownloadUrl().addOnSuccessListener(uri ->{
            String root = Environment.getExternalStorageDirectory().toString();
            root+="/"+userID+"_images";

            SaveImage(uri,root,userID);
        });

    }
    private void SaveImage(Uri finalBitmap, String path, String imageName){

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), finalBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(path);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String fname = imageName+".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
