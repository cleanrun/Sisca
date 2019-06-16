package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Activity.SearchActivity;
import com.siscaproject.sisca.Adapter.AssetAdapter;
import com.siscaproject.sisca.Adapter.LocationAdapter;
import com.siscaproject.sisca.Model.AssetAPI;
import com.siscaproject.sisca.Model.AssetMutasi;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MutasiFragment extends Fragment {
    private static final String TAG = "MutasiFragment";

    private OnFragmentInteractionListener mListener;

    private ArrayList<AssetMutasi> listAsset; // List terakhir mutasi

    @BindView(R.id.cv_scanner) CardView cvScanner;
    @BindView(R.id.cv_search) CardView cvSearch;
    @BindView(R.id.btn_filter) AppCompatImageButton btnFilter;
    @BindView(R.id.rv_mutasi_terakhir) RecyclerView recyclerView;
    @BindView(R.id.pv_mutasi) ProgressView progressView;

    private UserService userService;

    public MutasiFragment() {
        // Required empty public constructor
    }

    public static MutasiFragment newInstance() {
        MutasiFragment fragment = new MutasiFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mutasi, container, false);
        ButterKnife.bind(this, view);

        userService = APIProperties.getUserService();
        getDataAsset();

        return view;
    }

    @OnClick({R.id.cv_scanner, R.id.cv_search, R.id.btn_filter})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.cv_scanner:
                showToast("Scanner");
                break;
            case R.id.cv_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.btn_filter:
                showToast("Filter");
                break;
            default:
                break;
        }
    }

    public void getDataAsset(){
        progressView.setVisibility(View.VISIBLE);

        Call<ResponseIndex<AssetMutasi>> call = userService.indexAssetMutasi(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<AssetMutasi>>() {
            @Override
            public void onResponse(Call<ResponseIndex<AssetMutasi>> call, Response<ResponseIndex<AssetMutasi>> response) {
                progressView.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    int total = response.body().getTotal();
                    Log.d(TAG, "onResponse asset: total asset " + total);

                    listAsset = response.body().getData();

                    showData();
                }
                else{
                    Log.e(TAG, "onFailure: not successful");
                    showToast("Unable to fetch the data.");
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<AssetMutasi>> call, Throwable t) {
                progressView.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: " + t.getMessage());
                showToast("Unable to fetch the data.");
            }
        });
    }

    private void showData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        AssetAdapter adapter = new AssetAdapter(getContext(), listAsset);
        recyclerView.setAdapter(adapter);

        progressView.setVisibility(View.INVISIBLE);
    }

    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
