package com.framgia.quangtran.music_42.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.ProgressBar;

public abstract class LoadMoreAbstract extends AppCompatActivity {
    protected boolean mIsScrolling = false;
    protected RecyclerView mRecyclerView;
    protected ProgressBar mProgressBar;
    protected int mCurrentItem, mTotalItem, mScrollOutItem;
    protected LinearLayoutManager mLinearLayoutManager;

    protected void setLoadMore() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true;
                }
                mCurrentItem = mLinearLayoutManager.getChildCount();
                mTotalItem = mLinearLayoutManager.getItemCount();
                mScrollOutItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                if (mIsScrolling && (mCurrentItem + mScrollOutItem == mTotalItem)) {
                    loadMoreData();
                }
            }
        });
    }

    public abstract void loadMoreData();

    public abstract void initViewLoadMore();
}
