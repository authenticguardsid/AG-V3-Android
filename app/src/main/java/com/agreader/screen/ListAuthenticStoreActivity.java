package com.agreader.screen;

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

import com.agreader.R;
import com.agreader.adapter.ListStoreAdapter;
import com.agreader.model.ListStore;
import com.agreader.utils.CustomItemClickListener;
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
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListAuthenticStoreActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private String token="", token2="";
    private ListStoreAdapter listStoreAdapter;
    private ArrayList<ListStore> modelArrayList;

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.noimage, R.drawable.noimage, R.drawable.noimage, R.drawable.noimage};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_authentic_store);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_authentic_store);
        modelArrayList = new ArrayList<>();

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        firebaseUser.getIdToken(true)
//                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<GetTokenResult> task) {
//                        token = task.getResult().getToken();
//                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/getuser?token=" + token + "&appid=003", null, new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                token2 = token;
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                            }
//                        });
//                        Volley.newRequestQueue(ListAuthenticStoreActivity.this).add(jsonObjectRequest);
//                        Log.e("token-firebase", "" + token2);
//                    }
//                });

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.net/api/locator_?token=a&appid=001", null, new Response.Listener<JSONObject>() {
////            @Override
////            public void onResponse(JSONObject response) {
////                if (response.length() > 0) {
////                    for (int i = 0; i < response.length(); i++) {
////                        try {
////                            JSONArray jsonArray = response.getJSONArray("result");
////                            for (int j = 0; j < jsonArray.length() ; j++) {
////                                JSONObject jsonObject = jsonArray.getJSONObject(j);
////                                String brand_name = jsonObject.getString("Name");
////                                String address = jsonObject.getString("addressOfficeOrStore");
////                                Log.d("asdasd", "brand_name : " + brand_name + "address : " + address);
////                            }
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////
////            }
////        });
////        Volley.newRequestQueue(this).add(jsonObjectRequest);

        carouselView = (CarouselView) findViewById(R.id.slider);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        modelArrayList.add(new ListStore(R.drawable.test8, "SABICHI","Jl. Pasirluyu VII No. 7 Bandung 40254", -6.6867070, 107.02222));
        modelArrayList.add(new ListStore(R.drawable.test6, "Doa Indonesia","Taman Holis Indah Blok C5 50-51", -6.943330, 107.613670));
        modelArrayList.add(new ListStore(R.drawable.test12, "DEENAY","Jl. Kembar Timur No.39,Cigereleng,Regol,Kota", -6.943320, 107.613904));
        modelArrayList.add(new ListStore(R.drawable.test7, "MERZ", " Gandaria 8 Office Tower, 11th Floor, Unit, Jl. Sultan Iskandar Muda, RT.10/RW.6, Pd. Pinang, Kby. Lama, Kota Jakarta Selatan, Daerah Khusus Ibukota ",1.169968, 104.003014));


        listStoreAdapter = new ListStoreAdapter(this, modelArrayList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(ListAuthenticStoreActivity.this, AuthenticeStoreActivity.class);
                if (position == 0){
                    intent.putExtra("brand_name","SABICHI");
                }else if (position == 1){
                    intent.putExtra("brand_name","Doa Indonesia");
                }else if (position == 2){
                    intent.putExtra("brand_name","DEENAY");
                }else if (position == 3){
                    intent.putExtra("brand_name","MERZ");
                }
                startActivity(intent);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ListAuthenticStoreActivity.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(ListAuthenticStoreActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(listStoreAdapter);
        listStoreAdapter.notifyDataSetChanged();

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

}
