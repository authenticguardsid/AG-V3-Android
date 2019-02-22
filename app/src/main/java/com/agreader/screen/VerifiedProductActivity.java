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
import android.widget.TextView;

import com.agreader.R;

public class VerifiedProductActivity extends AppCompatActivity {

    private Button button;
    public String PARTNER = "";
    public String GCODE = "";
    String token;

    private static TextView result_code, result_brand, result_company, result_address, result_phone, result_email, result_web,report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_product);

//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
//        changeStatusBarColor();

        result_code = (TextView) findViewById(R.id.textView21);
        result_brand = (TextView) findViewById(R.id.textView23);
        result_company = (TextView) findViewById(R.id.textView25);
        result_address = (TextView) findViewById(R.id.textView27);
        result_phone = (TextView) findViewById(R.id.textView31);
        result_email = (TextView) findViewById(R.id.textView32);
        result_web = (TextView) findViewById(R.id.textView33);

        result_code.setText(getIntent().getStringExtra("key"));
        result_brand.setText(getIntent().getStringExtra("brand"));
        result_company.setText(getIntent().getStringExtra("company"));
        result_address.setText(getIntent().getStringExtra("address"));
        result_phone.setText(getIntent().getStringExtra("phone"));
        result_email.setText(getIntent().getStringExtra("email"));
        result_web.setText(getIntent().getStringExtra("web"));

        button = (Button) findViewById(R.id.product_detail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifiedProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("size",getIntent().getStringExtra("size"));
                intent.putExtra("color",getIntent().getStringExtra("color"));
                intent.putExtra("material",getIntent().getStringExtra("material"));
                intent.putExtra("price",getIntent().getStringExtra("price"));
                intent.putExtra("distributor",getIntent().getStringExtra("distributor"));
                intent.putExtra("expiredDate",getIntent().getStringExtra("expiredDate"));
                intent.putExtra("image",getIntent().getStringExtra("image"));
                intent.putExtra("code", getIntent().getStringExtra("code"));
                startActivity(intent);
                finish();
            }
        });

        report = (TextView)findViewById(R.id.textView37);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerifiedProductActivity.this,ReportActivity.class));
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

}
