package com.hics.g500.Views.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.Presenter.Callbacks.MainCallback;
import com.hics.g500.Presenter.MainPresenter;
import com.hics.g500.R;
import com.hics.g500.Views.Fragment.MapFragment;
import com.hics.g500.Views.Fragment.RouteFragment;
import com.hics.g500.Views.Fragment.SyncFragment;
import com.hics.g500.Views.Fragment.SyncParentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainCallback {


    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.navigation)BottomNavigationView bottomNavigationView;

    private final static int PLAY_SERVICES_REQUEST = 1000;
    MainPresenter mainPresenter;
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle("Gasolineras");
        setSupportActionBar(toolbar);
        mainPresenter = new MainPresenter(this,this);
        onChangeTabListener();
    }

    private void onChangeTabListener(){
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = new RouteFragment();
                                break;
                            case R.id.action_item2:
                                selectedFragment = new SyncFragment();
                                break;
                            case R.id.action_item3:
                                selectedFragment = new MapFragment();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new RouteFragment());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this,resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {

                DesignUtils.errorMessage(this,"","No es posible obtener la ubicación");
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.gral_mnu_close:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut(){
        mProgressDialog = ProgressDialog.show(this,null,"Cerrando Sesión...");
        mainPresenter.logOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();

    }

    @Override
    public void onSuccessLogOut(boolean isLogout) {
        mProgressDialog.dismiss();
        if (isLogout){
            start(LoginActivity.class,true);
        }else{
            DesignUtils.errorMessage(this,"Error","Por el momento no se puede cerrar sesión.");
        }
    }

    @Override
    public void onErrorLogOut(String msgError) {
        mProgressDialog.dismiss();
        DesignUtils.errorMessage(this,"Error",msgError);
    }

    private void start(Class<?> aClass,boolean isFinished ) {
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
        if (isFinished) {
            this.finish();
        }
    }
}
