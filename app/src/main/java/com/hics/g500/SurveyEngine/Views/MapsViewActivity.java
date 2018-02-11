package com.hics.g500.SurveyEngine.Views;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.Network.Request.GasolinerasRequest;
import com.hics.g500.R;
import com.hics.g500.db.Preguntas;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsViewActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
OnMapReadyCallback{

    private GoogleMap mMap;
    private Marker mQuestionMarker;

    Location mLocation;
    Preguntas mQuestion;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_view);
        ButterKnife.bind(this);
        setUpMapIfNeeded();

        buildGoogleApiClient();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.act_map);
        mapFragment.getMapAsync(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        mQuestionMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                                .title(mQuestion.getPregunta_encabezado())
                                .draggable(false));
                    }
                });
                setUpMap();
            }

        }
    }

    private void setUpMap() {
        getLocation();
        if (mLocation != null) {
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(CameraPosition
                            .fromLatLngZoom(
                                    new LatLng(mLocation.getLatitude(),
                                            mLocation.getLongitude()),
                                    15)
                    ));
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void getLocation() {
            try
            {
                mLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);

                Log.d("UBICACIÓNFragment","location "+mLocation);
                if (mMap != null && mLocation != null) {
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(CameraPosition
                                    .fromLatLngZoom(
                                            new LatLng(mLocation.getLatitude(),
                                                    mLocation.getLongitude()),
                                            15)
                            ));
                }

            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }

    }

    @OnClick(R.id.act_map_holder_save)
    void onSaveClick() {
        /*LatLng latLng = mQuestionMarker.getPosition();
        DesignUtils.showToast(this,"Posición "+latLng);*/
        /*if (latLng != null) {  AQUI VA GUARDAR
            EventBus.getDefault().postSticky(new EventSaveAnswer(mQuestion, latLng.latitude+","+latLng.longitude,mQuestion.getRespuestadetalle()));
        }*/
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(MapsViewActivity.class.getSimpleName(), "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Do other setup activities here too, as described elsewhere in this tutorial.

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
         //
            //   setUpMapIfNeeded();
            if (mMap != null && mLocation != null){
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(CameraPosition
                                .fromLatLngZoom(
                                        new LatLng(mLocation.getLatitude(),
                                                mLocation.getLongitude()),
                                        15)
                        ));

                mMap.setMyLocationEnabled(true);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        mQuestionMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                                .title("")
                                .draggable(false));
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
