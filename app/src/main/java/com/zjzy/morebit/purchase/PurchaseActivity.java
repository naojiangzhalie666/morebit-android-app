package com.zjzy.morebit.purchase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.wireless.security.open.middletier.fc.IFCActionCallback;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.PurchaseRuleDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.CircleDayHotFragment;
import com.zjzy.morebit.goods.shopping.ui.ShareMoneyGetImgActivity;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShareUrlBaen;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.goods.ShareUrlListBaen;
import com.zjzy.morebit.purchase.adapter.ProductAdapter;
import com.zjzy.morebit.purchase.adapter.PurchseAdapter;
import com.zjzy.morebit.purchase.control.PurchaseControl;
import com.zjzy.morebit.purchase.presenter.PurchasePresenter;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.ImageUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.QRCodeGenerater;
import com.zjzy.morebit.utils.QrcodeUtils;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.share.ShareMore;
import com.zjzy.morebit.view.CommercialShareDialog;
import com.zjzy.morebit.view.FixRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.zjzy.morebit.utils.C.requestType.initData;

/*
 *
 * 0元购
 * */
public class PurchaseActivity extends MvpActivity<PurchasePresenter> implements PurchaseControl.View, View.OnClickListener {


    private TextView txt_head_title, tv_rule;
    private RecyclerView rl_list, rcy_product;
    private PurchseAdapter adapter;
    private ProductAdapter padapter;
    private ImageView share;
    private LinearLayout btn_back;
    CommercialShareDialog shareDialog;
    private List<ShopGoodInfo> data, mdata;
    private NestedScrollView nscorll;
    private Bitmap mEwmBitmap;
    private String imgpath;
    private String ischeck;
    private LinearLayout linear_more;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_purchase);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();
        isCheckNew();

        initView();
        initData();

    }

    private void isCheckNew() {
        RxHttp.getInstance().getCommonService().checkPruchase()
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {

                            ischeck= data;
                    }
                });
    }



    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_purchase;
    }

    private void initData() {
        txt_head_title.setText("新人0元购");
        mPresenter.getPurchase(PurchaseActivity.this, 1);//免单
        mPresenter.getProduct(PurchaseActivity.this);//好货

    }

    private void initView() {

        txt_head_title = (TextView) findViewById(R.id.txt_head_title);

        rl_list = (RecyclerView) findViewById(R.id.rl_list);
        rl_list.setNestedScrollingEnabled(false);
        rl_list.setFocusable(false);

        rcy_product = (RecyclerView) findViewById(R.id.rcy_product);

        rcy_product.setNestedScrollingEnabled(false);
        rcy_product.setFocusable(false);


        share = (ImageView) findViewById(R.id.share);//分享
        share.setOnClickListener(this);

        tv_rule = (TextView) findViewById(R.id.tv_rule);//活动规则
        tv_rule.setOnClickListener(this);

        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        nscorll = (NestedScrollView) findViewById(R.id.nscorll);

        linear_more= (LinearLayout) findViewById(R.id.linear_more);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                mShare();
                break;

            case R.id.tv_rule:
                String purchaseRule = SPUtils.getInstance().getString("purchaseRule");
                final PurchaseRuleDialog purchaseRuleDialog = new PurchaseRuleDialog(this,purchaseRule);//规则h5链接
                purchaseRuleDialog.setmCancelListener(new PurchaseRuleDialog.OnCancelListner() {
                    @Override
                    public void onClick(View view) {
                        purchaseRuleDialog.dismiss();
                    }
                });
                purchaseRuleDialog.show();
                break;
            case R.id.btn_back:
                finish();
                break;

        }
    }


    public void mShare() {//分享
        if (!LoginUtil.checkIsLogin((Activity) this)) {
            return;
        }
        if (TaobaoUtil.isAuth()) {//淘宝授权
            TaobaoUtil.getAllianceAppKey((BaseActivity) this);
            return;
        }
        shareDialog = new CommercialShareDialog(this, new View.OnClickListener() {//分享
            @Override
            public void onClick(View v) {
                if (imgpath != null) {
                    switch (v.getId()) {
                        case R.id.weixinFriend: //分享到好友
                            // mshare(item, type, ShareUtil.WechatType);
                            ShareUtil.Image.toWechatFriend(PurchaseActivity.this, imgpath, null);
                            break;
                        case R.id.weixinCircle: //分享到朋友圈
                            //  mshare(item, type, ShareUtil.WeMomentsType);
                            ShareUtil.Image.toWechatMoments(PurchaseActivity.this, imgpath, null);
                            break;
                        case R.id.qqFriend: //分享到QQ
                            // mshare(item, type, ShareUtil.QQType);

                            ShareUtil.Image.toQQFriend(PurchaseActivity.this, imgpath, null);
                            break;
                        case R.id.qqRoom: //分享到QQ空间
                            // mshare(item, type, ShareUtil.QQZoneType);
                            ShareUtil.Image.toQQRoom(PurchaseActivity.this, imgpath, null);
                            break;
                        case R.id.sinaWeibo: //分享到新浪微博
                            // mshare(item, type, ShareUtil.WeiboType);
                            ShareUtil.Image.toSinaWeibo(PurchaseActivity.this, imgpath, null);
                            break;
                        default:
                            break;

                    }
                }
                shareDialog.dismiss();
            }
        });

        if (!shareDialog.isShowing()) {
            shareDialog.show();
        }
    }


    @Override
    public void onSuccess(final List<ShopGoodInfo> shopGoodInfo) {

        try {
            data = new ArrayList<>();
            if (shopGoodInfo.size()>7){
                for (int i=0;i<8;i++){
                    data.add(shopGoodInfo.get(i));
                    linear_more.setVisibility(View.VISIBLE);
                }
                linear_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.clear();
                        linear_more.setVisibility(View.GONE);
                        data.addAll(shopGoodInfo);
                        adapter.notifyDataSetChanged();
                    }
                });
            }else{
                linear_more.setVisibility(View.GONE);
                data.addAll(shopGoodInfo);
            }

            adapter = new PurchseAdapter(this, data,ischeck);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            rl_list.setLayoutManager(manager);
            rl_list.setAdapter(adapter);

            imgpath = GoodsUtil.savePruchaseGoodsImg(this, shopGoodInfo);

            adapter.setOnAddClickListener(new PurchseAdapter.OnAddClickListener() {
                @Override
                public void onShareClick() {
                    mShare();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(String throwable) {

    }

    @Override
    public void onProductSuccess(List<ShopGoodInfo> shopGoodInfo) {
        mdata = new ArrayList<>();
        mdata.addAll(shopGoodInfo);

        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        padapter = new ProductAdapter(this, mdata, ischeck);
        rcy_product.setLayoutManager(manager2);
        rcy_product.setAdapter(padapter);

        padapter.setOnAddClickListener(new ProductAdapter.OnAddClickListener() {
            @Override
            public void onItemClick() {
                nscorll.scrollTo(0, 0);//滑到顶部
            }

            @Override
            public void onShareItemClick() {
                mShare();
            }
        });

    }

    @Override
    public void onProductError(String throwable) {

    }


}
