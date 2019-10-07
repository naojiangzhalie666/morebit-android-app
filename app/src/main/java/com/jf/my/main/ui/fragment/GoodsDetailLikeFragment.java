package com.jf.my.main.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.R;
import com.jf.my.adapter.GoodsAdapter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.ShopGoodInfo;
import com.jf.my.pojo.SystemConfigBean;
import com.jf.my.pojo.event.GoodsHeightUpdateEvent;
import com.jf.my.pojo.requestbodybean.RequestGoodsLike;
import com.jf.my.utils.C;
import com.jf.my.utils.ConfigListUtlis;
import com.jf.my.utils.action.MyAction;
import com.jf.my.utils.fire.DeviceIDUtils;
import com.jf.my.view.FixRecyclerView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by fengrs on 2018/9/10.
 * 备注:
 */

public class GoodsDetailLikeFragment extends BaseFragment {
    FixRecyclerView mRlList;
    RelativeLayout mRlTitle;
    private TextView titleTv;
    private GoodsAdapter mAdapter;
    private ShopGoodInfo mGoodsInfo;

    public static GoodsDetailLikeFragment newInstance(ShopGoodInfo goodsInfo) {
        GoodsDetailLikeFragment fragment = new GoodsDetailLikeFragment();
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
        View view = inflater.inflate(R.layout.fragment_goods_detail_like_list, container, false);
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
        mAdapter = new GoodsAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRlList.setLayoutManager(gridLayoutManager);
        mRlList.setAdapter(mAdapter);
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
