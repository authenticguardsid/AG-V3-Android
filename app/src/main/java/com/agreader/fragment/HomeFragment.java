package com.agreader.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.agreader.R;
import com.agreader.adapter.BrandAdapter;
import com.agreader.adapter.NewsAdapter;
import com.agreader.adapter.PromoAdapter;
import com.agreader.adapter.hadiahAdapter;
import com.agreader.base.BaseFragment;
import com.agreader.model.Brand;
import com.agreader.model.Hadiah;
import com.agreader.model.NewsModel;
import com.agreader.model.Promo;
import com.agreader.model.User;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    View rootView;

    Button mButtonAuthenticStore,
           mButtonMoreInfoStories,
           mButtonGoProfile,
           mButtonHighlight;

    TextView mButtonSeeAllStories,
             mButtonSeeAllPromo,
             mButtonSeeAllBrand;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser firebaseUser;

    String token,
           finalImage;

    private static final int ERROR_DIALOG_REQUEST = 9001;

    List<String> imageUrls = new ArrayList<String>();

    CardView completeProfileCard;

    private ArrayList<String> mDataId,
                              mDataIdPromo;

    private BrandAdapter mAdapter;
    private PromoAdapter mAdapterPromo;

    private ActionMode mActionMode;
    private ActionMode mActionPromo;

    RecyclerView recyclerView, recylerPromo;

    private ArrayList<Brand> mData = new ArrayList<>();
    private ArrayList<Promo> mDataPromo = new ArrayList<>();

    ProgressBar mProgressBarPromo,
                mProgressBarBrand;

    ShimmerFrameLayout mShimmerViewContainer;

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4};

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
        setUpView(rootView);
        generateView(rootView);
        return rootView;
    }

    private void completeProfileLayout(){
        final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());
        dbf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("completeProfile").exists()) {
                    String complete = dataSnapshot.child("completeProfile").getValue().toString();
                    if (complete.equals("true")) {
                        completeProfileCard.setVisibility(View.GONE);
                    } else {
                        completeProfileCard.setVisibility(View.VISIBLE);
                    }
                } else {
                    completeProfileCard.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataSlider() {
        Log.d("1", "getDataSlider: ");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://admin.authenticguards.com/api/slider_?&appid=003&loclang=a&loclong=a", null, new Response.Listener<JSONObject>() {
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
                        finalImage = "https://admin.authenticguards.com/storage/" + image + ".jpg";
                        imageUrls.add(finalImage);
                    }
                    carouselView.setViewListener(viewListener);
                    carouselView.setPageCount(imageUrls.size());
                    mShimmerViewContainer.stopShimmerAnimation();

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
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

    }

    @Override
    public void onResume() {

        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();

        getBrand();
        getPromo(token);
        getDataSlider();

        carouselView.setViewListener(viewListener);
        carouselView.setPageCount(imageUrls.size());
    }

    private void getBrand() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, " https://admin.authenticguards.com/api/feature?appid=003", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        JSONObject json = response.getJSONObject("result");
                        JSONArray jsonArray = json.getJSONArray("data");
                        for (int i = 0; i < 5; i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            Log.d("lol", "ini data json" + data);
                            int id = data.getInt("id");
                            String idString = String.valueOf(id);
                            String image = data.getString("image");
                            String name = data.getString("Name");
                            JSONObject brand = data.getJSONObject("client");
                            mData.add(new Brand(idString, name, "https://admin.authenticguards.com/storage/" + image + ".jpg"));
                        }
                        mAdapter.notifyDataSetChanged();
                        mProgressBarBrand.setVisibility(View.GONE);
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
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }


    private void getPromo(String token) {
        String url = "https://admin.authenticguards.com/api/promo_?token=" + token + "&appid=003";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        JSONObject json = response.getJSONObject("result");
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
                            finalImage = "https://admin.authenticguards.com/storage/" + image + ".jpg";
                            mDataPromo.add(new Promo(idx, finalImage, title, harga, "5", tanggal, desc, termC));
                        }
                        mAdapterPromo.notifyDataSetChanged();
                        mProgressBarBrand.setVisibility(ProgressBar.INVISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }



    @Override
    public void setUpView(View view) {

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = mFirebaseAuth.getCurrentUser();

        completeProfileCard = view.findViewById(R.id.cardProfile);

        mProgressBarPromo = (ProgressBar) view.findViewById(R.id.progressPromo);
        mProgressBarBrand = (ProgressBar) view.findViewById(R.id.progressBrand);

        mShimmerViewContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
        carouselView = view.findViewById(R.id.slider);

        mButtonGoProfile = rootView.findViewById(R.id.goProfile);
        mButtonHighlight = (Button) rootView.findViewById(R.id.more_info_highlight);
        mButtonSeeAllPromo = (TextView) rootView.findViewById(R.id.seeAllPromo);
        mButtonMoreInfoStories = rootView.findViewById(R.id.more_info_ag_stories);
        mButtonAuthenticStore = rootView.findViewById(R.id.more_info_authentic_store);
        mButtonSeeAllBrand = rootView.findViewById(R.id.seeAllBrand);

        recylerPromo = (RecyclerView) rootView.findViewById(R.id.listPromo);
        recylerPromo.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.list_brand);
        recyclerView.setHasFixedSize(false);

    }

    @Override
    public void generateView(View view) {

        if(firebaseUser != null ) {
            firebaseUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            token = task.getResult().getToken();
                            Log.d("lol", "onCompleteBaru: " + token);
                            String result = "";
                            DataRequest.setUser(getApplicationContext(), token);
                        }
                    });
            completeProfileLayout();
        }
        mButtonGoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        mButtonHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HighLightScreen.class);
                //for HighlightScreen
                intent.putExtra("hasil", "0");
                startActivity(intent);
            }
        });

        mButtonSeeAllPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PointActivity.class);
                startActivity(intent);
            }
        });


        //more info AG Stories
        mButtonMoreInfoStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HighLightScreen.class);
                intent.putExtra("hasil", "1");
                startActivity(intent);
            }
        });

        mButtonAuthenticStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListAuthenticStoreActivity.class);
                startActivity(intent);
            }
        });

        mButtonSeeAllBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SeeAllBrand.class);
                startActivity(intent);
            }
        });

        mAdapter = new BrandAdapter(getApplicationContext(), mData, mDataId,
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

        mAdapterPromo = new PromoAdapter(getApplicationContext(), mDataPromo, mDataId);
        recyclerView.setAdapter(mAdapter);
        recylerPromo.setAdapter(mAdapterPromo);

    }

    @Override
    public void setupListener(View view) {

    }
}