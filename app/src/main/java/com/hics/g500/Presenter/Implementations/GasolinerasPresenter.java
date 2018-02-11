package com.hics.g500.Presenter.Implementations;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hics.g500.Dal.Dal;
import com.hics.g500.G500App;
import com.hics.g500.Library.Connection;
import com.hics.g500.Network.Request.GasolinerasRequest;
import com.hics.g500.Network.Response.GasolinerasResponse;
import com.hics.g500.Network.Response.LoginResponse;
import com.hics.g500.Presenter.Callbacks.GasolinerasCallback;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by david.barrera on 2/8/18.
 */

public class GasolinerasPresenter {

    private final GasolinerasCallback mGasolinerasCallback;
    private Context mContext;

    public GasolinerasPresenter(GasolinerasCallback gasolinerasCallback, Context mContext) {
        this.mGasolinerasCallback = gasolinerasCallback;
        this.mContext = mContext;
    }

    public void gasolineras(final GasolinerasRequest gasolinerasRequest){
        try {
            if (Connection.isConnected(mContext)){
                Call<GasolinerasResponse> gasolinerasResponseCall = G500App.getHicsService().Gasolineras("bearer "+Dal.token(),gasolinerasRequest);
                gasolinerasResponseCall.enqueue(new Callback<GasolinerasResponse>() {
                    @Override
                    public void onResponse(Call<GasolinerasResponse> call, Response<GasolinerasResponse> response) {
                        if (response.code() == 200){
                            if (response.body() != null) {
                                mGasolinerasCallback.onSuccessLoadGasolineras(response.body().getGasolinerases());
                            }
                        }else{
                            try {
                                GasolinerasResponse gasolinerasResponse = new Gson().fromJson(response.errorBody().string(), GasolinerasResponse.class);
                                if (gasolinerasResponse.isError()){
                                    mGasolinerasCallback.onErrorLoadGasolineras(response.message());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                try {
                                    mGasolinerasCallback.onErrorLoadGasolineras(response.errorBody().string());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<GasolinerasResponse> call, Throwable t) {
                        mGasolinerasCallback.onErrorLoadGasolineras(t.getMessage());
                    }
                });
            }else{
                mGasolinerasCallback.onErrorLoadGasolineras("No hay conexión a internet");
            }
        }catch (Exception e){
            e.printStackTrace();
            mGasolinerasCallback.onErrorLoadGasolineras(e.getLocalizedMessage());
        }
    }
}
/*
LoginResponse errorService = new Gson().fromJson(response.errorBody().string(), LoginResponse.class);
 */