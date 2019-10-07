package com.markermall.cat.circle.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.markermall.cat.Module.common.Fragment.BaseFragment;
import com.markermall.cat.R;
import com.markermall.cat.fragment.CircleDayHotFragment;
import com.markermall.cat.fragment.CircleSearchFragment;
import com.markermall.cat.pojo.CategoryListChildDtos;
import com.markermall.cat.utils.MyLog;
import com.markermall.cat.utils.action.MyAction;
import com.markermall.cat.view.CollegeCategoryPopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangBoTian on 2019/6/11.
 * 蜜粉圈二级分类
 */

public class CircleCategoryFragment extends BaseFragment implements View.OnClickListener {
    SlidingTabLayout mTablayout;
    ViewPager mViewPager;
    private AppCompatImageButton more_category;
    private LinearLayout categoryLayout;
    private CategoryAdapter mAdapter;
    private View mView;
    private List<CategoryListChildDtos> mChilds;
    private int mTwoLevelId;
    private String mMainTitle;
    private CollegeCategoryPopWindow popWindow;
    List<String> homeColumns;
    View  searchLayout;
    public static CircleCategoryFragment newInstance(List<CategoryListChildDtos> child,int oneLevelId,String mainTitle) {
        Bundle args = new Bundle();
        args.putSerializable("mChild", (ArrayList) child);
        args.putInt("oneLevelId", oneLevelId);
        args.putString("mainTitle", mainTitle);
        CircleCategoryFragment fragment = new CircleCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_circle_category, container, false);
            initView();
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        return mView;
    }

    private void initView() {
        MyLog.i("test", "initView");
        Bundle bundle = getArguments();
        if (bundle != null) {
            mChilds = (List<CategoryListChildDtos>) bundle.getSerializable("mChild");
            mMainTitle = bundle.getString("mainTitle");
            mTwoLevelId = bundle.getInt("oneLevelId");
        }
        categoryLayout = mView.findViewById(R.id.categoryLayout);
        mTablayout = mView.findViewById(R.id.tablayout);
        mViewPager = mView.findViewById(R.id.viewpager);
        more_category = mView.findViewById(R.id.more_category);
        searchLayout = mView.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleSearchFragment.start(getActivity());
            }
        });
        more_category.setOnClickListener(this);
        homeColumns = new ArrayList<>();
        if(null != mChilds && mChilds.size() > 4){
            categoryLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mChilds.size(); i++) {
                homeColumns.add(mChilds.get(i).getTitle());
            }
        }else{
            categoryLayout.setVisibility(View.GONE);
        }
        if(mChilds !=null){
            setupViewPager(mChilds);
        }



    }




    private void setupViewPager(List<CategoryListChildDtos> data) {

        mAdapter = new CategoryAdapter(getChildFragmentManager(), data);
        mViewPager.setAdapter(mAdapter);
        mTablayout.setViewPager(mViewPager);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_category:
                if(null == popWindow){
                    popWindow = new CollegeCategoryPopWindow(getActivity(), homeColumns, new MyAction.One<Integer>() {
                        @Override
                        public void invoke(Integer arg) {
                            if(arg == -1){
                                more_category.setImageResource(R.drawable.icon_circle_category_down);
                                popWindow = null;
                            }else{
                                popWindow.dismiss();
                                mViewPager.setCurrentItem(arg);
                            }

                        }
                    });
                    popWindow.showAsDropDown(mTablayout);
                    more_category.setImageResource(R.drawable.icon_circle_category_up);
                }
                break;
        }
    }

    private class CategoryAdapter extends FragmentPagerAdapter {
        private final List<CategoryListChildDtos> homeColumns;

        public CategoryAdapter(FragmentManager fragmentManager, List<CategoryListChildDtos> datas) {
            super(fragmentManager);
            this.homeColumns = datas;
        }

        @Override
        public Fragment getItem(int position) {
            CategoryListChildDtos imageInfo = homeColumns.get(position);
            return CircleDayHotFragment.newInstance(imageInfo,mTwoLevelId,mMainTitle);
        }

        @Override
        public int getCount() {
            return homeColumns.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            MyLog.i("test", "this: " + this);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return homeColumns.get(position).getTitle();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyLog.i("test", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.i("test", "onDestroy");
    }
}
