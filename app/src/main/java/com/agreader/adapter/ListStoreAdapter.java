package com.agreader.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agreader.R;
import com.agreader.model.Hadiah;
import com.agreader.model.ListStore;
import com.agreader.model.NewsModel;
import com.agreader.model.Promo;
import com.agreader.utils.CustomItemClickListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class
ListStoreAdapter extends RecyclerView.Adapter<ListStoreAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<ListStore> dataList;
    private CustomItemClickListener listener;

    public ListStoreAdapter(Activity context, ArrayList<ListStore> dataList, CustomItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_authentic_store, parent, false);
        final ViewHolder mholder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,mholder.getPosition());
            }
        });
        return mholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.get().load(dataList.get(position).getImage()).into(holder.imageView);
        holder.txtBrands.setText(dataList.get(position).getBrand_name());
        holder.txtAddress.setText(dataList.get(position).getBrand_address());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtBrands, txtAddress;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.logo_brandStore);
            txtBrands = (TextView) itemView.findViewById(R.id.nama_brandStore);
            txtAddress = (TextView) itemView.findViewById(R.id.alamat_brandStore);
        }

    }


}
