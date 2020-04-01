package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.NumberLeaderUpgradeDialog;
import com.zjzy.morebit.Module.common.Dialog.NumberVipUpgradeDialog;
import com.zjzy.morebit.Module.common.View.BaseCustomTabEntity;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goods.shopping.contract.NumberGoodsDetailContract;
import com.zjzy.morebit.goods.shopping.presenter.NumberGoodsDetailPresenter;
import com.zjzy.morebit.goods.shopping.ui.fragment.NumberGoodsDetailImgFragment;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.order.ui.ConfirmOrderActivity;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.GoodsHeightUpdateEvent;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.GoodsOrderInfo;
import com.zjzy.morebit.pojo.number.NumberGoodsInfo;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.view.goods.GoodSizePopupwindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 商品详情页
 */

public class NumberGoodsDetailsActivity extends MvpActivity<NumberGoodsDetailPresenter> implements View.OnClickListener, NumberGoodsDetailContract.View {

    private static final String TAG = NumberGoodsDetailsActivity.class.getSimpleName();

    @BindView(R.id.roll_view_pager)
    Banner mRollViewPager;


    @BindView(R.id.go_top)
    ImageView go_top;

    @BindView(R.id.nsv_view)
    NestedScrollView nsv_view;
    @BindView(R.id.srl_view)
    SwipeRefreshLayout srl_view;
    @BindView(R.id.view_bar)
    View view_bar;

    @BindView(R.id.fl_img)
    FrameLayout fl_img;
    @BindView(R.id.tablayout)
    CommonTabLayout tablayout;

    @BindView(R.id.re_tab)
    RelativeLayout re_tab;
    @BindView(R.id.search_statusbar_rl)
    LinearLayout search_statusbar_rl;

    @BindView(R.id.number_goods_price)
    TextView goodsPrice;


    @BindView(R.id.btn_goods_buy_action)
    TextView btnBuy;


    TextView addCartNumTv;

    @BindView(R.id.room_view)
    View room_view;

    @BindView(R.id.number_goods_title)
    TextView numberGoodsTitle;

    @BindView(R.id.tv_give_growth_value)
    TextView tvGiveGrowthValue;

    /**
     * 会员商品Id
     */
    private String mGoodsId;

    private GoodSizePopupwindow sizePopWin;

    private ImageView goodsPicView;

    private TextView selectedGoodsPrice;

    private TextView txtGoodsRule;

    private TextView txtGoodsName;
    private TextView txtGoodsAction;

    private NumberGoodsInfo mGoodsInfo;

    private GoodsOrderInfo mGoodsOrderInfo;


    private Bundle bundle;
    private NumberGoodsDetailImgFragment mDetailImgFragment;
    private int mWidth;
    private List<String> mBannerList = new ArrayList<>();


    private int mConsumerPadding;

    private float duration = 855.0f;//在0-855.0之间去改变头部的透明度
    private int mListHeight;
    private Handler mHandler;
    private int mTitleHeight;


    private String[] mTitles;
    ArrayList mTabArrayList = new ArrayList<BaseCustomTabEntity>();


    public static void start(Activity activity, String goodsId) {
        if (TextUtils.isEmpty(goodsId)) {
            return;
        }
        //跳转到商品详情
        Intent it = new Intent(activity, NumberGoodsDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", goodsId);
        it.putExtras(bundle);
        activity.startActivity(it);
    }



    @Override
    protected int getViewLayout() {
        return R.layout.activity_number_goodsdetail2;
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(GoodsHeightUpdateEvent event) {
        getViewLocationOnScreen();
    }

    private void getViewLocationOnScreen() {

        search_statusbar_rl.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        search_statusbar_rl.getViewTreeObserver().removeOnPreDrawListener(this);
                        int i = search_statusbar_rl.getHeight() - mTitleHeight;
                        mListHeight = i; // 获取高度
                        return true;
                    }
                });

        MyLog.d("setOnScrollChangeListener  ", "mListHeight " + mListHeight);
    }

    private void initData(boolean isRefresh) {

        mGoodsOrderInfo = new GoodsOrderInfo();
        mGoodsOrderInfo.setCount(1);
        mPresenter.getGoodsDetail(NumberGoodsDetailsActivity.this,mGoodsId);

    }
    /**
     * 设置升级佣金
     */
    private void setUPdateData() {
        if (TextUtils.isEmpty(C.SysConfig.SELF_COMMISSION_PERCENT_VALUE)) {
            mPresenter.getSysSelfCommissionPercent(this);
        } else {
            setUpdateView(C.SysConfig.SELF_COMMISSION_PERCENT_VALUE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoodsId = getIntent().getStringExtra("id");
        mTitles = new String[]{getString(R.string.goods_detail_baby),  getString(R.string.goods_detail_det)};
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.margin_small);
        mConsumerPadding = getResources().getDimensionPixelSize(R.dimen.goods_consumer_itme_padding);
        mTitleHeight = getResources().getDimensionPixelSize(R.dimen.goods_detail_title_height);
        mWidth = AppUtil.getTaobaoIconWidth() + dimensionPixelSize;
        duration = getWindowManager().getDefaultDisplay().getWidth() - mTitleHeight + 0.0F;
        EventBus.getDefault().register(this);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .titleBar(view_bar)    //解决状态栏和布局重叠问题，任选其一
                .init();
        initBundle();
        initView();
        initImgFragment();
        initData(false);

        mHandler = new Handler();

    }


    private void initImgFragment() {

        mDetailImgFragment = NumberGoodsDetailImgFragment.newInstance();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(), mDetailImgFragment, R.id.fl_img);

    }

    private void initBundle() {
        bundle = getIntent().getExtras();
    }

    private void initView() {

        initTab();

        srl_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true);
            }
        });

        go_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollViewToLocation(0);
            }
        });
        re_tab.setAlpha(0);
        view_bar.setAlpha(0);
        nsv_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollTitleChange(scrollY);
                scrollTabChange(scrollY);
                if (scrollY > 1000) {
                    go_top.setVisibility(View.VISIBLE);
                } else {
                    go_top.setVisibility(View.GONE);
                }
            }


            private void scrollTitleChange(int scrollY) {
                float alpha = (float) (scrollY / duration);
                if (duration > scrollY) {
                    if (scrollY < 5) {
                        getViewLocationOnScreen();
                        re_tab.setVisibility(View.GONE);
                        re_tab.setAlpha(0);
                        view_bar.setAlpha(0);
                        re_tab.setBackgroundColor(ContextCompat.getColor(NumberGoodsDetailsActivity.this, R.color.white));
                        view_bar.setBackgroundColor(ContextCompat.getColor(NumberGoodsDetailsActivity.this, R.color.white));
                    } else {
                        if (re_tab.getVisibility() == View.GONE) {
                            re_tab.setVisibility(View.VISIBLE);
                        }
                        re_tab.setAlpha(alpha);
                        view_bar.setAlpha(alpha);
                        isTitleBarSetBg = false;
                    }
                } else {

                    if (re_tab.getVisibility() == View.GONE) {
                        re_tab.setVisibility(View.VISIBLE);
                    }
                    if (!isTitleBarSetBg) {
                        re_tab.setAlpha(1);
                        view_bar.setAlpha(1);
                        re_tab.setBackgroundColor(ContextCompat.getColor(NumberGoodsDetailsActivity.this, R.color.white));
                        view_bar.setBackgroundColor(ContextCompat.getColor(NumberGoodsDetailsActivity.this, R.color.white));
                    }
                }
            }

            private void scrollTabChange(int scrollY) {


                MyLog.d("setOnScrollChangeListener  ", "mListHeight " + mListHeight);
                if (tablayout == null || mListHeight == 0 ||  isContinueScrollTabChange) {
                    return;
                }

                int currentTab = tablayout.getCurrentTab();
                if (scrollY <= mListHeight) {
                    if (currentTab != 0)
                        tablayout.setCurrentTab(0);
                } else if (scrollY > mListHeight ) {
                    if (currentTab != 1)
                        tablayout.setCurrentTab(1);
                }
            }
        });
    }

    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabArrayList.add(new BaseCustomTabEntity(mTitles[i], 0, 0));
        }
        tablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                isContinueScrollTabChange = true;
                switch (position) {
                    case 0:
                        scrollViewToLocation(0);
                        break;
                    case 1:
                        scrollViewToLocation(mListHeight);
                        break;

                    default:
                        break;
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isContinueScrollTabChange = false;
                    }
                }, 500);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        tablayout.setTabData(mTabArrayList);
    }

    private void scrollViewToLocation(int location) {
        nsv_view.fling(location);
        nsv_view.smoothScrollTo(location, location);
    }

    private boolean isTitleBarSetBg = true;
    private boolean isContinueScrollTabChange = false;





    /**
     * 设置商品轮播图
     *
     * @param
     */

    private void setGoodsAdImg() {

            //简单使用
            mRollViewPager.setImages(mBannerList)
                    .setBannerStyle(BannerConfig.NUM_INDICATOR)
                    .setImageLoader(new GlideImageLoader())
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
//                            Intent intent = new Intent(NumberGoodsDetailsActivity.this, ImagePagerActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) mBannerList);
//                            bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
//                            bundle.putString(ImagePagerActivity.TAOBAO_ID, mGoodsInfo.getItemSourceId());
//                            intent.putExtras(bundle);
//                            startActivity(intent);
                        }
                    })
                    .isAutoPlay(true)
                    .setDelayTime(4000)
                    .start();
//        }
    }




    @OnClick({R.id.btn_back,  R.id.bottomLy,   R.id.btn_tltle_back,R.id.btn_goods_buy_action})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomLy:
                break;
            case R.id.btn_back:
            case R.id.btn_tltle_back:
                finish();
                break;
            case R.id.btn_goods_buy_action:
                showPopupwindow();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }




    /**
     * 确定IView类型
     *
     * @return
     */
    @Override
    public BaseView getBaseView() {
        return this;
    }




    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }


    @Override
    public void showSuccessful(NumberGoodsInfo goodsInfo) {

        mGoodsInfo = goodsInfo;
        setUPdateData();
        mBannerList = goodsInfo.getGallery();

        if (mBannerList == null || mBannerList.size() == 0) {
            mRollViewPager.setVisibility(View.GONE);
            return;
        }
        mRollViewPager.setVisibility(View.VISIBLE);
        setGoodsAdImg();

        if (mGoodsOrderInfo != null){
            if (mDetailImgFragment != null) {
                mDetailImgFragment.loadHtmlData(mGoodsInfo.getDetail());
            }
        }
        double price = mGoodsInfo.getRetailPrice();
        String priceStr = String.valueOf(price);
        goodsPrice.setText(priceStr);
        numberGoodsTitle.setText(goodsInfo.getName());
        srl_view.setRefreshing(false);
        tvGiveGrowthValue.setText(getResources().getString(R.string.give_growth_value,priceStr));
        btnBuy.setText(getResources().getString(R.string.number_goods_buy_txt,priceStr));
    }

    @Override
    public void onError() {
        MyLog.e(TAG,"获取商品详情失败");
        srl_view.setRefreshing(false);
    }

    @Override
    public void setUpdateView(String sysValue) {
        String[] split = null;
        if (!TextUtils.isEmpty(sysValue)) {
            split = sysValue.split(",");
            if (split.length < 3) return;
        }
        UserInfo user = UserLocalData.getUser();
        String price = String.valueOf(mGoodsInfo.getRetailPrice());
        int integer;
        if (C.UserType.vipMember.equals(user.getPartner())) {
            integer = Integer.valueOf(C.UserType.operator);
        }else {
            integer = Integer.valueOf(C.UserType.vipMember);
        }
        String s = split[integer];
        String muRatioComPrice = MathUtils.getMuRatioComPrice(s, price);
//        tvNumberGoodsHint.setText(getResources().getString(R.string.update_vip2_earnings,muRatioComPrice));

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.goodsRule_minusRelative:
                    int count = Integer.valueOf((String)addCartNumTv.getText());
                    if(count==1){
                        ViewShowUtils.showShortToast(NumberGoodsDetailsActivity.this, "不能再减了哦");
                    }else{
                        count--;
                        addCartNumTv.setText((count)+"");
                        mGoodsOrderInfo.setCount(count);
                    }
                    break;
                case R.id.goodsRule_addRelative:
                    int count2 = Integer.valueOf((String)addCartNumTv.getText());

                    if (count2 + 1 > mGoodsInfo.getInventory()){
                        ViewShowUtils.showShortToast(NumberGoodsDetailsActivity.this, "不能再加了。没有库存了哦");
                    }else{
                        count2++;
                        addCartNumTv.setText(count2+"");
                        mGoodsOrderInfo.setCount(count2);
                    }

                    break;
            }
        }
    };
    private void showPopupwindow() {
        if (mGoodsInfo == null ){
            ViewShowUtils.showShortToast(NumberGoodsDetailsActivity.this, "请确认商品信息");
            return;
        }
        sizePopWin = new GoodSizePopupwindow(NumberGoodsDetailsActivity.this, onClickListener);
        View contentView = sizePopWin.getContentView();
        addCartNumTv = ((TextView) contentView.findViewById(R.id.goodsRule_numTv));

        goodsPicView = ((ImageView) contentView.findViewById(R.id.number_goods_pic));
        selectedGoodsPrice =  ((TextView) contentView.findViewById(R.id.selected_goods_price));
        txtGoodsRule =  ((TextView) contentView.findViewById(R.id.txt_selected_rule_goods));
        txtGoodsName =  ((TextView) contentView.findViewById(R.id.goods_name));
        txtGoodsAction =  ((TextView) contentView.findViewById(R.id.goods_buy_action));
        txtGoodsAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGoodsOrderObj();

                sizePopWin.dismiss();
                ConfirmOrderActivity.start(NumberGoodsDetailsActivity.this,mGoodsOrderInfo);


            }
        });

        txtGoodsName.setText(mGoodsInfo.getName());
        txtGoodsRule.setText(mGoodsInfo.getUnit());
        selectedGoodsPrice.setText(String.valueOf(mGoodsInfo.getRetailPrice()));

        LoadImgUtils.setImg(NumberGoodsDetailsActivity.this, goodsPicView, mGoodsInfo.getPicUrl());


        //设置Popupwindow显示位置（从底部弹出）
        sizePopWin.showAtLocation(room_view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        //当弹出Popupwindow时，背景变半透明
        backgroundAlpha(0.4f);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        sizePopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });



    }

    private void createGoodsOrderObj(){
        mGoodsOrderInfo.setGoodsId(Integer.parseInt(mGoodsId));
        mGoodsOrderInfo.setImage(mGoodsInfo.getPicUrl());
        int count = mGoodsOrderInfo.getCount();

        String formatPrice = MathUtils.formatMoney(mGoodsInfo.getRetailPrice());

        mGoodsOrderInfo.setPrice(formatPrice);
        mGoodsOrderInfo.setPayPrice(formatPrice);

        double totalPrice = MathUtils.mul(count, mGoodsInfo.getRetailPrice());

        String formatTotalPrice = MathUtils.formatMoney(totalPrice);

        mGoodsOrderInfo.setGoodsTotalPrice(formatTotalPrice);
        mGoodsOrderInfo.setPayPrice(formatTotalPrice);
        mGoodsOrderInfo.setSpec(mGoodsInfo.getUnit());
        mGoodsOrderInfo.setTitle(mGoodsInfo.getName());
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    public void showError(String errorNo,String msg) {
        MyLog.i("test", "onFailure: " + this);
        if ("B1100007".equals(errorNo)
                ||"B1100008".equals(errorNo)
                ||"B1100009".equals(errorNo)
                || "B1100010".equals(errorNo)) {
            ViewShowUtils.showShortToast(NumberGoodsDetailsActivity.this,msg);
        }

    }

//    public void onGradeSuccess(UpdateInfoBean info) {
//        if (info != null){
//            UserInfo userInfo = UserLocalData.getUser();
//            userInfo.setUserType(String.valueOf(info.getUserType()));
//            userInfo.setMoreCoin(info.getMoreCoin());
//            UserLocalData.setUser(NumberGoodsDetailsActivity.this,userInfo);
//            if (C.UserType.vipMember.equals(info.getUserType())){
//                ViewShowUtils.showShortToast(NumberGoodsDetailsActivity.this,"Vip升级成功");
//            }else if (C.UserType.operator.equals(info.getUserType())){
//                ViewShowUtils.showShortToast(NumberGoodsDetailsActivity.this,"团队长升级成功");
//            }
//
//        }else{
//            MyLog.d("test","用户信息为空");
//        }
//
//    }


//    public Observable<BaseResponse<NumberGoodsList>> getNumberGoodsList(BaseActivity fragment, int page) {
//        RequestNumberGoodsList bean = new RequestNumberGoodsList();
//        bean.setLimit(10);
//        bean.setPage(page);
//        return RxHttp.getInstance().getGoodsService().getNumberGoodsList(bean)
//                .compose(RxUtils.<BaseResponse<NumberGoodsList>>switchSchedulers())
//                .compose(fragment.<BaseResponse<NumberGoodsList>>bindToLifecycle());
//    }
//    /**
//     * 用户等级升级
//     *
//     * @param fragment
//     * @return
//     */
//    public Observable<BaseResponse<UpdateInfoBean>> updateUserGrade(BaseActivity fragment,int userGrade) {
//        RequestUpdateUserBean updateUserBean = new RequestUpdateUserBean();
//        updateUserBean.setType(userGrade);
//        return RxHttp.getInstance().getUsersService().updateUserGrade(updateUserBean)
//                .compose(RxUtils.<BaseResponse<UpdateInfoBean>>switchSchedulers())
//                .compose(fragment.<BaseResponse<UpdateInfoBean>>bindToLifecycle());
//    }
}
