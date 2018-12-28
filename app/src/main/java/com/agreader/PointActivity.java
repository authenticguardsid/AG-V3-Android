package com.agreader;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.adapter.hadiahAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class PointActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView fotoProfile;
    private TextView namaProfile;
    private TextView totalPoints;
    private RelativeLayout peringkat;

    private FirebaseUser currentUser;
    private hadiahAdapter adapter;
    private ArrayList<Hadiah> hadiahs;
    private DatabaseReference dbf;



    private boolean kosong = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = (RecyclerView)findViewById(R.id.recycleViewPoint);
        fotoProfile = (ImageView)findViewById(R.id.fotoPoint);
        namaProfile = (TextView)findViewById(R.id.namaPoint);
        totalPoints = (TextView)findViewById(R.id.totalPoints);
        peringkat = (RelativeLayout)findViewById(R.id.peringkat);

        dbf = FirebaseDatabase.getInstance().getReference("hadiah");
        loadData();


        dbf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hadiahs = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Hadiah hd = ds.getValue(Hadiah.class);
                    hadiahs.add(hd);
                }
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                LinearLayoutManager layoutManager = new LinearLayoutManager(PointActivity.this, LinearLayoutManager.VERTICAL, true);
                layoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(layoutManager);

                adapter = new hadiahAdapter(PointActivity.this,hadiahs);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        peringkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PointActivity.this, "Dalam Tahap pengembangan", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadData(){
        final DatabaseReference dbs = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
        dbs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User us = dataSnapshot.getValue(User.class);

                Picasso.get().load(us.getGambar()).into(fotoProfile);
                namaProfile.setText(us.getName());
                if(us.getTotalPoint() != null)
                    totalPoints.setText(us.getTotalPoint() + " pts");
                else{
                    totalPoints.setText("0 pts");
                }
                String point = us.getTotalPoint();
                double parsepoint = Double.parseDouble(point);
                NumberFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(parsepoint);
                totalPoints.setText(formattedNumber + " pts");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
