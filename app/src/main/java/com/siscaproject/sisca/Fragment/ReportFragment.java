package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.siscaproject.sisca.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.btn_audit_log) LinearLayout btn_audit_log;
    @BindView(R.id.btn_depreciation) LinearLayout btn_depreciation;
    @BindView(R.id.btn_assets_maintenance) LinearLayout btn_assets_maintenance;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.btn_audit_log, R.id.btn_assets_maintenance, R.id.btn_depreciation})
    public void onClick(View view){
        int id = view.getId();
        switch(id){
            case R.id.btn_audit_log:
                Toast.makeText(getActivity(), "Audit Log", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_depreciation:
                Toast.makeText(getActivity(), "Depreciation", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_assets_maintenance:
                Toast.makeText(getActivity(), "Assets Maintenance", Toast.LENGTH_SHORT).show();
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
