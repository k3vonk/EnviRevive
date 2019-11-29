package com.KGRJJ.kgrjj_android_20192020.News_content;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventAdapter;
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
        public static final String WEB_URL = "web_url";

    private TextView mTextViewResult;
    private RequestQueue mQueue;

    private ArrayList<NewsObjects> mNewsObj;
    private NewsAdapter mNewsAdapter;
    private RecyclerView mRecycleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mNewsObj = new ArrayList<>();

        mTextViewResult = findViewById(R.id.news_TextView);


        mRecycleView = findViewById(R.id.recycler_view);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

      //testInputs();
        mQueue = Volley.newRequestQueue(this    );
        jsonParse();

    }

    @Override
    protected int getLayoutResourceID() { return R.layout.activity_news;}


    //remove for later
    public void testInputs(){
        mTextViewResult.setText("WELCOME");
        String img_test = "https://www.weareopen.ie/wp-content/uploads/UCD-Logo.jpg";
        mNewsObj.add(new NewsObjects("title_test", "desc_test", "auth_test", "date_test", img_test, "haha"));
        mNewsObj.add(new NewsObjects("title_test2", "desc_test2", "auth_test2", "date_test2", img_test, "haha2"));
        //MOVED THESE THINGS TO HERE
        // THEY WERE IN JSONPARSE
        mNewsAdapter = new NewsAdapter(NewsActivity.this, mNewsObj);
        mRecycleView.setAdapter(mNewsAdapter);
    }

    public void jsonParse(){
        String URL = "https://newsapi.org/v2/everything?q=ireland+waste&apiKey=f05bc125586849b5b53391ce06b75ae9";

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

                             mNewsObj.add(new NewsObjects(title, description, author, date, image, url));
                       //         mTextViewResult.setText(title);

                       //         mNewsObj.add(new NewsObjects("haha", "haha", "haha", "haha", "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=200&w=200", "haha"));
                            }
                            //TODO
                            //ERROR 20:56	Emulator: Trying to erase a non-existent color buffer with handle 0
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

    @Override
    public void onItemClick(int position) {
        Intent webpageIntent = new Intent(this, NewsWebView.class);
        NewsObjects clicked = mNewsObj.get(position);

        webpageIntent.putExtra(WEB_URL, clicked.getURL());
        startActivity(webpageIntent);
    }
}
