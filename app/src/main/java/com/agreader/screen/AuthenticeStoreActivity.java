package com.agreader.screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.agreader.R;
import com.agreader.model.ListStore;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class AuthenticeStoreActivity extends FragmentActivity  {

    GoogleMap mMap;
    FirebaseUser firebaseUser;
    String token = "", token2 = "";

    Button bCallClinic, bDirectionsClinic;

    private SlidingUpPanelLayout mLayout;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;

    String brand_name ="";
    double currentLatitude,currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentice_store);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        bDirectionsClinic = (Button) findViewById(R.id.bDirectionsClinic);
        bCallClinic = (Button) findViewById(R.id.bCallClinic);

        brand_name = getIntent().getStringExtra("brand_name");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        token = task.getResult().getToken();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/getuser?token=" + token + "&appid=003", null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                token2 = token;
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        Volley.newRequestQueue(AuthenticeStoreActivity.this).add(jsonObjectRequest);
                        Log.e("token-firebase", "" + token);
                        Log.e("token2-firebase", "" + token2);
                    }
                });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(AuthenticeStoreActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AuthenticeStoreActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AuthenticeStoreActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AuthenticeStoreActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    ActivityCompat.requestPermissions(AuthenticeStoreActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_REQUEST_CODE);
                    return;
                } else {
                    //list_store();
                    try{
                        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(AuthenticeStoreActivity.this, R.raw.style_maps));
                        if(!success){
                            Log.e("ERROR", "Style Parsing Failed");
                        }
                    }catch (Resources.NotFoundException e){
                        Log.e("ERROR", "Can't find style, error : "+e);
                    }

                    if (brand_name.equals("SABICHI")){
                        currentLatitude = -6.6867070;
                        currentLongitude = 107.02222;
                    }else if (brand_name.equals("Doa Indonesia")){
                        currentLatitude = -6.943330;
                        currentLongitude = 107.613670;
                    }else if (brand_name.equals("DEENAY")){
                        currentLatitude = -6.943320;
                        currentLongitude = 107.613904;
                    }else if (brand_name.equals("MERZ")){
                        currentLatitude = 1.169968;
                        currentLongitude = 104.003014;
                    }

                    double currentLatitude1 = 1.169968;
                    double currentLongitude1 = 104.003014;

                   /* double currentLatitude = -6.943330;
                    double currentLongitude = 107.613670;*/
                    LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                    googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Lorem Ipsum")
                            //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                    );

                    LatLng lol = new LatLng(currentLatitude, currentLongitude);
                    googleMap.addMarker(new MarkerOptions()
                                    .position(lol)
                                    .title("Lorem Ipsum")
                            //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                    );
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)      // Sets the center of the map to location user
                            .zoom(17)                   // Sets the zoom
                            .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            final String CliniNameStreet = "Deenay";
                            final double vlat = -6.943330;
                            final double vlong = 107.613670;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                                }
                            }, TIME_OUT);
                            bCallClinic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+6287825936365", null)));
                                }
                            });
                            bDirectionsClinic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Uri gmmIntentUri = Uri.parse("geo:"+vlat+","+vlong+"?q="+CliniNameStreet);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    startActivity(mapIntent);
                                }
                            });

                            return true;
                        }
                    });
                }
            }
        });

    }

    public void list_store() {
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.net/api/locator_?token=a&appid=001", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("result");
                            for (int j = 0; j < jsonArray.length() ; j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                String keypartner = jsonObject.getString("id");
                                String clinicname = jsonObject.getString("Name");
                                double currentLatitude = jsonObject.getDouble("lat");
                                double currentLongitude = jsonObject.getDouble("lon");
                                mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(currentLatitude, currentLongitude))
                                                .title(clinicname)
                                                .snippet(keypartner)
                                        //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                                );

                                Log.e("syalala", "hasil : "+keypartner+" "+clinicname+" "+currentLatitude+" "+currentLongitude);
                            }

                        } catch (JSONException e) {

                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(AuthenticeStoreActivity.this).add(jsonObjectRequest2);
    }

}
