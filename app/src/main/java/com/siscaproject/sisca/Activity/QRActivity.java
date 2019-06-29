package com.siscaproject.sisca.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.Result;
import com.siscaproject.sisca.Model.AssetMutasi;
import com.siscaproject.sisca.Model.ResponseIndex;
import com.siscaproject.sisca.Utilities.APIProperties;
import com.siscaproject.sisca.Utilities.Header;
import com.siscaproject.sisca.Utilities.UserService;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;

public class QRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private static final String TAG = "QRActivity";

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    private UserService userService;

    private ArrayList<AssetMutasi> listAsset;

    private MaterialDialog nullDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        userService = APIProperties.getUserService();
        getAsset();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                //Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            }else{
                requestPermission();
            }
        }
    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(QRActivity.this, CAMERA)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int grantResults[]){
        switch(requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted){
                        //Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                String message = "You need to allow access for both permissions";

                                showAlertMessage(message,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                //Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }else{
                requestPermission();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void showAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        String scanResult = result.getText();

        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scannerView.resumeCameraPreview(QRActivity.this);
            }
        });

        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();
        */
        int exist = isAssetExist(scanResult);

        try{
            if(exist != 0){
                //showToast("Exist");
                Intent detailIntent = new Intent(this, DetailAssetActivity.class);
                detailIntent.putExtra("ID_ASSET_EXTRA", exist);
                startActivity(detailIntent);
            }
            else{
                showNullDialog(scanResult);
            }
        }
        catch(Exception e){
            Log.e(TAG, "handleResult exception : " + e.getMessage() );
        }
    }

    public void showNullDialog(String result){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("This QR Code has not yet registered.")
                .content("QR ID : " + result)
                .autoDismiss(true)
                .negativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        scannerView.resumeCameraPreview(QRActivity.this);
                    }
                })
                .canceledOnTouchOutside(true);

        nullDialog = builder.build();
        nullDialog.show();
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private int isAssetExist(String assetTag){
        for(AssetMutasi a : listAsset){
            String tag = a.getAsset_id();
            if (tag.equals(assetTag)){
                Log.i(TAG, "isAssetExist: true");
                return a.getId();
            }
        }
        Log.i(TAG, "isAssetExist: false");
        return 0;
    }

    private void getAsset() {
        Call<ResponseIndex<AssetMutasi>> call = userService.indexAssetMutasi(Header.auth, Header.accept);
        call.enqueue(new Callback<ResponseIndex<AssetMutasi>>() {
            @Override
            public void onResponse(Call<ResponseIndex<AssetMutasi>> call, Response<ResponseIndex<AssetMutasi>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse success: total " + response.body().getTotal());
                    listAsset = response.body().getData();
                    Log.i(TAG, "onResponse: listAsset size " + listAsset.size());
                    Log.i(TAG, "onResponse: listAsset rfid random index == " + listAsset.get(4).getAsset_rfid());
                    Log.i(TAG, "onResponse: listAsset assetid random index == " + listAsset.get(4).getAsset_id());
                    Log.i(TAG, "onResponse: listAsset name random index == " + listAsset.get(4).getName());
                }
                else{
                    Log.e(TAG, "getAsset onResponse: not successful");
                    showToast("Unable to get Asset data");
                }
            }

            @Override
            public void onFailure(Call<ResponseIndex<AssetMutasi>> call, Throwable t) {
                Log.e(TAG, "getAsset onFailure: " + t.getMessage() );
                showToast("Unable to get Asset data");
            }
        });
    }
}