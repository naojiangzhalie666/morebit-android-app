package com.zjzy.morebit.main.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.zjzy.morebit.App;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.pojo.RankingTitleBean;
import com.zjzy.morebit.pojo.pddjd.PddJdTitleTypeItem;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 首页分类-拼多多商品列表
 */
public class PddChildFragment extends BaseMainFragmeng {

    @BindView(R.id.tab)
    SlidingTabLayout tablayout;
    @BindView(R.id.cv_tab)
    CardView cv_tab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    boolean isUserHint =true;
    private ArrayList<CirclePagerBaen> mCirclePagerBaens = new ArrayList<>();
    private View mView;
    private int mPushType;

    /**
     * 1、实时排行/2、今日排行
     *
     * @param type
     * @return
     */
    public static PddChildFragment newInstance(int type) {
        PddChildFragment rankingChildFragment = new PddChildFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.pushType, type);
        rankingChildFragment.setArguments(args);
        return rankingChildFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_pdd_child, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPushType = getArguments().getInt(C.Extras.pushType);
//        if (mPushType == 1) {
            initView(mView);
//        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null&&mPushType == 2) {
            initView(mView);
            isUserHint = false;
        }

    }
    //初始化拼多多标题。
    private List<PddJdTitleTypeItem>  initPddTitle(){
        ArrayList<PddJdTitleTypeItem> data = new ArrayList<>();
        PddJdTitleTypeItem item = new PddJdTitleTypeItem();
        item.setTitle("男装");
        item.setType(2);
        item.setCatId(239);
        data.add(item);

        PddJdTitleTypeItem item1 = new PddJdTitleTypeItem();
        item1.setTitle("女装/女士精品");
        item1.setType(2);
        item1.setCatId(8439);
        data.add(item1);

        PddJdTitleTypeItem item2 = new PddJdTitleTypeItem();
        item2.setTitle("办公用品");
        item2.setType(2);
        item2.setCatId(2603);
        data.add(item2);

        PddJdTitleTypeItem item3 = new PddJdTitleTypeItem();
        item3.setTitle("3c数码配件");
        item3.setType(2);
        item3.setCatId(2933);
        data.add(item3);

        PddJdTitleTypeItem item4 = new PddJdTitleTypeItem();
        item4.setTitle("男鞋");
        item4.setType(2);
        item4.setCatId(8508);
        data.add(item4);

        PddJdTitleTypeItem item5 = new PddJdTitleTypeItem();
        item5.setTitle("女鞋");
        item5.setType(2);
        item5.setCatId(8509);
        data.add(item5);

        PddJdTitleTypeItem item6 = new PddJdTitleTypeItem();
        item6.setTitle("箱包皮具/女包/男包");
        item6.setType(2);
        item6.setCatId(8538);
        data.add(item6);

        PddJdTitleTypeItem item7 = new PddJdTitleTypeItem();
        item7.setTitle("内衣裤袜");
        item7.setType(2);
        item7.setCatId(8583);
        data.add(item7);

        PddJdTitleTypeItem item8 = new PddJdTitleTypeItem();
        item8.setTitle("家具");
        item8.setType(2);
        item8.setCatId(9324);
        data.add(item8);

        PddJdTitleTypeItem item9 = new PddJdTitleTypeItem();
        item9.setTitle("运动");
        item9.setType(2);
        item9.setCatId(11685);
        data.add(item9);

        PddJdTitleTypeItem item10 = new PddJdTitleTypeItem();
        item10.setTitle("美妆");
        item10.setType(2);
        item10.setCatId(18482);
        data.add(item10);

        PddJdTitleTypeItem item11 = new PddJdTitleTypeItem();
        item11.setTitle("零食");
        item11.setType(2);
        item11.setCatId(6398);
        data.add(item11);

        PddJdTitleTypeItem item12 = new PddJdTitleTypeItem();
        item12.setTitle("汽车配件");
        item12.setType(2);
        item12.setCatId(7639);
        data.add(item12);

        PddJdTitleTypeItem item13 = new PddJdTitleTypeItem();
        item13.setTitle("生鲜");
        item13.setType(2);
        item13.setCatId(8172);
        data.add(item13);

        PddJdTitleTypeItem item14 = new PddJdTitleTypeItem();
        item14.setTitle("居家日用");
        item14.setType(2);
        item14.setCatId(16989);
        data.add(item14);

        PddJdTitleTypeItem item15 = new PddJdTitleTypeItem();
        item15.setTitle("清洁用品");
        item15.setType(2);
        item15.setCatId(17285);
        data.add(item15);

        PddJdTitleTypeItem item16 = new PddJdTitleTypeItem();
        item16.setTitle("生活电器");
        item16.setType(2);
        item16.setCatId(6128);
        data.add(item16);

        PddJdTitleTypeItem item17 = new PddJdTitleTypeItem();
        item17.setTitle("医药健康");
        item17.setType(2);
        item17.setCatId(13176);
        data.add(item17);

        PddJdTitleTypeItem item18 = new PddJdTitleTypeItem();
        item18.setTitle("童装");
        item18.setType(2);
        item18.setCatId(14966);
        data.add(item18);

        PddJdTitleTypeItem item19 = new PddJdTitleTypeItem();
        item19.setTitle("孕产妇用品");
        item19.setType(2);
        item19.setCatId(15356);
        data.add(item19);

        PddJdTitleTypeItem item20 = new PddJdTitleTypeItem();
        item20.setTitle("玩具");
        item20.setType(2);
        item20.setCatId(15083);
        data.add(item20);
        return data;
    }
    /**
     * 初始化界面
     */
    private void initView(View view) {
        App.getACache().put(C.sp.PDD_CATEGORY, (ArrayList) initPddTitle());
        List<PddJdTitleTypeItem> data = (List<PddJdTitleTypeItem>) App.getACache().getAsObject(C.sp.PDD_CATEGORY);
        if (data != null && data.size() != 0) {
            initTab(data);
        } else {
            cv_tab.setVisibility(View.GONE);
        }

    }

    private void initTab(List<PddJdTitleTypeItem> data) {
        cv_tab.setVisibility(View.VISIBLE);
        for (int i = 0; i < data.size(); i++) {
            PddJdTitleTypeItem titleTypeItem = data.get(i);
            PddListFragment pddListFragment = PddListFragment.newInstance(titleTypeItem);
            CirclePagerBaen baen = new CirclePagerBaen(pddListFragment, titleTypeItem.getTitle());
            mCirclePagerBaens.add(baen);
        }
        viewPager.setAdapter(new ChannelAdapter(getChildFragmentManager()));
        tablayout.setViewPager(viewPager);
    }

    private class ChannelAdapter extends FragmentStatePagerAdapter {

        public ChannelAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mCirclePagerBaens.get(position).title;
        }

        @Override
        public Fragment getItem(int position) {
            return mCirclePagerBaens.get(position).mFragment;
        }

        @Override
        public int getCount() {
            return mCirclePagerBaens.size();
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


}
