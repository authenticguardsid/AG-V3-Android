package com.agreader.tabhost;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.agreader.R;
import com.codesgood.views.JustifiedTextView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class TokoFragment extends Fragment {

    private JustifiedTextView detailInfoText;

    CarouselView carouselView;
    int[] sampleImages = {
            R.drawable.noimage,
            R.drawable.noimage,
            R.drawable.noimage
    };

    public TokoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_toko, container, false);
        detailInfoText = view.findViewById(R.id.detail_info);
        detailInfoText.setText(getActivity().getIntent().getStringExtra("desc"));

        carouselView = view.findViewById(R.id.slider);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

}
