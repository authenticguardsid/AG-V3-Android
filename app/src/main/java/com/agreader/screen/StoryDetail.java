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

public class StoryDetail extends AppCompatActivity {

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
        setContentView(R.layout.activity_story_detail);

        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        date = getIntent().getStringExtra("time");
        image = getIntent().getStringExtra("image");
        Log.e("tea", "onCreate: " + image);
        article = getIntent().getStringExtra("article");

        titleTextView = (TextView) findViewById(R.id.titleText);
        urlTextView = (TextView) findViewById(R.id.url);
        dateTextView = (TextView) findViewById(R.id.dateTexe);
        articleTextView = (TextView) findViewById(R.id.detail);
        imageView = (ImageView) findViewById(R.id.image_story);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.TRANSPARENT)
                .borderWidthDp(0)
                .cornerRadiusDp(10)
                .oval(false)
                .build();


        Picasso.get().load(image).fit()
                .transform(transformation).into(imageView);

        titleTextView.setText(title);
        urlTextView.setText(url);
        dateTextView.setText(date);
        articleTextView.setText(article);


        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HighLightScreen.class);
                intent.putExtra("hasil", "1");
                startActivity(intent);
            }
        });

        home = (ImageView) findViewById(R.id.toHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MasterActivity.class);
                startActivity(intent);
            }
        });

    }
}
