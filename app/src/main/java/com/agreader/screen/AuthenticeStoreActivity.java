package com.agreader.screen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.agreader.R;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticeStoreActivity extends FragmentActivity  {

    GoogleMap mMap;
    FirebaseUser firebaseUser;
    String token = "", token2 = "";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentice_store);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        token = task.getResult().getToken();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/getuser?token="+token+"&appid=003", null, new Response.Listener<JSONObject>() {
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
                if (ActivityCompat.checkSelfPermission(AuthenticeStoreActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AuthenticeStoreActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AuthenticeStoreActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    return;
                } else {
                    list_store();
                }
            }
        });

    }

    public void list_store(){
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
