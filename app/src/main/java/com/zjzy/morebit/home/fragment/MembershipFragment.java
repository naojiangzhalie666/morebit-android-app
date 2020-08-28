package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.MembershipAdapter1;
import com.zjzy.morebit.adapter.MembershipAdapter2;
import com.zjzy.morebit.adapter.SkillAdapter;
import com.zjzy.morebit.adapter.StudyTitleAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.NumberSubFragment;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.DoorGodCategoryBean;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.StudyRank;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.VipBean;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.pojo.request.RequestListBody;
import com.zjzy.morebit.pojo.request.base.RequestBaseOs;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 会员权益
 */
public class MembershipFragment extends BaseMainFragmeng {


    private RecyclerView rcy_title;
    private RecyclerView rcy_icon;
    private MembershipAdapter1 adapter1;
    private MembershipAdapter2 adapter2;
    private LinearLayoutManager manager;
    private Handler handler = new Handler();
    private View view1;
    private LinearLayout ll1;
    /**
     * 是否来自点击
     */
    private boolean mIsFromClick = false;


    public static MembershipFragment newInstance() {
        MembershipFragment fragment = new MembershipFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_membership, container, false);
        initView(view);
        getData();
        getStudyRank();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return view;
    }

    private void getStudyRank() {


    }


    private void getData() {
        getVipRightship(this)
                .subscribe(new DataObserver<List<VipBean>>(false) {
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
                    protected void onSuccess(List<VipBean> data) {

                        onGetDoorGodCategory(data);
                    }
                });

    }


    private void onGetDoorGodCategory(List<VipBean> data) {

        adapter1.setData(data);
        adapter2.setData(data);

    }

    private void onActivityFailure() {

    }


    private void getVipRightsData(VipBean data) {


    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
        }
        view1 = view.findViewById(R.id.view1);
        ll1 = view.findViewById(R.id.ll1);
        rcy_title = view.findViewById(R.id.rcy_title);
        rcy_icon = view.findViewById(R.id.rcy_icon);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        adapter1 = new MembershipAdapter1(getActivity());
        rcy_title.setLayoutManager(layoutManager1);
        rcy_title.setAdapter(adapter1);

        initViewData();



        adapter2 = new MembershipAdapter2(getActivity());
//        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter2);
        rcy_icon.setAdapter(adapter2);
        manager = new LinearLayoutManager(getActivity());
        rcy_icon.setLayoutManager(manager);
//        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_vip_head_ship,null, false);
//        mLRecyclerViewAdapter.addHeaderView(header);
//        TextView title = header.findViewById(R.id.title);
//        title.getPaint().setFakeBoldText(true);


        rcy_icon.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mIsFromClick) {//防止来回调
                    return;
                }
                changePosition();
            }
        });


        adapter1.setOnAddClickListener(new MembershipAdapter1.OnAddClickListener() {
            @Override
            public void onShareClick(int postion) {
                Log.e("kkkk", postion + "");
                adapter1.mCheckedPosition = postion;
                adapter1.setmPosition(postion);
                adapter1.notifyDataSetChanged();


                mIsFromClick = true;//不走onScrolled，防止来回调
//                manager.scrollToPositionWithOffset(postion,0);
//
//                mIsFromClick = false;//放开


                LinearSmoothScroller topScroller = new LinearSmoothScroller(getActivity()) {

                    @Override
                    protected int getHorizontalSnapPreference() {
                        return SNAP_TO_START;//具体见源码注释
                    }

                    @Override
                    protected int getVerticalSnapPreference() {
                        return SNAP_TO_START;//具体见源码注释
                    }

                    @Override
                    protected void onStop() {
                        super.onStop();
                        //发送一个延时handler
                        //因为onStop时还有惯性动画
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mIsFromClick = false;
                            }
                        }, 500);
                    }
                };
                topScroller.setTargetPosition(postion);
                manager.startSmoothScroll(topScroller);
                Log.e("kkkk", postion + "00000");

            }
        });


    }

    private void initViewData() {
        UserInfo mUserInfo = UserLocalData.getUser(getActivity());
        if (mUserInfo != null) {
            if (C.UserType.member.equals(mUserInfo.getUserType())) {
                view1.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.VISIBLE);
            } else {
                view1.setVisibility(View.GONE);
                ll1.setVisibility(View.GONE);
            }
        }
    }

    private void changePosition() {

        int firstPosition = manager.findFirstVisibleItemPosition();
        Log.e("kkkk", firstPosition + "无奈");
        Log.e("kkkk", adapter1.mCheckedPosition + "无奈2");
        if (adapter1.mCheckedPosition != firstPosition) {
            adapter1.mCheckedPosition = firstPosition;
            adapter1.notifyDataSetChanged();
            Log.e("kkkk", "无奈3");
            //此方法无置顶效果
            rcy_title.scrollToPosition(adapter1.mCheckedPosition);
        }


    }

    //获取会员权益
    public Observable<BaseResponse<List<VipBean>>> getVipRightship(RxFragment fragment) {
        RequestBaseOs requestBannerBean = new RequestBaseOs();
        requestBannerBean.setOs(1);
        return RxHttp.getInstance().getSysteService().getVipRights(requestBannerBean)
                .compose(RxUtils.<BaseResponse<List<VipBean>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<VipBean>>>bindToLifecycle());
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        if (event.getAction().equals(EventBusAction.ACTION_REFRSH)) {
            getData();
        }else if (event.getAction().equals(EventBusAction.UPGRADE_SEHNGJI)){
            initViewData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
