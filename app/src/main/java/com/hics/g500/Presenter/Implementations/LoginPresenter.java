package com.hics.g500.Presenter.Implementations;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.hics.g500.G500App;
import com.hics.g500.Library.Connection;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.Network.HicsWebService;
import com.hics.g500.Network.Request.LoginRequest;
import com.hics.g500.Network.Request.SignUpRequest;
import com.hics.g500.Network.Response.LoginResponse;
import com.hics.g500.Network.Response.SignUpResponse;
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
                            DesignUtils.showToast(mContext,"8 autenticando");
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
            mLoginCallback.onErrorLogin(e.getMessage());
        }
    }

    public void signUp(SignUpRequest signUpRequest) {
        try {
            if (Connection.isConnected(mContext)){
                Call<SignUpResponse> call = G500App.getHicsService().signUp(signUpRequest);
                call.enqueue(new Callback<SignUpResponse>() {
                    @Override
                    public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                        if (response.code() == 201){
                            mLoginCallback.onSuccessSignUp(response.body());
                        }else{
                            try {
                                SignUpResponse errorService = new Gson().fromJson(response.errorBody().string(), SignUpResponse.class);
                                mLoginCallback.onErrorSignUp(errorService.getMessage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SignUpResponse> call, Throwable t) {
                        mLoginCallback.onErrorSignUp(t.getMessage());
                    }
                });
            }else{
                mLoginCallback.onErrorSignUp("No hay conexión a internet");
            }
        }catch (Exception e){
            e.printStackTrace();
            mLoginCallback.onErrorSignUp(e.getMessage());
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

    public String validateCredentialsPassword(Context context,String username,String password,String confirm){
        if (Connection.isConnected(context)) {
            if (username.isEmpty() && password.isEmpty() && !confirm.isEmpty()) {
                return "Favor de ingresar todos los campos";
            } else {
                if (!username.isEmpty()) {
                    if (!password.isEmpty()) {
                        if (!confirm.isEmpty()){
                            if (password.equals(confirm)){
                                return "";
                            }else{
                                return "No coinciden las contraseñas";
                            }
                        }else {
                            return "la confirmación contraseña es requerida";
                        }
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
