package com.hics.g500.Presenter.Implementations;

import android.content.Context;

import com.hics.g500.Dal.Dal;
import com.hics.g500.G500App;
import com.hics.g500.Library.Connection;
import com.hics.g500.Network.Response.SurveyResponse;
import com.hics.g500.Presenter.Callbacks.SurveyCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by david.barrera on 2/8/18.
 */

public class SurveyPresenter {

    private final SurveyCallback mSurveyCallback;
    private Context mContext;

    public SurveyPresenter(SurveyCallback mSurveyCallback, Context mContext) {
        this.mSurveyCallback = mSurveyCallback;
        this.mContext = mContext;
    }

    public void getSurvey(){
        try {
            if (Connection.isConnected(mContext)){
                Call<SurveyResponse> surveyResponseCall = G500App.getHicsService().Survey("bearer "+ Dal.token());
                surveyResponseCall.enqueue(new Callback<SurveyResponse>() {
                    @Override
                    public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
                        if (response.code() == 200){
                            mSurveyCallback.onSuccessSurvey(response.body());
                        }else{
                            mSurveyCallback.onErrorSurvey(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<SurveyResponse> call, Throwable t) {
                        mSurveyCallback.onErrorSurvey(t.getMessage());
                    }
                });
            }else{
                mSurveyCallback.onErrorSurvey("No hay conexión a internet");
            }
        }catch (Exception e){
            e.printStackTrace();
            mSurveyCallback.onErrorSurvey(e.getMessage());
        }
    }
}
