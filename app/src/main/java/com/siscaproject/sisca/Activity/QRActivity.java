package com.siscaproject.sisca.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.siscaproject.sisca.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QRActivity extends AppCompatActivity {

    @BindView(R.id.surface_camera) SurfaceView surface_camera;
    @BindView(R.id.tv_qr_result) TextView tv_qr_result;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    private static final int requestCameraPermissionID = 1001;
    private static final String TAG = "QRActivity";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case requestCameraPermissionID:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED){
                        return;
                    }

                    try{
                        cameraSource.start(surface_camera.getHolder());
                        Log.d(TAG, "onRequestPermissionsResult: camera started");
                    }catch(IOException e){
                        Log.e(TAG, "surfaceCreated: " + e.getMessage());
                    }
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        ButterKnife.bind(this);

        addCameraEvent();
    }

    private void addCameraEvent(){
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(330, 250)
                .build();

        surface_camera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
                    // Request permission
                    ActivityCompat.requestPermissions(QRActivity.this,
                            new String[]{Manifest.permission.CAMERA}, requestCameraPermissionID);
                    return;
                }
                try{
                    cameraSource.start(surface_camera.getHolder());
                    Log.d(TAG, "addCameraEvent: camera started");
                }catch(IOException e){
                    Log.e(TAG, "surfaceCreated: " + e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
                Log.d(TAG, "addCameraEvent: camera stopped");
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Do nothing
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if(qrCodes.size() != 0){
                    tv_qr_result.post(new Runnable() {
                        @Override
                        public void run() {
                            // Create vibration
                            //Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            //vibrator.vibrate(100);
                            //Log.d(TAG, "receiveDetection: vibrate");
                            tv_qr_result.setText(qrCodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
