package com.agreader.screen;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    ArrayList<String> arrayList = new ArrayList<String>();
    String size,color,material,price,distributor,expiredDate,img;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_baru);

        arrayList.add("");
        CodeScannerView scannerView = (CodeScannerView) findViewById(R.id.scanner_view);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                QRCodeBaruActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                        Toast.makeText(QRCodeBaruActivity.this, "Code : " + result.getText(), Toast.LENGTH_SHORT).show();
                        token2 = DataRequest.getResultToken(getApplicationContext());
                        String resultcode = result.getText();
                        int length = resultcode.length();
                        Log.d("length", "run: " + length);
//                        if (length <= 9) {
                            validation_code(result.getText(), token2);
//                        } else {
//                            Intent intent = new Intent(QRCodeBaruActivity.this, UnverifiedProductActivity.class);
//                            startActivity(intent);
//                        }
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


    public void validation_code(final String scancode, String tokenbaru) {
        displayLoader();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/check_/" + scancode + "?token=" + tokenbaru + "&appid=003&loclang=a&loclong=a", null, new Response.Listener<JSONObject>() {
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
                                Log.i("tokenFirebase", token2);
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

                        } catch (JSONException e) {
                            Log.e("erorpisan", "onResponse: " + e);
                        }
                        Log.e("scancode", ""+scancode);
                        Log.e("token-firebase", ""+token);
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
                    } else if (scancode.equals("AG-PS1CW9")) {
                        Intent intent = new Intent(QRCodeBaruActivity.this, Certificate.class);
                        intent.putExtra("Code", scancode);
                        intent.putExtra("Name", "Muhamad Taufiq Ramadhan (6706162106)");
                        intent.putExtra("job", "Android Developer");
                        intent.putExtra("company", "PT Authentikasi Garda Teknologi");
                        intent.putExtra("instance", "Telkom University");
                        intent.putExtra("in", "2 November 2018");
                        intent.putExtra("out", "2 Februari 2019");
                        intent.putExtra("nilai", "Sangat Baik (A)");
                        startActivity(intent);
                    } else if (scancode.equals("AG-U3NBCF")) {
                        Intent intent = new Intent(QRCodeBaruActivity.this, Certificate.class);
                        intent.putExtra("Code", scancode);
                        intent.putExtra("Name", "Rahmad Satria Kurniawan (6706162127)");
                        intent.putExtra("job", "Android Developer");
                        intent.putExtra("company", "PT Authentikasi Garda Teknologi");
                        intent.putExtra("instance", "Telkom University");
                        intent.putExtra("in", "22 November 2018");
                        intent.putExtra("out", "22 Februari 2019");
                        intent.putExtra("nilai", "Sangat Baik (A)");
                        startActivity(intent);
                    } else if (scancode.equals("AG-F0OIEQ")) {
                        Intent intent = new Intent(QRCodeBaruActivity.this, Certificate.class);
                        intent.putExtra("Code", scancode);
                        intent.putExtra("Name", "Yudhistira Caraka (6706164022)");
                        intent.putExtra("job", "Android Developer");
                        intent.putExtra("instance", "Telkom University");
                        intent.putExtra("in", "22 November 2018");
                        intent.putExtra("out", "22 Februari 2019");
                        intent.putExtra("company", "PT Authentikasi Garda Teknologi");
                        intent.putExtra("nilai", "Baik (B)");
                        startActivity(intent);
                    } else if (scancode.equals("AG-019BCJ")) {
                        Intent intent = new Intent(QRCodeBaruActivity.this, Certificate.class);
                        intent.putExtra("Code", scancode);
                        intent.putExtra("Name", "Ficky Ikhsan Sujana (201503024)");
                        intent.putExtra("job", "Business Developmet");
                        intent.putExtra("instance", "Unisadhuguna Business School");
                        intent.putExtra("in", "2 Februari 2018");
                        intent.putExtra("out", "5 Mei 2019");
                        intent.putExtra("company", "PT Authentikasi Garda Teknologi");
                        intent.putExtra("nilai", "Sangat Baik (A)");
                        startActivity(intent);
                    } else {
                        pDialog.dismiss();
                        Intent intent_fake = new Intent(QRCodeBaruActivity.this, UnverifiedProductActivity.class);
                        startActivity(intent_fake);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Code Verification...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}
