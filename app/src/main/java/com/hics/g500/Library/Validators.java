package com.hics.g500.Library;

import android.app.Activity;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.hics.g500.R;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by david.barrera on 2/1/18.
 */

public class Validators {

    public static boolean validateEdt(EditText edt, Activity activity, String question){
        if (edt.getText().toString().trim().isEmpty()){
            edt.requestFocus();
            DesignUtils.errorMessage(activity,"Campo Obligatorio", activity.getString(R.string.validate_field, question));
            return false;
        }
        return true;
    }

    public static boolean validateSpiner(MaterialSpinner sp, Activity activity, String question){
        if (sp.getSelectedItemPosition() < 1){
            /*sp.requestFocus();
            sp.setFocusable(true);*/
            sp.setFocusableInTouchMode(true);
            sp.setError(activity.getString(R.string.validate_field, question));
            return false;
        }
        sp.setError(null);
        return  true;
    }

    public static boolean validateRadioGroup(RadioGroup radioGroup, Activity activity, String question){
        if (radioGroup.getCheckedRadioButtonId() == -1)
        {
            radioGroup.requestFocus();
            radioGroup.setFocusable(true);
            radioGroup.setFocusableInTouchMode(true);
            DesignUtils.errorMessage(activity,"Campo Obligatorio", activity.getString(R.string.validate_field, question));
            return false;
        }
        return  true;
    }

    public static boolean validateArrayListString(ArrayList<String> arrayListl, Activity activity, String question){
        if (arrayListl.isEmpty() && arrayListl.size() <= 0 ){
            DesignUtils.errorMessage(activity,"Campo Obligatorio", activity.getString(R.string.validate_field, question));
            return false;
        }
        return true;
    }

    public static String validateString(String word){
        if (word != null && !word.isEmpty()){
            return word;
        } else{
            return "";
        }
    }
}
