package com.siscaproject.sisca.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.DummyData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// HomeAsetDetailActivity

public class DetailAsetActivity extends AppCompatActivity {

    @BindView(R.id.pv_home_aset_detail) ProgressView progressView;
    @BindView(R.id.tv_name_home_aset_detail) TextView tvName;
    @BindView(R.id.tv_id_home_aset_detail) TextView tvIdAset;
    @BindView(R.id.tv_lokasi_home_aset_detail) TextView tvIdLokasi;
    @BindView(R.id.tv_pic_home_aset_detail) TextView tvPic;
    @BindView(R.id.tv_desc_home_aset_detail) TextView tvDesc;

    private List<Asset> assetModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_aset);

        ButterKnife.bind(this);

        progressView.bringToFront();
        progressView.setVisibility(View.VISIBLE);

        String idAset = getIntent().getStringExtra("ID_ASSET_EXTRA");
        assetModelList = new ArrayList<>();
        assetModelList = DummyData.getListAsset();

        for (int i=0; i<assetModelList.size(); i++){
            if (idAset.equals(assetModelList.get(i).getId())){
                tvName.setText(assetModelList.get(i).getName());
                tvIdAset.setText(assetModelList.get(i).getId());
                tvIdLokasi.setText(assetModelList.get(i).getIdLocation());
                tvPic.setText("Riski");
                tvDesc.setText(assetModelList.get(i).getDesc());
            }
        }

        progressView.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.cv_back_home_aset_detail) void cvBackOnClick(){
        onBackPressed();
    }
}
