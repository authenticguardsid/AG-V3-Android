package com.agreader.screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.test13, R.drawable.test15, R.drawable.test16};

    private Button button;

    private TextView size,color,material,price,distributor,city,expiredDate;
    String harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
//        changeStatusBarColor();

        carouselView = (CarouselView)findViewById(R.id.slider);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        size = (TextView)findViewById(R.id.sizeDetail);
        color = (TextView)findViewById(R.id.colorDetail);
        material = (TextView)findViewById(R.id.materialDetail);
        price = (TextView)findViewById(R.id.priceDetail);
        distributor = (TextView)findViewById(R.id.distributorDetail);

        expiredDate = (TextView)findViewById(R.id.expiredDateDetail);

        size.setText(getIntent().getStringExtra("size"));
        color.setText(getIntent().getStringExtra("color"));
        material.setText(getIntent().getStringExtra("material"));
        harga = getIntent().getStringExtra("price");

        double total = Double.parseDouble(harga.substring(3));
        DecimalFormat df = new DecimalFormat("#.##");
        String hargaFormat = df.format(total);

        price.setText("Rp"+hargaFormat);
        distributor.setText(getIntent().getStringExtra("distributor"));
        expiredDate.setText("-");

        button = (Button) findViewById(R.id.claim_product);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, ClaimProductActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

}
