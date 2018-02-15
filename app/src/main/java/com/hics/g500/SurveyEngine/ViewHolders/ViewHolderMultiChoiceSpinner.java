package com.hics.g500.SurveyEngine.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Presenter.SurveySaveCallback;
import com.hics.g500.db.Opciones;
import com.hics.g500.db.Preguntas;

/**
 * Created by david.barrera on 2/1/18.
 */

public class ViewHolderMultiChoiceSpinner extends RecyclerView.ViewHolder {

    public Spinner mSpinner;
    public LinearLayout mLinearSp;
    public TextView txtRequired;
    public TextView txtTitle;
    SurveySaveCallback mSurveyCallback;

    public ViewHolderMultiChoiceSpinner(final View v,SurveySaveCallback surveyCallback) {
        super(v);
        mSpinner = (Spinner) v.findViewById(R.id.item_survey_multi_list_spinner);
        mLinearSp = (LinearLayout) v.findViewById(R.id.container_spinner);
        txtRequired = (TextView) v.findViewById(R.id.item_survey_list_spinner_required);
        txtTitle = (TextView)v.findViewById(R.id.item_survey_spinner_txt_title);
        this.mSurveyCallback = surveyCallback;

        mSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (AdapterView.INVALID_POSITION != position) {
                            Preguntas question = (Preguntas) adapterView.getTag();
                            Opciones value = (Opciones) adapterView.getSelectedItem();
                            //AQUI SE ENVIA LA RESPUESTA
                            mSurveyCallback.onSaveAnswer(null,value.getOpcion_id().intValue(),question,
                                    Integer.parseInt(question.getPregunta_tipo()),question.getRespuestaDetalle());
                            Log.d("MultiSpinner","Option selected " + position);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
    }
}

