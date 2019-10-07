package com.markermall.cat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.markermall.cat.home.fragment.FloorImageDetailFragment;

import java.util.ArrayList;

/**
 * @Author: wuchaowen
 * @Description:
 * @Time: $date$ $time$
 **/
public class FloorImagePagerAdapter extends FragmentStatePagerAdapter {
    public ArrayList<String> fileList;

    public FloorImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
        super(fm);
        this.fileList = fileList;
    }

    @Override
    public int getCount() {
        return fileList == null ? 0 : fileList.size();
    }

    @Override
    public Fragment getItem(int position) {
        String url = fileList.get(position);
        return FloorImageDetailFragment.newInstance(url);
    }
}
