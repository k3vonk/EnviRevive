package com.KGRJJ.kgrjj_android_20192020.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;


public class FirebaseStorageHandler {

    private StorageReference mStorageReference;

    public FirebaseStorageHandler(){
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    public void uploadPhoto(Uri file,String reference, Context context){
        if(file != null){
            StorageReference ref = mStorageReference.child(reference);
            ref.putFile(file)
                    .addOnSuccessListener(taskSnapshot -> Toast.makeText(context,
                            "Uploaded Image Succesfully",
                            Toast.LENGTH_LONG).show())
                    .addOnFailureListener(e -> Toast.makeText(context,"Failed" +
                            ""+e.getLocalizedMessage(),Toast.LENGTH_LONG).show());
        }
    }
//     STORING HER FOR FUTURE USE
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode,resultCode,data);
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
//                &&data != null && data.getData() != null) {
//            filePath = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
//                mUploadImageBTN.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
