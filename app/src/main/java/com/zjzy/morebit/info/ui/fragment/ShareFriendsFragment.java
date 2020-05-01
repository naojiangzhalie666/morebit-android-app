package com.zjzy.morebit.info.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.SdDirPath;
import com.zjzy.morebit.info.adapter.HorizontalPagerAdapter;
import com.zjzy.morebit.info.contract.ShareFriendsContract;
import com.zjzy.morebit.info.presenter.ShareFriendsPresenter;
import com.zjzy.morebit.info.ui.SettingWechatActivity;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ProtocolRuleBean;
import com.zjzy.morebit.pojo.ShareUrlBaen;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.QrcodeUtils;
import com.zjzy.morebit.utils.ReadImgUtils;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.share.ShareMore;
import com.zjzy.morebit.view.SharePopwindow;
import com.zjzy.morebit.view.ToolbarHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangBoTian on 2018/12/26.
 * 分享好友
 */

public class ShareFriendsFragment extends MvpFragment<ShareFriendsPresenter> implements ShareFriendsContract.View {

    private List<ImageInfo> mImageInfos;
    private List<String> mUrlsList = new ArrayList<>();
    private int mCount;   //生成图片的次数
    private Bitmap qrBitmap;
    private String mInvite_code = "";

    int mTextPaddingButtom = 65; // 文字距离底下
    int mYQMPaddingText = 5; // 邀请码距离文字
    int mEWMPaddingYQM = 15; // 二维码距离邀请码

    private String mImageUrl;
    private String shareUrl;
    private SharePopwindow shareUrlPopwindow;
    private SharePopwindow sharePosterPopwindow;


    @BindView(R.id.hicvp)
    HorizontalInfiniteCycleViewPager mHorizontalInfiniteCycleViewPager;
    @BindView(R.id.room_view)
    View room_view;
    @BindView(R.id.ll_write)
    LinearLayout mLlWrite;
    String shareText = App.getAppContext().getString(R.string.shareapp_text);
    private String mRuleUrl;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCount++;
            switch (msg.what) {
                case 1:
                    mUrlsList.add((String) msg.obj);
                    if (mImageInfos != null && mCount == mImageInfos.size()) {
                        LoadingView.dismissDialog();
                        mHorizontalInfiniteCycleViewPager.setAdapter(new HorizontalPagerAdapter(getActivity(), mUrlsList));
                        if (mUrlsList.size() >= 2) {
                            mHorizontalInfiniteCycleViewPager.setCurrentItem(1);
                        } else {
                            mHorizontalInfiniteCycleViewPager.setCurrentItem(0);
                        }
                        MyLog.i("test", "mUrlsList: " + mUrlsList.size());
                        App.getACache().put(C.sp.SHARE_POSTER + mInvite_code, (ArrayList) mUrlsList);
                    }
                    break;
            }

        }
    };

    @Override
    protected void initData() {
        mInvite_code = UserLocalData.getUser(getActivity()).getInviteCode();
        mImageUrl = UserLocalData.getUser(getActivity()).getHeadImg();
        if (TextUtils.isEmpty(mImageUrl)) {
            mImageUrl = "applogo";
        }
        inivEWM();
        getBannerData();
    }

    private void getBannerData() {
        ReadImgUtils.callPermission(getActivity(), new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {

                mPresenter.getBanner(ShareFriendsFragment.this, C.UIShowType.Poster);
            }

            @Override
            public void onError() {
                ViewShowUtils.showShortToast(getActivity(), R.string.call_premission_read_sd);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    @Override
    protected void initView(View view) {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.share_friends)).setCustomRightTitle(""/*"奖励规则"*/, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJiangRule();
            }
        });
        initWriteView();
    }

    private void initWriteView() {
        if (TextUtils.isEmpty(UserLocalData.getUser().getWxNumber())) {
            mLlWrite.setVisibility(View.VISIBLE);
        } else {
            mLlWrite.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initWriteView();

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_share_friends;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onSuccessful(List<ImageInfo> datas) {
        mImageInfos = datas;
        loadingPicture();

    }

    private void loadingPicture() {
        LoadingView.showDialog(getActivity(), "正在生成图片...");
        mUrlsList = (List<String>) App.getACache().getAsObject(C.sp.SHARE_POSTER + mInvite_code);
        if (mUrlsList == null || mUrlsList.size() != mImageInfos.size()) {   //本地没有缓存或者和线上的不一致都重新生成图片缓存在本地
            if (mUrlsList == null) {
                mUrlsList = new ArrayList<>();
            }
            createPoster();
        } else {
            boolean isLocalExit = true;
            for (int i = 0; i < mImageInfos.size(); i++) {   //以线上为标准,截取图片名称 循环对比
                boolean isExit = false;
                String remoteName = FileUtils.interceptingPictureName(mImageInfos.get(i).getPicture()) + mInvite_code;
                for (int j = 0; j < mUrlsList.size(); j++) {  //循环缓存的路径和线上对比
                    String localName = FileUtils.interceptingPictureName(mUrlsList.get(j));
                    if (remoteName.equals(localName)) {   //如果缓存存在,跳出循环进行下一个对比
                        isExit = true;
                        break;
                    }
                }
                if (!isExit) {    // 如果本地集合有一个不存在线上的图片名称,跳出循环
                    isLocalExit = false;
                    break;
                }
            }
            if (isLocalExit) {   //缓存已存在,不需要重新下载
                for (int i = 0; i < mUrlsList.size(); i++) {    //判断本地是否存在文件,不存在重新生成图片
                    if (!FileUtils.isFileExit(mUrlsList.get(i))) {
                        createPoster();
                        return;
                    }
                }
                if (mUrlsList.size() >= 1) {
                    mHorizontalInfiniteCycleViewPager.setAdapter(new HorizontalPagerAdapter(getActivity(), mUrlsList));
                    if (mUrlsList.size() > 1)
                        mHorizontalInfiniteCycleViewPager.setCurrentItem(1);
                } else {
                    ViewShowUtils.showShortToast(getActivity(), "加载出错,请退出页面重试");
                }
                LoadingView.dismissDialog();
            } else {//如果本地图片已不存在或者文件名字和线上的不一致,重新生成图片
                createPoster();
            }
        }
    }

    @OnClick({R.id.share_url, R.id.share_poster, R.id.tv_write})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_url:
                openShareUrlPop(getString(R.string.app_name), shareText);
                break;
            case R.id.share_poster:
                openSharePosterPop();
                break;
            case R.id.tv_write:
                SettingWechatActivity.start(getActivity(), 0);
                break;
        }

    }

    private void createPoster() {
        mUrlsList.clear();
        getQrcodeUrl();
    }

    /**
     * 下载具体操作
     */
    private void getQrcodeUrl() {

        if (mImageInfos != null && mImageInfos.size() > 0) {
            ShareMore.getShareAppLinkObservable(this)
                    .subscribe(new DataObserver<ShareUrlBaen>() {
                        @Override
                        protected void onSuccess(ShareUrlBaen data) {
                            for (int i = 0; i < mImageInfos.size(); i++) {
                                if (!TextUtils.isEmpty(data.getLink())) {
                                    downloadPicture(data.getLink(), mImageInfos.get(i));
                                }
                            }
                        }
                    });
        }
    }


    public void downloadPicture(final String qrcodeUrl, final ImageInfo makePosterInfo) {
        final String fileName = FileUtils.interceptingPictureName(makePosterInfo.getPicture()) + mInvite_code;
        MyLog.i("test", "fileName: " + fileName);
        LoadImgUtils.getImgToBitmapObservable(getActivity(), makePosterInfo.getPicture())
                .subscribe(new CallBackObserver<Bitmap>() {
                    @Override
                    public void onNext(@NonNull Bitmap bitmap) {
                        compoundImage(bitmap, fileName, qrcodeUrl);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mHandler.sendEmptyMessage(0);
                    }
                });
    }

    /**
     * 把二维码图片内嵌到图片中
     *
     * @param bitmap
     */
    private void compoundImage(final Bitmap bitmap, final String fileName, final String ewmUrl) {
        Observable.just(ewmUrl)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        String img = getImg(bitmap, fileName, ewmUrl);
                        return Observable.just(img);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<String>bindToLifecycle())
                .subscribe(new CallBackObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Message message = new Message();
                        message.obj = s;
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });


    }

    private String getImg(Bitmap bitmap, String fileName, String ewmUrl) {
        int qrSize = 290;
        int wblSize = 2;
        int qrscSize = 200;
//        Bitmap logoBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.share_logo);
        Bitmap logoBitmap = UserLocalData.getmMyHeadBitmap();
        if (qrBitmap == null) {
            qrBitmap = QrcodeUtils.createQRCode(ewmUrl, qrSize);
            try {
                int whiteBitmapWidth = 12;
                qrBitmap = LoadImgUtils.bg2WhiteBitmap(qrBitmap, whiteBitmapWidth);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        Bitmap qrBitmap = QrcodeUtils.createQRCode(mQrContnet, qrSize);
        //缩放二维码大小
        if (qrBitmap == null) {
            ViewShowUtils.showLongToast(App.getAppContext(), "生成失败");
            return fileName;
        }
        Bitmap scBitmap = Bitmap.createScaledBitmap(qrBitmap, qrscSize - wblSize * 2, qrscSize - wblSize * 2, true);
        //给二维码添加白色边框

        Bitmap wblBitmap = Bitmap.createBitmap(qrscSize, qrscSize, Bitmap.Config.ARGB_8888);
        Canvas wblCanvas = new Canvas(wblBitmap);
        Paint wblPaint = new Paint();
//        wblCanvas.drawColor(Color.parseColor("#ffffff"));
        wblCanvas.drawBitmap(scBitmap, wblSize, wblSize, wblPaint);

        Canvas canvas = new Canvas(bitmap);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.image_yaoqingma);
        int yamWidth = bmp.getWidth();
        int yamHeight = bmp.getHeight();


        Paint PaintText = new Paint();
        PaintText.setStrokeWidth(5);
        PaintText.setTextSize(40);
        PaintText.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();
        PaintText.getTextBounds(mInvite_code, 0, mInvite_code.length(), bounds);

        Paint paintBitmap = new Paint();
        // 设置去掉锯齿
        PaintFlagsDrawFilter paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        canvas.setDrawFilter(paintFlagsDrawFilter);

        int textHeight = bounds.height();// 文字高度
        // 画文字
        canvas.drawText(mInvite_code, bitmap.getWidth() / 2 - bounds.width() / 2, bitmap.getHeight() - mTextPaddingButtom, PaintText);

        // 邀请码图片
        int i = bitmap.getWidth() / 2 - yamWidth / 2;
        canvas.drawBitmap(bmp, i, bitmap.getHeight() - mYQMPaddingText - yamHeight - mTextPaddingButtom - textHeight, paintBitmap);


        //往海报上绘上二维码
        int qrLeft = bitmap.getWidth() / 2 - qrscSize / 2;
        int qrTop = bitmap.getHeight() - qrscSize - mEWMPaddingYQM - mYQMPaddingText - yamHeight - mTextPaddingButtom - textHeight;
        canvas.drawBitmap(wblBitmap, qrLeft, qrTop, paintBitmap);

        String s = FileUtils.savePhoto(bitmap, SdDirPath.IMAGE_CACHE_PATH, fileName);
        scBitmap.recycle();
        scBitmap = null;
        wblBitmap.recycle();
        wblBitmap = null;
        return s;
    }


    /**
     * 打开分享链接窗口
     */
    private void openShareUrlPop(final String title, final String content) {

        if (shareUrlPopwindow == null) {
            shareUrlPopwindow = new SharePopwindow(getActivity(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.weixinFriend: //分享到好友
                            if (!AppUtil.isWeixinAvilible(getActivity())) {
                                ViewShowUtils.showShortToast(getActivity(), "请先安装微信客户端");
                                return;
                            }

                            AppUtil.isCheckImgUrlTrue(getActivity(), mImageUrl,
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
                            if (!AppUtil.isWeixinAvilible(getActivity())) {
                                ViewShowUtils.showShortToast(getActivity(), "请先安装微信客户端");
                                return;
                            }
                            AppUtil.isCheckImgUrlTrue(getActivity(), mImageUrl,
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
                            if (!AppUtil.isQQClientAvailable(getActivity())) {
                                ViewShowUtils.showShortToast(getActivity(), "请先安装QQ客户端");
                                return;
                            }


                            ShareUtil.App.toQQFriend(getActivity(), title, content, mImageUrl, shareUrl, null);
                            shareUrlPopwindow.dismiss();
                            break;
                        case R.id.qqRoom: //分享到QQ空间
                            if (!AppUtil.isQQClientAvailable(getActivity())) {
                                ViewShowUtils.showShortToast(getActivity(), "请先安装QQ客户端");
                                return;
                            }

                            //获取商品id列表
                            ShareUtil.App.toQQRoom(getActivity(), title, content, mImageUrl, shareUrl, null);
                            shareUrlPopwindow.dismiss();
                            break;
                        case R.id.sinaWeibo: //分享到新浪微博


                            //获取商品id列表
                            ShareUtil.App.toSinaWeibo(getActivity(), title, content, mImageUrl, shareUrl, null);
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
            sharePosterPopwindow = new SharePopwindow(getActivity(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.weixinFriend: //分享到好友
                            if (!AppUtil.isWeixinAvilible(getActivity())) {
                                ViewShowUtils.showShortToast(getActivity(), "请先安装微信客户端");
                                return;
                            }
                            LoadingView.showDialog(getActivity());
                            toShareFriends(1);
                            break;
                        case R.id.weixinCircle: //分享到朋友圈
                            if (!AppUtil.isWeixinAvilible(getActivity())) {
                                ViewShowUtils.showShortToast(getActivity(), "请先安装微信客户端");
                                return;
                            }
                            LoadingView.showDialog(getActivity());
                            toShareFriends(2);
                            break;
                        case R.id.qqFriend: //分享到QQ
                            LoadingView.showDialog(getActivity());
                            toShareFriends(3);
                            break;
                        case R.id.qqRoom: //分享到QQ空间
                            LoadingView.showDialog(getActivity());
                            toShareFriends(4);
                            break;
                        case R.id.sinaWeibo: //分享到新浪微博
                            LoadingView.showDialog(getActivity());
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

    private void inivEWM() {
        ShareMore.getShareAppLinkObservable(this)
                .subscribe(new DataObserver<ShareUrlBaen>() {
                    @Override
                    protected void onSuccess(final ShareUrlBaen data) {
                        shareUrl = data.getLink();
                        shareText = data.getSharp();
                    }
                });
    }


    /**
     * 微信好友
     */
    private void shareToWeixinFriend(boolean isUrlTrue, final String title, final String content) {
        if (!isUrlTrue) {
            mImageUrl = "applogo";
        }
        ShareUtil.App.toWechatFriend(getActivity(), title, content, mImageUrl, shareUrl, null);
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
        ShareUtil.App.toWechatMoments(getActivity(), title, content, mImageUrl, shareUrl, null);
    }

    /**
     * 海报分享
     *
     * @param type
     */
    private void toShareFriends(int type) {
        if (mUrlsList.size() == 0 || mUrlsList.size() < mHorizontalInfiniteCycleViewPager.getRealItem()) {
            return;
        }
        if (type == 1) {    //微信好友
            ShareUtil.Image.toWechatFriend(getActivity(), mUrlsList.get(mHorizontalInfiniteCycleViewPager.getRealItem()), null);
        } else if (type == 2) {  //分享到朋友圈
            ShareUtil.Image.toWechatMoments(getActivity(), mUrlsList.get(mHorizontalInfiniteCycleViewPager.getRealItem()), null);
        } else if (type == 3) {  //分享到qq好友
            ShareUtil.Image.toQQFriend(getActivity(), mUrlsList.get(mHorizontalInfiniteCycleViewPager.getRealItem()), null);
        } else if (type == 4) {  //qq空间
            if (mUrlsList.size() < mHorizontalInfiniteCycleViewPager.getRealItem()) {
                return;
            }
            ShareUtil.Image.toQQRoom(getActivity(), mUrlsList.get(mHorizontalInfiniteCycleViewPager.getRealItem()), null);
        } else if (type == 5) {  //新浪微博
            if (mUrlsList.size() < mHorizontalInfiniteCycleViewPager.getRealItem()) {
                return;
            }
            ShareUtil.Image.toSinaWeibo(getActivity(), mUrlsList.get(mHorizontalInfiniteCycleViewPager.getRealItem()), null);
        }
        LoadingView.dismissDialog();
    }


    /**
     * 奖励规则
     */
    private void getJiangRule() {
        if (TextUtils.isEmpty(mRuleUrl)) {
            LoadingView.showDialog(getActivity(), "");
            LoginUtil.getSystemStaticPage((RxAppCompatActivity) this.getActivity(), C.ProtocolType.rewardRules)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            LoadingView.dismissDialog();
                        }
                    }).subscribe(new DataObserver<List<ProtocolRuleBean>>() {
                @Override
                protected void onSuccess(List<ProtocolRuleBean> data) {
                    mRuleUrl = data.get(0).getHtmlUrl();
                    PageToUtil.goToWebview(getActivity(), "奖励规则", data.get(0).getHtmlUrl());
                }
            });
        } else {
            PageToUtil.goToWebview(getActivity(), "奖励规则", mRuleUrl);
        }


    }
}
