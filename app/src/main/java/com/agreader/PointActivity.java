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


        /*for (int i = 0; i < 3 ; i++) {
            final HashMap<String, Object> hadiah = new HashMap<>();
            if (i == 0){
                hadiah.put("gambar","https://firebasestorage.googleapis.com/v0/b/ag-version-3.appspot.com/o/hadiah%2F1.jpg?alt=media&token=1d3b030a-0595-4589-97fc-22e645ac66f8");
                hadiah.put("judul","Voucher Discount 50%");
                hadiah.put("totalPoint","25000");
                hadiah.put("tersisa","5");
            }else if (i == 1){
                hadiah.put("gambar","https://firebasestorage.googleapis.com/v0/b/ag-version-3.appspot.com/o/hadiah%2F2.jpg?alt=media&token=f4d5465e-a363-40b9-aa42-eb0312407fa6");
                hadiah.put("judul","Pulsa Telkomsel Rp50.000");
                hadiah.put("totalPoint","25000");
                hadiah.put("tersisa","5");
            }else if (i == 2){
                hadiah.put("gambar","https://firebasestorage.googleapis.com/v0/b/ag-version-3.appspot.com/o/hadiah%2F3.jpg?alt=media&token=c367b633-e090-4bc5-8c15-e5df2539597c");
                hadiah.put("judul","Voucher GO-JEK Rp25.000");
                hadiah.put("totalPoint","25000");
                hadiah.put("tersisa","5");
            }
            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String key = dbf.push().getKey();
                    hadiah.put("idHadiah",key);
                    dbf.child(key).setValue(hadiah);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }*/

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

                Picasso.get().load(us.getGambar())/*.transform(new RoundedCornersTransformation(50,10))*/.fit().centerInside().into(fotoProfile);
                namaProfile.setText(us.getName());
                totalPoints.setText(us.getTotalPoint() + " pts");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
