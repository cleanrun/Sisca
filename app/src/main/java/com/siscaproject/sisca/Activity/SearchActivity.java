package com.siscaproject.sisca.Activity;

import android.content.Intent;
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

import com.siscaproject.sisca.Fragment.SearchAssetFragment;
import com.siscaproject.sisca.Fragment.SearchLocationFragment;
import com.siscaproject.sisca.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity{
    @BindView(R.id.et_search_home_search) EditText etSearch;
    @BindView(R.id.tl_home_search) TabLayout tabLayout;
    @BindView(R.id.vp_home_search) ViewPager viewPager;

    private SearchAssetFragment homeSearchAssetFragment;
    private SearchLocationFragment homeSeachLocationFragment;
    private String searchIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        searchIntent = getIntent().getStringExtra("EXTRA_SEARCH");
        etSearch.setText(searchIntent);
        homeSearchAssetFragment = new SearchAssetFragment();
        homeSeachLocationFragment = new SearchLocationFragment();
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        homeSearchAssetFragment.setTextSearch(searchIntent);
        homeSeachLocationFragment.setTextSearch(searchIntent);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Aset");
        tabLayout.getTabAt(1).setText("Lokasi");

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                homeSearchAssetFragment.setTextSearch(etSearch.getText().toString());
                homeSeachLocationFragment.setTextSearch(etSearch.getText().toString());
                homeSearchAssetFragment.onLoadData();
                homeSeachLocationFragment.onLoadData();
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
                    return homeSearchAssetFragment;
                default:
                    return homeSeachLocationFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @OnClick(R.id.cv_back_home_search) void cvBackOnClick(){
        onBackPressed();
    }

    @OnClick(R.id.cv_search_submit_home_search) void cvSearchSubmitOnClick(){
        if (etSearch.getText().toString().isEmpty())
            Toast.makeText(this, "Harap isi kolom dahulu", Toast.LENGTH_SHORT).show();
        else{

        }
    }
}
