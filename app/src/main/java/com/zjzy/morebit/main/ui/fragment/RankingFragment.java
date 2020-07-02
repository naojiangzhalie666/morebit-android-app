package com.zjzy.morebit.main.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.App;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.RankingAdapter;
import com.zjzy.morebit.adapter.SelectGoodsAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.QueryDhAndGyBean;
import com.zjzy.morebit.pojo.RankTimeBean;
import com.zjzy.morebit.pojo.RankingTitleBean;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MathUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;


/**
 * 首页分类-热门排行
 */
public class RankingFragment extends BaseMainFragmeng {


    @BindView(R.id.iv_head_bg)
    ImageView iv_head_bg;


    private View mView;
    private int mHeadBgHeight;
    private RecyclerView rcy_rank;
    private NestedScrollView nscorll;
    private int page = 1;
    private TextView tv_time;
    private RankingAdapter rankingAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_ranking, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.with(getActivity())
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                //解决状态栏和布局重叠问题，任选其一
                .init();
        initView(mView);

        initmData();


    }

    private void initmData() {
        getRealTime(this, page)
                .subscribe(new DataObserver<RankTimeBean>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(RankTimeBean data) {
                        onGetRealTime(data);
                    }
                });

    }

    private void onGetRealTime(RankTimeBean data) {
        List<RankTimeBean.ItemListBean> itemList = data.getItemList();
        tv_time.setText("热销榜每小时更新一次 更新于" + MathUtils.getToDate(data.getUpdateTime()));
        if (page == 1) {
            rankingAdapter = new RankingAdapter(getActivity(), itemList);
            rcy_rank.setAdapter(rankingAdapter);
        } else {
            rankingAdapter.setData(itemList);
        }
    }

    private void onActivityFailure() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        getActivity().finish();
    }

    /**
     * 初始化界面
     */
    private void initView(View view) {
        tv_time = view.findViewById(R.id.tv_time);
        rcy_rank = view.findViewById(R.id.rcy_rank);
        nscorll = view.findViewById(R.id.nscorll);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rcy_rank.setLayoutManager(manager);
        rcy_rank.setNestedScrollingEnabled(false);
        nscorll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //判断是否滑到的底部
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    page++;
                    initmData();

                }

            }
        });

    }


    //实时排行（新）
    public Observable<BaseResponse<RankTimeBean>> getRealTime(RxFragment fragment, int page) {
        RequestBannerBean requestBannerBean = new RequestBannerBean();
        requestBannerBean.setPage(page);
        requestBannerBean.setType(1);
        return RxHttp.getInstance().getSysteService().getRealTime(requestBannerBean)
                .compose(RxUtils.<BaseResponse<RankTimeBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<RankTimeBean>>bindToLifecycle());
    }

}
