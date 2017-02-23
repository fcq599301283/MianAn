package com.mianan.Self;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class FragPagerAdpter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public FragPagerAdpter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
