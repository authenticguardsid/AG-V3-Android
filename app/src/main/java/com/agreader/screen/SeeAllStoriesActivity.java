package com.agreader.screen;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.agreader.R;
import com.agreader.adapter.StoriesAdapter;
import com.agreader.adapter.hadiahAdapter;
import com.agreader.adapter.produkAdapter;
import com.agreader.model.Hadiah;
import com.agreader.model.Stories;
import com.agreader.utils.CustomItemClickListener;
import com.agreader.utils.DataRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeeAllStoriesActivity extends AppCompatActivity {

    private ArrayList<Stories> storiesList;
    private MaterialSearchView searchView;
    private String finalImage = "";
    private StoriesAdapter storiesAdapter;
    private RecyclerView recyclerView;

    private FirebaseUser firebaseUser;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_stories);


        storiesList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_stories);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        token = task.getResult().getToken();
                        Log.d("lol", "onCompleteBaru: " + token);
                        String result = "";
                        DataRequest.setUser(getApplicationContext(),token);
                    }
                });

        token = DataRequest.getResultToken(getApplicationContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager gridLayoutManager = new GridLayoutManager(SeeAllStoriesActivity.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        dataRecycler();

    }

    private void dataRecycler(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, "https://admin.authenticguards.com/api/news_?&appid=003", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("result");
                            JSONArray results = (JSONArray) jsonObject.get("data");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject data = results.getJSONObject(i);
                                String image = data.getString("image");
                                final String title = data.getString("title");
                                final String article = data.getString("article");
                                final String url = data.getString("url");

                                finalImage = "https://admin.authenticguards.com/storage/" + image + ".jpg";
                                storiesList.add(new Stories(title,article,finalImage,url));


                                /*LinearLayoutManager layoutManager = new LinearLayoutManager(SeeAllStoriesActivity.this, LinearLayoutManager.VERTICAL, true);
                                layoutManager.setStackFromEnd(true);*/


                                storiesAdapter = new StoriesAdapter(SeeAllStoriesActivity.this, storiesList, new CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        final Stories stories = storiesList.get(position);
                                        Intent detail_intent = new Intent(SeeAllStoriesActivity.this,DetailStoriesActivity.class);
                                        detail_intent.putExtra("gambar",stories.getImage());
                                        detail_intent.putExtra("judul",stories.getTitl());
                                        detail_intent.putExtra("article",stories.getShort_info());
                                        detail_intent.putExtra("url",stories.getUrl());
                                        startActivity(detail_intent);
                                    }
                                });
                                recyclerView.setAdapter(storiesAdapter);
                                storiesAdapter.notifyDataSetChanged();
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
        Volley.newRequestQueue(SeeAllStoriesActivity.this).add(jsonObjectRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
