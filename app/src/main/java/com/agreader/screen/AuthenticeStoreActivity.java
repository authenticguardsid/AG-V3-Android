package com.agreader.screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.model.ListStore;
import com.agreader.model.MarkerData;
import com.agreader.utils.BubleTransformation;
import com.agreader.utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class AuthenticeStoreActivity extends FragmentActivity implements OnMapReadyCallback {

    FloatingActionButton fab;
    String id;
    Utils utils;
    View progressOverlay;
    MarkerOptions markerOption;
    List<Target> targets;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    HashMap<Marker, MarkerData> mMarkersHashMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms\
                    Intent intent = getIntent();
                    id = intent.getStringExtra("id");
                    displaymarker(id);
                }
            }, 100);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            final Utils utils = new Utils(getApplicationContext());
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    final double vlat = marker.getPosition().latitude;
                    final double vlong = marker.getPosition().latitude;
                    utils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                            utils.animateView(progressOverlay, View.GONE, 0, 200);
                        }
                    }, TIME_OUT);
                    return true;
                }
            });

        }
    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentice_store);

        getLocationPermission();
        mMarkersHashMap = new HashMap<>();
        targets = new ArrayList<>();
        fab = (FloatingActionButton) findViewById(R.id.getmy);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        progressOverlay = (FrameLayout) findViewById(R.id.progress_overlay);


    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(AuthenticeStoreActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void moveCameraStore(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(AuthenticeStoreActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }


    public void displaymarker(String id) {
        Log.d(TAG, "displaymarker: disni");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/locatorshow_/" + id + "?appid=003", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = response.getJSONObject("result");
                    String name = jsonObject.getString("Name");
                    String id = String.valueOf(jsonObject.getInt("id"));
                    String latitude = jsonObject.getString("lat");
                    String longitude = jsonObject.getString("lon");
                    String image = "http://admin.authenticguards.com/storage/app/public/" + jsonObject.getString("image") + ".jpg";
                    ArrayList<MarkerData> markers = new ArrayList<MarkerData>();
                    Log.d(TAG, "markerLOOLLL " + name + id + latitude + longitude + image);
                    markers.clear();
                    if (!latitude.equals("-") && !longitude.equals("-")) {
                        markers.add(new MarkerData(id, image, Double.parseDouble(latitude), Double.parseDouble(longitude), name));
                        plotMarkers(markers);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }


    public void plotMarkers(ArrayList<MarkerData> markers) {
        if (markers.size() > 0) {
            for (MarkerData myMarker : markers) {

                markerOption = new MarkerOptions().title(myMarker.getName()).position(new LatLng(myMarker.getLat(), myMarker.getLng())).snippet(myMarker.getId());
                Marker location_marker = mMap.addMarker(markerOption);

                Target target = new PicassoMarker(location_marker);
                targets.add(target);
                Picasso.get().load(myMarker.getImageUrl()).resize(200, 200).centerCrop().transform(new BubleTransformation(20)).
                        into(target);

                mMarkersHashMap.put(location_marker, myMarker);

//                i = getIntent();
//                if (i.getBooleanExtra("maps", true)) {
//                    // buttonNavigasi.setVisibility(View.VISIBLE);

                location_marker.setTitle(myMarker.getName());
                moveCamera(new LatLng(myMarker.getLat(), myMarker.getLng()),
                        DEFAULT_ZOOM);
//                    LatLng dest = new LatLng(myMarker.getLat(), myMarker.getLng());
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dest, DEFAULT_ZOOM));
//                } else {
//                    Log.d(MapsActivity.class.getSimpleName(), "In else{}");
                // mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
//                }
            }
        }
    }

    protected Marker createMarker(final double latitude, final double longitude, final String title, final String image) {

        Marker mymarker;

        mymarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.0f, 0.5f)
                .title(title));

        Target target = new PicassoMarker(mymarker);
        Picasso.get().load(image).resize(250, 250).centerInside().transform(new BubleTransformation(50)).into(target);

        return mymarker;
    }

    public class PicassoMarker implements Target {
        Marker mMarker;

        PicassoMarker(Marker marker) {
            mMarker = marker;
        }

        @Override
        public int hashCode() {
            return mMarker.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PicassoMarker) {
                Marker marker = ((PicassoMarker) o).mMarker;
                return mMarker.equals(marker);
            } else {
                return false;
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }


        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

}
