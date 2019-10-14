package com.KGRJJ.kgrjj_android_20192020.Authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities.UserProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity implements View.OnClickListener{


    // Essential for using Firebase Authentication
    private FirebaseAuth mAuth;

    // Useful for Log messaged - assign a tag (this is placeholder for now)
    private String TAG = "log";

    //These variables will be assigned the text input areas of this screen.
    private TextView email;
    private TextView password;
    private Button mLoginBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Adding listeners to both button - no need to store them in a variable since they
            are only ever used for listening. "This links to the OnClick method defined below
         */
        findViewById(R.id.createAccount).setOnClickListener(this);
        // " create account is a clickable text to on the activity screen.
        mLoginBTN = findViewById(R.id.LoginButton_loginScreen);
        mLoginBTN.setOnClickListener(this);
        mLoginBTN.setEnabled(false);

        //Essential for using firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // find the text inputs based on their ID.
        email = findViewById(R.id.email_inputLoginScreen);
        password = findViewById(R.id.password_input_loginScreen);
//        mVideoView = findViewById(R.id.videoView);
//        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.mountains_video2);
//        mVideoView.setVideoURI(uri);
//        mVideoView.start();
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /* Function written using Firebase tips. Note we dont use our input handlers here.
        This is because if the input isnt correct the FirebaseAuthentication method
        "signInWithEmailAndPassword" does this.

     */
    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        Intent intent = new Intent(this, UserProfileActivity.class);
                        startActivity(intent);
                       // overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_botton);


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed: " +
                                        "username or password incorrect",
                                Toast.LENGTH_LONG).show();

                    }

                    // ...
                });
    }
    private void checkRequiredFields(){
        if(email.getText().toString().isEmpty() &&
                password.getText().toString().isEmpty()  ){
            mLoginBTN.setEnabled(true);
        }
        else{
            mLoginBTN.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.createAccount){
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(i == R.id.LoginButton_loginScreen){
            signIn(email.getText().toString(), password.getText().toString());
        }
    }

}
