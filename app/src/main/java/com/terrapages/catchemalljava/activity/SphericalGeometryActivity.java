package com.terrapages.catchemalljava.activity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;

public class SphericalGeometryActivity extends BaseMapActivity {
    
    private Marker mMarker1;
    private Marker mMarker2;
    private Polyline mPolyline;
    
    @Override
    protected void initMapSettings() {
//        mGoogleMap.setOnMarkerDragListener(this);

        MarkerOptions options = new MarkerOptions();
        options.position(mCenterLocation);
        options.draggable(true);
        options.icon(BitmapDescriptorFactory.defaultMarker());
        mMarker1 = mGoogleMap.addMarker(options);

        options = new MarkerOptions();
        options.position(new LatLng(mCenterLocation.latitude - 0.3, mCenterLocation.longitude + 0.3));
        options.draggable(true);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMarker2 = mGoogleMap.addMarker(options);
        
        mPolyline = mGoogleMap.addPolyline(new PolylineOptions().geodesic(true));
        updateLine();
    }

    private void updateLine() {
        mPolyline.setPoints(Arrays.asList(mMarker1.getPosition(), mMarker2.getPosition()));
    }
}
