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
import com.siscaproject.sisca.Model.Supplier;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ItemHolder>{
    private static final String TAG = "SupplierAdapter";

    private ArrayList<Supplier> listData;
    private Context activityContext;

    public SupplierAdapter(ArrayList<Supplier> listData, Context activityContext) {
        this.listData = listData;
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_supplier, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tv_name.setText(listData.get(position).getName());
        holder.tv_address.setText(listData.get(position).getAddress());
        holder.tv_email.setText(listData.get(position).getEmail());
        holder.tv_phone.setText(listData.get(position).getPhone());

        holder.btn_view_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activityContext, "View Detail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listData.isEmpty()) return 0;
        else return listData.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private CircularImageView iv_photo;
        private TextView tv_name;
        private TextView tv_address;
        private TextView tv_email;
        private TextView tv_phone;
        private Button btn_view_detail;

        public ItemHolder(View itemView) {
            super(itemView);

            iv_photo = itemView.findViewById(R.id.iv_photo);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_email = itemView.findViewById(R.id.tv_email);
            btn_view_detail = itemView.findViewById(R.id.btn_view_detail);
        }
    }

}