package com.terrapages.catchemalljava.activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.terrapages.catchemalljava.model.ClusterMarkerLocation;

import java.util.Random;

public class ClusterMarkerActivity extends BaseMapActivity {

    @Override
    protected void initMapSettings() {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        initMarkers();
    }

    /*@Override
    protected void initMapIfNecessary() {
        super.initMapIfNecessary();
        initMarkers();
    }*/

    private void initMarkers() {
        ClusterManager<ClusterMarkerLocation> clusterManager = new ClusterManager<ClusterMarkerLocation>(this, mGoogleMap);
        mGoogleMap.setOnCameraChangeListener(clusterManager);

        double lat;
        double lng;
        Random generator = new Random();
        for (int i = 0; i < 1000; i++) {
            lat = generator.nextDouble() / 3;
            lng = generator.nextDouble() / 3;
            if (generator.nextBoolean()) {
                lat = -lat;
            }
            if (generator.nextBoolean()) {
                lng = -lng;
            }
            clusterManager.addItem(new ClusterMarkerLocation(new LatLng(mCenterLocation.latitude + lat, mCenterLocation.longitude + lng)));
        }
    }
}
