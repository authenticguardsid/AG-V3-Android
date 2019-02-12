package com.agreader.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.agreader.R;
import com.agreader.adapter.LeaderboardAdapter;
import com.agreader.adapter.ListStoreAdapter;
import com.agreader.model.ListStore;
import com.agreader.model.Rank;
import com.agreader.utils.CustomItemClickListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LeaderBoard extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private String token = "", token2 = "";
    private LeaderboardAdapter leaderboardAdapter;
    private ArrayList<Rank> modelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        recyclerView = (RecyclerView) findViewById(R.id.listLeader);
        modelArrayList = new ArrayList<>();

        modelArrayList.add(new Rank(R.drawable.goldmedal, "", "Satria Kurniawan", "10000"));
        modelArrayList.add(new Rank(R.drawable.silvermedal, "", "Rahmad Satria", "10000"));
        modelArrayList.add(new Rank(R.drawable.bronzemedal, "", "Yudhistira Caraka", "10000"));
        modelArrayList.add(new Rank(R.drawable.normalmedal, "4", "Taufiq Ramadhan", "10000"));

        leaderboardAdapter = new LeaderboardAdapter(this, modelArrayList);


        recyclerView.setLayoutManager(new LinearLayoutManager(LeaderBoard.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(leaderboardAdapter);
    }
}
