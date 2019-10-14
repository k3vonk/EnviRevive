package com.KGRJJ.kgrjj_android_20192020.Adapter;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.KGRJJ.kgrjj_android_20192020.Data.ImageData;
import com.KGRJJ.kgrjj_android_20192020.R;

import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;

public class WheelSelectionAdapter extends CursorWheelLayout.CycleWheelAdapter {

    private Context mContext;
    private List<ImageData> mSelections;
    private LayoutInflater mInflater;
    private int gravity;

    public WheelSelectionAdapter(Context mContext, List<ImageData> mSelections) {
        this.mContext = mContext;
        this.mSelections = mSelections;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount(){
        return mSelections.size();

    }

    public View getView(View parent, int position){
        ImageData mData = getItem(position);
        View root = mInflater.inflate(R.layout.wheel_image_layout, null, false);
        ImageView mImageView = (ImageView)root.findViewById(R.id.wheel_menu_item_tv);
        mImageView.setImageResource(mData.imageResources);
        return root;

    }

    public ImageData getItem(int position){
        return mSelections.get(position);
    }
}
