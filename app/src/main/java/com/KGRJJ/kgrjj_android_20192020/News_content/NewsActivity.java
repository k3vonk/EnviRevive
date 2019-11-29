package com.KGRJJ.kgrjj_android_20192020.News_content;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends BaseActivity implements NewsAdapter.OnItemClickListener {
    public static final String WEB_URL = "url";

    private RequestQueue mQueue;
    private ArrayList<NewsObjects> mNewsObj;
    private NewsAdapter mNewsAdapter;
    private RecyclerView mRecycleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_news);

        mNewsObj = new ArrayList<>();

        mRecycleView = findViewById(R.id.recycler_view);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mQueue = Volley.newRequestQueue(this    );
        parseNews();

    }

    @Override
    protected int getLayoutResourceID() { return R.layout.activity_news;}

    //function to parse the JSON data from the NewsAPI
    public void parseNews(){
        //using NewsAPI key
        //Change "ireland+environment"
        String URL = "https://newsapi.org/v2/everything?q=ireland+environment&apiKey=f05bc125586849b5b53391ce06b75ae9";

        //request objects
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("articles");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject article = jsonArray.getJSONObject(i);

                                //get data from the JSON
                                String title = article.getString("title");
                                String author = article.getString("author");
                                String description = article.getString("description");
                                String url = article.getString("url");
                                String image = article.getString("urlToImage");
                                String date = article.getString("publishedAt");

                             mNewsObj.add(new NewsObjects(title, description, author, date, image, url));

                            }
                            //TODO

                             mNewsAdapter = new NewsAdapter(NewsActivity.this, mNewsObj);
                             mRecycleView.setAdapter(mNewsAdapter);

                             mNewsAdapter.setOnItemClicklistener(NewsActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                });

        mQueue.add(request);
    }

    //clicker to access the news web page
    @Override
    public void onItemClick(int position) {
        Intent webpageIntent = new Intent(this, NewsWebView.class);
        NewsObjects clicked = mNewsObj.get(position);

        webpageIntent.putExtra(WEB_URL, clicked.getURL());
        startActivity(webpageIntent);
    }
}
