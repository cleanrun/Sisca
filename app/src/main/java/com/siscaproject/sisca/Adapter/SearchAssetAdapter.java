package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.siscaproject.sisca.Activity.DetailAssetActivity;
import com.siscaproject.sisca.Model.AssetAPI;
import com.siscaproject.sisca.R;

import java.util.List;

public class SearchAssetAdapter extends RecyclerView.Adapter<SearchAssetAdapter.ItemHolder>{

    private Context activityContext;
    private List<AssetAPI> listData;

    public SearchAssetAdapter(Context context, List<AssetAPI> assetList) {
        this.activityContext = context;
        this.listData = assetList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_asset, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        //holder.ivPhoto.setImageDrawable(activityContext.getResources().getDrawable(listData.get(position).getPhoto()));
        holder.tvName.setText(listData.get(position).getName());
        holder.tvId.setText(listData.get(position).getId()+"");
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activityContext, DetailAssetActivity.class);
                intent.putExtra("ID_ASSET_EXTRA", listData.get(position).getId());

                activityContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto;
        private TextView tvName;
        private TextView tvId;
        private CardView cvItem;

        public ItemHolder(View itemView) {
            super(itemView);

            ivPhoto = itemView.findViewById(R.id.iv_photo_item_asset);
            tvName = itemView.findViewById(R.id.tv_name_item_asset);
            tvId = itemView.findViewById(R.id.tv_id_asset);
            cvItem = itemView.findViewById(R.id.cv_item_asset);
        }
    }
}
