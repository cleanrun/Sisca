package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.Activity.DetailMonitoringActivity;
import com.siscaproject.sisca.Activity.ReportMonitoringActivity;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.Config;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ItemHolder>{
    private Context activityContext;
    //List<LocationModel> locationList;
    private ArrayList<LocationAPI> listData = new ArrayList<>();
    private ExceptionHandler exceptionHandler;

    public LocationAdapter(Context context, ArrayList<LocationAPI> locationAPIList) {
        this.activityContext = context;
        this.listData = locationAPIList;
    }

    public void setExceptionHandler(ExceptionHandler handler){
        this.exceptionHandler = handler;
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
        holder.tvAsetSize.setText(listData.get(position).getAsset_count()+" aset");
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateNow = Config.getDateNow();
                if (listData.get(position).getUpdated_at().substring(5, 7).equals(dateNow.substring(5, 7))){
                    Intent intent = new Intent(activityContext, ReportMonitoringActivity.class);
                    intent.putExtra("ID_LOCATION_EXTRA", listData.get(position).getId());

                    activityContext.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(activityContext, DetailMonitoringActivity.class);
                    intent.putExtra("ID_LOCATION_EXTRA", listData.get(position).getId());

                    activityContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            if (listData.isEmpty()) {
                return 0;
            } else {
                return listData.size();
            }
        }catch(Exception e){
            Log.e("LocationAdapter", "getItemCount: " + e.getMessage() );
            exceptionHandler.onException();
        }
        return 0;

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

    public interface ExceptionHandler{
        void onException();
    }

}
