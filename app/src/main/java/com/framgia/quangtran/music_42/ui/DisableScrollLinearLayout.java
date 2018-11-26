package com.framgia.quangtran.music_42.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class DisableScrollLinearLayout {
    public static DisableScrollLinearLayout getInstance() {
        DisableScrollLinearLayout disableScrollLinearLayout = new DisableScrollLinearLayout();
        return disableScrollLinearLayout;
    }

    public LinearLayoutManager DisableScroll(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        return layoutManager;
    }
}
