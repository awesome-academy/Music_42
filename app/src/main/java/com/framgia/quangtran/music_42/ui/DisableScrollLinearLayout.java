package com.framgia.quangtran.music_42.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class DisableScrollLinearLayout extends LinearLayoutManager{
    public DisableScrollLinearLayout(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
