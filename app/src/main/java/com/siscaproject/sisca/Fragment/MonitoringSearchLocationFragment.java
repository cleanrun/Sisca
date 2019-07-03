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
import com.siscaproject.sisca.Adapter.MonitoringSearchLocationAdapter;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.LocationModel;
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

public class MonitoringSearchLocationFragment extends Fragment {

    @BindView(R.id.pv_monitoring_search_location)
    ProgressView progressView;
    @BindView(R.id.rv_monitoring_search_location)
    RecyclerView recyclerView;
    @BindView(R.id.ll_empty_monitoring_search_location)
    LinearLayout llEmpty;

    private List<LocationModel> locationModelList, locationFoundList;
    private String search;
    private List<LocationAPI> locationAPIList, locationAPIFoundList;
    private UserService userService;

    public MonitoringSearchLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monitoring_search_location, container, false);
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
        Call<ResponseIndex<LocationAPI>> call = userService.indexLocation(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<LocationAPI>>() {
            @Override
            public void onResponse(Call<ResponseIndex<LocationAPI>> call, Response<ResponseIndex<LocationAPI>> response) {
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    Log.i(TAG, "onResponse: total " + total);
                    Log.d(TAG, "testotaluser" + total);

                    locationAPIList = response.body().getData();
                    onLoadData();
                } else {
                    Log.i(TAG, "onResponse: else");
                    errorToast();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<LocationAPI>> call, Throwable t) {
                Log.i(TAG, "onResponse: else");
                errorToast();
            }
        });
    }

    public void onLoadData(){

        if (locationAPIList==null){
            loadDataInit();
        }
        else{
            progressView.setVisibility(View.VISIBLE);
            locationAPIFoundList = new ArrayList<>();
            for (int i=0; i<locationAPIList.size(); i++){
                if (locationAPIList.get(i).getName().toLowerCase().contains(search.toLowerCase()) && !search.isEmpty()){
                    locationAPIFoundList.add(locationAPIList.get(i));
                }
            }

            if (locationAPIFoundList.isEmpty()){
                recyclerView.setVisibility(View.INVISIBLE);
                llEmpty.setVisibility(View.VISIBLE);
            }
            else{
                recyclerView.removeAllViews();
                recyclerView.setVisibility(View.VISIBLE);
                llEmpty.setVisibility(View.INVISIBLE);
                MonitoringSearchLocationAdapter adapter = new MonitoringSearchLocationAdapter(getContext(), locationAPIFoundList);
                recyclerView.setAdapter(adapter);
            }

            progressView.setVisibility(View.INVISIBLE);
        }
    }

    private void errorToast() {
        Toast.makeText(getContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }
}
