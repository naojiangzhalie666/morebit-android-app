package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.CircleBusinessFragment;
import com.zjzy.morebit.circle.ui.CircleCategoryFragment;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.main.ui.fragment.HomeCollegeFragment;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CategoryListChildDtos;
import com.zjzy.morebit.pojo.CategoryListDtos;
import com.zjzy.morebit.pojo.request.RequestReleaseCategory;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.zjzy.morebit.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 首页分类-发圈
 */
public class CircleFragment extends BaseMainFragmeng {
    @BindView(R.id.xablayout)
    XTabLayout tab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
//    @BindView(R.id.status_bar)
//    View status_bar;


    List<BaseFragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private View mView;
    private boolean isVisible;
    private boolean isLoadTitleData;





    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
        } else {
            GSYVideoManager.releaseAllVideos();
        }

        if (isVisibleToUser && mFragments.size() == 0 && mView != null) {
            initData();
            LinearLayout l1 = (LinearLayout) tab.getChildAt(0);
            MyLog.i("test", "(l1).getChildAt(1): " + (l1).getChildAt(1));

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        StatusBarUtils.addTranslucentColorView(getActivity(), Color.WHITE, 0);
        ImmersionBar.with(CircleFragment.this)
                .navigationBarColorInt(Color.WHITE)
                .navigationBarDarkIcon(true)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .statusBarColorInt(Color.TRANSPARENT)
                .init();
        mView = inflater.inflate(R.layout.fragment_circle, container, false);
        ButterKnife.bind(this, mView);
        initView(mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (isVisible && mFragments.size() == 0) {
            initData();
        }
    }

    private void initData() {
        if (isLoadTitleData) {
            return;
        }
        RequestReleaseCategory requestReleaseCategory = new RequestReleaseCategory();
        requestReleaseCategory.setType(1);
        RxHttp.getInstance().getCommonService().getShareRangCategory(requestReleaseCategory)
                .compose(RxUtils.<BaseResponse<List<CategoryListDtos>>>switchSchedulers())
                .compose(this.<BaseResponse<List<CategoryListDtos>>>bindToLifecycle())
                .subscribe(new DataObserver<List<CategoryListDtos>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        super.onError(errorMsg, errCode);
                        List<CategoryListDtos> data = (List<CategoryListDtos>) App.getACache().getAsObject(C.sp.SHARE_RANG_DATA);
                        if (C.requestCode.dataListEmpty.equals(errCode) || C.requestCode.dataNull.equals(errCode)) {
                            App.getACache().remove(C.sp.SHARE_RANG_DATA);
                            setupViewPager(new ArrayList<CategoryListDtos>());
                        } else {
                            if (data != null) {
                                setupViewPager(data);
                            }
                        }

                    }

                    @Override
                    protected void onSuccess(List<CategoryListDtos> data) {
                        setupViewPager(data);
                        App.getACache().put(C.sp.SHARE_RANG_DATA, (ArrayList<CategoryListDtos>) data);
                    }
                });
        isLoadTitleData = true;
    }

    private void setupViewPager(List<CategoryListDtos> data) {
        if (mFragments.size() != 0) {
            return;
        }
        Integer size = data.size();
        mTitles = new String[size+1];

        for (int i = 0; i < size; i++) {
            List<CategoryListChildDtos> categoryDtos = data.get(i).getChild();

//            String title = data.get(i).getTitle();
//            if (data.get(i).getChild() == null || data.get(i).getChild().size() == 0) {
//
//                mFragments.add(CircleDayHotFragment.newInstance(categoryDtos,data.get(i).getTitle()));
//            } else {
//                mFragments.add(CircleCategoryFragment.newInstance(data.get(i).getChild(), data.get(i).getId(), data.get(i).getTitle()));
//            }

            if(i==0){
                mFragments.add(GoodDailyFragment.newInstance(categoryDtos,data.get(i).getId(), Integer.parseInt(data.get(i).getType()),i));
            }else{
                mFragments.add(NewMaterialFragment.newInstance(categoryDtos,data.get(i).getId(), Integer.parseInt(data.get(i).getType()),i));
            }

            mTitles[i] = data.get(i).getTitle();
        }
//        mTitles[size]="商学院";
//        //mFragments.add(new HomeCollegeFragment());
//        mFragments.add(new CircleBusinessFragment());//跳转商学院二级页面
        viewPager.setAdapter(new ChannelAdapter(getChildFragmentManager()));
        tab.setupWithViewPager(viewPager);
    }

    /**
     * 初始化界面
     */
    private void initView(View view) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //获取窗口区域
//            Window window = getActivity().getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//                window.setStatusBarColor(Color.WHITE);
//
//                //设置显示为白色背景，黑色字体
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }


//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //处理全屏显示
//            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
//            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
//            status_bar.setLayoutParams(viewParams);
//
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getActivity().getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        //避免滑动切换界面卡顿
//        viewPager .setOffscreenPageLimit(3);



    }


    public void swithchCollegeFragment() {
        if (null != viewPager) {
            viewPager.setCurrentItem(2);
        }
    }


    private class ChannelAdapter extends FragmentPagerAdapter {


        public ChannelAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            MyLog.i("test", "this: " + this);
        }
    }


    public class CirclePagerBaen {
        public Fragment mFragment;
        public String title;

        public CirclePagerBaen(Fragment fragment, String title) {
            mFragment = fragment;
            this.title = title;
        }

    }






    @Override
    protected void onVisible() {
        super.onVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoadTitleData = false;
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();

    }
}
