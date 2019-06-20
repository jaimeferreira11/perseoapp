package py.com.ideasweb.perseo.utilities;

import android.content.Context;
import android.widget.EditText;

import java.util.regex.Pattern;

import py.com.ideasweb.R;


/**
 * Created by jaime on 24/11/16.
 */

public class Validation {
    // 294633311
    //5174  mv

    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";

    // Error Messages
    private static final String REQUIRED_MSG = "Campo Requerido";
    private static final String EMAIL_MSG = "Email Invalido";
    private static final String PHONE_MSG = "####-#######";
    private static final String CHARACTER_MIN = "Debe conterner minimo ";



    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required, Context context) {
        return isValid(editText, EMAIL_REGEX, context.getResources().getString(R.string.mail_invalid), required, context);
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required, Context context) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required, context);
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required, Context context) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText, context) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText, Context context) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(context.getResources().getString(R.string.required));
            return false;
        }

        return true;
    }

    public static boolean minLenght(EditText editText, int length, Context context) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() < length) {
            editText.setError( context.getResources().getString(R.string.required_min) + length + " "+ context.getResources().getString(R.string.caracter));
            return false;
        }

        return true;
    }



}
