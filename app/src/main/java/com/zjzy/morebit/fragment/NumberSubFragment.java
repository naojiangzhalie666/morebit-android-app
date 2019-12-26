package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Dialog.NumberLeaderUpgradeDialog;
import com.zjzy.morebit.Module.common.Dialog.NumberVipUpgradeDialog;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.View.ReUseGridView;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.Module.common.View.ReUseNumberGoodsView;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.MarkermallCircleAdapter;
import com.zjzy.morebit.adapter.NumberAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CategoryListChildDtos;
import com.zjzy.morebit.pojo.CategoryListDtos;
import com.zjzy.morebit.pojo.CircleBrand;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.CirleUpdataShareCountEvent;
import com.zjzy.morebit.pojo.event.LoginSucceedEvent;
import com.zjzy.morebit.pojo.event.LogoutEvent;
import com.zjzy.morebit.pojo.event.MyMoreCoinEvent;
import com.zjzy.morebit.pojo.event.RefreshCircleEvent;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestCircleBransBean;
import com.zjzy.morebit.pojo.request.RequestCircleCollectBean;
import com.zjzy.morebit.pojo.request.RequestMarkermallCircleBean;
import com.zjzy.morebit.pojo.request.RequestRemoveCircleCollectBean;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.pojo.requestbodybean.RequestPage;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LogUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.AspectRatioView;
import com.zjzy.morebit.view.HorzProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * 每日爆款
 */
public class NumberSubFragment extends BaseFragment {

    View mView;
    ReUseNumberGoodsView mReUseListView;


    TextView gradeHint1;
    TextView gradeHint2;
    TextView gradeHint3;
    TextView gradeHint4;
    TextView gradeHint5;
    TextView gradeHint6;
    RoundedImageView mUserIcon;
    TextView myGradedView;
    TextView numberGradeName;
    TextView moreCoinBiaozhun;

    HorzProgressView mHorzProgressView;
    ImageView leader_icon;

    RelativeLayout rl_duodou_progress;
    TextView updateVip;
    View headView ;
    TextView userName;

    NumberAdapter mNumberGoodsAdapter;
    List<NumberGoods> numberGoodsList = new ArrayList<NumberGoods>();
    UserInfo mUserInfo;
    private int page = 1;

    private boolean isLoadData = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(NumberSubFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_number_sub, container, false);
            headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_number_header, null);
            initHeadView(headView);
            initView(mView);
            initData();
        }
        return mView;
    }


    public static NumberSubFragment newInstance() {
        NumberSubFragment fragment = new NumberSubFragment();
        return fragment;
    }




    public void initView(View view) {

        mReUseListView = (ReUseNumberGoodsView) view.findViewById(R.id.mListView);
        mNumberGoodsAdapter = new NumberAdapter(getActivity(),numberGoodsList);
        mNumberGoodsAdapter.addHeaderView(headView);

        mReUseListView.setAdapter(mNumberGoodsAdapter, new ReUseNumberGoodsView.RefreshAndLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
//                if (!mReUseListView.getSwipeList().isRefreshing())
                MyLog.i("test", "setRecommendData1");
                getData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isLoadData = true;
                page = 1;
                mReUseListView.getSwipeList().setRefreshing(true);
                mNumberGoodsAdapter.setEnableLoadMore(false);

                MyLog.i("test", "refreshData");
                refreshData();
            }
        });

    }

    private void initHeadView(View headView){
        userName = (TextView)headView.findViewById(R.id.user_name);
        mUserIcon = (RoundedImageView)headView.findViewById(R.id.userIcon);
        myGradedView = (TextView)headView.findViewById(R.id.lb_user_grade);
        numberGradeName = (TextView)headView.findViewById(R.id.number_grade_name);
        moreCoinBiaozhun = (TextView)headView.findViewById(R.id.more_corn_biaozhun);

        gradeHint1 = (TextView)headView.findViewById(R.id.grade_hint1);
        gradeHint2 = (TextView)headView.findViewById(R.id.grade_hint2);
        gradeHint3 = (TextView)headView.findViewById(R.id.grade_hint3);
        gradeHint4 = (TextView)headView.findViewById(R.id.grade_hint4);
        gradeHint5 = (TextView)headView.findViewById(R.id.grade_hint5);
        gradeHint6 = (TextView)headView.findViewById(R.id.grade_hint6);
        mHorzProgressView = (HorzProgressView)headView.findViewById(R.id.horzProgressView);
        rl_duodou_progress = (RelativeLayout)headView.findViewById(R.id.rl_duodou_progress);
        leader_icon = (ImageView)headView.findViewById(R.id.leader_icon);
        updateVip = (TextView) headView.findViewById(R.id.btn_number_update_vip);

    }

    private void updataUser() {
        mUserInfo = UserLocalData.getUser(getActivity());
        if (mUserInfo != null) {
            initViewData(mUserInfo);
        }

    }

    private void getData() {
        getNumberGoodsListPresenter(this, page);
    }


    /**
     * 升级vip的弹框
     */
    private void updateGrade(){

        NumberVipUpgradeDialog leaderUpgradeDialog = new NumberVipUpgradeDialog(getActivity(),R.style.dialog);
        leaderUpgradeDialog.setOnListner(new NumberVipUpgradeDialog.OnListener(){

            @Override
            public void onClick(int type) {
                //vip ==1
                if (type == 1){
                    updateGradePresenter(NumberSubFragment.this,Integer.parseInt(C.UserType.vipMember));
                }else if (type ==2){
                    updateGradePresenter(NumberSubFragment.this,Integer.parseInt(C.UserType.operator));
                }
            }

        });
        leaderUpgradeDialog.show();
    }

    /**
     * 升级团队长的弹框
     */
    private void updateGradeForLeader(){
        NumberLeaderUpgradeDialog vipUpgradeDialog = new NumberLeaderUpgradeDialog(getActivity(),R.style.dialog);
        vipUpgradeDialog.setOnListner(new NumberLeaderUpgradeDialog.OnListener(){

            @Override
            public void onClick(){
                updateGradePresenter(NumberSubFragment.this,Integer.parseInt(C.UserType.operator));
            }

        });
        vipUpgradeDialog.show();

    }

    private void initViewData(UserInfo info) {


        if ("null".equals(info.getHeadImg()) || "NULL".equals(info.getHeadImg()) || TextUtils.isEmpty(info.getHeadImg())) {
            mUserIcon.setImageResource(R.drawable.head_icon);
        } else {
            LoadImgUtils.setImgCircle(getActivity(), mUserIcon, info.getHeadImg(), R.drawable.head_icon);
        }
        userName.setText(info.getNickName());
        refreshUserInfo(info);
        String userType = info.getUserType();
        if (C.UserType.operator.equals(userType)){
            updateVip.setVisibility(View.GONE);
        }else{
            updateVip.setVisibility(View.VISIBLE);
            updateVip.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (mUserInfo != null){
                        if (C.UserType.member.equals(mUserInfo.getUserType())){
                            updateGrade();

                        }else if (C.UserType.vipMember.equals(mUserInfo.getUserType())){
                            updateGradeForLeader();
                        }
                    }

                }
            });
        }
    }
    protected void initData() {
//        LoginUtil.getUserInfo((RxAppCompatActivity) getActivity(), false, new MyAction.OnResultFinally<UserInfo>() {
//            /**
//             * 结束
//             */
//            @Override
//            public void onFinally() {
//                if (mReUseListView.getSwipeList() != null) {
//                    mReUseListView.getSwipeList().setRefreshing(false);
//                }
//
//            }
//
//            @Override
//            public void invoke(UserInfo arg) {
//                page = 1;
//                mReUseListView.getSwipeList().setRefreshing(true);
//                updataUser();
//
//            }
//
//            @Override
//            public void onError() {
//            }
//        });
        updataUser();
        getData();
    }

    private void refreshData() {
//        LoginUtil.getUserInfo((RxAppCompatActivity) getActivity(), false, new MyAction.OnResultFinally<UserInfo>() {
//            /**
//             * 结束
//             */
//            @Override
//            public void onFinally() {
//                if (mReUseListView.getSwipeList() != null) {
//                    mReUseListView.getSwipeList().setRefreshing(false);
//                }
//
//            }
//
//            @Override
//            public void invoke(UserInfo arg) {
//                page = 1;
////                mReUseListView.getListView().setNoMore(false);
//                mReUseListView.getSwipeList().setRefreshing(true);
//                updataUser();
//
//            }
//
//            @Override
//            public void onError() {
//            }
//        });
        updataUser();
        getData();
    }



    /**
     * vip的view
     */
    private void gradeForNumberView(){

        gradeHint1.setText("购物更省钱");
        gradeHint1.setText("分享奖励高" );
        gradeHint1.setText("可申请团队长");
        gradeHint1.setText("大咖辅导");
        gradeHint1.setText("数据化运营");
        gradeHint1.setText("六位邀请码");
    }

    /**
     * vip的view
     */
    private void gradeForVipView(){

        gradeHint1.setText("购物更省钱");
        gradeHint1.setText("分享奖励高" );
        gradeHint1.setText("团队奖不停");
        gradeHint1.setText("大咖辅导");
        gradeHint1.setText("数据化运营");
        gradeHint1.setText("六位邀请码");
    }

    /**
     * 团队长的view
     */
    private void gradeForLeaderView(){

        gradeHint1.setText("购物更省钱");
        gradeHint1.setText("分享奖励高");
        gradeHint1.setText("团队奖不停");
        gradeHint1.setText("大咖辅导");
        gradeHint1.setText("专属后台运营");
        gradeHint1.setText("靓号邀请码");
    }




    public void showSuccessful(NumberGoodsList datas) {
        mReUseListView.getSwipeList().setRefreshing(false);
        mNumberGoodsAdapter.setEnableLoadMore(true);
        List<NumberGoods> list = datas.getList();
        if (list == null || (list != null && list.size() == 0)) {
            mNumberGoodsAdapter.loadMoreEnd();
            return;
        }
        mNumberGoodsAdapter.loadMoreComplete();
        if (page == 1) {
//            if (list.size() % 2 != 0) {   //第一页也可能有奇数
//                list.remove(list.size() - 1);
//            }
            mNumberGoodsAdapter.setNewData(list);
        } else {
            List<NumberGoods> newList = new ArrayList<>();
            newList.addAll(list);
            mNumberGoodsAdapter.addData(newList);
        }
        page++;
        if (mReUseListView.getSwipeList() != null) {
            mReUseListView.getSwipeList().setRefreshing(false);
        }


    }


    public void showError(String errorNo,String msg) {
        MyLog.i("test", "onFailure: " + this);
        if ("B1100007".equals(errorNo)
        ||"B1100008".equals(errorNo)
        ||"B1100009".equals(errorNo)
        || "B1100010".equals(errorNo)) {
            ViewShowUtils.showShortToast(getActivity(),msg);
        }

        mNumberGoodsAdapter.loadMoreEnd();

        if (mReUseListView.getSwipeList() != null) {
            mReUseListView.getSwipeList().setRefreshing(false);
        }



    }


    public void showFinally() {
    }


    public void onGradeSuccess(UpdateInfoBean info) {
        if (info != null){
            UserInfo userInfo = UserLocalData.getUser();
            userInfo.setUserType(String.valueOf(info.getUserType()));
            userInfo.setMoreCoin(info.getMoreCoin());
            UserLocalData.setUser(getActivity(),userInfo);
            refreshUserInfo(userInfo);
        }else{
            MyLog.d("test","用户信息为空");
        }

    }

    private void refreshUserInfo(UserInfo info){
        if (info == null){
            MyLog.d("test","用户信息为空");
            return;
        }

        if (C.UserType.vipMember.equals(info.getUserType())){
            rl_duodou_progress.setVisibility(View.VISIBLE);
            mHorzProgressView.setMax(20000.00);
            mHorzProgressView.setCurrentNum(info.getMoreCoin());
            leader_icon.setVisibility(View.GONE);
            myGradedView.setText("VIP会员");
            numberGradeName.setText("团队长");

            gradeForVipView();
        }else if (C.UserType.operator.equals(info.getUserType())) {
            myGradedView.setText("团队长");
            numberGradeName.setText("团队长");
            rl_duodou_progress.setVisibility(View.GONE);
            leader_icon.setVisibility(View.VISIBLE);
            gradeForLeaderView();
        }else{
            rl_duodou_progress.setVisibility(View.VISIBLE);
            mHorzProgressView.setMax(20000.00);
            Long coin = info.getMoreCoin();
            if (coin != null){
                mHorzProgressView.setCurrentNum(info.getMoreCoin());
            }else{
                mHorzProgressView.setCurrentNum(0);
            }

            leader_icon.setVisibility(View.GONE);
            myGradedView.setText("会员");
            numberGradeName.setText("VIP会员");
            gradeForNumberView();
        }
        Long coin = info.getMoreCoin();
        EventBus.getDefault().post(new MyMoreCoinEvent(coin));

    }

    /**
     * 商品列表
     * @param fragment
     * @param page
     */
    public void getNumberGoodsListPresenter(RxFragment fragment, int page) {
        getNumberGoodsList(fragment, page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                        showFinally();

                    }
                })
                .subscribe(new DataObserver<NumberGoodsList>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        super.onError(errorMsg, errCode);
                        showError(errCode,errorMsg);
                    }

                    @Override
                    protected void onDataListEmpty() {
                if (mReUseListView.getSwipeList() != null) {
                    mReUseListView.getSwipeList().setRefreshing(false);
                }

                    }
                    @Override
                    protected void onSuccess(NumberGoodsList data) {
                        showSuccessful(data);
                    }
                });
    }

    /**
     * 升级
     * @param fragment
     * @param userType
     */
    public void updateGradePresenter(RxFragment fragment,int userType) {
        updateUserGrade(fragment, userType)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                        showFinally();

                    }
                })
                .subscribe(new DataObserver<UpdateInfoBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        super.onError(errorMsg, errCode);
                        showError(errCode,errorMsg);
                    }

                    @Override
                    protected void onDataListEmpty() {

                    }
                    @Override
                    protected void onSuccess(UpdateInfoBean data) {
                        onGradeSuccess(data);
                    }
                });
    }

    public Observable<BaseResponse<NumberGoodsList>> getNumberGoodsList(RxFragment fragment, int page) {
        RequestNumberGoodsList bean = new RequestNumberGoodsList();
        bean.setPage(page);
        bean.setLimit(10);
        return RxHttp.getInstance().getGoodsService().getNumberGoodsList(bean)
                .compose(RxUtils.<BaseResponse<NumberGoodsList>>switchSchedulers())
                .compose(fragment.<BaseResponse<NumberGoodsList>>bindToLifecycle());
    }
    /**
     * 用户等级升级
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<UpdateInfoBean>> updateUserGrade(RxFragment fragment,int userGrade) {
        RequestUpdateUserBean updateUserBean = new RequestUpdateUserBean();
        updateUserBean.setType(userGrade);
        return RxHttp.getInstance().getUsersService().updateUserGrade(updateUserBean)
                .compose(RxUtils.<BaseResponse<UpdateInfoBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<UpdateInfoBean>>bindToLifecycle());
    }


    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        switch (event.getAction()) {
            case EventBusAction.LOGINA_SUCCEED:
                initData();


                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(NumberSubFragment.this);
    }
}
