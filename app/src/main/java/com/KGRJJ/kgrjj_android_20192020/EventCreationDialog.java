package com.KGRJJ.kgrjj_android_20192020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.KGRJJ.kgrjj_android_20192020.Event_related_content.EventCreation;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventCreationDialog extends Dialog implements View.OnClickListener{

    Context context;
    private TextView mTitle;
    private TextView mDescription;
    private TextView mDate;
    private TextView mTime;
    private Button mButton;
    private EventCreation eventCreation;
    private FirebaseFirestore db;
    private FirebaseUser user;

    public EventCreationDialog(@NonNull Context context, FirebaseFirestore db, FirebaseUser user) {
        super(context);
        this.context=context;
        this.db = db;
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_event_creation);
        mTitle = findViewById(R.id.EVENT_TITLE);
        mDescription = findViewById(R.id.EVENT_DESCRIPTION);
        mDate = findViewById(R.id.EVENT_DATE);
        mTime = findViewById(R.id.EVENT_TIME);
        mButton = findViewById(R.id.CREATE_EVENT_BTN);
        eventCreation = new EventCreation(db,user);
        mButton.setOnClickListener(v1 -> {
            Toast.makeText(context,"Testing",Toast.LENGTH_SHORT).show();
            eventCreation.CreateEvent(mTitle.getText().toString(),
                    mDescription.getText().toString(),
                    mDate.getText().toString(),
                    mTime.getText().toString()
            );
            this.dismiss();
        });
    }


    @Override
    public void onClick(View v) {

    }
}
