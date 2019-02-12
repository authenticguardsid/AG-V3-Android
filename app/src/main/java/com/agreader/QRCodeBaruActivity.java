package com.agreader;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.agreader.screen.QRcodeActivity;
import com.agreader.screen.UnverifiedProductActivity;
import com.agreader.screen.VerifiedProductActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

public class QRCodeBaruActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private FirebaseUser firebaseUser;

    final int REQUEST_CODE_CAMERA = 999;

    String brand, company, address, phone, email, web;
    String GENIUNE_CODE = "success";
    String token = "", token2 = "";
    String GCODE = "";
    String rvalid;

    String size,color,material,price,distributor,expiredDate,img;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_baru);

        CodeScannerView scannerView = (CodeScannerView) findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                QRCodeBaruActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation_code(result.getText());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<GetTokenResult> task) {
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
                        Volley.newRequestQueue(QRCodeBaruActivity.this).add(jsonObjectRequest);
                        Log.e("token-firebase", "" + token2);
                    }
                });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCodeScanner.releaseResources();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_CAMERA){
            if(grantResults.length <0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "You don't have permission to access camera!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void validation_code(final String scancode){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/check_/"+scancode+"?token="+token2+"&appid=003&loclang=a&loclong=a", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            rvalid = response.getString("status");
                            JSONObject jsonObject = response.getJSONObject("result");
                            JSONObject resultObject = jsonObject.getJSONObject("product");

                            size = resultObject.getString("size");
                            color = resultObject.getString("color");
                            price = resultObject.getString("price");
                            material = resultObject.getString("material");
                            distributor = resultObject.getString("distributedOn");
                            expiredDate = resultObject.getString("expireDate");
                            img = resultObject.getString("image");

                            GCODE = jsonObject.getString("code");
                            Log.i("scancode",scancode);
                            Log.i("tokenFirebase",token2);
                            JSONObject dataclient = jsonObject.getJSONObject("client");
                            JSONObject data = jsonObject.getJSONObject("brand");
                            brand = data.getString("Name");
                            company = dataclient.getString("name");
                            address = data.getString("addressOfficeOrStore");
                            phone = data.getString("csPhone");
                            email = data.getString("csEmail");
                            web = data.getString("web");


                        } catch (JSONException e) {

                        }
                        Log.e("scancode", ""+scancode);
                        Log.e("token-firebase", ""+token);
                    }
                    if (rvalid.equals(GENIUNE_CODE)) {
                        Intent intent_geniune = new Intent(QRCodeBaruActivity.this, VerifiedProductActivity.class);
                        intent_geniune.putExtra("key", scancode);
                        intent_geniune.putExtra("brand", brand);
                        intent_geniune.putExtra("company", company);
                        intent_geniune.putExtra("address", address);
                        intent_geniune.putExtra("phone", phone);
                        intent_geniune.putExtra("email", email);
                        intent_geniune.putExtra("web", web);

                        intent_geniune.putExtra("size",size);
                        intent_geniune.putExtra("color",color);
                        intent_geniune.putExtra("material",material);
                        intent_geniune.putExtra("price",price);
                        intent_geniune.putExtra("distributor",distributor);
                        intent_geniune.putExtra("expiredDate",expiredDate);
                        intent_geniune.putExtra("image",img);
                        startActivity(intent_geniune);
                    }else {
                        Intent intent_fake = new Intent(QRCodeBaruActivity.this, UnverifiedProductActivity.class);
                        startActivity(intent_fake);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void displayLoader() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Account Verification...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}
