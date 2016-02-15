package com.wildnettechnologies.mapit.mapit.routeModule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.wildnettechnologies.mapit.mapit.R;
import com.wildnettechnologies.mapit.mapit.routeModule.adapter.InitSearchRowAdapter;
import com.wildnettechnologies.mapit.mapit.routeModule.webSerice.SearchDataWebService;

import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InitialSearchActivity extends AppCompatActivity {

    SearchDataWebService searchWebSearch;

    @Bind(R.id.edit_text_header)
    EditText searchEditText;
    @Bind(R.id.init_search_result_recycler_view)
    RecyclerView searchResultView;
    @Bind(R.id.current_location_icon)
    ImageView mainSearch;

    boolean searchingOn = true;
    public static ArrayList<SearchResponse> searchList;
    InitSearchRowAdapter searchRowAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_search);

        ButterKnife.bind(this);
        setTextWatcher();
    }

    private void setTextWatcher() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (RouteActivity.currentCoordinates != null) {
                    if (searchingOn) {
                        if (searchWebSearch != null) {
                            searchWebSearch.cancel(true);
                            searchWebSearch = new SearchDataWebService(InitialSearchActivity.this);
                            try {
                                String encodedString = URLEncoder.encode(searchEditText.getText().toString().trim(), "UTF-8");
                                searchWebSearch.execute(encodedString);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            searchWebSearch = new SearchDataWebService(InitialSearchActivity.this);
                            try {
                                String encodedString = URLEncoder.encode(searchEditText.getText().toString().trim(), "UTF-8");
                                searchWebSearch.execute(encodedString);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {

                    }
                }
            }
        });
    }


    public void setSearchList(ArrayList<SearchResponse> searchList) {
        if (searchList != null) {
            this.searchList = searchList;
            searchResultView.setVisibility(View.VISIBLE);

            if (!searchList.isEmpty()) {
                searchRowAdapter = new InitSearchRowAdapter(this);
                searchRowAdapter.setSearchList(searchList);
                searchResultView.setLayoutManager(new LinearLayoutManager(this));
                searchResultView.setAdapter(searchRowAdapter);
                searchRowAdapter.notifyDataSetChanged();
            }
            searchingOn = false;
        } else {
        }

    }


    @OnClick(R.id.edit_text_header)
    void onClickingSearchEditView(final View view) {
        searchingOn = true;
    }

}