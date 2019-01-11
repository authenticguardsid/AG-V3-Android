package com.agreader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.agreader.model.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.ViewHolder> {

    private ClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<ProductModel> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private View mEmptyView;
    private View mProgressbar;

    public MyProductAdapter(Context context, ArrayList<ProductModel> data, ArrayList<String> dataId,
                            View emptyView, View progresView, ClickHandler handler) {
        mContext = context;
        mData = data;
        mDataId = dataId;
        mEmptyView = emptyView;
        mProgressbar = progresView;
        mClickHandler = handler;
        mSelectedId = new ArrayList<>();
        setHasStableIds(true);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.list_item_myproduct, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel pet = mData.get(position);
        Picasso.get().load(pet.getImageProduct()).into(holder.mImg);
        holder.mName.setText(pet.getNameProduct());
        holder.mDate.setText(pet.getDateProduct());
        holder.mBrand.setText(pet.getMerchant());
        holder.mPoint.setText(pet.getPoint());
        holder.itemView.setSelected(mSelectedId.contains(mDataId.get(position)));
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

        boolean onItemLongClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener {
        final ImageView mImg;
        final TextView mName;
        final TextView mDate;
        final TextView mBrand;
        final TextView mPoint;

        ViewHolder(View itemView) {
            super(itemView);

            mImg = itemView.findViewById(R.id.image_product);
            mName = itemView.findViewById(R.id.product_name);
            mDate = itemView.findViewById(R.id.date_claim);
            mBrand = itemView.findViewById(R.id.product_brand);
            mPoint = itemView.findViewById(R.id.reward_claim);

            itemView.setFocusable(true);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            mClickHandler.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return mClickHandler.onItemLongClick(getAdapterPosition());
        }
    }

}
