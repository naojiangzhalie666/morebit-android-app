package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout;
import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.lzy.okgo.model.Progress;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
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
import com.zjzy.morebit.adapter.SubNumberAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.goods.shopping.ui.fragment.CategoryListFragment2;
import com.zjzy.morebit.home.fragment.AdvancedClassFragment;
import com.zjzy.morebit.home.fragment.MembershipFragment;
import com.zjzy.morebit.home.fragment.SelectGoodsFragment;
import com.zjzy.morebit.home.fragment.ShoppingMallFragment;
import com.zjzy.morebit.main.ui.fragment.GuessLikeFragment;
import com.zjzy.morebit.main.ui.fragment.JdChildFragment;
import com.zjzy.morebit.main.ui.fragment.JdongListFragment;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.DoorGodCategoryBean;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.VipBean;
import com.zjzy.morebit.pojo.event.RefreshUserInfoEvent;
import com.zjzy.morebit.pojo.goods.Child2;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.pddjd.PddJdTitleTypeItem;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.pojo.request.base.RequestBaseOs;
import com.zjzy.morebit.pojo.requestbodybean.RequestInviteCodeBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.RecyclerViewSpacesItemDecoration;
import com.zjzy.morebit.utils.SpaceItemDecorationUtils;
import com.zjzy.morebit.utils.SwipeDirectionDetector;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.HorzProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * 每日爆款
 */
public class NumberSubFragment extends BaseFragment {

    private static final int REQUEST_COUNT = 10;
    View mView;

    RecyclerView mReUseGridView;


    TextView tvUserType;

    private ImageView go_top;
    //    @BindView(R.id.ll_user_grade)
    LinearLayout llUserGrade;

    RoundedImageView mUserIcon;
    TextView myGradedView;

    TextView moreCoinBiaozhun;

    HorzProgressView mHorzProgressView;
//    ImageView leader_icon;

    TextView updateVip;
    View headView;
    TextView userName;
    TextView txtWelcome;

    SubNumberAdapter mAdapter;
    RoundedImageView cardNumber;
    RoundedImageView cardVip;
    RoundedImageView cardLeader;

    TextView tvGrowthValue;
    private SwipeDirectionDetector swipeDirectionDetector;


    UserInfo mUserInfo;
    private int page = 1;
    private ImageView huiyuan1;
    private TextView get_operator_growth, tv_huiyuan2, tv_vip2, vip_optional,
            vip_settlement, vip_directly, vip_intermedium, tv_more, getMorce, tv_operator, tv_huo, tv_skill, tv_bao;
    private ImageView grade;
    private LinearLayout vip_reward, ll4, ll5, ll3, hy, vip, tdz, ll6;
    private RelativeLayout vip_rl1, vip_rl3, rl3, rl4;
    private RecyclerView skill_rcy, activity_rcy;
    private SkillAdapter skillAdapter;
    private ActivityFloorAdapter floorAdapter;
    private TextView vip_kefu;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView netscrollview;

    private XTabLayout xablayout;
    private List<String> pagerData = new ArrayList<>();
    private ViewPager viewPager;
    private int currentViewPagerPosition = 0;
    private boolean canRefresh = true;
    private AppBarLayout mAppBarLt;
    private RoundedImageView vip_tou;
    private TextView vip_name, vip_grade, group_quanyi, more_corn_biaozhun, vip_zhuan, upgrade;
    private HorzProgressView horzProgressView;
    private LinearLayout group_ll, vip_ll,ll_vip;
    private ImageView img_vip;
    private TextView tv_coin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(NumberSubFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (headView == null) {
            headView = inflater.inflate(R.layout.fragment_vip_header, container, false);
            //  headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_number2_header, null);
            initHeadView(headView);
            initView(headView);

//            initTan();
//             initData();
//            initPush();
//            refreshData();


        }
        return headView;
    }


    private void initPush() {

        String growth = SPUtils.getInstance().getString("growth");
        if (growth != null) {
            if (growth.equals("360")) {
                updateGrade();
                SPUtils.getInstance().remove("growth");
            }

            if (growth.equals("50000")) {
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
        mAppBarLt = view.findViewById(R.id.app_bar_lt);
        xablayout = view.findViewById(R.id.xablayout);
        viewPager = view.findViewById(R.id.viewPager);
        initViewPager();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setNestedScrollingEnabled(true);
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        swipeRefreshLayout.setProgressViewOffset(true, -0, 100);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        swipeRefreshLayout.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF645B"));
        //设置触发刷新的距离
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        tv_coin=view.findViewById(R.id.tv_coin);//成长值
        vip_tou = view.findViewById(R.id.vip_tou);//头像
        vip_name = view.findViewById(R.id.vip_name);//昵称
        getMorce = view.findViewById(R.id.getMorce);//获取成长值
        vip_grade = view.findViewById(R.id.vip_grade);//会员等级
        group_quanyi = view.findViewById(R.id.group_quanyi);//会员权益（团队长）
        more_corn_biaozhun = view.findViewById(R.id.more_corn_biaozhun);//成长值
        vip_zhuan = view.findViewById(R.id.vip_zhuan);//赚
        horzProgressView = view.findViewById(R.id.horzProgressView);//进度条
        upgrade = view.findViewById(R.id.upgrade);//升级
        group_ll = view.findViewById(R.id.group_ll);//团队长模块
        vip_ll = view.findViewById(R.id.vip_ll);//VIP模块
        img_vip = view.findViewById(R.id.img_vip);//vip icon
        ll_vip=view.findViewById(R.id.ll_vip);
        upgrade.setOnClickListener(new View.OnClickListener() {//升级VIP
            @Override
            public void onClick(View v) {
                GoodsUtil.getVipH5(getActivity());
            }
        });
        initTou();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initTou();
                EventBus.getDefault().post(new MessageEvent(EventBusAction.ACTION_REFRSH));
            }
        });


    }

    private void initTou() {
        UserInfo mUserInfo = UserLocalData.getUser(getActivity());
        if (mUserInfo != null) {
            initViewData(mUserInfo);
        }
        getUserDetails(this).doFinally(new Action() {
            @Override
            public void run() throws Exception {
            }
        })
                .subscribe(new DataObserver<UserInfo>() {
                    @Override
                    protected void onSuccess(UserInfo data) {
                        showDetailsView(data);
                        swipeRefreshLayout.setRefreshing(false);

                    }
                });


    }

    private void initViewPager() {
        pagerData.add("优选商城");
        pagerData.add("会员权益");
        pagerData.add("进阶学院");

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        swipeDirectionDetector = new SwipeDirectionDetector();
        viewPager.setAdapter(pagerAdapter);
        xablayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                swipeDirectionDetector.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                swipeDirectionDetector.onPageSelected(position);
                currentViewPagerPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                swipeDirectionDetector.onPageScrollStateChanged(state);
                MyLog.d("addOnPageChangeListener", " onPageScrollStateChanged  state = " + state);
            }
        });
//添加页面滑动监听,控制 SwipeRefreshLayout与ViewPager滑动冲突
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == 1) {
                    swipeRefreshLayout.setEnabled(false);//设置不可触发
                } else if (state == 2 && canRefresh) {
                    swipeRefreshLayout.setEnabled(true);//设置可触发
                }
            }
        });
        mAppBarLt.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i < -150 && canRefresh) {
                    swipeRefreshLayout.setEnabled(false);//设置可触发
                    canRefresh = false;
                } else if (i > -150 && !canRefresh) {
                    canRefresh = true;
                    swipeRefreshLayout.setEnabled(true);
                }
            }
        });

    }


    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return pagerData == null ? "" + position : pagerData.get(position);

        }

        @Override
        public Fragment getItem(int position) {
            String name = pagerData.get(position);
            if ("优选商城".equals(name)) {
                ShoppingMallFragment mallFragment = ShoppingMallFragment.newInstance();
                return mallFragment;
            } else if ("会员权益".equals(name)) {
                MembershipFragment membershipFragment = MembershipFragment.newInstance();
                return membershipFragment;
            } else {
                AdvancedClassFragment advancedClassFragment = AdvancedClassFragment.newInstance();
                return advancedClassFragment;
            }


        }

        @Override
        public int getCount() {
            return pagerData != null ? pagerData.size() : 0;
        }


    }

    private void initHeadView(View headView) {
//        go_top = headView.findViewById(R.id.go_top);
//        mReUseGridView = (RecyclerView) headView.findViewById(R.id.mReUseGridView);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                refreshData();
//                initData();
//                swipeRefreshLayout.setRefreshing(false);
//
//            }
//        });
//
//
//        netscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                //判断是否滑到的底部
//                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    page++;
//                    getData();
//
//                }
//
//
//
//                if (scrollY > 1000) {
//                    Log.e("=====", "下滑");
//                    go_top.setVisibility(View.VISIBLE);
//                }
//                if (scrollY < oldScrollY) {
//                    Log.e("=====", "上滑");
//                }
//
//                if (scrollY == 0) {
//                    Log.e("=====", "滑倒顶部");
//                    go_top.setVisibility(View.GONE);
//                }
//
//
//            }
//        });
//
//
//        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
//        //设置图标的间距
//        // SpaceItemDecorationUtils spaceItemDecorationUtils = new SpaceItemDecorationUtils(10, 2);
//        mReUseGridView.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 2)));
//        mReUseGridView.setLayoutManager(manager);
//
//
//        mReUseGridView.setNestedScrollingEnabled(false);
//        userName = (TextView) headView.findViewById(R.id.user_name);
//        mUserIcon = (RoundedImageView) headView.findViewById(R.id.userIcon);
//        moreCoinBiaozhun = (TextView) headView.findViewById(R.id.more_corn_biaozhun);
//        mHorzProgressView = (HorzProgressView) headView.findViewById(R.id.horzProgressView);
////
//        updateVip = (TextView) headView.findViewById(R.id.btn_number_update_vip);
//        tvGrowthValue = (TextView) headView.findViewById(R.id.tv_growth_value);
//        grade = (ImageView) headView.findViewById(R.id.grade);
//        vip_reward = headView.findViewById(R.id.vip_reward);
//        vip_rl1 = headView.findViewById(R.id.vip_rl1);
//        vip_rl3 = headView.findViewById(R.id.vip_rl3);
//        get_operator_growth = headView.findViewById(R.id.get_operator_growth);
//
//
//        tv_huiyuan2 = headView.findViewById(R.id.tv_huiyuan2);
//        tv_vip2 = headView.findViewById(R.id.tv_vip2);
//        ll3 = headView.findViewById(R.id.ll3);
//        ll4 = headView.findViewById(R.id.ll4);
//        ll5 = headView.findViewById(R.id.ll5);
//        rl3 = headView.findViewById(R.id.rl3);
//        rl4 = headView.findViewById(R.id.rl4);
//        skill_rcy = headView.findViewById(R.id.skill_rcy);
//        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        skill_rcy.setLayoutManager(manager2);
//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(skill_rcy);
//        skill_rcy.setNestedScrollingEnabled(false);
//        huiyuan1 = headView.findViewById(R.id.huiyuan1);
//        vip_optional = headView.findViewById(R.id.vip_optional);//自选商品
//        vip_settlement = headView.findViewById(R.id.vip_settlement);//结算
//        vip_directly = headView.findViewById(R.id.vip_directly);//直属
//        vip_intermedium = headView.findViewById(R.id.vip_intermedium);//间属
//        activity_rcy = headView.findViewById(R.id.activity_rcy);//活动专区
//        GridLayoutManager manager3 = new GridLayoutManager(getActivity(), 2);
//        activity_rcy.setLayoutManager(manager3);
//        activity_rcy.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 3)));
//        tv_more = headView.findViewById(R.id.tv_more);
//        getMorce = headView.findViewById(R.id.getMorce);
//        vip_kefu = headView.findViewById(R.id.vip_kefu);//专属客服
//        hy = headView.findViewById(R.id.hy);
//        vip = headView.findViewById(R.id.vip);
//        tdz = headView.findViewById(R.id.tdz);
//        tv_operator = headView.findViewById(R.id.tv_operator);
//        tv_huo = headView.findViewById(R.id.tv_huo);
//        tv_skill = headView.findViewById(R.id.tv_skill);
//        tv_bao = headView.findViewById(R.id.tv_bao);
//        ll6 = headView.findViewById(R.id.ll6);
//
//        userName.getPaint().setFakeBoldText(true);
//        updateVip.getPaint().setFakeBoldText(true);
//        tv_operator.getPaint().setFakeBoldText(true);
//        tv_huo.getPaint().setFakeBoldText(true);
//        tv_skill.getPaint().setFakeBoldText(true);
//        tv_bao.getPaint().setFakeBoldText(true);
//        tv_more.setOnClickListener(new View.OnClickListener() {//跳转技能课堂
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), SkillClassActivity.class));
//            }
//        });
//        getMorce.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                netscrollview.smoothScrollTo(0, ll6.getTop());
//            }
//
//
//        });
//
//        go_top.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                netscrollview.scrollTo(0,0);
//            }
//        });


    }

    private void updataUser() {

        UserInfo mUserInfo = UserLocalData.getUser(getActivity());
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
    private void updateGrade() {

        NumberVipUpgradeDialog leaderUpgradeDialog = new NumberVipUpgradeDialog(getActivity(), R.style.dialog);
        leaderUpgradeDialog.setOnListner(new NumberVipUpgradeDialog.OnListener() {

            @Override
            public void onClick() {
                if (mUserInfo.getMoreCoin() < 360) {
                    ToastUtils.showLong("成长值不足");
                } else {
                    updateGradePresenter(NumberSubFragment.this, Integer.parseInt(C.UserType.vipMember));
                }

            }

        });
        leaderUpgradeDialog.show();
    }

    /**
     * 升级团队长的弹框
     */
    private void updateGradeForLeader() {
        NumberLeaderUpgradeDialog vipUpgradeDialog = new NumberLeaderUpgradeDialog(getActivity(), R.style.dialog);
        vipUpgradeDialog.setOnListner(new NumberLeaderUpgradeDialog.OnListener() {

            @Override
            public void onClick() {
                if (mUserInfo.getMoreCoin() < 50000) {
                    ToastUtils.showLong("成长值不足");
                } else {
                    updateGradePresenter(NumberSubFragment.this, Integer.parseInt(C.UserType.operator));
                }
            }

        });
        vipUpgradeDialog.show();

    }

    private void initViewData(UserInfo info) {
        if ("null".equals(info.getHeadImg()) || "NULL".equals(info.getHeadImg()) || TextUtils.isEmpty(info.getHeadImg())) {
            vip_tou.setImageResource(R.drawable.head_icon);
        } else {
            LoadImgUtils.setImgCircle(getActivity(), vip_tou, info.getHeadImg(), R.drawable.head_icon);
        }
        vip_name.setText(info.getNickName());
        // refreshUserInfo(info);
        String userType = info.getUserType();
        if (C.UserType.operator.equals(userType)) {
            img_vip.setImageResource(R.mipmap.group_bg_icon);
            vip_ll.setVisibility(View.GONE);
            group_ll.setVisibility(View.VISIBLE);
            vip_grade.setText("掌柜(黑金)");
            vip_grade.setTextColor(Color.parseColor("#222222"));
            ll_vip.setBackgroundResource(R.drawable.bg_opertoater_round_9dp);
            getMorce.setText("成长值：" + info.getMoreCoin());
        } else {
            getMorce.setText("获取成长值");
            vip_ll.setVisibility(View.VISIBLE);
            group_ll.setVisibility(View.GONE);
            if (info != null) {
                if (C.UserType.member.equals(info.getUserType())) {
                    img_vip.setImageResource(R.mipmap.vip_icon_right2);
                    vip_grade.setText("普通会员");
                    upgrade.setText("升级VIP");
                    vip_grade.setTextColor(Color.parseColor("#A8947A"));
                    ll_vip.setBackgroundResource(R.drawable.bg_e9c8a7_round_9dp);
                    horzProgressView.setMax(360.00);
                    Long coin = info.getMoreCoin();
                    String coin1;
                    if (coin != null) {
                        horzProgressView.setCurrentNum(info.getMoreCoin());
                        coin1 = info.getMoreCoin()+"";
                    } else {
                        horzProgressView.setCurrentNum(0);
                        coin1 =  "0";
                        return;
                    }
                    more_corn_biaozhun.setText(coin1);
                    tv_coin.setText("/360");
                } else if (C.UserType.vipMember.equals(info.getUserType())) {
                    img_vip.setImageResource(R.mipmap.vip_bg_icon);
                    upgrade.setText("升级掌柜(黑金)");
                    ll_vip.setBackgroundResource(R.drawable.bg_vip_round_9dp);
                    vip_grade.setText("掌柜(黄金)");
                    vip_grade.setTextColor(Color.parseColor("#FCAF00"));
                    horzProgressView.setMax(50000.00);
                    horzProgressView.setCurrentNum(info.getMoreCoin());
                    Long moreCoin = info.getMoreCoin();
                    String coin1;
                    if (moreCoin == null) {
                        coin1 ="0";
                    } else {
                        coin1 = moreCoin + "";
                    }
                    more_corn_biaozhun.setText(coin1);
                    tv_coin.setText("/50000");
                }
            }
            getMorce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    xablayout.getTabAt(0).select();
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
      initTou();

    }

    private void initTan() {
        UserInfo mUserInfo = UserLocalData.getUser(getActivity());
        if (mUserInfo == null) {
            return;
        }
        Long moreCoin = mUserInfo.getMoreCoin();
        if (moreCoin != null) {
            if (moreCoin > 360) {
                if (C.UserType.member.equals(mUserInfo.getUserType())) {
                    updateGrade();
                }

            }
            if (moreCoin > 50000) {
                if (C.UserType.vipMember.equals(mUserInfo.getUserType())) {
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



        getVipFloor(this).compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ImageInfo>>>bindToLifecycle())
                .subscribe(new DataObserver<List<ImageInfo>>() {
                    @Override
                    protected void onDataListEmpty() {
                        rl3.setVisibility(View.GONE);
                    }
                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        if (data != null) {
                            if (data.size() < 2) {
                                rl3.setVisibility(View.GONE);
                            } else {
                                rl3.setVisibility(View.VISIBLE);
                                floorAdapter = new ActivityFloorAdapter(getActivity(), data);
                                if (floorAdapter != null) {
                                    activity_rcy.setAdapter(floorAdapter);
                                }
                            }
                        } else {
                            rl3.setVisibility(View.GONE);
                        }
                    }
                });
        getData();
    }

    //获取用户详情
    private void showDetailsView(UserInfo data) {
        if (!TextUtils.isEmpty(data.getAccumulatedAmount())){
            vip_zhuan.setText("累计省赚"+data.getAccumulatedAmount()+"元");

        }

    }





    public void showSuccessful(NumberGoodsList datas) {
        List<NumberGoods> list = datas.getList();
        if (list == null || (list != null && list.size() == 0)) {
            return;
        }
        if (page == 1) {
            mAdapter = new SubNumberAdapter(getActivity());
            mReUseGridView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(list);
        }

    }


    public void showError(String errorNo, String msg) {
        MyLog.i("test", "onFailure: " + this);
        if ("B1100007".equals(errorNo)
                || "B1100008".equals(errorNo)
                || "B1100009".equals(errorNo)
                || "B1100010".equals(errorNo)) {
            ViewShowUtils.showShortToast(getActivity(), msg);
        }


    }


    public void showFinally() {
        LoadingView.dismissDialog();


    }


    public void onGradeSuccess(UpdateInfoBean info) {
        if (info != null) {
            UserInfo userInfo = UserLocalData.getUser();
            userInfo.setUserType(String.valueOf(info.getUserType()));
            userInfo.setMoreCoin(info.getMoreCoin());
            UserLocalData.setUser(getActivity(), userInfo);
            EventBus.getDefault().post(new RefreshUserInfoEvent());
//            refreshUserInfo(userInfo);
        } else {
            MyLog.d("test", "用户信息为空");
        }

    }


    /**
     * 商品列表
     *
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
                        showError(errCode, errorMsg);
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
     *
     * @param fragment
     * @param userType
     */
    public void updateGradePresenter(RxFragment fragment, int userType) {
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
                        showError(errCode, errorMsg);
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
        UserInfo mUserInfo = UserLocalData.getUser(getActivity());
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
    public Observable<BaseResponse<UpdateInfoBean>> updateUserGrade(RxFragment fragment, int userGrade) {
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
    public void onEventMainThread(RefreshUserInfoEvent event) {
        updataUser();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(NumberSubFragment.this);
    }




 /*   class SubNumberAdapter extends RecyclerView<NumberGoods, SimpleViewHolder> {

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
                LoadImgUtils.loadingCornerTop(mContext, pic, img,4);
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

    }*/
}
