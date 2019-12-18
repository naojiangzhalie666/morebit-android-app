package com.zjzy.morebit.goods.shopping.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sendtion.xrichtext.RichTextView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zjzy.morebit.Activity.ChannelWebActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.address.ui.AddModifyAddressActivity;
import com.zjzy.morebit.goods.shopping.contract.NumberGoodsDetailContract;
import com.zjzy.morebit.goods.shopping.presenter.NumberGoodsDetailPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.order.ui.ConfirmOrderActivity;
import com.zjzy.morebit.pojo.number.GoodsOrderInfo;
import com.zjzy.morebit.pojo.number.NumberGoodsInfo;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.StringUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.view.AspectRatioView;
import com.zjzy.morebit.view.goods.GoodSizePopupwindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 会员商品详情
 * Created by haiping.liu on 2019-12-10.
 */
public class NumberGoodsDetailActivity  extends MvpActivity<NumberGoodsDetailPresenter> implements View.OnClickListener, NumberGoodsDetailContract.View  {
    private static final String TAG = NumberGoodsDetailActivity.class.getSimpleName();

    @BindView(R.id.as_banner)
    AspectRatioView mAsBanner;

    @BindView(R.id.banner)
    Banner mBanner;

    @BindView(R.id.number_goods_price)
    TextView goodsPrice;

    @BindView(R.id.number_goods_corns)
    TextView morebitCorn;

    @BindView(R.id.btn_goods_buy_action)
    TextView btnBuy;

    @BindView(R.id.room_view)
    View room_view;

//    @BindView(R.id.number_goods_detail_content)
//            TextView txtDetailContent;

    TextView addCartNumTv;
    /**
     * 商品详情
     */
    @BindView(R.id.number_goods_detail_content)
    RichTextView detailContent;
    /**
     * 商品轮播图
     */
    ArrayList<String> imgs = new ArrayList<String>();
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

    private ProgressDialog loadingDialog;

    private Disposable mDisposable;

    public static void start(Activity activity, String goodsId) {
        if (TextUtils.isEmpty(goodsId)) {
            return;
        }
        //跳转到网页
        Intent it = new Intent(activity, NumberGoodsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", goodsId);
        it.putExtras(bundle);
        activity.startActivity(it);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoodsId = getIntent().getStringExtra("id");
        initView();
        getData();
    }

    private void initView(){
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("数据加载中...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

    }
    @OnClick({R.id.btn_back,R.id.btn_goods_buy_action})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                //返回
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
    public void showSuccessful(NumberGoodsInfo goodsInfo) {
        mGoodsInfo = goodsInfo;
        imgs = goodsInfo.getGallery();

        if (imgs == null || imgs.size() == 0) {
                mAsBanner.setVisibility(View.GONE);
                return;
        }
        mAsBanner.setVisibility(View.VISIBLE);
        setBanner(imgs, mBanner, mAsBanner);
        if (mGoodsOrderInfo != null){
//            txtDetailContent.setText(Html.fromHtml(mGoodsInfo.getDetail()));
            detailContent.post(new Runnable() {
                @Override
                public void run() {
                    dealWithContent(mGoodsInfo.getDetail());
                }
            });
        }
        double price = mGoodsInfo.getRetailPrice();
        goodsPrice.setText(getResources().getString(R.string.number_goods_price,String.valueOf(price)));
        int moreCorn = (int)price*10;
        morebitCorn.setText(getResources().getString(R.string.number_give_more_corn_1,String.valueOf(moreCorn)));


    }

    @Override
    public void onError() {

    }

    private void dealWithContent(String detail){

        detailContent.clearAllLayout();
        showDataSync(detail);


    }
    /**
     * 异步方式显示数据
     */
    private void showDataSync(final String html){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                showEditData(emitter, html);
            }
        })
                //.onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        if (loadingDialog != null){
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (loadingDialog != null){
                            loadingDialog.dismiss();
                        }
//                        showToast("解析错误：图片不存在或已损坏");
                        MyLog.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(String text) {
                        try {
                            if (detailContent !=null) {
                                if (text.contains("<img") && text.contains("src=")) {
                                    //imagePath可能是本地路径，也可能是网络地址
                                    String imagePath = StringUtils.getImgSrc(text);
                                    detailContent.addImageViewAtIndex(detailContent.getLastIndex(), imagePath);
                                } else {
                                    detailContent.addTextViewAtIndex(detailContent.getLastIndex(), text);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 显示数据
     */
    private void showEditData(ObservableEmitter<String> emitter, String html) {
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                emitter.onNext(text);
            }
            emitter.onComplete();
        } catch (Exception e){
            e.printStackTrace();
            emitter.onError(e);
        }
    }

    /**
     * 设置轮播图
     *
     * @param data
     */
    private void setBanner(final List<String> data, Banner banner, AspectRatioView aspectRatioView) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (aspectRatioView != null) {
            String img = data.get(0);
            float width = 750;
            float height = 500;
            if (width != 0 && width / height != 0) {
                aspectRatioView.setAspectRatio(width / height);
            }

        }

        //简单使用
        banner.setImages(imgs)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
//                       String img = data.get(position);

//                        BannerInitiateUtils.gotoAction(NumberGoodsDetailActivity.this, img);
                    }
                })
                .isAutoPlay(true)
                .setDelayTime(4000)
                .start();
    }



    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_number_goods_detail;
    }

    private  void getData(){
        mGoodsOrderInfo = new GoodsOrderInfo();
        mGoodsOrderInfo.setCount(1);
        mPresenter.getGoodsDetail(NumberGoodsDetailActivity.this,mGoodsId);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.goodsRule_minusRelative:
                    int count = Integer.valueOf((String)addCartNumTv.getText());
                    if(count==1){
                        ViewShowUtils.showShortToast(NumberGoodsDetailActivity.this, "不能再减了哦");
                    }else{
                        count--;
                        addCartNumTv.setText((count)+"");
                        mGoodsOrderInfo.setCount(count);
                    }
                    break;
                case R.id.goodsRule_addRelative:
                    int count2 = Integer.valueOf((String)addCartNumTv.getText());

                    if (count2 + 1 > mGoodsInfo.getInventory()){
                        ViewShowUtils.showShortToast(NumberGoodsDetailActivity.this, "不能再加了。没有库存了哦");
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
                ViewShowUtils.showShortToast(NumberGoodsDetailActivity.this, "请确认商品信息");
                return;
            }
            sizePopWin = new GoodSizePopupwindow(NumberGoodsDetailActivity.this, onClickListener);
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
                    ConfirmOrderActivity.start(NumberGoodsDetailActivity.this,mGoodsOrderInfo);
                    finish();

                }
            });

            txtGoodsName.setText(mGoodsInfo.getName());
            txtGoodsRule.setText(mGoodsInfo.getUnit());
            selectedGoodsPrice.setText(String.valueOf(mGoodsInfo.getRetailPrice()));

            LoadImgUtils.setImg(NumberGoodsDetailActivity.this, goodsPicView, mGoodsInfo.getPicUrl());


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
        double totalPrice = count* mGoodsInfo.getRetailPrice();
        mGoodsOrderInfo.setPrice(String.valueOf(mGoodsInfo.getRetailPrice()));
        mGoodsOrderInfo.setPayPrice(String.valueOf(mGoodsInfo.getRetailPrice()));
        mGoodsOrderInfo.setGoodsTotalPrice(String.valueOf(totalPrice));
        mGoodsOrderInfo.setPayPrice(String.valueOf(totalPrice));
        mGoodsOrderInfo.setSpec(mGoodsInfo.getUnit());
        mGoodsOrderInfo.setTitle(mGoodsInfo.getName());
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDisposable != null && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
    }
}
