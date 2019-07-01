package com.siscaproject.sisca.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Activity.BluetoothActivity;
import com.siscaproject.sisca.Activity.QRActivity;
import com.siscaproject.sisca.Activity.SearchActivity;
import com.siscaproject.sisca.Activity.SplashScreenActivity;
import com.siscaproject.sisca.Adapter.LocationAdapter;
import com.siscaproject.sisca.Adapter.LocationBoxAdapter;
import com.siscaproject.sisca.Model.AssetAPI;
import com.siscaproject.sisca.Model.LocationModel;
import com.siscaproject.sisca.Model.LocationAPI;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Model.User;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.DummyData;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class HomeFragment extends Fragment{

    @BindView(R.id.tv_number_users_home) TextView tv_number_users;
    @BindView(R.id.tv_number_assets_home) TextView tv_number_assets;
    @BindView(R.id.pv_home) ProgressView progressView;
    @BindView(R.id.rv_home) RecyclerView recyclerView;
    @BindView(R.id.et_search_home) EditText et_search;
    @BindView(R.id.iv_icon_list_home) ImageView iv_icon_list;

    private boolean isIconBoxList = false;
    private int sortNumber = 1;
    private List<LocationModel> locationList;

    private UserService userService;
    private ArrayList<LocationAPI> locationAPIList;

    private MaterialDialog scannerDialog;

    // Handling the NullPointerException bug right after installing the app
    private LocationAdapter.ExceptionHandler exceptionHandler = new LocationAdapter.ExceptionHandler() {
        @Override
        public void onException() {
            Prefs.clear();
            getActivity().finish();
            startActivity(new Intent(getActivity(), SplashScreenActivity.class));
        }
    };

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        progressView.bringToFront();
        progressView.setVisibility(View.VISIBLE);

        userService = APIProperties.getUserService();
        getDataUsers();

        return view;
    }

    private void getDataUsers() {
        Call<ResponseIndex<User>> call = userService.indexUser(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<User>>() {
            @Override
            public void onResponse(Call<ResponseIndex<User>> call, Response<ResponseIndex<User>> response) {
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    Log.i(TAG, "onResponse: total " + total);
                    Log.d(TAG, "testotaluser" + total);

                    tv_number_users.setText(total+" "+tv_number_users.getText().toString());
                    getDataAssets();
                    //ArrayList<AssetModel> rows = response.body().getRows();

                    /*adapter = new AssetsAdapter(rows, getApplicationContext(), userService, listener);
                    recyclerView.setAdapter(adapter);*/
                } else {
                    Log.i(TAG, "onResponse: else");
                    errorToast();
                    getDataAssets();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<User>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                errorToast();
                getDataAssets();
            }
        });
    }

    private void getDataAssets() {
        Call<ResponseIndex<AssetAPI>> call = userService.indexAsset(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<AssetAPI>>() {
            @Override
            public void onResponse(Call<ResponseIndex<AssetAPI>> call, Response<ResponseIndex<AssetAPI>> response) {
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    tv_number_assets.setText(total+" "+tv_number_assets.getText().toString());
                    /*ArrayList<AssetAPI> data = response.body().getData();
                    Log.d(TAG, "testotalasset : " + data.get(0).getName());
                    Log.d(TAG, "testotalasset : " + data.get(1).getName());
                    Log.d(TAG, "testotalasset : " + data.get(2).getName());
                    Log.d(TAG, "testotalasset : " + data.get(3).getName());*/
                    getDataLocation();
                }
                else{
                    Log.i(TAG, "onResponse: else");
                    errorToast();
                    getDataLocation();
                }

            }

            @Override
            public void onFailure(Call<ResponseIndex<AssetAPI>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                errorToast();
                getDataLocation();
            }
        });
    }

    private void getDataLocation() {
        Call<ResponseIndex<LocationAPI>> call = userService.indexLocation(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<LocationAPI>>() {
            @Override
            public void onResponse(Call<ResponseIndex<LocationAPI>> call, Response<ResponseIndex<LocationAPI>> response) {
                if (response.isSuccessful()) {
                    int total = response.body().getTotal();
                    Log.i(TAG, "onResponse: total " + total);

                    ArrayList<LocationAPI> rows = response.body().getRows();
                    Log.d(TAG, "testotalloc" + total);
                    /*Log.d(TAG, "testotalloc" + rows.size());
                    Log.d(TAG, "testotalloc" + rows.get(0).getName());
                    Log.d(TAG, "testotalloc" + rows.get(1).getName());*/
                    //ArrayList<LocationAPI> data = response.body().getData();
                    locationAPIList = response.body().getData();

                    /*locationList = new ArrayList<>();
                    for (int i=0; i<data.size(); i++){
                        locationList.add(new LocationModel(data.get(i).getName(), data.get(i).getId()+"", data.get(i).getManager_id()+""));
                    }*/

                    /*for (int i=0; i<total; i++){
                        Log.d(TAG, "testotalloc" + rows.get(i).getName());
                    }*/

                    Collections.sort(locationAPIList, new Comparator<LocationAPI>() {
                        @Override
                        public int compare(LocationAPI locationAPI1, LocationAPI locationAPI2) {
                            return locationAPI1.getUpdated_at().compareTo(locationAPI2.getUpdated_at());
                        }
                    });

                    if (locationAPIList!=null){
                        if (locationAPIList.size()>=4){
                            //cuma menampilkan 5 lokasi terbaru
                            ArrayList<LocationAPI> listTmp = new ArrayList<>();
                            for (int i=0; i<4; i++){
                                listTmp.add(locationAPIList.get(i));
                            }
                            locationAPIList = listTmp;
                        }
                    }

                    showData();
                    /*adapter = new AssetsAdapter(rows, getApplicationContext(), userService, listener);
                    recyclerView.setAdapter(adapter);*/
                } else {
                    Log.i(TAG, "onResponse: else");
                    errorToast();
                    showData();
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<LocationAPI>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                errorToast();
                showData();
            }
        });
    }

    private void getData() {
        locationList = DummyData.getListLocation();

        /*locationList.add(new LocationModel("Ruang kerja 1", "K101", 20));
        locationList.add(new LocationModel("Ruang istirahat", "I203", 28));
        locationList.add(new LocationModel("Ruang kerja 2", "K102", 17));*/

        showData();
    }

    private void showData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        LocationAdapter adapter = new LocationAdapter(getContext(), locationAPIList);
        adapter.setExceptionHandler(exceptionHandler);
        recyclerView.setAdapter(adapter);

        progressView.setVisibility(View.INVISIBLE);
    }

    private void showDataListBox() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        LocationBoxAdapter adapter = new LocationBoxAdapter(getContext(), locationAPIList);
        recyclerView.setAdapter(adapter);

        progressView.setVisibility(View.INVISIBLE);
    }

    private void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
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

    @OnClick(R.id.cv_scan_qr_home) void cvScanQrOnClick() {
        showScannerDialog();
    }

    @OnClick(R.id.cv_search_submit_home) void cvSearchOnClick() {
        if (!et_search.getText().toString().isEmpty()){
            progressView.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra("EXTRA_SEARCH", et_search.getText().toString());
            getContext().startActivity(intent);
            progressView.setVisibility(View.INVISIBLE);
        }
        else
            Toast.makeText(getContext(), "Harap isi kolom pencarian", Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.cv_filter_home) void cvFilterOnClick() {
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
                Collections.sort(locationAPIList, new Comparator<LocationAPI>() {
                    @Override
                    public int compare(LocationAPI locationAPI1, LocationAPI locationAPI2) {
                        return locationAPI1.getUpdated_at().compareTo(locationAPI2.getUpdated_at());
                    }
                });

                sortNumber = 1;
                ivTerbaru.setVisibility(View.VISIBLE);
                ivTerlama.setVisibility(View.INVISIBLE);
                ivAtoZ.setVisibility(View.INVISIBLE);
                ivZtoA.setVisibility(View.INVISIBLE);
                if (!isIconBoxList)
                    showData();
                else
                    showDataListBox();
                dialog.dismiss();
            }
        });

        tvTerlama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                Collections.sort(locationAPIList, new Comparator<LocationAPI>() {
                    @Override
                    public int compare(LocationAPI locationAPI1, LocationAPI locationAPI2) {
                        return locationAPI2.getUpdated_at().compareTo(locationAPI1.getUpdated_at());
                    }
                });

                sortNumber = 2;
                ivTerbaru.setVisibility(View.INVISIBLE);
                ivTerlama.setVisibility(View.VISIBLE);
                ivAtoZ.setVisibility(View.INVISIBLE);
                ivZtoA.setVisibility(View.INVISIBLE);
                if (!isIconBoxList)
                    showData();
                else
                    showDataListBox();
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
                if (!isIconBoxList)
                    showData();
                else
                    showDataListBox();
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
                if (!isIconBoxList)
                    showData();
                else
                    showDataListBox();
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.cv_icon_list_home) void cvIconListOnClick() {
        progressView.setVisibility(View.VISIBLE);
        if (!isIconBoxList){
            iv_icon_list.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_list));
            isIconBoxList = true;
            showDataListBox();
        }
        else{
            iv_icon_list.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_box_list));
            isIconBoxList = false;
            showData();
        }
    }

    private void errorToast() {
        Toast.makeText(getActivity(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
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
