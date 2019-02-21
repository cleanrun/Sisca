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
import com.siscaproject.sisca.Model.Category;
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

public class EditCategoryFragment extends DialogFragment {
    private static final String TAG = "EditCategoryFragment";

    private Category category;

    private UserService userService;

    @BindView(R.id.btn_close) ImageButton btnClose;
    @BindView(R.id.btn_save) Button btnSave;

    @BindView(R.id.tv_id) TextView tvId;
    @BindView(R.id.et_name) AppCompatEditText etName;
    @BindView(R.id.et_type) AppCompatEditText etType;

    private MaterialDialog savingDialog;

    private OnFragmentInteractionListener mListener;

    public EditCategoryFragment() {
        // Required empty public constructor
    }

    public static EditCategoryFragment newInstance() {
        EditCategoryFragment fragment = new EditCategoryFragment();
        return fragment;
    }

    public void setCategory(Category category){
        this.category = category;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = APIProperties.getUserService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_category, container, false);
        ButterKnife.bind(this, view);

        tvId.setText(category.getId());

        etName.setText(category.getName());
        etType.setText(category.getType());

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
        category.setName(etName.getText().toString());
        category.setType(etType.getText().toString());

        showSavingDialog();

        Call<Category> call = userService.putCategory(Header.auth, Header.accept, Integer.valueOf(category.getId()), category);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
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
            public void onFailure(Call<Category> call, Throwable t) {
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
