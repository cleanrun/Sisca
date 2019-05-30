package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

public class PindahPicAdapter extends RecyclerView.Adapter<PindahPicAdapter.ItemHolder>{

    private ArrayList<String> listData;
    private Context activityContext;

    private OnItemClickListener listener;

    public PindahPicAdapter(ArrayList<String> listData, Context activityContext, OnItemClickListener listener) {
        this.listData = listData;
        this.activityContext = activityContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_dialog_pic, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        holder.tvName.setText(listData.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.putPic(listData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listData.isEmpty()){
            return 0;
        }
        else{
            return listData.size();
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private LinearLayout parentLayout;
        private TextView tvName;
        private CircularImageView ivPhoto;

        public ItemHolder(View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parent_layout);
            tvName = itemView.findViewById(R.id.tv_name);
            //ivPhoto = itemView.findViewById(R.id.iv_photo);
        }
    }

    public interface OnItemClickListener{
        void putPic(String pic);
    }
}
