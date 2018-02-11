package com.hics.g500.Network;

import com.hics.g500.Network.Request.GasolinerasRequest;
import com.hics.g500.Network.Request.LoginRequest;
import com.hics.g500.Network.Response.GasolinerasResponse;
import com.hics.g500.Network.Response.LoginResponse;
import com.hics.g500.Network.Response.SurveyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by david.barrera on 1/31/18.
 */

public interface HicsWebService {

    @POST("POST/login")
    Call<LoginResponse> Login(@Body LoginRequest loginRequest);

    @GET("GET/cuestionario")
    Call<SurveyResponse> Survey(@Header("Authorization") String authorization);

    @POST("POST/ubicacion/gascercanas")
    Call<GasolinerasResponse> Gasolineras(@Header("Authorization")String authorization, @Body GasolinerasRequest gasolinerasRequest);

}
