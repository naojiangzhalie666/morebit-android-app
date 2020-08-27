package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.BillDetailAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.DoorGodCategoryBean;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.RequestWithdrawDetailBean;
import com.zjzy.morebit.pojo.TimeBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.WithdrawDetailBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.PageToUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.Observable;

/**
 *提现明细
 */
public class BillDetailsFragment extends BaseMainFragmeng implements View.OnClickListener {
    private RecyclerView rcy_bill;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout dataList_ly;
    private int type=1;
    private String data="";
    private int page=1;
    private   BillDetailAdapter  billDetailAdapter;



    public static BillDetailsFragment newInstance(int type) {
        BillDetailsFragment fragment = new BillDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.BILL_ORDER_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billdetails, container, false);
        initView(view);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return view;
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(TimeBean evBean) {
        data = evBean.getTime();
        getData();
    }



    private void getData() {
        getUserBillDetail(this,type,data,page)
                .subscribe(new DataObserver<List<WithdrawDetailBean>>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        if (page==1){
                            dataList_ly.setVisibility(View.VISIBLE);
                           refreshLayout.setVisibility(View.GONE);
                        }
                        refreshLayout.finishLoadMore();
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        if (page==1){
                            dataList_ly.setVisibility(View.VISIBLE);
                          refreshLayout.setVisibility(View.GONE);
                        }
                        onActivityFailure();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    protected void onSuccess(List<WithdrawDetailBean> data) {
                        onSuccessful(data);
                    }
                });

    }

    private void onSuccessful(List<WithdrawDetailBean> data) {
        if (data!=null){
            refreshLayout.finishRefresh();

            dataList_ly.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            if (page==1){
                billDetailAdapter.setData(data);
            }  else {
                billDetailAdapter.addData(data);
                refreshLayout.finishLoadMore();

            }
        }else{

            dataList_ly.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        }

    }

    private void onActivityFailure() {
        Log.e("ssss", "1111");
    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt(C.Extras.BILL_ORDER_TYPE);//账单类型
        }

        rcy_bill=view.findViewById(R.id.rcy_bill);
        refreshLayout=view.findViewById(R.id.refreshLayout);
        dataList_ly=view.findViewById(R.id.dataList_ly);
        billDetailAdapter=new BillDetailAdapter(getActivity());
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        rcy_bill.setLayoutManager(manager);
        rcy_bill.setAdapter(billDetailAdapter);

        getData();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData();
            }
        });

    }






    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }

    }


    /**
     * 账单明细
     *
     * @param fragment
     * @return
     */

    public Observable<BaseResponse<List<WithdrawDetailBean>>> getUserBillDetail(RxFragment fragment,int type,String data,int page) {
        RequestWithdrawDetailBean detailBean=new RequestWithdrawDetailBean();
        detailBean.setType(type+"");
        if (!TextUtils.isEmpty(data)){
            detailBean.setDate(data);
        }
        detailBean.setPage(page);
        return RxHttp.getInstance().getCommonService()
                .getUserBillDetail(detailBean)
                .compose(RxUtils.<BaseResponse<List<WithdrawDetailBean>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<WithdrawDetailBean>>>bindToLifecycle());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
