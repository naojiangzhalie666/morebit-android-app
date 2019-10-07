package com.jf.my.info.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.request.RequestOptions;
import com.jf.my.Activity.ShowWebActivity;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Dialog.VipUpgradeDialog;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.Module.common.widget.SwipeRefreshLayout;
import com.jf.my.R;
import com.jf.my.circle.CollegeListActivity;
import com.jf.my.circle.ui.ArticleListFragment;
import com.jf.my.info.contract.VipContract;
import com.jf.my.info.presenter.VipPresenter;
import com.jf.my.info.ui.fragment.ShareFriendsFragment;
import com.jf.my.main.ui.fragment.RankingFragment;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.mvp.base.frame.MvpActivity;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.CallBackObserver;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.HotKeywords;
import com.jf.my.pojo.ImageClassroom;
import com.jf.my.pojo.TeamInfo;
import com.jf.my.pojo.UpgradeInstructions;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.VipUseInfoBean;
import com.jf.my.pojo.request.ClassroomBean;
import com.jf.my.pojo.request.RequestVideoId;
import com.jf.my.utils.ActivityStyleUtil;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.FileUtils;
import com.jf.my.utils.GoodsUtil;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.LoginUtil;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.utils.action.MyAction;
import com.jf.my.view.AspectRatioView;
import com.jf.my.view.HorzProgressView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liys on 2019/1/11
 * Vip个人中心
 */
public class VipActivity extends MvpActivity<VipPresenter> implements VipContract.View, View.OnClickListener {

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.userIcon)
    RoundedImageView mUserIcon;
    @BindView(R.id.tv_name)
    TextView mUserName;
    @BindView(R.id.level)
    ImageView mUserLevel;
    @BindView(R.id.iv_limits)
    ImageView mUserLimits;
    @BindView(R.id.ar_limits)
    AspectRatioView ar_limits;
    @BindView(R.id.iv_exclusive_course)
    ImageView mExclusiveCourse;
    @BindView(R.id.ll_bootom_upgrade)
    View mBootomUpgrade;

    View mUpgrade;
    View mCompanyService;
    View mOperatorService;
    View mCommonMember;


    //1. 进度item
    TextView[] mConditions;
    HorzProgressView[] mHorzProgressView;
    TextView[] mTvProgress;

    //vip
    //2. mCompanyService (公司客服)
    RoundedImageView mCompanyServiceUserIcon;
    TextView mCompanyServiceUserName;
    //3. OperatorService (个人客服)
    RoundedImageView mOperatorServiceUserIcon;
    TextView mOperatorServiceUserName;
    TextView mOperatorServiceGrade;
    TextView mOperatorServiceInviteCode;
    TextView mOperatorServiceWeChat;
    ImageView mOperatorServiceQrCode;

    //普通会员 view
    List<ImageClassroom> imageClassrooms = new ArrayList<>(); //我的课堂 前3个
//    ImageView[] ivNewPeople = new ImageView[3]; //我的课堂 3个ImageView
//    ImageView[] ivIconMp4 = new ImageView[3]; //我的课堂 mp4图标


    String mUpGradeExplainUrl; //升级说明url
    String mServiceUrl; //联系我们url
    UserInfo userInfo;//个人数据
    private Bitmap mBitmap;
    private String mPictureName;

    //升级弹框提示
    final int UPGRADE_DIALOG1 = 1; // 普通会员(升级说明)
    final int UPGRADE_DIALOG2 = 2; //普通会员 (已经申请)
    final int UPGRADE_DIALOG3 = 3; //vip(升级说用)
    final int UPGRADE_DIALOG4 = 4; //vip(已经申请)

    List<ClassroomBean> classroomList = new ArrayList<>();

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, VipActivity.class));
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_vip;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        ActivityStyleUtil.setStatusBar(this);
        LoginUtil.getUserInfo(this, false, new MyAction.OnResultFinally<UserInfo>() {
            @Override
            public void onFinally() {
                LoadingView.dismissDialog();
            }

            @Override
            public void invoke(UserInfo userInfo) {
                initData(userInfo);
            }

            @Override
            public void onError() {
            }
        });
    }

    private void initData(UserInfo userInfo) {
        this.userInfo = userInfo;
        mUserName.setText(this.userInfo.getNickName());
        if ("null".equals(this.userInfo.getHeadImg()) || "NULL".equals(this.userInfo.getHeadImg()) || TextUtils.isEmpty(this.userInfo.getHeadImg())) {
            mUserIcon.setImageResource(R.drawable.logo);
        } else {
            LoadImgUtils.setImgCircle(this, mUserIcon, this.userInfo.getHeadImg());
        }


        if (C.UserType.operator.equals(this.userInfo.getPartner())) {  //运营专员
            mUserLevel.setImageResource(R.drawable.icon_yunyingshang);
            ar_limits.setAspectRatio(0.932F);
            mUserLimits.setBackgroundResource(R.drawable.yunyingshangwodequanli);
            mExclusiveCourse.setVisibility(View.VISIBLE);
            mExclusiveCourse.setOnClickListener(this);
//            mBootomUpgrade.setVisibility(View.GONE);
        } else if (C.UserType.vipMember.equals(this.userInfo.getPartner())) {    //VIP
            mUserLevel.setImageResource(R.drawable.icon_viphuiyuan);
            ar_limits.setAspectRatio(0.905F);
            mUserLimits.setBackgroundResource(R.drawable.vipwodequanxian);
            mExclusiveCourse.setVisibility(View.GONE);
//            mBootomUpgrade.setVisibility(View.VISIBLE);
            initCompanyService(); //初始化客服
            initVipView();

        } else if (C.UserType.member.equals(this.userInfo.getPartner())) {   //普通会员
            initCompanyService(); //初始化客服
            mUserLevel.setImageResource(R.drawable.icon_putonghuiyuan);
            ar_limits.setAspectRatio(1.211F);
            mUserLimits.setBackgroundResource(R.drawable.pintonghuiyuanquanxian);
            mExclusiveCourse.setVisibility(View.GONE);
            mBootomUpgrade.setVisibility(View.VISIBLE);

            mCommonMember = ((ViewStub) findViewById(R.id.common_member)).inflate();

            mConditions = new TextView[1];
            mHorzProgressView = new HorzProgressView[1];
            mTvProgress = new TextView[1];

            View upgradeItem = mCommonMember.findViewById(R.id.term);
            mConditions[0] = upgradeItem.findViewById(R.id.tv_condition);
            mHorzProgressView[0] = upgradeItem.findViewById(R.id.horzProgressView);
            mTvProgress[0] = upgradeItem.findViewById(R.id.tv_progress);
            upgradeItem.findViewById(R.id.tv_go).setOnClickListener(this);


            //我的课堂
            int[] newPeopleIds = {R.id.iv_new_people1, R.id.iv_new_people2, R.id.iv_new_people3};
            int[] iconMp4Ids = {R.id.icon_1_mp4, R.id.icon_2_mp4, R.id.icon_3_mp4};
            for (int i = 0; i < newPeopleIds.length; i++) {
//                ivNewPeople[i] = mCommonMember.findViewById(newPeopleIds[i]);
//                ivIconMp4[i] = mCommonMember.findViewById(iconMp4Ids[i]);
//                ivNewPeople[i].setOnClickListener(this);
                ImageClassroom imageClassroom = new ImageClassroom();
                imageClassroom.iconMp4 = mCommonMember.findViewById(iconMp4Ids[i]);
                imageClassroom.imageView = mCommonMember.findViewById(newPeopleIds[i]);
                imageClassroom.imageView.setOnClickListener(this);
                imageClassrooms.add(imageClassroom);
            }

            mCommonMember.findViewById(R.id.more).setOnClickListener(this); //更多
            mCommonMember.findViewById(R.id.btn_upgrade).setOnClickListener(this);
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendHttp();
            }
        });
        //第一次进入
        sendHttp();
    }

    private void sendHttp() {

        if (C.UserType.operator.equals(userInfo.getPartner())) { //运营专员的话,不需要发起请求
            return;
        }
        mPresenter.userInfo(VipActivity.this); //获取升级条件
        if (C.UserType.vipMember.equals(UserLocalData.getUser().getPartner())) { //vip
//            mPresenter.upgradeInstructions(this, "1"); //升级说明
            mPresenter.getServiceQrcode(VipActivity.this);//获取客服数据
            mPresenter.getConfigForKey(this);
        } else if (C.UserType.member.equals(UserLocalData.getUser().getPartner())) { //普通会员
//            mPresenter.upgradeInstructions(this, "0"); //升级说明
            mPresenter.getServiceQrcode(VipActivity.this);//获取客服数据
            mPresenter.myCurriculum(this);
        }
    }

    /**
     * 初始化vipView
     */
    public void initVipView() {
        mUpgrade = ((ViewStub) findViewById(R.id.upgrade)).inflate();
        //1. mUpgrade
        mUpgrade.findViewById(R.id.btn_vip_upgrade).setOnClickListener(this);
        mUpgrade.findViewById(R.id.btn_vip_upgrade2).setOnClickListener(this);
        View[] baseViews = {
                mUpgrade.findViewById(R.id.term_1_1),
                mUpgrade.findViewById(R.id.term_1_2),
                mUpgrade.findViewById(R.id.term_2_1),
                mUpgrade.findViewById(R.id.term_2_2),
                mUpgrade.findViewById(R.id.term_2_3)
        };
        mConditions = new TextView[baseViews.length];
        mHorzProgressView = new HorzProgressView[baseViews.length];
        mTvProgress = new TextView[baseViews.length];
        TextView[] go = new TextView[baseViews.length];

        String[] txt = {"去邀请", "去赚佣", "去邀请", "", "去赚佣"};

        for (int i = 0; i < baseViews.length; i++) {
            mConditions[i] = baseViews[i].findViewById(R.id.tv_condition);
            mHorzProgressView[i] = baseViews[i].findViewById(R.id.horzProgressView);
            mTvProgress[i] = baseViews[i].findViewById(R.id.tv_progress);
            go[i] = baseViews[i].findViewById(R.id.tv_go);
            go[i].setText(txt[i]);
            go[i].setOnClickListener(this);
        }
        go[3].setVisibility(View.GONE);

    }

    /**
     * 初始化客服
     */
    private void initCompanyService() {
        mCompanyService = ((ViewStub) findViewById(R.id.company_service)).inflate();
        mOperatorService = ((ViewStub) findViewById(R.id.operator_service)).inflate();

        //2. mCompanyService
        mCompanyServiceUserIcon = mCompanyService.findViewById(R.id.userIcon);
        mCompanyServiceUserName = mCompanyService.findViewById(R.id.userName);
        mCompanyService.findViewById(R.id.tv_contact).setOnClickListener(this);

        //3. mOperatorService
        mOperatorServiceUserIcon = mOperatorService.findViewById(R.id.userIcon);
        mOperatorServiceUserName = mOperatorService.findViewById(R.id.userName);
        mOperatorServiceGrade = mOperatorService.findViewById(R.id.tv_grade);
        mOperatorServiceInviteCode = mOperatorService.findViewById(R.id.tv_invite_code);
        mOperatorServiceQrCode = mOperatorService.findViewById(R.id.iv_qr_code);
        mOperatorServiceWeChat = mOperatorService.findViewById(R.id.weChat);

        mOperatorService.findViewById(R.id.tv_save).setOnClickListener(this);
        mOperatorService.findViewById(R.id.tv_copy).setOnClickListener(this);

//        mPresenter.getServiceQrcode(this);//获取客服数据
    }

    @OnClick({R.id.bnt_back, R.id.ll_bootom_upgrade})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bnt_back:
                finish();
                break;
            case R.id.tv_go: // 去邀请/去赚佣
                ViewGroup parent = (ViewGroup) v.getParent();
                if (parent.getId() == R.id.term_1_1) {
                    OpenFragmentUtils.goToSimpleFragment(this, ShareFriendsFragment.class.getName(), new Bundle());
                } else if (parent.getId() == R.id.term_1_2) { //跳转>>爆款排行
                    OpenFragmentUtils.goToSimpleFragment(this, RankingFragment.class.getName(), null);
                } else if (parent.getId() == R.id.term_2_1) {
                    OpenFragmentUtils.goToSimpleFragment(this, ShareFriendsFragment.class.getName(), new Bundle());
                } else if (parent.getId() == R.id.term_2_3) {
                    OpenFragmentUtils.goToSimpleFragment(this, RankingFragment.class.getName(), null);
                } else if (parent.getId() == R.id.term) { //普通会员
                    OpenFragmentUtils.goToSimpleFragment(this, ShareFriendsFragment.class.getName(), new Bundle());
                }
                break;
            case R.id.tv_contact: //联系我们
                startWeb("专属客服", mServiceUrl);
                break;
            case R.id.tv_save: //保存二维码

//                if (TextUtils.isEmpty(localPath)) {
                if (mBitmap == null) {
                    Toast.makeText(this, "二维码还没有生成", Toast.LENGTH_SHORT).show();
                } else {

                    Uri uri = GoodsUtil.saveBitmap(this, mBitmap, mPictureName);
                    if (uri != null) {
                        ViewShowUtils.showShortToast(this, "保存成功");
                    }
                }
//                } else {
//                    Toast.makeText(this, "二维码已保存： " + localPath, Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.tv_copy: //复制
                AppUtil.coayText(this, mOperatorServiceWeChat.getText() + "");
                Toast.makeText(this, "已复制到粘贴版", Toast.LENGTH_SHORT).show();
                break;
            case R.id.more: //更多 (我的课堂)
                CollegeListActivity.start(this, "我的课程", "0", CollegeListActivity.NEW_COURSE);
                break;
            case R.id.iv_new_people1: // 我的课堂1
//                startWeb("了解蜜源", "https://api.gzmiyuan.com/api/h5/businessSchool/#/detail?id=18");
                if (classroomList.size() > 0) {
                    clickClassrooms(classroomList.get(0));
                }
                break;
            case R.id.iv_new_people2: //我的课堂2
//                startWeb("蜜源的优势", "https://api.gzmiyuan.com/api/h5/businessSchool/#/detail?id=19");
                if (classroomList.size() > 1) {
                    clickClassrooms(classroomList.get(1));
                }
                break;
            case R.id.iv_new_people3: // 我的课堂3
//                startWeb("如何使用蜜源", "https://api.gzmiyuan.com/api/h5/businessSchool/#/detail?id=20");
                if (classroomList.size() > 2) {
                    clickClassrooms(classroomList.get(2));
                }
                break;
            case R.id.btn_vip_upgrade: //vip 升级说明1
                mPresenter.upgradeInstructions2(this, 1);
                break;
            case R.id.btn_vip_upgrade2: //vip 升级说明2
                mPresenter.upgradeInstructions2(this, 2);
                break;
            case R.id.btn_upgrade: //普通用户 升级说明
                mPresenter.upgradeInstructions2(this, 1);
//                if (TextUtils.isEmpty(mUpGradeExplainUrl)) {
//                    return;
//                }
//                startWeb("升级说明", mUpGradeExplainUrl);
                break;
            case R.id.ll_bootom_upgrade: //申请升级
                mPresenter.upgradeVip(this);
                break;
            case R.id.iv_exclusive_course: //专属课程
                ArticleListFragment.start(this, "4", "高级学堂");
                break;
        }
    }

    //点击我的课堂
    public void clickClassrooms(ClassroomBean bean) {
        //1.文章 2.视频
        if (bean.getType() == 2) { //视频
            VideoActivity.start(this, bean.getUrl(), bean.getTitle());
            updateVideoClicks(bean.getId());

        } else { //文章
            startWeb(bean.getTitle(), bean.getUrl() + "?id=" + bean.getId());
        }
    }

    private void updateVideoClicks(int id) {
        //网络请求
        RxHttp.getInstance().getUsersService().mp4Browse(new RequestVideoId(id))
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                    }
                });
    }


    /**
     * 升级说明 弹窗
     *
     * @param data
     */
    public void vipUpgradeDialog(final UpgradeInstructions data) {
        String content = data.getDesc();
        VipUpgradeDialog vipUpgradeDialog = new VipUpgradeDialog(this, R.style.dialog);
        vipUpgradeDialog.setContent(content); //内容
        //图片是否显示
        if (TextUtils.isEmpty(data.getPicture())) {
            vipUpgradeDialog.getIvIcon().setVisibility(View.GONE);
            vipUpgradeDialog.getTvTitle().setVisibility(View.VISIBLE);
        } else {
            LoadImgUtils.setImg(this, vipUpgradeDialog.getIvIcon(), data.getPicture());
            vipUpgradeDialog.getIvIcon().setVisibility(View.VISIBLE);
            vipUpgradeDialog.getTvTitle().setVisibility(View.GONE);
        }
        TextView tvLeft = vipUpgradeDialog.getTvLeft();
        TextView tvRight = vipUpgradeDialog.getTvRight();

        //弹框类型
        int level = data.getDisplayLevel(); //1.会员 2.代理商 3.全部
        int type = UPGRADE_DIALOG1;
//        if(C.UserType.member.equals(this.userInfo.getPartner())){ //普通会员
        if (level == 1) { //普通会员
            if (data.getUpgrade() == 1) { //已申请升级
                type = UPGRADE_DIALOG2;
            }
//        }else if(C.UserType.vipMember.equals(this.userInfo.getPartner())){ //vip
        } else if (level == 2) { //vip(代理商)
            type = UPGRADE_DIALOG3;
//            if(data.getUpgrade() == 1){
//                type = UPGRADE_DIALOG4;
//            }else{
//                type = UPGRADE_DIALOG3;
//            }
        }

        switch (type) {
            case UPGRADE_DIALOG1: // 普通会员(升级说明)
//                tvLeft.setVisibility(View.GONE);
//                tvRight.setText("去邀请好友");
//                break;
            case UPGRADE_DIALOG2: //普通会员 (已经申请)
                tvLeft.setVisibility(View.GONE);
                if (TextUtils.isEmpty(data.getButtonDesc())) {
                    tvRight.setText("继续邀请");
                } else {
                    tvRight.setText(data.getButtonDesc());
                }
                break;
            case UPGRADE_DIALOG3: //vip(升级说明)
                tvLeft.setText("继续邀请");
                tvRight.setText("赚佣金");
                break;
            case UPGRADE_DIALOG4: //vip(已经申请)
                tvLeft.setVisibility(View.GONE);
                tvRight.setText("继续邀请");
                break;
        }

        final int finalType = type;
        vipUpgradeDialog.setOnListner(new VipUpgradeDialog.OnListener() {
            @Override
            public void onClick(int clickType) {
                if (clickType == VipUpgradeDialog.OnListener.LETF) {
                    OpenFragmentUtils.goToSimpleFragment(VipActivity.this, ShareFriendsFragment.class.getName(), new Bundle());
                } else if (clickType == VipUpgradeDialog.OnListener.IMG) { // 跳转banner事件
                    ShowWebActivity.start(VipActivity.this, data.getUrl(), data.getTitle());
                } else { //
                    switch (finalType) {
                        case UPGRADE_DIALOG3: //赚佣金
                            OpenFragmentUtils.goToSimpleFragment(VipActivity.this, RankingFragment.class.getName(), null);
                            break;
                        case UPGRADE_DIALOG1:
                        case UPGRADE_DIALOG2:
                        case UPGRADE_DIALOG4:
                            OpenFragmentUtils.goToSimpleFragment(VipActivity.this, ShareFriendsFragment.class.getName(), new Bundle());
                            break;
                    }
                }
            }
        });
        vipUpgradeDialog.show();

    }

    /**
     * 跳转到web页面
     */
    public void startWeb(String title, String url) {
        ShowWebActivity.start(this,url,title);
//        Intent it = new Intent(this, ShowWebActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("title", title);
//        bundle.putString("url", url);
//        it.putExtras(bundle);
//        startActivity(it);
    }

    @Override
    public void onSuccess(VipUseInfoBean vipUseInfoBean) {
        if (vipUseInfoBean == null) {
            return;
        }

        //是否满足升级条件
        int type = vipUseInfoBean.getType();
        if (type == 1) {
            mBootomUpgrade.setVisibility(View.VISIBLE);
        } else {
            mBootomUpgrade.setVisibility(View.GONE);
        }

        if (C.UserType.member.equals(UserLocalData.getUser().getPartner())) {    //普通会员
            setMemberInfo(vipUseInfoBean);
        }
        if (C.UserType.vipMember.equals(UserLocalData.getUser().getPartner())) {    //VIP
            setVipInfo(vipUseInfoBean);
        }
    }

    @SuppressLint("DefaultLocale")
    private void setMemberInfo(VipUseInfoBean vipUseInfoBean) { //普通会员
        if (vipUseInfoBean == null || vipUseInfoBean.getConditionOne() == null) {
            return;
        }
        //条件:
        List<VipUseInfoBean.ConditionOneBean> oneList = vipUseInfoBean.getConditionOne();
//        String progressStr = "%d/%d(人)";
        mCommonMember.findViewById(R.id.query_failure).setVisibility(View.GONE);
        mCommonMember.findViewById(R.id.term).setVisibility(View.VISIBLE);
        if (oneList != null && oneList.size() != 0) { //只有一个条件
            VipUseInfoBean.ConditionOneBean oneBean = oneList.get(0);
            mConditions[0].setText(oneBean.getMessage());
            mTvProgress[0].setText(oneBean.getSchedule());
            mHorzProgressView[0].setMax(oneBean.getOneLevelCount() * 1.00);
            mHorzProgressView[0].setCurrentNum(oneBean.getFinishOneLevelCount() * 1.00);
        }
    }

    @SuppressLint("DefaultLocale")
    private void setVipInfo(VipUseInfoBean vipUseInfoBean) { //Vip
        if (vipUseInfoBean == null || vipUseInfoBean.getConditionOne() == null) {
            return;
        }

        mUpgrade.findViewById(R.id.ll_content).setVisibility(View.VISIBLE);
        mUpgrade.findViewById(R.id.query_failure1).setVisibility(View.GONE);
        //条件1:
        List<VipUseInfoBean.ConditionOneBean> oneList = vipUseInfoBean.getConditionOne();
        if (oneList != null) {
            for (int i = 0; i < oneList.size(); i++) {
                VipUseInfoBean.ConditionOneBean oneBean = oneList.get(i);
                mConditions[i].setText(oneBean.getMessage());
                mTvProgress[i].setText(oneBean.getSchedule());
                mHorzProgressView[i].setMax(oneBean.getOneLevelCount() * 1.00);
                mHorzProgressView[i].setCurrentNum(oneBean.getFinishOneLevelCount() * 1.00);
            }
        }

        //条件2:
        List<VipUseInfoBean.ConditionOneBean> twoList = vipUseInfoBean.getConditionTwo();
        if (twoList == null) {
            return;
        }
        for (int i = 0; i < twoList.size(); i++) {
            VipUseInfoBean.ConditionOneBean twoBean = twoList.get(i);
            int posision = i + 2;
            mConditions[posision].setText(twoBean.getMessage());
            mTvProgress[posision].setText(twoBean.getSchedule());
            mHorzProgressView[posision].setMax(twoBean.getOneLevelCount() * 1.00);
            mHorzProgressView[posision].setCurrentNum(twoBean.getFinishOneLevelCount() * 1.00);
        }
    }

    @Override
    public void serviceSuccess(TeamInfo data) {
        //客服回调
        if (data.getCustomerServiceType() == 1) { //公司
            mCompanyService.setVisibility(View.VISIBLE);
            mOperatorService.setVisibility(View.GONE);
            //设置客服信息
            LoadImgUtils.setImg(this, mCompanyServiceUserIcon, data.getHeadImg());
            mCompanyServiceUserName.setText(data.getNickname());
        } else { //个人
            mCompanyService.setVisibility(View.GONE);
            mOperatorService.setVisibility(View.VISIBLE);
            LoadImgUtils.setImg(this, mOperatorServiceUserIcon, data.getHeadImg());
            mOperatorServiceUserName.setText(data.getNickname()); //名字
            String type = data.getUserType() + "";
            if (C.UserType.member.equals(type)) { //类型
                mOperatorServiceGrade.setText("普通会员");
            } else if (C.UserType.vipMember.equals(type)) {
                mOperatorServiceGrade.setText("vip会员");
            } else if (C.UserType.operator.equals(type)) {
                mOperatorServiceGrade.setText("运营专员");
            }
            mOperatorServiceWeChat.setText(data.getWxNumber()); //微信号
            mOperatorServiceInviteCode.setText(data.getInviteCode()); //邀请码

            LoadImgUtils.setImg(this, mOperatorServiceQrCode, data.getWxQrCode(),R.drawable.service_person_qrcode); //二维码
            //设置二维码
            String wxQrCode = data.getWxQrCode();
            if (!TextUtils.isEmpty(wxQrCode)) {
                mPictureName = FileUtils.getPictureName(wxQrCode);
                try {// ios 没有压缩图片 有可能内存溢出  mQrcode.setImageBitmap(resource);
                    //设置图片圆角角度
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.service_person_qrcode)
                            .error(R.drawable.service_person_qrcode);
                    LoadImgUtils.getBitmapObservable(this, wxQrCode, options)
                            .subscribe(new CallBackObserver<Bitmap>() {
                                @Override
                                public void onNext(@NonNull Bitmap bitmap) {
                                    mBitmap = bitmap;
                                    mOperatorServiceQrCode.setImageBitmap(bitmap);
                                }

                                @Override
                                public void onError(Throwable e) {
                                }
                            });

                } catch (Exception e) {
                    LoadImgUtils.setImg(this, mOperatorServiceQrCode, wxQrCode,R.drawable.service_person_qrcode);
                }
            }
        }

    }


    @Override
    public void upgradeVipSuccess(String data) {
        ToastUtils.showShort(data);
//        mBootomUpgrade.setVisibility(View.GONE);
    }

    @Override
    public void upgradeVipFailure() {
        MyLog.i("test", "upgradeVipFailure");
        if (C.UserType.member.equals(UserLocalData.getUser().getPartner())) {    //普通会员
            mCommonMember.findViewById(R.id.term).setVisibility(View.GONE);
            mCommonMember.findViewById(R.id.query_failure).setVisibility(View.VISIBLE);
        } else if (C.UserType.vipMember.equals(UserLocalData.getUser().getPartner())) { //VIP
            mUpgrade.findViewById(R.id.ll_content).setVisibility(View.GONE);
            mUpgrade.findViewById(R.id.query_failure1).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void upgradeInstructionsSuccess(String data) {
        mUpGradeExplainUrl = data;
    }

    @Override
    public void upgradeInstructionsSuccess2(UpgradeInstructions data) {
        //升级说明
        if (data == null) {
            ToastUtils.showShort("数据为空");
            return;
        }
        vipUpgradeDialog(data);

    }

    @Override
    public void forKeySuccess(HotKeywords data) {
        mServiceUrl = data.getSysValue();
    }

    @Override
    public void doFinally() {
        LoadingView.dismissDialog();
        swipe.setRefreshing(true);
    }

    @Override
    public void classroomSuccess(List<ClassroomBean> datas) { //我的课程
        if (datas == null) {
            return;
        }
        classroomList.clear();
        for (int i = 0; i < datas.size(); i++) {
            ClassroomBean bean = datas.get(i);
            ImageClassroom imageClassroom = imageClassrooms.get(i);
            if (bean.getType() == 1) { //文章
                imageClassroom.isMp4 = false;
                imageClassroom.iconMp4.setVisibility(View.GONE);
            } else if (bean.getType() == 2) { //视频
                imageClassroom.isMp4 = true;
                imageClassroom.iconMp4.setVisibility(View.VISIBLE);
            }
            LoadImgUtils.setImg(this, imageClassroom.imageView, bean.getImage());
            classroomList.add(bean);
        }
    }
}
