package com.agreader.screen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.adapter.hadiahAdapter;
import com.agreader.model.Hadiah;
import com.agreader.model.User;
import com.agreader.utils.DataRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

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
    LinearLayout emptyView,emptyViewLogin;
    RelativeLayout goLogin,goPoint;


    String token = "";
    String finalImage;
    String completee = "yes";
    List<String> imageUrls = new ArrayList<String>();

    private boolean kosong = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        hadiahs = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recycleViewPoint);
//        fotoProfile = (ImageView)findViewById(R.id.fotoPoint);
//        namaProfile = (TextView)findViewById(R.id.namaPoint);
        totalPoints = (TextView)findViewById(R.id.totalPoints);
        peringkat = (RelativeLayout)findViewById(R.id.peringkat);
        emptyView = (LinearLayout)findViewById(R.id.emptyView);
        emptyViewLogin = (LinearLayout) findViewById(R.id.empty_view_login);
        goLogin = (RelativeLayout) findViewById(R.id.go_login);
        goPoint = (RelativeLayout) findViewById(R.id.go_point);




        if(currentUser != null){
            goLogin.setVisibility(View.GONE);
            emptyViewLogin.setVisibility(View.GONE);
            currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    token = task.getResult().getToken();
                    DataRequest.setUser(getApplicationContext(),token);
                }
            });
            getDataPromo(token);
            loadData();
        }else {
            goLogin.setVisibility(View.VISIBLE);
            goPoint.setVisibility(View.GONE);
            goLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PointActivity.this,LoginScreenActivity.class);
                    startActivity(intent);
                }
            });
            peringkat.setVisibility(View.GONE);
            emptyViewLogin.setVisibility(View.VISIBLE);
        }


        peringkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PointActivity.this, LeaderBoard.class);
                startActivity(intent);
            }
        });

    }

    private void getDataPromo(String token){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, "https://admin.authenticguards.com/api/promo_?token=" + token + "&appid=003", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("result");
                        JSONArray results = (JSONArray) jsonObject.get("data");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject data = results.getJSONObject(i);
                            int id = data.getInt("id");
                            String idx = String.valueOf(id);
                            String image = data.getString("image");
                            final String title = data.getString("title");
                            final String price = data.getString("price");
                            final String time = data.getString("time");
                            final String desc = data.getString("description");
                            final String termC = data.getString("termCondition");
                            final String tanggal = time.substring(0,10);
                            final String harga = price.substring(6,9);
                            finalImage = "https://admin.authenticguards.com/storage/" + image + ".jpg";
                            hadiahs.add(new Hadiah(idx,finalImage,title,harga,"5",tanggal,desc,termC));
                            if(hadiahs.size() == 0 ){
                                emptyView.setVisibility(View.VISIBLE);
                            }
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setNestedScrollingEnabled(false);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(PointActivity.this, LinearLayoutManager.VERTICAL, true);
                            layoutManager.setStackFromEnd(true);
                            recyclerView.setLayoutManager(layoutManager);
                            adapter = new hadiahAdapter(PointActivity.this, hadiahs);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        completee = "no";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(PointActivity.this).add(jsonObjectRequest);
    }

    private void loadData(){
        final DatabaseReference dbs = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
        dbs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User us = dataSnapshot.getValue(User.class);

//                Picasso.get().load(us.getGambar()).into(fotoProfile);
//                namaProfile.setText(us.getName());
                String point = us.getTotalPoint();
                double parsepoint = Double.parseDouble(point);
                NumberFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(parsepoint);
                if(point != null)
                    totalPoints.setText(formattedNumber + " pts");
                else{
                    totalPoints.setText("0 pts");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
