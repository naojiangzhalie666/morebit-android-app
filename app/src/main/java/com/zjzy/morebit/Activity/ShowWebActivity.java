package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.RecommendListActivity;
import com.zjzy.morebit.goodsvideo.ShopMallActivity;
import com.zjzy.morebit.goodsvideo.adapter.ShopmallAdapter;
import com.zjzy.morebit.main.ui.fragment.ShowWebFragment;
import com.zjzy.morebit.main.ui.fragment.ShowWebFragment.MYWebChromeClient;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ActivityLinkBean;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.event.WebViewEvent;
import com.zjzy.morebit.pojo.event.WebViewUpdataColorEvent;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestActivityLinkBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.utils.UploadingOnclickUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.ACache;
import com.zjzy.morebit.view.CommercialShareDialog;
import com.zjzy.morebit.view.helper.ToolbarWebHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.net.ssl.SSLHandshakeException;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qq.QQClientNotExistException;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatFavoriteNotSupportedException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 展示网页用界面
 */

public class ShowWebActivity extends BaseActivity {
  public static final int REQUEST_COLLEGE_FEEDBACK = 100;   //商学院文章意见反馈
    private String url = "";
    private String title = "";
    private Bundle bundle;
    private ShowWebFragment mFragment;
    private ToolbarWebHelper mToolbarWebHelper;
    private boolean mIsStop;
    MyPlatformActionListener mMyPlatformActionListener;
    private Article mArticle;

    @BindView(R.id.view_bar)
    View mViewBar;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.ll_toolbar)
    RelativeLayout mRlToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTtle;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_refresh)
    ImageView mIvRefresh;
    @BindView(R.id.iv_off)
    ImageView mIvOff;

    @Override
    public boolean isShowAllSeek() {
        return false;
    }


    public static void start(Activity activity, String url, String title, Article article) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //跳转淘宝
        if (isTaobaoClient(activity, url)) {
            return;
        };
        //跳转到网页
        Intent it = new Intent(activity, ShowWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        bundle.putSerializable("article", article);
        it.putExtras(bundle);
        activity.startActivity(it);
    }


    public static void start(Activity activity, String url, String title) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //跳转淘宝
        if (!url.contains("activity.morebit.com.cn/#/ele?") && !url.contains("activity.morebit.com.cn/#/resta?")) {
            if (isTaobaoClient(activity, url)) {
                return;
            };

        }else{
            if (TaobaoUtil.isAuth()) {//淘宝授权
                TaobaoUtil.getAllianceAppKey((BaseActivity) activity,false);
                return;
            }
        }

        //跳转到网页
        Intent it = new Intent(activity, ShowWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        it.putExtras(bundle);
        activity.startActivity(it);
    }

    public static void start(Activity activity, String url, String title,String id) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //跳转到网页
        Intent it = new Intent(activity, ShowWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        it.putExtras(bundle);
        activity.startActivity(it);
    }

    /**
     * 物流信息
     * @param activity
     * @param url
     * @param title
     */
    public static void startForShip(Activity activity, String url, String title) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        //跳转到网页
        Intent it = new Intent(activity, ShowWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        it.putExtras(bundle);
        activity.startActivity(it);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showweb);
        EventBus.getDefault().register(this);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .titleBar(mViewBar)    //解决状态栏和布局重叠问题，任选其一
                .keyboardEnable(true)
                .init();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
//        } else {
//            ActivityStyleUtil.initSystemBar(this, R.color.color_757575); //设置标题栏颜色值
//        }


        initBundle();
        if (mFragment == null) {
            mFragment = ShowWebFragment.newInstance(url);
            ActivityUtils.replaceFragmentToActivity(
                    ShowWebActivity.this.getSupportFragmentManager(), mFragment, R.id.fl_course);
            mFragment.setWebChromeClient(new MYWebChromeClient() {
                @Override
                public void onPageCommitVisible(boolean canGoBack) {
                    mToolbarWebHelper.setOffVisibility(canGoBack ? View.VISIBLE : View.GONE);
                }
            });
        }


        mToolbarWebHelper = new ToolbarWebHelper(this).setCustomTitle(title).setCustomOff(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).setCustomBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsStop) {
                                return;
                            }
                            // 按下home键,执行这个方法会报错
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }


            }
        }).setCustomRefresh(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragment != null)
                    mFragment.refreshWeb();
            }
        });
        if (mArticle != null && mArticle.getuType() != RecommendListActivity.ARTICLE_REVIEW) {
            mToolbarWebHelper.setCustomShare(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mArticle != null) {
                        openShareDialog(mArticle.getTitle(), mArticle.getShareContent(), mArticle.getImage(), mArticle.getShareUrl(),mArticle.getId());
                    }
                }
            });
        }
    }


    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            url = bundle.getString("url");
            mArticle = (Article) bundle.getSerializable("article");
        }
    }

    @Override
    public boolean isSwipe() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragment != null) {
            mFragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Subscribe
    public void onEventMainThread(WebViewEvent event) {
        //h5标题修改，因为首页二级只有id，只有webview加载了h5，取到title，同时修改这里的标题
        if (!TextUtils.isEmpty(event.getTitle())) {
            setWebTitle(event.getTitle());
        }
    }

    @Subscribe
    public void onEventMainThread(WebViewUpdataColorEvent event) {
        //修改颜色
        if ( mRlToolbar != null && mViewLine != null && mViewBar != null && event != null) {
            if (!TextUtils.isEmpty(event.getColorStr()) && StringsUtils.checkColor(event.getColorStr().trim())) {
                int color = Color.parseColor(event.getColorStr().trim());
                mRlToolbar.setBackgroundColor(color);
                mViewBar.setBackgroundColor(color);
                mViewLine.setBackgroundColor(color);

                if ( mIvOff != null && mIvRefresh != null &&mIvBack != null &&mToolbarTtle != null ){
                    if (event.getIconType() == 0) {
                        mToolbarTtle.setTextColor(ContextCompat.getColor(this, R.color.white));
                        mIvBack.setImageResource(R.drawable.btn_title_return_icon_white);
                        mIvRefresh.setImageResource(R.drawable.web_refresh_icon_white);
                        mIvOff.setImageResource(R.drawable.icon_guanbi_off_white);
                    }else {
                        mToolbarTtle.setTextColor(ContextCompat.getColor(this, R.color.color_333333));
                        mIvBack.setImageResource(R.drawable.btn_title_return_icon);
                        mIvRefresh.setImageResource(R.drawable.web_refresh_icon);
                        mIvOff.setImageResource(R.drawable.icon_guanbi_off);

                    }
                }
            }
        }
    }


    public void setWebTitle(@Nullable String title) {
        if (!TextUtils.isEmpty(title) && !title.startsWith("http")) {
            mToolbarWebHelper.setCustomTitle(title);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsStop = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsStop = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void openShareDialog(final String title, final String text, final String imageUrl, final String shareUrl, final int articleId) {

        CommercialShareDialog shareDialog = new CommercialShareDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadingOnclickUtils.mp4Browse(articleId,UploadingOnclickUtils.TYPE_SHARE_ONCLICK);
                switch (v.getId()) {
                    case R.id.weixinFriend: //分享到好友
                        if (!AppUtil.isWeixinAvilible(ShowWebActivity.this)) {
                            ViewShowUtils.showShortToast(ShowWebActivity.this, "请先安装微信客户端");
                            return;
                        }
                        LoadingView.showDialog(ShowWebActivity.this);
                        toShareFriends(1, title, text, imageUrl, shareUrl);
                        break;
                    case R.id.weixinCircle: //分享到朋友圈
                        if (!AppUtil.isWeixinAvilible(ShowWebActivity.this)) {
                            ViewShowUtils.showShortToast(ShowWebActivity.this, "请先安装微信客户端");
                            return;
                        }
                        LoadingView.showDialog(ShowWebActivity.this);
                        toShareFriends(2, title, text, imageUrl, shareUrl);
                        break;
                    case R.id.qqFriend: //分享到QQ
                        LoadingView.showDialog(ShowWebActivity.this);
                        toShareFriends(3, title, text, imageUrl, shareUrl);
                        break;
                    case R.id.qqRoom: //分享到QQ空间
                        LoadingView.showDialog(ShowWebActivity.this);
                        toShareFriends(4, title, text, imageUrl, shareUrl);
                        break;
                    case R.id.sinaWeibo: //分享到新浪微博
                        LoadingView.showDialog(ShowWebActivity.this);
                        toShareFriends(5, title, text, imageUrl, shareUrl);
                        break;
                    default:
                        break;
                }
            }
        });

        if (!shareDialog.isShowing()) {
            shareDialog.show();
        }
    }


    /**
     * 海报分享
     *
     * @param type
     */
    private void toShareFriends(int type, String title, String text, String imageUrl, String shareUrl) {

        if (mMyPlatformActionListener == null) {
            mMyPlatformActionListener = new MyPlatformActionListener(this);
        }
        if (type == 1) {    //微信好友
            ShareUtil.App.toWechatFriend(this, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 2) {  //分享到朋友圈
            ShareUtil.App.toWechatMoments(this, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 3) {  //分享到qq好友
            ShareUtil.App.toQQFriend(this, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 4) {  //qq空间

            ShareUtil.App.toQQRoom(this, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 5) {  //新浪微博
            ShareUtil.App.toSinaWeibo(this, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        }
        LoadingView.dismissDialog();
    }

    public static class MyPlatformActionListener implements PlatformActionListener {

        Context mContext;

        public MyPlatformActionListener(Context context) {
            mContext = context;
        }

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            ViewShowUtils.showShortToast(com.zjzy.morebit.App.getAppContext(), "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            MyLog.i("test", "throwable: " + throwable.toString());
            String msg = "分享失败，请稍后再试";
            String type = "";
            if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                type = "微信";
            } else if (platform.getName().equalsIgnoreCase(SinaWeibo.NAME)) {
                type = "微博";
            } else if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                type = "QQ";
            } else if (platform.getName().equalsIgnoreCase(QZone.NAME)) {
                type = "QQ空间";
            }
            if (throwable instanceof SSLHandshakeException) {
                msg = type + "分享失败，请检查您的网络状态";
            } else if (throwable instanceof WechatClientNotExistException
                    || throwable instanceof WechatFavoriteNotSupportedException
                    || throwable instanceof WechatTimelineNotSupportedException) {
                msg = "请先安装微信客户端";
            } else if (throwable instanceof QQClientNotExistException) {
                msg = "请先安装" + type + "客户端";
            } else if (throwable instanceof cn.sharesdk.tencent.qzone.QQClientNotExistException) {
                msg = "请先安装" + type + "客户端";
            }

            if (mContext != null && mContext instanceof Activity) {
                final Activity activity = (Activity) mContext;
                if (!activity.isFinishing()) {
                    final String finalMsg = msg;
                    activity.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ViewShowUtils.showShortToast(activity, finalMsg);
                        }
                    }));
                }
            }
        }

        @Override
        public void onCancel(Platform platform, int i) {
            ViewShowUtils.showShortToast(com.zjzy.morebit.App.getAppContext(), "分享取消");
        }
    }

    public static boolean isTaobaoClient(Activity activity, String url) {
        List<String> taobaoLink = (List<String>) ACache.get(activity).getAsObject(C.sp.TAOBAO_LINK_CACHE);
        if(null != taobaoLink && taobaoLink.size()>0){
            boolean isFind = false;
            for (String urlStr: taobaoLink) {
                if(url.contains(urlStr)){
                    isFind = true;
                }
            }
            if(isFind){
                if (AppUtil.isTaobaoClientAvailable(activity)) {
                    if (LoginUtil.checkIsLogin(activity)) {
                        TaobaoUtil.showUrl(activity, url);
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
