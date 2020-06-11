package com.zjzy.morebit.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


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
import com.zjzy.morebit.contact.SdDirPath;
import com.zjzy.morebit.info.adapter.HorizontalPagerAdapter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShareUrlBaen;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.Banner;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.QrcodeUtils;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.fire.BuglyUtils;
import com.zjzy.morebit.utils.share.ShareMore;
import com.zjzy.morebit.utils.sys.RequestPermissionUtlis;
import com.zjzy.morebit.view.SharePopwindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class InvateActivity extends BaseActivity implements View.OnClickListener {

    private View status_bar;
    private TextView tv_name;
    private Banner banner;
    private List<ImageInfo> mImageInfos;
    private List<String> mUrlsList = new ArrayList<>();
    private String mInvite_code = "";
    private TextView tv_code, tv_copy;
    private LinearLayout btn_back,share_link,share_poster,share_picture;
    private int mCount;   //生成图片的次数
    private Bitmap qrBitmap;
    private String mImageUrl;
    private String shareUrl;
    int mTextPaddingButtom = 65; // 文字距离底下
    int mYQMPaddingText = 5; // 邀请码距离文字
    int mEWMPaddingYQM = 15; // 二维码距离邀请码
    private SharePopwindow shareUrlPopwindow;
    private SharePopwindow sharePosterPopwindow;
    private RelativeLayout room_view;
    String shareText = App.getAppContext().getString(R.string.shareapp_text);
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
                        banner.update(mUrlsList);
                        MyLog.i("test", "mUrlsList: " + mUrlsList.size());
                        App.getACache().put(C.sp.SHARE_POSTER + mInvite_code, (ArrayList) mUrlsList);

                    }
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invate);
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

    private void onSuccessful(List<ImageInfo> data) {
        mImageInfos = data;
        loadingPicture();
    }

    private void loadingPicture() {
        LoadingView.showDialog(this, "正在生成图片...");
        mUrlsList = (List<String>) App.getACache().getAsObject(C.sp.SHARE_POSTER + mInvite_code);
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



                banner.update(mUrlsList);
                LoadingView.dismissDialog();
            } else {//如果本地图片已不存在或者文件名字和线上的不一致,重新生成图片
                createPoster();
            }
        }
    }

    private void createPoster() {
        mUrlsList.clear();
        getQrcodeUrl();
    }

    private void getQrcodeUrl() {
        if (mImageInfos != null && mImageInfos.size() > 0) {
            ShareMore.getShareAppLinkObservable(this)
                    .subscribe(new DataObserver<ShareUrlBaen>() {
                        @Override
                        protected void onSuccess(ShareUrlBaen data) {
                            for (int i = 0; i < mImageInfos.size(); i++) {
                                if (!TextUtils.isEmpty(data.getWxRegisterlink())) {
                                    downloadPicture(data.getWxRegisterlink(), mImageInfos.get(i));
                                }
                            }
                        }
                    });
        }
    }

    private void initView() {
        status_bar = findViewById(R.id.status_bar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        banner = (Banner) findViewById(R.id.banner);
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_copy = (TextView) findViewById(R.id.tv_copy);
        btn_back= (LinearLayout) findViewById(R.id.btn_back);
        share_link= (LinearLayout) findViewById(R.id.share_link);
        share_picture= (LinearLayout) findViewById(R.id.share_picture);
        share_poster= (LinearLayout) findViewById(R.id.share_poster);
        room_view= (RelativeLayout) findViewById(R.id.room_view);
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
        banner.setImageLoader(new GlideImageLoader())
                .setBannerStyle(BannerConfig.NOT_INDICATOR)
                .setOffscreenPageLimit(3)
                .setPageMargin(20)
                .setBannerAnimation(Transformer.ZoomOutSlide)
                .isAutoPlay(false)
                .setViewPageMargin((int) 100, 0, (int) 100, 0)
                .start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(InvateActivity.this, ImagePagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) mUrlsList);
                bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                bundle.putString(ImagePagerActivity.TAOBAO_ID, "get_gther_poster");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



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
                AppUtil.coayTextPutNative(this,tv_code.getText().toString());
                ToastUtils.showLong("复制成功");
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.share_link:
                openShareUrlPop(getString(R.string.app_name), shareText);
                break;
            case R.id.share_picture:
              final int   currentItem2 = banner.getViewPager().getCurrentItem()-1;
                RequestPermissionUtlis.requestOne(InvateActivity.this, new MyAction.OnResult<String>() {
                    @Override
                    public void invoke(String arg) {
                        saveImg(mUrlsList.get(currentItem2));
                    }


                    @Override
                    public void onError() {

                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.share_poster:
                openSharePosterPop();
                break;
        }
    }

    private void saveImg(String urls) {
//        final String bitName = getIntent().getStringExtra(TAOBAO_ID) + mPage;
        String bitName = "";

        if (TextUtils.isEmpty(urls)) {
            BuglyUtils.e("ImagePagerActivity", "url 为空");
            ViewShowUtils.showLongToast(this, "保存图片失败");
            return;
        }
        bitName = FileUtils.getPictureName(urls);
        LoadingView.showDialog(InvateActivity.this, "");
        GoodsUtil.saveImg(InvateActivity.this, urls, bitName, new MyAction.OnResultTwo<File, Integer>() {
            @Override
            public void invoke(File arg, Integer arg1) {
                LoadingView.dismissDialog();
                if (arg == null) {
                    ViewShowUtils.showShortToast(InvateActivity.this, "保存图片失败");
                    return;
                }
                if (arg1 == 1) {
                    ViewShowUtils.showShortToast(InvateActivity.this, R.string.save_img_succeed);
                } else {
                    String dir = SdDirPath.IMAGE_CACHE_PATH;
//                                FileUtils.updaImgToMedia(ImagePagerActivity.this,dir,bitName);
                    String string = InvateActivity.this.getResources().getString(R.string.img_save_to, dir);
                    ViewShowUtils.showShortToast(InvateActivity.this, string);
                }
            }

            @Override
            public void onError() {
                LoadingView.dismissDialog();
            }
        });
    }
    public void downloadPicture(final String qrcodeUrl, final ImageInfo makePosterInfo) {
        final String fileName = FileUtils.interceptingPictureName(makePosterInfo.getPicture()) + mInvite_code;
        MyLog.i("test", "fileName: " + fileName);
        LoadImgUtils.getImgToBitmapObservable(this, makePosterInfo.getPicture())
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

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yqm_bg_poster);
        int yamWidth = bmp.getWidth();
        int yamHeight = bmp.getHeight();


        Paint PaintText = new Paint();
        PaintText.setStrokeWidth(5);
        PaintText.setTextSize(28);
        PaintText.setTextAlign(Paint.Align.LEFT);
        PaintText.setColor(Color.WHITE);

        Rect bounds = new Rect();
        PaintText.getTextBounds("邀请码："+mInvite_code, 0, mInvite_code.length(), bounds);

        Paint paintBitmap = new Paint();
        // 设置去掉锯齿
        PaintFlagsDrawFilter paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        // 邀请码图片
        int i = bitmap.getWidth() / 2 - yamWidth / 2;
        canvas.drawBitmap(bmp, i, bitmap.getHeight() - mYQMPaddingText - yamHeight - mTextPaddingButtom, paintBitmap);

        int textHeight = bounds.height();// 文字高度
        // 画文字
        canvas.drawText("邀请码："+mInvite_code, bitmap.getWidth() / 2 - bounds.width()/2-34 , bitmap.getHeight() - mTextPaddingButtom-18, PaintText);

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

      int  currentItem = banner.getViewPager().getCurrentItem()-1;
        if (type == 1) {    //微信好友
            ShareUtil.Image.toWechatFriend(this, mUrlsList.get(currentItem), null);
        } else if (type == 2) {  //分享到朋友圈
            ShareUtil.Image.toWechatMoments(this, mUrlsList.get(currentItem), null);
        } else if (type == 3) {  //分享到qq好友
            ShareUtil.Image.toQQFriend(this, mUrlsList.get(currentItem), null);
        } else if (type == 4) {  //qq空间
            ShareUtil.Image.toQQRoom(this, mUrlsList.get(currentItem), null);
        } else if (type == 5) {  //新浪微博
            ShareUtil.Image.toSinaWeibo(this, mUrlsList.get(currentItem), null);
        }
        LoadingView.dismissDialog();
    }
}
