package com.siscaproject.sisca.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.siscaproject.sisca.Adapter.SearchResultAdapter;
import com.siscaproject.sisca.Model.Category;
import com.siscaproject.sisca.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.btn_clear_search) ImageButton btnClearSearch;

    @BindView(R.id.lyt_no_results) LinearLayout lytNoResults;
    @BindView(R.id.pb_search_results) ProgressBar pbSearchResults;

    @BindView(R.id.rv_search_result) RecyclerView rvResults;

    private SearchResultAdapter rvAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        lytNoResults.setVisibility(View.VISIBLE);

        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvResults.setHasFixedSize(true);

        rvAdapter = new SearchResultAdapter(getContext(), dummyData());
        rvResults.setAdapter(rvAdapter);

        rvResults.setVisibility(View.GONE);

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    //Toast.makeText(getContext(), etSearch.getText(), Toast.LENGTH_SHORT).show();
                    doSearch();
                    try{
                        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    } catch(NullPointerException e){
                        Log.e(TAG, "onKey : " + e.getCause().toString());
                    }
                    return true;
                }

                return false;
            }
        });

        return view;
    }

    @OnClick(R.id.btn_clear_search)
    public void onClick(){
        etSearch.setText(null);
    }

    private void doSearch(){
        try{
            pbSearchResults.setVisibility(View.VISIBLE);
            lytNoResults.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pbSearchResults.setVisibility(View.GONE);
                    lytNoResults.setVisibility(View.GONE);
                    rvResults.setVisibility(View.VISIBLE);
                }
            }, 1500);
        }catch (Exception e){
            Log.e(TAG, "doSearch : " + e.getMessage());
            // Do Nothing
        }
    }


    public ArrayList<Category> dummyData(){
        ArrayList<Category> listData = new ArrayList<>();

        listData.add(new Category("Object 1", "12345"));
        listData.add(new Category("Object 2", "12345"));
        listData.add(new Category("Object 3", "12345"));
        listData.add(new Category("Object 4", "12345"));
        listData.add(new Category("Object 5", "12345"));
        listData.add(new Category("Object 6", "12345"));
        listData.add(new Category("Object 7", "12345"));
        listData.add(new Category("Object 8", "12345"));
        listData.add(new Category("Object 9", "12345"));
        listData.add(new Category("Object 10", "12345"));

        return listData;
    }
}
