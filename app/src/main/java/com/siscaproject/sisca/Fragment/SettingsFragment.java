package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.siscaproject.sisca.Activity.ModelAssetsActivity;
import com.siscaproject.sisca.Activity.StatusLabelActivity;
import com.siscaproject.sisca.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.btn_status_label) LinearLayout btn_status_label;
    @BindView(R.id.btn_asset_model) LinearLayout btn_asset_model;
    @BindView(R.id.btn_category) LinearLayout btn_category;
    @BindView(R.id.btn_manufacturer) LinearLayout btn_manufacturer;
    @BindView(R.id.btn_supplier) LinearLayout btn_supplier;
    @BindView(R.id.btn_location) LinearLayout btn_location;
    @BindView(R.id.btn_company) LinearLayout btn_company;
    @BindView(R.id.btn_depreciation) LinearLayout btn_depreciation;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.btn_status_label, R.id.btn_asset_model, R.id.btn_category, R.id.btn_manufacturer,
            R.id.btn_supplier, R.id.btn_location, R.id.btn_company, R.id.btn_depreciation})
    public void onClick(View view){
        int id = view.getId();
        switch(id){
            case R.id.btn_status_label:
                //Toast.makeText(getActivity(), "Status Label", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), StatusLabelActivity.class));
                break;
            case R.id.btn_asset_model:
                //Toast.makeText(getActivity(), "Asset Model", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), ModelAssetsActivity.class));
                break;
            case R.id.btn_category:
                Toast.makeText(getActivity(), "Category", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_manufacturer:
                Toast.makeText(getActivity(), "Manufacturer", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_supplier:
                Toast.makeText(getActivity(), "Supplier", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_location:
                Toast.makeText(getActivity(), "Location", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_company:
                Toast.makeText(getActivity(), "Company", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_depreciation:
                Toast.makeText(getActivity(), "Depreciation", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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
