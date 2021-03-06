package com.KGRJJ.kgrjj_android_20192020.Authentication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.MapsActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.Services.UserProfileDataService;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/***
 * The Login activity produces an authentication screen that prompts the user to login using
 * their credentials. Should the user not have an account already created, an option to create
 * an account is present.
 *
 *
 * @author Ga Jun Young, Jackie Ju, Joiedel Agustin, Kiowa Daly, Rebecca Lobo
 * @since 07-10-2019
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{


    // Useful for Log messaged - assign a tag (this is placeholder for now)
    private String TAG = "LOGIN_ACTIVITY: ";

    //These variables will be assigned the text input areas of this screen.
    private TextView email;
    private TextView password;
    private Button mLoginBTN;
    private TextView signUp;
    private Animation fadein;

    //Provides animation transition between login and next activity
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //"Sign In" title fades in upon activity start up
        signUp = findViewById(R.id.Log_in_title);
        fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_in);
        signUp.startAnimation(fadein);

        // Adding listeners to both button - no need to store them in a variable since they
        // are only ever used for listening. "This links to the OnClick method defined below
        findViewById(R.id.createAccount).setOnClickListener(this);
        //create account is a clickable text to on the activity screen.

        // create account is a clickable text to on the activity screen.
        mLoginBTN = findViewById(R.id.loginBTN);
        mLoginBTN.setOnClickListener(this);
        mLoginBTN.setEnabled(false);
        animationView = findViewById(R.id.animation);
        animationView.setVisibility(View.INVISIBLE);

        //Essential for using Firebase authentication

        // find the text inputs based on their ID.
        email = findViewById(R.id.email_inputLoginScreen);
        password = findViewById(R.id.password_input_loginScreen);

        email.addTextChangedListener(new TextWatcher() { //check for input updates
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

    /* Method "signIn" written using Firebase tips. Note we dont use our input handlers here.
        This is because if the input isnt correct the FirebaseAuthentication method
        "signInWithEmailAndPassword" handles this.

     */
    private void signIn(String email, String password){
        animationView.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        user = task.getResult().getUser();
                        mAuth.updateCurrentUser(user);
                        getUserData(user); //pre-load the data required in userProfile
                        Intent intent = new Intent(this, MapsActivity.class);
                        startActivity(intent);
                        finish(); // end the login activity
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed: " +
                                        "username or password incorrect",
                                Toast.LENGTH_LONG).show();
                        //gives the user the capability to try to log in again
                        mLoginBTN.setVisibility(View.VISIBLE);
                        mLoginBTN.setEnabled(true);
                        //loading animation is stopped if authentication fails
                        animationView.setVisibility(View.INVISIBLE);
                    }
                });
    }


    /* login button is available if all details are entered correctly
        if they are not, the button will be inactive until this is
        resolved
    */
    private void checkRequiredFields() {
        if(email.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty()){
            mLoginBTN.setEnabled(false);
        }
        else{
            mLoginBTN.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.createAccount){
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }
        if(i == R.id.loginBTN){

            mLoginBTN.setVisibility(View.INVISIBLE);
            //ensure that the device is connectedto internet
            NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                    getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

            if (info == null)
            {
                Log.d(TAG,"no internet connection: Please connect to internet");
                Toast.makeText(getApplicationContext(),"Please connect to the internet",Toast.LENGTH_SHORT).show();
                //gives the user the capability to try to log in again
                mLoginBTN.setVisibility(View.VISIBLE);
                mLoginBTN.setEnabled(true);
                //loading animation is stopped if authentication fails
                animationView.setVisibility(View.INVISIBLE);
            }
            else
            {
                signIn(email.getText().toString(), password.getText().toString());
            }

        }
    }

    /*
        Function derived from BaseActivity
     */
    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_login;
    }
}
