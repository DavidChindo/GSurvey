package com.hics.g500.Views.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hics.g500.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsDetailActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    String coordinates;
    String name;
    @BindView(R.id.act_maps_detail_name)TextView nameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_detail);
        ButterKnife.bind(this);
        this.setFinishOnTouchOutside(false);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try{
            Intent ActivityIntent = getIntent();
            Bundle extras=ActivityIntent.getExtras();
            coordinates = "";
            name = "";
            coordinates = extras.getString("coordinates");
            name = extras.getString("name");
        }catch (Exception g)
        {
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (coordinates != null && !coordinates.isEmpty()) {
            double lat = new Double(coordinates.split(",")[0]);
            double lon = new Double(coordinates.split(",")[1]);
            LatLng gas = new LatLng(lat,lon);
            nameTxt.setText(name);
            mMap.addMarker(new MarkerOptions().position(gas).title(name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(gas));
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(CameraPosition
                            .fromLatLngZoom(
                                    new LatLng(lat,
                                            lon),
                                    15)
                    ));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @OnClick(R.id.act_maps_detail_close)
    void onCloseMap(){
        finish();
    }
}
