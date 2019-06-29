package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siscaproject.sisca.Activity.DetailLocationActivity;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.R;

import java.util.List;

public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.ItemHolder>{

    private Context activityContext;
    //List<LocationModel> locationList;
    private List<LocationAPI> listData;

    public SearchLocationAdapter(Context context, List<LocationAPI> locationList) {
        this.activityContext = context;
        this.listData = locationList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_location, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        holder.tvId.setText(listData.get(position).getName());
        holder.tvAsetSize.setText(listData.get(position).getAsset_count()+" aset");
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activityContext, DetailLocationActivity.class);
                intent.putExtra("ID_LOCATION_EXTRA", listData.get(position).getId());

                activityContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public TextView tvId;
        public TextView tvAsetSize;
        public CardView cvItem;

        public ItemHolder(View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id_item_location);
            tvAsetSize = itemView.findViewById(R.id.tv_jumlah_aset_location);
            cvItem = itemView.findViewById(R.id.cv_item_location);
        }
    }
}
