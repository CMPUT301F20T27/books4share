package com.example.books4share;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Open a map that allows user to select the location
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPoiClickListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener{

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000 * 60;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private GoogleMap googleMap = null;
    private LocationRequest mLocationRequest;

    private LocationCallback callback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            LatLng myLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(myLocation)
                    .title("location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        }

    };
    String[] permissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION};
    List<String> mPermissionList = new ArrayList<>();

    /**
     * inflate the layout view of map fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * check the two permissions
     * if two permissions are granted, this will call setMap function
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MapActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {
            setMap();
        } else {
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this, permissions, 101);
        }

    }

    /**
     * initialize the map view and set some click listeners
     */
    @SuppressLint("MissingPermission")
    private void setMap(){
        googleMap.setMyLocationEnabled(true);

        mLocationRequest =new  LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS) ;
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) ;
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, callback, Looper.myLooper());
        googleMap.setOnPoiClickListener(this);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }


    /**
     * check the permission request result
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 101){
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(MapActivity.this,"Permission not requested",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            setMap();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {
        Intent intent =new  Intent();
        intent.putExtra("address", poi.name);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    /**
     * locate the location
     * @param location
     */
    @Override
    public void onMyLocationClick(@NonNull Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    /**
     * Set up a on-Map click listener
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


}
