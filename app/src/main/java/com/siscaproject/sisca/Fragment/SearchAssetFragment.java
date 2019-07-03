package com.siscaproject.sisca.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Adapter.SearchAssetAdapter;
import com.siscaproject.sisca.Model.AssetAPI;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;


public class SearchAssetFragment extends Fragment {

    @BindView(R.id.pv_home_search_asset) ProgressView progressView;
    @BindView(R.id.rv_home_search_aset) RecyclerView recyclerView;
    @BindView(R.id.ll_empty_home_search_aset)
    LinearLayout llEmpty;

    private String search;
    private List<AssetAPI> assetAPIList, assetAPIFoundList;
    private UserService userService;

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
        progressView.setVisibility(View.INVISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //onLoadData();

        return view;
    }

    public void setTextSearch(String search){
        this.search = search;
    }

    private void loadDataInit() {
        progressView.setVisibility(View.VISIBLE);
        userService = APIProperties.getUserService();
        Call<ResponseIndex<AssetAPI>> call = userService.indexAsset(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<AssetAPI>>() {
            @Override
            public void onResponse(Call<ResponseIndex<AssetAPI>> call, Response<ResponseIndex<AssetAPI>> response) {
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    Log.i(TAG, "onResponse: total " + total);
                    Log.d(TAG, "testotaluser" + total);

                    assetAPIList = response.body().getData();
                    onLoadData();
                } else {
                    Log.i(TAG, "onResponse: else");
                    errorToast();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<AssetAPI>> call, Throwable t) {
                Log.i(TAG, "onResponse: else");
                errorToast();
            }
        });
    }

    public void onLoadData(){
        if (assetAPIList==null){
            loadDataInit();
        }
        else{
            assetAPIFoundList = new ArrayList<>();
            for (int i=0; i<assetAPIList.size(); i++){
                if (assetAPIList.get(i).getAsset_id().toLowerCase().contains(search.toLowerCase()) && !search.isEmpty()){
                    assetAPIFoundList.add(assetAPIList.get(i));
                }
            }

            if (assetAPIFoundList.isEmpty()){
                recyclerView.setVisibility(View.INVISIBLE);
                llEmpty.setVisibility(View.VISIBLE);
            }
            else{
                recyclerView.removeAllViews();
                recyclerView.setVisibility(View.VISIBLE);
                llEmpty.setVisibility(View.INVISIBLE);
                SearchAssetAdapter adapter = new SearchAssetAdapter(getContext(), assetAPIFoundList);
                recyclerView.setAdapter(adapter);
            }

            progressView.setVisibility(View.INVISIBLE);
        }
    }

    private void errorToast() {
        Toast.makeText(getContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }

}
