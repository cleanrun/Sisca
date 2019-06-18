package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.R;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ItemHolder>{
    private Context activityContext;
    //List<LocationModel> locationList;
    private ArrayList<LocationAPI> listData = new ArrayList<>();

    public LocationAdapter(Context context, ArrayList<LocationAPI> locationAPIList) {
        this.activityContext = context;
        this.listData = locationAPIList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_location, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        holder.tvId.setText(listData.get(position).getName());
        holder.tvAsetSize.setText("0 aset");
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(activityContext, MonitoringDetailActivity.class);
                intent.putExtra("ID_LOCATION_EXTRA", locationList.get(position).getId());

                activityContext.startActivity(intent);*/
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

        //return 0;
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