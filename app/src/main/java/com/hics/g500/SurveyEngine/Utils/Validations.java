package com.hics.g500.SurveyEngine.Utils;

import java.util.regex.Pattern;

/**
 * Created by david.barrera on 2/18/18.
 */

public class Validations {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validMail(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public static String validNotNull(String word){

        if(word == null)
        {
            word = "";
        }
        return word;
    }
}

