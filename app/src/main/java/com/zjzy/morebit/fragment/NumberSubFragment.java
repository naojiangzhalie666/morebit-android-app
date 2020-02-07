package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.NumberGoodsDetailsActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Dialog.NumberLeaderUpgradeDialog;
import com.zjzy.morebit.Module.common.Dialog.NumberVipUpgradeDialog;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.common.View.NumberReUseGridView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.MyMoreCoinEvent;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.view.HorzProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * 每日爆款
 */
public class NumberSubFragment extends BaseFragment {

    private static final int REQUEST_COUNT = 10;
    View mView;

    NumberReUseGridView mReUseGridView;



//    TextView gradeHint1;
//    TextView gradeHint2;
//    TextView gradeHint3;
//    TextView gradeHint4;
//    TextView gradeHint5;
//    TextView gradeHint6;
    RoundedImageView mUserIcon;
    TextView myGradedView;
//    TextView numberGradeName;
    TextView moreCoinBiaozhun;

    HorzProgressView mHorzProgressView;
    ImageView leader_icon;

    RelativeLayout rl_duodou_progress;
    TextView updateVip;
    View headView ;
    TextView userName;

    SubNumberAdapter mAdapter;
    RoundedImageView cardNumber;
    RoundedImageView cardVip;
    RoundedImageView cardLeader;



    UserInfo mUserInfo;
    private int page = 1;



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
        mReUseGridView = (NumberReUseGridView)view.findViewById(R.id.mListView);

//        mReUseGridView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.NumberSwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                refreshData();
//            }
//        });
        mReUseGridView.setOnReLoadListener(new NumberReUseGridView.OnReLoadListener() {
            @Override
            public void onReload() {
                page = 1;
                refreshData();
            }

            @Override
            public void onLoadMore() {
                getData();
            }
        });

        mAdapter = new SubNumberAdapter(getActivity());

        mReUseGridView.setAdapterAndHeadView(headView,mAdapter);

//        mReUseListView.setAdapter(mNumberGoodsAdapter, new ReUseNumberGoodsView.RefreshAndLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
////                if (!mReUseListView.getSwipeList().isRefreshing())
//                MyLog.i("test", "setRecommendData1");
//                getData();
//            }
//
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                isLoadData = true;
//                page = 1;
//                mReUseListView.getSwipeList().setRefreshing(true);
//                mNumberGoodsAdapter.setEnableLoadMore(false);
//
//                MyLog.i("test", "refreshData");
//                refreshData();
//            }
//        });

    }

    private void initHeadView(View headView){
        userName = (TextView)headView.findViewById(R.id.user_name);
        mUserIcon = (RoundedImageView)headView.findViewById(R.id.userIcon);
        myGradedView = (TextView)headView.findViewById(R.id.lb_user_grade);
//        numberGradeName = (TextView)headView.findViewById(R.id.number_grade_name);
        moreCoinBiaozhun = (TextView)headView.findViewById(R.id.more_corn_biaozhun);
        cardNumber = (RoundedImageView)headView.findViewById(R.id.card_number);
        cardVip = (RoundedImageView)headView.findViewById(R.id.card_vip);
        cardLeader = (RoundedImageView)headView.findViewById(R.id.card_leader);
//        gradeHint1 = (TextView)headView.findViewById(R.id.grade_hint1);
//        gradeHint2 = (TextView)headView.findViewById(R.id.grade_hint2);
//        gradeHint3 = (TextView)headView.findViewById(R.id.grade_hint3);
//        gradeHint4 = (TextView)headView.findViewById(R.id.grade_hint4);
//        gradeHint5 = (TextView)headView.findViewById(R.id.grade_hint5);
//        gradeHint6 = (TextView)headView.findViewById(R.id.grade_hint6);
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
        updataUser();
        getData();
    }

    private void refreshData() {
        updataUser();
        getData();
    }



    /**
     * vip的view
     */
    private void gradeForNumberView(){

          cardNumber.setVisibility(View.VISIBLE);
          cardVip.setVisibility(View.GONE);
          cardLeader.setVisibility(View.GONE);
//        gradeHint1.setText("购物更省钱");
//        gradeHint2.setText("分享奖励高" );
//        gradeHint3.setText("可申请团队长");
//        gradeHint4.setText("大咖辅导");
//        gradeHint5.setText("数据化运营");
//        gradeHint6.setText("六位邀请码");
    }

    /**
     * vip的view
     */
    private void gradeForVipView(){
        cardVip.setVisibility(View.VISIBLE);
        cardNumber.setVisibility(View.GONE);
        cardLeader.setVisibility(View.GONE);
//        gradeHint1.setText("购物更省钱");
//        gradeHint2.setText("分享奖励高" );
//        gradeHint3.setText("团队奖不停");
//        gradeHint4.setText("大咖辅导");
//        gradeHint5.setText("数据化运营");
//        gradeHint6.setText("六位邀请码");
    }

    /**
     * 团队长的view
     */
    private void gradeForLeaderView(){
        cardNumber.setVisibility(View.GONE);
        cardVip.setVisibility(View.GONE);
        cardLeader.setVisibility(View.VISIBLE);
//        gradeHint1.setText("购物更省钱");
//        gradeHint2.setText("分享奖励高");
//        gradeHint3.setText("团队奖不停");
//        gradeHint4.setText("大咖辅导");
//        gradeHint5.setText("专属后台运营");
//        gradeHint6.setText("六位邀请码");
    }




    public void showSuccessful(NumberGoodsList datas) {

//        mReUseListView.getSwipeList().setRefreshing(false);
//        mNumberGoodsAdapter.setEnableLoadMore(true);
//        List<NumberGoods> list = datas.getList();
//        if (list == null || (list != null && list.size() == 0)) {
//            mNumberGoodsAdapter.loadMoreEnd();
//            return;
//        }
//        mNumberGoodsAdapter.loadMoreComplete();
//        if (page == 1) {
//            mNumberGoodsAdapter.setNewData(list);
//        } else {
//            List<NumberGoods> newList = new ArrayList<>();
//            newList.addAll(list);
//            mNumberGoodsAdapter.addData(newList);
//        }
//        page++;
//        if (mReUseListView.getSwipeList() != null) {
//            mReUseListView.getSwipeList().setRefreshing(false);
//        }
        List<NumberGoods> list = datas.getList();
        if (list == null || (list != null && list.size() == 0)) {
            mReUseGridView.getListView().setNoMore(true);
            return;
        }
        if (page == 1) {
            mAdapter.replace(list);
        } else {
            if (list.size() == 0) {
                mReUseGridView.getListView().setNoMore(true);
            } else {
                mAdapter.add(list);
            }

        }
        page++;
        mAdapter.notifyDataSetChanged();
    }


    public void showError(String errorNo,String msg) {
        MyLog.i("test", "onFailure: " + this);
        if ("B1100007".equals(errorNo)
        ||"B1100008".equals(errorNo)
        ||"B1100009".equals(errorNo)
        || "B1100010".equals(errorNo)) {
            ViewShowUtils.showShortToast(getActivity(),msg);
        }
        if (page != 1)
            mReUseGridView.getListView().setNoMore(true);

//        mNumberGoodsAdapter.loadMoreEnd();
//
//        if (mReUseListView.getSwipeList() != null) {
//            mReUseListView.getSwipeList().setRefreshing(false);
//        }



    }


    public void showFinally() {
        LoadingView.dismissDialog();
        mReUseGridView.getSwipeList().setRefreshing(false);
        mReUseGridView.getListView().refreshComplete(REQUEST_COUNT);
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
            Long moreCoin = info.getMoreCoin();
            String coin1 ;
            if (moreCoin == null){
                coin1 = "多豆：" +"0/20000";
            }else{
                coin1 = "多豆：" +info.getMoreCoin()+"/20000";
            }
            moreCoinBiaozhun.setText(coin1);

            gradeForVipView();
        }else if (C.UserType.operator.equals(info.getUserType())) {
            myGradedView.setText("团队长");
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
            Long moreCoin = info.getMoreCoin();
            String coin1 ;
            if (moreCoin == null){
                coin1 = "多豆：" +"0/20000";
            }else{
                coin1 = "多豆：" +info.getMoreCoin()+"/20000";
            }
            moreCoinBiaozhun.setText(coin1);

            leader_icon.setVisibility(View.GONE);
            myGradedView.setText("会员");
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
    //                if (mReUseListView.getSwipeList() != null) {
    //                    mReUseListView.getSwipeList().setRefreshing(false);
    //                }

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
            case EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA:
                mUserInfo = UserLocalData.getUser();
                if ("null".equals(mUserInfo.getHeadImg()) || "NULL".equals(mUserInfo.getHeadImg()) || TextUtils.isEmpty(mUserInfo.getHeadImg())) {
                    mUserIcon.setImageResource(R.drawable.head_icon);
                } else {
                    LoadImgUtils.setImgCircle(getActivity(), mUserIcon, mUserInfo.getHeadImg(), R.drawable.head_icon);
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(NumberSubFragment.this);
    }


    class SubNumberAdapter extends SimpleAdapter<NumberGoods, SimpleViewHolder> {

        private Activity mContext;

        public SubNumberAdapter(Activity context) {
            super(context);
            mContext = context;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
           final NumberGoods goods = getItem(position);
            RoundedImageView pic = holder.viewFinder().view(R.id.number_goods_pic);
            TextView desc = holder.viewFinder().view(R.id.number_goods_desc);
            TextView tvPrice = holder.viewFinder().view(R.id.number_goods_price);
            TextView morebitCorn = holder.viewFinder().view(R.id.txt_morebit_corn);

            String img = goods.getPicUrl();
            if (!TextUtils.isEmpty(img)) {
                LoadImgUtils.setImg(mContext, pic, img);
            }
            desc.setText(goods.getName());
            String price = goods.getRetailPrice();
            double  pricedouble  = Double.parseDouble(price);


            if (TextUtils.isEmpty(price)){
                tvPrice.setText("0");
            }else{
                //            holder.price.setText(String.valueOf(((Number)pricedouble).longValue()));
                tvPrice.setText(price);
            }
            String moreCoin = MathUtils.getMorebitCorn(price);
            morebitCorn.setText(mContext.getResources().getString(R.string.number_give_more_corn,moreCoin));
            holder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    NumberGoodsDetailsActivity.start(mContext,String.valueOf(goods.getId()));
                }
            });

        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_number_goods, parent, false);
        }

    }
}
