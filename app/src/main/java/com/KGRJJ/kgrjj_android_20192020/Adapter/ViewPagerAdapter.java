package com.KGRJJ.kgrjj_android_20192020.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.KGRJJ.kgrjj_android_20192020.R;

import java.util.TreeMap;

public class ViewPagerAdapter extends PagerAdapter {

    Context mCtx;
    LayoutInflater mInflater;

    public ViewPagerAdapter(Context ctx){
        this.mCtx = ctx;
    }

    public int[] images = {
            R.mipmap.ic_launcher_envi,
            R.mipmap.cameratake,
            R.mipmap.searchimages,
            R.mipmap.eventadd,
            R.mipmap.eventsearch,
            R.mipmap.userprofile,
            R.mipmap.mapmarker,
            R.mipmap.exit
    };

    public String[] Headings = {
            "ENVI-REVIVE",
            "TAKE PHOTO",
            "BROWSE PHOTOS",
            "CREATE EVENTS",
            "BROWSE EVENTS",
            "USER PROFILE",
            "MAP",
            "LOGOUT",
    };
    public int[] descriptions = {
            R.string.AppDescription,
           R.string.TakePhoto,
           R.string.BrowsePhoto,
           R.string.CreateEvent,
           R.string.BrowseEvents,
           R.string.UserProfile,
           R.string.Map,
           R.string.Logout
    };
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }


    public Object instantiateItem(ViewGroup cont,int pos){
        mInflater = (LayoutInflater) mCtx.getSystemService(mCtx.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.tut_slide_layout,cont,false);

        ImageView mImage = view.findViewById(R.id.TutotalLayoutImage);
        TextView mTitile = view.findViewById(R.id.title_tut);
        TextView mDesc = view.findViewById(R.id.tut_description);
        mImage.setImageResource(images[pos]);
        mTitile.setText(Headings[pos]);
        mDesc.setText(descriptions[pos]);
        cont.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup cont, int pos, Object o){
        cont.removeView((RelativeLayout)o);
    }
}
