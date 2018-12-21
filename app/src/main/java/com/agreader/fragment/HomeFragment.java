package com.agreader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.agreader.R;
import com.agreader.adapter.brandAdapter;
import com.agreader.utils.ViewPagerAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View rootView;

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4};

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        carouselView = rootView.findViewById(R.id.slider);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        GridView gridview = rootView.findViewById(R.id.gridview);
        gridview.setAdapter(new brandAdapter(getContext()));

        return rootView;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

}
