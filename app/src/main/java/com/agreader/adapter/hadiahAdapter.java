package com.agreader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.model.Hadiah;
import com.agreader.R;
import com.agreader.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Yudhistira Caraka on 12/21/2018.
 */

public class hadiahAdapter extends RecyclerView.Adapter<hadiahAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Hadiah> mData;
    private final ArrayList<Integer> mSelected;

    public hadiahAdapter(Context mContext, ArrayList<Hadiah> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mSelected = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_hadiah,parent,false);
        return new hadiahAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final Hadiah hadiah = mData.get(position);
        if (!hadiah.getGambar().isEmpty()){
            Picasso.get().load(hadiah.getGambar()).fit().centerInside().into(holder.gambarHadiah);
        }

        final int points = Integer.parseInt(hadiah.getTotalPoint());

        holder.judulHadiah.setText(hadiah.getJudul());
        holder.jmlahPoint.setText(hadiah.getTotalPoint() + " pts");
        holder.tersisa.setText(hadiah.getTersisa());

        final int jumlahTersisa = Integer.parseInt(hadiah.getTersisa());

        holder.tukarPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
                dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User us = dataSnapshot.getValue(User.class);
                        int pts = Integer.parseInt(us.getTotalPoint());
                        if (pts < points){
                            Toast.makeText(mContext, "Point Anda tidak mencukupi", Toast.LENGTH_SHORT).show();
                        }else {
                            int total = pts - points;
                            String jmlh = String.valueOf(total);
                            dbf.child("totalPoint").setValue(jmlh);

                            final DatabaseReference dbs = FirebaseDatabase.getInstance().getReference("hadiah").child(hadiah.getIdHadiah());
                            dbs.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int jumlah = jumlahTersisa - 1;
                                    String jmlhh = String.valueOf(jumlah);
                                    dbs.child("tersisa").setValue(jmlhh);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(mContext, "Hadiah anda berhasil ditukarkan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public ArrayList<Integer> getmSelected() {
        return mSelected;
    }

    private int selectionCount(){
        return mSelected.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tukarPoint;
        final TextView tersisa;
        final TextView judulHadiah;
        final TextView jmlahPoint;
        final ImageView gambarHadiah;

        public ViewHolder(View itemView) {
            super(itemView);
            tukarPoint = itemView.findViewById(R.id.tukarPoint);
            tersisa = itemView.findViewById(R.id.jmlhTersisa);
            gambarHadiah = itemView.findViewById(R.id.gambarHadiah);
            judulHadiah = itemView.findViewById(R.id.judulHadiah);
            jmlahPoint = itemView.findViewById(R.id.jmlhPoint);
        }

        @Override
        public void onClick(View v) {
            if (selectionCount() > 0 && !mSelected.contains(getAdapterPosition()))
                itemView.setBackgroundColor(Color.LTGRAY);
            else itemView.setBackgroundColor(android.R.attr.selectableItemBackground);
        }
    }


}