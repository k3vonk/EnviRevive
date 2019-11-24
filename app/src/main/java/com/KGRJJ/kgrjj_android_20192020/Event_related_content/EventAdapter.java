package com.KGRJJ.kgrjj_android_20192020.Event_related_content;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.KGRJJ.kgrjj_android_20192020.R;




import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ProductViewHolder> {

    private Context mCtxEvent;
    private List<EventDataObject> eventobjList;

    public EventAdapter(Context mCtx, List<EventDataObject> eventobjList) {
        this.mCtxEvent = mCtxEvent;
        this.eventobjList = eventobjList;
    }

    @NonNull
    @Override
    public EventAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtxEvent);
        View view = inflater.inflate(R.layout.list_layout, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ProductViewHolder holder, int position) {
        EventDataObject eventDataObject = eventobjList.get(position);
        holder.textViewTitle.setText(eventDataObject.getTitle());
        holder.textViewDescription.setText(eventDataObject.getDescription());
        holder.textViewDate.setText(eventDataObject.getDate().toString());
        holder.textViewTime.setText(eventDataObject.getTime().toString());
        holder.textViewLocation.setText(eventDataObject.getLocation().toString());
    }

    @Override
    public int getItemCount() {
        return eventobjList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewDescription, textViewDate, textViewTime, textViewLocation;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDesc);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);

        }
    }
}
