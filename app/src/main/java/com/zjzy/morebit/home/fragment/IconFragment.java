package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ActivityFloorAdapter1;
import com.zjzy.morebit.adapter.HomeMenuAdapter;
import com.zjzy.morebit.goodsvideo.adapter.VideoGoodsAdapter;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.DoorGodCategoryBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.SpaceItemDecorationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * icon分类
 */
public class IconFragment extends MvpFragment<VideoPresenter> implements VideoContract.View  {

    private RecyclerView rcy_icon;

    private List<ShopGoodInfo>  list=new ArrayList<>();
    private SwipeRefreshLayout  swipeList;
    private LinearLayout searchNullTips_ly;
    private List<DoorGodCategoryBean.ResultListBean.WheelChartDisplayVoBean> wheelChartDisplayVo;


    public static IconFragment newInstance(List<DoorGodCategoryBean.ResultListBean.WheelChartDisplayVoBean> wheelChartDisplayVo) {
        IconFragment fragment = new IconFragment();
        Bundle args = new Bundle();
        args.putSerializable(C.UserType.ICONNAME, (Serializable) wheelChartDisplayVo);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initData() {
        getData();

    }

    private void getData() {

    }



    @Override
    protected void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            wheelChartDisplayVo = (List<DoorGodCategoryBean.ResultListBean.WheelChartDisplayVoBean>) arguments.getSerializable(C.UserType.ICONNAME);
        }
        rcy_icon = view.findViewById(R.id.rcy_icon);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 5){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rcy_icon.setLayoutManager(manager);
        if (rcy_icon.getItemDecorationCount()==0){//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
            rcy_icon.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 5)));
        }
//        int windowWidth = AppUtil.getWindowWidth(getActivity());
//        int defpadding = DensityUtil.dip2px(getActivity(), 30);
//        float menuItmeWidth = 0.0F;
//        if (windowWidth != 0) {
//            int padding = windowWidth * defpadding / 1086;
//            menuItmeWidth = ((windowWidth - padding) / 5) + 0.0f;
//        }
//        double horizontalSpacing = defpadding;
//        double itmeWidth = DensityUtil.dip2px(getActivity(), 42);
//        if (menuItmeWidth != 0) {
//            horizontalSpacing = menuItmeWidth * 0.36;
//            itmeWidth = menuItmeWidth * 0.67;
//        }
        HomeMenuAdapter menuAdapter = new HomeMenuAdapter(getActivity()/*,(int) itmeWidthwheelChartDisplayVo,*/);
        menuAdapter.setData(wheelChartDisplayVo);
        rcy_icon.setAdapter(menuAdapter);



    }



    @Override
    protected int getViewLayout() {
        return R.layout.fragment_icon;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onVideoClassSuccess(List<VideoClassBean> shopGoodInfo) {

    }

    @Override
    public void onVideoClassError(String throwable) {

    }

    @Override
    public void onVideoGoodsSuccess(List<ShopGoodInfo> shopGoodInfo) {




    }

    @Override
    public void onVideoGoodsError() {

    }

    @Override
    public void onCommissionGoodsSuccess(List<ShopGoodInfo> shopGoodInfo) {

    }

    @Override
    public void onCommissionGoodsError() {

    }
}
