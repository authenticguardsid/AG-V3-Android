package com.agreader.screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class DetailHighlightScreen extends AppCompatActivity {

    ImageView back, imageView;
    String title = "",
            url = "",
            date = "",
            image = "",
            description = "";

    TextView titleTextView, urlTextView, dateTextView, descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_highlight_screen);

        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        date = getIntent().getStringExtra("time");
        image = getIntent().getStringExtra("image");
        description = getIntent().getStringExtra("description");

        titleTextView = (TextView) findViewById(R.id.titleText);
        urlTextView = (TextView) findViewById(R.id.url);
        dateTextView = (TextView) findViewById(R.id.dateTexe);
        descriptionTextView = (TextView) findViewById(R.id.detail);
        imageView = (ImageView) findViewById(R.id.image_news);

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
        descriptionTextView.setText(description);



        back = (ImageView) findViewById(R.id.backPress);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MasterActivity.class);
                startActivity(intent);
            }
        });
    }
}