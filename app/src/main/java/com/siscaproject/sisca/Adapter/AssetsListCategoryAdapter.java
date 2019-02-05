package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

public class AssetsListCategoryAdapter extends RecyclerView.Adapter<AssetsListCategoryAdapter.ItemHolder>
        implements View.OnClickListener{
    private static final String TAG = "AssetsListCategoryAdapter";

    private ArrayList<Asset> listData;
    private Context activityContext;

    public AssetsListCategoryAdapter(ArrayList<Asset> listData, Context activityContext) {
        this.listData = listData;
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_assets_list_category, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        // holder.iv_asset.setImageDrawable(); On progress

        holder.tv_name.setText(listData.get(position).getName());
        holder.tv_manufacturer.setText(listData.get(position).getAsset_id());
        holder.tv_quantity.setText(listData.get(position).getPurchase_cost());
        //holder.tv_status.setText(listData.get(position).getModel_no()); // Still on Progress

        holder.btn_delete.setOnClickListener(this);
        holder.btn_edit.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_edit:
                // On progress
                Toast.makeText(activityContext, "Edit Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_delete:
                // On progress
                Toast.makeText(activityContext, "Delete Button", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private ImageView iv_asset;

        private TextView tv_name;
        private TextView tv_manufacturer;
        private TextView tv_quantity;
        private TextView tv_status;

        private ImageButton btn_edit;
        private ImageButton btn_delete;

        public ItemHolder(View itemView) {
            super(itemView);

            iv_asset = itemView.findViewById(R.id.iv_asset);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_manufacturer = itemView.findViewById(R.id.tv_manufacturer);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_status = itemView.findViewById(R.id.tv_status);

            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
