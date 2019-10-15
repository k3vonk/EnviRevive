/*
Java file to house the base code for our home page. Titled mainActivity as this will be the base of the app.
 */

package com.KGRJJ.kgrjj_android_20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.KGRJJ.kgrjj_android_20192020.Authentication.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/*Class definition contains Implements "View.OnClickListener - this allows us to create one
    OnClick method and have an if statement or switch case rather than create a listener for each
    button.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRegisterButton = findViewById(R.id.signUpBtn_homeScreen);

        mRegisterButton.setOnClickListener(this);
        //"This" refers to the OnClick Listener defined below
    }

    //A method used to create a new activity. Will be fleshed out with configuration etc later on

    private void LaunchRegistration(){
        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.signUpBtn_homeScreen){
            LaunchRegistration();
        }
    }
}
