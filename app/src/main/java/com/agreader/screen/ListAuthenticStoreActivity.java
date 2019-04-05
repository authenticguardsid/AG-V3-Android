package com.agreader.screen;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import static com.facebook.FacebookSdk.getApplicationContext;

public class ListAuthenticStoreActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private String token="", token2="";
    private ListStoreAdapter listStoreAdapter;
    private ProgressBar pDialog;
    private ArrayList<ListStore> modelArrayList;
    String idbrand = "",
            namebrand = "",
            imagebrand = "",
            addressbrand = "";

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

        getBrand(token);

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

    private void getBrand(String token) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/locator_?token=" + token + "&appid=003", null, new Response.Listener<JSONObject>() {
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
                        modelArrayList.add(new ListStore(idbrand, "http://admin.authenticguards.com/storage/app/public/" + imagebrand + ".jpg", namebrand, addressbrand));
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




}