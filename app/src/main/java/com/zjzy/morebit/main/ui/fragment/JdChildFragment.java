package com.zjzy.morebit.main.ui.fragment;

import android.content.Intent;
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
import android.widget.ImageView;

import com.zjzy.morebit.Activity.SearchActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.pddjd.PddJdTitleTypeItem;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.SwipeDirectionDetector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 首页分类-京东商品列表
 */
public class JdChildFragment extends BaseMainFragmeng {

    @BindView(R.id.xTablayout)
    XTabLayout tablayout;
    @BindView(R.id.status_bar)
    View status_bar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_right_search)
    ImageView iv_right_search;

    boolean isUserHint =true;
    private View mView;
    private int mPushType;
    private ChannelAdapter mChannelAdapter;
    private SwipeDirectionDetector swipeDirectionDetector;
    private int currentViewPagerPosition = 0;


    /**
     *jd商品页面
     *
     * @param type
     * @return
     */
    public static JdChildFragment newInstance(int type) {
        JdChildFragment rankingChildFragment = new JdChildFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.pushType, type);
        rankingChildFragment.setArguments(args);
        return rankingChildFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_jd_child, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPushType = getArguments().getInt(C.Extras.pushType);
        initView(mView);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null) {
            initView(mView);
            isUserHint = false;
        }

    }


    /**
     * 初始化界面
     */
    private void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);

        }

       // App.getACache().put(C.sp.PDD_CATEGORY, (ArrayList) initPddTitle());
      //  List<PddJdTitleTypeItem> data = (List<PddJdTitleTypeItem>) App.getACache().getAsObject(C.sp.PDD_CATEGORY);

        RxHttp.getInstance().getCommonService().getJdTitle()//获取京东栏目
                .compose(RxUtils.<BaseResponse<List<PddJdTitleTypeItem>>>switchSchedulers())
                .compose(this.<BaseResponse<List<PddJdTitleTypeItem>>>bindToLifecycle())
                .subscribe(new DataObserver<List<PddJdTitleTypeItem>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {

                    }

                    @Override
                    protected void onSuccess(List<PddJdTitleTypeItem> data) {
                        if (data != null) {
                            if (data != null && data.size() != 0) {
                                initTab(data);
                            }

                        }
                    }
                });




        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        iv_right_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

    }



    private void setupViewPager(final List<PddJdTitleTypeItem> homeColumns) {

        mChannelAdapter.setHomeColumns(homeColumns);
        mChannelAdapter.notifyDataSetChanged();

        tablayout.setupWithViewPager(viewPager);


    }

    private void initTab(List<PddJdTitleTypeItem> data) {
        mChannelAdapter = new ChannelAdapter(getChildFragmentManager());
        swipeDirectionDetector = new SwipeDirectionDetector();
        viewPager.setAdapter(mChannelAdapter);
        setupViewPager(data);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                swipeDirectionDetector.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                swipeDirectionDetector.onPageSelected(position);
                currentViewPagerPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                swipeDirectionDetector.onPageScrollStateChanged(state);
                MyLog.d("addOnPageChangeListener", " onPageScrollStateChanged  state = " + state);
            }
        });
    }

    private class ChannelAdapter extends FragmentPagerAdapter {
        private List<PddJdTitleTypeItem> mHomeColumns = new ArrayList<>();

        public ChannelAdapter(FragmentManager fm) {
            super(fm);

        }
        public void setHomeColumns(List<PddJdTitleTypeItem> homeColumns) {
            mHomeColumns.clear();
            if (homeColumns == null && homeColumns.size() == 0) {
                return;
            }
            mHomeColumns.addAll(homeColumns);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            PddJdTitleTypeItem homeColumn = mHomeColumns.get(position);
            return homeColumn.getEliteName();

        }

        @Override
        public Fragment getItem(int position) {
            return JdongListFragment.newInstance(mHomeColumns.get(position));
        }

        @Override
        public int getCount() {
            return mHomeColumns.size();
        }
    }

}
