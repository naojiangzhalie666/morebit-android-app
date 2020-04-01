package com.zjzy.morebit;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.LocalData.CommonLocalData;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.NumberLeaderUpgradeDialog;
import com.zjzy.morebit.Module.common.Dialog.NumberVipUpgradeDialog;
import com.zjzy.morebit.Module.common.Dialog.OpenPushDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.push.PushAction;
import com.zjzy.morebit.adapter.MarkermallCircleAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.CircleFragment;
import com.zjzy.morebit.fragment.MineFragment;
import com.zjzy.morebit.fragment.NumberFragment;
import com.zjzy.morebit.fragment.NumberSubFragment;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.home.contract.MainContract;
import com.zjzy.morebit.home.presenter.MainPresenter;
import com.zjzy.morebit.interfaces.GuideNextCallback;
import com.zjzy.morebit.main.ui.dialog.HomeRedPackageDialog;
import com.zjzy.morebit.main.ui.fragment.HomeCollegeFragment;
import com.zjzy.morebit.main.ui.fragment.SuperNavigationFragment;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.AppUpgradeInfo;
import com.zjzy.morebit.pojo.GrayUpgradeInfo;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.LogoutEvent;
import com.zjzy.morebit.pojo.event.OpenCategoryEvent;
import com.zjzy.morebit.pojo.event.OpenNumberEvent;
import com.zjzy.morebit.pojo.event.UpdateGradeEvent;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.request.RequestSplashStatistics;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.CleanSdUtil;
import com.zjzy.morebit.utils.ConfigListUtlis;
import com.zjzy.morebit.utils.GuideViewUtil;
import com.zjzy.morebit.utils.LogUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.PutErrorUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.ACache;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.appDownload.QianWenUpdateUtlis;
import com.zjzy.morebit.utils.appDownload.update_app.UpdateAppBean;
import com.zjzy.morebit.utils.fire.DeviceIDUtils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.view.AuthForPersonDialog;
import com.zjzy.morebit.view.ConsCommissionRuleDialog;
import com.zjzy.morebit.view.GrayUpgradeDialog;
import com.zjzy.morebit.view.NoScrollViewPager;
import com.zjzy.morebit.view.main.SysNotificationView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends MvpActivity<MainPresenter> implements View.OnClickListener, MainContract.View {
    private static final String TAG = "MainActivity";
    public static boolean isNetwork = true;
    private RelativeLayout rl_mine, rl_community, rl_homepage, rl_shop, rl_number;
    //Fragment
    HomeFragment homePageFragment;
    BaseMainFragmeng superNavigationFragment;
    BaseMainFragmeng circleFragment;
//    BaseMainFragmeng collegeFragment;
    BaseMainFragmeng numberFragment;
    BaseMainFragmeng partnerFragment;
    // 底部界面集合
    private ArrayList<RelativeLayout> menuList = new ArrayList<>();
    private NoScrollViewPager mViewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private InnerRecevier mInnerReceiver;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private HomeRedPackageDialog mHomeRedPackageDialog;
    private int curPosition;   //当前位置

    @BindView(R.id.rl_urgency_notifi)
    RelativeLayout rl_urgency_notifi;


    private List<ImageInfo> mSysNotificationData;
    private SysNotificationView mSysNotificationView;
    private GuideNextCallback mGuideNextCallback;
    private Uri h5Uri;
    private Handler mHandler;

    private AuthForPersonDialog mAuthForPersonDialog;

    private AuthForPersonDialog.OnAuthListener mAuthListener;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //不调 super 的方法，这样在 Activity 被销毁的时候，FragmentManager 里的 Fragment 也不会得到保留
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .init();
        EventBus.getDefault().register(this);

        initView();
        //未授权隐私
        mAuthListener = new AuthForPersonDialog.OnAuthListener() {
            @Override
            public void onAuthClick() {

                CommonLocalData.authedPrivate();
                initAppData();
            }

            @Override
            public void onNoAuthClick() {
                // 清空数据
                MarkermallCircleAdapter.mShareCountId = 0;
                ActivityLifeHelper.getInstance().finishAllActivities();
            }
        };
        if (!CommonLocalData.getAuthedStatus()){
            if (mAuthForPersonDialog == null){
                mAuthForPersonDialog = new AuthForPersonDialog(MainActivity.this,mAuthListener);
                mAuthForPersonDialog.show();
            }

        }else{
            initAppData();
        }


    }
    private void initAppData(){
        mHandler = new Handler();
        try {

            onCallPermission();//6.0申请相机权限
            openPush();//打开推送
            initPush();
            initRecevier();
            mPresenter.getPicture(MainActivity.this);
            mPresenter.getAppInfo(MainActivity.this);//检查app版本更新
            mPresenter.putHeart(MainActivity.this);
            mPresenter.getSysNotification(MainActivity.this, false);
            getHomeRedPackageData();
            mPresenter.getTaobaoLink(MainActivity.this);
            ConfigListUtlis.getConfigList(MainActivity.this, ConfigListUtlis.getConfigAllKey(), null);
            mPresenter.getServerTime(MainActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
        gotoAd();
        h5Uri = getIntent().getData();
        AppUtil.sendWebOpenApp(h5Uri,MainActivity.this);
    }


    @Override
    protected int getViewLayout() {
        return R.layout.activity_main;
    }


    private void gotoAd() {
        ImageInfo imageInfo = (ImageInfo) getIntent().getSerializableExtra("imageInfo");
        if (imageInfo != null) {
//            SensorsDataUtil.getInstance().advClickTrack(imageInfo.getTitle(),imageInfo.getId()+"",imageInfo.getOpen()+"","我的",0,imageInfo.getClassId()+"",imageInfo.getUrl());
            statisticsStartAdOnclick(imageInfo.getId() + "");
           SensorsDataUtil.getInstance().advClickTrack(imageInfo.getTitle(),imageInfo.getId()+"",imageInfo.getOpen()+"","闪屏页",0,imageInfo.getClassId()+"",imageInfo.getUrl());
            BannerInitiateUtils.gotoAction(this, imageInfo);
        }
    }


    public void getHomeRedPackageData() {
        HomeRedPackageDialog.getData(this, new MyAction.OnResult<List<ImageInfo>>() {
            @Override
            public void invoke(final List<ImageInfo> arg) {
                mHomeRedPackageDialog = new HomeRedPackageDialog(MainActivity.this, arg);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mHomeRedPackageDialog != null) {
                                    if(arg.size()>=1){
                                        SensorsDataUtil.getInstance().exposureViewTrack("首页活动弹窗",arg.get(0).getOpen()+"",arg.get(0).getClassId()+"",arg.get(0).getUrl(),
                                                arg.get(0).getTitle(),arg.get(0).getId()+"");
                                    }

                            mHomeRedPackageDialog.show();
                        }
                    }
                }, 1000);
                mHomeRedPackageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mHomeRedPackageDialog = null;
                    }
                });
            }

            @Override
            public void onError() {

            }
        });
    }

    private void initRecevier() {
        try {

            // 监听有没有网络广播
            IntentFilter intentFilternet = new IntentFilter();
            intentFilternet.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            mNetworkChangeReceiver = new NetworkChangeReceiver();
            registerReceiver(mNetworkChangeReceiver, intentFilternet);

            //创建广播
            mInnerReceiver = new InnerRecevier();
            //动态注册广播
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            //启动广播
            registerReceiver(mInnerReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示升级
     *
     * @param data
     */
    @Override
    public void showUpdateView(AppUpgradeInfo data) {
        UpdateAppBean data1 = QianWenUpdateUtlis.getData(data);
        if ("2".equals(data.getUpgradde())) {//是否强制升级 1:是，2：否，3：静默更新
            //普通升级
            data1.setConstraint(false);
            QianWenUpdateUtlis.constraintUpdate(this, data.getDownload(), data1);
        } else if ("1".equals(data.getUpgradde())) {
            //强制升级
            data1.setConstraint(true);
            QianWenUpdateUtlis.constraintUpdate(this, data.getDownload(), data1);
        } else {//3：静默更新
            QianWenUpdateUtlis.onlyUpdateApp(this, data.getDownload(), data1);
        }

    }

    /**
     * 设置系统通知
     *
     * @param data
     * @param isLoginSucceed
     */
    @Override
    public void setSysNotificationData(List<ImageInfo> data, boolean isLoginSucceed) {
        this.mSysNotificationData = data;
        if (homePageFragment != null) {
            if (isLoginSucceed) {
                homePageFragment.cleseRecommendGoodsView();
            } else {
                homePageFragment.setSysNotificationData(mSysNotificationData);
            }
        }
        setSysNotificationView();
    }

    @Override
    public void saveTaobaoLinkData(List<String> data) {
        if (null != data && data.size() > 0) {
            ACache.get(this).put(C.sp.TAOBAO_LINK_CACHE, (Serializable) data);
        }
    }

    @Override
    public void showGrayUpgradeView(GrayUpgradeInfo upgradeInfo) {
        GrayUpgradeDialog grayUpgradeDialog = new GrayUpgradeDialog(this,R.style.dialog,upgradeInfo);
        grayUpgradeDialog.show();
    }

    private void setSysNotificationView() {
        rl_urgency_notifi.removeAllViews();
        int sysNotificationpos = 0;
        if (mSysNotificationData == null || mSysNotificationData.size() == 0) return;
        for (int i = 0; i < mSysNotificationData.size(); i++) {
            final ImageInfo imageInfo = mSysNotificationData.get(i);
            //1【首页】、2【商品详情页】、3【分类】、4【发圈】、5【我的】
            if (curPosition == C.mainPage.HOME) {
                sysNotificationpos = 1;
            } else if (curPosition == C.mainPage.SUPER_NAVIGATION) {
                sysNotificationpos = 3;
            } else if (curPosition == C.mainPage.CIRCLE) {
                sysNotificationpos = 4;
            } else if (curPosition == C.mainPage.MINE) {
                sysNotificationpos = 5;
            }
            if (sysNotificationpos == imageInfo.getDisplayPage()) {
                if (mSysNotificationView == null) {
                    mSysNotificationView = new SysNotificationView();
                }
                mSysNotificationView.addSysNotificationView(this, rl_urgency_notifi, imageInfo);
            }
        }
    }

    class NetworkChangeReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                isNetwork = true;
                ActivityLifeHelper.getInstance().hideNotWorkLinkSucceed();
            } else {
                ActivityLifeHelper.getInstance().addNotWorkLinkError();
                isNetwork = false;
            }
        }
    }

    class InnerRecevier extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        MarkermallCircleAdapter.mShareCountId = 0;

                        LogUtils.Log("CircleDayHotFrgment", "SYSTEM_DIALOG_REASON_HOME_KEY");
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                        LogUtils.Log("CircleDayHotFrgment", "SYSTEM_DIALOG_REASON_RECENT_APPS");
                        MarkermallCircleAdapter.mShareCountId = 0;
                    }
                }
            }
        }
    }


    private void initPush() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int inType = extras.getInt(C.Extras.pushType, 1);
            switch (inType) {
                case C.Push.homePage://首页
                    onClick(rl_homepage);
                    break;
                case C.Push.fenlei://分类
                    onClick(rl_shop);
                    break;
                case C.Push.partner://我的
                    onClick(rl_mine);
                    break;
                case C.Push.circle://发圈
                    onClick(rl_community);
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 打开推送
     */
    private void openPush() {
        UserInfo userInfo = UserLocalData.getUser(MainActivity.this);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getPhone())) {
            PushAction.bindAccount(getApplicationContext(), userInfo.getPhone());
            if (TextUtils.isEmpty(App.getACache().getAsString(userInfo.getPhone() + C.sp.isOpenHotMsgPush))) {
                PushAction.addTags(getApplicationContext(), C.Push.tag_everydayHotCommodity);
            }
        }
    }

    public void initView() {
        rl_mine = (RelativeLayout) findViewById(R.id.rl_mine);
        rl_community = (RelativeLayout) findViewById(R.id.rl_community);
        rl_homepage = (RelativeLayout) findViewById(R.id.rl_homepage);
        rl_shop = (RelativeLayout) findViewById(R.id.rl_shop);
        rl_number = (RelativeLayout) findViewById(R.id.rl_number);
        rl_mine.setOnClickListener(this);
        rl_community.setOnClickListener(this);
        rl_homepage.setOnClickListener(this);
        rl_shop.setOnClickListener(this);
        rl_number.setOnClickListener(this);
        menuList.add(rl_homepage);
        menuList.add(rl_shop);
        menuList.add(rl_number);
        menuList.add(rl_community);
        menuList.add(rl_mine);
        rl_homepage.setSelected(true);

        mViewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        homePageFragment = new HomeFragment();
        superNavigationFragment = new SuperNavigationFragment();
//        collegeFragment = new HomeCollegeFragment();
        numberFragment = new NumberFragment();
        circleFragment = new CircleFragment();
        partnerFragment = new MineFragment();
        fragments.add(homePageFragment);
        fragments.add(superNavigationFragment);
        fragments.add(numberFragment);
//        fragments.add(collegeFragment);
        fragments.add(circleFragment);
        fragments.add(partnerFragment);

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);

        ((HomeFragment) homePageFragment).setmGuideNextCallback(new GuideNextCallback() {
            @Override
            public void nextGuide() {
                if (LoginUtil.checkIsLogin(MainActivity.this, false) && UserLocalData.isShowGuide()) {
                    //showGuideCircle();
                    showGuideCollege();
                }
            }
        });


    }


    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        if (event.getAction().equals(EventBusAction.LOGINA_SUCCEED)) {  //登录成功
            if(homePageFragment!=null){
                homePageFragment.getLoginView();
            }
            mPresenter.getSysNotification(this, true);

        } else if (event.getAction().equals(EventBusAction.ACTION_SCHOOL)) {
            //打开会员首页
            onClick(rl_number);
        }
    }

    // 退出登录
    @Subscribe
    public void onEventMainThread(LogoutEvent event) {
        if (event.isPutError()) {
            PutErrorUtils.putMyServerLog(this, event.getLogoutErrorMsg());
        }
        curPosition = C.mainPage.HOME;
        mViewPager.setCurrentItem(0, false);
        resetMenuStatus(menuList.get(0));
        if (mSysNotificationData != null) {
            mSysNotificationData.clear();
        }
        rl_urgency_notifi.removeAllViews();
        if(homePageFragment!=null){
            homePageFragment.getLoginView();
        }
    }

    @Subscribe
    public void onEventMainThread(OpenCategoryEvent event) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_shop.performClick();
            }
        }, 500);

    }
    @Subscribe
    public void onEventMainThread(OpenNumberEvent event) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_number.performClick();
            }
        }, 500);

    }


    @Subscribe
    public void onEventMainThread(UpdateGradeEvent event) {
        //支付成功以后，根据积分到达的情况升级等级弹窗。
        UserInfo userInfo = UserLocalData.getUser();
        if (userInfo != null){
            if (C.UserType.member.equals(userInfo.getUserType())){
                updateGrade();

            }else if (C.UserType.vipMember.equals(userInfo.getUserType())){
                updateGradeForLeader();
            }
        }
    }

    /**
     * 升级vip的弹框
     */
    private void updateGrade(){

        NumberVipUpgradeDialog leaderUpgradeDialog = new NumberVipUpgradeDialog(this,R.style.dialog);
        leaderUpgradeDialog.setOnListner(new NumberVipUpgradeDialog.OnListener(){

            @Override
            public void onClick() {
                updateGradePresenter(MainActivity.this,Integer.parseInt(C.UserType.vipMember));
            }

        });
        leaderUpgradeDialog.show();
    }

    /**
     * 升级团队长的弹框
     */
    private void updateGradeForLeader(){
        NumberLeaderUpgradeDialog vipUpgradeDialog = new NumberLeaderUpgradeDialog(this,R.style.dialog);
        vipUpgradeDialog.setOnListner(new NumberLeaderUpgradeDialog.OnListener(){

            @Override
            public void onClick(){
                updateGradePresenter(MainActivity.this,Integer.parseInt(C.UserType.operator));
            }

        });
        vipUpgradeDialog.show();

    }

    /**
     * 升级
     * @param activity
     * @param userType
     */
    public void updateGradePresenter(BaseActivity activity, int userType) {
        updateUserGrade(activity, userType)
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
    /**
     * 用户等级升级
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<UpdateInfoBean>> updateUserGrade(BaseActivity fragment, int userGrade) {
        RequestUpdateUserBean updateUserBean = new RequestUpdateUserBean();
        updateUserBean.setType(userGrade);
        return RxHttp.getInstance().getUsersService().updateUserGrade(updateUserBean)
                .compose(RxUtils.<BaseResponse<UpdateInfoBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<UpdateInfoBean>>bindToLifecycle());
    }

    public void showError(String errorNo,String msg) {
        MyLog.i("test", "onFailure: " + this);
        if ("B1100007".equals(errorNo)
                ||"B1100008".equals(errorNo)
                ||"B1100009".equals(errorNo)
                || "B1100010".equals(errorNo)) {
            ViewShowUtils.showShortToast(this,msg);
        }
    }
    public void onGradeSuccess(UpdateInfoBean info) {
        if (info != null){
            UserInfo userInfo = UserLocalData.getUser();
            userInfo.setUserType(String.valueOf(info.getUserType()));
            userInfo.setMoreCoin(info.getMoreCoin());
            UserLocalData.setUser(this,userInfo);
        }else{
            MyLog.d("test","用户信息为空");
        }

    }

    public void showFinally() {
        LoadingView.dismissDialog();
    }

    //重置菜单状态
    private void resetMenuStatus(View view) {
        for (int i = 0; i < menuList.size(); i++) {
            menuList.get(i).setSelected(false);

        }
        view.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_homepage: //首页
                mViewPager.setCurrentItem(0, false);
                if (homePageFragment != null) {
                    if (mViewPager.getCurrentItem() == curPosition) {
                        ((HomeFragment) homePageFragment).selectFirst();
                    }
                }
                curPosition = C.mainPage.HOME;
                setSysNotificationView();

                break;
            case R.id.rl_shop: //分类
                mViewPager.setCurrentItem(1, false);
                curPosition =  C.mainPage.SUPER_NAVIGATION;
                superNavigationFragment.onResume();
                setSysNotificationView();
                break;
            case R.id.rl_number:
                if (!LoginUtil.checkIsLogin(MainActivity.this)) {
                    return;
                }
                //会员
                mViewPager.setCurrentItem(2, false);
                curPosition = C.mainPage.NUMBER;
//                collegeFragment.onResume();
                numberFragment.onResume();
                setSysNotificationView();
                break;
            case R.id.rl_community: //发圈
                mViewPager.setCurrentItem(3, false);
                curPosition = C.mainPage.CIRCLE;
                setSysNotificationView();
                break;
            case R.id.rl_mine: //我的
                if (!LoginUtil.checkIsLogin(MainActivity.this)) {
                    return;
                }
                mViewPager.setCurrentItem(4, false);
                curPosition = C.mainPage.MINE;
                setSysNotificationView();
                break;

            default:
                break;

        }
        BaseMainFragmeng fragment = (BaseMainFragmeng) fragments.get(curPosition);
        fragment.setUserVisibleHint(true);
        resetMenuStatus(v);
    }

    public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        UserLocalData.mMyHeadBitmap = null;

        if (mInnerReceiver != null) {
            this.unregisterReceiver(mInnerReceiver);
        }
        if (mNetworkChangeReceiver != null) {
            this.unregisterReceiver(mNetworkChangeReceiver);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }


    public void onCallPermission() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        CleanSdUtil.initCacheDirs(); //初始化文件目录
                        DeviceIDUtils.getimei(MainActivity.this);
                    }
                });
        String versionCode = AppUtil.getVersionCode(this);
        String version_Codesp = App.getACache().getAsString(C.sp.version_Code);
        if (versionCode == null || versionCode.equals(version_Codesp)) {
            return;
        }
        //判断该app是否打开了通知，如果没有的话就打开手机设置页面
        if (!AppUtil.isNotificationEnabled(this)) {
            showGotoNotification();
            App.getACache().put(C.sp.version_Code, versionCode);
        }
    }


    private void showGotoNotification() {
        OpenPushDialog openPushDialog = new OpenPushDialog(this);
        openPushDialog.show();
    }

    /**
     * 是否滑动
     *
     * @return
     */
    public boolean isSwipe() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * app闪屏页点击统计
     */

    private void statisticsStartAdOnclick(String id) {
        RequestSplashStatistics requestBean = new RequestSplashStatistics();
        requestBean.setAdId(id);
        RxHttp.getInstance().getCommonService().statisticsStartAdOnclick(requestBean)
                .compose(RxUtils.<BaseResponse>switchSchedulers())
                .compose(this.<BaseResponse>bindToLifecycle())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        MyLog.i("test", "已统计");
                    }
                });

    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }


    /**
     * 连续按两次返回键就退出
     */
    private int keyBackClickCount = 0;

    @Override
    public void onBackPressed() {
//        if (numberFragment != null) {
//            MyLog.i("test", "((CircleFragment)circleFragment).onBackPressed(): ");
////            if (((HomeCollegeFragment) collegeFragment).onBackPressed()) {
////                keyBackClickCount = 0;
////                return;
////            }
//        }

        switch (keyBackClickCount++) {
            case 0:
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        keyBackClickCount = 0;
                    }
                }, 3000);
                break;
            case 1:
                // 清空数据
                MarkermallCircleAdapter.mShareCountId = 0;
                ActivityLifeHelper.getInstance().finishAllActivities();
                break;
        }
    }


    @Override
    public BaseView getBaseView() {
        return this;
    }


    public void showGuideCollege() {
        if (null != rl_number) {
            GuideViewUtil.showGuideView(this, rl_number, GuideViewUtil.GUIDE_HOME_COLLEGE, 0, null, new GuideViewUtil.GuideCallback() {
                @Override
                public void onDissmiss() {
                    showGuideOrder();
                }
            });

        }
    }

    public void showGuideOrder() {
        if (null != rl_mine) {
            GuideViewUtil.showGuideView(this, rl_mine, GuideViewUtil.GUIDE_ORDER, 0, null, null);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GuideViewUtil.dismiss();
                }
            }, 3000);
        }
    }



}
