package com.agreader.screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.utils.DataRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;

public class QRCodeBaruActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private FirebaseUser firebaseUser;
    private static final int RC_PERMISSION = 10;
    private boolean mPermissionGranted;
    final int REQUEST_CODE_CAMERA = 999;

    private String name, address, phone, web, product;
    private String GENIUNE_CODE = "success";
    private String token = "";
    private String rvalid;
    private String history;
    String GCODE = "";
    String size,color,material,price,distributor,expiredDate,img,brand,company,email;
    ProgressDialog pDialog;

    private double longitude, latitude;

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_baru);


        CodeScannerView scannerView = (CodeScannerView) findViewById(R.id.scanner_view);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        token = Objects.requireNonNull(task.getResult()).getToken();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://admin.authenticguards.com/api/getuser?token=" + token + "&appid=003", null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("status-token", "berhasil terdaftar");
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        Volley.newRequestQueue(QRCodeBaruActivity.this).add(jsonObjectRequest);
                        Log.e("status-token", "token-firebase : "+token);
                    }
                });


        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                QRCodeBaruActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(300);
                        }
                        displayLoader();
                        String resultcode = result.getText();
                        int length = resultcode.length();
                        Log.d("length", "run: " + length);
//                        if (length <= 9) {
                        if(firebaseUser != null){
                            validation_code(result.getText());
                        }else{
                            Intent intent = new Intent(QRCodeBaruActivity.this,LoginScreenActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        mCodeScanner.setErrorCallback(error -> runOnUiThread(
                () -> Toast.makeText(QRCodeBaruActivity.this, getString(R.string.scanner_error, error), Toast.LENGTH_LONG).show()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[] {Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        getLocation();
    }

    public void validation_code(final String scancode){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://admin.authenticguards.com/api/check_/"+scancode+"?token="+token+"&appid=003&loclang="+latitude+"&loclong="+longitude, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            rvalid = response.getString("status");
                            JSONObject jsonObject = response.getJSONObject("result");
                            if (!jsonObject.isNull("product")) {
                                Log.d("dodol", "kondisi 1");
                                JSONObject resultObject = jsonObject.getJSONObject("product");
                                Log.d("dodol", "kondisi 1");
                                size = resultObject.getString("size");
                                color = resultObject.getString("color");
                                price = resultObject.getString("price");
                                material = resultObject.getString("material");
                                distributor = resultObject.getString("distributedOn");
                                expiredDate = resultObject.getString("expireDate");
                                img = resultObject.getString("image");
                                GCODE = jsonObject.getString("code");
                                Log.i("scancode", scancode);
                                JSONObject dataclient = jsonObject.getJSONObject("client");
                                JSONObject data = jsonObject.getJSONObject("brand");
                                brand = data.getString("Name");
                                company = dataclient.getString("name");
                                address = data.getString("addressOfficeOrStore");
                                phone = data.getString("csPhone");
                                email = data.getString("csEmail");
                                web = data.getString("web");
                            } else {
                                Log.d("dodol", "kondisi 2");
                                JSONObject newresultObject = jsonObject.getJSONObject("package_code");
                                JSONObject brandnew = jsonObject.getJSONObject("brand");
                                JSONObject clientnew = jsonObject.getJSONObject("client");
                                size = "unregistered";
                                color = "unregistered";
                                price = "unregistered";
                                material = "unregistered";
                                distributor = "unregistered";
                                expiredDate = "unregistered";
                                img = "unregistered";
                                GCODE = jsonObject.getString("code");
                                brand = brandnew.getString("Name");
                                company = clientnew.getString("name");
                                address = clientnew.getString("address");
                                phone = clientnew.getString("phone");
                                email = clientnew.getString("email");
                                web = clientnew.getString("web");
                            }
                        } catch (JSONException ignored) {

                        }
                    }
                    if (rvalid.equals(GENIUNE_CODE)) {
                        pDialog.dismiss();
                        Intent intent_geniune = new Intent(QRCodeBaruActivity.this, VerifiedProductActivity.class);
                        intent_geniune.putExtra("key", scancode);
                        intent_geniune.putExtra("brand", brand);
                        intent_geniune.putExtra("company", company);
                        intent_geniune.putExtra("address", address);
                        intent_geniune.putExtra("phone", phone);
                        intent_geniune.putExtra("email", email);
                        intent_geniune.putExtra("web", web);

                        intent_geniune.putExtra("code", GCODE);

                        intent_geniune.putExtra("size",size);
                        intent_geniune.putExtra("color",color);
                        intent_geniune.putExtra("material",material);
                        intent_geniune.putExtra("price",price);
                        intent_geniune.putExtra("distributor",distributor);
                        intent_geniune.putExtra("expiredDate",expiredDate);
                        intent_geniune.putExtra("image",img);
                        startActivity(intent_geniune);
                    } else {
                        pDialog.dismiss();
                        Intent intent_fake = new Intent(QRCodeBaruActivity.this, UnverifiedProductActivity.class);
                        startActivity(intent_fake);
//                        finish();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QRCodeBaruActivity.this, "Wrong Code, Check your connection", Toast.LENGTH_SHORT).show();
                Intent intent_fake = new Intent(QRCodeBaruActivity.this, UnverifiedProductActivity.class);
                startActivity(intent_fake);
//                finish();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    protected void getLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d("lol", "latitude"+latitude+"longitude"+longitude);
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionGranted) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }
    private void displayLoader() {
        pDialog = new ProgressDialog(QRCodeBaruActivity.this);
        pDialog.setMessage("Scanning...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


}
