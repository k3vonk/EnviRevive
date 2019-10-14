package com.KGRJJ.kgrjj_android_20192020.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private InputHandler inputHandler = new InputHandler();
    public static final int PICK_IMAGE = 1;


    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mUsername;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // [END declare_auth]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // TextViews need to be stored as their content will be used later

        mEmailField = findViewById(R.id.email_inputLoginScreen);
        mPasswordField = findViewById(R.id.password_Input);
        mUsername = findViewById(R.id.username_input);

        //Buttons don't need to be stored as they are jus just for listeners
        findViewById(R.id.image_selection).setOnClickListener(this);
        findViewById(R.id.registerBtn_regScreen).setOnClickListener(this);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action

        }
    }

    /*
        Create a user method. We take in two strings for email and password.
        These are sent to a method defined in InputHandler which ensure the strings follow the
        desired specs.

        If it passes this check then we call on the FirebaseAuth method to create an account.

        Inside this method we call on addUser() - this is a method written by Kiowa that adds the
        newly created user to our firebase remote storage.
     */
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if(inputHandler.isValidEmailInput(email)) {
            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                mAuth.updateCurrentUser(user);
                                addUserData(user);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(this,"password or username do not meet criteria",Toast.LENGTH_LONG);
        }
    }


    /* Method to add a user to our firebase storage.
        This method is a standard method that simply connects to our firebase - finds the collection
        named "users" and adds a username to the specified user. Our users are stored with specific
        User IDS - this id from firebase auth is then used as a document ID in our fire Store
        Each user has a username which is stored in the fire store along with profile image and
        other relevant data.
     */
    private void addUserData(FirebaseUser user){

        Map<String,String> username = new HashMap<>();
        username.put("Username",mUsername.getText().toString());
        db.collection("users").document(user.getUid()).set(username)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void uploadImage(){

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.registerBtn_regScreen) {

            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            Intent myIntent = new Intent(this, UserProfileActivity.class);
            startActivity(myIntent);
            FirebaseUser user = mAuth.getCurrentUser();
            Toast.makeText(this,user.getUid(),Toast .LENGTH_LONG).show();
        }
        if(i == R.id.image_selection){
            selectImage();
        }
    }
}