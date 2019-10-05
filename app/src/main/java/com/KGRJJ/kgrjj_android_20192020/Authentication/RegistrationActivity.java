package com.KGRJJ.kgrjj_android_20192020.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.KGRJJ.kgrjj_android_20192020.MainActivity;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities.UserProfileActivity;
import com.KGRJJ.kgrjj_android_20192020.utilities.FirebaseStorageHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private InputHandler inputHandler = new InputHandler();
    public static final int PICK_IMAGE = 1;
    private FirebaseStorageHandler fbh = new FirebaseStorageHandler();

    AnimationDrawable animationDrawable;
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mUsername;
    private Button mRegButton;
    private TextView mInfoReq;
    private Uri filePath;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    // [END declare_auth]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // TextViews need to be stored as their content will be used later

        mEmailField = findViewById(R.id.email_inputLoginScreen);
        mPasswordField = findViewById(R.id.password_Input);
        mUsername = findViewById(R.id.username_input);
        mRegButton = findViewById(R.id.registerBtn_regScreen);

        mInfoReq = findViewById(R.id.infoRequired);

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

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        // [END initialize_auth]
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
    private boolean createAccount(String email, String password) {

        if(inputHandler.isValidEmailInput(email)) {
            if (inputHandler.isValidPasswordCombination(password)) {
                // [START create_user_with_email]
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                mAuth.updateCurrentUser(user);


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
        addUserData();

        return true;
    }


    /* Method to add a user to our firebase storage.
        This method is a standard method that simply connects to our firebase - finds the collection
        named "users" and adds a username to the specified user. Our users are stored with specific
        User IDS - this id from firebase auth is then used as a document ID in our fire Store
        Each user has a username which is stored in the fire store along with profile image and
        other relevant data.
     */
    private void addUserData(){
        user = mAuth.getCurrentUser();
        Map<String,String> UserData = new HashMap<>();
        UserData.put("username",mUsername.getText().toString());



        db.collection("user").document(user.getUid()).set(UserData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Log.d("SUCCESSFULLY ADDED USER",user.getUid());
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

    }

    private void checkRequiredFields(){
        if(!mEmailField.getText().toString().isEmpty() &&
                !mPasswordField.getText().toString().isEmpty() &&
                !mUsername.getText().toString().isEmpty()){
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
            if(createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString())) {
                Intent myIntent = new Intent(this, UserProfileActivity.class);
                startActivity(myIntent);
            }
        }
        if(i == R.id.cancelSignUp){
            Intent myInten = new Intent(this, MainActivity.class);
            startActivity(myInten);
        }
    }
}