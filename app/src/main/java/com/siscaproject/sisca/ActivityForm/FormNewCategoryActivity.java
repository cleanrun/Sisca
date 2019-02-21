package com.siscaproject.sisca.ActivityForm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class FormNewCategoryActivity extends AppCompatActivity {
    private static final String TAG = "FormNewCategory";

    private UserService userService;

    private MaterialDialog loadingDialog;

    @BindView(R.id.et_name) EditText etName;
    @BindView(R.id.spn_type) Spinner spnType;
    @BindView(R.id.btn_upload) Button btnUpload;
    @BindView(R.id.btn_save) Button btnSave;

    private static final String[] itemSpinnerType = {"stock", "fixed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_category);

        userService = APIProperties.getUserService();

        ButterKnife.bind(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemSpinnerType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(arrayAdapter);
        spnType.setSelection(0);
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Do nothing
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

    }

    @OnClick({R.id.btn_upload, R.id.btn_save})
    public void onClick(View view){
        int id = view.getId();
        switch(id){
            case R.id.btn_upload:
                Toast.makeText(this, "Upload image", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_save:
                //Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                createCategory();
                break;
            default:
                break;
        }
    }

    private void showDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content("Inputting data..")
                .progress(true, 0)
                .contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .canceledOnTouchOutside(false);

        loadingDialog = builder.build();
        loadingDialog.show();
    }

    private void createCategory(){
        String name = etName.getText().toString().trim();

        String type = spnType.getSelectedItem().toString();

        Category category = new Category(name, type);
        addCategory(category);
    }

    private void addCategory(Category category){
        showDialog();

        Call<Category> call = userService.storeCategory(Header.auth, Header.accept, category);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                loadingDialog.dismiss();

                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: " + response.body());
                    successDialog();
                }
                else{
                    try{
                        errorDialog();
                        Log.e(TAG, "onResponse: add category is not successful " + response.errorBody().string() );
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: catch " + e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                loadingDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage() );
                errorDialog();
            }
        });
    }

    private void errorDialog(){
        Toast.makeText(FormNewCategoryActivity.this, "Something wrong just happened :(", Toast.LENGTH_SHORT).show();
    }

    private void successDialog(){
        Toast.makeText(FormNewCategoryActivity.this, "Data successfully inputted!", Toast.LENGTH_SHORT).show();
    }
}
