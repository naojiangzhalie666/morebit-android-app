package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.BillDetailAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.RequestWithdrawDetailBean;
import com.zjzy.morebit.pojo.TimeBean;
import com.zjzy.morebit.pojo.WithdrawDetailBean;
import com.zjzy.morebit.utils.C;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;

/**
 *提现明细
 */
public class WithdrawDetailsFragment extends BaseMainFragmeng implements View.OnClickListener {
    private RecyclerView rcy_bill;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout dataList_ly;
    private int type=1;
    private int page=1;
    private   BillDetailAdapter  billDetailAdapter;



    public static WithdrawDetailsFragment newInstance(int type) {
        WithdrawDetailsFragment fragment = new WithdrawDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.BILL_ORDER_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billdetails, container, false);
        initView(view);
        return view;
    }




    private void getData() {
        getWithdrawalDetail(this,type,page)
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
     * 提现明细
     *
     * @param fragment
     * @return
     */

    public Observable<BaseResponse<List<WithdrawDetailBean>>> getWithdrawalDetail(RxFragment fragment,int type,int page) {
        RequestWithdrawDetailBean detailBean=new RequestWithdrawDetailBean();
        detailBean.setType(type+"");
        detailBean.setPage(page);
        return RxHttp.getInstance().getCommonService()
                .getWithdrawalDetail(detailBean)
                .compose(RxUtils.<BaseResponse<List<WithdrawDetailBean>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<WithdrawDetailBean>>>bindToLifecycle());

    }


}
