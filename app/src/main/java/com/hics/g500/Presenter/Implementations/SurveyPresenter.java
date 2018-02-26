package com.hics.g500.Presenter.Implementations;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.hics.g500.Dal.Dal;
import com.hics.g500.G500App;
import com.hics.g500.Library.Connection;
import com.hics.g500.Library.DesignUtils;
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
                            /*DesignUtils.showToast(mContext,"5 getSurvey");
                            Crashlytics.log(1, "Survey", "5 getSurvey");
                            Crashlytics.logException(new Throwable("5 getSurvey"));
                            Crashlytics.log("Mensaje: "+ "5 GETSurvey");*/
                            mSurveyCallback.onSuccessSurvey(response.body());
                        }else{
                           /* DesignUtils.showToast(mContext,"6 getSurvey");
                            Crashlytics.log(1, "Survey", "6 getSurvey");
                            Crashlytics.logException(new Throwable("6 getSurvey"));
                            Crashlytics.log("Mensaje: "+ "6 getSurvey");*/
                            mSurveyCallback.onErrorSurvey(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<SurveyResponse> call, Throwable t) {
                        /*DesignUtils.showToast(mContext,"3 getSurvey");
                        Crashlytics.log(1, "Survey", "3 getSurvey");
                        Crashlytics.logException(t);
                        Crashlytics.log("Mensaje: "+ t.getLocalizedMessage());*/
                        mSurveyCallback.onErrorSurvey(t.getMessage());
                    }
                });
            }else{
               /* DesignUtils.showToast(mContext,"2 No hay conexión internet");
                Crashlytics.log(1, "Survey", "2 No hay conexión internet");
                Crashlytics.logException(new Throwable("2 No hay conexión internet"));
                Crashlytics.log("Mensaje: " + "2 No hay conexión internet");*/
                mSurveyCallback.onErrorSurvey("No hay conexión a internet");
            }
        }catch (Exception e){
            e.printStackTrace();
            mSurveyCallback.onErrorSurvey(e.getMessage());
            /*DesignUtils.showToast(mContext,"1 getSurvey");
            Crashlytics.log(1, "Survey", "1 getSurvey");
            Crashlytics.logException(e);
            Crashlytics.log("Mensaje: "+ e.getLocalizedMessage());*/
        }
    }
}
