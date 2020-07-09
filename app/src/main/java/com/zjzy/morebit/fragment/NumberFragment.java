package com.zjzy.morebit.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页面中的会员页面
 * 升级入口，会员商品列表
 * Created by haiping.liu on 2019-12-04.
 */
public class NumberFragment extends BaseMainFragmeng {
    private static final String TAG = NumberFragment.class.getSimpleName();

    private View mView;
    //
    @BindView(R.id.status_bar)
    View status_bar;
    private String extra;
    private ImageView back;
    private int fanhui;


//    @BindView(R.id.my_more_corn)
//    TextView myCornView;


    List<BaseFragment> mFragments = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);

//        Bundle args = getArguments();
//        extra = args.getString("extra");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_number, container, false);
        ButterKnife.bind(this, mView);

        initView(mView);
        return mView;
    }


    @Override
    public void onResume() {
        super.onResume();
        //initBar();
    }

    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            fanhui = arguments.getInt(C.UserType.NEWVIP);
        }
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        back = view.findViewById(R.id.back);
        mFragments.add(NumberSubFragment.newInstance());
        //initBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);

        }
        TextView tv = view.findViewById(R.id.txt_head_title);
        tv.getPaint().setFakeBoldText(true);
        if (fanhui == 1) {
            back.setVisibility(View.VISIBLE);
            // 设置状态栏颜色
           getActivity().getWindow().setStatusBarColor(Color.parseColor("#EDCFB1"));
        } else {
            back.setVisibility(View.GONE);
            // 设置状态栏颜色
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        viewPager.setAdapter(new NumbersGoodsAdapter(getChildFragmentManager()));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


    }

    private void initBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);
            // 设置状态栏颜色
            getActivity().getWindow().setStatusBarColor(Color.parseColor("#EDCFB1"));
        }
    }

    private class NumbersGoodsAdapter extends FragmentPagerAdapter {


        public NumbersGoodsAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
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


//    @Subscribe  //订阅事件
//    public void onEventMainThread(MyMoreCoinEvent event) {
////       Long coin = event.getCoin();
////       if (coin == null){
////            myCornView.setText("0");
////        }else{
////            myCornView.setText(String.valueOf(coin));
////        }
//
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }


}
