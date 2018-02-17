package com.hics.g500.Presenter.Callbacks;

import com.hics.g500.Network.Request.SurveySync;
import com.hics.g500.Network.Response.SentDataReponse;
import com.hics.g500.Network.Response.UploadFileResponse;

/**
 * Created by david.barrera on 2/15/18.
 */

public interface SyncCallback {

    void onSentData(SurveySync respuesta);

    void onUploadFile(SurveySync respuesta);

    void onSyncDataSuccess(SentDataReponse sentDataReponse);

    void onSyncDataError(String msg);

    void onUploadFileSuccess(UploadFileResponse uploadFileResponse);

    void onUploadFileErro(String msg);

}
