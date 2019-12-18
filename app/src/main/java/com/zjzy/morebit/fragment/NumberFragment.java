package com.zjzy.morebit.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
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
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Dialog.NumberLeaderUpgradeDialog;
import com.zjzy.morebit.Module.common.Dialog.NumberVipUpgradeDialog;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.goods.shopping.contract.NumberGoodsListContract;
import com.zjzy.morebit.goods.shopping.presenter.NumberGoodsListPresenter;
import com.zjzy.morebit.goods.shopping.ui.NumberGoodsDetailActivity;
import com.zjzy.morebit.goods.shopping.ui.NumberGoodsDetailsActivity;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.List;

import butterknife.BindView;

/**
 * 主页面中的会员页面
 * 升级入口，会员商品列表
 * Created by haiping.liu on 2019-12-04.
 */
public class NumberFragment extends MvpFragment<NumberGoodsListPresenter> implements NumberGoodsListContract.View {
    private static final String TAG = NumberFragment.class.getSimpleName();

    private static final int REQUEST_COUNT = 10;
    private int page = 1;
    private View mView;

    private boolean isUserHint = true;
    private boolean isVisible;

    @BindView(R.id.listview)
    ReUseListView mReUseListView;


    @BindView(R.id.status_bar)
    View status_bar;

    @BindView(R.id.dateNullView)
    LinearLayout mDateNullView;

    @BindView(R.id.my_more_corn)
    TextView myCornView;


    TextView myGradedView;


    TextView updateVip;

    RoundedImageView mUserIcon;


    View headView;

    NumberGoodsAdapter mNumberGoodsAdapter;

    UserInfo mUserInfo;


//    TextView number_grade_name

    TextView numberGradeName;

    TextView gradeHint1;
    TextView gradeHint2;
    TextView gradeHint3;
    TextView gradeHint4;
    TextView gradeHint5;
    TextView gradeHint6;

    TextView moreCoinBiaozhun;

    RelativeLayout processCornAll;

    RelativeLayout processMyCorn;


//    List<NumberGoods> mListArray = new ArrayList<>();
    private boolean isShowGuide = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View view) {

        mNumberGoodsAdapter = new NumberGoodsAdapter(getActivity());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);
            status_bar.setBackgroundResource(R.color.color_333333);
        }

        headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_number_header, null);

        myGradedView = (TextView)headView.findViewById(R.id.lb_user_grade);
        numberGradeName = (TextView)headView.findViewById(R.id.number_grade_name);
        moreCoinBiaozhun = (TextView)headView.findViewById(R.id.more_corn_biaozhun);

        gradeHint1 = (TextView)headView.findViewById(R.id.grade_hint1);
        gradeHint2 = (TextView)headView.findViewById(R.id.grade_hint2);
        gradeHint3 = (TextView)headView.findViewById(R.id.grade_hint3);
        gradeHint4 = (TextView)headView.findViewById(R.id.grade_hint4);
        gradeHint5 = (TextView)headView.findViewById(R.id.grade_hint5);
        gradeHint6 = (TextView)headView.findViewById(R.id.grade_hint6);

        processCornAll = (RelativeLayout)headView.findViewById(R.id.progress_100);
        processMyCorn = (RelativeLayout)headView.findViewById(R.id.process_my_corn);

        updateVip = headView.findViewById(R.id.btn_number_update_vip);


        mReUseListView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mReUseListView.getSwipeList().isRefreshing()) {
                    mPresenter.getNumberGoodsList(NumberFragment.this, page);
                }

            }
        });

        mReUseListView.setAdapterAndHeadView(headView, mNumberGoodsAdapter);
        //更新用户信息
        updataUser();

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
//                    Long coin = mUserInfo.getMoreCoin();
//                    if (coin == null || (coin != null && coin < 3600)){
//                        ViewShowUtils.showShortToast(getActivity(),"多豆不足20000，请积累多豆再试哦");
//                        return;
//                    }
                    mPresenter.updateGrade(NumberFragment.this,Integer.parseInt(C.UserType.vipMember));
                }else if (type ==2){
//                    Long coin = mUserInfo.getMoreCoin();
//                    if (coin == null || (coin != null && coin < 20000)){
//                        ViewShowUtils.showShortToast(getActivity(),"多豆不足20000，请积累多豆再试哦");
//                        return;
//                    }
                    mPresenter.updateGrade(NumberFragment.this,Integer.parseInt(C.UserType.operator));
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
                Long coin = mUserInfo.getMoreCoin();
//                if (coin == null || (coin != null && coin < 20000)){
//                    ViewShowUtils.showShortToast(getActivity(),"多豆不足20000，请积累多豆再试哦");
//                    return;
//                }
                mPresenter.updateGrade(NumberFragment.this,Integer.parseInt(C.UserType.operator));
            }

        });
        vipUpgradeDialog.show();

    }


    private void updataUser() {
         mUserInfo = UserLocalData.getUser(getActivity());
        if (mUserInfo != null) {
            initViewData(mUserInfo);
        }

    }
    private void initViewData(UserInfo info) {


        if ("null".equals(info.getHeadImg()) || "NULL".equals(info.getHeadImg()) || TextUtils.isEmpty(info.getHeadImg())) {
            mUserIcon.setImageResource(R.drawable.head_icon);
        } else {
            LoadImgUtils.setImgCircle(getActivity(), mUserIcon, info.getHeadImg(), R.drawable.head_icon);
        }
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

    private void displayProgress(UserInfo info){
        if (info == null){
            MyLog.d(TAG,"用户信息为空");
        }
        Long corn = info.getMoreCoin();
        double rate = 0;
        if (corn == null){
            rate = 0/20000;
        }else{
            rate = corn/20000;
        }

//        dip2px
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) processCornAll.getLayoutParams();
        RelativeLayout.LayoutParams myCornparams = (RelativeLayout.LayoutParams) processMyCorn.getLayoutParams();
        myCornparams.width = (int)rate*params.width;

        processMyCorn.setLayoutParams(myCornparams);





    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_number;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }
//
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
        }

    }


    @Override
    protected void initData() {
        getData();
    }

    private void refreshData() {

        page = 1;
        mReUseListView.getListView().setNoMore(false);
        mReUseListView.getSwipeList().setRefreshing(true);
        updataUser();
        getData();
    }
    private void getData() {
        mPresenter.getNumberGoodsList(this, page);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showSuccessful(NumberGoodsList datas) {
        List<NumberGoods> list = datas.getList();
        MyLog.i("test", ""+ list.size());
        if (list.size() == 0) {
            mDateNullView.setVisibility(View.VISIBLE);
            mReUseListView.setVisibility(View.GONE);
            return;
        }
        if (page == 1) {
            mNumberGoodsAdapter.clear();

            mNumberGoodsAdapter.add(list);
            mDateNullView.setVisibility(View.GONE);
            mReUseListView.setVisibility(View.VISIBLE);
        } else {
            if (list.size() == 0) {
                mReUseListView.getListView().setNoMore(true);
            } else {
                mNumberGoodsAdapter.add(list);
//                mListArray.addAll(list);
            }

        }
        page++;
//        mNumberGoodsAdapter.setData(mListArray);
        mNumberGoodsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        MyLog.i("test", "onFailure: " + this);
        if (page == 1) {
            mDateNullView.setVisibility(View.VISIBLE);
            mReUseListView.setVisibility(View.GONE);
        } else {
            mDateNullView.setVisibility(View.GONE);
            mReUseListView.setVisibility(View.VISIBLE);
        }
        mReUseListView.getListView().setNoMore(true);

    }

    @Override
    public void showFinally() {
        mReUseListView.getSwipeList().setRefreshing(false);
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
    }

    @Override
    public void onGradeSuccess(UpdateInfoBean info) {
        if (info != null){
            UserInfo userInfo = UserLocalData.getUser();
            userInfo.setUserType(String.valueOf(info.getUserType()));
            userInfo.setMoreCoin(info.getMoreCoin());
            UserLocalData.setUser(getActivity(),userInfo);
            refreshUserInfo(userInfo);
        }else{
            MyLog.d(TAG,"用户信息为空");
        }

    }

    private void refreshUserInfo(UserInfo info){
        displayProgress(info);
        if (C.UserType.vipMember.equals(info.getUserType())){
            myGradedView.setText("VIP会员");
            numberGradeName.setText("团队长");
            gradeForVipView();
        }else if (C.UserType.operator.equals(info.getUserType())) {
            myGradedView.setText("团队长");
            numberGradeName.setText("团队长");
            gradeForLeaderView();
        }else{
            myGradedView.setText("会员");
            numberGradeName.setText("VIP会员");
            gradeForNumberView();
        }
        Long coin = info.getMoreCoin();
        if (coin == null){
            myCornView.setText("0");
        }else{
            myCornView.setText(String.valueOf(info.getMoreCoin()));
        }

    }

    /**
     * 会员商品适配器
     */
    class NumberGoodsAdapter extends SimpleAdapter<NumberGoods, SimpleViewHolder> {

//        List<NumberGoods> datas;
        Context mContext;

        public NumberGoodsAdapter(Context context) {
            super(context);
//            this.datas = datas;
            mContext = context;

        }
//        public void setData(List<NumberGoods> datas) {
//            this.datas.clear();
//            if (datas != null && datas.size() > 0) {
//                this.datas.addAll(datas);
//            }
//        }
        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
            if (holder instanceof NumberGoodsHolder ){
                bindNumberGoodsHolder((NumberGoodsHolder) holder,position);
            }

        }

        private void bindNumberGoodsHolder(NumberGoodsHolder holder, int position) {
            final NumberGoods goods =  getItem(position);
            String img = goods.getPicUrl();
            if (!TextUtils.isEmpty(img)) {
                LoadImgUtils.setImg(mContext, holder.pic, img);
            }
            holder.desc.setText(goods.getName());
            String price = goods.getRetailPrice();
            if (TextUtils.isEmpty(price)){
                holder.price.setText(getResources().getString(R.string.number_goods_price,"0"));
            }else{
                holder.price.setText(getResources().getString(R.string.number_goods_price,price));
            }
            String moreCoin = MathUtils.getMorebitCorn(price);
            holder.morebitCorn.setText(getResources().getString(R.string.number_give_more_corn,moreCoin));
            holder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
//                    NumberGoodsDetailActivity.start(getActivity(),String.valueOf(goods.getId()));
                    NumberGoodsDetailsActivity.start(getActivity(),String.valueOf(goods.getId()));
                }
            });

        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NumberGoodsHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_number_goods, parent, false));
        }

        public class NumberGoodsHolder extends SimpleViewHolder {
            @BindView(R.id.number_goods_pic)
            ImageView pic;

            @BindView(R.id.number_goods_desc)
            TextView desc;

            @BindView(R.id.number_goods_price)
            TextView price;

            @BindView(R.id.txt_morebit_corn)
            TextView morebitCorn;

            public NumberGoodsHolder(View itemView) {
                super(itemView);
            }
        }
    }



}
