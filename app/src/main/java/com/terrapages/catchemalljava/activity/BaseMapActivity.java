package com.terrapages.catchemalljava.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.terrapages.catchemalljava.R;

public abstract class BaseMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    protected LatLng mCenterLocation = new LatLng(39.7392, -104.9903);

    protected GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getMapLayoutId());
        initMapIfNecessary();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMapIfNecessary();
    }

    protected void initMapIfNecessary() {
        if (mGoogleMap != null) {
            return;
        }

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    protected void initCamera() {
        CameraPosition position = CameraPosition.builder()
                .target(mCenterLocation)
                .zoom(getInitialMapZoomLevel())
                .build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
    }

    protected int getMapLayoutId() {
        return R.layout.fragment_map;
    }

    protected float getInitialMapZoomLevel() {
        return 12.0f;
    }

    protected abstract void initMapSettings();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        initMapSettings();
        initCamera();
    }
}
