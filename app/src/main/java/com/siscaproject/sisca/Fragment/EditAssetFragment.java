package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.R;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAssetFragment extends DialogFragment {
    private static final String TAG = "EditAssetFragment";

    private Asset asset;

    private UserService userService;

    @BindView(R.id.btn_close) ImageButton btnClose;
    @BindView(R.id.btn_save) Button btnSave;

    @BindView(R.id.tv_id) TextView tvId;

    @BindView(R.id.et_name) AppCompatEditText etName;
    @BindView(R.id.et_id) AppCompatEditText etId;
    @BindView(R.id.et_serial) AppCompatEditText etSerial;
    @BindView(R.id.et_purchase_date) AppCompatEditText etPurchaseDate;
    @BindView(R.id.et_order_number) AppCompatEditText etOrderNumber;
    @BindView(R.id.et_purchase_cost) AppCompatEditText etPurchaseCost;
    @BindView(R.id.et_warranty_months) AppCompatEditText etWarrantyMonths;

    private MaterialDialog savingDialog;

    private OnFragmentInteractionListener mListener;

    public EditAssetFragment() {
        // Required empty public constructor
    }

    public static EditAssetFragment newInstance(Asset asset) {
        EditAssetFragment fragment = new EditAssetFragment();
        return fragment;
    }

    public void setAsset(Asset asset){
        this.asset = asset;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = APIProperties.getUserService();
        Log.i(TAG, "onCreate userService");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_asset, container, false);
        ButterKnife.bind(this, view);

        tvId.setText(String.valueOf(asset.getId()));

        etName.setText(asset.getName());
        etId.setText(asset.getAsset_id());
        etSerial.setText(asset.getSerial());
        etPurchaseDate.setText(asset.getPurchase_date());
        etOrderNumber.setText(asset.getOrder_number());
        etPurchaseCost.setText(asset.getPurchase_cost());
        etWarrantyMonths.setText(asset.getWarranty_months());

        return view;
    }

    @OnClick({R.id.btn_close, R.id.btn_save})
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_save:
                saveEdits();
                break;
            default:
                break;
        }
    }

    private void showSavingDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .content("Saving data..")
                .progress(true, 0)
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .canceledOnTouchOutside(false);

        savingDialog = builder.build();
        savingDialog.show();
    }

    private void saveEdits(){
        asset.setName(etName.getText().toString());
        asset.setAsset_id(etId.getText().toString());
        asset.setSerial(etSerial.getText().toString());
        asset.setPurchase_date(etPurchaseDate.getText().toString());
        asset.setOrder_number(etOrderNumber.getText().toString());
        asset.setPurchase_cost(etPurchaseCost.getText().toString());
        asset.setWarranty_months(etWarrantyMonths.getText().toString());

        showSavingDialog();
        Call<Asset> call = userService.putFixed(Header.auth, Header.accept, asset.getId(), asset);
        call.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                savingDialog.dismiss();

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: " + response.body());

                    successToast();
                    dismiss();
                }
                else{
                    try{
                        Log.e(TAG, "onResponse: else " + response.errorBody().string() );
                        errorToast();
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: else catch " + e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(Call<Asset> call, Throwable t) {
                savingDialog.dismiss();

                Log.e(TAG, "onFailure: saveEdits " + t.getMessage() );
                errorToast();
            }
        });
    }

    private void errorToast() {
        Toast.makeText(getActivity(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }

    private void successToast() {
        Toast.makeText(getActivity(), "Data successfully updated!", Toast.LENGTH_SHORT).show();
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
