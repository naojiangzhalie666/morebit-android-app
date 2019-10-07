package com.jf.my.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jf.my.Activity.SettingActivity;
import com.jf.my.App;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.Module.common.Dialog.ClearSDdataDialog;
import com.jf.my.Module.common.Dialog.EarningsHintDialog;
import com.jf.my.Module.common.Dialog.UpgradeUserDialog;
import com.jf.my.Module.common.Dialog.WithdrawErrorDialog;
import com.jf.my.Module.common.widget.SwipeRefreshLayout;
import com.jf.my.R;
import com.jf.my.circle.ui.OpinionpReplyFragment;
import com.jf.my.contact.EventBusAction;
import com.jf.my.fragment.base.BaseMainFragmeng;
import com.jf.my.info.model.InfoModel;
import com.jf.my.info.ui.FansActivity;
import com.jf.my.info.ui.SettingMineInfoActivity;
import com.jf.my.info.ui.SettingWechatActivity;
import com.jf.my.info.ui.VipActivity;
import com.jf.my.info.ui.fragment.EarningsFragment;
import com.jf.my.info.ui.fragment.OrderDetailFragment;
import com.jf.my.info.ui.fragment.ShareFriendsFragment;
import com.jf.my.main.ui.adapter.PersonFunctionAdapter;
import com.jf.my.main.ui.adapter.ToolsAdapter;
import com.jf.my.main.ui.adapter.ToolsSplendidAdapter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.DayEarnings;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.MessageEvent;
import com.jf.my.pojo.MonthEarnings;
import com.jf.my.pojo.ProtocolRuleBean;
import com.jf.my.pojo.UpgradeCarousel;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.request.RequestBannerBean;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.DateTimeUtils;
import com.jf.my.utils.GlideImageLoader;
import com.jf.my.utils.GuideViewUtil;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.LoginUtil;
import com.jf.my.utils.MathUtils;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.utils.SharedPreferencesUtils;
import com.jf.my.utils.StringsUtils;
import com.jf.my.utils.TaobaoUtil;
import com.jf.my.utils.UI.BannerInitiateUtils;
import com.jf.my.utils.action.MyAction;
import com.jf.my.view.AspectRatioView;
import com.jf.my.view.UPMarqueeView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/9/10.
 * 我的页面
 */

public class MineFragment extends BaseMainFragmeng {
    @BindView(R.id.userIcon)
    RoundedImageView mUserIcon;
    @BindView(R.id.swipeList)
    SwipeRefreshLayout mSwipeList;
    @BindView(R.id.userLevel)
    ImageView mUserLevel;
    @BindView(R.id.invitation_code)
    TextView mInvitationCode;
    @BindView(R.id.tv_remainder)
    TextView tv_remainder;
    @BindView(R.id.day_price)
    TextView day_price;
    @BindView(R.id.month_price)
    TextView month_price;
    @BindView(R.id.userName)
    TextView mUserName;
    @BindView(R.id.upgrade_text)
    TextView upgrade_text;
    @BindView(R.id.as_banner)
    AspectRatioView mAsBanner;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.ll_category)
    LinearLayout mLLFindSplendid;
    @BindView(R.id.mRv_gView)
    RecyclerView mRv_gView;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.ll_my_tootls)
    LinearLayout ll_my_tootls;
    @BindView(R.id.ll_mine_earnings)
    LinearLayout ll_mine_earnings;
    @BindView(R.id.moneyCardView)
    CardView moneyCardView;
    @BindView(R.id.scrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.rl_set_weixin)
    RelativeLayout rl_set_weixin;
    @BindView(R.id.rl_balance)
    RelativeLayout rl_balance;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.rl_upgrade)
    RelativeLayout rl_upgrade;
    @BindView(R.id.recyclerView_recommend)
    RecyclerView recyclerView_recommend;
    @BindView(R.id.fl_share_expert)
    FrameLayout fl_share_expert;
    @BindView(R.id.iv_bg_expert)
    ImageView iv_bg_expert;
    @BindView(R.id.tv_role)
    TextView tv_role;
    @BindView(R.id.ll_up_marquee)
    LinearLayout ll_up_marquee;
    @BindView(R.id.upview)
    UPMarqueeView upview;
    @BindView(R.id.iv_mine_up_marquee_arrows)
    ImageView iv_mine_up_marquee_arrows;


    ToolsAdapter mAdapter;
    PersonFunctionAdapter mPersonFunctionAdapter;
    ToolsSplendidAdapter mAdapterSplendid;
    private boolean isUserHint = true;
    private View mView;
    private InfoModel mInfoModel;

    public static DayEarnings mDayEarnings;
    public static MonthEarnings mMonthEarnings;
    private String mTotalMoney;
    private ClearSDdataDialog mLogoutDialog;
    EarningsHintDialog mDialog;
    UpgradeUserDialog mUpgradeUserDialog;
    private int serviceRvPositon = 0; //专属客服的位置
    private boolean isShowGuide = false;
    boolean isleft = false;
    List<View> mHotviews = new ArrayList<>();
    private  boolean isLogin;
    private int mIsUpgrade;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mInfoModel = new InfoModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mine, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow()
                .getDecorView()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            getActivity().getWindow()
                                    .getDecorView()
                                    .getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            getActivity().getWindow()
                                    .getDecorView()
                                    .getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }

                    }
                });
        initView();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null) {
            refreshData();
            isUserHint = false;
        }
        MyLog.i("test","isLogin: " +isLogin);
        if(isVisibleToUser&&isLogin){
            MyLog.i("test1","isLogin: " +isLogin);
            isLogin =false;
            openUpgradeUserDialog();
        }

    }

    private void initView() {
        mSwipeList.setRefreshing(false);
        mSwipeList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mLLFindSplendid.setVisibility(View.GONE);
        mAdapter = new ToolsAdapter(getActivity());
        mRecyclerview.setAdapter(mAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (UserLocalData.isPartner) {
            UserLocalData.isPartner = false;
            updataUser();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initViewData(UserInfo info) {

        if ("null".equals(info.getHeadImg()) || "NULL".equals(info.getHeadImg()) || TextUtils.isEmpty(info.getHeadImg())) {
            mUserIcon.setImageResource(R.drawable.logo);
        } else {
            LoadImgUtils.setImgCircle(getActivity(), mUserIcon, info.getHeadImg(), R.drawable.logo);
        }


        mInvitationCode.setText(info.getInviteCode());
        mUserName.setText(info.getNickName());
//        mUserLevel.setText(info.getGradename());
        if (C.UserType.member.equals(info.getPartner())) {
            mUserLevel.setImageResource(R.drawable.icon_member);
//            rl_upgrade.setVisibility(View.VISIBLE);
            ll_mine_earnings.setBackgroundResource(R.drawable.bg_mine_earnings_big);
            ll_up_marquee.setVisibility(View.GONE);
        } else if (C.UserType.vipMember.equals(info.getPartner())) {
            mUserLevel.setImageResource(R.drawable.icon_vip);
//            rl_upgrade.setVisibility(View.VISIBLE);
            ll_mine_earnings.setBackgroundResource(R.drawable.bg_mine_earnings_big);
            ll_up_marquee.setVisibility(View.GONE);
        } else if (C.UserType.operator.equals(info.getPartner())) {
            rl_upgrade.setVisibility(View.GONE);
            mUserLevel.setImageResource(R.drawable.icon_operator);
            ll_mine_earnings.setBackgroundResource(R.drawable.bg_mine_earnings);
//            ll_up_marquee.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(info.getWxNumber())) {
            rl_set_weixin.setVisibility(View.VISIBLE);
        } else {
            rl_set_weixin.setVisibility(View.GONE);
        }
        if(UserLocalData.getUser().getReleasePermission() == 1){
            fl_share_expert.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(UserLocalData.getUser().getLabelPicture())){
                LoadImgUtils.setImg(getActivity(),iv_bg_expert,UserLocalData.getUser().getLabelPicture());
            }
            if (!TextUtils.isEmpty(UserLocalData.getUser().getLabel())){
                tv_role.setText(UserLocalData.getUser().getLabel());
            }
        } else {
            fl_share_expert.setVisibility(View.GONE);
        }
    }


    private void updataUser() {
        UserInfo usInfo = UserLocalData.getUser(getActivity());
        if (usInfo != null) {
            initViewData(usInfo);
        }

    }

    private void refreshData() {
        LoginUtil.getUserInfo((RxAppCompatActivity) getActivity(), false, new MyAction.OnResultFinally<UserInfo>() {
            /**
             * 结束
             */
            @Override
            public void onFinally() {
                if (mSwipeList != null) {
                    mSwipeList.setRefreshing(false);
                }
            }

            @Override
            public void invoke(UserInfo arg) {
                updataUser();
            }

            @Override
            public void onError() {
            }
        });
        getBannerData(C.UIShowType.Personal);   //个人轮播
//        getBannerData(C.UIShowType.Welfare);   //福利津贴
        getBannerData(C.UIShowType.myTool);   //福利津贴
        getBannerData(C.UIShowType.PERSONAL_FUNCTION);   //功能区
        getFindSplendidList();
        getDayIncome();
        getMonthIncome();
        if(!C.UserType.operator.equals(UserLocalData.getUser().getPartner())){

//            boolean isShow = (boolean) SharedPreferencesUtils.get(App.getAppContext(),C.sp.DIALOG_USER_IS_UPGRADE,false);
            getIsUpgrade();
        }

//        getAccumulatedAmount();
    }


    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        if (event.getAction().equals(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA)) {
            updataUser();
        } else if (event.getAction().equals(EventBusAction.LOGINA_SUCCEED)) { //更新个人信息
            updataUser();
            refreshData();
        }
    }

    @OnClick({R.id.copy_invitation_code, R.id.my_earnings, R.id.rl_set_weixin,
            R.id.my_team, R.id.order_detail, R.id.share_friends, R.id.setting, R.id.tv_y, R.id.iv_wenhao,
            R.id.rl_upgrade,R.id.tv_withdraw,R.id.ll_earnings,R.id.userIcon})
    public void onClick(View v) {
        switch (v.getId()) {  //复制邀请码
            case R.id.copy_invitation_code:

                AppUtil.coayText(getActivity(), UserLocalData.getUser(getActivity()).getInviteCode());
                Toast.makeText(getActivity(), "已复制到粘贴版", Toast.LENGTH_SHORT).show();
                break;
            case R.id.my_earnings: //我的收益
            case R.id.ll_earnings:
            case R.id.tv_y:
                Bundle bundle = new Bundle();
                bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
                OpenFragmentUtils.goToSimpleFragment(getActivity(), EarningsFragment.class.getName(), bundle);
//                String appId = "wx92f654d146c13d44"; // 填应用AppId
//                IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
//                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                req.userName = "gh_f8d561cd1942"; // 填小程序原始id
//                req.userName = "gh_5bfc84043725"; // 填小程序原始id（测试）
//                req.path = "pages/index/index/index?token="+UserLocalData.getToken()+"&inviteCode="+UserLocalData.getUser().getInviteCode()+"&appVersion="+C.Setting.app_version;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
//                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
//                api.sendReq(req);
                break;
            case R.id.my_team:  //我的团队
                FansActivity.start(getActivity());
//                openUpgradeUserDialog();
                break;
            case R.id.order_detail:   //订单明细
                OpenFragmentUtils.goToSimpleFragment(getActivity(), OrderDetailFragment.class.getName(), new Bundle());
//                OpenFragmentUtils.goToSimpleFragment(getActivity(), OrderFragment.class.getName(), new Bundle());
//                TaobaoUtil.authTaobao(getActivity(), "25107719");
                break;
            case R.id.share_friends:   //分享好友
                OpenFragmentUtils.goToSimpleFragment(getActivity(), ShareFriendsFragment.class.getName(), new Bundle());
//                PartnerShareActivity.start(getActivity());
                break;
            case R.id.setting: //设置
                Intent in = new Intent(getActivity(), SettingActivity.class);
                startActivity(in);
                break;
            case R.id.iv_wenhao:
                openDialog("累计收益说明", "累计收益=已提现金额+余额");
                break;
            case R.id.rl_set_weixin:
                SettingWechatActivity.start(getActivity(), 0);
                break;
            case R.id.rl_upgrade:   //会员升级
                VipActivity.start(getActivity());
                break;
            case R.id.tv_withdraw: //提现
                if (TaobaoUtil.isAuth()) {   //淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) getActivity());
                } else {
                    AppUtil.gotoCashMoney(getActivity(), mTotalMoney);
                }
                break;
            case R.id.userIcon: //个人设置
                // 个人
                startActivity(new Intent(getActivity(), SettingMineInfoActivity.class));
                break;
            default:
                break;

        }
    }

    /**
     * 获取轮播图数据
     */
    private void getBannerData(final int back) {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setType(back);
        RxHttp.getInstance().getSysteService()
//                .getBanner(back, 2)
                .getBanner(requestBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ImageInfo>>>bindToLifecycle())
                .subscribe(new DataObserver<List<ImageInfo>>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        switch (back) {
                            case C.UIShowType.Personal:
                                mAsBanner.setVisibility(View.GONE);
                                break;
                            case C.UIShowType.Welfare:
//                                mRedPackages.setVisibility(View.GONE);
                                break;
                            case C.UIShowType.myTool:
                                ll_my_tootls.setVisibility(View.GONE);
                                break;
                            case C.UIShowType.PERSONAL_FUNCTION:
                                recyclerView_recommend.setVisibility(View.GONE);
                                break;

                        }
                    }

                    @Override
                    protected void onDataNull() {
                        mAsBanner.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(List<ImageInfo> data) {

                        if (C.UIShowType.Welfare == back) {
//                            if (data.size() == 0) {
//                                mRedPackages.setVisibility(View.GONE);
//                                return;
//                            }
//                            setBanner(data, mRedPackagesBanner, mRedPackages);
//                            mRedPackages.setVisibility(View.VISIBLE);
                        } else if(C.UIShowType.PERSONAL_FUNCTION==back){

                            setPersonalFunction(data);
                        } else if (C.UIShowType.Personal == back) {
                            if (data == null || data.size() == 0) {
                                mAsBanner.setVisibility(View.GONE);
                                return;
                            }
                            mAsBanner.setVisibility(View.VISIBLE);
                            setBanner(data, mBanner, mAsBanner);
                        } else if (C.UIShowType.myTool == back) {
                            List<ImageInfo> list = new ArrayList<ImageInfo>();
                            for (int i = 0; i < data.size(); i++) {
                                ImageInfo imageInfo = data.get(i);
                                String partner = UserLocalData.getUser().getPartner();
                                if (imageInfo == null) continue;
                                String permission = imageInfo.getPermission();
                                if (TextUtils.isEmpty(permission) || TextUtils.isEmpty(partner))
                                    continue;
                                if ("发布管理".equals(data.get(i).getTitle())) {
                                    if (UserLocalData.getUser().getReleasePermission() == 1) {
                                        list.add(data.get(i));
                                    }
                                    continue;
                                }
                                if (permission.contains("3") || permission.contains(partner)) {//Permission为"0,1,2,3"格式的字符串，
                                    list.add(imageInfo);
                                }
                            }

                            if (list.size() == 0) {
                                ll_my_tootls.setVisibility(View.GONE);
                                return;
                            }
                            ll_my_tootls.setVisibility(View.VISIBLE);
                            mAdapter.replace(list);
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }

    /**
     * 获取发现精彩列表
     */

    private void getFindSplendidList() {
        LoginUtil.getSystemStaticPage((RxAppCompatActivity) getActivity(), C.ProtocolType.wonderful)
                .subscribe(new DataObserver<List<ProtocolRuleBean>>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        mLLFindSplendid.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onDataListEmpty() {
                        mLLFindSplendid.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(List<ProtocolRuleBean> data) {
                        if (data.size() == 0) {
                            mLLFindSplendid.setVisibility(View.GONE);
                        } else {
                            mLLFindSplendid.setVisibility(View.VISIBLE);
                            setFindSplendid(data);
                        }

                    }
                });
    }

    /**
     * 设置轮播图
     *
     * @param data
     */
    private void setBanner(final List<ImageInfo> data, Banner banner, AspectRatioView aspectRatioView) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (aspectRatioView != null) {
            ImageInfo imageInfo = data.get(0);
            float width = imageInfo.getWidth();
            float height = imageInfo.getHeight();
            if (width != 0 && width / height != 0) {
                aspectRatioView.setAspectRatio(width / height);
            }

        }

        final List<String> imgUrls = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ImageInfo imageInfo = data.get(i);
            imgUrls.add(imageInfo.getThumb());
//            imgUrls.add("https://img.gzzhitu.com/picture/20181016/153968239230500.gif");
        }
        //简单使用
        banner.setImages(imgUrls)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        ImageInfo imageInfo = data.get(position);
//                        SensorsDataUtil.getInstance().advClickTrack(imageInfo.getTitle(),imageInfo.getId()+"",imageInfo.getOpen()+"","我的",position,imageInfo.getClassId()+"",imageInfo.getUrl());
                        BannerInitiateUtils.gotoAction(getActivity(), imageInfo);
                    }
                })
                .isAutoPlay(true)
                .setDelayTime(4000)
                .start();
    }

    public void getDayIncome() {
        mInfoModel.getDayIncome(this)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(C.UserType.operator.equals(UserLocalData.getUser().getPartner())){
                            getUpgradeCarousel();
                        }

                    }
                })
                .subscribe(new DataObserver<DayEarnings>(false) {

                    @Override
                    protected void onSuccess(DayEarnings data) {
                        MyLog.i("test", "data: " + data.getTodayMoney());
                        mDayEarnings = data;
                        mTotalMoney = MathUtils.getMoney(data.getTotalMoney());
                        tv_remainder.setText(MathUtils.getMoney(data.getAccumulatedAmount()));
                        day_price.setText(MathUtils.getMoney(data.getTodayEstimateMoney()));
                        checkWithdrawTime();
//                        if(Double.parseDouble(mTotalMoney)<1){
//                            rl_balance.setVisibility(View.GONE);
//                        } else {
//                            tv_balance.setText(getString(R.string.mine_text_withdraw,mTotalMoney));
//                            rl_balance.setVisibility(View.VISIBLE);
//                        }
                    }
                });
    }

    public void getMonthIncome() {
        mInfoModel.getMonthIncome(this)
                .subscribe(new DataObserver<MonthEarnings>(false) {
                    @Override
                    protected void onSuccess(MonthEarnings data) {
                        mMonthEarnings = data;
                        month_price.setText(MathUtils.getMoney(data.getThisMonthEstimateMoney()));
                    }
                });
    }

    /**
     * 升级运营商弹窗提示
     */
    public void getIsUpgrade() {
        mInfoModel.getIsUpgrade(this)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        String s = "";
                        if(mIsUpgrade == 1){
                            if(C.UserType.vipMember.equals(UserLocalData.getUser().getPartner())){
                                s = getString(R.string.mine_upgrade_text_one,"运营专员");
                            } else if(C.UserType.member.equals(UserLocalData.getUser().getPartner())){
                                s = getString(R.string.mine_upgrade_text_one,"VIP会员");
                            }
                        } else if(mIsUpgrade == 2){
                            if(C.UserType.vipMember.equals(UserLocalData.getUser().getPartner())){
                                s = getString(R.string.mine_upgrade_text_two,"运营专员");
                            } else if(C.UserType.member.equals(UserLocalData.getUser().getPartner())){
                                s = getString(R.string.mine_upgrade_text_two,"VIP会员");
                            }
                        } else {
                            if(C.UserType.vipMember.equals(UserLocalData.getUser().getPartner())){
                                s = getString(R.string.mine_upgrade_text,"运营专员","90%");
                            } else if(C.UserType.member.equals(UserLocalData.getUser().getPartner())){
                                s = getString(R.string.mine_upgrade_text,"VIP会员","60%");
                            }
                        }
                        upgrade_text.setText(Html.fromHtml(s));
                        rl_upgrade.setVisibility(View.VISIBLE);
                    }
                })
                .subscribe(new DataObserver<UpgradeCarousel>(false) {
                    @Override
                    protected void onSuccess(UpgradeCarousel data) {
                        mIsUpgrade = data.getIsUpgrade();
                        if(data.getIsUpgrade()==1){

                            if(DateTimeUtils.isToday()) {
                                MyLog.i("test","isLogin: " +isLogin);
                                isLogin = true;
                                openUpgradeUserDialog();
                            }

                        }

                    }
                });
    }

    /**
     * 升级运营商弹窗提示
     */
    public void getUpgradeCarousel() {
        mInfoModel.getUpgradeCarousel(this)
                .subscribe(new DataObserver<List<UpgradeCarousel>>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        if(StringsUtils.isDataEmpty(errCode)){
                            ll_up_marquee.setVisibility(View.VISIBLE);
                            setHotView(null);
                        }  else {
                            ll_up_marquee.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    protected void onSuccess(List<UpgradeCarousel> data) {
                        if(data.size()>0){
                            ll_up_marquee.setVisibility(View.VISIBLE);
                            setHotView(data);
                        }
                    }
                });
    }

    /**
     * 设置下线升级拉新轮播
     * @param datas
     */
    private void setHotView(final List<UpgradeCarousel> datas) {
        //蜜源热门
        if (datas == null || datas.size() == 0) {
            iv_mine_up_marquee_arrows.setVisibility(View.GONE);
            mHotviews.clear();
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_hot_text, null);
            TextView tv = (TextView) moreView.findViewById(R.id.title);
            if(mDayEarnings!=null){
                try {
                    double dayMoney = Double.parseDouble(MathUtils.getMoney(mDayEarnings.getTodayEstimateMoney()));
                    if(dayMoney>0){
                        tv.setText(R.string.mine_day_money_up_text);
                    } else {
                        tv.setText(R.string.mine_day_money_text);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                tv.setText(R.string.mine_day_money_text);
            }
            mHotviews.add(tv);
        } else {
            iv_mine_up_marquee_arrows.setVisibility(View.VISIBLE);
            ll_up_marquee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FansActivity.start(getActivity());
                }
            });
            mHotviews.clear();
            for (int i = 0; i < datas.size(); i++) {
                //设置滚动的单个布局
                LinearLayout moreView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_hot_text, null);
                TextView tv = (TextView) moreView.findViewById(R.id.title);
                ImageView iv_head = (ImageView) moreView.findViewById(R.id.iv_head);
                iv_head.setVisibility(View.VISIBLE);
                String s ="";
                if(datas.get(i).getIsUpgrade()==1){
                    s= getString(R.string.mine_upgrade_carousel_text,datas.get(i).getNickname(),datas.get(i).getNewUsers()+"");
                } else {
                    s = getString(R.string.mine_upgrade_carousel_text_one,datas.get(i).getNickname(),+datas.get(i).getNewUsers()+"");
                }
                if(TextUtils.isEmpty(datas.get(i).getHeadImg())){
                    iv_head.setImageResource(R.drawable.head_icon);
                } else {
                    LoadImgUtils.setImgHead(getActivity(),iv_head,datas.get(i).getHeadImg());
                }
                //进行对控件赋值
                tv.setText(Html.fromHtml(s));
                //添加到循环滚动数组里面去
                mHotviews.add(moreView);
            }
        }


        upview.setViews(mHotviews);
        upview.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                FansActivity.start(getActivity());
            }

            /**
             * 滚动监听
             *
             * @param tag
             * @param view
             */
            @Override
            public void onAnimationListener(Object tag, View view) {

            }
        });
    }
    /**
     * 设置发现精彩
     */
    private void setFindSplendid(final List<ProtocolRuleBean> datas) {
        //分类数据
        if (mAdapterSplendid == null) {
            mAdapterSplendid = new ToolsSplendidAdapter(getActivity());
            mRv_gView.setAdapter(mAdapterSplendid);
        }
        mAdapterSplendid.replace(datas);
        mAdapterSplendid.notifyDataSetChanged();
    }
    /**
     * 设置推荐
     */
    private void setPersonalFunction(List<ImageInfo> datas) {
        recyclerView_recommend.setVisibility(View.VISIBLE);
        if(datas.size()>2){
            datas =  datas.subList(0,2);
        }
        //分类数据
        if (mPersonFunctionAdapter == null) {
            mPersonFunctionAdapter = new PersonFunctionAdapter(getActivity());
            recyclerView_recommend.setAdapter(mPersonFunctionAdapter);
        }
        mPersonFunctionAdapter.replace(datas);
        mPersonFunctionAdapter.notifyDataSetChanged();
    }
    private void openDialog(String title, String content) {  //收益说明dialog
        if (mDialog == null) {
            mDialog = new EarningsHintDialog(getActivity(), R.style.dialog, title, content);
            mDialog.setGravity(true);
            mDialog.setmOkListener(new EarningsHintDialog.OnOkListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }
    private void openUpgradeUserDialog() {  //收益说明dialog
        if (mUpgradeUserDialog == null) {
            mUpgradeUserDialog = new UpgradeUserDialog(getActivity());

            mUpgradeUserDialog.setmOkListener(new UpgradeUserDialog.OnOkListener() {
                @Override
                public void onClick(View view) {
                    VipActivity.start(getActivity());
                }
            });
            mUpgradeUserDialog.setCancelListener(new UpgradeUserDialog.OnCancelListner() {
                @Override
                public void onClick(View view) {

                }
            });
        }


        if (!mUpgradeUserDialog.isShowing()&&!isLogin) {
//            SharedPreferencesUtils.put(App.getAppContext(),C.sp.DIALOG_USER_IS_UPGRADE,true);
            mUpgradeUserDialog.show();


        }
    }

    //    public void getAccumulatedAmount() {
//        mInfoModel.getAccumulatedAmount(this)
//                .subscribe(new DataObserver<String>(false) {
//                    @Override
//                    protected void onSuccess(String data) {
//                        tv_remainder.setText(data+"");
//                    }
//                });
//    }
    public void checkWithdrawTime (){
        if (AppUtil.isFastClick(500)) return;
        mInfoModel.checkWithdrawTime(this)
                .subscribe(new DataObserver<String>(false) {
                    @Override
                    protected void onSuccess(String data) {
                        AppUtil.gotoCashMoney(getActivity(), mTotalMoney);
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        MyLog.i("test","errCode: " +errCode);
                        double money = 0;
                        try {
                            money  = Double.parseDouble(mTotalMoney);
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        if(!C.requestCode.B10301.equals(errCode)&&money>=1){//因为成功的话data会为空，所以判断下
                            tv_balance.setText(getString(R.string.mine_text_withdraw,mTotalMoney));
                            rl_balance.setVisibility(View.VISIBLE);
//                        }  else {
//                            withdrawErrorDialog(errorMsg);
                        } else {
                            rl_balance.setVisibility(View.GONE);
                        }

                    }
                });
    }
    WithdrawErrorDialog withdrawErrorDialog;
    private void withdrawErrorDialog(String title) {  //淘宝取消授权弹框
        if(withdrawErrorDialog==null){
            withdrawErrorDialog =new WithdrawErrorDialog(getActivity(), R.style.dialog, "温馨提示", title);
            withdrawErrorDialog.setmOkListener(new WithdrawErrorDialog.OnOkListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        if(!withdrawErrorDialog.isShowing()){
            withdrawErrorDialog.show();
        }

    }

    public void showGuideInvite() {
        GuideViewUtil.showGuideView(getActivity(), mInvitationCode, GuideViewUtil.GUIDE_INVITE, 0, null, new GuideViewUtil.GuideCallback() {
            @Override
            public void onDissmiss() {
                App.mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showGuideMoney();
                    }
                }, 200);

            }
        });
    }

    public void showGuideMoney() {
        GuideViewUtil.showGuideView(getActivity(), moneyCardView, GuideViewUtil.GUIDE_MONEY, 0, null, new GuideViewUtil.GuideCallback() {
            @Override
            public void onDissmiss() {
                showGuideService();
            }
        });
    }

    public void showGuideService() {
        if (null != mAdapter) {
            if (null != mAdapter.getItems() && mAdapter.getItems().size() > 0) {
                for (int i = 0; i < mAdapter.getItems().size(); i++) {
                    if ("专属客服".equals(mAdapter.getItems().get(i).getTitle())) {
                        serviceRvPositon = i;
                    }
                }


                //待优化
                int position = serviceRvPositon + 1;
                if (position <= 4) {
                    if (position <= 2) isleft = true;
                } else {
                    if (position % 2 != 0) {
                        if ((position - 4) == 1) isleft = true;
                        if ((position - 4) == 5) isleft = true;
                        if ((position - 4) == 9) isleft = true;
                    } else {
                        if ((position - 4) == 2) isleft = true;
                        if ((position - 4) == 6) isleft = true;
                        if ((position - 4) == 10) isleft = true;
                    }
                }

                App.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        App.mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                View view = null;
                                if (null != mRecyclerview) {
                                    view = mRecyclerview.getLayoutManager().findViewByPosition(serviceRvPositon);
                                    Rect viewRect = new Rect();
                                    view.getGlobalVisibleRect(viewRect);
                                    int width = viewRect.right - viewRect.left;

                                    GuideViewUtil.showGuideView(getActivity(), view, GuideViewUtil.GUIDE_SERVICE, width / 2, isleft, serviceRvPositon + 1, null, new GuideViewUtil.GuideCallback() {
                                        @Override
                                        public void onDissmiss() {
                                            SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuide, false);
                                        }
                                    });

                                    App.mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            GuideViewUtil.dismiss();
                                        }
                                    }, 3000);
                                }

                            }
                        }, 200);

                    }
                });
            }
        }

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (LoginUtil.checkIsLogin(getActivity(), false) && UserLocalData.isShowGuideMine()) {
            if (!isShowGuide) {
                isShowGuide = true;
                App.mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showGuideInvite();
                    }
                }, 300);

            }
        }
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        isShowGuide = false;
    }
}
