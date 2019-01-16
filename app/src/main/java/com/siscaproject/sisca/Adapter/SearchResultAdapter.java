package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.siscaproject.sisca.Model.Category;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ItemHolder>{
    private static final String TAG = "SearchResultAdapter";

    private ArrayList<Category> listData;
    private Context activityContext;

    public SearchResultAdapter(Context activityContext, ArrayList<Category> listData) {
        this.listData = listData;
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_search, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tvName.setText(listData.get(position).getName());
        holder.tvID.setText(listData.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private LinearLayout lytParent;
        private TextView tvID;
        private TextView tvName;

        public ItemHolder(View itemView) {
            super(itemView);
            lytParent = itemView.findViewById(R.id.lyt_parent_list_search);
            tvID = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }


}
