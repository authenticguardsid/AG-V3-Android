package com.agreader.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.agreader.R;

public class DetailStoriesActivity extends AppCompatActivity {

    private ImageView mCloseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stories);

        mCloseImage = (ImageView) findViewById(R.id.close);
        mCloseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
