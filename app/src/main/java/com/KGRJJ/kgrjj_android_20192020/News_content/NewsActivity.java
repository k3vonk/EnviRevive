package com.KGRJJ.kgrjj_android_20192020.News_content;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends BaseActivity {
   // private TextView mTextViewResult;
    private RequestQueue mQueue;

    private ArrayList<NewsObjects> mNewsObj;
    private NewsAdapter mNewsAdapter;
    private RecyclerView mRecycleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mRecycleView = findViewById(R.id.recycler_view);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mNewsObj = new ArrayList<>();
        jsonParse();

    }

    @Override
    protected int getLayoutResourceID() { return R.layout.activity_news;}

    public void jsonParse(){
        String URL = "https://newsapi.org/v2/top-headlines?country=ie&category=business&apiKey=f05bc125586849b5b53391ce06b75ae9";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("articles");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject article = jsonArray.getJSONObject(i);
                                NewsObjects newsObjects = new NewsObjects();
                             //   NewsObjects newsObjects = new NewsObjects();
/*
                                //use object instead
                                newsObjects.setTitle(article.getString("title"));
                                newsObjects.setAuthor(article.getString("author"));
                                newsObjects.setDescription(article.getString("description"));
                                newsObjects.setURL(article.getString("url"));
                                newsObjects.setPreviewImg(article.getString("urlToImage"));
                                newsObjects.setDate(article.getString("publishedAt"));
*/

                                String title = article.getString("title");
                                String author = article.getString("author");
                                String description = article.getString("description");
                                String url = article.getString("url");
                                String image = article.getString("urlToImage");
                                String date = article.getString("publishedAt");

                            //    mTextViewResult.setText(title);
                      //          mNewsObj.add(new NewsObjects(title, description, author, date, image, url));
                     //           mNewsObj.add(new NewsObjects("haha", "haha", "haha", "haha", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=200&w=200", "haha"));
                            }
                                mNewsAdapter = new NewsAdapter(NewsActivity.this, mNewsObj);
                                mRecycleView.setAdapter(mNewsAdapter);

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

}
