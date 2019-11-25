package com.KGRJJ.kgrjj_android_20192020.Authentication;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.Data.FirestoreDocumentModel;
import com.KGRJJ.kgrjj_android_20192020.Data.Image_Upload;
import com.KGRJJ.kgrjj_android_20192020.MainActivity;
import com.KGRJJ.kgrjj_android_20192020.MapsActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities.UserProfileActivity;
import com.KGRJJ.kgrjj_android_20192020.utilities.FirebaseStorageHandler;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mukesh.countrypicker.CountryPicker;

import java.io.IOException;
import java.util.Map;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private InputHandler inputHandler = new InputHandler();
    private FirebaseStorageHandler fbh = new FirebaseStorageHandler();
    private FirestoreDocumentModel FDM = new FirestoreDocumentModel();
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mUsername;
    private Button mRegButton;
    private TextView mInfoReq;
    private TextView mCountry;
    private EditText mName;
    private EditText mCity;
    private ProgressBar mProgress;
    private LottieAnimationView mLottie;
    private static Bitmap image;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private boolean countryPicked;

    private static ImageView mTakePhoto;


    // [END declare_auth]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        countryPicked = false;
        // TextViews need to be stored as their content will be used later
        mName = findViewById(R.id.Name);
        mCity = findViewById(R.id.city);
        mTakePhoto = findViewById(R.id.takePhoto);
        mEmailField = findViewById(R.id.email_inputLoginScreen);
        mPasswordField = findViewById(R.id.password_Input);
        mUsername = findViewById(R.id.username_input);
        mRegButton = findViewById(R.id.registerBtn_regScreen);
        mCountry = findViewById(R.id.country_picker);
        mInfoReq = findViewById(R.id.infoRequired);
        mLottie = findViewById(R.id.animation_reg);
        mLottie.setVisibility(View.INVISIBLE);



        mTakePhoto.setOnClickListener(view-> {
            takePhoto(false, true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });






        //Buttons don't need to be stored as they are jus just for listeners

        mRegButton.setOnClickListener(this);
        findViewById(R.id.cancelSignUp).setOnClickListener(this);
        //KEEP THESE 3 MINIMISED TO AVOID CLUTTER
        mEmailField.addTextChangedListener(new TextWatcher() {
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

        mUsername.addTextChangedListener(new TextWatcher() {
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
        mCity.addTextChangedListener(new TextWatcher() {
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
        mName.addTextChangedListener(new TextWatcher() {
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
        mPasswordField.addTextChangedListener(new TextWatcher() {
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

        mRegButton.setEnabled(false);
        mCountry.setOnClickListener(this);
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        // [END initialize_auth]
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_registration;
    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //TODO
    }
    // [END on_start_check_user]




    /*
        Create a user method. We take in two strings for email and password.
        These are sent to a method defined in InputHandler which ensure the strings follow the
        desired specs.

        If it passes this check then we call on the FirebaseAuth method to create an account.

        Inside this method we call on addUser() - this is a method written by Kiowa that adds the
        newly created user to our firebase remote storage.
     */

    private boolean createAccount(String email, String password){


        if(inputHandler.isValidEmailInput(email)) {
            if (inputHandler.isValidPasswordCombination(password)) {
                // [START create_user_with_email]
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
//                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                user = task.getResult().getUser();
                                Log.i("TESTING", "uSER"+user.getUid());
                                mAuth.updateCurrentUser(user);
                                addUserData(user);
                                getUserData(user);


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });



            }else{
                Toast.makeText(getBaseContext(),                    "   Password must contain the following:    \n" +
                        "   atleast 1 digit\n"+
                        "   upper and lowerclass letters\n" +
                        "   special characters\n"+
                        "   8 characters\n",Toast.LENGTH_LONG).show();
                return false;
            }

        }
        else{
            Toast.makeText(getBaseContext(),"Email not valid",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    /* Method to add a user to our firebase storage.
        This method is a standard method that simply connects to our firebase - finds the collection
        named "users" and adds a username to the specified user. Our users are stored with specific
        User IDS - this id from firebase auth is then used as a document ID in our fire Store
        Each user has a username which is stored in the fire store along with profileImage image and
        other relevant data.
     */
    private void addUserData(FirebaseUser user){


        String uri = "gs://kgrjj-android-2019.appspot.com/images";
//        UserData.put("username",mUsername.getText().toString());

        BitmapDrawable bitmapDrawable = (BitmapDrawable) mTakePhoto.getDrawable();

        Map<String,Object> UserData = FDM.addDataToHashMap(mName.getText().toString(),mUsername.getText().toString(),
                mCity.getText().toString(),(uri+user.getUid()+"/profileImage.png"),mCountry.getText().toString());

        db.collection("user").document(user.getUid()).set(UserData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Log.d("TESTING",user.getUid());
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error writing document", e);
                    Log.d("TESTING",": USER NOT CREATED");
                });
        image_upload.UplaodProfileImage(thumbnail,user);
    }

    private void checkRequiredFields(){
        if(!mEmailField.getText().toString().isEmpty() &&
                !mPasswordField.getText().toString().isEmpty() &&
                !mUsername.getText().toString().isEmpty()
        && countryPicked &&
        !mName.getText().toString().isEmpty() &&
        !mCity.getText().toString().isEmpty()){
            mRegButton.setEnabled(true);
            mInfoReq.setEnabled(false);
        }
        else{
            mRegButton.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.registerBtn_regScreen) {
            mRegButton.setEnabled(false);
            mRegButton.setVisibility(View.INVISIBLE);
            if(createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString())) {
                Log.i("TESTING","createaccount ocured");

                mRegButton.setVisibility(View.INVISIBLE);
                mLottie.setVisibility(View.VISIBLE);
                mLottie.playAnimation();

                Thread loading = new Thread(){
                    public void run(){
                        try {
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent myIntentProfile = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(myIntentProfile);
                    }
                };
                loading.start();

            }else{
                mRegButton.setEnabled(true);
                mRegButton.setVisibility(View.VISIBLE);
            }

        }
        if(i == R.id.cancelSignUp){
            Intent myIntentMain = new Intent(this, LoginActivity.class);
            startActivity(myIntentMain);
        }
        if(i == R.id.country_picker){
            CountryPicker.Builder builder = new CountryPicker.Builder().with(this)
                    .listener(country -> {
                        mCountry.setText(country.getName());
                        countryPicked = true;
                        checkRequiredFields();
                    });
            CountryPicker picker = builder.build();
            picker.showDialog(this);
            countryPicked = true;
        }
    //fix test
    }
}