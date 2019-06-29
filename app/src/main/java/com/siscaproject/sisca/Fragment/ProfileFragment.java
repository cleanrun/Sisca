package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pixplicity.easyprefs.library.Prefs;
import com.siscaproject.sisca.Activity.DetailLocationActivity;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.Header;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.iv_photo) CircularImageView ivPhoto;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_email) TextView tvEmail;
    @BindView(R.id.ll_nama) LinearLayout llNama;
    @BindView(R.id.ll_email) LinearLayout llEmail;
    @BindView(R.id.ll_password) LinearLayout llPassword;
    @BindView(R.id.btn_keluar) AppCompatButton btnKeluar;

    private MaterialDialog logoutDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        tvName.setText(Header.name);
        tvEmail.setText(Header.email);

        return view;
    }

    @OnClick({R.id.btn_keluar, R.id.ll_nama, R.id.ll_email, R.id.ll_password})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_keluar:
                logOut();
                break;
            case R.id.ll_nama:
                showToast("Nama");
                //Intent locationIntent = new Intent(getActivity(), DetailLocationActivity.class);
                //locationIntent.putExtra("ID_EXTRA", 1);
                //startActivity(locationIntent);
                break;
            case R.id.ll_email:
                showToast("Email");
                break;
            case R.id.ll_password:
                showToast("Password");
                break;
            default:
                break;
        }
    }

    private void logOut(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
                .content("Anda yakin ingin keluar?")
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .positiveText("Ya")
                .negativeText("Tidak")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Prefs.clear();
                        getActivity().finish();
                        getActivity().moveTaskToBack(true);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // Do nothing
                    }
                })
                .canceledOnTouchOutside(true);

        logoutDialog = builder.build();
        logoutDialog.show();

    }

    private void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
