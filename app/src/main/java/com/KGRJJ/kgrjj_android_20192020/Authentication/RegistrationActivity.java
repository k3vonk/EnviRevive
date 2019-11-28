package com.KGRJJ.kgrjj_android_20192020.Authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.KGRJJ.kgrjj_android_20192020.BaseActivity;
import com.KGRJJ.kgrjj_android_20192020.Data.FirestoreDocumentModel;
import com.KGRJJ.kgrjj_android_20192020.MapsActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mukesh.countrypicker.CountryPicker;
import java.io.IOException;
import java.util.Map;

/***
 * The Registration activity produces an screen that prompts users to creat a profile.
 * Including profile image,Email,Password,Username,City and Country
 *
 *
 * @author Ga Jun Young, Jackie Ju, Joiedel Agustin, Kiowa Daly, Rebecca Lobo
 * @since 07-10-2019
 */
public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private boolean countryPicked;
    private TextView regTitle, mUsername, mInfoReq, mCountry;

    // Essential for using Firebase Authentication
    private FirestoreDocumentModel FDM = new FirestoreDocumentModel();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    //Visuals and user interaction
    private Button mRegButton;
    private EditText mEmailField, mPasswordField, mName, mCity;
    private InputHandler inputHandler = new InputHandler();
    private ProgressBar mProgress;
    private LottieAnimationView mLottie;
    private Animation fadein;
    private static Bitmap image;
    private static ImageView mTakePhoto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        countryPicked = false;

        //fade in animation for the "Sign Up" title and transition animation initialised
        regTitle = findViewById(R.id.RegisterTitle);
        fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_in);
        regTitle.startAnimation(fadein);
        mLottie = findViewById(R.id.animation_reg);
        mLottie.setVisibility(View.INVISIBLE);

        // TextViews need to be stored as their content will be used later
        mUsername = findViewById(R.id.username_input);
        mInfoReq = findViewById(R.id.infoRequired);
        mCountry = findViewById(R.id.country_picker);

        mName = findViewById(R.id.Name);
        mCity = findViewById(R.id.city);
        mEmailField = findViewById(R.id.email_inputLoginScreen);
        mPasswordField = findViewById(R.id.password_Input);
        mRegButton = findViewById(R.id.registerBtn_regScreen);
        mTakePhoto = findViewById(R.id.takePhoto);

        mTakePhoto.setOnClickListener(view-> {
            takePhoto(false, true);
            ImageView m = findViewById(R.id.takePhoto);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //Note: Buttons don't need to be stored as they are just used for listeners
        mRegButton.setOnClickListener(this);
        findViewById(R.id.cancelSignUp).setOnClickListener(this);
        mRegButton.setEnabled(false);
        mCountry.setOnClickListener(this);

        //listeners for the when the user inputs into the fields
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
                Toast.makeText(getBaseContext(),
                        "   Password must contain the following:    \n" +
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

        String uri = "gs://kgrjj-android-2019.appspot.com/images/";

        Map<String,Object> UserData = FDM.addDataToHashMap(mName.getText().toString(),mUsername.getText().toString(),
                mCity.getText().toString(),(uri+user.getUid()+"/profileImage.jpg"),mCountry.getText().toString());

        db.collection("user").document(user.getUid()).set(UserData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Log.d("TESTING",user.getUid());
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error writing document", e);
                    Log.d("TESTING",": USER NOT CREATED");
                });
        Bitmap bmp = BitmapFactory.decodeFile(mostRecentPhotoPath);
        try {
            UploadProfileImage(bmp,user);
            profileImage = bmp; // circumvents download issues
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Method to ensure that all input fields have been completed
     */
    private void checkRequiredFields(){
        if(!mEmailField.getText().toString().isEmpty() &&
                !mPasswordField.getText().toString().isEmpty() &&
                !mUsername.getText().toString().isEmpty()
        && countryPicked &&
        !mName.getText().toString().isEmpty() &&
        !mCity.getText().toString().isEmpty()
        &&mTakePhoto.getDrawable()!=null){
            mRegButton.setEnabled(true);
            mInfoReq.setEnabled(false);
        }
        else{
            mRegButton.setEnabled(false);
        }
    }

    /*
            Method the handles all the button listeners.
            Code is housed here to keep the "onCreate" tidy.
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.registerBtn_regScreen) {
            mRegButton.setEnabled(false);
            mRegButton.setVisibility(View.INVISIBLE);
            if(createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString())) {
                mRegButton.setVisibility(View.INVISIBLE);
                mLottie.setVisibility(View.VISIBLE);
                mLottie.playAnimation();
                //allow the system time to create the user account by waiting a few seconds
                Thread loading = new Thread(){
                    public void run(){
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent myIntentProfile = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(myIntentProfile);
                    }
                };
                loading.start(); //start teh new thread

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
    }

}