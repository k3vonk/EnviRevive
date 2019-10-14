package com.KGRJJ.kgrjj_android_20192020.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.KGRJJ.kgrjj_android_20192020.R;
import com.KGRJJ.kgrjj_android_20192020.UserSpecificActivities.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    // Essential for using Firebase Authentication
    private FirebaseAuth mAuth;

    // Useful for Log messaged - assign a tag (this is placeholder for now)
    private String TAG = "log";

    //These variables will be assigned the text input areas of this screen.
    private TextView email;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Adding listeners to both button - no need to store them in a variable since they
            are only ever used for listening. "This links to the OnClick method defined below
         */
        findViewById(R.id.createAccount).setOnClickListener(this);
        // " create account is a clickable text to on the activity screen.

        findViewById(R.id.LoginButton_loginScreen).setOnClickListener(this);


        //Essential for using firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // find the text inputs based on their ID.
        email = findViewById(R.id.email_inputLoginScreen);
        password = findViewById(R.id.password_input_loginScreen);
    }

    /* Function written using Firebase tips. Note we dont use our input handlers here.
        This is because if the input isnt correct the FirebaseAuthentication method
        "signInWithEmailAndPassword" does this.

     */
    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Authentication passed with." + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.createAccount){
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }
        if(i == R.id.LoginButton_loginScreen){
            signIn(email.getText().toString(), password.getText().toString());
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }
    }

}
