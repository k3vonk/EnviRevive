package com.KGRJJ.kgrjj_android_20192020;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.*;
import android.transition.TransitionInflater;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        FirebaseAuth.AuthStateListener mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            assert user != null;
            Toast.makeText(getApplicationContext(), "Signed in user => " + user.getUid(), Toast.LENGTH_SHORT)
                    .show();
        };
    }
    private void setupWindowAnimations(){
        Transition slide = TransitionInflater
                .from(getApplicationContext())
                .inflateTransition(R.transition.activity_fade);
        getWindow().setEnterTransition(slide);
        getWindow().setExitTransition(slide);
    }
}
