package com.agreader.screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
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

    TextView titleTextView, urlTextView, articleTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stories);

        imageView = (ImageView)findViewById(R.id.gambar_stories);
        titleTextView = (TextView)findViewById(R.id.judulStories);
        urlTextView = (TextView)findViewById(R.id.urlStories);
        articleTextView = (TextView)findViewById(R.id.detail_info);
        home = (ImageView)findViewById(R.id.close);

        Picasso.get().load(getIntent().getStringExtra("gambar")).fit().into(imageView);
        titleTextView.setText(getIntent().getStringExtra("judul"));
        articleTextView.setText(getIntent().getStringExtra("article"));
        urlTextView.setText(getIntent().getStringExtra("url"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            articleTextView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailStoriesActivity.this,MasterActivity.class));
            }
        });
    }

}
