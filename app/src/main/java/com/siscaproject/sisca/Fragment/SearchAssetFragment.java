package com.siscaproject.sisca.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Adapter.SearchAssetAdapter;
import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.DummyData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchAssetFragment extends Fragment {

    @BindView(R.id.pv_home_search_asset) ProgressView progressView;
    @BindView(R.id.rv_home_search_aset) RecyclerView recyclerView;
    @BindView(R.id.tv_empty_home_search_aset) TextView tvEmpty;

    private List<Asset> assetModelList, assetFoundList;
    private String search;

    public SearchAssetFragment() {
        // Required empty public constructor
    }

    public static SearchAssetFragment newInstance(String param1, String param2) {
        SearchAssetFragment fragment = new SearchAssetFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_asset, container, false);
        ButterKnife.bind(this, view);

        progressView.bringToFront();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        onLoadData();

        return view;
    }

    public void setTextSearch(String search){
        this.search = search;
    }

    public void onLoadData(){
        progressView.setVisibility(View.VISIBLE);

        assetModelList = new ArrayList<>();
        assetFoundList = new ArrayList<>();
        assetModelList = DummyData.getListAsset();

        for (int i=0; i<assetModelList.size(); i++){
            if (assetModelList.get(i).getId().toLowerCase().contains(search.toLowerCase()) && !search.isEmpty()){
                assetFoundList.add(assetModelList.get(i));
            }
        }

        if (assetFoundList.isEmpty()){
            recyclerView.setVisibility(View.INVISIBLE);
            tvEmpty.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.removeAllViews();
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.INVISIBLE);
            SearchAssetAdapter adapter = new SearchAssetAdapter(getContext(), assetFoundList);
            recyclerView.setAdapter(adapter);
        }

        progressView.setVisibility(View.INVISIBLE);
    }

}
