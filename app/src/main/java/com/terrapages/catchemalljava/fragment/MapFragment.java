package com.terrapages.catchemalljava.fragment;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.terrapages.catchemalljava.R;
import com.terrapages.catchemalljava.RetrievePokemonAsyncTask;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnIndoorStateChangeListener,
        SeekBar.OnSeekBarChangeListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private GoogleMap mGoogleMap;

    private IndoorBuilding mIndoorBuilding;
    private SeekBar mIndoorSelector;
    private TextView mIndoorMinLevel;
    private TextView mIndoorMaxLevel;

    private StreetViewPanoramaView mStreetViewPanoramaView;
    private StreetViewPanorama mPanorama;

    private final int[] MAP_TYPES = {
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE };
    private int curMapTypeIndex = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup parent = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
//        View overlay = inflater.inflate(R.layout.view_map_overlay, parent, false);
//
//        mIndoorSelector = (SeekBar) overlay.findViewById(R.id.indoor_level_selector);
//        mIndoorMinLevel = (TextView) overlay.findViewById(R.id.indoor_min_level);
//        mIndoorMaxLevel = (TextView) overlay.findViewById(R.id.indoor_max_level);
//
//        mStreetViewPanoramaView = (StreetViewPanoramaView) overlay.findViewById(R.id.street_view_panorama);
//        mStreetViewPanoramaView.onCreate(savedInstanceState);
//
//        parent.addView(overlay);

        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        getMapAsync(this);
    }

    private void initStreetView() {
        mStreetViewPanoramaView.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
                mPanorama = streetViewPanorama;
                showStreetView(new LatLng(40.7506, -73.9936));
            }
        });
    }

    private void showStreetView(LatLng latLng) {
        if (mPanorama == null)
            return;

        StreetViewPanoramaCamera.Builder builder = new StreetViewPanoramaCamera.Builder(mPanorama.getPanoramaCamera());
        builder.tilt(0.0f);
        builder.zoom(0.0f);
        builder.bearing(0.0f);
        mPanorama.animateTo(builder.build(), 0);

        mPanorama.setPosition(latLng, 300);
        mPanorama.setStreetNamesEnabled(true);
    }

    private void initMapIndoorSelector() {
        mIndoorSelector.setOnSeekBarChangeListener(this);

        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mGoogleMap.setOnIndoorStateChangeListener(this);
    }

    private void hideFloorLevelSelector() {
        mIndoorSelector.setVisibility(View.GONE);
        mIndoorMaxLevel.setVisibility(View.GONE);
        mIndoorMinLevel.setVisibility(View.GONE);
    }

    private void showFloorLevelSelector() {
        if (mIndoorBuilding == null)
            return;

        int numOfLevels = mIndoorBuilding.getLevels().size();

        mIndoorSelector.setMax(numOfLevels - 1);

        mIndoorMaxLevel.setText(mIndoorBuilding.getLevels().get(0).getShortName());
        mIndoorMinLevel.setText(mIndoorBuilding.getLevels().get(numOfLevels - 1).getShortName());

        mIndoorSelector.setProgress(mIndoorBuilding.getActiveLevelIndex());

        mIndoorSelector.setVisibility(View.VISIBLE);
        mIndoorMaxLevel.setVisibility(View.VISIBLE);
        mIndoorMinLevel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        // initListener
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setOnMapClickListener(this);

        styleMapFromRawResource();

//        initMapIndoorSelector();
//        hideFloorLevelSelector();
//        initStreetView();
    }

    private void styleMapFromRawResource() {
        try {
            boolean success = mGoogleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    private void initCamera(Location location) {
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(18f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);
        mGoogleMap.setMapType(MAP_TYPES[curMapTypeIndex]);
        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        initCamera(mCurrentLocation);
//        Location location = new Location("Madison Square Garden");
//        location.setLatitude(40.7506);
//        location.setLongitude(-73.9936);
//        initCamera(location);

        RetrievePokemonAsyncTask retrievePokemonAsyncTask = new RetrievePokemonAsyncTask(
                getActivity(),
                mCurrentLocation,
                mGoogleMap);
        retrievePokemonAsyncTask.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity());

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
        } catch (IOException e) {
            //
        }

        return address;
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        MarkerOptions options = new MarkerOptions().position(latLng);
//        options.title(getAddressFromLatLng(latLng));
//
//        options.icon(BitmapDescriptorFactory.defaultMarker());
//        mGoogleMap.addMarker(options);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
//        showStreetView(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    /** helper methods **/
    private void drawCircle(LatLng location) {
        CircleOptions options = new CircleOptions();
        options.center(location);

        options.radius(10);
        options.fillColor(getResources()
                .getColor(R.color.fill_color));
        options.strokeColor(getResources()
                .getColor(R.color.stroke_color));
        options.strokeWidth(10);
        mGoogleMap.addCircle(options);
    }

    private void drawPolygon(LatLng startingLocation) {
        LatLng point2 = new LatLng(startingLocation.latitude + .001,
                startingLocation.longitude);
        LatLng point3 = new LatLng(startingLocation.latitude,
                startingLocation.longitude + .001);

        PolygonOptions options = new PolygonOptions();
        options.add(startingLocation, point2, point3);

        options.fillColor(getResources()
                .getColor(R.color.fill_color));
        options.strokeColor(getResources()
                .getColor(R.color.stroke_color));
        options.strokeWidth(10);

        mGoogleMap.addPolygon(options);
    }

    private void drawOverlay(LatLng location, int width, int height) {
        GroundOverlayOptions options = new GroundOverlayOptions();
        options.position(location, width, height);

        options.image(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R.mipmap.ic_launcher)));
        mGoogleMap.addGroundOverlay(options);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if (mIndoorBuilding == null)
            return;

        int numOfLevels = mIndoorBuilding.getLevels().size();
        mIndoorBuilding.getLevels().get(numOfLevels - 1 - progress).activate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onIndoorBuildingFocused() {
        mIndoorBuilding = mGoogleMap.getFocusedBuilding();

        if (mIndoorBuilding == null || mIndoorBuilding.getLevels() == null
                || mIndoorBuilding.getLevels().size() <= 1) {
            hideFloorLevelSelector();
        } else {
            showFloorLevelSelector();
        }
    }

    @Override
    public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {

    }
}
