package com.agreader.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.agreader.screen.AuthenticeStoreActivity;
import com.agreader.screen.DetailStoriesActivity;
import com.agreader.screen.EditProfileActivity;
import com.agreader.screen.FeaturedDetailActivity;
import com.agreader.screen.HighLightScreen;
import com.agreader.screen.PointActivity;
import com.agreader.screen.SeeAllStoriesActivity;
import com.agreader.utils.DataRequest;
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

/**
 * A simple {@link Fragment} subclass.
 */



/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View rootView;
    Button mButtonAuthenticStore, mButtonMoreInfoStories, mButtonGoProfile, mButtonHighlight;
    TextView mButtonSeeAllStories, mButtonSeeAllPromo;
    FirebaseUser firebaseUser;
    String token;
    String finalImage;
    List<String> imageUrls = new ArrayList<String>();
    CardView aa;
    FirebaseUser currentUser;

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
        getDataSlider(token);
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

        mButtonSeeAllStories = rootView.findViewById(R.id.more_info_ag_stories);
        mButtonSeeAllStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SeeAllStoriesActivity.class);
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
                Intent intent = new Intent(getContext(), AuthenticeStoreActivity.class);
                startActivity(intent);
            }
        });

        //home_section_8
        final Intent intent = new Intent(getContext(), FeaturedDetailActivity.class);
        gambar1 = rootView.findViewById(R.id.merz_photo);
        gambar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("name", "Merz");
                intent.putExtra("rating", "Rating : 8 / 10");
                intent.putExtra("desc", "Merz Aesthetic merupakan produk kecantikan yang merupakan perawatan dengan memanfaatkan teknologi ultrasound non-invasif yang meningkatkan sistem regeneratif tubuh untuk merangsang pertumbuhan kolagen dengan lembut dan alami, dan telah membawa perubahan besar dalam dunia kecantikan di seluruh dunia. ");
                intent.putExtra("image", R.drawable.test7);
                startActivity(intent);
            }
        });
        gambar2 = rootView.findViewById(R.id.deenay_photo);
        gambar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("name", "Deenay");
                intent.putExtra("rating", "Rating : 8 / 10");
                intent.putExtra("desc", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
                intent.putExtra("image", R.drawable.test12);
                startActivity(intent);
            }
        });
        gambar3 = rootView.findViewById(R.id.doa_photo);
        gambar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("name", "Doa");
                intent.putExtra("rating", "Rating : 8 / 10");
                intent.putExtra("desc", "DOA merupakan karya terbaru dari Dewi Sandra sebagai brand Fashion Muslim di Indonesia. Melalui DOA, Dewi Sandra ingin menciptakan sebuah karya yang bukan hanya mengutamakan sisi keindahan di mata sesama manusia melainkan juga dimata sang Pencipta Allah SWT.");
                intent.putExtra("image", R.drawable.test6);
                startActivity(intent);
            }
        });
        gambar4 = rootView.findViewById(R.id.sandbox_photo);
        gambar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("name", "Sandbox");
                intent.putExtra("rating", "Rating : 8 / 10");
                intent.putExtra("desc", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
                intent.putExtra("image", R.drawable.test17);
                startActivity(intent);
            }
        });
        gambar5 = rootView.findViewById(R.id.sabichi_photo);
        gambar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("name", "Sabichi");
                intent.putExtra("rating", "Rating : 8 / 10");
                intent.putExtra("desc", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
                intent.putExtra("image", R.drawable.test8);
                startActivity(intent);
            }
        });
        gambar6 = rootView.findViewById(R.id.moruka_photo);
        gambar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("name", "Moruka");
                intent.putExtra("rating", "Rating : 8 / 10");
                intent.putExtra("desc", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
                intent.putExtra("image", R.drawable.test11);
                startActivity(intent);
            }
        });

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


}