package com.KGRJJ.kgrjj_android_20192020.Authentication;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;


public class SignUpHandler {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final InputHandler inputHandler = new InputHandler();

    public void create(String email, String password, final Context context) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();

                            } else {
                                // If sign in fails, display a message to the user.
                                //TODO


                                Toast.makeText(context, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

    }

    public void createUser(String email, String password, final Context context) {
        if(inputHandler.isValidEmailInput(email)&&inputHandler.isValidPasswordCombination(password)){
            create(email,password,context);
        }else{
            Toast.makeText(context, "Email or Password has the wrong format",
                    Toast.LENGTH_SHORT).show();
        }
    }







}
