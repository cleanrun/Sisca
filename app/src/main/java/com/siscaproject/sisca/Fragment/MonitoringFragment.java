package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Model.LocationModel;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonitoringFragment extends Fragment {
    private static final String TAG = "MonitoringFragment";

    private OnFragmentInteractionListener mListener;

    /*@BindView(R.id.et_search_report)
    EditText et_search;*/
    @BindView(R.id.pv_report)
    ProgressView progressView;
    @BindView(R.id.ll_empty_report)
    LinearLayout ll_empty;
    @BindView(R.id.rv_report)
    RecyclerView recyclerView;

    private List<LocationModel> locationList;
    private UserService userService;
    private ArrayList<LocationModel> locations;

    public MonitoringFragment() {
        // Required empty public constructor
    }

    public static MonitoringFragment newInstance() {
        MonitoringFragment fragment = new MonitoringFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monitoring, container, false);
        ButterKnife.bind(this, view);

        ll_empty.setVisibility(View.VISIBLE);
        progressView.bringToFront();
        progressView.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userService = APIProperties.getUserService();
        getAsset();

        return view;
    }

    private void getData() {
        locationList = new ArrayList<>();

        for (int i=0; i<locations.size(); i++){
            String id = locations.get(i).getId();
            String name = locations.get(i).getName();
            int sizeAset = 99;

            locationList.add(new LocationModel(name, id, sizeAset));

            /*boolean isSame = false;
            for (int j=0; j<locationList.size(); j++){
                if (locationList.get(j).getId().equals(id)){
                    isSame = true;
                }
            }
            if (!isSame)
                locationList.add(new LocationModel(name, id, pic));*/
        }

        showData();
    }

    private void getAsset() {
        /*Call<ResponseIndex<LocationModel>> call = userService.indexLocation(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<LocationModel>>() {
            @Override
            public void onResponse(Call<ResponseIndex<LocationModel>> call, Response<ResponseIndex<LocationModel>> response) {
                int index = 0;
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    Log.i(TAG, "onResponse: total " + total);

                    locations = response.body().getRows();
                    Log.d("TOTAL_MSG", total+"");
                    Log.d("INDEX_MSG", index+"");
                    Log.d("ROWS_MSG", locations.get(0).getId());
                    Log.d("ROWS_MSG", locations.get(1).getId());
                    Log.d("ROWS_MSG", locations.get(2).getId());
                    Log.d("MSG_MSG", response.message());
                    index++;

                } else {
                    Log.i(TAG, "onResponse: else");
                }

                getData();
            }

            @Override
            public void onFailure(Call<ResponseIndex<LocationModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });*/
    }

    private void showData() {
        /*if (!locationList.isEmpty()){
            LocationAdapter adapter = new LocationAdapter(getContext(), locationList);
            recyclerView.setAdapter(adapter);

            ll_empty.setVisibility(View.INVISIBLE);
            progressView.setVisibility(View.INVISIBLE);
        }
        else{
            ll_empty.setVisibility(View.VISIBLE);
            progressView.setVisibility(View.INVISIBLE);
        }*/

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
