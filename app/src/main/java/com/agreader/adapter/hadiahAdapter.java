package com.agreader.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.model.Hadiah;
import com.agreader.model.User;
import com.agreader.screen.DetailPointActivity;
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
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_hadiah, parent, false);
        return new hadiahAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final Hadiah hadiah = mData.get(position);
        if (!hadiah.getGambar().isEmpty()) {
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
                Intent intentDetailPoint = new Intent(mContext, DetailPointActivity.class);
                intentDetailPoint.putExtra("postId", hadiah.getIdHadiah());
                intentDetailPoint.putExtra("title",hadiah.getJudul());
                intentDetailPoint.putExtra("gambar",hadiah.getGambar());
                intentDetailPoint.putExtra("price",hadiah.getTotalPoint());
                intentDetailPoint.putExtra("availablePoint",hadiah.getExpired());
                intentDetailPoint.putExtra("descriptionPoint",hadiah.getDesc());
                intentDetailPoint.putExtra("termC",hadiah.getTermC());
                mContext.startActivity(intentDetailPoint);
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

    private int selectionCount() {
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