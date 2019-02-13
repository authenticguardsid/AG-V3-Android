package com.agreader.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.agreader.adapter.BrandAdapter;
import com.agreader.adapter.NewsAdapter;
import com.agreader.adapter.PromoAdapter;
import com.agreader.adapter.hadiahAdapter;
import com.agreader.model.Brand;
import com.agreader.model.Hadiah;
import com.agreader.model.NewsModel;
import com.agreader.model.Promo;
import com.agreader.screen.AuthenticeStoreActivity;
import com.agreader.screen.DetailHighlightScreen;
import com.agreader.screen.DetailPointActivity;
import com.agreader.screen.DetailStoriesActivity;
import com.agreader.screen.EditProfileActivity;
import com.agreader.screen.FeaturedDetailActivity;
import com.agreader.screen.HighLightPromo;
import com.agreader.screen.HighLightScreen;
import com.agreader.screen.ListAuthenticStoreActivity;
import com.agreader.screen.PointActivity;
import com.agreader.screen.SeeAllBrand;
import com.agreader.screen.SeeAllStoriesActivity;
import com.agreader.utils.DataRequest;
import com.agreader.utils.LinePagerIndicatorDecoration;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */



/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View rootView;
    Button mButtonAuthenticStore, mButtonMoreInfoStories, mButtonGoProfile, mButtonHighlight;
    TextView mButtonSeeAllStories, mButtonSeeAllPromo ,mButtonSeeAllBrand;
    FirebaseUser firebaseUser;
    String token;
    String finalImage;
    List<String> imageUrls = new ArrayList<String>();
    CardView aa;
    FirebaseUser currentUser;
    private ArrayList<String> mDataId;
    private ArrayList<String> mDataIdPromo;
    private BrandAdapter mAdapter;
    private PromoAdapter mAdapterPromo;
    private ActionMode mActionMode;
    private ActionMode mActionPromo;
    RecyclerView recyclerView,recylerPromo;
    private ArrayList<Brand> mData = new ArrayList<>();
    private ArrayList<Promo> mDataPromo = new ArrayList<>();
    String  JSON;


    CarouselView carouselView;
    int[] sampleImages = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4};
    private ImageView gambar1, gambar2, gambar3, gambar4, gambar5, gambar6;

    public HomeFragment() {
        // Required empty public constructor
    }

    ViewListener viewListener = new ViewListener() {
        //
        @Override
        public View setViewForPosition(int position) {
            View customView = getActivity().getLayoutInflater().inflate(R.layout.view_custom, null);
            ImageView myImageView = customView.findViewById(R.id.myImage);
            Log.d("lol", "arraylist: " + imageUrls);
            Picasso.get().load(imageUrls.get(position)).into(myImageView);
            return customView;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layoutarraylist: for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //fragment home see all AG Stories

        aa = rootView.findViewById(R.id.cardProfile);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        token = task.getResult().getToken();
                        Log.d("lol", "onCompleteBaru: " + token);
                        String result = "";
                        DataRequest.setUser(getContext(),token);
                    }
                });

        carouselView = rootView.findViewById(R.id.slider);
//        getDataSlider(token);
        mButtonGoProfile = rootView.findViewById(R.id.goProfile);
        mButtonGoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        mButtonHighlight = (Button) rootView.findViewById(R.id.more_info_highlight);
        mButtonHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HighLightScreen.class);
                intent.putExtra("hasil", "0");
                startActivity(intent);
            }
        });

        mButtonSeeAllPromo = (TextView) rootView.findViewById(R.id.seeAllPromo);
        mButtonSeeAllPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PointActivity.class);
                startActivity(intent);
            }
        });

        mButtonSeeAllStories = rootView.findViewById(R.id.seeAllStorirs);
        mButtonSeeAllStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SeeAllStoriesActivity.class);
                intent.putExtra("hasil", "1");
                startActivity(intent);
            }
        });

        //more info AG Stories
        mButtonMoreInfoStories = rootView.findViewById(R.id.more_info_ag_stories);
        mButtonMoreInfoStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailStoriesActivity.class);
                startActivity(intent);
            }
        });

        //home_section_1



        //home_section_7
        mButtonAuthenticStore = rootView.findViewById(R.id.more_info_authentic_store);
        mButtonAuthenticStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListAuthenticStoreActivity.class);
                startActivity(intent);
            }
        });

        mButtonSeeAllBrand = rootView.findViewById(R.id.seeAllBrand);
        mButtonSeeAllBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SeeAllBrand.class);
                startActivity(intent);
            }
        });
        //homoesection4
        recylerPromo = (RecyclerView) rootView.findViewById(R.id.listPromo);
        recylerPromo.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recylerPromo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstItemVisible = linearLayoutManager.findFirstVisibleItemPosition();
                if (firstItemVisible != 0 && firstItemVisible % mDataPromo.size() == 0) {
                    recyclerView.getLayoutManager().scrollToPosition(0);
                }
            }
        });
        recylerPromo.setLayoutManager(linearLayoutManager);

        recylerPromo.setBackgroundColor(Color.parseColor("#ffffff"));
        PagerSnapHelper snapHelper = new PagerSnapHelper();

        snapHelper.attachToRecyclerView(recylerPromo);
//        recylerPromo.addItemDecoration(new LinePagerIndicatorDecoration());
//        autoScroll();
        getPromo(token);


        //home_section_8
        recyclerView = (RecyclerView) rootView.findViewById(R.id.list_brand);
        recyclerView.setHasFixedSize(false);


        getBrand(JSON);
        mAdapter = new BrandAdapter(getContext(), mData, mDataId,
                new BrandAdapter.ClickHandler() {
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
                        Brand pet = mData.get(position);
                        Intent intent = new Intent(getApplicationContext(), FeaturedDetailActivity.class);
                        intent.putExtra("id", pet.getId());
                        intent.putExtra("name", pet.getName());
                        intent.putExtra("image", pet.getImage());
                        startActivity(intent);
                    }
                });

        mAdapterPromo = new PromoAdapter(getContext(), mDataPromo, mDataId,
                new PromoAdapter.ClickHandler() {
                    @Override
                    public void onItemClick(int position) {
                        if (mActionMode != null) {
                            mAdapterPromo.toggleSelection(mDataId.get(position));
                            if (mAdapterPromo.selectionCount() == 0)
                                mActionMode.finish();
                            else
                                mActionMode.invalidate();
                            return;
                        }
                        Promo hadiah = mDataPromo.get(position);
                        Intent intentDetailPoint = new Intent(getContext(), DetailPointActivity.class);
                        intentDetailPoint.putExtra("postId", hadiah.getIdHadiah());
                        intentDetailPoint.putExtra("title", hadiah.getJudul());
                        intentDetailPoint.putExtra("gambar", hadiah.getGambar());
                        intentDetailPoint.putExtra("price", hadiah.getTotalPoint());
                        intentDetailPoint.putExtra("availablePoint", hadiah.getExpired());
                        intentDetailPoint.putExtra("descriptionPoint", hadiah.getDesc());
                        intentDetailPoint.putExtra("termC", hadiah.getTermC());
                        startActivity(intentDetailPoint);
                    }
                });
        recyclerView.setAdapter(mAdapter);
        recylerPromo.setAdapter(mAdapterPromo);
//        autoScroll();
        return rootView;
    }

    private void getDataSlider(String token){
        Log.d("1", "getDataSlider: ");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/slider_?token=" + token + "&appid=003&loclang=a&loclong=a", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("result");
                    JSONArray results = (JSONArray) jsonObject.get("data");
                    imageUrls = new ArrayList<>();
                    Log.d("lol", "result0" + results);
                    Log.d("lol", "result1" + imageUrls);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject data = results.getJSONObject(i);
                        String image = data.getString("image");
                        finalImage = "http://admin.authenticguards.com/storage/app/public/" + image + ".jpg";
                        imageUrls.add(finalImage);
                    }
                    carouselView.setViewListener(viewListener);
                    carouselView.setPageCount(imageUrls.size());

                    Log.d("lol", "result3 " + imageUrls);
                } catch (JSONException e) {

                    e.printStackTrace();
                    Log.d("lol", "Error: " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);

        final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
        dbf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("completeProfile").exists()){
                    String complete = dataSnapshot.child("completeProfile").getValue().toString();
                    if (complete.equals("true")){
                        aa.setVisibility(View.GONE);
                    }else {
                        aa.setVisibility(View.VISIBLE);
                    }
                }else {
                    aa.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataSlider(token);
        carouselView.setViewListener(viewListener);
        carouselView.setPageCount(imageUrls.size());
    }
     private void getBrand(String tes){
         String url = "http://admin.authenticguards.com/api/feature?appid=003";
         StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     JSONObject jsonObject = new JSONObject(response);
                     JSONObject json= jsonObject.getJSONObject("result");
                     JSONArray jsonArray = json.getJSONArray("data");
                     Log.d("twtw", "onResponse: " + jsonArray);
                     for (int i = 0; i < 5 ; i++) {
                         JSONObject data = jsonArray.getJSONObject(i);
                         Log.d("lol", "ini data json" + data);
                         int id = data.getInt("id");
                         String idString = String.valueOf(id);
                         String image = data.getString("image");
                         String name = data.getString("Name");
                         Log.d("twtw", "onResponse: " +image);
                         JSONObject brand = data.getJSONObject("client");
                         Log.d("tolil", "onResponse: " + brand.getString("name"));
//                         String client = brand.getString("name");
                         mData.add(new Brand(idString,name,"http://admin.authenticguards.com/storage/app/public/"+image+".jpg" ));
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

    private void getPromo(String token){
        String url = "http://admin.authenticguards.com/api/promo_?token="+ token +"&appid=003";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject json= jsonObject.getJSONObject("result");
                    JSONArray jsonArray = json.getJSONArray("data");
                    Log.d("twtw", "onResponse: " + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        int id = data.getInt("id");
                        String idx = String.valueOf(id);
                        String image = data.getString("image");
                        final String title = data.getString("title");
                        final String price = data.getString("price");
                        final String time = data.getString("time");
                        final String desc = data.getString("description");
                        final String termC = data.getString("termCondition");
                        final String tanggal = time.substring(0, 10);
                        final String harga = price.substring(6, 9);
                        finalImage = "http://admin.authenticguards.com/storage/app/public/" + image + ".jpg";
                        mDataPromo.add(new Promo(idx, finalImage, title, harga, "5", tanggal, desc, termC));
                    }
                    mAdapterPromo.notifyDataSetChanged();
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
    public void autoScroll() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                recylerPromo.smoothScrollToPosition(++count);
                if (count == mData.size() - 1) {
                    count = 0;
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}