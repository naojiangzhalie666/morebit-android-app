package com.zjzy.morebit.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.AppletsActivity;
import com.zjzy.morebit.Activity.FansDragonActivity;
import com.zjzy.morebit.Activity.FansListFragment;
import com.zjzy.morebit.Activity.InvateActivity;
import com.zjzy.morebit.Activity.MyOrderActivity;
import com.zjzy.morebit.Activity.SettingActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.EarningsHintDialog;
import com.zjzy.morebit.Module.common.Dialog.InterIconDialog;
import com.zjzy.morebit.Module.common.Dialog.UpgradeUserDialog;
import com.zjzy.morebit.Module.common.Dialog.WithdrawErrorDialog;
import com.zjzy.morebit.Module.common.Dialog.WxBindDialog;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.info.ui.AppFeedActivity;
import com.zjzy.morebit.info.ui.FansActivity;
import com.zjzy.morebit.info.ui.GoodsBrowsingHistoryActivity;
import com.zjzy.morebit.info.ui.SettingMineInfoActivity;
import com.zjzy.morebit.info.ui.SettingWechatActivity;
import com.zjzy.morebit.info.ui.VipActivity;
import com.zjzy.morebit.info.ui.fragment.EarningsFragment;
import com.zjzy.morebit.main.ui.CollectFragment2;
import com.zjzy.morebit.main.ui.adapter.PersonFunctionAdapter;
import com.zjzy.morebit.main.ui.adapter.ToolsSplendidAdapter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.order.ui.OrderTeamActivity;
import com.zjzy.morebit.pojo.UserIncomeDetail;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.MonthEarnings;
import com.zjzy.morebit.pojo.UpgradeCarousel;
import com.zjzy.morebit.pojo.UserIncomeDetail2;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.RefreshUserInfoEvent;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.AspectRatioView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
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
    @BindView(R.id.invitation_code)
    TextView mInvitationCode;
    @BindView(R.id.tv_remainder)
    TextView tv_remainder;
    @BindView(R.id.jifen)
    TextView jifen;
    @BindView(R.id.today_jifen)
    TextView today_jifen;
    @BindView(R.id.today_yugu)
    TextView today_yugu;
    @BindView(R.id.userName)
    TextView mUserName;
    @BindView(R.id.tv_collectnum)
    TextView tv_collectnum;
    @BindView(R.id.tv_tixian)
    TextView tv_tixian;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv_wx)
    TextView tv_wx;


    @BindView(R.id.ll_user_grade)
    LinearLayout llUserGrade;

    @BindView(R.id.tv_user_type)
    TextView tvUserType;

    @BindView(R.id.as_banner)
    AspectRatioView mAsBanner;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.icon_tou)
    ImageView icon_tou;
    //    @BindView(R.id.recyclerview)
//    RecyclerView mRecyclerview;
//    @BindView(R.id.ll_my_tootls)
//    LinearLayout ll_my_tootls;
//    @BindView(R.id.ll_mine_earnings)
//    RelativeLayout ll_mine_earnings;
    //    @BindView(R.id.moneyCardView)
//    CardView moneyCardView;
    @BindView(R.id.scrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.my_earnings)
    LinearLayout my_earnings;

//    @BindView(R.id.tv_role)
//    TextView tv_role;

    @BindView(R.id.tv_withdraw)
    LinearLayout tvWithDraw;
    @BindView(R.id.privacy_agreement)
    LinearLayout privacy_agreement;

    @BindView(R.id.user_agreemen)
    LinearLayout user_agreemen;


    @BindView(R.id.tv_today_money)
    TextView tv_today_money;
    @BindView(R.id.tv_yesterday_estimate_money)
    TextView tv_yesterday_estimate_money;
    @BindView(R.id.tv_this_month_estimate_money)
    TextView tv_this_month_estimate_money;
    @BindView(R.id.tv_prev_month_estimate_oney)
    TextView tv_prev_month_estimate_oney;
    @BindView(R.id.offen_question)
    LinearLayout offen_question;
    @BindView(R.id.order_search)
    LinearLayout order_search;
    @BindView(R.id.my_footmarker)
    LinearLayout my_footmarker;
    @BindView(R.id.my_favorite)
    LinearLayout my_favorite;
    @BindView(R.id.ll_new)
    LinearLayout ll_new;
    @BindView(R.id.ll_coustom)
    LinearLayout ll_coustom;
    @BindView(R.id.ll_jian)
    LinearLayout ll_jian;
    @BindView(R.id.ll_dragon)
    LinearLayout ll_dragon;
    @BindView(R.id.integer_icon)
    LinearLayout integer_icon;


    // ToolsAdapter mAdapter;
    private LinearLayout my_little;
    PersonFunctionAdapter mPersonFunctionAdapter;
    ToolsSplendidAdapter mAdapterSplendid;
    private boolean isUserHint = true;
    private View mView;
    private InfoModel mInfoModel;

    public static UserIncomeDetail mDayEarnings;
    public static MonthEarnings mMonthEarnings;
    private String mTotalMoney;
    private ClearSDdataDialog mLogoutDialog;
    EarningsHintDialog mDialog;
    UpgradeUserDialog mUpgradeUserDialog;
    private int serviceRvPositon = 0; //专属客服的位置
    private boolean isShowGuide = false;
    boolean isleft = false;
    List<View> mHotviews = new ArrayList<>();
    private boolean isLogin;
    private int mIsUpgrade;
    private LinearLayout tab_title;
    private float duration = 855.0f;//在0-855.0之间去改变头部的透明度
    private int mTitleHeight;
    private boolean isTitleBarSetBg = true;
    private TextView tv_mine;
    private String newUrl, customerUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mInfoModel = new InfoModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mine, container, false);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.transparent)
                .init();
        my_little = mView.findViewById(R.id.my_little);
        tab_title = mView.findViewById(R.id.tab_title);
        tv_mine = mView.findViewById(R.id.tv_mine);
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
        MyLog.i("test", "isLogin: " + isLogin);
        if (isVisibleToUser && isLogin) {
            MyLog.i("test1", "isLogin: " + isLogin);
            isLogin = false;
            openUpgradeUserDialog();
        }

    }

    private void initView() {
        tv_mine.getPaint().setFakeBoldText(true);
        mSwipeList.setRefreshing(false);
        mSwipeList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
//        mLLFindSplendid.setVisibility(View.GONE);
//        mAdapter = new ToolsAdapter(getActivity());
//        mRecyclerview.setAdapter(mAdapter);
        tab_title.setAlpha(0);
        mTitleHeight = getResources().getDimensionPixelSize(R.dimen.title_height);
        duration = getActivity().getWindowManager().getDefaultDisplay().getWidth() - mTitleHeight + 0.0F;
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("gggg", scrollY + "");
                float alpha = (float) (scrollY / duration);
                if (duration > scrollY) {
                    if (scrollY < 5) {

                        tab_title.setVisibility(View.GONE);
                        tab_title.setAlpha(0);
                    } else {
                        if (tab_title.getVisibility() == View.GONE) {
                            tab_title.setVisibility(View.VISIBLE);
                        }
                        tab_title.setAlpha(alpha);
                    }
                } else {

                    if (tab_title.getVisibility() == View.GONE) {
                        tab_title.setVisibility(View.VISIBLE);
                    }
                    tab_title.setAlpha(1);

                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (UserLocalData.isPartner) {
            UserLocalData.isPartner = false;
            updataUser();
        }
        refreshData();
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
            LoadImgUtils.setImgCircle(getActivity(), mUserIcon, info.getHeadImg(), R.drawable.head_icon);
        }


        mInvitationCode.setText(getResources().getString(R.string.number_invite_code, info.getInviteCode()));
        mUserName.setText(info.getNickName());
        if (!TextUtils.isEmpty(info.getWxNumber())) {
            tv_wx.setVisibility(View.GONE);
        } else {
            tv_wx.setVisibility(View.VISIBLE);
        }

        if (C.UserType.member.equals(info.getPartner())) {
            tvUserType.setText("普通会员");
            icon_tou.setImageResource(R.mipmap.mine_memeber_icon);
            tv1.setVisibility(View.VISIBLE);
            tv1.setText("升级");
            tv2.setText("VIP");
            tv3.setText("享更多权益");
            tv4.setText("立即升级");

        } else if (C.UserType.vipMember.equals(info.getPartner())) {
            tvUserType.setText("VIP");
            icon_tou.setImageResource(R.mipmap.mine_vip_icon);
            tv1.setVisibility(View.VISIBLE);
            tv1.setText("升级");
            tv2.setText("团队长");
            tv3.setText("享更多权益");
            tv4.setText("立即升级");

        } else if (C.UserType.operator.equals(info.getPartner())) {
            tvUserType.setText("团队长");
            icon_tou.setImageResource(R.mipmap.mine_operator_icon);
            tv1.setVisibility(View.GONE);
            tv2.setText("团队长");
            tv3.setText("权益享不停");
            tv4.setText("查看权益");
        }
        if (!TextUtils.isEmpty(info.getBalance())) {
            tv_tixian.setText("¥ " + info.getBalance());
        }

        if (!TextUtils.isEmpty(info.getCollectCount())) {
            tv_collectnum.setText(info.getCollectCount() + "个喜欢");
        } else {
            tv_collectnum.setText("0个喜欢");
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
//        getFindSplendidList();

        getMonthIncome();

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

    @OnClick({R.id.copy_invitation_code, R.id.my_earnings,
            R.id.my_team, R.id.order_detail, R.id.share_friends, R.id.tv_y, R.id.iv_wenhao,
            R.id.tv_withdraw, R.id.ll_earnings, R.id.userIcon, R.id.offen_question, R.id.order_search,
            R.id.my_footmarker, R.id.my_favorite, R.id.my_little, R.id.user_agreemen, R.id.privacy_agreement,
            R.id.ll_new, R.id.ll_coustom, R.id.ll_jian, R.id.tv_wx, R.id.img_right, R.id.ll_dragon, R.id.integer_icon,
            R.id.tv4})
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

                break;
            case R.id.my_team:  //我的粉丝
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean(C.Extras.openFragment_isSysBar, true);
                OpenFragmentUtils.goToSimpleFragment(getActivity(), FansListFragment.class.getName(), bundle2);
                //FansActivity.start(getActivity());
                FansListFragment.newInstance();
                break;
            case R.id.order_detail:   //订单明细
                Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                intent.putExtra(C.Extras.SEARCHINFO, mLocalData.get("2"));
                startActivity(intent);
                //OrderTeamActivity.start(getActivity());
                break;
            case R.id.share_friends:   //分享好友
                startActivity(new Intent(getActivity(), InvateActivity.class));
                break;
            case R.id.offen_question:
                ImageInfo question = new ImageInfo();
                question.setOpen(3);//打开外部链接。
                question.setUrl("https://helper.morebit.com.cn/#/question");
                BannerInitiateUtils.gotoAction(getActivity(), question);

                break;
            case R.id.order_search:
                ImageInfo orderSearch = mLocalData.get("2");
                if (orderSearch == null) {
                    //  ViewShowUtils.showShortToast(getActivity(),"没有订单查询权限");
                    ImageInfo orderSearch2 = new ImageInfo();
                    orderSearch2.setOpen(3);
                    orderSearch2.setUrl("https://helper.morebit.com.cn/#/search");
                    orderSearch2.setSubTitle("找回订单");
                    orderSearch2.setTitle("订单查询");
                    orderSearch2.setSort(2);
                    BannerInitiateUtils.gotoAction(getActivity(), orderSearch2);
                } else {
                    BannerInitiateUtils.gotoAction(getActivity(), orderSearch);
                }

                break;
            case R.id.my_footmarker://足迹
                GoodsBrowsingHistoryActivity.start(getActivity());
                break;
            case R.id.my_favorite://收藏
                OpenFragmentUtils.goToSimpleFragment(getActivity(), CollectFragment2.class.getName(), null);
                break;
            case R.id.iv_wenhao:
                openDialog("累计收益说明", "累计收益=已提现金额+余额");
                break;
            case R.id.tv_withdraw: //提现
                mInfoModel.checkWithdrawTime(this)
                        .subscribe(new DataObserver<String>(false) {
                            @Override
                            protected void onSuccess(String data) {
                                UserInfo info = UserLocalData.getUser(getActivity());
                                if (TextUtils.isEmpty(mTotalMoney)) {
                                    mTotalMoney = "0";
                                }
                                if (Double.parseDouble(mTotalMoney) < 1) {
                                    ViewShowUtils.showShortToast(getActivity(), "不足1元，无法提现");
                                } else {
                                    if (TaobaoUtil.isAuth()) {   //淘宝授权
                                        TaobaoUtil.getAllianceAppKey((BaseActivity) getActivity());
                                    } else {
                                        if (info.getAliPayNumber() != null && !"".equals(info.getAliPayNumber())) {
                                            AppUtil.gotoCashMoney(getActivity(), mTotalMoney);
                                        } else {
                                            PageToUtil.goToUserInfoSimpleFragment(getActivity(), "绑定支付宝", "BindZhiFuBaoFragment");
                                            ToastUtils.showLong("请先绑定支付宝!");
                                        }

                                    }
                                }

                            }

                            @Override
                            protected void onError(String errorMsg, String errCode) {
                                MyLog.i("test", "errCode: " + errCode);
                                double money = 0;
                                try {
                                    money = Double.parseDouble(mTotalMoney);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (C.requestCode.B10301.equals(errCode)) {//因为成功的话data会为空，所以判断下
                                    ToastUtils.showLong("提现时间为每月25,26,27,28,29,30,31号");
                                }

                            }
                        });

                break;
            case R.id.my_little://小程序入口
                startActivity(new Intent(getActivity(), AppletsActivity.class));
                break;
            case R.id.user_agreemen://用户协议
                LoginUtil.getUserProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.privacy_agreement://隐私
                LoginUtil.getPrivateProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.ll_new://新手教程
                if (!TextUtils.isEmpty(newUrl)) {
                    ShowWebActivity.start(getActivity(), newUrl, "新手教程");
                }

                break;
            case R.id.ll_coustom://客服中心
                if (!TextUtils.isEmpty(customerUrl)) {
                    ShowWebActivity.start(getActivity(), customerUrl, "专属客服");
                }
                break;
            case R.id.ll_jian://反馈建议
                Intent feedBackIt = new Intent(getActivity(), AppFeedActivity.class);
                Bundle feedBackBundle = new Bundle();
                feedBackBundle.putString("title", "意见反馈");
                feedBackBundle.putString("fragmentName", "AppFeedBackFragment");
                feedBackIt.putExtras(feedBackBundle);
                getActivity().startActivity(feedBackIt);
                break;

            case R.id.tv_wx:
            case R.id.img_right:
                Intent in = new Intent(getActivity(), SettingActivity.class);
                startActivity(in);
                break;
            case R.id.ll_dragon:
                startActivity(new Intent(getActivity(), FansDragonActivity.class));
                break;
            case R.id.integer_icon://积分弹框
                InterIconDialog iconDialog = new InterIconDialog(getActivity());
                iconDialog.show();
                break;
            case R.id.tv4://升级页面
                GoodsUtil.getVipH5(getActivity());
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
                        } else if (C.UIShowType.PERSONAL_FUNCTION == back) {

                            setPersonalFunction(data);
                        } else if (C.UIShowType.Personal == back) {
                            if (data == null || data.size() == 0) {
                                mAsBanner.setVisibility(View.GONE);
                                return;
                            }
                            mAsBanner.setVisibility(View.VISIBLE);
                            setBanner(data, mBanner, mAsBanner);
                        } else if (C.UIShowType.myTool == back) {
                            setTool(data);
                        }

                    }
                });
    }

    private void setTool(List<ImageInfo> data) {
        for (int i = 0; i < data.size(); i++) {
            if ("专属客服".equals(data.get(i).getTitle())) {
                customerUrl = data.get(i).getUrl();
            } else if ("新手教程".equals(data.get(i).getTitle())) {
                newUrl = data.get(i).getUrl();
            }
        }
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


    public void getMonthIncome() {
        getUserIncomeDetail(this)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new DataObserver<UserIncomeDetail2>() {

            @Override
            protected void onSuccess(UserIncomeDetail2 data) {
                getDaySuccess(data);
            }
        });
    }

    private void getDaySuccess(UserIncomeDetail2 data) {
        if (data != null) {
            tv_remainder.setText(data.getTotalIncome() + "");
            jifen.setText(data.getTotalIntegral() + "");
            today_yugu.setText(data.getTodayEstimateMoney() + "");
            today_jifen.setText(data.getTotalEstimateIntegral() + "");
        }


    }


    /**
     * 设置推荐
     */
    //本地数据
    HashMap<String, ImageInfo> mLocalData = new HashMap<String, ImageInfo>();

    private void setPersonalFunction(List<ImageInfo> datas) {
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            if ("订单查询".equals(datas.get(i).getTitle())) {
                mLocalData.put("2", datas.get(i));
                break;
            }
        }

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


        if (!mUpgradeUserDialog.isShowing() && !isLogin) {
//            SharedPreferencesUtils.put(App.getAppContext(),C.sp.DIALOG_USER_IS_UPGRADE,true);
            mUpgradeUserDialog.show();


        }
    }


    WithdrawErrorDialog withdrawErrorDialog;

    private void withdrawErrorDialog(String title) {  //淘宝取消授权弹框
        if (withdrawErrorDialog == null) {
            withdrawErrorDialog = new WithdrawErrorDialog(getActivity(), R.style.dialog, "温馨提示", title);
            withdrawErrorDialog.setmOkListener(new WithdrawErrorDialog.OnOkListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        if (!withdrawErrorDialog.isShowing()) {
            withdrawErrorDialog.show();
        }

    }


    @Override
    protected void onVisible() {
        super.onVisible();

    }

    @Override
    protected void onInvisible() {
        super.onInvisible();

    }


    @Subscribe
    public void onEventMainThread(RefreshUserInfoEvent event) {

        updataUser();
    }


    /**
     * 获取收益
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<UserIncomeDetail2>> getUserIncomeDetail(RxFragment rxFragment) {
        return RxHttp.getInstance().getUsersService().getUserIncome()
                .compose(RxUtils.<BaseResponse<UserIncomeDetail2>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<UserIncomeDetail2>>bindToLifecycle());
    }


}
