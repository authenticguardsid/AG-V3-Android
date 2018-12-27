package com.agreader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.agreader.screen.UnverifiedProductActivity;
import com.agreader.screen.VerifiedProductActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRcodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    FirebaseUser firebaseUser;
    String GENIUNE_CODE = "success";
    String GCODE = "";
    String token = "";
    String rvalid;

    String brand, company, address, phone, email, web;

    final int REQUEST_CODE_CAMERA = 999;

    ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        token = task.getResult().getToken();
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_CAMERA){
            if(grantResults.length <0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "You don't have permission to access camera!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        validation_code(result.getText());
    }

    public void validation_code(final String scancode){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/check_/"+scancode+"?token="+token+"&appid=003&loclang=a&loclong=a", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            rvalid = response.getString("status");
                            JSONObject jsonObject = response.getJSONObject("result");
                            GCODE = jsonObject.getString("code");
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
                        Intent intent_geniune = new Intent(QRcodeActivity.this, VerifiedProductActivity.class);
                        intent_geniune.putExtra("key", scancode);
                        intent_geniune.putExtra("brand", brand);
                        intent_geniune.putExtra("company", company);
                        intent_geniune.putExtra("address", address);
                        intent_geniune.putExtra("phone", phone);
                        intent_geniune.putExtra("email", email);
                        intent_geniune.putExtra("web", web);
                        startActivity(intent_geniune);
                    }else {
                        Intent intent_fake = new Intent(QRcodeActivity.this, UnverifiedProductActivity.class);
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


}
