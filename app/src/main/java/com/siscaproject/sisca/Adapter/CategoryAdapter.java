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

import com.siscaproject.sisca.Model.Category;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemHolder>{
    private static final String TAG = "CategoryAdapter";

    private ArrayList<Category> listData;
    private Context activityContext;

    private OnButtonClickListener listener;

    public CategoryAdapter(ArrayList<Category> listData, Context activityContext, OnButtonClickListener listener) {
        this.listData = listData;
        this.activityContext = activityContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_category, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        holder.tv_name.setText(listData.get(position).getName());
        holder.tv_type.setText(listData.get(position).getType());
        //holder.tv_assets.setText(listData.get(position).getAssets());

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showEditDialog(listData.get(position));
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showDeleteDialog(Integer.valueOf(listData.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listData.isEmpty()) return 0;
        else return listData.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder{
        private ImageView iv_category;
        private TextView tv_name;
        private TextView tv_type;
        private TextView tv_assets;
        private ImageButton btn_edit;
        private ImageButton btn_delete;

        public ItemHolder(View itemView) {
            super(itemView);

            iv_category = itemView.findViewById(R.id.iv_category);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_assets = itemView.findViewById(R.id.tv_assets);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnButtonClickListener{
        void showDeleteDialog(final int id);
        void showEditDialog(Category category);
    }
}
