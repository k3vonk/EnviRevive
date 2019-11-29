package com.KGRJJ.kgrjj_android_20192020;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.KGRJJ.kgrjj_android_20192020.Adapter.ViewPagerAdapter;
import com.KGRJJ.kgrjj_android_20192020.Authentication.RegistrationActivity;

public class FirstTimeUserScreen extends AppCompatActivity {

    private ViewPager mSlideView;
    private LinearLayout mTutLinear;
    private Button mButton;
    private int currentPage;
    private TextView[] mdots;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_user_screen);

        mSlideView = findViewById(R.id.viewpager_tut);
        mTutLinear = findViewById(R.id.tutlayout);
        mButton = findViewById(R.id.button2);
        mButton.setEnabled(false);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstTimeUserScreen.this, RegistrationActivity.class);
                startActivity(i);
                finish();
            }
        });
        adapter = new ViewPagerAdapter(this);
        mSlideView.setAdapter(adapter);

        addDotsIndicator(0);
        mSlideView.addOnPageChangeListener(viewListener);
    }

    public void addDotsIndicator(int pos){
        mdots = new TextView[7];
        mTutLinear.removeAllViews();
        for(int i = 0;i<mdots.length;i++){
            mdots[i] = new TextView(this);
            mdots[i].setText(""+(i+1));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.bpDark_gray));
            mTutLinear.addView(mdots[i]);
        }
        if(mdots.length > 0){
            mdots[pos].setTextColor(getResources().getColor(R.color.mdtp_white));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position;
            if(position == mdots.length-1){
                mButton.setEnabled(true);
            }
            else{
                mButton.setEnabled(false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
