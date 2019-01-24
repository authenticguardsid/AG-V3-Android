package com.agreader.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.squareup.picasso.Picasso;

public class DetailPointActivity extends AppCompatActivity {


    private String idHadiah,gambar,judul,totalPoint,expired,desc,termC;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_point);

        Intent intentGetData = getIntent();
        gambar = intentGetData.getStringExtra("gambar");
        judul = intentGetData.getStringExtra("title");
        totalPoint = intentGetData.getStringExtra("price");
        expired = intentGetData.getStringExtra("availablePoint");
        desc = intentGetData.getStringExtra("descriptionPoint");
        termC = intentGetData.getStringExtra("termC");

        TextView title_point = (TextView)findViewById(R.id.title_point);
        ImageView gambar_point = (ImageView)findViewById(R.id.image_point);
        TextView price_point = (TextView)findViewById(R.id.price_point);
        TextView expired_point = (TextView)findViewById(R.id.available_point);
        TextView desc_point = (TextView)findViewById(R.id.desc_point);
        TextView termC_point = (TextView)findViewById(R.id.term_point);

        title_point.setText(judul);
        Picasso.get().load(gambar).fit().into(gambar_point);
        price_point.setText(totalPoint + " point");
        expired_point.setText(expired);
        desc_point.setText(desc);
        termC_point.setText(termC);

        back = (ImageView) findViewById(R.id.backPressPoint);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MasterActivity.class);
                startActivity(intent);
            }
        });


    }
}
