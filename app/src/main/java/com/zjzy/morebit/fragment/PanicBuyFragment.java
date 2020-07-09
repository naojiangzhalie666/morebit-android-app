package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShoppingListAdapter;
import com.zjzy.morebit.goodsvideo.VideoFragment;
import com.zjzy.morebit.home.fragment.HomeOtherFragment;
import com.zjzy.morebit.home.fragment.LimiteFragment;
import com.zjzy.morebit.home.fragment.LimiteSkillFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.PanicBuyTiemBean;
import com.zjzy.morebit.pojo.PanicBuyingListBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.request.RequestPanicBuyTabBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetTimedSpikeList;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LimitedTimePageTitleView;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.view.AspectRatioView;
import com.zjzy.morebit.view.ToolbarHelper;
import com.zjzy.morebit.view.helper.PanicBuyTabView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * 限时秒杀
 */
public class PanicBuyFragment extends BaseFragment {
    @BindView(R.id.ll_root)
    LinearLayout ll_root;

    private int scrollHeight;
    private ImageInfo mImageInfo;
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private CommonNavigator mCommonNavigator;
    private List<PanicBuyTiemBean> mTimeTitleList = new ArrayList<>();
    private List<LimiteSkillFragment> fragments = new ArrayList<>();

    private LimitePagerAdapter limiteAdapter;
    private ImageView back;
    private String title;

    public static void start(Activity activity, ImageInfo info) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.Extras.GOODSBEAN, info);
        OpenFragmentUtils.goToSimpleFragment(activity, PanicBuyFragment.class.getName(), bundle);
    }
    public static void start(Activity activity, ImageInfo info,String title) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.Extras.GOODSBEAN, info);
        bundle.putSerializable(C.UserType.TIMETITLE, title);
        OpenFragmentUtils.goToSimpleFragment(activity, PanicBuyFragment.class.getName(), bundle);
    }

    public static void mstart(Activity activity) {
        Bundle bundle = new Bundle();
        OpenFragmentUtils.goToSimpleFragment(activity, PanicBuyFragment.class.getName(), bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ImmersionBar.with(getActivity())
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.color_F05557)
                .init();
        View view = inflater.inflate(R.layout.fragment_panicbuy, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(R.string.panic_buy);
        mImageInfo = (ImageInfo) getArguments().getSerializable(C.Extras.GOODSBEAN);
        initView(view);
        initmData();

    }

    private void initmData() {
        getGet_taoqianggou_time((RxAppCompatActivity) getActivity())
                .subscribe(new DataObserver<List<PanicBuyTiemBean>>() {
                    @Override
                    protected void onSuccess(List<PanicBuyTiemBean> bean) {
                        fragments.clear();
                        mTimeTitleList.clear();
                        mTimeTitleList.addAll(bean);
                        initIndicator();
                        limiteAdapter = new LimitePagerAdapter(getActivity().getSupportFragmentManager(), fragments,bean);
                        mViewPager.setAdapter(limiteAdapter);

                        for (int i=0;i<mTimeTitleList.size();i++){
                            if (title.equals(mTimeTitleList.get(i).getTitle())){
                                mViewPager.setCurrentItem(i);
                            }
                        }
                    }
                });
    }


    public void initView(View view) {
        mMagicIndicator = view.findViewById(R.id.magicIndicator);
        mViewPager = view.findViewById(R.id.viewPager);
        ImageView back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        Bundle arguments = getArguments();
        if (arguments != null) {
            title = (String) arguments.getSerializable(C.UserType.TIMETITLE);

        }

    }

    public class LimitePagerAdapter extends FragmentPagerAdapter {
        private List<LimiteSkillFragment> mFragments;
        private List<PanicBuyTiemBean> list;


        public LimitePagerAdapter(FragmentManager fm, List<LimiteSkillFragment> fragments, List<PanicBuyTiemBean> bean) {
            super(fm);
            mFragments = fragments;
            list=bean;

        }

        @Override
        public Fragment getItem(int position) {
            return LimiteSkillFragment.newInstance(list.get(position).getStartTime(),list.get(position).getSubTitle());
        }

        @Override
        public int getCount() {
            return mTimeTitleList.size();
        }


        //    为Tabayout设置主题名称
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return title == null ? "" + position : title.get(position);
//        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {

        }
    }

    private void initIndicator() {

        mCommonNavigator = new CommonNavigator(getActivity());
        mCommonNavigator.setAdjustMode(true);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTimeTitleList == null ? 0 : mTimeTitleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                LimitedTimePageTitleView pagerTitleView = new LimitedTimePageTitleView(context);
                pagerTitleView.setTimeText(mTimeTitleList.get(index).getTitle());
                pagerTitleView.setTipsText(mTimeTitleList.get(index).getSubTitle());

                pagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return pagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        mMagicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }




    private boolean isShowHeadPicture(ImageInfo imageInfo) {
        return !TextUtils.isEmpty(imageInfo.getBackgroundImage());
    }



    /*
     *
     * 限时秒杀时间
     *
     * */
    private Observable<BaseResponse<List<PanicBuyTiemBean>>> getGet_taoqianggou_time(RxAppCompatActivity activity) {
        RequestPanicBuyTabBean requestBean = new RequestPanicBuyTabBean();
        requestBean.setType(0);

        return RxHttp.getInstance().getSysteService()
                .getPanicBuyTabData(requestBean)
                .compose(RxUtils.<BaseResponse<List<PanicBuyTiemBean>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<PanicBuyTiemBean>>>bindToLifecycle());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
