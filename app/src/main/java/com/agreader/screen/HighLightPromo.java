package com.agreader.screen;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class HighLightPromo extends AppCompatActivity {

    ImageView askImage,imageView;

    String title = "",
            price = "",
            date = "",
            image = "",
            description = "",
            termReferences;

    TextView titleTextView, priceTextView, dateTextView, descriptionTextView, termTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_light_promo);

        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        date = getIntent().getStringExtra("time");
        image = getIntent().getStringExtra("image");
        description = getIntent().getStringExtra("description");
        termReferences = getIntent().getStringExtra("termCondition");

        titleTextView = (TextView) findViewById(R.id.title_promo);
        priceTextView = (TextView) findViewById(R.id.price_promo);
        dateTextView = (TextView) findViewById(R.id.available_promo);
        descriptionTextView = (TextView) findViewById(R.id.desc_promo);
        termTextView = (TextView) findViewById(R.id.term_promo);
        imageView = (ImageView) findViewById(R.id.image_promo);



        Picasso.get().load(image).into(imageView);

        titleTextView.setText(title);
        priceTextView.setText(price  + "Point");
        dateTextView.setText(date);
        descriptionTextView.setText(description);
        termTextView.setText(termReferences);


        askImage = (ImageView) findViewById(R.id.how_to_use);
        askImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(HighLightPromo.this);
                View mView = getLayoutInflater().inflate(R.layout.term_condition_modal,
                        null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });



    }
}