package com.agreader.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailNotifActivity extends AppCompatActivity {

    private String key;
    private ImageView imageView, mBackPressed;
    private TextView title, date, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notif);
        imageView = (ImageView) findViewById(R.id.image_notif_click);
        title = (TextView) findViewById(R.id.title_notif_click);
        date = (TextView) findViewById(R.id.date_notif_click);
        message = (TextView) findViewById(R.id.message_notif_click);
        mBackPressed = (ImageView) findViewById(R.id.backPressNotif) ;
        mBackPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getData(getIntent().getStringExtra("id"));
    }

    @Override
    public void onBackPressed() {

    }

    private void getData(String key){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/notification/" + key + "?appid=003", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.length() > 0){
                    try {
                        for (int j = 0; j < response.length() ; j++) {
                            title.setText(response.getString("title"));
                            message.setText(response.getString("message"));
                            date.setText(response.getString("created_at"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
