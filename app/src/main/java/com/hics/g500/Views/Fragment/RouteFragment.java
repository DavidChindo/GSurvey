package com.hics.g500.Views.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.AnnimationsBuilding;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.Network.Request.GasolinerasRequest;
import com.hics.g500.Presenter.Callbacks.GasolinerasCallback;
import com.hics.g500.Presenter.Implementations.GasolinerasPresenter;
import com.hics.g500.R;
import com.hics.g500.Views.Activity.RecordAudioActivity;
import com.hics.g500.Views.Adapter.RouteAdapter;
import com.hics.g500.db.Gasolineras;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,GasolinerasCallback{

    @BindView(R.id.fr_route_txt_error)TextView txtError;
    @BindView(R.id.fr_route_recycler)RecyclerView recyclerView;
    @BindView(R.id.fr_route_record)FloatingActionButton btnRecord;
    @BindView(R.id.animation_error_open)LottieAnimationView animationView;
    @BindView(R.id.swiperefresh_route)SwipyRefreshLayout swiperefresh_auth;

    Activity mActivity;
    List<Gasolineras> gasos;
    private boolean mLocationPermissionGranted;
    private boolean mLocationPermissionCoarseGranted;
    private Location mLastLocation;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    private final String TAG = RouteFragment.class.getSimpleName();
    GoogleApiClient mGoogleApiClient;
    GasolinerasPresenter gasolinerasPresenter;
    ProgressDialog mProgressDialog;
    private boolean focusing = false;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean GpsStatus = false;

    public RouteFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swiperefresh_auth.setDirection(SwipyRefreshLayoutDirection.TOP);
        swiperefresh_auth.setColorSchemeColors(R.color.colorPrimary);
        swiperefresh_auth.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (isInabledGPS()) {
                    swiperefresh_auth.setRefreshing(true);
                    if (checkPlayServices()) {

                        buildGoogleApiClient();
                    }
                }else{
                    txtError.setText("Una vez activado el GPS, deslize hacia abajo para actualizar.");
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    private void initView(){
        mActivity = getActivity();
        btnRecord.setAnimation(AnnimationsBuilding.getDown(mActivity));
        getLocationPermission();
        gasolinerasPresenter = new GasolinerasPresenter(this,mActivity);
        gasos = new ArrayList<Gasolineras>();

        if (!isInabledGPS()) {
            txtError.setText("Una vez activado el GPS, deslize hacia abajo para actualizar.");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }else {
            initData();
        }
    }

    private void initData(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = LocationServices.getSettingsClient(mActivity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(mActivity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (checkPlayServices()) {

                    buildGoogleApiClient();
                }
            }
        });

        task.addOnFailureListener(mActivity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(mActivity,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private boolean isInabledGPS(){
        LocationManager service = (LocationManager) mActivity.getSystemService(LOCATION_SERVICE);

        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return enabled;
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionCoarseGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionCoarseGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionCoarseGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionCoarseGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

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
        if (mLocationPermissionCoarseGranted && mLocationPermissionGranted){
            if (checkPlayServices()) {
                buildGoogleApiClient();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
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
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d("RouteFragment","valor");
                        break;
                }
            }
        });
    }

    private void getLocation() {

        if (mLocationPermissionGranted && mLocationPermissionCoarseGranted) {

            try
            {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
                MapFragment.lastLocation = mLastLocation;
                Log.d("UBICACIÓNFragment","location "+mLastLocation);
                if (!focusing){
                    focusing = true;
                    if (mLastLocation != null) {
                        String coodinates = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                        GasolinerasRequest gasolinerasRequest = new GasolinerasRequest(Dal.user().getEmail(), coodinates);
                        mProgressDialog = ProgressDialog.show(mActivity, null, "Ubicando...");
                        mProgressDialog.setCancelable(false);
                        gasolinerasPresenter.gasolineras(gasolinerasRequest);
                    }else{
                        focusing = false;
                        txtError.setText("No se pudo obtener la ubicación, deslize hacia abajo para actualizar.");
                    }
                }
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
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


    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(mActivity);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(mActivity,resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {

                DesignUtils.errorMessage(mActivity,"","No es posible obtener la ubicación");
            }
            return false;
        }
        return true;
    }

    @Override
    public void onSuccessLoadGasolineras(ArrayList<Gasolineras> gasolineras) {
        swiperefresh_auth.setRefreshing(false);
        focusing = false;
        if (gasolineras != null && gasolineras.size() > 0){
            Dal.insertGasolineras(gasolineras);
            gasos = Dal.gasolinerasAll();
            if (gasos != null && gasos.size() > 0){
                mLayoutManager = new LinearLayoutManager(mActivity);
                recyclerView.setLayoutManager(mLayoutManager);

                recyclerView.setAdapter(new RouteAdapter(gasos,mActivity,mActivity));
                recyclerView.setVisibility(View.VISIBLE);
                txtError.setVisibility(View.GONE);
            }
        }
        mProgressDialog.dismiss();
    }

    @Override
    public void onErrorLoadGasolineras(String msgError) {
        swiperefresh_auth.setRefreshing(false);
        focusing = false;
        mProgressDialog.dismiss();
        DesignUtils.errorMessage(mActivity,"Error",msgError);
        txtError.setText(msgError);
        recyclerView.setVisibility(View.GONE);
        txtError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isInabledGPS()) {
            txtError.setText("Una vez activado el GPS, deslize hacia abajo para actualizar.");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }else{
            initData();
        }
    }

    @OnClick(R.id.fr_route_record)
    public void onRecordAudioLib(){
        dialogRecord();

    }


    private void dialogRecord(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_record_name, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.record_name);

        dialogBuilder.setTitle("Nombre de la grabación");
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = edt.getText().toString().trim();
                if (name != null && !name.isEmpty()){
                    Intent intent = new Intent(mActivity,RecordAudioActivity.class);
                    intent.putExtra("name",name);
                    startActivity(intent);
                }else{
                    DesignUtils.showToast(mActivity,"Ingrese nombre");
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
