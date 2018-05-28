package com.ftn.mdj.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jasmina on 24/05/2018.
 */

public class UtilHelper {

    public enum ToastLength{
        SHORT,
        LONG
    }

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    public static boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showToastMessage(Context context, String message, ToastLength length){
        Toast toast;
        if(length == ToastLength.SHORT){
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }else{
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        toast.show();
    }
}
