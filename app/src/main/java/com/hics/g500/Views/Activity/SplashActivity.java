package com.hics.g500.Views.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.Prefs;
import com.hics.g500.Library.Statics;
import com.hics.g500.R;

import butterknife.ButterKnife;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash1);
        ButterKnife.bind(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Dal.token() != null && !Dal.token().isEmpty()) {
                    start(MainActivity.class);
                } else {
                    start(LoginActivity.class);
                }
            }
        }, 2000);
    }

    private void start(Class<?> aClass) {
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
        finish();
    }
}
