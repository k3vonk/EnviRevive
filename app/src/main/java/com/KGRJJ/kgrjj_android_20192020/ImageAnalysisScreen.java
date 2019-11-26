package com.KGRJJ.kgrjj_android_20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.KGRJJ.kgrjj_android_20192020.Adapter.LabelAdapter;
import com.KGRJJ.kgrjj_android_20192020.Authentication.PackageManagerUtils;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An Activity class that produces the image and analysis to users
 */
public class ImageAnalysisScreen extends AppCompatActivity {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyAtV-bOc020EN9DQSxbnTbG_8NYnHBa33M";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final String TAG = ImageAnalysisScreen.class.getSimpleName();

    private TextView mLoadingText;
    ImageButton imgButton;
    ImageView imgView;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_analysis_screen);
        imgView = findViewById(R.id.gallery_image_view);
        mLoadingText = findViewById(R.id.loading_text);
        gridView = findViewById(R.id.gridView);

        //Finish Activity onClick
        imgButton = findViewById(R.id.imageButton);
        imgButton.setOnClickListener(v -> onBackPressed());

        //Load up bitmap
        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        setPic(filename);

        callCloudVision(bmp);

    }
    private void setPic(String filename) {
        // Get the dimensions of the View
        int targetW = imgView.getWidth();
        int targetH = imgView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filename, bmOptions);
        imgView.setImageBitmap(bitmap);
    }

    /*=================== CLOUD VISION ===================*/
    /**
     * Call Cloud Vision API to compute labels
     * @param bitmap Bitmap image to be analyzed
     */
    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
        mLoadingText.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, HashMap<String, Float>> labelDetectionTask = new LabelDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    /**
     * Call Cloud Vision API to annotate the image provided
     */
    private Vision.Images.Annotate prepareAnnotationRequest(Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("LABEL_DETECTION");
                labelDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(labelDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG, "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    /**
     * An inner class to detect labels
     */
    private static class LabelDetectionTask extends AsyncTask<Object, Void, HashMap<String, Float>> {
        private final WeakReference<ImageAnalysisScreen> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LabelDetectionTask(ImageAnalysisScreen activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected HashMap<String, Float> doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToHashMap(response);

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            HashMap<String, Float> invalid = new HashMap<>();
            invalid.put("Cloud Vision API request failed. Check logs for details.", 0.0f);
            return invalid;
        }

        protected void onPostExecute(HashMap<String, Float> result) {
            ImageAnalysisScreen activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                 // Switch text to Finished
                 TextView imageDetail = activity.findViewById(R.id.loading_text);
                 imageDetail.setText(R.string.image_classification_labels);

                 //Set GridView Layout if labels are created for an image
                 if(result != null){
                     GridView gridView = activity.findViewById(R.id.gridView);
                     LabelAdapter labelAdapter = new LabelAdapter(activity, result);
                     gridView.setAdapter(labelAdapter);
                 }
                 else{
                     String message = "No Labels Found";
                     imageDetail.setText(message);
                 }

            }
        }
    }

    /**
     * Converts the response to a HashMap for the GridView to interpret.
     * @param response labels created from an image
     * @return HashMap containing the label,value pair
     */
    private static HashMap<String,Float> convertResponseToHashMap(BatchAnnotateImagesResponse response) {
        HashMap<String, Float> hashMap = new HashMap<>();
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if(labels != null){
            for(EntityAnnotation label: labels){
                hashMap.put(label.getDescription(), label.getScore());
            }
        }

        return hashMap;
    }


}
