package com.zjzy.morebit.info.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.home.fragment.HomeOtherFragment;
import com.zjzy.morebit.home.fragment.IconFragment;
import com.zjzy.morebit.home.fragment.LimiteFragment;
import com.zjzy.morebit.info.adapter.MsgEarningsAdapter;
import com.zjzy.morebit.info.contract.MsgContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.info.presenter.MsgPresenter;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.pojo.EarningsMsg;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.view.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fengrs on 2018/12/4.
 * 收益消息
 */

public class MsgEarningsFragment extends BaseFragment implements View.OnClickListener {
    private static final int REQUEST_COUNT = 10;
    private XTabLayout xTablayout;
    private ViewPager viewPager;
    private List<String> title = new ArrayList<>();
    private List<MsgEarningsFragment2> mfragment = new ArrayList<>();
    private TextView txt_head_title;
    private LinearLayout btn_back;

    public static MsgEarningsFragment newInstance() {
        Bundle args = new Bundle();
        MsgEarningsFragment fragment = new MsgEarningsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_msg_list2, null);
        initView(view);
        return view;
    }


    private void initView(View view) {
        xTablayout = view.findViewById(R.id.xablayout);
        viewPager = view.findViewById(R.id.viewPager);
        txt_head_title = (TextView) view.findViewById(R.id.txt_head_title);
        txt_head_title.setText("收益通知");
        txt_head_title.setTextSize(18);
        txt_head_title.getPaint().setFakeBoldText(true);
        btn_back = (LinearLayout) view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        title.add("普通订单");
        title.add("优选订单");

        for (int i = 0; i < title.size(); i++) {
            MsgEarningsFragment2 fragment = null;
            fragment = new MsgEarningsFragment2();
            mfragment.add(fragment.newInstance(i+1));
        }
        NewsPagerAdapter mAdapter = new NewsPagerAdapter(getActivity().getSupportFragmentManager(), mfragment, title);
        viewPager.setAdapter(mAdapter);
        xTablayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getActivity().finish();
                break;
        }
    }

    public class NewsPagerAdapter extends FragmentPagerAdapter {
        private List<MsgEarningsFragment2> mFragments;
        private List<String> title;

        public NewsPagerAdapter(FragmentManager fm, List<MsgEarningsFragment2> fragments, List<String> stitle) {
            super(fm);
            mFragments = fragments;
            title = stitle;
        }

        @Override
        public Fragment getItem(int position) {
            return MsgEarningsFragment2.newInstance(position+1);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }


        //    为Tabayout设置主题名称
        @Override
        public CharSequence getPageTitle(int position) {
            return title == null ? "" + position : title.get(position);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {

        }
    }


    public static List<EarningsMsg> handlerData(List<EarningsMsg> data) {
        List<EarningsMsg> earningsMsgList = new ArrayList<>();
        String lastTime = "";
        if (null != data && data.size() > 0) {
            //通过时间判断是今天的商品不显示时间分线
            for (int i = 0; i < data.size(); i++) {
                EarningsMsg item = data.get(i);
                if (!lastTime.equals(DateTimeUtils.getYMDTime(item.getCreateTime()))) {
                    //lastTime = DateTimeUtils.getDatetoString(item.getSendTime());
                    String isTodayTime = DateTimeUtils.getDatetoString(item.getCreateTime());
                    if (!isTodayTime.equals("今天")) {
                        String currentTime = DateTimeUtils.getYMDTime(item.getCreateTime());
                        if (!currentTime.equals(lastTime)) {
                            lastTime = currentTime;
                            EarningsMsg earningsMsg = new EarningsMsg();
                            earningsMsg.setViewType(EarningsMsg.TWO_TYPE);
                            earningsMsg.setCreateTime(item.getCreateTime());
                            earningsMsgList.add(earningsMsg);
                        }

                    }

                }
                earningsMsgList.add(item);
            }
        }

        return earningsMsgList;
    }
}
