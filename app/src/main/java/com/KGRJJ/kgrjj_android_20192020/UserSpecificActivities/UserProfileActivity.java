package com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.KGRJJ.kgrjj_android_20192020.MainActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView profile_name;
    private String username;
    private Image profileImage;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profile_name = findViewById(R.id.profile_username);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        getUserName();
        Log.i("HELLO","USER NAME RETRIEVED: "+username);

        findViewById(R.id.SignOutBtn_profile).setOnClickListener(this);
    }


    /* method that connects to firestore and finds the user with the same ID as the currently signed
        in user. "Username" string is then assigned the value found form the database.
     */

    private void getUserName(){
        user = mAuth.getCurrentUser();
        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.i("IDS",document.getId());
                        Log.i("User ID: ",user.getUid());
                        if(document.getId().equals(user.getUid())){
                            username = document.getString("Username");
                            Log.i("Result","Match Found: "+username);
                            profile_name.setText(username);
                        }
                    }
                } else {
                    username = "failed";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.SignOutBtn_profile) {
            mAuth.signOut();
            Intent myIntent = new Intent(this, MainActivity.class);
            Toast.makeText(this,"Starting main activity",Toast.LENGTH_SHORT);
            startActivity(myIntent);
        }
    }

}
