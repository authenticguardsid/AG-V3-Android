package com.agreader.screen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.model.ListStore;
import com.agreader.model.MarkerData;
import com.agreader.utils.BubleTransformation;
import com.agreader.utils.FetchURL;
import com.agreader.utils.TaskLoadedCallback;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class AuthenticeStoreActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    FloatingActionButton fab;
    String id;
    Utils utils;
    double kmdistance;
    View progressOverlay;
    private MarkerOptions place1, place2;
    ;
    double currentLatitude, currentLongitude, targetLongitude, targetLatitude;
    MarkerOptions markerOption;
    List<Target> targets;
    String title, address, callnumber;
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

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                    .anchor(0.5f, 0.5f));
                            currentLatitude = currentLocation.getLatitude();
                            currentLongitude = currentLocation.getLongitude();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms\
                                    Intent intent = getIntent();
                                    id = intent.getStringExtra("id");
                                    displaymarker(id, currentLatitude, currentLongitude);
                                }
                            }, 100);
                            place1 = new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Location 1");



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
        utils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                utils.animateView(progressOverlay, View.GONE, 0, 200);
            }
        }, TIME_OUT);
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


    public void displaymarker(String id, final double culat, final double culong) {
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
                    String waStore = jsonObject.getString("whatsapp");
                    String callStore = jsonObject.getString("csPhone");
                    String address = jsonObject.getString("addressOfficeOrStore");
                    String image = "http://admin.authenticguards.com/storage/app/public/" + jsonObject.getString("image") + ".jpg";
                    ArrayList<MarkerData> markers = new ArrayList<MarkerData>();
                    Log.d(TAG, "markerLOOLLL " + name + id + latitude + longitude + image);
                    markers.clear();

//                    place2 = new MarkerOptions().position(new LatLng(targetLatitude, targetLongitude)).title("Location 2");
                    Log.d("culatculong", "onResponse: " + culat + culong);
//                    new FetchURL(AuthenticeStoreActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                    if (!latitude.equals("-") && !longitude.equals("-")) {
                        targetLatitude = Double.parseDouble(latitude);
                        targetLongitude = Double.parseDouble(longitude);
                        markers.add(new MarkerData(id, image, Double.parseDouble(latitude), Double.parseDouble(longitude), name));
                        plotMarkers(markers);
                        moveCameraStore(new LatLng(targetLatitude, targetLongitude), DEFAULT_ZOOM);
                        displaySliding(image, name, waStore, callStore, address);
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

    public void displaySliding(String image, String name, String wa, final String call, final String address) {

        final ImageView iconMarker = (ImageView) findViewById(R.id.iconMarker);
        final TextView nameStore = (TextView) findViewById(R.id.name_store);
        final TextView callStore = (TextView) findViewById(R.id.call_store);
        final TextView addressStore = (TextView) findViewById(R.id.address_store);
        final TextView distaceStore = (TextView) findViewById(R.id.distance_store);
        final Button buttonCall = (Button) findViewById(R.id.button_call_store);
        final Button buttonDirection = (Button) findViewById(R.id.button_call_direction);
        CalculationByDistance(new LatLng(currentLatitude, currentLongitude), new LatLng(targetLatitude, targetLongitude));
        String distancereal = String.valueOf(kmdistance);
        Picasso.get().load(image).into(iconMarker);
        nameStore.setText(name);
        callStore.setText("Whatsapp :" + wa + " Call : " + call);
        addressStore.setText(address);
        distaceStore.setText(distancereal.substring(0, 4) + " KM");
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", call, null)));
            }
        });

        buttonDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:" + targetLatitude + "," + targetLongitude + "?q=" + address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        kmdistance = km;
        return Radius * c;
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

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route

        Log.d("lognaaa", "getUrl: " + origin + dest + directionMode);
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyDZwjotwJaIdIwEWjZYcJwjsDHyTm3Mpl8";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {

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
