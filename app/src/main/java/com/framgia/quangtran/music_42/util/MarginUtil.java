package com.framgia.quangtran.music_42.util;

import android.view.View;
import android.view.ViewGroup;

public class MarginUtil {
    public static MarginUtil getInstance() {
        MarginUtil marginUtil = new MarginUtil();
        return marginUtil;
    }

    public void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
