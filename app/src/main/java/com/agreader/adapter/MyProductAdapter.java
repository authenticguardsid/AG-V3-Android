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
import com.agreader.utils.CustomItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProductModel> mData;
    private ArrayList<ProductModel> listData;
    private CustomItemClickListener listener;

    public MyProductAdapter(Context context, ArrayList<ProductModel> mData, ArrayList<ProductModel>listData ,CustomItemClickListener listener) {
        this.context = context;
        this.mData = mData;
        this.listener = listener;
        this.listData = listData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_myproduct, parent, false);
        final MyProductAdapter.ViewHolder mholder = new MyProductAdapter.ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,mholder.getPosition());
            }
        });
        return mholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_product.setText(mData.get(position).getNameProduct());
        holder.txt_brand.setText(mData.get(position).getBrand());
        holder.txt_date.setText(mData.get(position).getDateProduct());
        if (mData.get(position).getStatus().equals("on_review")){
            holder.status.setImageResource(R.drawable.ic_time);
            holder.txt_status.setText("Pending");
        }else {
            holder.status.setImageResource(R.drawable.crop__ic_done);
            holder.txt_status.setText("Disetujui");
        }
        Picasso.get().load(mData.get(position).getImageProduct()).fit().into(holder.gambarProduct);
    }

    @Override
    public int getItemCount() {
        return (mData != null) ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_product, txt_brand,txt_date,txt_status;
        private ImageView status,gambarProduct;
        //private Button read_more;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_product= (TextView) itemView.findViewById(R.id.namaProduct);
            txt_brand= (TextView) itemView.findViewById(R.id.namaBrandProduct);
            txt_date= (TextView) itemView.findViewById(R.id.tanggalClaimProduct);
            status = (ImageView)itemView.findViewById(R.id.status);
            txt_status = (TextView)itemView.findViewById(R.id.status_text);
            gambarProduct = (ImageView)itemView.findViewById(R.id.gambarProduct);

        }
    }

}
