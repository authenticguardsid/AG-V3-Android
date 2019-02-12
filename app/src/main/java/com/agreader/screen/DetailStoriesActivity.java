package com.agreader.screen;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class DetailStoriesActivity extends AppCompatActivity {

    ImageView back, home, imageView;

    String title = "",
            url = "",
            date = "",
            image = "",
            article = "";

    TextView titleTextView, urlTextView, dateTextView, articleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stories);


    }

}
