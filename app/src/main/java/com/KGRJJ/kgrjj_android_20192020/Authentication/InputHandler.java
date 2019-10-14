package com.KGRJJ.kgrjj_android_20192020.Authentication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {

    public boolean isValidEmailInput(String email){
        //Checking the string has to correct pattern using built in libraries.
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPasswordCombination(String password){

        //the below regular expression checks
        /*
        (?=.*[0-9]) a digit must occur at least once
        (?=.*[a-z]) a lower case letter must occur at least once
        (?=.*[A-Z]) an upper case letter must occur at least once
        (?=.*[@#$%^&+=]) a special character must occur at least once
        (?=\\S+$) no whitespace allowed in the entire string
        .{8,} at least 8 characters
         */

        // found on https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation

        String expression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


}
