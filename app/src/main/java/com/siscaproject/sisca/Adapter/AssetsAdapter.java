package com.siscaproject.sisca.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.Activity.AssetsActivity;
import com.siscaproject.sisca.Activity.BluetoothActivity;
import com.siscaproject.sisca.ActivityForm.FormNewAssetActivity;
import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.Model.ResponseDelete;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.ItemHolder>{
    private static final String TAG = "AssetsAdapter";

    private OnButtonClickListener listener;

    private ArrayList<Asset> listData;
    private Context activityContext;
    private UserService userService;

    private MaterialDialog dialog;

    public AssetsAdapter(ArrayList<Asset> listData, Context activityContext, UserService userService, OnButtonClickListener listener) {
        this.listData = listData;
        this.activityContext = activityContext;
        this.userService = userService;
        this.listener = listener;
    }

    public AssetsAdapter(Context activityContext) {
        this.activityContext = activityContext;
    }

    public void setListData(ArrayList<Asset> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_assets, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        // holder.iv_asset.setImageDrawable(); On progress

        holder.tv_name.setText(listData.get(position).getName());
        holder.tv_manufacturer.setText(listData.get(position).getAsset_id());
        holder.tv_quantity.setText(listData.get(position).getPurchase_cost());
        //holder.tv_status.setText(listData.get(position).getModel_no()); // Still on Progress

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showDeleteDialog(listData.get(position).getId());
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activityContext, "Edit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listData.isEmpty()) return 0;
        else return listData.size();
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

    public interface OnButtonClickListener {
        void showDeleteDialog(final int id);
    }

}
