package com.hics.g500.Network;

import android.os.RecoverySystem;

import com.hics.g500.Network.Request.GasolinerasRequest;
import com.hics.g500.Network.Request.LoginRequest;
import com.hics.g500.Network.Request.RecoveryPasswordRequest;
import com.hics.g500.Network.Request.SignUpRequest;
import com.hics.g500.Network.Request.SurveySync;
import com.hics.g500.Network.Response.GasolinerasResponse;
import com.hics.g500.Network.Response.LogOutResponse;
import com.hics.g500.Network.Response.LoginResponse;
import com.hics.g500.Network.Response.SentDataReponse;
import com.hics.g500.Network.Response.SignUpResponse;
import com.hics.g500.Network.Response.SurveyResponse;
import com.hics.g500.Network.Response.UploadFileResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    @Multipart
    @POST("POST/cuestionario/upload")
    Call<UploadFileResponse> uploadFile(@Header("Authorization") String authorization,
                                        @Part MultipartBody.Part file,
                                        @Part("encuesta_id") RequestBody encuestaId,
                                        @Part("pregunta_id") RequestBody preguntaId,
                                        @Part("email") RequestBody email);


  /*  @Multipart
    @POST("POST/cuestionario/upload")
    Call<UploadFileResponse> uploadFile(@Header("Authorization") String authorization,
                                        @Part MultipartBody.Part file,
                                        @Part("encuesta_id") int encuestaId,
                                        @Part("pregunta_id") int preguntaId,
                                        @Part("email") String email);
*/
    @POST("POST/cuestionario/responder")
    Call<SentDataReponse> syncData(@Header("Authorization") String authorization,
                                   @Body SurveySync surveySync);

    @POST("POST/recoverypasswd")
    Call<RecoverySystem> recoveryPswd(@Body RecoveryPasswordRequest recoveryPasswordRequest);

    @POST("POST/login/signup")
    Call<SignUpResponse> signUp(@Body SignUpRequest signUpRequest);

    @POST("POST/logout")
    Call<LogOutResponse> logOut(@Header("Authorization") String authorization);


}
