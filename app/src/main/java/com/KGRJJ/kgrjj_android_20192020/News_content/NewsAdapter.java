package com.KGRJJ.kgrjj_android_20192020.News_content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.R;

import java.util.ArrayList;


//implement methods
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context mCtxNews;
    private ArrayList<NewsObjects> mNewsObjList;

    //constructor
    public NewsAdapter(Context mCtxNews, ArrayList<NewsObjects> mNewsObjList) {
        this.mCtxNews = mCtxNews;
        this.mNewsObjList = mNewsObjList;

    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtxNews);
        View view = inflater.inflate(R.layout.list_news, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        /*
        holder.title.setText(mNewsObjList.get(position).getTitle());
        holder.description.setText(mNewsObjList.get(position).getDescription());
        holder.author.setText(mNewsObjList.get(position).getAuthor());
        holder.date.setText(mNewsObjList.get(position).getDate());
      //  holder.preview.setImageResource(mNewsObjList.get(position).getPreviewImg());
        Picasso.get().load(mNewsObjList.get(position).getURL()).fit().centerInside().into(holder.preview);

         */
        holder.title.setText(mNewsObjList.get(position).getTitle());
        holder.description.setText(mNewsObjList.get(position).getDescription());
        holder.author.setText(mNewsObjList.get(position).getAuthor());
       // holder.date.setText(mNewsObjList.get(position).getDate());

        /*
        holder.title.setText(mNewsObjList.get(position).getTitle());
        holder.description.setText(mNewsObjList.get(position).getDescription());
        holder.author.setText(mNewsObjList.get(position).getAuthor());
        holder.date.setText(mNewsObjList.get(position).getDate());
        Picasso.get().load(mNewsObjList.get(position).getPreviewImg()).fit().centerInside().into(holder.preview);
        */
    }

    @Override
    public int getItemCount() {
        return mNewsObjList.size();
    }

    //constructor
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, author, URL, date;
        ImageView preview;
        LinearLayout view_container;

        //attributes from list_news.xml
        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.news_TitleTextView);
            description = itemView.findViewById(R.id.news_DescriptionTextView);
            author = itemView.findViewById(R.id.news_AuthorTextView);
            preview = itemView.findViewById(R.id.news_imgPreview);
            date = itemView.findViewById(R.id.news_DateTextView);
          //  URL = itemView.findViewById(R.id.news)
        }
    }

}
