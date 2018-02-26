package com.hics.g500.SurveyEngine.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Presenter.SurveySaveCallback;
import com.hics.g500.db.Opciones;
import com.hics.g500.db.Preguntas;

import java.util.HashMap;

/**
 * Created by david.barrera on 2/1/18.
 */

public class ViewHolderRadIoGroup extends RecyclerView.ViewHolder {

    public TextView textView;
    public RadioGroup radioGroup;
    public LinearLayout linear;
    public LinearLayout linearEdt;
    public LinearLayout linearContainer;
    
    public TextView txtTitle;
    public TextView txtRequired;
    public HashMap<Long, CheckedChangeImpl> checkedChangeHashMap;
    public String responsefortype3radio;
    private Context mContext;
    SurveySaveCallback mSurveyCallback;

    public ViewHolderRadIoGroup(View v,Context context,SurveySaveCallback surveyCallback) {
        super(v);
        mContext = context;
        radioGroup = (RadioGroup) v.findViewById(R.id.item_survey_list_radio_group);
        textView = (TextView) v.findViewById(R.id.item_survey_list_list_radio_title);
        linearContainer = (LinearLayout) v.findViewById(R.id.item_survey_list_linear_edit);
        linear = (LinearLayout) v.findViewById(R.id.linear_radios);
        txtRequired = (TextView) v.findViewById(R.id.item_survey_list_list_radio_required);
        checkedChangeHashMap = new HashMap<>();
        this.mSurveyCallback = surveyCallback;
    }
    public CheckedChangeImpl getCheckedChangeListener(RadioButton radioButton, Preguntas question,
                                                      int position){
        Opciones answer = (Opciones) radioButton.getTag();
        if(!checkedChangeHashMap.containsKey(answer.getOpcion_id())){
            checkedChangeHashMap.put(new Long(answer.getOpcion_id()), new CheckedChangeImpl(this, question, position));
        }
        return checkedChangeHashMap.get(answer.getOpcion_id());
    }

    public class CheckedChangeImpl implements CompoundButton.OnCheckedChangeListener{
        private ViewHolderRadIoGroup vh;
        private Preguntas question;
        private int position;

        public CheckedChangeImpl(ViewHolderRadIoGroup vh, Preguntas question, int position){
            this.vh = vh;
            this.question = question;
            this.position = position;
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(question != null) {
                final Opciones answerTag = (Opciones) compoundButton.getTag();

                        //else {
                //Se envia la opcion seleccionda to saves
                        //EventBus.getDefault().postSticky(new EventSaveOption(question, answerTag, String.valueOf(position), false, question.getRespuestadetalle(),false));
                if (b) {
                    mSurveyCallback.onSaveAnswer(null, answerTag.getOpcion_id().intValue(), question,
                            Integer.parseInt(question.getPregunta_tipo()), question.getRespuestaDetalle());
                    Log.d(getClass().getName(), "Position saved: " + position);
                }


            }

        }
    }

    public String getResponsefortype3radio() {
        return responsefortype3radio;
    }

    public void setResponsefortype3radio(String responsefortype3radio) {
        this.responsefortype3radio = responsefortype3radio;
    }

    public void setCheckedDefault(Opciones value,boolean isChecked, final Preguntas question,int position,ViewHolderRadIoGroup vh1,boolean firstSave){
        final Opciones answerTag = value;
            if (isChecked) {
                if (firstSave){
                    Log.d("RadioGroup","position first "+position);
                    //EventBus.getDefault().postSticky(new EventSaveOption(question, answerTag, String.valueOf(position), false, question.getRespuestadetalle(),false));
                    mSurveyCallback.onSaveAnswer(null,answerTag.getOpcion_id().intValue(),question,
                            Integer.parseInt(question.getPregunta_tipo()),question.getRespuestaDetalle());
                }
                Log.d(getClass().getName(), "Position saved: " + position);
            }

    }

}
