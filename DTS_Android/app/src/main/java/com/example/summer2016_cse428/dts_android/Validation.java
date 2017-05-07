package com.example.summer2016_cse428.dts_android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rafiqul on 8/26/16.
 */
public class Validation {
    public static boolean emailValidation(String emailAddress){
        String EmailPattern;
        Pattern pattern;
        EmailPattern = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        pattern = Pattern.compile(EmailPattern);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }
}
