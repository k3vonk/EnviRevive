package com.KGRJJ.kgrjj_android_20192020.News_content;

import android.content.Intent;
import android.os.Bundle;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;

public class NewsWebView extends BaseActivity {


    @Override
    protected int getLayoutResourceID() {
        return 0;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();

    }
}
