package com.KGRJJ.kgrjj_android_20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileInputStream;

public class ImageAnalysisScreen extends AppCompatActivity {

    ImageButton imgButton;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_analysis_screen);
        imgView = findViewById(R.id.gallery_image_view);

        //Finish Activity onClick
        imgButton = (ImageButton)findViewById(R.id.imageButton);
        imgButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        //Load up bitmap
        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");

        try {
            FileInputStream stream = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imgView.setImageBitmap(bmp);


    }

    /**
     * Go back to previous activity.
     * @param item: The activity to return to
     * @return: true if a valid return, else false.
     */
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
