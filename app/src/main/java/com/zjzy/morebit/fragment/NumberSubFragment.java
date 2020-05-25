package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
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
import com.zjzy.morebit.Activity.SkillClassActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Dialog.NumberLeaderUpgradeDialog;
import com.zjzy.morebit.Module.common.Dialog.NumberVipUpgradeDialog;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.common.View.NumberReUseGridView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ActivityFloorAdapter;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.SkillAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.RefreshUserInfoEvent;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
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
    private ImageView huiyuan1;
    private TextView get_operator_growth,tv_vip,tv_tuanduizhang,tv_huiyuan2,tv_vip2,vip_optional,vip_settlement,vip_directly,vip_intermedium,tv_more;
    private ImageView grade,img_vip,img_tuanduizhang;
    private LinearLayout vip_reward,ll4,ll5,ll3;
    private RelativeLayout vip_rl1,vip_rl3;
    private View view1,view2;
    private RecyclerView skill_rcy,activity_rcy;
    private SkillAdapter skillAdapter;
    private ActivityFloorAdapter  floorAdapter;



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
            headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_number2_header, null);
            initHeadView(headView);
            initView(mView);
            initTan();
             initData();
            initPush();
            refreshData();
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

        mReUseGridView.setNestedScrollingEnabled(false);




    }



    private void initHeadView(View headView){
        userName = (TextView)headView.findViewById(R.id.user_name);
        mUserIcon = (RoundedImageView)headView.findViewById(R.id.userIcon);
         moreCoinBiaozhun = (TextView)headView.findViewById(R.id.more_corn_biaozhun);
         mHorzProgressView = (HorzProgressView)headView.findViewById(R.id.horzProgressView);
//
         updateVip = (TextView) headView.findViewById(R.id.btn_number_update_vip);
        tvGrowthValue = (TextView)headView.findViewById(R.id.tv_growth_value);
        grade=(ImageView) headView.findViewById(R.id.grade);
        vip_reward=headView.findViewById(R.id.vip_reward);
        vip_rl1=headView.findViewById(R.id.vip_rl1);
        vip_rl3=headView.findViewById(R.id.vip_rl3);
        get_operator_growth=headView.findViewById(R.id.get_operator_growth);
        img_vip=headView.findViewById(R.id.img_vip);
        tv_vip=headView.findViewById(R.id.tv_vip);
        img_tuanduizhang=headView.findViewById(R.id.img_tuanduizhang);
        tv_tuanduizhang=headView.findViewById(R.id.tv_tuanduizhang);
        view1=headView.findViewById(R.id.view1);
        view2=headView.findViewById(R.id.view2);
        tv_huiyuan2=headView.findViewById(R.id.tv_huiyuan2);
        tv_vip2=headView.findViewById(R.id.tv_vip2);
        ll3=headView.findViewById(R.id.ll3);
        ll4=headView.findViewById(R.id.ll4);
        ll5=headView.findViewById(R.id.ll5);
        skill_rcy=headView.findViewById(R.id.skill_rcy);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        skill_rcy.setLayoutManager(manager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(skill_rcy);
        skill_rcy.setNestedScrollingEnabled(false);
        huiyuan1=headView.findViewById(R.id.huiyuan1);
        vip_optional=headView.findViewById(R.id.vip_optional);//自选商品
        vip_settlement=headView.findViewById(R.id.vip_settlement);//结算
        vip_directly=headView.findViewById(R.id.vip_directly);//直属
        vip_intermedium=headView.findViewById(R.id.vip_intermedium);//间属
        activity_rcy=headView.findViewById(R.id.activity_rcy);//活动专区
        GridLayoutManager manager2=new GridLayoutManager(getActivity(),2);
        activity_rcy.setLayoutManager(manager2);
        tv_more = headView.findViewById(R.id.tv_more);
        tv_more.setOnClickListener(new View.OnClickListener() {//跳转技能课堂
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SkillClassActivity.class));
            }
        });


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
                if (mUserInfo.getMoreCoin()<360){
                    ToastUtils.showLong("成长值不足");
                }else{
                    updateGradePresenter(NumberSubFragment.this,Integer.parseInt(C.UserType.vipMember));
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
                if (mUserInfo.getMoreCoin()<50000){
                    ToastUtils.showLong("成长值不足");
                }else{
                    updateGradePresenter(NumberSubFragment.this,Integer.parseInt(C.UserType.operator));
                }

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
        getUserDetails(this).doFinally(new Action() {
            @Override
            public void run() throws Exception {
            }
        })
                .subscribe(new DataObserver<UserInfo>() {
                    @Override
                    protected void onSuccess( UserInfo data) {
                      showDetailsView(data);

                    }
                });

        getSkillClass(this).compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(this.<BaseResponse<List<Article>>>bindToLifecycle())
                .subscribe(new DataObserver<List<Article>>() {
                    @Override
                    protected void onSuccess(List<Article> data) {
                        if (data!=null){
                            skillAdapter=new SkillAdapter(getActivity(),data);
                            if (skillAdapter!=null){
                                skill_rcy.setAdapter(skillAdapter);
                            }

                        }
                    }
                });

        getVipFloor(this).compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ImageInfo>>>bindToLifecycle())
                .subscribe(new DataObserver<List<ImageInfo>>() {
                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        if (data!=null){
                            floorAdapter=new ActivityFloorAdapter(getActivity(),data);
                            if (floorAdapter!=null){
                                activity_rcy.setAdapter(floorAdapter);
                            }

                        }
                    }
                });
        getData();
    }
    //获取用户详情
    private void showDetailsView(UserInfo data) {
        vip_intermedium.setText(data.getIndirectCoin()+"");//间属
        vip_directly.setText(data.getDirectCoin()+"");//直属
        vip_settlement.setText(data.getSettleCoin()+"");//结算
        vip_optional.setText(data.getSettleCoin()+"");//自购
    }


    /**
     * number的view
     */
    private void gradeForNumberView(){
        huiyuan1.setImageResource(R.mipmap.huiyuan1);
        vip_rl1.setVisibility(View.VISIBLE);
        vip_rl3.setVisibility(View.GONE);
        vip_reward.setVisibility(View.GONE);
        grade.setImageResource(R.mipmap.icon_huiyuan);
        img_vip.setImageResource(R.mipmap.vip2);
        tv_vip.setTextColor(Color.parseColor("#C4C1C1"));
        tv_tuanduizhang.setTextColor(Color.parseColor("#C4C1C1"));
        img_tuanduizhang.setImageResource(R.mipmap.tuanduizhang2);
        view1.setBackgroundResource(R.drawable.background_f2f2f2_radius_1dp);
        view2.setBackgroundResource(R.drawable.background_f2f2f2_radius_1dp);
        tv_huiyuan2.setText("会员权益");
        tv_vip2.setText("VIP专享权益");
        ll3.setBackgroundResource(R.mipmap.huiyuan3);
        ll4.setVisibility(View.VISIBLE);
        ll5.setVisibility(View.GONE);
        tv_huiyuan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_huiyuan2.setTextSize(18);
                tv_vip2.setTextSize(16);
                tv_vip2.setTextColor(Color.parseColor("#CFC5BA"));
                tv_huiyuan2.setTextColor(Color.parseColor("#EFD3B7"));
                ll3.setBackgroundResource(R.mipmap.huiyuan3);
            }
        });
        tv_vip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_huiyuan2.setTextSize(16);
                tv_vip2.setTextSize(18);
                tv_vip2.setTextColor(Color.parseColor("#EFD3B7"));
                tv_huiyuan2.setTextColor(Color.parseColor("#CFC5BA"));
                ll3.setBackgroundResource(R.mipmap.vip_bg3);
            }
        });

    }

    /**
     * vip的view
     */
    private void gradeForVipView(){
        huiyuan1.setImageResource(R.mipmap.vip1);
        grade.setImageResource(R.mipmap.icon_vip);
        vip_reward.setVisibility(View.VISIBLE);
        vip_rl1.setVisibility(View.VISIBLE);
        vip_rl3.setVisibility(View.GONE);
        img_vip.setImageResource(R.mipmap.vip3);
        tv_vip.setTextColor(Color.parseColor("#765F5F"));
        tv_tuanduizhang.setTextColor(Color.parseColor("#C4C1C1"));
        img_tuanduizhang.setImageResource(R.mipmap.tuanduizhang2);
        view1.setBackgroundResource(R.drawable.background_fff2e5_radius_1dp);
        view2.setBackgroundResource(R.drawable.background_f2f2f2_radius_1dp);
        tv_huiyuan2.setText("VIP专享权益");
        tv_vip2.setText("团队长尊享权益");
        ll3.setBackgroundResource(R.mipmap.vip_bg3);
        ll4.setVisibility(View.VISIBLE);
        ll5.setVisibility(View.GONE);
        tv_huiyuan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_huiyuan2.setTextSize(18);
                tv_vip2.setTextSize(16);
                tv_vip2.setTextColor(Color.parseColor("#CFC5BA"));
                tv_huiyuan2.setTextColor(Color.parseColor("#EFD3B7"));
                ll3.setBackgroundResource(R.mipmap.vip_bg3);
            }
        });
        tv_vip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_huiyuan2.setTextSize(16);
                tv_vip2.setTextSize(18);
                tv_vip2.setTextColor(Color.parseColor("#EFD3B7"));
                tv_huiyuan2.setTextColor(Color.parseColor("#CFC5BA"));
                ll3.setBackgroundResource(R.mipmap.tuanduizhang_bg3);
            }
        });
    }

    /**
     * 团队长的view
     */
    private void gradeForLeaderView(){
        vip_rl1.setVisibility(View.GONE);
        vip_rl3.setVisibility(View.VISIBLE);
        grade.setImageResource(R.mipmap.icon_tuanduizhang);
        vip_reward.setVisibility(View.GONE);
        tv_tuanduizhang.setTextColor(Color.parseColor("#765F5F"));
        img_tuanduizhang.setImageResource(R.mipmap.tuanduizhang3);
        img_vip.setImageResource(R.mipmap.vip3);
        tv_vip.setTextColor(Color.parseColor("#765F5F"));
        view1.setBackgroundResource(R.drawable.background_fff2e5_radius_1dp);
        view2.setBackgroundResource(R.drawable.background_fff2e5_radius_1dp);
        ll3.setBackgroundResource(R.mipmap.tuanduizhang_bg3);
        ll4.setVisibility(View.GONE);
        ll5.setVisibility(View.VISIBLE);


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
            mHorzProgressView.setMax(50000.00);
            mHorzProgressView.setCurrentNum(info.getMoreCoin());
            Long moreCoin = info.getMoreCoin();
            String coin1 ;
            if (moreCoin == null){
                coin1 = "成长值：" +"0/50000";
            }else{
                coin1 = "成长值：" +moreCoin+"/50000";
            }
            moreCoinBiaozhun.setText(coin1);
            Long growthValue = 50000 - info.getMoreCoin();
            if (growthValue>0){
                tvGrowthValue.setVisibility(View.VISIBLE);
                tvGrowthValue.setText(getResources().getString(R.string.vip_growth_value,
                        growthValue.toString()));
            }else{
                tvGrowthValue.setVisibility(View.INVISIBLE);
            }

            gradeForVipView();
        }else if (C.UserType.operator.equals(info.getUserType())) {
            gradeForLeaderView();
            get_operator_growth.setText("成长值： "+info.getMoreCoin());
        }else{
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
            Long growthValue = 360 - coin;
            moreCoinBiaozhun.setText(coin1);
            if (growthValue>0){
                tvGrowthValue.setVisibility(View.VISIBLE);
                tvGrowthValue.setText(getResources().getString(R.string.number_growth_value,
                        growthValue.toString()));
            }else{
                tvGrowthValue.setVisibility(View.INVISIBLE);
            }
            gradeForNumberView();
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
     * 获取楼层
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<ImageInfo>>> getVipFloor(RxFragment fragment) {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setOs(1);

        return RxHttp.getInstance().getGoodsService().getVipFloor(requestBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ImageInfo>>>bindToLifecycle());
    }
    /**
     * 获取技能课程
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<Article>>> getSkillClass(RxFragment fragment) {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setOs(1);

        return RxHttp.getInstance().getGoodsService().getVipSkillClass(requestBean)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<Article>>>bindToLifecycle());
    }
    /**
     * 用户详情
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<UserInfo>> getUserDetails(RxFragment fragment) {
        UserInfo bean = new UserInfo();
        UserInfo  mUserInfo = UserLocalData.getUser(getActivity());
        bean.setId(mUserInfo.getId());

        return RxHttp.getInstance().getGoodsService().getUserDetails(bean)
                .compose(RxUtils.<BaseResponse<UserInfo>>switchSchedulers())
                .compose(fragment.<BaseResponse<UserInfo>>bindToLifecycle());
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
                tvPrice.setText(MathUtils.getnum(price));
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
