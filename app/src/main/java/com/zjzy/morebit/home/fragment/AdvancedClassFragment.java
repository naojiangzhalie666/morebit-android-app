package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SelectGoodsAdapter;
import com.zjzy.morebit.adapter.SkillAdapter;
import com.zjzy.morebit.adapter.StudyTitleAdapter;
import com.zjzy.morebit.adapter.SubNumberAdapter;
import com.zjzy.morebit.circle.ui.SearchArticleListActitivty;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.StudyRank;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestListBody;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.pojo.requestbodybean.RequestTwoLevel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 进阶课堂
 */
public class AdvancedClassFragment extends BaseMainFragmeng {

    private LRecyclerView rcy_goods;
    private int page = 1;
    private SkillAdapter mAdapter;
    private SmartRefreshLayout swipeList;
    private RecyclerView rcy_title;
    private StudyTitleAdapter titleAdapter;


    public static AdvancedClassFragment newInstance() {
        AdvancedClassFragment fragment = new AdvancedClassFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advance_class, container, false);
        getData();
        getStudyRank();
        initView(view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return view;
    }

    private void getStudyRank() {


    }

    private void onStudyRankSuccessful(List<StudyRank> data) {

        if (data != null) {
            titleAdapter.setData(data);
        }


    }

    private void onStudyRankEmpty() {

    }


    private void getData() {
        getStudyRankTitle(this)
                .subscribe(new DataObserver<List<StudyRank>>() {
                    @Override
                    protected void onDataListEmpty() {
                        onStudyRankEmpty();
                    }


                    @Override
                    protected void onSuccess(List<StudyRank> data) {
                        onStudyRankSuccessful(data);
                    }
                });



        getRecommendMoreList(this, page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {


                    }
                })
                .subscribe(new DataObserver<List<Article>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        super.onError(errorMsg, errCode);
                        showError(errCode, errorMsg);
                        swipeList.finishLoadMore();
                    }

                    @Override
                    protected void onDataListEmpty() {
                        swipeList.finishLoadMore();

                    }

                    @Override
                    protected void onSuccess(List<Article> data) {
                        showSuccessful(data);
                    }
                });
    }

    private void showSuccessful(List<Article> list) {


//            if (page == 1) {
//                mAdapter.setData(data);
//            } else {
//                mAdapter.setData(data);
//                swipeList.finishLoadMore();
//            }



//        List<NumberGoods> list = data.getList();//
        if (list == null || (list != null && list.size() == 0)) {
            return;
        }
        if (page == 1) {
            mAdapter.setData(list);
        } else {
            mAdapter.addData(list);
            swipeList.finishLoadMore();

         }


    }

    private void showError(String errCode, String errorMsg) {

    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
        }

        mAdapter = new SkillAdapter(getActivity());
        swipeList = view.findViewById(R.id.swipeList);
        //底部课程列表
        rcy_goods = view.findViewById(R.id.rcy_goods);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        rcy_goods.setAdapter(mLRecyclerViewAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rcy_goods.setLayoutManager(manager);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_vip_skill_head, null, false);
        mLRecyclerViewAdapter.addHeaderView(header);
        swipeList.setEnableRefresh(false);

        swipeList.setEnableLoadMore(true);
        swipeList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData();
            }
        });
        //头部title列表
        rcy_title = header.findViewById(R.id.rcy_title);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rcy_title.setLayoutManager(gridLayoutManager);
        titleAdapter = new StudyTitleAdapter(getActivity());
        rcy_title.setAdapter(titleAdapter);

        RelativeLayout search_rl = header.findViewById(R.id.search_rl);
        search_rl.setOnClickListener(new View.OnClickListener() {//进入商学院搜索
            @Override
            public void onClick(View v) {
                SearchArticleListActitivty.start(getActivity());
            }
        });


    }

    /**
     * 获取商学院推荐更多文章列表
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<Article>>> getRecommendMoreList(RxFragment fragment, int page) {
        RequestListBody body = new RequestListBody();
        body.setPage(page);
        return RxHttp.getInstance().getCommonService().getRecommendArticleList(body)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<Article>>>bindToLifecycle());
    }

    /**
     * 获取商学院学习等级模块
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<StudyRank>>> getStudyRankTitle(RxFragment fragment) {
        return RxHttp.getInstance().getCommonService().getStudyRank()
                .compose(RxUtils.<BaseResponse<List<StudyRank>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<StudyRank>>>bindToLifecycle());
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        if (event.getAction().equals(EventBusAction.ACTION_REFRSH)) {
            getData();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
