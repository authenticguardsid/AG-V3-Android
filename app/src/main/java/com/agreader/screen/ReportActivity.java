package com.agreader.screen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agreader.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private static EditText edit_naddress, edit_ncity, edit_nclinic,edit_nreason;
    private static Button btn_sendfake, btn_fakeCancel;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mAuth = FirebaseAuth.getInstance();
        edit_nclinic = (EditText) findViewById(R.id.edit_nclinic);
        edit_naddress = (EditText) findViewById(R.id.edit_naddress);
        edit_ncity = (EditText) findViewById(R.id.edit_ncity);
        edit_nreason = (EditText) findViewById(R.id.edit_nreason);
        btn_sendfake = (Button) findViewById(R.id.btn_sendfake);
        btn_sendfake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        btn_fakeCancel = (Button) findViewById(R.id.btn_fakeCancel);
        btn_fakeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelReport();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        token = task.getResult().getToken();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/getuser?token=" + token + "&appid=002", null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("token-firebase", "token-firebase : " + token);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        Volley.newRequestQueue(ReportActivity.this).add(jsonObjectRequest);
                    }
                });

    }

    private void cancelReport() {
        startActivity(new Intent(this, MasterActivity.class));
        finish();
    }

    private void checkValidation() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String personEmail = currentUser.getEmail();
        final String getNclinic = edit_nclinic.getText().toString();
        final String getAddress = edit_naddress.getText().toString();
        final String getCity = edit_ncity.getText().toString();
        final String getReason = edit_nreason.getText().toString();

        if (getNclinic.equals("") || getNclinic.length() == 0
                || getAddress.equals("") || getAddress.length() == 0
                || getCity.equals("") || getCity.length() == 0
                || getReason.equals("") || getReason.length() == 0){
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue queue = Volley.newRequestQueue(ReportActivity.this);
            StringRequest postRequest = new StringRequest(Request.Method.POST, "http://admin.authenticguards.com/api/report?token="+token+"&appid=003", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response", "response berhasil : "+response);
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Response", "response error : "+error);
                }
            }
            ){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("namaKlinik", getNclinic);
                    params.put("kota",getCity);
                    params.put("isiLaporan",getReason);
                    params.put("alamatKlinik",getAddress);
                    return params;
                }
            };
            queue.add(postRequest);
            Toast.makeText(this, "Send report success", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent(this, MasterActivity.class);
            setResult(RESULT_CANCELED, returnIntent);
            finish();
        }
    }

}
