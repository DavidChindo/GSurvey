package com.hics.g500.Views.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hics.g500.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class LoginActivity extends Activity {


    @BindView(R.id.act_login_login) Button btnLogin;
    @BindView(R.id.act_login_signup)Button btnRegister;
    @BindView(R.id.act_login_confirm)EditText edtConfirmPass;
    @BindView(R.id.act_login_username)EditText edtUsername;
    @BindView(R.id.act_login_password)EditText edtPassword;
    @BindView(R.id.act_login_enter)Button btnEnter;
    @BindView(R.id.act_login_recovery)Button btnRecovery;
    @BindView(R.id.act_login_txt_confirm)TextInputLayout tilConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
        startActivity(new Intent(this, MainActivity.class));
    }
}
