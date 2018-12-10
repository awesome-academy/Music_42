package com.framgia.quangtran.music_42.ui.search;

import android.os.Bundle;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.ui.LoadMoreAbstract;

public class SearchActivity extends LoadMoreAbstract {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViewLoadMore();
    }

    @Override
    public void loadMoreData() {
    }

    @Override
    public void initViewLoadMore() {
    }
}
