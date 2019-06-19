package com.siscaproject.sisca.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Activity.BluetoothActivity;
import com.siscaproject.sisca.Activity.MonitoringSearchActivity;
import com.siscaproject.sisca.Activity.QRActivity;
import com.siscaproject.sisca.Adapter.MonitoringAdapter;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoringFragment extends Fragment {
    private static final String TAG = "MonitoringFragment";

    private OnFragmentInteractionListener mListener;

    /*@BindView(R.id.et_search_report)
        EditText et_search;*/
    @BindView(R.id.pv_report)
    ProgressView progressView;
    @BindView(R.id.rv_report)
    RecyclerView recyclerView;

    private MaterialDialog scannerDialog;

    //private List<LocationModel> locationList;
    private UserService userService;
    //private ArrayList<Location> locations;
    private List<LocationAPI> locationAPIList;

    private int sortNumber = 3;

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

        //ll_empty.setVisibility(View.VISIBLE);
        progressView.bringToFront();
        progressView.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userService = APIProperties.getUserService();
        getDataLocation();

        return view;
    }

    private void getDataLocation() {
        Call<ResponseIndex<LocationAPI>> call = userService.indexLocation(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<LocationAPI>>() {
            @Override
            public void onResponse(Call<ResponseIndex<LocationAPI>> call, Response<ResponseIndex<LocationAPI>> response) {
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    locationAPIList = response.body().getData();

                    Collections.sort(locationAPIList, new Comparator<LocationAPI>() {
                        @Override
                        public int compare(LocationAPI locationAPI1, LocationAPI locationAPI2) {
                            return locationAPI1.getName().compareTo(locationAPI2.getName());
                        }
                    });

                    showData();
                    /*adapter = new AssetsAdapter(rows, getApplicationContext(), userService, listener);
                    recyclerView.setAdapter(adapter);*/
                } else {
                    Log.i(Constraints.TAG, "onResponse: else");
                    errorToast();
                    showData();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<LocationAPI>> call, Throwable throwable) {
                Log.i(Constraints.TAG, "onResponse: else");
                errorToast();
                showData();
            }
        });
    }

    /*private void getAsset() {
     *//*Call<ResponseIndex<Location>> call = userService.indexLocation(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<Location>>() {
            @Override
            public void onResponse(Call<ResponseIndex<Location>> call, Response<ResponseIndex<Location>> response) {
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
            public void onFailure(Call<ResponseIndex<Location>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });*//*
    }
*/
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

        MonitoringAdapter adapter = new MonitoringAdapter(getContext(), locationAPIList);
        recyclerView.removeAllViews();
        recyclerView.setAdapter(adapter);

        progressView.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.cv_scan_qr_report) void scanQrOnClick(){
        showScannerDialog();
    }

    @OnClick(R.id.cv_search_report) void searchOnClick(){
        getContext().startActivity(new Intent(getContext(), MonitoringSearchActivity.class));
    }

    @OnClick(R.id.cv_filter_report) void filterOnClick(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sort_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView tvTerbaru = (TextView) dialog.findViewById(R.id.tv_terbaru);
        TextView tvTerlama = (TextView) dialog.findViewById(R.id.tv_terlama);
        TextView tvAtoZ = (TextView) dialog.findViewById(R.id.tv_az);
        TextView tvZtoA = (TextView) dialog.findViewById(R.id.tv_za);
        final ImageView ivTerbaru = (ImageView) dialog.findViewById(R.id.iv_terbaru);
        final ImageView ivTerlama = (ImageView) dialog.findViewById(R.id.iv_terlama);
        final ImageView ivAtoZ = (ImageView) dialog.findViewById(R.id.iv_az);
        final ImageView ivZtoA = (ImageView) dialog.findViewById(R.id.iv_za);

        ivTerbaru.setVisibility(View.INVISIBLE);
        ivTerlama.setVisibility(View.INVISIBLE);
        ivAtoZ.setVisibility(View.INVISIBLE);
        ivZtoA.setVisibility(View.INVISIBLE);

        switch (sortNumber){
            case 1 :
                ivTerbaru.setVisibility(View.VISIBLE);
                break;
            case 2 :
                ivTerlama.setVisibility(View.VISIBLE);
                break;
            case 3 :
                ivAtoZ.setVisibility(View.VISIBLE);
                break;
            case 4 :
                ivZtoA.setVisibility(View.VISIBLE);
                break;
        }

        tvTerbaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                /*locationList.clear();
                locationList.add(new LocationModel("Ruang kerja 1", "K101", 6));
                locationList.add(new LocationModel("Ruang istirahat", "I203", 6));
                locationList.add(new LocationModel("Ruang kerja 2", "K102", 6));*/
                Collections.sort(locationAPIList, new Comparator<LocationAPI>() {
                    @Override
                    public int compare(LocationAPI locationAPI1, LocationAPI locationAPI2) {
                        return locationAPI2.getUpdated_at().compareTo(locationAPI1.getUpdated_at());
                    }
                });

                sortNumber = 1;
                ivTerbaru.setVisibility(View.VISIBLE);
                ivTerlama.setVisibility(View.INVISIBLE);
                ivAtoZ.setVisibility(View.INVISIBLE);
                ivZtoA.setVisibility(View.INVISIBLE);
                showData();

                dialog.dismiss();
            }
        });

        tvTerlama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                /*locationList.clear();
                locationList.add(new LocationModel("Ruang kerja 2", "K102", 6));
                locationList.add(new LocationModel("Ruang istirahat", "I203", 6));
                locationList.add(new LocationModel("Ruang kerja 1", "K101", 6));*/
                Collections.sort(locationAPIList, new Comparator<LocationAPI>() {
                    @Override
                    public int compare(LocationAPI locationAPI1, LocationAPI locationAPI2) {
                        return locationAPI1.getUpdated_at().compareTo(locationAPI2.getUpdated_at());
                    }
                });

                sortNumber = 2;
                ivTerbaru.setVisibility(View.INVISIBLE);
                ivTerlama.setVisibility(View.VISIBLE);
                ivAtoZ.setVisibility(View.INVISIBLE);
                ivZtoA.setVisibility(View.INVISIBLE);
                showData();

                dialog.dismiss();
            }
        });

        tvAtoZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                /*Collections.sort(locationList, new Comparator<LocationModel>() {
                    @Override
                    public int compare(LocationModel locationModel1, LocationModel locationModel2) {
                        return locationModel1.getId().compareTo(locationModel2.getId());
                    }
                });*/
                Collections.sort(locationAPIList, new Comparator<LocationAPI>() {
                    @Override
                    public int compare(LocationAPI locationAPI1, LocationAPI locationAPI2) {
                        return locationAPI1.getName().compareTo(locationAPI2.getName());
                    }
                });

                sortNumber = 3;
                ivTerbaru.setVisibility(View.INVISIBLE);
                ivTerlama.setVisibility(View.INVISIBLE);
                ivAtoZ.setVisibility(View.VISIBLE);
                ivZtoA.setVisibility(View.INVISIBLE);
                showData();

                dialog.dismiss();
            }
        });

        tvZtoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                /*Collections.sort(locationList, new Comparator<LocationModel>() {
                    @Override
                    public int compare(LocationModel locationModel1, LocationModel locationModel2) {
                        return locationModel2.getId().compareTo(locationModel1.getId());
                    }
                });*/
                Collections.sort(locationAPIList, new Comparator<LocationAPI>() {
                    @Override
                    public int compare(LocationAPI locationAPI1, LocationAPI locationAPI2) {
                        return locationAPI2.getName().compareTo(locationAPI1.getName());
                    }
                });

                sortNumber = 4;
                ivTerbaru.setVisibility(View.INVISIBLE);
                ivTerlama.setVisibility(View.INVISIBLE);
                ivAtoZ.setVisibility(View.INVISIBLE);
                ivZtoA.setVisibility(View.VISIBLE);
                showData();

                dialog.dismiss();
            }
        });
    }

    private void errorToast() {
        Toast.makeText(getContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }

    private void showScannerDialog() {
        Log.i(TAG, "showScannerDialog: called");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .content("Silahkan pilih scanner yang ingin digunakan")
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .positiveText("Bluetooth")
                .negativeText("QR")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getActivity(), BluetoothActivity.class));
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getActivity(), QRActivity.class));
                    }
                })
                .canceledOnTouchOutside(true);

        scannerDialog = builder.build();
        scannerDialog.show();
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
