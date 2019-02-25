package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.siscaproject.sisca.Model.Label;
import com.siscaproject.sisca.R;

import java.util.ArrayList;
import java.util.List;

public class StatusLabelAdapter extends RecyclerView.Adapter<StatusLabelAdapter.ItemHolder> implements Filterable{
    private static final String TAG = "StatusLabelAdapter";

    private ArrayList<Label> listData;
    private ArrayList<Label> listDataFull;
    private Context activityContext;

    public StatusLabelAdapter(ArrayList<Label> listData, Context activityContext) {
        this.listData = listData;
        listDataFull = new ArrayList<>(listData);
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_status_label, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tvStatusLabel.setText(listData.get(position).getName());
        holder.tvStatusType.setText(listData.get(position).getType());
        holder.tvNotes.setText(listData.get(position).getNotes());
    }

    @Override
    public int getItemCount() {
        if(listData.isEmpty()) return 0;
        else return listData.size();
    }

    @Override
    public Filter getFilter() {
        return dataFilter;
    }

    private Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Label> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(listDataFull);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Label item : listDataFull){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listData.clear();
            listData.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ItemHolder extends RecyclerView.ViewHolder{
        private TextView tvStatusLabel;
        private TextView tvStatusType;
        private TextView tvNotes;

        public ItemHolder(View itemView) {
            super(itemView);

            tvStatusLabel = itemView.findViewById(R.id.tv_status_label);
            tvStatusType = itemView.findViewById(R.id.tv_status_type);
            tvNotes = itemView.findViewById(R.id.tv_notes);
        }
    }
}
