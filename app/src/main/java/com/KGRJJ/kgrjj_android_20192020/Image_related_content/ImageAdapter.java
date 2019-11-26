package com.KGRJJ.kgrjj_android_20192020.Image_related_content;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<com.KGRJJ.kgrjj_android_20192020.Image_related_content.ImageAdapter.ProductViewHolder> {

    private Context mCtxImage;
    private List<ImageDataObject> imageobjList;
    private StorageReference mStorage;

    public ImageAdapter(Context mCtx, List<ImageDataObject> imageobjList) {
        this.mCtxImage = mCtx;
        this.imageobjList = imageobjList;
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(
                "gs://kgrjj-android-2019.appspot.com/images");
    }

    @NonNull
    @Override
    public com.KGRJJ.kgrjj_android_20192020.Image_related_content.ImageAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtxImage);
        View view = inflater.inflate(R.layout.image_list_layout, null);
        com.KGRJJ.kgrjj_android_20192020.Image_related_content.ImageAdapter.ProductViewHolder holder = new com.KGRJJ.kgrjj_android_20192020.Image_related_content.ImageAdapter.ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull com.KGRJJ.kgrjj_android_20192020.Image_related_content.ImageAdapter.ProductViewHolder holder, int position) {
        ImageDataObject imageDataObject = imageobjList.get(position);
        Log.i("UWU",imageDataObject.getImage());
        StorageReference profileRef = mStorage.child("images/"+imageDataObject.getImage());
        profileRef.getDownloadUrl()
                .addOnSuccessListener(uri-> {
                    Glide.with(mCtxImage)
                            .load(uri)
                            .into(holder.urlImage);
                });

        holder.geoPointImage = imageDataObject.getGeoPoint();
        GoogleMap thisMap = holder.googleMapImage;
        if (thisMap != null) {
            thisMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(holder.geoPointImage.getLatitude(), holder.geoPointImage.getLongitude())));
        }
        //holder.textViewLocation

    }

    @Override
    public int getItemCount() {
        return imageobjList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        ImageView urlImage;
        GeoPoint geoPointImage;
        GoogleMap googleMapImage;
        MapView mapViewImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);


            urlImage = itemView.findViewById(R.id.imageImage);

            mapViewImage = itemView.findViewById(R.id.mapImageView);


            if (mapViewImage != null) {
                mapViewImage.onCreate(null);
                mapViewImage.onResume();
                mapViewImage.getMapAsync(this);
            }

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(mCtxImage);
            googleMapImage = googleMap;
            googleMapImage.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMapImage.getUiSettings().setZoomControlsEnabled(true);
            googleMapImage.getUiSettings().setZoomGesturesEnabled(false);
            googleMapImage.getUiSettings().setCompassEnabled(false);

            //Place current location marker
            com.google.android.gms.maps.model.LatLng latLng = new LatLng(geoPointImage.getLatitude(), geoPointImage.getLongitude());

            // TODO: Change this marker to user profileImage
            MarkerOptions markerOptions = new MarkerOptions();

            //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(profileImage));
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            googleMapImage.addMarker(markerOptions);

            //move map camera & animation
            //TODO: add a boundary here so it doesnt change positions
            googleMapImage.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMapImage.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
    }
}
