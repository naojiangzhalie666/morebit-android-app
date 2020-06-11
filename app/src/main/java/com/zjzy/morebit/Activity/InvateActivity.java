package com.zjzy.morebit.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Activity.ImagePagerActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShareUrlBaen;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.Banner;
import com.zjzy.morebit.utils.BannerLayoutLoader;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.FileUtil;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.share.ShareMore;
import com.zjzy.morebit.view.SharePopwindow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class InvateActivity extends BaseActivity implements View.OnClickListener {

    private View status_bar;
    private TextView tv_name;
    private Banner banner;
    private List<String> mUrlsList = new ArrayList<>();
    private String mInvite_code = "";
    private TextView tv_code, tv_copy;
    private LinearLayout btn_back, share_link, share_poster, share_picture;
    private String shareUrl;
    private String mImageUrl;
    private ArrayList<String> list = new ArrayList<>();
    private SharePopwindow shareUrlPopwindow;
    private SharePopwindow sharePosterPopwindow;
    private int currentItem = 0;
    private RelativeLayout room_view;
    String shareText = App.getAppContext().getString(R.string.shareapp_text);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invate);
        LoadingView.showDialog(this, "正在生成图片...");
        getQrcodeUrl();
        initView();
        initmData();
        inivEWM();

    }

    private void initmData() {
        getBanner(InvateActivity.this, C.UIShowType.Poster)
                .subscribe(new DataObserver<List<ImageInfo>>() {
                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        onSuccessful(data);
                    }
                });
    }

    private void inivEWM() {
        ShareMore.getShareAppLinkObservable(this)
                .subscribe(new DataObserver<ShareUrlBaen>() {
                    @Override
                    protected void onSuccess(final ShareUrlBaen data) {
                        shareUrl = data.getWxRegisterlink();
                        shareText = data.getSharp();
                    }
                });
    }

    private void onSuccessful(List<ImageInfo> data) {
        for (int i = 0; i < data.size(); i++) {
            mUrlsList.add(data.get(i).getPicture());
        }
        banner.setImageLoader(new BannerLayoutLoader())
                .setBannerStyle(BannerConfig.NOT_INDICATOR)
                .setOffscreenPageLimit(3)
                .setPageMargin(20)
                .setBannerAnimation(Transformer.ZoomOutSlide)
                .isAutoPlay(false)
                .setViewPageMargin((int) 100, 0, (int) 100, 0)
                .start();

        banner.update(mUrlsList);
    }

    private void getQrcodeUrl() {
        ShareMore.getShareAppLinkObservable(this)
                .subscribe(new DataObserver<ShareUrlBaen>() {
                    @Override
                    protected void onSuccess(ShareUrlBaen data) {
                        if (!TextUtils.isEmpty(data.getWxRegisterlink())) {
                            SPUtils.getInstance().put(C.sp.WXRESGINLINK, data.getWxRegisterlink());
                            LoadingView.dismissDialog();
                        }

                    }
                });

    }

    private void initView() {
        status_bar = findViewById(R.id.status_bar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        banner = (Banner) findViewById(R.id.banner);
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_copy = (TextView) findViewById(R.id.tv_copy);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        share_link = (LinearLayout) findViewById(R.id.share_link);
        share_picture = (LinearLayout) findViewById(R.id.share_picture);
        share_poster = (LinearLayout) findViewById(R.id.share_poster);
        room_view = (RelativeLayout) findViewById(R.id.room_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(this);
            status_bar.setLayoutParams(viewParams);
            status_bar.setBackgroundResource(R.color.transparent);
        }
        tv_name.getPaint().setFakeBoldText(true);
        mInvite_code = UserLocalData.getUser(this).getInviteCode();
        tv_code.setText(mInvite_code + "");
        tv_copy.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        share_poster.setOnClickListener(this);
        share_picture.setOnClickListener(this);
        share_link.setOnClickListener(this);



        banner.setNestedScrollingEnabled(false);

    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }


    /**
     * 获取分享海报列表
     *
     * @param \
     * @param back
     * @return
     */
    public Observable<BaseResponse<List<ImageInfo>>> getBanner(BaseActivity rxActivity, int back) {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setType(back);
        return RxHttp.getInstance().getSysteService()
//                .getBanner(back,2)
                .getBanner(requestBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<ImageInfo>>>bindToLifecycle());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_copy:
                AppUtil.coayTextPutNative(this, tv_code.getText().toString());
                ToastUtils.showLong("复制成功");
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.share_link:
                openShareUrlPop(getString(R.string.app_name), shareText);
                break;
            case R.id.share_picture:
                finish();
                break;
            case R.id.share_poster:
                openSharePosterPop();
                break;
        }
    }

    /**
     * 打开分享链接窗口
     */
    private void openShareUrlPop(final String title, final String content) {

        if (shareUrlPopwindow == null) {
            shareUrlPopwindow = new SharePopwindow(InvateActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.weixinFriend: //分享到好友
                            if (!AppUtil.isWeixinAvilible(InvateActivity.this)) {
                                ViewShowUtils.showShortToast(InvateActivity.this, "请先安装微信客户端");
                                return;
                            }

                            AppUtil.isCheckImgUrlTrue(InvateActivity.this, mImageUrl,
                                    new MyAction.OnResult<Integer>() {
                                        @Override
                                        public void invoke(Integer arg) {
                                            shareToWeixinFriend(true, title, content);
                                        }

                                        @Override
                                        public void onError() {
                                            shareToWeixinFriend(false, title, content);

                                        }
                                    });
                            shareUrlPopwindow.dismiss();
                            break;
                        case R.id.weixinCircle: //分享到朋友圈
                            if (!AppUtil.isWeixinAvilible(InvateActivity.this)) {
                                ViewShowUtils.showShortToast(InvateActivity.this, "请先安装微信客户端");
                                return;
                            }
                            AppUtil.isCheckImgUrlTrue(InvateActivity.this, mImageUrl,
                                    new MyAction.OnResult<Integer>() {
                                        @Override
                                        public void invoke(Integer arg) {
                                            shareToWeixinCicle(true, title, content);
                                        }

                                        @Override
                                        public void onError() {
                                            shareToWeixinCicle(false, title, content);
                                        }
                                    });
                            shareUrlPopwindow.dismiss();
                            break;
                        case R.id.qqFriend: //分享到QQ
                            if (!AppUtil.isQQClientAvailable(InvateActivity.this)) {
                                ViewShowUtils.showShortToast(InvateActivity.this, "请先安装QQ客户端");
                                return;
                            }


                            ShareUtil.App.toQQFriend(InvateActivity.this, title, content, mImageUrl, shareUrl, null);
                            shareUrlPopwindow.dismiss();
                            break;
                        case R.id.qqRoom: //分享到QQ空间
                            if (!AppUtil.isQQClientAvailable(InvateActivity.this)) {
                                ViewShowUtils.showShortToast(InvateActivity.this, "请先安装QQ客户端");
                                return;
                            }

                            //获取商品id列表
                            ShareUtil.App.toQQRoom(InvateActivity.this, title, content, mImageUrl, shareUrl, null);
                            shareUrlPopwindow.dismiss();
                            break;
                        case R.id.sinaWeibo: //分享到新浪微博


                            //获取商品id列表
                            ShareUtil.App.toSinaWeibo(InvateActivity.this, title, content, mImageUrl, shareUrl, null);
                            shareUrlPopwindow.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        shareUrlPopwindow.showAtLocation(room_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 打开分享海报窗口
     */
    private void openSharePosterPop() {

        if (sharePosterPopwindow == null) {
            sharePosterPopwindow = new SharePopwindow(InvateActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.weixinFriend: //分享到好友
                            if (!AppUtil.isWeixinAvilible(InvateActivity.this)) {
                                ViewShowUtils.showShortToast(InvateActivity.this, "请先安装微信客户端");
                                return;
                            }
                            LoadingView.showDialog(InvateActivity.this);
                            toShareFriends(1);
                            break;
                        case R.id.weixinCircle: //分享到朋友圈
                            if (!AppUtil.isWeixinAvilible(InvateActivity.this)) {
                                ViewShowUtils.showShortToast(InvateActivity.this, "请先安装微信客户端");
                                return;
                            }
                            LoadingView.showDialog(InvateActivity.this);
                            toShareFriends(2);
                            break;
                        case R.id.qqFriend: //分享到QQ
                            LoadingView.showDialog(InvateActivity.this);
                            toShareFriends(3);
                            break;
                        case R.id.qqRoom: //分享到QQ空间
                            LoadingView.showDialog(InvateActivity.this);
                            toShareFriends(4);
                            break;
                        case R.id.sinaWeibo: //分享到新浪微博
                            LoadingView.showDialog(InvateActivity.this);
                            toShareFriends(5);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
        if (sharePosterPopwindow != null) {
            sharePosterPopwindow.showAtLocation(room_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

    }


    /**
     * 微信好友
     */
    private void shareToWeixinFriend(boolean isUrlTrue, final String title, final String content) {
        if (!isUrlTrue) {
            mImageUrl = "applogo";
        }
        ShareUtil.App.toWechatFriend(this, title, content, mImageUrl, shareUrl, null);
    }

    /**
     * 微信朋友圈
     *
     * @param isUrlTrue
     */
    private void shareToWeixinCicle(boolean isUrlTrue, final String title, final String content) {
        if (!isUrlTrue) {
            mImageUrl = "applogo";
        }

        //获取商品id列表
        ShareUtil.App.toWechatMoments(this, title, content, mImageUrl, shareUrl, null);
    }

    /**
     * 海报分享
     *
     * @param type
     */
    private void toShareFriends(int type) {
        if (mUrlsList.size() == 0) {
            return;
        }

        currentItem = banner.getViewPager().getCurrentItem();
        View view = banner.getImageViews().get(currentItem);
        String fileName = "poster" + currentItem;
        String screenPosterUrl = FileUtil.getScreenPosterUrl(fileName, this);

        if (type == 1) {    //微信好友
            ShareUtil.Image.toWechatFriend(this, screenPosterUrl, null);
        } else if (type == 2) {  //分享到朋友圈
            ShareUtil.Image.toWechatMoments(this, screenPosterUrl, null);
        } else if (type == 3) {  //分享到qq好友
            // ShareUtil.shareQqFile(this,file);
            ShareUtil.Image.toQQFriend(this, screenPosterUrl, null);
        } else if (type == 4) {  //qq空间
            ShareUtil.Image.toQQRoom(this, screenPosterUrl, null);
        } else if (type == 5) {  //新浪微博
            ShareUtil.Image.toSinaWeibo(this, screenPosterUrl, null);
        }
        LoadingView.dismissDialog();


    }


}
