package com.hics.g500.Views.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.hics.g500.BuildConfig;
import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.Connection;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.Library.LogicUtils;
import com.hics.g500.Library.Validators;
import com.hics.g500.Network.Request.LoginRequest;
import com.hics.g500.Network.Request.SignUpRequest;
import com.hics.g500.Network.Response.LoginResponse;
import com.hics.g500.Network.Response.SignUpResponse;
import com.hics.g500.Network.Response.SurveyResponse;
import com.hics.g500.Presenter.Callbacks.LoginCallback;
import com.hics.g500.Presenter.Callbacks.SurveyCallback;
import com.hics.g500.Presenter.Implementations.LoginPresenter;
import com.hics.g500.Presenter.Implementations.SurveyPresenter;
import com.hics.g500.R;
import com.hics.g500.db.User;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.fabric.sdk.android.Fabric;
import retrofit2.http.Body;

public class LoginActivity extends Activity implements LoginCallback,SurveyCallback,ActivityCompat.OnRequestPermissionsResultCallback {


    @BindView(R.id.act_login_signup)Button btnRegister;
    @BindView(R.id.act_login_confirm)EditText edtConfirmPass;
    @BindView(R.id.act_login_username)EditText edtUsername;
    @BindView(R.id.act_login_password)EditText edtPassword;
    @BindView(R.id.act_login_enter)Button btnEnter;
    @BindView(R.id.act_login_recovery)Button btnRecovery;
    @BindView(R.id.act_login_txt_confirm)TextInputLayout tilConfirm;
    @BindView(R.id.act_login_version)TextView txtVersion;
    @BindView(R.id.act_login_no_acount)TextView txtNoAccount;

    LoginPresenter loginPresenter;
    SurveyPresenter surveyPresenter;
    ProgressDialog mProgressDialog;

    private boolean mLocationPermissionGranted;
    private boolean mLocationPermissionCoarseGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2;
    private static final String[] PHONE_STATE = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.WAKE_LOCK};
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Fabric.with(this, new Crashlytics());
        ButterKnife.bind(this);
        setHint();
        loginPresenter = new LoginPresenter(this,this);
        surveyPresenter = new SurveyPresenter(this,this);

        if (Build.VERSION.SDK_INT >=23) {
            //init_permission();
            LogicUtils.requestPermission(this, Manifest.permission.RECORD_AUDIO);
            LogicUtils.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            LogicUtils.requestPermission(this, Manifest.permission.CAMERA);
            LogicUtils.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            LogicUtils.requestPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            LogicUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            LogicUtils.requestPermission(this, Manifest.permission.WAKE_LOCK);
        }
    }

    private void setHint(){
        txtVersion.setText(getString(R.string.text_version, BuildConfig.VERSION_NAME));
        edtConfirmPass.setHint("Confirmar contraseña");
        edtUsername.setHint("Usuario");
        edtPassword.setHint("Contraseña");
        btnEnter.setTag(1);
    }

    /*
    * Summary: inicia los permisos necesarios enla aplicación
    * */
    @TargetApi(Build.VERSION_CODES.M)
    public void init_permission()
    {
        final List<String> permissionsList = new ArrayList<String>();
        /*permisos necesarios*/
        if(!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            if(!addPermission(permissionsList, Manifest.permission.CAMERA))
                if(!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
                        if(!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
                            if(!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                                if(!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
                                    if(!addPermission(permissionsList, Manifest.permission.WAKE_LOCK))

                            if (permissionsList.size() > 0) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                return;
                            }
    }
    /*
    * Summary: agrega los persmisos que se necesitan para que la app funcione
    * */
    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            /* Check for Rationale Option*/
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
            case PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionCoarseGranted = true;
                }
            }
        }

    }

    @OnClick(R.id.act_login_signup)
    void onSignUpChange(){
        if (((int)btnEnter.getTag()) == 1) {
            edtConfirmPass.setVisibility(View.VISIBLE);
            tilConfirm.setVisibility(View.VISIBLE);
            btnEnter.setText(R.string.act_login_register);
            btnEnter.setTag(2);
            btnRegister.setText("Iniciar Sesión");
            txtNoAccount.setVisibility(View.GONE);
            btnRecovery.setVisibility(View.GONE);
        }else {
            edtConfirmPass.setVisibility(View.GONE);
            tilConfirm.setVisibility(View.GONE);
            btnEnter.setText(R.string.act_login_enter);
            btnRegister.setText("Regístrate aquí");
            txtNoAccount.setVisibility(View.VISIBLE);
            btnEnter.setTag(1);
            btnRecovery.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.act_login_enter)
    void onEnterClick(View v){
        if (((int)v.getTag()) == 1) {
            String message = loginPresenter.validateCredentials(this, edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim());
            if (message.isEmpty()) {
                final String pass = LogicUtils.MD5(edtPassword.getText().toString().trim());
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail(edtUsername.getText().toString().trim());
                loginRequest.setPassword(pass);
                mProgressDialog = ProgressDialog.show(this, null, "Autenticando...");
                mProgressDialog.setCancelable(false);
                loginPresenter.login(loginRequest);
            } else {
                DesignUtils.errorMessage(LoginActivity.this, "Error", message);
            }
        }else if (((int) v.getTag()) == 2){
            String message = loginPresenter.validateCredentialsPassword(this,edtUsername.getText().toString().trim(),edtPassword.getText().toString().trim(),edtConfirmPass.getText().toString().trim());
            if (message.isEmpty()){
                SignUpRequest signUpRequest = new SignUpRequest(edtUsername.getText().toString().trim(),edtPassword.getText().toString().trim());
                mProgressDialog = ProgressDialog.show(this,null,"Regitrando...");
                mProgressDialog.setCancelable(false);
                loginPresenter.signUp(signUpRequest);
            }else{
                DesignUtils.errorMessage(LoginActivity.this, "Error", message);
            }
        }
    }

    @Override
    public void onSuccessLogin(LoginResponse loginResponse) {
        if (loginResponse != null){
            User user = new User(loginResponse.getEmail(),loginResponse.getArray().getJwt());
            if (Dal.insertUser(LoginActivity.this,user) != null) {
                mProgressDialog.setMessage("Descargando encuesta...");
                surveyPresenter.getSurvey();
            }else{
                mProgressDialog.dismiss();
            }
        }else{
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onErrorLogin(String msgError) {
        mProgressDialog.dismiss();
        DesignUtils.errorMessage(this,"Error",msgError);
    }

    @Override
    public void onSuccessSignUp(SignUpResponse signUpResponse) {
        mProgressDialog.dismiss();
        if (signUpResponse != null){
            edtConfirmPass.setVisibility(View.GONE);
            tilConfirm.setVisibility(View.GONE);
            btnEnter.setText(R.string.act_login_enter);
            btnEnter.setTag(1);
            btnRecovery.setVisibility(View.VISIBLE);
            DesignUtils.successMessage(this,"Registro", Validators.validateString(signUpResponse.getMessage()));
        }
    }

    @Override
    public void onErrorSignUp(String msg) {
        mProgressDialog.dismiss();
        DesignUtils.errorMessage(this,"Error",msg);
    }

    @Override
    public void onSuccessSurvey(SurveyResponse surveyResponse) {
        if (surveyResponse != null){
            long idEncuesta = Dal.insertSurvey(LoginActivity.this,surveyResponse);
            if (idEncuesta > -1){
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else{
                DesignUtils.errorMessage(this,"Error","No se pudo guardar la encuesta");
            }
        }

        mProgressDialog.dismiss();

    }

    @Override
    public void onErrorSurvey(String msgError) {
        mProgressDialog.dismiss();
        DesignUtils.errorMessage(this,"Error",msgError);
    }

    @OnClick(R.id.act_login_recovery)
    void onOpenRecovery(){
        startActivity(new Intent(this,RecoveryPasswordActivity.class));
    }

}
