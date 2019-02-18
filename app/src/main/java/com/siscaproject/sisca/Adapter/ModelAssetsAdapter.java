package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.siscaproject.sisca.Model.Model;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

public class ModelAssetsAdapter extends RecyclerView.Adapter<ModelAssetsAdapter.ItemHolder>{
    private static final String TAG = "ModelAssetsAdapter";

    private ArrayList<Model> listData;
    private Context activityContext;

    private OnButtonClickListener listener;

    public ModelAssetsAdapter(ArrayList<Model> listData, Context activityContext, OnButtonClickListener listener) {
        this.listData = listData;
        this.activityContext = activityContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_model_assets, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        holder.tvName.setText(listData.get(position).getName());
        holder.tvManufacturer.setText(listData.get(position).getManufacturer_id());
        holder.tvModelNo.setText(listData.get(position).getModel_number());

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(activityContext, "View Detail", Toast.LENGTH_SHORT).show();
                listener.showDetails(listData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listData.isEmpty()) return 0;
        else return listData.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private CircularImageView ivPhoto;
        private TextView tvName;
        private TextView tvManufacturer;
        private TextView tvModelNo;
        private Button btnDetail;

        public ItemHolder(View itemView) {
            super(itemView);

            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvName = itemView.findViewById(R.id.tv_name);
            tvManufacturer = itemView.findViewById(R.id.tv_manufacturer);
            tvModelNo = itemView.findViewById(R.id.tv_model_no);
            btnDetail = itemView.findViewById(R.id.btn_view_detail);
        }
    }

    public interface OnButtonClickListener{
        void showDetails(Model model);
    }
}
