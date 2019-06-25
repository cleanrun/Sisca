package com.siscaproject.sisca.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.siscaproject.sisca.Adapter.AssetAdapter;
import com.siscaproject.sisca.Model.AssetMutasi;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailLocationActivity extends AppCompatActivity {
    private static final String TAG = "DetailLocationActivity";

    @BindView(R.id.cv_back) CardView cvBack;
    @BindView(R.id.tv_title_location) TextView tvTitle;
    @BindView(R.id.tv_alamat) TextView tvAlamat;
    @BindView(R.id.tv_manager) TextView tvManager;
    @BindView(R.id.tv_jumlah_aset) TextView tvJumlahAset;
    @BindView(R.id.rv_list_aset) RecyclerView recyclerView;
    @BindView(R.id.btn_monitoring) AppCompatButton btnMonitoring;

    private AssetAdapter.cardClickListener listener = new AssetAdapter.cardClickListener() {
        @Override
        public void onCardClick(int id) {
            // do nothing
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_location);

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AssetAdapter adapter = new AssetAdapter(this, dummyData(), listener);
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<AssetMutasi> dummyData(){
        ArrayList<AssetMutasi> data = new ArrayList<>();

        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 1", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 2", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 3", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 4", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 5", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 6", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 7", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 8", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 9", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 10", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 11", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 12", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 13", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 14", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 15", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 16", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 17", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 18", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 19", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 20", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));
        data.add(new AssetMutasi(1, "1", "1234", 1, 1, 1, "Asset 21", "asdf", "12-01-2019", "1000", "bagus", "12-01-2019", "13-01-2019"));

        return data;
    }
}
