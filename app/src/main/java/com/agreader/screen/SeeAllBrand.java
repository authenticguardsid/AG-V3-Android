package com.agreader.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.ImageView;

import com.agreader.R;
import com.agreader.adapter.AllBrandAdapter;
import com.agreader.adapter.BrandAdapter;
import com.agreader.adapter.NewsAdapter;
import com.agreader.model.AllbrandModel;
import com.agreader.model.Brand;
import com.agreader.model.NewsModel;
import com.agreader.utils.DataRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SeeAllBrand extends AppCompatActivity {

    View progress, emptyView;
    RecyclerView recyclerView;
    ImageView back;
    String token, JSON;
    ;
    private ArrayList<String> mDataId;
    private AllBrandAdapter mAdapter;
    private ActionMode mActionMode;
    JSONObject brand, client;
    private ArrayList<AllbrandModel> mData = new ArrayList<>();
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_brand);

        token = DataRequest.getResultToken(getApplicationContext());
        Log.d("lol", "onCreate: LOOL" + token);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MasterActivity.class);
                startActivity(intent);
            }
        });

        mDataId = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.listAllbrand);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SeeAllBrand.this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new AllBrandAdapter(getApplicationContext(), mData, mDataId,
                new AllBrandAdapter.ClickHandler() {
                    @Override
                    public void onItemClick(int position) {
                        if (mActionMode != null) {
                            mAdapter.toggleSelection(mDataId.get(position));
                            if (mAdapter.selectionCount() == 0)
                                mActionMode.finish();
                            else
                                mActionMode.invalidate();
                            return;
                        }
                        AllbrandModel pet = mData.get(position);
                        Intent intent = new Intent(getApplicationContext(), FeaturedDetailActivity.class);
                        intent.putExtra("id", pet.getId());
                        intent.putExtra("name", pet.getName());
                        intent.putExtra("image", pet.getImage());
                        startActivity(intent);
                    }
                });
        recyclerView.setAdapter(mAdapter);
        getBrand();

    }

    private void getBrand() {
        String url = "https://admin.authenticguards.com/api/feature?appid=003";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject json= jsonObject.getJSONObject("result");
                    JSONArray jsonArray = json.getJSONArray("data");
                    Log.d("twtw", "onResponse: " + jsonArray);
                    for (int i = 0; i < jsonArray.length() ; i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        Log.d("lol", "ini data json" + data);
                        int id = data.getInt("id");
                        String idString = String.valueOf(id);
                        String image = data.getString("image");
                        String name = data.getString("Name");
                        String address = data.getString("addressOfficeOrStore");
                        Log.d("twtw", "onResponse: " +image);
                        JSONObject brand = data.getJSONObject("client");
                        Log.d("tolil", "onResponse: " + brand.getString("name"));
                         String client = brand.getString("name");
                        mData.add(new AllbrandModel(idString,"https://admin.authenticguards.com/storage/"+image+".jpg",name,address,client ));
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("twtw", "onErrorResponse: " + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
