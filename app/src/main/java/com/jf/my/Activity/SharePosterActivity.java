package com.jf.my.Activity;

import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.my.App;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.Module.push.Logger;
import com.jf.my.R;
import com.jf.my.adapter.SharePosterAdapter;
import com.jf.my.contact.SdDirPath;
import com.jf.my.network.CallBackObserver;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.ShareUrlBaen;
import com.jf.my.pojo.UserInfo;
import com.jf.my.utils.ActivityStyleUtil;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.FileUtils;
import com.jf.my.utils.GoodsUtil;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.QrcodeUtils;
import com.jf.my.utils.ShareUtil;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.utils.share.ShareMore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * 生成海报分享
 * Created by Administrator on 2017/9/11 0011.
 */

public class SharePosterActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btn_back;
    private RecyclerView mRecyclerView;
    private SharePosterAdapter mAdapter;
    private List<String> mDatas = new ArrayList<>();
    private TextView tv_ticknum, tv_copy;
    private EditText et_copy;
    int ticknum;
    private LinearLayout rl_weixpyq, rl_weix;
    private View status_bar;
    private ArrayList<LinearLayout> menuList = new ArrayList<LinearLayout>();
    private String tkl = "";
    private Bundle bundle;
    private ClipboardManager cm;
    private List<ImageInfo> arrMpInfo;
    private Bitmap qrBitmap;
    private int mCount;   //生成图片的次数

    int mTextPaddingButtom = 65; // 文字距离底下
    int mYQMPaddingText = 5; // 邀请码距离文字
    int mEWMPaddingYQM = 15; // 二维码距离邀请码


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCount++;
            switch (msg.what) {
                case 1:
                    mDatas.add((String) msg.obj);
                    if (arrMpInfo != null && mCount == arrMpInfo.size()) {
                        LoadingView.dismissDialog();
                        mAdapter.setItemisSelectedMap(0, true);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }

        }
    };
    private String mInvite_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_poster);
        initBundle();
        initView();
        mInvite_code = UserLocalData.getUser(this).getInviteCode();
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            arrMpInfo = (List<ImageInfo>) bundle.getSerializable("data");
        }
        UserInfo userInfo = UserLocalData.getUser(SharePosterActivity.this);
        if (userInfo == null) {
            return;
        }
    }

    public void initView() {
        ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值

        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        rl_weixpyq = (LinearLayout) findViewById(R.id.weixinCircle);
        rl_weix = (LinearLayout) findViewById(R.id.friend);
        btn_back.setOnClickListener(this);
        rl_weixpyq.setOnClickListener(this);
        rl_weix.setOnClickListener(this);
        findViewById(R.id.sinaWeibo).setOnClickListener(this);
        findViewById(R.id.qqFriend).setOnClickListener(this);
        findViewById(R.id.qqRoom).setOnClickListener(this);
        //画廊
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new SharePosterAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        getQrcodeUrl();
        mAdapter.setOnItemClickListener(new SharePosterAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                boolean isSelect = mAdapter.getisSelectedAt(position);
                for (int i = 0; i < mDatas.size(); i++) {
                    mAdapter.setItemisSelectedMap(i, false);
                }
                mAdapter.setItemisSelectedMap(position, true);
            }
        });
        //自己加多余
        if (mDatas != null && mDatas.size() > 0) {
            mAdapter.setItemisSelectedMap(0, true);
        }
        mAdapter.notifyDataSetChanged();
        findViewById(R.id.album).setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoadingView.dismissDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.friend:
                if (!AppUtil.isWeixinAvilible(SharePosterActivity.this)) {
                    ViewShowUtils.showShortToast(SharePosterActivity.this, "请先安装微信客户端");
                    return;
                }
                LoadingView.showDialog(this);
                toShareWechatFriend();
                break;
            case R.id.weixinCircle:
                if (!AppUtil.isWeixinAvilible(SharePosterActivity.this)) {
                    ViewShowUtils.showShortToast(SharePosterActivity.this, "请先安装微信客户端");
                    return;
                }
                LoadingView.showDialog(this);
                toShareWechatMoments();
                break;
            case R.id.qqFriend:
                LoadingView.showDialog(this);
                toShareQQFriend();
                break;
            case R.id.qqRoom:
                LoadingView.showDialog(this);
                toShareQQRoom();
                break;
            case R.id.sinaWeibo:
                LoadingView.showDialog(this);
                toShareSinaWeibo();
                break;
            case R.id.album: //保存到相册
                LoadingView.showDialog(this);
                saveToalbum();
                break;
            default:
                break;
        }
    }

    /**
     * 保存图片到相册
     */
    private void saveToalbum() {
        int getSelectIndex = -1;
        for (int i = 0; i < mDatas.size(); i++) {
            boolean isSelect = mAdapter.getisSelectedAt(i);
            if (isSelect) {
                getSelectIndex = i;
                break;
            }
        }
        if (getSelectIndex == -1) {
            getSelectIndex = 0;
        }
        //系统相册目录
        final String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;
        try {
            String[] pathStrs = mDatas.get(getSelectIndex).split("/");
            final String picName = pathStrs[pathStrs.length - 1];


            LoadingView.showDialog(SharePosterActivity.this, "");
            final String downPath = mDatas.get(getSelectIndex);
            final int fGetSelectIndex = getSelectIndex;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 其次把文件插入到系统图库
                        try {
//                            MediaStore.Images.Media.insertImage(SharePosterActivity.this.getContentResolver(),
//                                    new File(arrMpInfo.get(fGetSelectIndex).getThumb()).getAbsolutePath(), picName, null);
                            GoodsUtil.updataImgToTK(SharePosterActivity.this, new File(mDatas.get(fGetSelectIndex)), picName);
                            LoadingView.dismissDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                            LoadingView.dismissDialog();
                        }
                        // 最后通知图库更新
//                        SharePosterActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(arrMpInfo.get(fGetSelectIndex).getThumb())));
                        SharePosterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ViewShowUtils.showShortToast(SharePosterActivity.this, "保存成功");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        LoadingView.dismissDialog();
                        SharePosterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ViewShowUtils.showShortToast(SharePosterActivity.this, "保存失败");
                            }
                        });
                    }
                    LoadingView.dismissDialog();
                }
            }).start();
        } catch (Exception e) {
            LoadingView.dismissDialog();
            e.printStackTrace();
        }

    }

    private void toShareWechatFriend() { //分享到好友
        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }
        int getSelectIndex = -1;
        for (int i = 0; i < mDatas.size(); i++) {
            boolean isSelect = mAdapter.getisSelectedAt(i);
            if (isSelect) {
                getSelectIndex = i;
                break;
            }
        }
        if (getSelectIndex == -1) {
            getSelectIndex = 0;
        }
        ShareUtil.Image.toWechatFriend(SharePosterActivity.this, mDatas.get(getSelectIndex), null);
    }

    private void toShareWechatMoments() {  //分享到朋友圈
        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }
        int getSelectIndex = -1;
        for (int i = 0; i < mDatas.size(); i++) {
            boolean isSelect = mAdapter.getisSelectedAt(i);
            if (isSelect) {
                getSelectIndex = i;
                break;
            }
        }
        if (getSelectIndex == -1) {
            getSelectIndex = 0;
        }

        ShareUtil.Image.toWechatMoments(SharePosterActivity.this, mDatas.get(getSelectIndex), null);
    }

    private void toShareQQFriend() {  //分享到QQ好友
        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }
        int getSelectIndex = -1;
        for (int i = 0; i < mDatas.size(); i++) {
            boolean isSelect = mAdapter.getisSelectedAt(i);
            if (isSelect) {
                getSelectIndex = i;
                break;
            }
        }
        if (getSelectIndex == -1) {
            getSelectIndex = 0;
        }

        ShareUtil.Image.toQQFriend(SharePosterActivity.this, mDatas.get(getSelectIndex), new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ViewShowUtils.showShortToast(SharePosterActivity.this, "分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ViewShowUtils.showShortToast(SharePosterActivity.this, "分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ViewShowUtils.showShortToast(SharePosterActivity.this, "分享取消");
            }
        });
    }

    private void toShareQQRoom() {  //分享到QQ空间
        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }
        int getSelectIndex = -1;
        for (int i = 0; i < mDatas.size(); i++) {
            boolean isSelect = mAdapter.getisSelectedAt(i);
            if (isSelect) {
                getSelectIndex = i;
                break;
            }
        }
        if (getSelectIndex == -1) {
            getSelectIndex = 0;
        }

        ShareUtil.Image.toQQRoom(SharePosterActivity.this, mDatas.get(getSelectIndex), null);
    }

    /**
     * 分享到新浪微博
     */
    private void toShareSinaWeibo() {
        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }
        int getSelectIndex = -1;
        for (int i = 0; i < mDatas.size(); i++) {
            boolean isSelect = mAdapter.getisSelectedAt(i);
            if (isSelect) {
                getSelectIndex = i;
                break;
            }
        }
        if (getSelectIndex == -1) {
            getSelectIndex = 0;
        }

        ShareUtil.Image.toSinaWeibo(SharePosterActivity.this, mDatas.get(getSelectIndex), null);
    }


    /**
     * 下载具体操作
     */
    private void getQrcodeUrl() {
        LoadingView.showDialog(SharePosterActivity.this, "正在生成图片...");
        if (arrMpInfo != null && arrMpInfo.size() > 0) {
            ShareMore.getShareAppLinkObservable(this)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            LoadingView.dismissDialog();
                        }
                    })
                    .subscribe(new DataObserver<ShareUrlBaen>() {

                        @Override
                        protected void onSuccess(ShareUrlBaen data) {
                            for (int i = 0; i < arrMpInfo.size(); i++) {
                                if (!TextUtils.isEmpty(data.getLink())) {
                                    downloadPicture(data.getLink(), arrMpInfo.get(i));
                                }
                            }
                        }
                    });

        }
    }

    public void downloadPicture(final String qrcodeUrl, final ImageInfo makePosterInfo) {
        final String fileName = FileUtils.getPictureName(makePosterInfo.getPicture()) + mInvite_code;

        LoadImgUtils.getImgToBitmapObservable(SharePosterActivity.this, makePosterInfo.getPicture())
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
                .compose(SharePosterActivity.this.<String>bindToLifecycle())
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
}
