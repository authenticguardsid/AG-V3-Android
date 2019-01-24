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
import com.agreader.model.NewsModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private NewsAdapter.ClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<NewsModel> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private View mEmptyView;
    private View mProgressbar;

    public NewsAdapter(Context context, ArrayList<NewsModel> data, ArrayList<String> dataId,
                       View emptyView, View progresView, NewsAdapter.ClickHandler handler) {
        mContext = context;
        mData = data;
        mDataId = dataId;
        mEmptyView = emptyView;
        mProgressbar = progresView;
        mClickHandler = handler;
        mSelectedId = new ArrayList<>();
//        setHasStableIds(true);
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
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.list_item_news, parent, false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        NewsModel pet = mData.get(position);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.TRANSPARENT)
                .borderWidthDp(0)
                .cornerRadiusDp(10)
                .oval(false)
                .build();


        Picasso.get().load(pet.getImage()).fit()
                .transform(transformation).into(holder.mImg);
        holder.title_news.setText(pet.getTitle());
        holder.date_news.setText(pet.getUrl());
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
        final RoundedImageView mImg;
        final TextView title_news;
        final TextView date_news;

        ViewHolder(View itemView) {
            super(itemView);

            mImg = itemView.findViewById(R.id.image_news);
            title_news = itemView.findViewById(R.id.title_news);
            date_news = itemView.findViewById(R.id.date_news);

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
