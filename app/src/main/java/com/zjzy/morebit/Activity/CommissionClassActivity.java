package com.zjzy.morebit.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.CommissonFragment;
import com.zjzy.morebit.goodsvideo.VideoFragment;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/*
 *
 * 高佣分类页面
 * */
public class CommissionClassActivity extends BaseActivity {

    private XTabLayout tabLayout;
    private ViewPager video_pager;
    private List<CommissonFragment> mFragments;

    private NewsPagerAdapter mAdapter;

    private List<VideoClassBean> mNewsTypeList = new ArrayList<>();

    private List<String> title;
    private TextView txt_head_title;
    private LinearLayout btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_video_class);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();
        initData();
        initView();
        initViewPager();


    }



    private void initView() {

        tabLayout = (XTabLayout) findViewById(R.id.xTablayout);
        video_pager = (ViewPager) findViewById(R.id.video_pager);
        txt_head_title=(TextView)findViewById(R.id.txt_head_title);
        txt_head_title.setText("定向高佣专区");
        txt_head_title.setTextSize(18);
          btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    //测试数据
    private void initData() {

        getCommissionClass(this)
                .compose(RxUtils.<BaseResponse<List<VideoClassBean>>>switchSchedulers())
                .compose(this.<BaseResponse<List<VideoClassBean>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new DataObserver<List<VideoClassBean>>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onVideoClassError(errorMsg);
                    }

                    @Override
                    protected void onSuccess(List<VideoClassBean> data) {
                       onVideoClassSuccess(data);

                    }

                });
    }

    private void onVideoClassError(String errorMsg) {

    }

    private void initViewPager() {


    }


    private void onVideoClassSuccess(List<VideoClassBean> shopGoodInfo) {
        mNewsTypeList.addAll(shopGoodInfo);
        title = new ArrayList<>();
        for (int i = 0; i < shopGoodInfo.size(); i++) {
            title.add(shopGoodInfo.get(i).getTabName());
        }
        mFragments = new ArrayList<>();
        for (int i = 0; i < shopGoodInfo.size(); i++) {
            CommissonFragment fragment = null;
            fragment = new CommissonFragment();
            mFragments.add(fragment.newInstance(shopGoodInfo.get(i).getTabName(),shopGoodInfo.get(i).getTabNo()));
        }
        mAdapter = new NewsPagerAdapter(getSupportFragmentManager(), mFragments, mNewsTypeList,title);
        video_pager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(video_pager);
        video_pager.setOffscreenPageLimit(title.size());

    }


    public class NewsPagerAdapter extends FragmentPagerAdapter {
        private List<CommissonFragment> mFragments;
        private List<VideoClassBean> mNewsTypes;
        private List<String> title;
        public NewsPagerAdapter(FragmentManager fm, List<CommissonFragment> fragments, List<VideoClassBean> mewsTypes, List<String> stitle) {
            super(fm);
            mFragments = fragments;
            mNewsTypes = mewsTypes;
            title=stitle;
        }

        @Override
        public Fragment getItem(int position) {
            VideoClassBean videoClassBean = mNewsTypes.get(position);
            return CommissonFragment.newInstance(mNewsTypes.get(position).getTabName(),mNewsTypes.get(position).getTabNo());
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0: mFragments.size();
        }


        //    为Tabayout设置主题名称
        @Override
        public CharSequence getPageTitle(int position) {
            return title == null ? "" + position : title.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);

        }


    }

    /**
     * 获取抖货分类条目
     *
     * @return
     */
    public Observable<BaseResponse<List<VideoClassBean>>> getCommissionClass(BaseActivity rxActivity) {

        return RxHttp.getInstance().getGoodsService()
                .getCommissionClass()
                .compose(RxUtils.<BaseResponse<List<VideoClassBean>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<VideoClassBean>>>bindToLifecycle());
    }

}

