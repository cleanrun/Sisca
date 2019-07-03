package com.siscaproject.sisca.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.siscaproject.sisca.Fragment.MonitoringSearchAssetFragment;
import com.siscaproject.sisca.Model.AssetAPI;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Config;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoringSearchAssetAdapter extends RecyclerView.Adapter<MonitoringSearchAssetAdapter.ItemHolder>{

    private Context activityContext;
    private List<AssetAPI> listData;
    private MonitoringSearchAssetFragment fragment;

    public MonitoringSearchAssetAdapter(Context context, List<AssetAPI> assetAPIList, MonitoringSearchAssetFragment fragment) {
        this.activityContext = context;
        this.listData = assetAPIList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_asset_report, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        holder.tvName.setText(listData.get(position).getName());
        holder.tvId.setText(listData.get(position).getAsset_id());
        holder.tvCondition.setText(listData.get(position).getCondition());

        String dateNow = Config.getDateNow();
        if (listData.get(position).getUpdated_at().substring(5, 7).equals(dateNow.substring(5, 7)))
            Picasso.get().load(R.drawable.ic_check_ada).into(holder.ivChecklist);

        if (listData.get(position).getCondition().equals("bagus"))
            holder.tvCondition.setTextColor(activityContext.getResources().getColor(R.color.color_v3_blue_1));
        else if (listData.get(position).getCondition().equals("rusak"))
            holder.tvCondition.setTextColor(activityContext.getResources().getColor(R.color.color_v3_red_2));
        else
            holder.tvCondition.setTextColor(activityContext.getResources().getColor(R.color.color_v3_gray_4));


        if (listData.get(position).getImage()!=null){
            Picasso.get().load(Config.getLinkImage()+listData.get(position).getImage()).into(holder.ivImage);
            Picasso.get().load(R.drawable.ic_check_tidak_ada).into(holder.ivChecklist);
        }

        holder.cvChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(activityContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_monitoring_condition);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                TextView tvAssetName = (TextView) dialog.findViewById(R.id.tv_asset_name);
                TextView tvAssetId = (TextView) dialog.findViewById(R.id.tv_asset_id);
                Button btnBagus = dialog.findViewById(R.id.btn_bagus);
                Button btnRusak = dialog.findViewById(R.id.btn_rusak);
                Button btnHilang = dialog.findViewById(R.id.btn_hilang);
                CardView cvBatal = dialog.findViewById(R.id.cv_batal);

                tvAssetName.setText(listData.get(position).getName());
                tvAssetId.setText(listData.get(position).getAsset_id());

                btnBagus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        putData(listData.get(position), "bagus");

                        listData.get(position).setCondition("bagus");
                        holder.tvCondition.setText("bagus");
                        holder.tvCondition.setTextColor(activityContext.getResources().getColor(R.color.color_v3_blue_1));
                        Picasso.get().load(R.drawable.ic_check_ada).into(holder.ivChecklist);
                        dialog.dismiss();
                    }
                });

                btnRusak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        putData(listData.get(position), "rusak");

                        listData.get(position).setCondition("rusak");
                        holder.tvCondition.setText("rusak");
                        holder.tvCondition.setTextColor(activityContext.getResources().getColor(R.color.color_v3_red_2));
                        Picasso.get().load(R.drawable.ic_check_ada).into(holder.ivChecklist);
                        dialog.dismiss();
                    }
                });

                btnHilang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        putData(listData.get(position), "hilang");

                        listData.get(position).setCondition("hilang");
                        holder.tvCondition.setText("hilang");
                        holder.tvCondition.setTextColor(activityContext.getResources().getColor(R.color.color_v3_gray_4));
                        Picasso.get().load(R.drawable.ic_check_tidak_ada).into(holder.ivChecklist);
                        dialog.dismiss();
                    }
                });

                cvBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private void putData(AssetAPI assetAPI, String condition){
        fragment.showProgressView();
        String dateNow = Config.getDateNow();
        Toast.makeText(activityContext, dateNow, Toast.LENGTH_LONG).show();
        //assetAPI.setUpdated_at(dateNow);
        assetAPI.setCondition(condition);

        UserService userService = APIProperties.getUserService();
        Call<AssetAPI> call = userService.putAsset(Header.auth, Header.accept, assetAPI.getId(), assetAPI);
        call.enqueue(new Callback<AssetAPI>() {
            @Override
            public void onResponse(Call<AssetAPI> call, Response<AssetAPI> response) {
                if (response.isSuccessful()){
                    Toast.makeText(activityContext, "Data successfully inputted!", Toast.LENGTH_SHORT).show();
                    fragment.hideProgressView();
                }
                else{
                    Toast.makeText(activityContext, "Something wrong just happened :(", Toast.LENGTH_SHORT).show();
                    fragment.hideProgressView();
                }
            }

            @Override
            public void onFailure(Call<AssetAPI> call, Throwable throwable) {
                Toast.makeText(activityContext, "Something wrong just happened :(", Toast.LENGTH_SHORT).show();
                fragment.hideProgressView();
            }
        });
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public TextView tvId;
        public TextView tvName;
        public TextView tvCondition;
        public ImageView ivImage;
        public CardView cvChecklist;
        public ImageView ivChecklist;

        public ItemHolder(View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id_item_asset_report);
            tvName = itemView.findViewById(R.id.tv_name_item_asset_report);
            tvCondition = itemView.findViewById(R.id.tv_condition_item_asset_report);
            ivImage = itemView.findViewById(R.id.iv_image_item_asset_report);
            ivChecklist = itemView.findViewById(R.id.iv_checklist_item_asset_report);
            cvChecklist = itemView.findViewById(R.id.cv_checklist_item_asset_report);
        }
    }
}
