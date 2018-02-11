package com.hics.g500.SurveyEngine.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hics.g500.R;
import com.hics.g500.db.Preguntas;

/**
 * Created by david.barrera on 2/1/18.
 */

public class ViewHolderEditText extends RecyclerView.ViewHolder {


    public LinearLayout linearLayout;
    public EditText editText;
    public TextView txtRequired;
    public TextView txtTitle;

    public ViewHolderEditText(View v,Preguntas questiont) {
        super(v);
        linearLayout = (LinearLayout) v.findViewById(R.id.open_ended_linear_layout);
        editText = (EditText) v.findViewById(R.id.item_survey_list_edit_text_open_ended);
        txtTitle = (TextView)v.findViewById(R.id.item_survey_open_ended_txt_title);
        txtRequired = (TextView) v.findViewById(R.id.item_survey_list_open_ended_required);
        Preguntas questionv = (Preguntas) editText.getTag();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Preguntas question = (Preguntas) editText.getTag();
                //if (!editable.toString().trim().isEmpty() && editable.toString().trim().length() > 0) {
                //SENT ANSWER
                //}
                Log.d("EditText",editable.toString().trim());
            }
        });
    }
}
