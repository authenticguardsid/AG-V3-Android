package com.agreader.screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.agreader.R;
import com.agreader.ShareActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifiedProductActivity extends AppCompatActivity {

    private Button button;
    public String PARTNER = "";
    public String GCODE = "";

    private static TextView result_code, result_brand, result_company, result_address, result_phone, result_email, result_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_product);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();

        result_code = (TextView) findViewById(R.id.dtext2content);
        result_brand = (TextView) findViewById(R.id.dtext3content);
        result_company = (TextView) findViewById(R.id.dtext4content);
        result_address = (TextView) findViewById(R.id.dtext5content);
        result_phone = (TextView) findViewById(R.id.dtext6content);
        result_email = (TextView) findViewById(R.id.dtext7content);
        result_web = (TextView) findViewById(R.id.dtext8content);
        set_profile_company(getIntent().getStringExtra("key"));

        button = (Button) findViewById(R.id.product_detail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifiedProductActivity.this, ProductDetailActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void set_profile_company(String code){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.net/api/check_/" + code + "?token=a&appid=001", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("result");
                            GCODE = jsonObject.getString("code");
                            JSONObject dataclient = jsonObject.getJSONObject("client");
                            JSONObject data = jsonObject.getJSONObject("brand");
                            result_code.setText(GCODE);
                            result_brand.setText(data.getString("Name"));
                            result_company.setText(dataclient.getString("name"));
                            result_address.setText(data.getString("addressOfficeOrStore"));
                            result_phone.setText(data.getString("csPhone"));
                            result_email.setText(data.getString("csEmail"));
                            result_web.setText(data.getString("web"));
                        } catch (JSONException e) {

                        }
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
