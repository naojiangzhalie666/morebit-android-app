package com.zjzy.morebit.main.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.GoodsGuessAdapter;
import com.zjzy.morebit.adapter.SelectGoodsAdapter2;
import com.zjzy.morebit.adapter.ShoppingListAdapter2;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.SystemConfigBean;
import com.zjzy.morebit.pojo.event.GoodsHeightUpdateEvent;
import com.zjzy.morebit.pojo.requestbodybean.RequestGoodsLike;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.ConfigListUtlis;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.fire.DeviceIDUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by fengrs on 2018/9/10.
 * 备注:
 */

public class GuessLikeFragment extends BaseFragment {
    RecyclerView mRlList;
    private SelectGoodsAdapter2 mAdapter;
    private ShopGoodInfo mGoodsInfo;
    private int page=1;


    public static GuessLikeFragment newInstance() {
        GuessLikeFragment fragment = new GuessLikeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_detail_like_guess2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGoodsInfo = (ShopGoodInfo) getArguments().getSerializable(C.Extras.GOODSBEAN);
        initView(view);
        getData();

    }
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("1")) {  //接收到广播通知的名字，在当前页面应与注册名称一致
                page++;
                getData();
            }
        }
    };
    protected void initView(View view) {
        mRlList = view.findViewById(R.id.rl_list);
        mAdapter = new SelectGoodsAdapter2(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRlList.setLayoutManager(gridLayoutManager);
        mRlList.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 6)));
        mRlList.setAdapter(mAdapter);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("1");//名字
        getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);




    }


    public void getData() {
        String getimei = DeviceIDUtils.getimei(getActivity());
        if (TextUtils.isEmpty(getimei))return;
        RequestGoodsLike requestGoodsLike = new RequestGoodsLike();
        requestGoodsLike.setDeviceType("IMEI");
        requestGoodsLike.setDeviceValue(getimei);
        requestGoodsLike.setPage(page);
        RxHttp.getInstance().getCommonService().getRecommendItemsById(requestGoodsLike)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        EventBus.getDefault().post(new GoodsHeightUpdateEvent());
                    }
                })
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {

                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {

                        if (page==1){
                            mAdapter.setData(data);
                        }else{
                            mAdapter.setData(data);
                        }


                    }
                });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
