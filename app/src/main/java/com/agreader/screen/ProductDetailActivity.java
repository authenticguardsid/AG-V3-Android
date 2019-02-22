package com.agreader.screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.QRCodeBaruActivity;
import com.agreader.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    CarouselView carouselView;
    String [] sampleImage;

    private Button button;

    String img="";
    String urlImage = "";
    List<String> imageUrls = new ArrayList<String>();

    private TextView size,color,material,price,distributor,city,expiredDate,reportText;
    String harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
//        changeStatusBarColor();
        img = getIntent().getStringExtra("image");

        urlImage = "http://admin.authenticguards.com/product/"+ img +".jpg";
        imageUrls.add(urlImage);

        carouselView = (CarouselView)findViewById(R.id.slider);
        carouselView.setPageCount(imageUrls.size());

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
                intent.putExtra("code", getIntent().getStringExtra("code"));
                startActivity(intent);
                finish();
            }
        });

        reportText = (TextView)findViewById(R.id.textView20);
        reportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this,ReportActivity.class));
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
            Picasso.get().load(imageUrls.get(position)).into(imageView);
        }
    };

}
