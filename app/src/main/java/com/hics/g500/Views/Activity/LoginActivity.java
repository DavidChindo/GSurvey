package com.hics.g500.Views.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.Connection;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.Library.LogicUtils;
import com.hics.g500.Network.Request.LoginRequest;
import com.hics.g500.Network.Response.LoginResponse;
import com.hics.g500.Network.Response.SurveyResponse;
import com.hics.g500.Presenter.Callbacks.LoginCallback;
import com.hics.g500.Presenter.Callbacks.SurveyCallback;
import com.hics.g500.Presenter.Implementations.LoginPresenter;
import com.hics.g500.Presenter.Implementations.SurveyPresenter;
import com.hics.g500.R;
import com.hics.g500.db.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class LoginActivity extends Activity implements LoginCallback,SurveyCallback,ActivityCompat.OnRequestPermissionsResultCallback {


    @BindView(R.id.act_login_login) Button btnLogin;
    @BindView(R.id.act_login_signup)Button btnRegister;
    @BindView(R.id.act_login_confirm)EditText edtConfirmPass;
    @BindView(R.id.act_login_username)EditText edtUsername;
    @BindView(R.id.act_login_password)EditText edtPassword;
    @BindView(R.id.act_login_enter)Button btnEnter;
    @BindView(R.id.act_login_recovery)Button btnRecovery;
    @BindView(R.id.act_login_txt_confirm)TextInputLayout tilConfirm;

    LoginPresenter loginPresenter;
    SurveyPresenter surveyPresenter;
    ProgressDialog mProgressDialog;

    private boolean mLocationPermissionGranted;
    private boolean mLocationPermissionCoarseGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2;
    private static final String[] PHONE_STATE = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this,this);
        surveyPresenter = new SurveyPresenter(this,this);

        if (Build.VERSION.SDK_INT >=23) {
            init_permission();
        }
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
        edtConfirmPass.setVisibility(View.VISIBLE);
        tilConfirm.setVisibility(View.VISIBLE);
        btnEnter.setText(R.string.act_login_register);
        btnRecovery.setVisibility(View.GONE);
    }

    @OnClick(R.id.act_login_login)
    void onSignInChange(){
        edtConfirmPass.setVisibility(View.GONE);
        tilConfirm.setVisibility(View.GONE);
        btnEnter.setText(R.string.act_login_enter);
        btnRecovery.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.act_login_enter)
    void onEnterClick(){
        String message = loginPresenter.validateCredentials(this,edtUsername.getText().toString().trim(),edtPassword.getText().toString().trim());
        if (message.isEmpty()) {
            //final String pass = LogicUtils.MD5(edtPassword.getText().toString().trim());
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(edtUsername.getText().toString().trim());
            loginRequest.setPassword(edtPassword.getText().toString().trim());
            mProgressDialog = ProgressDialog.show(this, null, "Autenticando...");
            mProgressDialog.setCancelable(false);
            loginPresenter.login(loginRequest);
        }else{
            DesignUtils.errorMessage(LoginActivity.this,"Error",message);
        }
    }

    @Override
    public void onSuccessLogin(LoginResponse loginResponse) {
        if (loginResponse != null){
            User user = new User(loginResponse.getEmail(),loginResponse.getArray().getJwt());
            if (Dal.insertUser(user) != null) {
                mProgressDialog.setMessage("Descargando encuesta...");
                surveyPresenter.getSurvey();
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
    public void onSuccessSurvey(SurveyResponse surveyResponse) {
        if (surveyResponse != null){
            long idEncuesta = Dal.insertSurvey(surveyResponse);
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
}
