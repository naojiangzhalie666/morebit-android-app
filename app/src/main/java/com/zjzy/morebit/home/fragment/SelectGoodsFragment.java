package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.LimitSkillAdapter;
import com.zjzy.morebit.adapter.SelectGoodsAdapter;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.pojo.goods.NewRecommendBean;
import com.zjzy.morebit.pojo.request.RequestRecommendBean;
import com.zjzy.morebit.utils.StringsUtils;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 精选商品
 */
public class SelectGoodsFragment extends BaseMainFragmeng {

    private RecyclerView rcy_goods;

    private List<ShopGoodInfo>  list=new ArrayList<>();
    private LinearLayout searchNullTips_ly;
    private int mMinNum=0;
    private int page = 1;
    private int mType=0;
    private  SelectGoodsAdapter selectGoodsAdapter;
    private  List<ShopGoodInfo> goods;
    private NestedScrollView netscrollview;



    public static SelectGoodsFragment newInstance() {
        SelectGoodsFragment fragment = new SelectGoodsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selectgoods, container, false);
        getData();
        initView(view);
        return view;
    }



    private void getData() {
        getNewRecommend(this, page, mMinNum, mType)
                .subscribe(new DataObserver<NewRecommendBean>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onRecommendFailure(errorMsg, errCode);
                    }


                    @Override
                    protected void onSuccess(NewRecommendBean data) {
                       onRecommendSuccessful(data);
                    }
                });
    }

    private void onRecommendSuccessful(NewRecommendBean recommendBean) {
        goods = recommendBean.getItemList();
        if (page==1){
            selectGoodsAdapter=new SelectGoodsAdapter(getActivity(),goods);
            rcy_goods.setAdapter(selectGoodsAdapter);


        }else{
                selectGoodsAdapter.setData(goods);
        }
        mMinNum = recommendBean.getMinNum();
        mType = recommendBean.getType();

    }

    private void onRecommendFailure(String errorMsg, String errCode) {

    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {

        }
        rcy_goods = view.findViewById(R.id.rcy_goods);
        netscrollview=view.findViewById(R.id.netscrollview);
        rcy_goods.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rcy_goods.setLayoutManager(manager);
        netscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //判断是否滑到的底部
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    page++;
                    getData();//调用刷新控件对应的加载更多方法
                }
            }
        });
        rcy_goods.getItemAnimator().setAddDuration(0);
        rcy_goods.getItemAnimator().setChangeDuration(0);
        rcy_goods.getItemAnimator().setMoveDuration(0);
        rcy_goods.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) rcy_goods.getItemAnimator()).setSupportsChangeAnimations(false);

//        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_selectgoods_head, null, false);
//        TextView tv_icon = header.findViewById(R.id.tv_icon);
//        TextView tv_title = header.findViewById(R.id.tv_title);
//        StringsUtils.retractTitle(tv_icon,tv_title, "特仑苏牛奶大减价了 快来买啊实践活动放松放松房间里刷卡缴费卡洛斯");


    }


    public Observable<BaseResponse<NewRecommendBean>> getNewRecommend(RxFragment fragment, int page, int minNum, int type) {
        RequestRecommendBean requestBean = new RequestRecommendBean();
        requestBean.setPage(page);
        requestBean.setMinNum(minNum);
        requestBean.setType(type);

        return RxHttp.getInstance().getGoodsService()
                .getNewRecommend(requestBean)
                .compose(RxUtils.<BaseResponse<NewRecommendBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<NewRecommendBean>>bindToLifecycle());
    }



}
