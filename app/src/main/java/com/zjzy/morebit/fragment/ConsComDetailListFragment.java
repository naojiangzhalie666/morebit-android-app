package com.zjzy.morebit.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ConsComDetailAdapter;
import com.zjzy.morebit.info.ui.fragment.ShareFriendsFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.AgentDetailList;
import com.zjzy.morebit.pojo.request.RequestVaultListBean;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.OpenFragmentUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;


/**
 * 我的账单
 * Created by Dell on 2016/3/22.
 */
public class ConsComDetailListFragment extends BaseFragment {
    TextView textView;
    private ReUseListView mRecyclerView;
    private ConsComDetailAdapter consComDetailAdapter;
    List<AgentDetailList> listArray = new ArrayList<>();
    private static final int REQUEST_COUNT = 20;
    private int pageNum = 1;
    private int pageSiz = 12;
    private boolean isRefresh = true;
    private int inType = 1;
    private LinearLayout dateNullView, dateNullView_clickbox;
    private TextView dateNullView_tips_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initBundle();
        View view = inflater.inflate(R.layout.fragment_conscomdetail_list, container, false);
        inview(view);
        mRecyclerView.getSwipeList().post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.getSwipeList().setRefreshing(true);
                getFirstData();
            }
        });
        return view;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }


    public void inview(View view) {
        mRecyclerView = (ReUseListView) view.findViewById(R.id.listview);
        consComDetailAdapter = new ConsComDetailAdapter(getActivity(), listArray);

        mRecyclerView.setAdapter(consComDetailAdapter);
        mRecyclerView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();
            }
        });

        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (!mRecyclerView.getSwipeList().isRefreshing())
                    getMoreData();
            }
        });
        consComDetailAdapter.setOnAdapterClickListener(new ConsComDetailAdapter.OnAdapterClickListener() {
            @Override
            public void onItem(int position) {

            }
        });
        //数据为空的
        dateNullView = (LinearLayout) view.findViewById(R.id.dateNullView);
        dateNullView_clickbox = (LinearLayout) view.findViewById(R.id.dateNullView_clickbox);
        dateNullView_tips_tv = (TextView) view.findViewById(R.id.dateNullView_tips_tv);
        dateNullView_tips_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        dateNullView_tips_tv.getPaint().setAntiAlias(true);//抗锯齿
        dateNullView_clickbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到分享界面
//               startActivity(new Intent(getActivity(),PartnerShareActivity.class));
                OpenFragmentUtils.goToSimpleFragment(getActivity(), ShareFriendsFragment.class.getName(), new Bundle());
            }
        });
    }

    /**
     * 第一次获取数据 //获取消费佣金明细列表
     */
    public void getFirstData() {
        pageNum = 1;
        mRecyclerView.getListView().setNoMore(false);

        RequestVaultListBean requestBean = new RequestVaultListBean();
        requestBean.setPage(pageNum);
        RxHttp.getInstance().getUsersService()
//                .getUsersVaultList(pageNum)
                .getUsersVaultList(requestBean)
                .compose(RxUtils.<BaseResponse<List<AgentDetailList>>>switchSchedulers())
                .compose(this.<BaseResponse<List<AgentDetailList>>>bindToLifecycle())
                // .map(RxUtils.<dayEarnings>handleRESTFulResult())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<List<AgentDetailList>>() {
                    @Override
                    protected void onDataListEmpty() {
                        dateNullView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(List<AgentDetailList> data) {
                        listArray.clear();
                        List<AgentDetailList> list = disposeList(data);
                        listArray.addAll(list);
                        consComDetailAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();
                        pageNum = pageNum + 1;
                    }
                });
    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {

        RequestVaultListBean requestBean = new RequestVaultListBean();
        requestBean.setPage(pageNum);
        RxHttp.getInstance().getUsersService()
//                .getUsersVaultList(pageNum)
                .getUsersVaultList(requestBean)
                .compose(RxUtils.<BaseResponse<List<AgentDetailList>>>switchSchedulers())
                .compose(this.<BaseResponse<List<AgentDetailList>>>bindToLifecycle())
                // .map(RxUtils.<dayEarnings>handleRESTFulResult())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<List<AgentDetailList>>() {
                    @Override
                    protected void onDataListEmpty() {
                        mRecyclerView.getListView().setNoMore(true);
                    }

                    @Override
                    protected void onSuccess(List<AgentDetailList> data) {
                        List<AgentDetailList> list = disposeList(data);
                        listArray.addAll(list);
                        consComDetailAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();
                        pageNum = pageNum + 1;
                    }
                });
    }

 public  List<AgentDetailList> disposeList(List<AgentDetailList> datas){
     List<AgentDetailList> list = new ArrayList<>();
     List<AgentDetailList> allList = new ArrayList<>();
     allList.addAll(listArray);
     allList.addAll(datas);
     int index =listArray.size();
     for (int i = index; i <allList.size() ; i++) {
         if(i==0){
             AgentDetailList agentDetailList= new AgentDetailList();
             agentDetailList.setViewType(1);
             agentDetailList.setYearMonthDay(DateTimeUtils.getTheMonth(allList.get(i).getYearMonthDay(),DateTimeUtils.FORMAT_PATTERN_YEAR_MONTH_DAY_WITHOUT_TEXT,DateTimeUtils.FORMAT_PATTERN_YEAR_MONTH));
             list.add(agentDetailList);
             list.add(allList.get(i));
         }else {
             String date = DateTimeUtils.getTheMonth(allList.get(i).getYearMonthDay(),DateTimeUtils.FORMAT_PATTERN_YEAR_MONTH_DAY_WITHOUT_TEXT,DateTimeUtils.FORMAT_PATTERN_YEAR_MONTH);
             if(date!=null&&!date.equals(DateTimeUtils.getTheMonth(allList.get(i-1).getYearMonthDay(),DateTimeUtils.FORMAT_PATTERN_YEAR_MONTH_DAY_WITHOUT_TEXT,DateTimeUtils.FORMAT_PATTERN_YEAR_MONTH))){//插入年月日
                 AgentDetailList agentDetailList= new AgentDetailList();
                 agentDetailList.setViewType(1);
                 agentDetailList.setYearMonthDay(date);
                 list.add(agentDetailList);
             }
             list.add(allList.get(i));
         }
//         else if(i==allList.size()-1){
//             String date = allList.get(i).getYearMonthDay();
//             if(date!=null&&!date.equals(allList.get(i-1).getYearMonthDay())){//插入年月日
//                 AgentDetailList agentDetailList= new AgentDetailList();
//                 agentDetailList.setViewType(1);
//                 agentDetailList.setYearMonthDay(datas.get(tureI).getYearMonthDay());
//                 list.add(agentDetailList);
//             }
//             list.add(datas.get(tureI));
//         }
     }
     return list;
 }

}
