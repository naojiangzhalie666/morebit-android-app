package com.zjzy.morebit.goodsvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.adapter.OnViewPagerListener;
import com.zjzy.morebit.goodsvideo.adapter.PagerLayoutManager;
import com.zjzy.morebit.goodsvideo.adapter.VideoDouAdapter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.pojo.request.RequestVideoGoodsBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestItemSourceId;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/*
 * 视频播放页
 *
 * */
public class VideoActivity extends BaseActivity implements View.OnClickListener {

    private Bundle bundle;
    private VideoDouAdapter douAdapter;
    private RecyclerView rcy_video;
    private List<ShopGoodInfo> data = new ArrayList<>();
    private VideoView mVideoView;
    private int videoId,page;
    private int mCurPos;
    private ImageView closs, first_img,img_stop;
    private int currentPosition;
    private  PagerLayoutManager mLayoutManager;
    private String cid;
    private TextView tv_title, tv_price, tv_subsidy, tv_num, tv_coupon_price, tv_buy, tv_share;
    private VideoView videoView;
    private ImageView iv_head, img_share,img_collect,img_xia;
    private TKLBean mTKLBean;
    List<ImageInfo> indexbannerdataArray = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initBundle();
        initView();
        initData();

    }


    private void initData() {


    }

    private void initView() {
        ActivityStyleUtil.initSystemBar(VideoActivity.this, R.color.black); //设置标题栏颜色值
        rcy_video = (RecyclerView) findViewById(R.id.rcy_video);
        closs = (ImageView) findViewById(R.id.closs);
        closs.setOnClickListener(this);
        iv_head = (ImageView) findViewById(R.id.iv_head);//主图
        tv_title = (TextView)findViewById(R.id.tv_title);//标题
        tv_price = (TextView)findViewById(R.id.tv_price);//优惠券
        tv_subsidy = (TextView)findViewById(R.id.tv_subsidy);//预估收益
        tv_num = (TextView)findViewById(R.id.tv_num);//销量
        tv_coupon_price = (TextView)findViewById(R.id.tv_coupon_price);//商品价格
        tv_buy = (TextView)findViewById(R.id.tv_buy);//立即购买
        tv_share = (TextView)findViewById(R.id.tv_share);//分享
        douAdapter = new VideoDouAdapter(this, data);
          mLayoutManager = new PagerLayoutManager(this, OrientationHelper.VERTICAL);
        rcy_video.setLayoutManager(mLayoutManager);
        rcy_video.setAdapter(douAdapter);
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete(View view) {
                playVideo(videoId, view);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom, View view,Context context) {
                if (mCurPos == position) return;
                playVideo(position, view);
                if (isBottom){
                    page++;
                    getVideoGoods((BaseActivity) context,cid,page)
                            .doFinally(new Action() {
                                @Override
                                public void run() throws Exception {

                                }
                            }).subscribe(new DataObserver<List<ShopGoodInfo>>() {
                        @Override
                        protected void onDataListEmpty() {
                            onVideoGoodsError();
                        }

                        @Override
                        protected void onSuccess(List<ShopGoodInfo> data) {
                            onVideoGoodsSuccess(data);
                        }
                    });
                }
            }

            @Override
            public void onPageRelease(boolean isNext, int position, View view) {
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(view);
            }
        });

        rcy_video.scrollToPosition(videoId);
        goodsDetais(videoId);

    }

    private void onVideoGoodsSuccess(List<ShopGoodInfo> mdata) {
        data.addAll(mdata);
        douAdapter.notifyDataSetChanged();

    }

    private void onVideoGoodsError() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closs:
                finish();
                break;


        }
    }


    /**
     * 播放视频
     */
    private void playVideo(int position, View view) {
        if (view != null) {
            mVideoView = view.findViewById(R.id.videoView);
            first_img = view.findViewById(R.id.first_img);
            img_stop=view.findViewById(R.id.img_stop);
            img_stop.setVisibility(View.GONE);
            Uri uri = Uri.parse(data.get(position).getItemVideo());
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            mVideoView.start();
            mCurPos = position;
           goodsDetais(position);
        }
    }

    private void goodsDetais(int position) {
        final ShopGoodInfo mGoodsInfo=data.get(position);
        mGoodsInfo.setItemSourceId(mGoodsInfo.getItemId());
        mGoodsInfo.setItemTitle(mGoodsInfo.getItemTitle());
        mGoodsInfo.setItemDesc(mGoodsInfo.getItemDesc());
        mGoodsInfo.setItemPicture(mGoodsInfo.getItemPic());
        mGoodsInfo.setItemPrice(String.valueOf(MathUtils.sum(Double.valueOf(mGoodsInfo.getItemPrice()),Double.valueOf(mGoodsInfo.getCouponMoney()))));
        mGoodsInfo.setCouponPrice(mGoodsInfo.getCouponMoney());
        mGoodsInfo.setItemVoucherPrice(mGoodsInfo.getItemPrice());
        mGoodsInfo.setSaleMonth(TextUtils.isEmpty(mGoodsInfo.getSaleMonth()) ? "0" : mGoodsInfo.getSaleMonth());
        mGoodsInfo.setCouponUrl(mGoodsInfo.getCouponUrl());
        mGoodsInfo.setCommission(mGoodsInfo.getTkMoney());
        Log.e("ko",mGoodsInfo.getItemPrice()+"");
        Log.e("ko",mGoodsInfo.getPrice()+"");
        Log.e("ko",mGoodsInfo.getItemVoucherPrice()+"");

        getBaseResponseObservable(this, mGoodsInfo)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new DataObserver<ShopGoodInfo>() {
                    @Override
                    protected void onSuccess(final ShopGoodInfo data) {
                        List<String> getBanner = data.getItemBanner();
                        indexbannerdataArray.clear();
                        for (int i = 0; i < getBanner.size(); i++) {
                            String s = StringsUtils.checkHttp(getBanner.get(i));
                            if (TextUtils.isEmpty(s)) return;
                            if (LoadImgUtils.isPicture(s)) {
                                ImageInfo imageInfo = new ImageInfo();
                                imageInfo.setThumb(getBanner.get(i));
                                indexbannerdataArray.add(imageInfo);
                            }
                        }
                        mGoodsInfo.setAdImgUrl(indexbannerdataArray);

                    }
                });


        LoadImgUtils.loadingCornerBitmap(this,iv_head, mGoodsInfo.getItemPic());
        tv_title.setText(mGoodsInfo.getItemTitle());
        tv_price.setText(mGoodsInfo.getCouponMoney() + "元劵");
        tv_num.setText("销量：" + mGoodsInfo.getItemSale());
        String itemPrice = mGoodsInfo.getItemPrice();
        tv_coupon_price.setText(mGoodsInfo.getItemVoucherPrice() + "");
        UserInfo userInfo1 = UserLocalData.getUser();
        if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
            tv_subsidy.setText("登录赚佣金");
        } else {
            if (C.UserType.operator.equals(UserLocalData.getUser(this).getPartner())
                    || C.UserType.vipMember.equals(UserLocalData.getUser(this).getPartner())) {
                tv_subsidy.setText("预估收益" + MathUtils.getMuRatioComPrice(UserLocalData.getUser(this).getCalculationRate(), mGoodsInfo.getTkMoney() + "") + "元");
            }
        }

        tv_buy.setOnClickListener(new View.OnClickListener() {//购买
            @Override
            public void onClick(View v) {
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey(VideoActivity.this);
                } else {
                    if (isGoodsLose(mGoodsInfo)) return;

                    if (mTKLBean == null) {
                        LoadingView.showDialog(VideoActivity.this, "");
                        GoodsUtil.getTaoKouLing( VideoActivity.this, mGoodsInfo, new MyAction.OnResult<TKLBean>() {
                            @Override
                            public void invoke(TKLBean arg) {
                                mTKLBean = arg;
                            }

                            @Override
                            public void onError() {
                            }
                        });
                    } else {
                        ShareMoneyActivity.start(VideoActivity.this, mGoodsInfo, mTKLBean);
                    }
                }
            }
        });

        tv_share.setOnClickListener(new View.OnClickListener() {//分享
            @Override
            public void onClick(View v) {
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey( VideoActivity.this);
                } else {
                    GoodsUtil.getCouponInfo(VideoActivity.this, mGoodsInfo);
                }

            }
        });



    }

    /**
     * 停止播放
     */
    private void releaseVideo(View view) {
        if (view != null) {
            VideoView videoView = view.findViewById(R.id.videoView);
            videoView.stopPlayback();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.seekTo(currentPosition);
            mVideoView.start();


        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
            currentPosition = mVideoView.getCurrentPosition();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }
    }

    public static void start(Context context, List<ShopGoodInfo> info, int videoId,String cid,int page) {
        Intent intent = new Intent((Activity) context, VideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.Extras.GOODSBEAN, (Serializable) info);
        bundle.putSerializable(C.Extras.VIDEOBEAN, videoId);
        bundle.putSerializable(C.Extras.PAGEBEAN, page);
        bundle.putSerializable(C.Extras.CIDBEAN, cid);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            data = (List<ShopGoodInfo>) bundle.getSerializable(C.Extras.GOODSBEAN);
            videoId = (int) bundle.getSerializable(C.Extras.VIDEOBEAN);
            page = (int) bundle.getSerializable(C.Extras.PAGEBEAN);
            cid = (String) bundle.getSerializable(C.Extras.CIDBEAN);
        }
    }

    /**
     * 获取抖货商品列表
     *
     * @param rxActivity
     *
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getVideoGoods(BaseActivity rxActivity, String catId, int page) {
        RequestVideoGoodsBean requestBean = new RequestVideoGoodsBean();
        requestBean.setCatId(catId);
        requestBean.setPage(page);

        return RxHttp.getInstance().getUsersService()
                .getVideoGoods(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 商品是否过期
     *
     * @return
     * @param mGoodsInfo
     */
    private boolean isGoodsLose(ShopGoodInfo mGoodsInfo) {
        if (!LoginUtil.checkIsLogin(this)) {
            return true;
        }
        if (AppUtil.isFastClick(200)) {
            return true;
        }
        if (mGoodsInfo == null) {
            return true;
        }
        if (TextUtils.isEmpty(mGoodsInfo.getPrice())) {
            ViewShowUtils.showLongToast(this, "商品已经过期，请联系管理员哦");
            return true;
        }
        return false;
    }

    //淘宝详情
    private Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservable(BaseActivity rxActivity, ShopGoodInfo goodsInfo) {
        return RxHttp.getInstance().getCommonService().getDetailData(new RequestItemSourceId().setItemSourceId(goodsInfo.getItemSourceId()).setItemFrom(goodsInfo.getItemFrom()))
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }
}
