package com.hics.g500.Presenter.Implementations;

import android.content.Context;
import android.util.Log;

import com.hics.g500.Dal.Dal;
import com.hics.g500.G500App;
import com.hics.g500.Library.Connection;
import com.hics.g500.Library.LogicUtils;
import com.hics.g500.Network.Request.AnswerSync;
import com.hics.g500.Network.Request.SurveySync;
import com.hics.g500.Network.Response.SentDataReponse;
import com.hics.g500.Network.Response.UploadFileResponse;
import com.hics.g500.Presenter.Callbacks.SurveyCallback;
import com.hics.g500.Presenter.Callbacks.SyncCallback;
import com.hics.g500.SurveyEngine.Enums.QuestionType;

import java.io.File;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by david.barrera on 2/15/18.
 */

public class SyncPresenter {

    private final SyncCallback mSyncCallback;
    private Context mContext;

    public SyncPresenter(SyncCallback syncCallback, Context mContext) {
        this.mSyncCallback = syncCallback;
        this.mContext = mContext;
    }

    public void syncData(SurveySync surveySync){
        try {
            if (Connection.isConnected(mContext)){
                Call<SentDataReponse> sentDataReponseCall = G500App.getHicsService().syncData("bearer "+ Dal.token(),surveySync);
                sentDataReponseCall.enqueue(new Callback<SentDataReponse>() {
                    @Override
                    public void onResponse(Call<SentDataReponse> call, Response<SentDataReponse> response) {
                        if (response.code() == 200){
                            mSyncCallback.onSyncDataSuccess(response.body());
                        }else{
                            mSyncCallback.onSyncDataError(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<SentDataReponse> call, Throwable t) {
                        mSyncCallback.onSyncDataError(t.getMessage());
                    }
                });
            }else{
                mSyncCallback.onSyncDataError("No hay conexión a internet");
            }
        }catch (Exception e){
            e.printStackTrace();
            mSyncCallback.onSyncDataError(e.getMessage());
        }
    }

    public void uploadFile(SurveySync surveySync){
        try {
            if (surveySync.getAnswerSyncs() != null && surveySync.getAnswerSyncs().size() > 0) {
                if (Connection.isConnected(mContext)) {
                    for (AnswerSync answerSync : surveySync.getAnswerSyncs()) {
                        if (answerSync.getTypeId() == QuestionType.IMAGE) {
                            MediaType TYPE_MULTIPART = MediaType.parse("multipart/form-data");
                            MultipartBody.Part body = null;

                            String nameZip = UUID.randomUUID().toString();
                            String path = zip(Dal.getFilesPath(surveySync.getParentId(), answerSync.getQuestionId()), nameZip);
                            final File file = new File(path);
                            if (file.exists()) {
                                final RequestBody requestFile = RequestBody.create(MediaType.parse("zip/*"), file);
                                body = MultipartBody.Part.createFormData("zipPackage", file.getPath(), requestFile);
                            }

                            RequestBody encuestaId = RequestBody.create(TYPE_MULTIPART,String.valueOf(surveySync.getSurveyId()));
                            RequestBody preguntaId = RequestBody.create(TYPE_MULTIPART,String.valueOf(answerSync.getQuestionId()));
                            RequestBody email = RequestBody.create(TYPE_MULTIPART,surveySync.getEmail());

                            if (file.exists()) {
                                Call<UploadFileResponse> call = G500App.getHicsService().uploadFile("bearer " + Dal.token(), body,
                                       encuestaId , preguntaId , email);
                                call.enqueue(new Callback<UploadFileResponse>() {
                                    @Override
                                    public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
                                        if (response.code() == 200) {
                                            if (!response.body().getError()) {
                                                Log.d("SYNCPRESENTER", "VALOR " + file.delete());
                                            }
                                            mSyncCallback.onUploadFileSuccess(response.body());
                                        } else {
                                            mSyncCallback.onUploadFileErro(response.message());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                                        t.printStackTrace();
                                        mSyncCallback.onUploadFileErro(t.getMessage());
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mSyncCallback.onUploadFileErro(e.getMessage());
        }
    }

    private String zip(ArrayList<String> filesm, String numRequisition){
        return  LogicUtils.compressZip(mContext,numRequisition,filesm);
    }
}
