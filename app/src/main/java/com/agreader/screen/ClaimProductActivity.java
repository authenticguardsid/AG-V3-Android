package com.agreader.screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.utils.DataRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ClaimProductActivity extends AppCompatActivity implements LocationListener {

    private static int IMG_CAMERA = 2;

    private Uri filepath;
    private Button btnShare;
    private ImageView imagePost, imageKlik;
    private String urlPost = "", code = "", token = "", gambar;
    private double longitude, latitude, loc, lang;
    private FirebaseUser firebaseUser;
    private FusedLocationProviderClient client;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_product);
        client = LocationServices.getFusedLocationProviderClient(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        code = getIntent().getStringExtra("code");
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        token = task.getResult().getToken();
                        Log.d("ClaimActivity", "new: " + token);
                        String result = "";
                    }
                });
        Log.d("ClaimActivity", "onCompleteBaru: " + token + code);
        urlPost = "http://admin.authenticguards.com/api/claim_/" + code + "?token=" + token + "&appid=003";

        btnShare = (Button) findViewById(R.id.btn_share);
        imagePost = (ImageView) findViewById(R.id.img_post);
        imageKlik = (ImageView) findViewById(R.id.img_klik);

        btnShare.setVisibility(View.GONE);
        imageKlik.setVisibility(View.VISIBLE);

        imageKlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(ClaimProductActivity.this);
            }
        });


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClaimProductActivity.this, ShareActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Log.d("cobacoba", "onCreate: " + longitude + latitude);
        getLocation();
        Log.d("cobacoba", "onCreate: " + longitude + latitude);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    filepath = result.getUri();
                    imagePost.setImageURI(filepath);
                    Log.d("isigambar", "onActivityResult: " + filepath);
                    btnShare.setVisibility(View.VISIBLE);
                    imageKlik.setVisibility(View.GONE);
                    Bitmap bitmapnew = null;
                    Log.d("isigambar", "onClick: " + filepath);
                    try {
                        bitmapnew = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap lastBitmap = null;
                    lastBitmap = bitmapnew;
                    Log.d("isigambar", "onClick: " + lastBitmap);
                    String image = getStringImage(lastBitmap);
                    uploadImage(image);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

            } else if (requestCode == IMG_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                Bitmap bitmapnew = null;
                Log.d("isigambar", "onClick: " + filepath);
                try {
                    bitmapnew = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap lastBitmap = null;
                lastBitmap = bitmapnew;
                Log.d("isigambar", "onClick: " + lastBitmap);
                String image = getStringImage(lastBitmap);
                uploadImage(image);
                Log.d("tes1", longitude + latitude + image);
                imagePost.setImageBitmap(thumbnail);
                btnShare.setVisibility(View.VISIBLE);
                imageKlik.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            Log.d("ClaimProduct", "onActivityResult: " + e.toString());
            Toast.makeText(ClaimProductActivity.this, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage(final String image) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("uploade", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ClaimProductActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("photo", image);
                params.put("loclang", String.valueOf(lang));
                params.put("loclong", String.valueOf(loc));
                return params;
            }
        };
        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    protected void getLocation() {
        if (isLocationEnabled(ClaimProductActivity.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(ClaimProductActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ClaimProductActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ClaimProductActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            } else {
            }
            // Write you code here if permission already given.
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                lang = location.getLatitude();
                loc = location.getLongitude();
                Log.d("cobacoba", "getLocation: " + loc + lang);
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
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