package com.KGRJJ.kgrjj_android_20192020.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.KGRJJ.kgrjj_android_20192020.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * LabelAdapter class provides implementations to the GridView in Image Analysis Screen
 *
 * @author Ga Jun Young, Jackie Ju, Joiedel Agustin, Kiowa Daly, Rebecca Lobo
 * @since 26-11-2019
 */
public class LabelAdapter extends BaseAdapter {

    private final Context mContext;
    private final HashMap<String, Float> labels;
    private List<String> keyList;

    public static final String[] candidates = {"Pollution", "Waste", "Litter"};

    /**
     * Constructor instantiate multiple private variables and
     * carry out sorting of the HashMap to display them in descending order
     */
    public LabelAdapter(Context context, HashMap<String, Float> labels){
        this.mContext = context;
        this.labels = labels;

        //Sort Map by descending order of values
        Map<String, Float> sorted = labels
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));


        Map<String, Float> badLabels = new HashMap<>();
        //Find the bad labels
        for(String candidate: candidates){
            Float result = sorted.get(candidate);
            sorted.remove(candidate);
            if(result != null)
                badLabels.put(candidate, result);
        }

        //Sort the bad labels
        badLabels = badLabels
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        badLabels.putAll(sorted); //put items from the first hashmap to the next
        keyList = new ArrayList<>(badLabels.keySet()); //Create a list of String
    }

    /**
     * Obtains the size of the label HashMap
     */
    @Override
    public int getCount(){
        return labels.size();
    }

    /**
     * getItemId is a mandatory implementation from BaseAdapter
     */
    @Override
    public long getItemId(int position){
        return 0;
    }

    /**
     * getItem is a mandatory implementation from BaseAdapter
     */
    @Override
    public Object getItem(int position){
        return null;
    }

    /**
     * getView returns a specific view for a cell of GridView based on the
     * position index that is queried.
     */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Parsing to correct format
        String key = keyList.get(position);
        Float value = labels.get(key);
        int intPart = 0;
        String percentage = "";

        //Convert to integer and string format
        if(value != null){
            intPart = (int) (value * 100);
            percentage = intPart + "%";
        }

        /* Optimize gridView memory usage by instantiating a cell when it is null */
        if(convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.label_layout, null);
        }

        //Create references to our label layout
        final TextView labelText = convertView.findViewById(R.id.label_id);
        final TextView percentageText = convertView.findViewById(R.id.percentage_id);
        final ProgressBar progressBar = convertView.findViewById(R.id.progressBar);

        //Set values for above references
        labelText.setText(key);
        percentageText.setText(percentage);

        //Set color for specific labels
        if(Arrays.asList(candidates).contains(key))
        {
            progressBar.setProgress(intPart);
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }else{
            progressBar.setProgress(intPart);
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#D06AE467"))); //Pale green color

        }
        return convertView;
    }
}
