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

import com.agreader.R;
import com.agreader.model.Brand;
import com.agreader.model.Hadiah;
import com.agreader.model.NewsModel;
import com.agreader.screen.DetailPointActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    private BrandAdapter.ClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<Brand> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private View mEmptyView;
    private View mProgressbar;

    public BrandAdapter(Context context, ArrayList<Brand> data, ArrayList<String> dataId,
                         BrandAdapter.ClickHandler handler) {
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
    public BrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.list_item_brand, parent, false);
        return new BrandAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandAdapter.ViewHolder holder, int position) {
        Brand pet = mData.get(position);

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

        ViewHolder(View itemView) {
            super(itemView);

            mImg = itemView.findViewById(R.id.logo_brand);

            itemView.setFocusable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            mClickHandler.onItemClick(getAdapterPosition());
        }

    }
}