package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.siscaproject.sisca.Adapter.AssetsListCategoryAdapter;
import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAssetFragment extends Fragment {
    private static final String TAG = "SearchAssetFragment";

    @BindView(R.id.btn_filter) ImageButton btn_filter;
    @BindView(R.id.btn_search) ImageButton btn_search;
    @BindView(R.id.et_search) EditText et_search;
    @BindView(R.id.swprefresh) SwipeRefreshLayout refresh;
    @BindView(R.id.rv_list_asset) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private AssetsListCategoryAdapter adapter;

    private OnFragmentInteractionListener mListener;

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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AssetsListCategoryAdapter(new ArrayList<Asset>(), getActivity());
        recyclerView.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //getAsset();
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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
