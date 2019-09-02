package com.agreader.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agreader.R;
import com.agreader.adapter.NewsAdapter;
import com.agreader.model.NewsModel;
import com.agreader.utils.DataRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HighLightScreen extends AppCompatActivity {

    FirebaseUser firebaseUser;
    View progress, emptyView;
    RecyclerView recyclerView;
    ImageView back;
    TextView titleBar;
    private DatabaseReference database;
    String token, JSON;
    ProgressBar pDialog;
    private ArrayList<String> mDataId;
    private NewsAdapter mAdapter;
    private ActionMode mActionMode;
    JSONObject brand, client;
    private ArrayList<NewsModel> mData = new ArrayList<>();
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mData.clear();
//        getDataHighlight(token);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_light_screen);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        token = DataRequest.getResultToken(getApplicationContext());
        Log.d("lol", "onCreate: LOOL" + token);

        back = (ImageView) findViewById(R.id.backPress);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MasterActivity.class);
                startActivity(intent);
            }
        });
        titleBar = (TextView) findViewById(R.id.titleBar);
        mDataId = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("news");
        recyclerView = (RecyclerView) findViewById(R.id.listNews);
        recyclerView.setHasFixedSize(false);

        pDialog = (ProgressBar) findViewById(R.id.progressLoad);

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        TextView rootEmpty = (TextView) findViewById(R.id.rootText);
        TextView childEmpty = (TextView) findViewById(R.id.childText);

        emptyView = (View) findViewById(R.id.emptyView);
        String news = getIntent().getStringExtra("hasil");
        if (news.equals("1")) {
            getDataStories(token);
            titleBar.setText("AG STORIES");
            rootEmpty.setText("No Stories Yet");
            childEmpty.setText("Stories will appear here");
        } else if (news.equals("0")) {
            titleBar.setText("HIGHLIGHT");
            getDataHighlight(token);
            rootEmpty.setText("No Highlight Yet");
            childEmpty.setText("Highlight will appear here");
        }

        mAdapter = new NewsAdapter(getApplicationContext(), mData, mDataId, emptyView, progress,
                new NewsAdapter.ClickHandler() {
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
                        NewsModel pet = mData.get(position);
                        if (pet.getType().equals("news")) {
                            Intent intent = new Intent(getApplicationContext(), DetailHighlightScreen.class);
                            intent.putExtra("id", pet.getId());
                            intent.putExtra("image", pet.getImage());
                            intent.putExtra("title", pet.getTitle());
                            intent.putExtra("price", pet.getPrice());
                            intent.putExtra("time", pet.getTime());
                            intent.putExtra("description", pet.getDescription());
                            intent.putExtra("termCondition", pet.getTermCondition());
                            intent.putExtra("agClientBrand_id", pet.getAgClientBrand_id());
                            intent.putExtra("type", pet.getType());
                            intent.putExtra("url", pet.getUrl());
                            startActivity(intent);
                        } else if (pet.getType().equals("promo")) {
                            Intent intent = new Intent(getApplicationContext(), HighLightPromo.class);
                            intent.putExtra("id", pet.getId());
                            intent.putExtra("image", pet.getImage());
                            intent.putExtra("title", pet.getTitle());
                            intent.putExtra("price", pet.getPrice());
                            intent.putExtra("time", pet.getTime());
                            intent.putExtra("description", pet.getDescription());
                            intent.putExtra("termCondition", pet.getTermCondition());
                            intent.putExtra("agClientBrand_id", pet.getAgClientBrand_id());
                            intent.putExtra("type", pet.getType());
                            intent.putExtra("url", pet.getUrl());
                            startActivity(intent);
                        } else {
                            Intent intentstory = new Intent(getApplicationContext(), StoryDetail.class);
                            intentstory.putExtra("title", pet.getTitle());
                            intentstory.putExtra("time", pet.getTime());
                            intentstory.putExtra("article", pet.getArtikel());
                            intentstory.putExtra("image", pet.getImage());
                            intentstory.putExtra("url", pet.getUrl());
                            startActivity(intentstory);
                        }
                    }

                    @Override
                    public boolean onItemLongClick(int position) {
                        if (mActionMode != null) return false;

                        return false;
                    }
                });
        recyclerView.setAdapter(mAdapter);

    }

    private void getDataHighlight(String token) {
        Log.d("1", "HERE!!! ");
//        recyclerView.getRecycledViewPool().clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://admin.authenticguards.com/api/newspromo_?&appid=003&loclang=a&loclong=a", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    JSON = response.toString();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        Log.d("lol", "ini data json" + data);
                        int id = data.getInt("id");
                        String idString = String.valueOf(id);
                        String image = data.getString("image");
                        String title = data.getString("title");
                        String time = data.getString("time");
                        String price = data.getString("price");
                        String termCondition = data.getString("termCondition");
                        String description = data.getString("description");
                        String agClientBrand_id = data.getString("agClientBrand_id");
                        String type = data.getString("type");
                        String url = data.getString("url");
                        if (type.equals("promo")) {
                            Log.d("lol", "ini data promo " + type);
                            Log.d("lol", "dataarraynya: " + id + title);
                            brand = data.getJSONObject("brand");
                            client = data.getJSONObject("client");
                            mData.add(new NewsModel(idString, "https://admin.authenticguards.com/storage/" + image + ".jpg",
                                    title, price, time, description, termCondition, agClientBrand_id, type,
                                    url, brand.toString(), client.toString()));
                        } else {
                            Log.d("lol", "ini data yang tidak ada promo" + type);
                            Log.d("lol", "dataarraynya: " + id + title);

                            mData.add(new NewsModel(idString, "https://admin.authenticguards.com/storage/" + image + ".jpg",
                                    title, price, time, description, termCondition, agClientBrand_id, type,
                                    url));
                        }
                        Log.d("lol", "mData : " + mData);
                        Log.d("lol", "dataArray: " + data);
                    }
                    Log.d("lol", "hasilresponse" + jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("lol", "Error: " + e);
                }
                mAdapter.notifyDataSetChanged();
                pDialog.setVisibility(View.GONE);
                mAdapter.updateEmptyView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }

    private void getDataStories(String token) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://admin.authenticguards.com/api/news_?token=" + token + "&appid=003&loclang=a&loclong=a", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("result");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        Log.d("lol", "ini data json" + data);
                        int id = data.getInt("id");
                        String idString = String.valueOf(id);
                        String image = data.getString("image");
                        String title = data.getString("title");
                        String article = data.getString("article");
                        String time = data.getString("time");
                        String url = data.getString("url");
                        mData.add(new NewsModel(idString, "https://admin.authenticguards.com/storage/" + image + ".jpg", title, article, time, url, "jvc"));
                        Log.d("lol", "mData : " + mData);
                        Log.d("lol", "dataArray: " + data);
                    }
                    Log.d("lol", "hasilresponse" + jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("lol", "Error: " + e);
                }
                mAdapter.notifyDataSetChanged();
                mAdapter.updateEmptyView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }

}
