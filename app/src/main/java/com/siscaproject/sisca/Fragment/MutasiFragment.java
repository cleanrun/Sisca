package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.siscaproject.sisca.Activity.DetailMutationActivity;
import com.siscaproject.sisca.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MutasiFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.iv_scan) ImageView ivScan;

    public MutasiFragment() {
        // Required empty public constructor
    }

    public static MutasiFragment newInstance() {
        MutasiFragment fragment = new MutasiFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mutasi, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.iv_scan)
    public void mutationActivity(View view){
        if(view.getId() == R.id.iv_scan){
            Log.i("MutasiFragment", "mutationActivity");
            startActivity(new Intent(getActivity(), DetailMutationActivity.class));
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
