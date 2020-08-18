package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
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

import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.NumberLeaderUpgradeDialog;
import com.zjzy.morebit.Module.common.Dialog.NumberVipUpgradeDialog;
import com.zjzy.morebit.Module.common.View.BaseCustomTabEntity;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.EventBusAction;
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
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopCarNumBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.GoodsHeightUpdateEvent;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.GoodsOrderInfo;
import com.zjzy.morebit.pojo.number.NumberGoodsInfo;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestAddShopcarBean;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
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



    @BindView(R.id.fl_img)
    FrameLayout fl_img;



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

    @BindView(R.id.iv_taobao)
    TextView iv_taobao;

    private boolean isAdd;


    /**
     * 会员商品Id
     */
    private String mGoodsId;

    private GoodSizePopupwindow sizePopWin;

    private ImageView goodsPicView;

    private TextView selectedGoodsPrice;

    private TextView txtGoodsRule;

    private TextView txtGoodsName,shopnum;
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
    private TextView txt_head_title;
    private LinearLayout btn_back;


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
    protected void onResume() {
        super.onResume();
        getShopCarNum();
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

        EventBus.getDefault().register(this);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
              //解决状态栏和布局重叠问题，任选其一
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
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
        btn_back= (LinearLayout) findViewById(R.id.btn_back);
        txt_head_title= (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("商品详情");
        shopnum= (TextView) findViewById(R.id.shopnum);



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        go_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollViewToLocation(0);
            }
        });

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

            }

            private void scrollTabChange(int scrollY) {

            }
        });
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
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
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




    @OnClick({R.id.bottomLy,R.id.btn_goods_buy_action,R.id.ll_home,R.id.goods_car,R.id.shop_car})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomLy:
                isAdd=false;
                showPopupwindow();
                break;
            case R.id.btn_goods_buy_action:
                break;
            case R.id.ll_home://跳转首页
                ActivityLifeHelper.getInstance().finishActivity(MainActivity.class);
                EventBus.getDefault().post(new MessageEvent(EventBusAction.ACTION_HOME));
                break;
            case R.id.goods_car://添加购物车
                isAdd=true;
                showPopupwindow();
                break;
            case R.id.shop_car://进入购物车
                startActivity(new Intent(this,ShopCarActivity.class));
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
        goodsPrice.setText(MathUtils.getnum(priceStr));

        if (!StringsUtils.isEmpty(goodsInfo.getName())) {
            Paint paint = new Paint();
            paint.setTextSize(iv_taobao.getTextSize());
            float size = paint.measureText(iv_taobao.getText().toString());
            StringsUtils.retractTitles(numberGoodsTitle, goodsInfo.getName(), (int) (size) + 35);
        }
      //  numberGoodsTitle.setText(goodsInfo.getName());

      //  tvGiveGrowthValue.setText(getResources().getString(R.string.give_growth_value,MathUtils.getnum(priceStr)));
        btnBuy.setText(getResources().getString(R.string.number_goods_buy_txt,MathUtils.getnum(priceStr)));
    }

    @Override
    public void onError() {
        MyLog.e(TAG,"获取商品详情失败");

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
                if (isAdd){
                    getShopGoods(mGoodsId,mGoodsInfo.getProductId(),addCartNumTv.getText().toString());
                }else{
                    createGoodsOrderObj();
                    ConfirmOrderActivity.start(NumberGoodsDetailsActivity.this,mGoodsOrderInfo);
                }
                sizePopWin.dismiss();


            }
        });
     ImageView   pop_diss=contentView.findViewById(R.id.pop_diss);
        pop_diss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sizePopWin.dismiss();
            }
        });

        txtGoodsName.setText(mGoodsInfo.getName());
        txtGoodsRule.setText(mGoodsInfo.getUnit());
        selectedGoodsPrice.setText(" "+MathUtils.getnum(String.valueOf(mGoodsInfo.getRetailPrice())));

        LoadImgUtils.setImg(NumberGoodsDetailsActivity.this, goodsPicView, mGoodsInfo.getBuyPicUrl());


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
    private void getShopCarNum() {

        shopCarNum(this)
                .subscribe(new DataObserver<ShopCarNumBean>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(ShopCarNumBean data) {
                        int goodsNum = data.getGoodsNum();
                        if (goodsNum!=0){
                            shopnum.setVisibility(View.VISIBLE);
                            if (goodsNum>99){
                                shopnum.setText("99+");
                            }else{
                                shopnum.setText(goodsNum+"");
                            }

                        }else{
                            shopnum.setVisibility(View.GONE);
                        }

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

    private void getShopGoods( String  goodsId,String productId,String number) {
        addShopCar(this, goodsId,productId,number)
                .subscribe(new DataObserver<String>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure(errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.showShort("加入成功");
                        getShopCarNum();
                    }
                });
    }

    private void onActivityFailure(String errorMsg) {
        ToastUtils.showShort(""+errorMsg);
    }

    private void onActivityFailure() {

    }


    //添加购物车
    public Observable<BaseResponse<String>> addShopCar(RxAppCompatActivity fragment, String  goodsId,String productId,String number) {
        RequestAddShopcarBean requestAddShopcarBean=new RequestAddShopcarBean();
        requestAddShopcarBean.setGoodsId(goodsId);
        requestAddShopcarBean.setProductId(productId);
        requestAddShopcarBean.setNumber(number);
        return RxHttp.getInstance().getSysteService().getAddShopCar(requestAddShopcarBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(fragment.<BaseResponse<String>>bindToLifecycle());
    }

    //购物车数量
    public Observable<BaseResponse<ShopCarNumBean>> shopCarNum(RxAppCompatActivity fragment) {

        return RxHttp.getInstance().getSysteService().getShopCarNum()
                .compose(RxUtils.<BaseResponse<ShopCarNumBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<ShopCarNumBean>>bindToLifecycle());
    }


}
