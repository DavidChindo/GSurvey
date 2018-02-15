package com.hics.g500.SurveyEngine.Presenter;

import com.hics.g500.db.Preguntas;
import com.hics.g500.db.RespuestaDetalle;

/**
 * Created by david.barrera on 2/14/18.
 */

public interface SurveySaveCallback {

    void onSaveAnswer(String answer, int answerid, long idQuestion, int typeQuestion, RespuestaDetalle answerDetail);

    void onSaveAnswerMultiOption(boolean isChecked,String answer, int answerid, long idQuestion, int typeQuestion, RespuestaDetalle answerDetail);

}
