package com.agreader.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.agreader.model.ListStore;
import com.agreader.model.Rank;
import com.agreader.utils.CustomItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<Rank> dataList;

    public LeaderboardAdapter(Activity context, ArrayList<Rank> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_rank, parent, false);
        final ViewHolder mholder = new ViewHolder(view);
        return mholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.get().load(dataList.get(position).getImage()).into(holder.imageView);
        holder.txtNumber.setText(dataList.get(position).getRank());
        holder.txtName.setText(dataList.get(position).getName());
        holder.txtPoint.setText(dataList.get(position).getPoint());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNumber, txtName, txtPoint;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageRank);
            txtNumber = (TextView) itemView.findViewById(R.id.numberRank);
            txtName = (TextView) itemView.findViewById(R.id.nameRank);
            txtPoint = (TextView) itemView.findViewById(R.id.pointRank);
        }

    }

}
