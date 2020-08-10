package com.zjzy.morebit.main.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.GoodsAdapter;
import com.zjzy.morebit.adapter.GoodsGuessAdapter;
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
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.fire.DeviceIDUtils;
import com.zjzy.morebit.view.FixRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by fengrs on 2018/9/10.
 * 备注:
 */

public class GuessDetailLikeFragment extends BaseFragment {
    RecyclerView mRlList;
    RelativeLayout mRlTitle;
    private TextView titleTv;
    private GoodsGuessAdapter mAdapter;
    private ShopGoodInfo mGoodsInfo;

    public static GuessDetailLikeFragment newInstance(ShopGoodInfo goodsInfo) {
        GuessDetailLikeFragment fragment = new GuessDetailLikeFragment();
        Bundle args = new Bundle();
        args.putSerializable(C.Extras.GOODSBEAN, goodsInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_detail_like_guess, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGoodsInfo = (ShopGoodInfo) getArguments().getSerializable(C.Extras.GOODSBEAN);
        initView(view);
        getData();
        getRecommodTitle();

    }

    protected void initView(View view) {
        titleTv = view.findViewById(R.id.titleTv);
        mRlList = view.findViewById(R.id.rl_list);
        mRlTitle = view.findViewById(R.id.rl_title);
        mAdapter = new GoodsGuessAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRlList.setLayoutManager(gridLayoutManager);
        mRlList.setAdapter(mAdapter);
        mRlList.setNestedScrollingEnabled(false);

    }


    public void getData() {
        if (mGoodsInfo == null) return;
        String getimei = DeviceIDUtils.getimei(getActivity());
        if (TextUtils.isEmpty(getimei))return;
        RequestGoodsLike requestGoodsLike = new RequestGoodsLike();
        requestGoodsLike.setItemSourceId(mGoodsInfo.getItemSourceId());
        requestGoodsLike.setDeviceType("IMEI");
        requestGoodsLike.setDeviceValue(getimei);
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
                        mRlTitle.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        mRlTitle.setVisibility(View.VISIBLE);
                        mAdapter.replace(data);
                        mAdapter.notifyDataSetChanged();

                    }
                });


    }

    /**
     * 获取推荐title
     */
    private void getRecommodTitle() {
        ConfigListUtlis.getConfigListCacheNet((RxAppCompatActivity) getActivity(), ConfigListUtlis.getConfigAllKey(), new MyAction.One<List<SystemConfigBean>>() {
            @Override
            public void invoke(List<SystemConfigBean> arg) {
                SystemConfigBean detailBean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.ITEM_DETAILS_RECOMMENDED_TITLE);
                if(null != detailBean && !TextUtils.isEmpty(detailBean.getSysValue())){
                    titleTv.setText(detailBean.getSysValue());
                }
            }
        });
    }
}
