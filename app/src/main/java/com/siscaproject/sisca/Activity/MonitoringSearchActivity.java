package com.siscaproject.sisca.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.siscaproject.sisca.Fragment.MonitoringSearchAssetFragment;
import com.siscaproject.sisca.Fragment.MonitoringSearchLocationFragment;
import com.siscaproject.sisca.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MonitoringSearchActivity extends AppCompatActivity {

    @BindView(R.id.et_search_monitoring_search)
    EditText etSearch;
    @BindView(R.id.tl_monitoring_search)
    TabLayout tabLayout;
    @BindView(R.id.vp_monitoring_search)
    ViewPager viewPager;

    private MonitoringSearchAssetFragment monitoringSearchAssetFragment;
    private MonitoringSearchLocationFragment monitoringSearchLocationFragment;
    private String searchIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_search);
        ButterKnife.bind(this);

        searchIntent = getIntent().getStringExtra("EXTRA_SEARCH");
        etSearch.setText(searchIntent);
        monitoringSearchAssetFragment = new MonitoringSearchAssetFragment();
        monitoringSearchLocationFragment = new MonitoringSearchLocationFragment();
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        monitoringSearchAssetFragment.setTextSearch(searchIntent);
        monitoringSearchLocationFragment.setTextSearch(searchIntent);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Aset");
        tabLayout.getTabAt(1).setText("Lokasi");

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                monitoringSearchAssetFragment.setTextSearch(etSearch.getText().toString());
                monitoringSearchLocationFragment.setTextSearch(etSearch.getText().toString());
                monitoringSearchAssetFragment.onLoadData();
                monitoringSearchLocationFragment.onLoadData();
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return monitoringSearchAssetFragment;
                default:
                    return monitoringSearchLocationFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @OnClick(R.id.cv_back_monitoring_search) void cvBackOnClick(){
        onBackPressed();
    }

    @OnClick(R.id.cv_search_submit_monitoring_search) void cvSearchSubmitOnClick(){
        if (etSearch.getText().toString().isEmpty())
            Toast.makeText(this, "Harap isi kolom dahulu", Toast.LENGTH_SHORT).show();
        else{

        }
    }
}
