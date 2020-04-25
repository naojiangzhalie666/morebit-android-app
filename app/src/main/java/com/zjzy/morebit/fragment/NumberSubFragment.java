package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
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
import com.zjzy.morebit.pojo.event.RefreshUserInfoEvent;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
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




    TextView tvUserType;

//    @BindView(R.id.ll_user_grade)
    LinearLayout llUserGrade;

    RoundedImageView mUserIcon;
    TextView myGradedView;

    TextView moreCoinBiaozhun;

    HorzProgressView mHorzProgressView;
//    ImageView leader_icon;

    RelativeLayout rl_duodou_progress;
    TextView updateVip;
    View headView ;
    TextView userName;
    TextView txtWelcome;

    SubNumberAdapter mAdapter;
    RoundedImageView cardNumber;
    RoundedImageView cardVip;
    RoundedImageView cardLeader;

    TextView tvGrowthValue;



    UserInfo mUserInfo;
    private int page = 1;
    private ImageView user_king;
    private String extra;



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
            initTan();
            initData();
            initPush();
        }
        return mView;
    }

    private void initPush() {

        String growth = SPUtils.getInstance().getString("growth");
        if (growth!=null){
            if (growth.equals("360")){
                updateGrade();
                SPUtils.getInstance().remove("growth");
            }

            if (growth.equals("50000")){
                updateGradeForLeader();
                SPUtils.getInstance().remove("growth");
            }
        }
    }


    public static NumberSubFragment newInstance() {
        NumberSubFragment fragment = new NumberSubFragment();
//        Bundle args = new Bundle();
//        args.putString("extra", extra);
//        fragment.setArguments(args);
        return fragment;
    }

    public void initView(View view) {
        mReUseGridView = (NumberReUseGridView) view.findViewById(R.id.mListView);
        mReUseGridView.setOnReLoadListener(new NumberReUseGridView.OnReLoadListener() {
            @Override
            public void onReload() {
                page = 1;
                refreshData();
            }

            @Override
            public void onLoadMore() {
                page++;
                getData();
            }
        });

        mAdapter = new SubNumberAdapter(getActivity());

        mReUseGridView.setAdapterAndHeadView(headView, mAdapter);






    }



    private void initHeadView(View headView){

        txtWelcome = (TextView)headView.findViewById(R.id.txt_number_welcome_hint);
        userName = (TextView)headView.findViewById(R.id.user_name);
        mUserIcon = (RoundedImageView)headView.findViewById(R.id.userIcon);
        myGradedView = (TextView)headView.findViewById(R.id.tv_user_type);
//        numberGradeName = (TextView)headView.findViewById(R.id.number_grade_name);
        moreCoinBiaozhun = (TextView)headView.findViewById(R.id.more_corn_biaozhun);
        cardNumber = (RoundedImageView)headView.findViewById(R.id.card_number);
        cardVip = (RoundedImageView)headView.findViewById(R.id.card_vip);
        cardLeader = (RoundedImageView)headView.findViewById(R.id.card_leader);
        mHorzProgressView = (HorzProgressView)headView.findViewById(R.id.horzProgressView);
        rl_duodou_progress = (RelativeLayout)headView.findViewById(R.id.rl_duodou_progress);

        updateVip = (TextView) headView.findViewById(R.id.btn_number_update_vip);
        tvUserType= (TextView)headView.findViewById(R.id.tv_user_type);
        llUserGrade = (LinearLayout)headView.findViewById(R.id.ll_user_grade);
        tvGrowthValue = (TextView)headView.findViewById(R.id.tv_growth_value);
        user_king = headView.findViewById(R.id.user_king);
    }

    private void updataUser() {

        UserInfo  mUserInfo = UserLocalData.getUser(getActivity());
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
            public void onClick() {
                    updateGradePresenter(NumberSubFragment.this,Integer.parseInt(C.UserType.vipMember));
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

        if (C.UserType.member.equals(info.getPartner())) {
            tvUserType.setText("会员");
            llUserGrade.setBackgroundResource(R.drawable.bg_grade_member_2dp);
            user_king.setVisibility(View.GONE);

        } else if (C.UserType.vipMember.equals(info.getPartner())) {
            tvUserType.setText("VIP");
            llUserGrade.setBackgroundResource(R.drawable.bg_gray_grade_vip);
            user_king.setVisibility(View.GONE);
        } else if (C.UserType.operator.equals(info.getPartner())) {
            tvUserType.setText("团队长");
            llUserGrade.setBackgroundResource(R.drawable.bg_grade_leader_2dp);
            user_king.setVisibility(View.VISIBLE);

        }
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
                            if (mUserInfo.getMoreCoin()<360){
                                ToastUtils.showLong("成长值不足");
                            }else{
                                updateGrade();

                            }

                        }else if (C.UserType.vipMember.equals(mUserInfo.getUserType())){
                            if (mUserInfo.getMoreCoin()<50000){
                                ToastUtils.showLong("成长值不足");
                            }else{
                                updateGradeForLeader();
                            }

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

    @Override
    public void onResume() {
        super.onResume();
        refreshData();

    }

    private void initTan() {
        UserInfo  mUserInfo = UserLocalData.getUser(getActivity());
        if (mUserInfo==null){
            return;
        }
        Long moreCoin = mUserInfo.getMoreCoin();
        if (moreCoin!=null){
            if (moreCoin>360){
                if (C.UserType.member.equals(mUserInfo.getUserType())){
                    updateGrade();
                }

            }
            if(moreCoin>50000){
                if (C.UserType.vipMember.equals(mUserInfo.getUserType())){
                    updateGradeForLeader();
                }

            }
        }


    }

    private void refreshData() {
        LoginUtil.getUserInfo((RxAppCompatActivity) getActivity(), false, new MyAction.OnResultFinally<UserInfo>() {
            /**
             * 结束
             */
            @Override
            public void onFinally() {

            }

            @Override
            public void invoke(UserInfo arg) {
                updataUser();
            }

            @Override
            public void onError() {
            }
        });
        getData();
    }



    /**
     * number的view
     */
    private void gradeForNumberView(){

          cardNumber.setVisibility(View.VISIBLE);
          cardVip.setVisibility(View.GONE);
          cardLeader.setVisibility(View.GONE);
        txtWelcome.setText(getResources().getString(R.string.number_welcome_hint));

    }

    /**
     * vip的view
     */
    private void gradeForVipView(){
        cardVip.setVisibility(View.VISIBLE);
        cardNumber.setVisibility(View.GONE);
        cardLeader.setVisibility(View.GONE);
        txtWelcome.setText(getResources().getString(R.string.vip_welcome_hint));

    }

    /**
     * 团队长的view
     */
    private void gradeForLeaderView(){
        cardNumber.setVisibility(View.GONE);
        cardVip.setVisibility(View.GONE);
        cardLeader.setVisibility(View.VISIBLE);
        txtWelcome.setText(getResources().getString(R.string.tuandui_welcome_hint));

    }




    public void showSuccessful(NumberGoodsList datas) {
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
            EventBus.getDefault().post(new RefreshUserInfoEvent());
//            refreshUserInfo(userInfo);
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
            mHorzProgressView.setMax(50000.00);
            mHorzProgressView.setCurrentNum(info.getMoreCoin());
            llUserGrade.setBackgroundResource(R.drawable.bg_gray_grade_vip);
            myGradedView.setText("VIP");
            updateVip.setVisibility(View.VISIBLE);
            Long moreCoin = info.getMoreCoin();
            String coin1 ;
            if (moreCoin == null){
                coin1 = "成长值：" +"0/50000";
            }else{
                coin1 = "成长值：" +moreCoin+"/50000";
            }
            moreCoinBiaozhun.setText(coin1);
            Long growthValue = 50000 - info.getMoreCoin();
            if (growthValue > 0 ){
                tvGrowthValue.setText(getResources().getString(R.string.vip_growth_value,
                        growthValue.toString()));
            }else {
                tvGrowthValue.setText("立即升级尊享高佣权益");
            }
            user_king.setVisibility(View.GONE);
            gradeForVipView();
        }else if (C.UserType.operator.equals(info.getUserType())) {
            myGradedView.setText("团队长");
            llUserGrade.setBackgroundResource(R.drawable.bg_grade_leader_2dp);
            rl_duodou_progress.setVisibility(View.GONE);
            user_king.setVisibility(View.VISIBLE);
            updateVip.setVisibility(View.GONE);

            gradeForLeaderView();
        }else{
            rl_duodou_progress.setVisibility(View.VISIBLE);
            mHorzProgressView.setMax(360.00);
            Long coin = info.getMoreCoin();
            String coin1 ;
            if (coin != null){
                mHorzProgressView.setCurrentNum(info.getMoreCoin());
                coin1 = "成长值：" +info.getMoreCoin()+"/360";
            }else{
                mHorzProgressView.setCurrentNum(0);
                coin1 = "成长值：" +"0/360";
                return;
            }
            llUserGrade.setBackgroundResource(R.drawable.bg_grade_member_2dp);
            user_king.setVisibility(View.GONE);
            moreCoinBiaozhun.setText(coin1);
            if (coin < 360){
                tvGrowthValue.setText(getResources().getString(R.string.number_growth_value,
                        String.valueOf(360-coin)));
            }else{
                tvGrowthValue.setText("立即升级尊享高佣权益");
            }
           // leader_icon.setVisibility(View.GONE);
            myGradedView.setText("会员");
            gradeForNumberView();
            updateVip.setVisibility(View.VISIBLE);
        }



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


    @Subscribe
    public void onEventMainThread(RefreshUserInfoEvent event){
        updataUser();
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



            if (TextUtils.isEmpty(price)){
                tvPrice.setText("0");
            }else{
                double  pricedouble  = Double.parseDouble(price);
                long pricelong = ((Number)pricedouble).longValue();
                if (pricelong == 0){
                    tvPrice.setText(price);
                }else{
                    tvPrice.setText(String.valueOf(pricelong));
                }
//                tvPrice.setText(price);
            }
            String moreCoin = MathUtils.getMorebitCorn(price);
            morebitCorn.setText(mContext.getResources().getString(R.string.give_growth_value,moreCoin));
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
