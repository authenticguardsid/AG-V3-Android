package com.agreader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agreader.R;
import com.agreader.model.AllbrandModel;
import com.agreader.model.Brand;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class AllBrandAdapter extends RecyclerView.Adapter<AllBrandAdapter.ViewHolder> {
    private AllBrandAdapter.ClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<AllbrandModel> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private View mEmptyView;
    private View mProgressbar;

    public AllBrandAdapter(Context context, ArrayList<AllbrandModel> data, ArrayList<String> dataId,
                        AllBrandAdapter.ClickHandler handler) {
        mContext = context;
        mData = data;
        mDataId = dataId;
        mClickHandler = handler;
        mSelectedId = new ArrayList<>();
    }

    public void updateEmptyView() {
        if (mData.size() == 0)
            mEmptyView.setVisibility(View.VISIBLE);
        else
            mEmptyView.setVisibility(View.GONE);
    }

    public void progressView() {
        if (mData.size() == 0)
            mProgressbar.setVisibility(View.VISIBLE);
        else
            mProgressbar.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public AllBrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.list_item_allbrand, parent, false);
        return new AllBrandAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllBrandAdapter.ViewHolder holder, int position) {
        AllbrandModel pet = mData.get(position);
        holder.mName.setText(pet.getName());
        holder.mAddress.setText(pet.getAddress());
        holder.mClient.setText(pet.getClient());
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.TRANSPARENT)
                .borderWidthDp(0)
                .cornerRadiusDp(10)
                .oval(false)
                .build();


        Picasso.get().load(pet.getImage()).fit()
                .transform(transformation).into(holder.mImg);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void toggleSelection(String dataId) {
        if (mSelectedId.contains(dataId))
            mSelectedId.remove(dataId);
        else
            mSelectedId.add(dataId);
        notifyDataSetChanged();
    }

    public int selectionCount() {
        return mSelectedId.size();
    }

    public void resetSelection() {
        mSelectedId = new ArrayList<>();
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedId() {
        return mSelectedId;
    }

    public interface ClickHandler {
        void onItemClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
        final RoundedImageView mImg;
        final TextView mName;
        final TextView mAddress;
        final TextView mClient;


        ViewHolder(View itemView) {
            super(itemView);

            mImg = itemView.findViewById(R.id.logo_brand);
            mName = itemView.findViewById(R.id.name);
            mAddress = itemView.findViewById(R.id.address);
            mClient = itemView.findViewById(R.id.client);

            itemView.setFocusable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            mClickHandler.onItemClick(getAdapterPosition());
        }

    }
}