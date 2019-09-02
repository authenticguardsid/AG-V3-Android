package com.agreader.screen;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.adapter.ListStoreAdapter;
import com.agreader.model.ListStore;
import com.agreader.utils.CustomItemClickListener;
import com.agreader.utils.DataRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.agreader.screen.ClaimProductActivity.isLocationEnabled;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ListAuthenticStoreActivity extends AppCompatActivity implements LocationListener {

    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private String token="", token2="";
    private ListStoreAdapter listStoreAdapter;
    private ProgressBar pDialog;
    private double longitude, latitude;
    private ArrayList<ListStore> modelArrayList;
    String idbrand = "",
            namebrand = "",
            imagebrand = "",
            addressbrand = "";

    private FusedLocationProviderClient client;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;

    private static final String TAG = "FindingFriend";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.noimage, R.drawable.noimage, R.drawable.noimage, R.drawable.noimage};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_authentic_store);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_authentic_store);
        modelArrayList = new ArrayList<>();

        ImageView imgBack = findViewById(R.id.backPressStore);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pDialog = (ProgressBar) findViewById(R.id.progressBrand);

        token = DataRequest.getResultToken(getApplicationContext());


        carouselView = (CarouselView) findViewById(R.id.slider);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        getLocation();

        listStoreAdapter = new ListStoreAdapter(this, modelArrayList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (isServicesOK()) {
                    Intent intent = new Intent(ListAuthenticStoreActivity.this, AuthenticeStoreActivity.class);
                    intent.putExtra("id", modelArrayList.get(position).getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(ListAuthenticStoreActivity.this, "Tou dont have permission yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ListAuthenticStoreActivity.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(ListAuthenticStoreActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(listStoreAdapter);

    }

    @Override
    public void onBackPressed() {

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    private void getBrand(String token, double latitudeString, double longitudeString) {
        modelArrayList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://admin.authenticguards.com/api/locator_?token=" + token + "&appid=003&lat=" + latitudeString + "&lon=" + longitudeString, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        idbrand = data.getString("id");
                        namebrand = data.getString("Name");
                        imagebrand = data.getString("image");
                        Log.d("bismillahinidia", "onResponse: " + imagebrand);
                        addressbrand = data.getString("addressOfficeOrStore");
                        modelArrayList.add(new ListStore(idbrand, "https://admin.authenticguards.com/storage/" + imagebrand + ".jpg", namebrand, addressbrand));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listStoreAdapter.notifyDataSetChanged();
                pDialog.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }

    public boolean isServicesOK() {
        Log.d("lol", "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ListAuthenticStoreActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d("lol", "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d("lol", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(ListAuthenticStoreActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    protected void getLocation() {
        if (isLocationEnabled(ListAuthenticStoreActivity.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(ListAuthenticStoreActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ListAuthenticStoreActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ListAuthenticStoreActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            } else {
            }
            // Write you code here if permission already given.
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getBrand(token, latitude, longitude);
                Log.d("cobacoba", "getLocation: " + latitude + " dan " + longitude);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        getLocation();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}