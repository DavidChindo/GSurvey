package com.hics.g500.Presenter.Implementations;

import android.content.Context;

import com.google.gson.Gson;
import com.hics.g500.G500App;
import com.hics.g500.Library.Connection;
import com.hics.g500.Network.HicsWebService;
import com.hics.g500.Network.Request.LoginRequest;
import com.hics.g500.Network.Response.LoginResponse;
import com.hics.g500.Presenter.Callbacks.LoginCallback;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by david.barrera on 1/31/18.
 */

public class LoginPresenter {

    private final LoginCallback mLoginCallback;
    private Context mContext;

    public LoginPresenter(LoginCallback mLoginCallback, Context mContext) {
        this.mLoginCallback = mLoginCallback;
        this.mContext = mContext;
    }

    public void login(LoginRequest loginRequest){
        try {
            if (Connection.isConnected(mContext)){
                Call<LoginResponse> call = G500App.getHicsService().Login(loginRequest);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.code() == 201){
                            mLoginCallback.onSuccessLogin(response.body());
                        }else{
                            try {
                                LoginResponse errorService = new Gson().fromJson(response.errorBody().string(), LoginResponse.class);
                                mLoginCallback.onErrorLogin(errorService.getMessage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        mLoginCallback.onErrorLogin(t.getMessage());
                    }
                });
            }else{
                mLoginCallback.onErrorLogin("No hay conexión a internet");
            }
        }catch (Exception e){
            e.printStackTrace();
            mLoginCallback.onErrorLogin(e.getLocalizedMessage());
        }
    }

    public String validateCredentials(Context context,String username,String password){
        if (Connection.isConnected(context)) {
            if (username.isEmpty() && password.isEmpty()) {
                return "Favor de ingresar usuario y contraseña";
            } else {
                if (!username.isEmpty()) {
                    if (!password.isEmpty()) {
                        return "";
                    } else {
                        return "La contraseña es requerida";
                    }
                } else {
                    return "El usuario es requerido";
                }
            }
        }else{
            return "No hay conexión a internet";
        }
    }
}
