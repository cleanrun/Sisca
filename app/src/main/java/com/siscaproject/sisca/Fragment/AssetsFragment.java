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

import com.siscaproject.sisca.Activity.AllAssetsActivity;
import com.siscaproject.sisca.Activity.AssetsListCategoryActivity;
import com.siscaproject.sisca.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AssetsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.btn_all) LinearLayout btn_all;
    @BindView(R.id.btn_deployed) LinearLayout btn_deployed;
    @BindView(R.id.btn_deployable) LinearLayout btn_deployable;
    @BindView(R.id.btn_pending) LinearLayout btn_pending;
    @BindView(R.id.btn_undeployable) LinearLayout btn_undeployable;
    @BindView(R.id.btn_archived) LinearLayout btn_archived;

    public AssetsFragment() {
        // Required empty public constructor
    }

    public static AssetsFragment newInstance() {
        AssetsFragment fragment = new AssetsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assets, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick ({R.id.btn_all, R.id.btn_deployed, R.id.btn_deployable, R.id.btn_pending, R.id.btn_undeployable, R.id.btn_archived})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_all:
                Intent intent = new Intent(getActivity(), AssetsListCategoryActivity.class);
                intent.putExtra("title", "All Assets");
                startActivity(intent);
                break;
            case R.id.btn_deployed:
                Toast.makeText(getActivity(), "Deployed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_deployable:
                Toast.makeText(getActivity(), "Deployable", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_pending:
                Toast.makeText(getActivity(), "Pending", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_undeployable:
                Toast.makeText(getActivity(), "Undeployable", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_archived:
                Toast.makeText(getActivity(), "Archived", Toast.LENGTH_SHORT).show();
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
