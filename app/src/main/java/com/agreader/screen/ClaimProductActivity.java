package com.agreader.screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
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
import java.util.Map;

public class ClaimProductActivity extends AppCompatActivity {

    private static int IMG_CAMERA = 2;

    private Uri filepath;
    private Button btnShare;
    private ImageView imagePost, imageKlik;
    private String urlPost = "", code = "", token = "", gambar;
    private double longitude, latitude;
    private FirebaseUser firebaseUser;
    private FusedLocationProviderClient client;

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
                        Log.d("lol", "onCompleteBaru: " + token);
                        String result = "";
                    }
                });
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


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlPost, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(ClaimProductActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ClaimProductActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }


                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    Log.i("proses5", longitude + "");
                    Log.i("proses6", latitude + "");

                    response.put("photo", image);
                    response.put("loclong", longitude);
                    response.put("loclang", latitude);

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

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
}