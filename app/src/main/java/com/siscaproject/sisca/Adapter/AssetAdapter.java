package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.siscaproject.sisca.Model.AssetMutasi;
import com.siscaproject.sisca.R;

import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ItemHolder>{

    private Context activityContext;
    private List<AssetMutasi> listData;
    private cardClickListener listener;

    public AssetAdapter(Context context, List<AssetMutasi> assetList, cardClickListener listener) {
        this.activityContext = context;
        this.listData = assetList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_asset, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder assetHolder, final int position) {
        //assetHolder.ivPhoto.setImageDrawable(activityContext.getResources().getDrawable(listData.get(position).getPhoto()));
        assetHolder.tvName.setText(listData.get(position).getName());
        assetHolder.tvId.setText(listData.get(position).getAsset_id());
        assetHolder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(activityContext, DetailMutationActivity.class);
                intent.putExtra("ID_ASET_EXTRA", listData.get(position).getId());
                intent.putExtra("NAME_ASET_EXTRA", listData.get(position).getName());
                intent.putExtra("PHOTO_ASET_EXTRA", listData.get(position).getPhoto());
                intent.putExtra("DESC_ASET_EXTRA", listData.get(position).getDesc());
                intent.putExtra("ID_LOC_ASET_EXTRA", listData.get(position).getIdLocation());

                activityContext.startActivity(intent);
                */

                listener.onCardClick(listData.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listData.size() == 0){
           return 0;
        }
        else{
            return listData.size();
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public ImageView ivPhoto;
        public TextView tvName;
        public TextView tvId;
        public CardView cvItem;

        public ItemHolder(View itemView) {
            super(itemView);

            ivPhoto = itemView.findViewById(R.id.iv_photo_item_asset);
            tvName = itemView.findViewById(R.id.tv_name_item_asset);
            tvId = itemView.findViewById(R.id.tv_id_asset);
            cvItem = itemView.findViewById(R.id.cv_item_asset);
        }
    }

    public interface cardClickListener{
        void onCardClick(final int id);
    }
}
