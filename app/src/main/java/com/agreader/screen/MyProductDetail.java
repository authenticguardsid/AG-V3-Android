package com.agreader.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProductDetail extends AppCompatActivity {

    private CarouselView carouselView;

    ArrayList<String> imageUrls = new ArrayList<String>();

    private TextView txt_size, txt_color, txt_material, txt_price, txt_distributor, txt_expiredDate, txt_namaBrand, txt_alamatBrand, txt_namaProduk;
    private CircleImageView logo_brand;
    private String harga;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_detail);
        txt_size = (TextView) findViewById(R.id.sizeDetaill);
        txt_color = (TextView) findViewById(R.id.colorDetaill);
        txt_material = (TextView) findViewById(R.id.materialDetaill);
        txt_price = (TextView) findViewById(R.id.priceDetaill);
        txt_distributor = (TextView) findViewById(R.id.distributorDetaill);
        txt_expiredDate = (TextView) findViewById(R.id.expiredDateDetaill);
        txt_namaBrand = (TextView) findViewById(R.id.namaBrandd);
        txt_alamatBrand = (TextView) findViewById(R.id.alamatBrandd);
        logo_brand = (CircleImageView) findViewById(R.id.logoBrandd);
        txt_namaProduk = (TextView) findViewById(R.id.namaProductt);
        carouselView = (CarouselView) findViewById(R.id.gambarDetailProduct);


        txt_namaProduk.setText(getIntent().getStringExtra("namaProduk"));
        txt_size.setText(getIntent().getStringExtra("size"));
        txt_color.setText(getIntent().getStringExtra("color"));
        txt_material.setText(getIntent().getStringExtra("material"));
        txt_material.setText(getIntent().getStringExtra("material"));
        harga = getIntent().getStringExtra("price");
        back = (ImageView) findViewById(R.id.backPress);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProductDetail.this, MasterActivity.class);
                startActivity(intent);
            }
        });


        if (harga != "") {
            double total = Double.parseDouble(harga.substring(3));
            DecimalFormat df = new DecimalFormat("#.##");
            String hargaFormat = df.format(total);

            txt_price.setText("Rp." + hargaFormat);
        } else {
            txt_price.setText("");
        }

        txt_distributor.setText(getIntent().getStringExtra("distributor"));
        txt_expiredDate.setText(getIntent().getStringExtra("expiredDate"));

        Picasso.get().load(getIntent().getStringExtra("logo_brand")).fit().into(logo_brand);
        txt_namaBrand.setText(getIntent().getStringExtra("nama_brand"));
        txt_alamatBrand.setText(getIntent().getStringExtra("alamat_brand"));

        imageUrls.add(getIntent().getStringExtra("image"));
        carouselView.setPageCount(imageUrls.size());
        carouselView.setViewListener(viewListener);

    }

    ViewListener viewListener = new ViewListener() {
        //
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.custom_product, null);
            ImageView myImageView = customView.findViewById(R.id.myImageProduct);
            Log.d("lol", "arraylist: " + imageUrls);
            Picasso.get().load(imageUrls.get(position)).into(myImageView);
            return customView;
        }
    };


}
