package com.KGRJJ.kgrjj_android_20192020.News_content;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;

import static com.KGRJJ.kgrjj_android_20192020.News_content.NewsActivity.WEB_URL;

public class NewsWebView extends BaseActivity {
    WebView webView;

    @Override
    protected int getLayoutResourceID() {
        return 0;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_webpage);

        Intent intent = getIntent();
        String web_page = intent.getStringExtra(WEB_URL);


        webView = findViewById(R.id.webview);

        webView.loadUrl(web_page);
    }

}
