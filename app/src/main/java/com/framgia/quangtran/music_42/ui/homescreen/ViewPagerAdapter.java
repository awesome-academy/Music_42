package com.framgia.quangtran.music_42.ui.homescreen;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.framgia.quangtran.music_42.ui.homescreen.fragments.HomeFragment;
import com.framgia.quangtran.music_42.ui.homescreen.fragments.PersonalFragment;
import com.framgia.quangtran.music_42.ui.homescreen.fragments.SettingFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final String HOME = "HOME";
    private static final String PERSONAL = "PERSONAL";
    private static final String SETTING = "SETTING";
    private static final int TOTAL_TABS = 3;
    private static final int TAB_HOME = 0;
    private static final int TAB_PERSONAL = 1;
    private static final int TAB_SETTING = 2;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case TAB_HOME:
                return new HomeFragment();
            case TAB_PERSONAL:
                return new PersonalFragment();
            case TAB_SETTING:
                return new SettingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TOTAL_TABS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case TAB_HOME:
                title = HOME;
                break;
            case TAB_PERSONAL:
                title = PERSONAL;
                break;
            case TAB_SETTING:
                title = SETTING;
                break;
        }
        return title;
    }
}
