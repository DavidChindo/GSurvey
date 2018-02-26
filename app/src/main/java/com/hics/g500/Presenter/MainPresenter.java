package com.hics.g500.Presenter;

import android.content.Context;

import com.hics.g500.Dal.Dal;
import com.hics.g500.G500App;
import com.hics.g500.Library.Connection;
import com.hics.g500.Network.Response.LogOutResponse;
import com.hics.g500.Presenter.Callbacks.MainCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by david.barrera on 2/18/18.
 */

public class MainPresenter {

    private final MainCallback mMainCallback;
    private Context mContext;

    public MainPresenter(MainCallback mMainCallback, Context mContext) {
        this.mMainCallback = mMainCallback;
        this.mContext = mContext;
    }

    public void logOut(){
        try {
            if (Connection.isConnected(mContext)){
                Call<LogOutResponse> call = G500App.getHicsService().logOut("bearer "+ Dal.token());
                call.enqueue(new Callback<LogOutResponse>() {
                    @Override
                    public void onResponse(Call<LogOutResponse> call, Response<LogOutResponse> response) {
                        if (response.code() == 200 || response.code() == 201){
                            if (!response.body().isError()){
                                Dal.deleteUser();
                                mMainCallback.onSuccessLogOut(true);
                            }else{
                                mMainCallback.onSuccessLogOut(false);
                            }
                        }else{
                                mMainCallback.onErrorLogOut(response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<LogOutResponse> call, Throwable t) {
                        mMainCallback.onErrorLogOut(t.getMessage());
                    }
                });
            }else{
                mMainCallback.onErrorLogOut("No hay conexión de internet");
            }
        }catch (Exception e){
            e.printStackTrace();
            mMainCallback.onErrorLogOut(e.getMessage());
        }
    }
}
