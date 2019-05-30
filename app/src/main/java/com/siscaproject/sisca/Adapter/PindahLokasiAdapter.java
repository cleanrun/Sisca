package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.siscaproject.sisca.R;

import java.util.ArrayList;

public class PindahLokasiAdapter extends RecyclerView.Adapter<PindahLokasiAdapter.ItemHolder>{

    private ArrayList<String> listData;
    private Context activityContext;

    private OnItemClickListener listener;

    public PindahLokasiAdapter(ArrayList<String> listData, Context activityContext, OnItemClickListener listener) {
        this.listData = listData;
        this.activityContext = activityContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_dialog_lokasi, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        holder.tvLokasi.setText(listData.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.putLokasi(listData.get(position));
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
        private TextView tvLokasi;
        private LinearLayout parentLayout;

        public ItemHolder(View itemView) {
            super(itemView);

            tvLokasi = itemView.findViewById(R.id.tv_lokasi);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public interface OnItemClickListener{
        void putLokasi(String lokasi);
    }
}
