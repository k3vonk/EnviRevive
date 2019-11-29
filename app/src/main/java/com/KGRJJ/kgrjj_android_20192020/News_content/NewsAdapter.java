package com.KGRJJ.kgrjj_android_20192020.News_content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//implement methods
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context mCtxNews;
    private ArrayList<NewsObjects> mNewsObjList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    public void setOnItemClicklistener(OnItemClickListener listener){
        mListener = listener;
    }

    //constructor
    public NewsAdapter(Context mCtxNews, ArrayList<NewsObjects> mNewsObjList) {
        this.mCtxNews = mCtxNews;
        this.mNewsObjList = mNewsObjList;

    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtxNews);
        View view = inflater.inflate(R.layout.list_news, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        NewsObjects eventDataObject = mNewsObjList.get(position);

        holder.title.setText(eventDataObject.getTitle());
        holder.description.setText(eventDataObject.getDescription());
        holder.author.setText(eventDataObject.getAuthor());
        holder.date.setText(eventDataObject.getDate());
        Picasso.get().load(mNewsObjList.get(position).getPreviewImg()).fit().centerInside().into(holder.preview);

    }

    @Override
    public int getItemCount() {
        return mNewsObjList.size();
    }

    //constructor
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, author, date;
        ImageView preview;

        //attributes from list_news.xml
        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.news_TitleTextView);
            description = itemView.findViewById(R.id.news_DescriptionTextView);
            author = itemView.findViewById(R.id.news_AuthorTextView);
            preview = itemView.findViewById(R.id.news_imgPreview);
            date = itemView.findViewById(R.id.news_DateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            mListener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

}
