package com.agreader;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.adapter.brandAdapter;
import com.agreader.utils.SectionPagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MasterActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem prevMenuItem;
    private TextView pointt;
    private Intent ada;

    String total = "";

    private BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }*/
        setContentView(R.layout.activity_master);
        pointt = (TextView)findViewById(R.id.point);


        ada = getIntent();
        total = getIntent().getStringExtra("tambahPoint");

        loadData();

        pointt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MasterActivity.this,PointActivity.class));
            }
        });

        //changeStatusBarColor();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_view);
        bottomNavigationViewEx.setIconSize(20, 20);
        bottomNavigationViewEx.setTextSize(12);
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.setTextVisibility(true);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setIconTintList(0, getResources().getColorStateList(R.color.colorGold));
        bottomNavigationViewEx.setTextTintList(0, getResources().getColorStateList(R.color.colorGold));

        SectionPagesAdapter adapter = new SectionPagesAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        viewPager.setCurrentItem(0);
                        bottomNavigationViewEx.setIconTintList(0, getResources().getColorStateList(R.color.colorGold));
                        bottomNavigationViewEx.setTextTintList(0, getResources().getColorStateList(R.color.colorGold));
                        bottomNavigationViewEx.setIconTintList(1, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(1, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(2, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(2, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(3, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(3, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(4, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(4, getResources().getColorStateList(R.color.colorGREY));
                        //getSupportActionBar().setTitle("Home");
                        break;
                    case R.id.ic_product:
                        viewPager.setCurrentItem(1);
                        bottomNavigationViewEx.setIconTintList(0, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(0, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(1, getResources().getColorStateList(R.color.colorGold));
                        bottomNavigationViewEx.setTextTintList(1, getResources().getColorStateList(R.color.colorGold));
                        bottomNavigationViewEx.setIconTintList(2, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(2, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(3, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(3, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(4, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(4, getResources().getColorStateList(R.color.colorGREY));
                        //getSupportActionBar().setTitle("QR Code");
                        break;
                    case R.id.ic_qrcode:
                        viewPager.setCurrentItem(2);
                        bottomNavigationViewEx.setIconTintList(0, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(0, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(1, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(1, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(2, getResources().getColorStateList(R.color.colorGold));
                        bottomNavigationViewEx.setTextTintList(2, getResources().getColorStateList(R.color.colorGold));
                        bottomNavigationViewEx.setIconTintList(3, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(3, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(4, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(4, getResources().getColorStateList(R.color.colorGREY));
                        //getSupportActionBar().setTitle("QR Code");
                        break;
                    case R.id.ic_notification:
                        viewPager.setCurrentItem(3);
                        bottomNavigationViewEx.setIconTintList(0, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(0, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(1, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(1, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(2, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(2, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(3, getResources().getColorStateList(R.color.colorGold));
                        bottomNavigationViewEx.setTextTintList(3, getResources().getColorStateList(R.color.colorGold));
                        bottomNavigationViewEx.setIconTintList(4, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(4, getResources().getColorStateList(R.color.colorGREY));
                        //getSupportActionBar().setTitle("QR Code");
                        break;
                    case R.id.ic_profile:
                        viewPager.setCurrentItem(4);
                        bottomNavigationViewEx.setIconTintList(0, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(0, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(1, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(1, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(2, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(2, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(3, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setTextTintList(3, getResources().getColorStateList(R.color.colorGREY));
                        bottomNavigationViewEx.setIconTintList(4, getResources().getColorStateList(R.color.colorGold));
                        bottomNavigationViewEx.setTextTintList(4, getResources().getColorStateList(R.color.colorGold));
                        //getSupportActionBar().setTitle("Profile");
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationViewEx.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationViewEx.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationViewEx.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void loadData(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
        dbf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User us = dataSnapshot.getValue(User.class);
                if (ada.getStringExtra("tambahPoint") != null){
                    int point = Integer.parseInt(us.getTotalPoint());
                    int tambahPoint = point + 100;
                    String points = String.valueOf(tambahPoint);
                    Toast.makeText(MasterActivity.this, "Selamat Point Anda berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    dbf.child("totalPoint").setValue(points);
                    pointt.setText(points + " pts");
                }else {
                    pointt.setText(us.getTotalPoint() + " pts");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
