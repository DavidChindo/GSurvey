package com.hics.g500.SurveyEngine.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Presenter.SurveySaveCallback;
import com.hics.g500.db.Opciones;
import com.hics.g500.db.Preguntas;

import java.util.HashMap;

/**
 * Created by david.barrera on 2/1/18.
 */

public class ViewHolderMultiChoiceCheck extends RecyclerView.ViewHolder {

    public TextView textView;
    public TextView txtRequired;
    public LinearLayout linearLayout,linearEdt;
    public HashMap<Long, CheckedChangeImpl> checkedChangeListenerHashMap;
    SurveySaveCallback mSurveyCallback;

    public ViewHolderMultiChoiceCheck(View v,SurveySaveCallback surveyCallback) {
        super(v);
        linearLayout = (LinearLayout) v.findViewById(R.id.item_survey_list_check_container);
        textView = (TextView) v.findViewById(R.id.item_survey_list_chk_title);
        txtRequired = (TextView)v.findViewById(R.id.item_survey_list_check_required);
        linearEdt = (LinearLayout) v.findViewById(R.id.item_survey_check_linear_edit);
        checkedChangeListenerHashMap = new HashMap<>();
        this.mSurveyCallback = surveyCallback;
    }

    public CompoundButton.OnCheckedChangeListener getCheckedChangeListener(CheckBox checkBox,
                                                                           Preguntas question, int position) {
        Opciones answer = (Opciones) checkBox.getTag();
        if(!checkedChangeListenerHashMap.containsKey(answer.getOpcion_id())){
            checkedChangeListenerHashMap.put(new Long(answer.getOpcion_id()), new CheckedChangeImpl(this, question,position));
        }
        return checkedChangeListenerHashMap.get(answer.getOpcion_id());
    }

    public class CheckedChangeImpl implements CompoundButton.OnCheckedChangeListener{

        private final ViewHolderMultiChoiceCheck vh;
        private final Preguntas question;
        private final int position;

        public CheckedChangeImpl(ViewHolderMultiChoiceCheck vh, Preguntas question,int position){
            this.vh = vh;
            this.question = question;
            this.position = position;
        }
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (question != null) {
                final Opciones answerTag = (Opciones) compoundButton.getTag();
                    if (b) {
                      /* si requiere el otro
                        EventBus.getDefault().postSticky(new EventSaveOption(question, answerTag, String.valueOf(position), true, question.getRespuestadetalle(),b));*/
                      mSurveyCallback.onSaveAnswerMultiOption(b,null,answerTag.getOpcion_id().intValue(),question,
                              Integer.valueOf(question.getPregunta_tipo()),question.getRespuestaDetalle());
                        Log.d(getClass().getName(), "Position saved " + position);

                    }else{
                        mSurveyCallback.onSaveAnswerMultiOption(b,null,answerTag.getOpcion_id().intValue(),question,
                                Integer.valueOf(question.getPregunta_tipo()),question.getRespuestaDetalle());
                       // EventBus.getDefault().postSticky(new EventSaveOption(question, answerTag, String.valueOf(position), true, question.getRespuestadetalle(),b));
                        Log.d(getClass().getName(),"Position saved "+position);
                    }


            }
        }
    }

}
