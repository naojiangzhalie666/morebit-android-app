package com.zjzy.morebit.circle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;
/**
 * Created by JerryHo on 2019/3/15
 * Description:
 */

public class CollegeFragmentPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public CollegeFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments) {
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
