package com.agreader.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.agreader.R;
import com.agreader.model.Stories;
import com.agreader.utils.CustomItemClickListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<Stories> dataList;
    private CustomItemClickListener listener;

    public StoriesAdapter(Activity context, ArrayList<Stories> dataList, CustomItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public StoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_ag_stories, parent, false);
        final ViewHolder mholder=new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,mholder.getPosition());
            }
        });
        return mholder;
    }

    @Override
    public void onBindViewHolder(final StoriesAdapter.ViewHolder holder, int position) {
        holder.txt_title.setText(dataList.get(position).getTitl());
        holder.txt_info.setText(dataList.get(position).getShort_info());
        Picasso.get().load(dataList.get(position).getImage()).fit().into(holder.imageView);
        holder.read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, holder.getPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_title, txt_info;
        private RoundedImageView imageView;
        private Button read_more;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.title_list_ag_stories);
            txt_info = (TextView) itemView.findViewById(R.id.short_information_list_ag_stories);
            read_more = (Button) itemView.findViewById(R.id.read_more_ag_stories);
            imageView = (RoundedImageView)itemView.findViewById(R.id.image_stories);
        }
    }

}
