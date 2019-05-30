package com.siscaproject.sisca.Activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.siscaproject.sisca.Adapter.PindahLokasiAdapter;
import com.siscaproject.sisca.Adapter.PindahPicAdapter;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailMutationActivity extends AppCompatActivity {

    private static final String TAG = "DetailMutationActivity";

    @BindView(R.id.pv_mutation_detail) ProgressView progressView;
    @BindView(R.id.cv_back_mutation_detail) CardView back;
    @BindView(R.id.iv_photo_mutation_detail) ImageView ivPhoto;
    @BindView(R.id.tv_id_mutation_detail) TextView tvId;
    @BindView(R.id.tv_desc_mutation_detail) TextView tvDesc;
    @BindView(R.id.et_pindah_lokasi) EditText etPindahLokasi;
    @BindView(R.id.et_pindah_pic) EditText etPindahPic;
    @BindView(R.id.btn_pindah_lokasi) CardView btnPindahLokasi;
    @BindView(R.id.btn_pindah_pic) CardView btnPindahPic;
    @BindView(R.id.et_notes_mutation_detail) EditText etNotes;
    @BindView(R.id.btn_submit_mutation_detail) Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mutation);

        ButterKnife.bind(this);

        progressView.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_pindah_lokasi)
    public void dialogPindahLokasi(View view){
        if(view.getId() == R.id.btn_pindah_lokasi){
            showPindahLokasiDialog();
        }
    }

    @OnClick(R.id.btn_pindah_pic)
    public void dialogPindahPic(View view){
        if(view.getId() == R.id.btn_pindah_pic){
            showPindahPicDialog();
        }
    }

    @OnClick(R.id.btn_submit_mutation_detail)
    public void submit(View view){
        Toast.makeText(this, "Data aset berhasil dipindahkan.", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> dataLokasi(){
        ArrayList<String> data = new ArrayList<>();
        data.add("C102");
        data.add("C103");
        data.add("C201");
        data.add("C202");
        data.add("B101");
        data.add("B102");

        return data;
    }

    private ArrayList<String> dataPic(){
        ArrayList<String> data = new ArrayList<>();
        data.add("Riski Novanda");
        data.add("Susi Susanto");
        data.add("Abdul Kadir");
        data.add("Roberto Karlos");
        data.add("Ivan Gunawan");
        data.add("Imam Agus Faisal");
        data.add("Muhammad Idam");

        return data;
    }

    private void showPindahLokasiDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pindah_lokasi);
        dialog.setCancelable(true);

        PindahLokasiAdapter.OnItemClickListener listener = new PindahLokasiAdapter.OnItemClickListener() {
            @Override
            public void putLokasi(String lokasi) {
                etPindahLokasi.setText(lokasi);
                dialog.dismiss();
            }
        };

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button btnBatal = dialog.findViewById(R.id.btn_batal);
        final Button btnPilih = dialog.findViewById(R.id.btn_pilih);
        final RecyclerView recyclerView = dialog.findViewById(R.id.rv_list_lokasi);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PindahLokasiAdapter adapter = new PindahLokasiAdapter(dataLokasi(), getApplicationContext(), listener);
        recyclerView.setAdapter(adapter);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showPindahPicDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pindah_pic);
        dialog.setCancelable(true);

        PindahPicAdapter.OnItemClickListener listener = new PindahPicAdapter.OnItemClickListener() {
            @Override
            public void putPic(String pic) {
                etPindahPic.setText(pic);
                dialog.dismiss();
            }
        };

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button btnBatal = dialog.findViewById(R.id.btn_batal);
        final Button btnPilih = dialog.findViewById(R.id.btn_pilih);
        final RecyclerView recyclerView = dialog.findViewById(R.id.rv_list_pic);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PindahPicAdapter adapter = new PindahPicAdapter(dataPic(), getApplicationContext(), listener);
        recyclerView.setAdapter(adapter);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
