package com.hics.g500.Presenter.Callbacks;

import com.hics.g500.Network.Response.SurveyResponse;

/**
 * Created by david.barrera on 2/8/18.
 */

public interface SurveyCallback {

    void onSuccessSurvey(SurveyResponse surveyResponse);

    void onErrorSurvey(String msgError);


}
