package com.zjzy.morebit.main.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.InvateActivity;
import com.zjzy.morebit.Activity.SearchActivity;
import com.zjzy.morebit.Activity.ShareHungryActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.InputVerificationCodeDialog;
import com.zjzy.morebit.Module.common.Dialog.ShopkeeperUpgradeDialog2;
import com.zjzy.morebit.Module.common.Dialog.ShopkeeperUpgradeDialog3;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.OpinionpReplyFragment;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.NumberSubFragment;
import com.zjzy.morebit.info.ui.AppFeedActivity;
import com.zjzy.morebit.login.ui.LoginMainFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.AppUpgradeInfo;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.RefreshUserInfoEvent;
import com.zjzy.morebit.pojo.event.WebViewUpdataColorEvent;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.request.RequestCircleFeedBackBean;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.CommInterface;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.appDownload.QianWenUpdateUtlis;
import com.zjzy.morebit.utils.appDownload.update_app.UpdateAppBean;
import com.zjzy.morebit.view.SharePopwindow;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by fengrs on 2018/10/22.
 * 备注: js 交互类
 */

public class WebJSHook {
    private Fragment mFragment;
    private SharePopwindow sharePopwindow;
    private String mAccountDestroyHit;

    public WebJSHook(ShowWebFragment fragment) {
        this.mFragment = fragment;

    }

    /**
     * 保存图片地址
     */
    @JavascriptInterface
    public void saveImgUrl(final String stringUrl) {
        if (TextUtils.isEmpty(stringUrl)) return;
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                LoadingView.showDialog(mFragment.getContext());
                LoadImgUtils.getImgToBitmapObservable(mFragment.getContext(), stringUrl)

                        .subscribe(new CallBackObserver<Bitmap>() {
                            @Override
                            public void onNext(@io.reactivex.annotations.NonNull Bitmap bitmap) {
                                /** * 分享图片 */
                                if (bitmap != null) {
                                    File file = GoodsUtil.saveBitmapToFile(mFragment.getActivity(), bitmap, "img-" + System.currentTimeMillis());
                                    if (file != null) {
                                        showText("保存成功");
                                    }
                                }
                                LoadingView.dismissDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                LoadingView.dismissDialog();
                                showText("保存失败");

                            }
                        });
            }
        });
    }


    /**
     * 账号注销
     */
    @JavascriptInterface
    public void RegularOpreate(int type,String content) {
        if(type==0){
            if (TextUtils.isEmpty(mAccountDestroyHit)) {
                getAccountDestroyHint();
            } else {
                openAccountDestroyDialog(mAccountDestroyHit);
            }
        }else if (type==1){
            updateGrade2();
        }

    }



    /**
     * 保存图片
     */
    @JavascriptInterface
    public void saveImg(String string) {
        MyLog.d("WebJSHook", "saveImg");
        File file = getBitmapFile(string);
        if (file != null) {
            showText("保存成功");
        } else {
            showText("保存失败");

        }
    }

    /**
     * 保存图片
     * <p>
     * <p>
     * type: Int/// 0：外卖特惠，
     * <p>
     * content: string;
     * <p>
     * contentType: Int/// 0是网络图片、1是base64图片
     */
    @JavascriptInterface
    public void downImage(int type, String content, int contentType) {
        if (type == 0) {//外卖特惠
            GoodsUtil.poster(mFragment.getActivity());
        }

    }

    /*
     *
     *
url: String ///链接url地址
title: String///分享显示标题
text: String///分享显示文案内容
image:  String///分享显示图片，为base64类型，需要的时候传

type: Int/// 0是网络图片、1是base64图片
     *
     *
     * */
    @JavascriptInterface
    public void shareHtml(final String url, final String title,final String text,  final String image,int type ) {
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                LoadingView.dismissDialog();
                if (!LoginUtil.checkIsLogin(mFragment.getActivity())) {
                    return;
                }
                if (sharePopwindow != null) {
                    sharePopwindow.dismiss();
                }

                sharePopwindow = new SharePopwindow(mFragment.getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.weixinFriend: //分享到好友
                                if (!AppUtil.isWeixinAvilible(mFragment.getActivity())) {
                                    ViewShowUtils.showShortToast(mFragment.getActivity(), "请先安装微信客户端");
                                    return;
                                }
                                ShareUtil.App.toWechatFriend(mFragment.getActivity(), title, text, image, url, null);

                                break;
                            case R.id.weixinCircle: //分享到朋友圈
                                if (!AppUtil.isWeixinAvilible(mFragment.getActivity())) {
                                    ViewShowUtils.showShortToast(mFragment.getActivity(), "请先安装微信客户端");
                                    return;
                                }
                                ShareUtil.App.toWechatMoments(mFragment.getActivity(), title,text, image, url, null);
                                break;
                            case R.id.qqFriend: //分享到QQ
                                if (!AppUtil.isQQClientAvailable(mFragment.getActivity())) {
                                    ViewShowUtils.showShortToast(mFragment.getActivity(), "请先安装QQ客户端");
                                }
                                ShareUtil.App.toQQFriend(mFragment.getActivity(), title, text, image, url, null);
                                break;
                            case R.id.qqRoom: //分享到QQ空间
                                if (!AppUtil.isQQClientAvailable(mFragment.getActivity())) {
                                    ViewShowUtils.showShortToast(mFragment.getActivity(), "请先安装QQ客户端");
                                    return;
                                }
                                ShareUtil.App.toQQRoom(mFragment.getActivity(), title, text, image, url, null);
                                break;


                        }
                        sharePopwindow.dismiss();
                    }

                });
                sharePopwindow.showAtLocation(mFragment.getView().findViewById(R.id.webview), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

    }


    /**
     * 复制文字
     */
    @JavascriptInterface
    public void cayeText(String text) {
        AppUtil.coayText(mFragment.getActivity(), text);
        showText("复制成功");
    }
    /*
     *
     *
     * 获取用户id
     * */

    @JavascriptInterface
    public String getUserCode() {
        UserInfo user = UserLocalData.getUser(mFragment.getActivity());
        return user.getId();
    }

    /**
     * 复制文字 不会弹窗
     */
    @JavascriptInterface
    public void copyText(String text) {
        AppUtil.coayText(mFragment.getActivity(), text);
    }


    /**
     * 打开其他app
     *
     * @param pkg 包名
     * @param cls 页面路径
     */
    @JavascriptInterface
    public void oppeOtherApp(final String pkg, final String cls) {
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                PageToUtil.oppeOtherApp(mFragment.getActivity(), pkg, cls);
            }
        });
    }

    /**
     * 去登陆界面
     */
    @JavascriptInterface
    public void gotoLogin() {
        LoginMainFragment.start(mFragment.getActivity());
    }

    private void showText(final String text) {
        if (TextUtils.isEmpty(text)) return;
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                LoadingView.dismissDialog();
                ViewShowUtils.showShortToast(mFragment.getActivity(), text);
            }
        });
    }

    /**
     * 分享图片地址
     *
     * @param url
     */
    @JavascriptInterface
    public void shareUrl(String url) {
        MyLog.i("WebJSHook", "shareUrl");
        if (TextUtils.isEmpty(url)) {
            return;
        }
        startShare(url, "", "", false);
    }

    /**
     * 分享图片
     *
     * @param string
     */
    @JavascriptInterface
    public void shareBitmap(String string) {
        MyLog.i("WebJSHook", "shareBitmapByte    string  " + string);
        MyLog.i("WebJSHook", "shareBitmapByte   threadName " + Thread.currentThread().getName());
        File file = getBitmapFile(string);
        if (file == null) {
            showText("分享失败");
        }
        startShare(file.getPath(), "", "", false);
    }

    /**
     * 保存获取图片
     *
     * @param base64String js传过来的Base64
     * @return
     */
    private File getBitmapFile(String base64String) {
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                LoadingView.showDialog(mFragment.getContext());
            }
        });
        Bitmap bitmap = null;
        File file = null;
        if (!TextUtils.isEmpty(base64String)) {
            try {
                byte[] bitmapArray = Base64.decode(base64String.split(",")[1], Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                file = GoodsUtil.saveBitmapToFile(mFragment.getActivity(), bitmap, "img-" + System.currentTimeMillis());
            }
        }
        return file;
    }


    /**
     * 分享链接
     */
    @JavascriptInterface
    public void shareWebpage(final String title, final String content, final String url) {
        MyLog.i("WebJSHook", "shareWebpage");
        startShare(url, title, content, false);
    }

    /**
     * 分享饿了么
     */
    @JavascriptInterface
    public void shareEle() {
        MyLog.i("WebJSHook", "shareEle");
        mFragment.getActivity().startActivity(new Intent(mFragment.getActivity(), ShareHungryActivity.class));

    }

    /**
     * 跳转商品详情页
     *
     * @param taobaoId    商品淘宝id
     * @param taobaoTitle 商品标题
     */
    @JavascriptInterface
    public void gotoGoodsDetail(final String taobaoId, final String taobaoTitle) {
        MyLog.i("WebJSHook", "shareWebpage");
        if (TextUtils.isEmpty(taobaoId)) return;

        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                ShopGoodInfo shopGoodInfo = new ShopGoodInfo();
                shopGoodInfo.setItemSourceId(taobaoId);
                shopGoodInfo.setTitle(taobaoTitle);
                GoodsDetailActivity.start(mFragment.getActivity(), shopGoodInfo);
            }
        });

    }

    /**
     * 跳转商品详情页
     *
     * @param itmeData
     */
    @JavascriptInterface
    public void gotoGoodsDetail(final String itmeData) {
        MyLog.i("WebJSHook", "shareWebpage");
        if (TextUtils.isEmpty(itmeData)) return;


        final ShopGoodInfo shopGoodInfo = MyGsonUtils.jsonToBean(itmeData, ShopGoodInfo.class);
        if (shopGoodInfo == null) return;
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                GoodsDetailActivity.start(mFragment.getActivity(), shopGoodInfo);
            }
        });

    }


    /**
     * 跳转淘宝
     *
     * @param url
     */
    @JavascriptInterface
    public void goTaoBaoDetail(final String url) {
        MyLog.i("WebJSHook", "shareWebpage");
        if (TextUtils.isEmpty(url)) return;
        if (TaobaoUtil.isAuth()) {//淘宝授权
            TaobaoUtil.getAllianceAppKey((BaseActivity) mFragment.getContext());
        } else {
            App.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    TaobaoUtil.showUrl((Activity) mFragment.getContext(), url);
                }
            });
        }


    }

    /**
     * 修改顶部栏背景
     * 必须加 #
     *
     * @param colorStr 颜色值  如 #00ff00
     * @param colorStr 1 ：导航栏 ，标题为 黑色 ；0，：导航栏 ，标题为 白色 。
     */
    @JavascriptInterface
    public void updataTitleBgColor(final String colorStr, final int iconType) {
        MyLog.i("WebJSHook", "shareWebpage");
        if (TextUtils.isEmpty(colorStr)) {
            return;
        }
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new WebViewUpdataColorEvent(colorStr, iconType));
            }
        });
    }

    /**
     * 跳转搜索
     */
    @JavascriptInterface
    public void startSearch() {
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                mFragment.getActivity().startActivity(new Intent(mFragment.getActivity(), SearchActivity.class));
            }
        });
    }

    /**
     * 分享单个
     * 方法名  shareSpecialtyForBase64
     *
     * @param base64    图片为 base64格式  必传
     * @param shareType 需要分享滴type      必传<默认传"">     0 qq , 1 qq空间  2 朋友圈  3 微信  4微博
     * @param imgType   是否单个图片分享     必传<默认传"">     0 单个图片  1 网页
     * @param title     链接分享的标题      必传<默认传"">
     * @param content   链接分享的内容      必传<默认传"">
     * @param head      链接分享的头像       必传<默认传"">
     */
    @JavascriptInterface
    public void shareSpecialtyForBase64(final String base64, int shareType, int imgType, final String title, final String content, final String head) {
        MyLog.i("WebJSHook", "shareSpecialtyForBase64");
        File file = getBitmapFile(base64);
        if (file == null && TextUtils.isEmpty(file.getPath())) {
            showText("分享失败");
        }
        shareSpecialty(title, content, shareType, imgType, file.getPath(), "");
    }


    /**
     * 分享单个
     * 方法名  shareSpecialtyForUrl
     *
     * @param url       图片为 网页链接  必传
     * @param shareType 需要分享滴type      必传<默认传"">     0 qq , 1 qq空间  2 朋友圈  3 微信  4微博
     * @param imgType   是否单个图片分享     必传<默认传"">     0 单个图片  1 网页
     * @param title     链接分享的标题      必传<默认传"">
     * @param content   链接分享的内容      必传<默认传"">
     * @param head      链接分享的头像       必传<默认传"">
     */
    @JavascriptInterface
    public void shareSpecialtyForUrl(final String url, int shareType, int imgType, final String title, final String content, final String head) {
        MyLog.i("WebJSHook", "shareWebpage");
        shareSpecialty(title, content, shareType, imgType, url, "");
    }


    /**
     * 单个分享
     *
     * @param title
     * @param content
     * @param shareType 0 qq , 1 qq空间  2 朋友圈  3 微信  4微博
     * @param imgType
     * @param url
     */
    private void shareSpecialty(final String title, final String content, final int shareType, final int imgType, final String url, final String head) {
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (shareType) {
                    case 0:
                        shareQQFriend(imgType == 0, url, title, content, head);
                        break;
                    case 1:
                        shareQQRoom(imgType == 0, url, title, content, head);
                        break;
                    case 2:
                        shareWeixinCircle(imgType == 0, url, title, content, head);
                        break;
                    case 3:
                        shareWeiXinFriend(imgType == 0, url, title, content, head);
                        break;
                    case 4:
                        shareSinaWeibo(imgType == 0, url, title, content, head);
                        break;
                    default:
                        break;
                }
                LoadingView.dismissDialog();
            }
        });
    }


    /**
     * 跳转浏览器
     */
    @JavascriptInterface
    public void startBrowser(final String url) {
        if (TextUtils.isEmpty(url)) return;
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse(url);
                intent.setData(uri);
                //指定特定浏览器
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                mFragment.getActivity().startActivity(intent);

            }
        });

    }

    /**
     * 调整商学院意见反馈
     *
     * @param articleTitle 文字标题
     * @param articleId    文字ID
     */
    @JavascriptInterface
    public void jsToOcFeedback(final String articleTitle, final String articleId) {
        MyLog.i("WebJSHook", "shareWebpage");
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent feedBackIt = new Intent(mFragment.getActivity(), AppFeedActivity.class);
                Bundle feedBackBundle = new Bundle();
                feedBackBundle.putString("title", "意见反馈");
                RequestCircleFeedBackBean article = new RequestCircleFeedBackBean();
                article.setArticleId(articleId);
                article.setTitle(articleTitle);
                feedBackBundle.putSerializable("circleFeedBackBean", article);
                feedBackBundle.putString("fragmentName", "CircleFeedBackFragment");
                feedBackIt.putExtras(feedBackBundle);

                mFragment.getActivity().startActivityForResult(feedBackIt, ShowWebActivity.REQUEST_COLLEGE_FEEDBACK);
            }
        });

    }

    /**
     * 调整商学院意见反馈(用户已提交反馈)
     *
     * @param articleTitle 文字标题
     * @param articleId    文字ID
     */
    @JavascriptInterface
    public void jsToFeedbackSumbit(final int FeedbackId) {
        MyLog.i("WebJSHook", "shareWebpage");
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                OpinionpReplyFragment.start(mFragment.getActivity(), FeedbackId);
            }
        });

    }

    /**
     * 和banner方式跳转
     */
    @JavascriptInterface
    public void startBannerInitiate(final String jsonString) {
        if (TextUtils.isEmpty(jsonString)) return;
        final ImageInfo imageInfo = MyGsonUtils.jsonToBean(jsonString, ImageInfo.class);
        imageInfo.setSuperType(1);
        if (imageInfo != null) {
            App.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    BannerInitiateUtils.gotoAction(mFragment.getActivity(), imageInfo);
                }
            });
        }
    }


    /**
     * app升级
     */
    @JavascriptInterface
    public void checkAppUpgrade(final String jsonString) {
        if (TextUtils.isEmpty(jsonString)) return;
        final AppUpgradeInfo data = MyGsonUtils.jsonToBean(jsonString, AppUpgradeInfo.class);
        if (data == null) {
            return;
        }
        String myVersionCode = AppUtil.getVersionCode(mFragment.getActivity());
        String isDebug = data.getStatus();
        if (!"1".equals(isDebug)) {
            int myVcode = Integer.parseInt(myVersionCode);
            String sevrVersionCode = data.getVersion();
            int sevrVcode = Integer.parseInt(sevrVersionCode);
            if (myVcode < sevrVcode) { //需要升级
                App.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        UpdateAppBean data1 = QianWenUpdateUtlis.getData(data);
                        if ("2".equals(data.getUpgradde())) {//是否强制升级 1:是，2：否，3：静默更新
                            //普通升级
                            data1.setConstraint(false);
                            QianWenUpdateUtlis.constraintUpdate(mFragment.getActivity(), data.getDownload(), data1);
                        } else if ("1".equals(data.getUpgradde())) {
                            //强制升级
                            data1.setConstraint(true);
                            QianWenUpdateUtlis.constraintUpdate(mFragment.getActivity(), data.getDownload(), data1);
                        } else {//3：静默更新
                            QianWenUpdateUtlis.onlyUpdateApp(mFragment.getActivity(), data.getDownload(), data1);
                        }
                    }
                });
            }
        }
    }

    private void startShare(final String url, final String title, final String content, final boolean isShareImg) {
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                LoadingView.dismissDialog();
                if (!LoginUtil.checkIsLogin(mFragment.getActivity())) {
                    return;
                }
                if (sharePopwindow != null) {
                    sharePopwindow.dismiss();
                }

                sharePopwindow = new SharePopwindow(mFragment.getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.weixinFriend: //分享到好友
                                if (!AppUtil.isWeixinAvilible(mFragment.getActivity())) {
                                    ViewShowUtils.showShortToast(mFragment.getActivity(), "请先安装微信客户端");
                                    return;
                                }
                                shareWeiXinFriend(isShareImg, url, title, content, "");
                                break;
                            case R.id.weixinCircle: //分享到朋友圈
                                if (!AppUtil.isWeixinAvilible(mFragment.getActivity())) {
                                    ViewShowUtils.showShortToast(mFragment.getActivity(), "请先安装微信客户端");
                                    return;
                                }
                                shareWeixinCircle(isShareImg, url, title, content, "");
                                break;
                            case R.id.qqFriend: //分享到QQ
                                if (!AppUtil.isQQClientAvailable(mFragment.getActivity())) {
                                    ViewShowUtils.showShortToast(mFragment.getActivity(), "请先安装QQ客户端");
                                }
                                shareQQFriend(isShareImg, url, title, content, "");
                                break;
                            case R.id.qqRoom: //分享到QQ空间
                                if (!AppUtil.isQQClientAvailable(mFragment.getActivity())) {
                                    ViewShowUtils.showShortToast(mFragment.getActivity(), "请先安装QQ客户端");
                                    return;
                                }
                                shareQQRoom(isShareImg, url, title, content, "");
                                break;
                            case R.id.sinaWeibo: //分享到新浪微博
                                shareSinaWeibo(isShareImg, url, title, content, "");
                                break;

                        }
                        sharePopwindow.dismiss();
                    }

                });
                sharePopwindow.showAtLocation(mFragment.getView().findViewById(R.id.webview), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    // qq
    private void shareQQFriend(boolean isShareImg, String url, String title, String content, final String head) {
        if (isShareImg) {
            ShareUtil.shareImg(mFragment.getActivity(), url, ShareUtil.QQType);
        } else {
            String sHead = getHead(head);
            ShareUtil.App.toQQFriend(mFragment.getActivity(), title, content, "", url, null);
        }
    }



    //qq 空间
    private void shareQQRoom(boolean isShareImg, String url, String title, String content, final String head) {
        if (isShareImg) {
            ShareUtil.shareImg(mFragment.getActivity(), url, ShareUtil.QQZoneType);
        } else {
            String sHead = getHead(head);
            ShareUtil.App.toQQRoom(mFragment.getActivity(), title, content, "", url, null);
        }
    }

    //微博
    private void shareSinaWeibo(boolean isShareImg, String url, String title, String content, final String head) {
        if (isShareImg) {
            ShareUtil.shareImg(mFragment.getActivity(), url, ShareUtil.WeiboType);
        } else {
            String sHead = getHead(head);
            ShareUtil.App.toSinaWeibo(mFragment.getActivity(), title, content, sHead, url, null);
        }
    }


    // 朋友圈
    private void shareWeixinCircle(boolean isShareImg, final String url, final String title, final String content, final String head) {
        if (isShareImg) {
            ShareUtil.shareImg(mFragment.getActivity(), url, ShareUtil.WeMomentsType);
        } else {
            String sHead = getHead(head);
            ShareUtil.App.toWechatMoments(mFragment.getActivity(), title, content, "", url, null);
        }
    }

    // 微信朋友
    private void shareWeiXinFriend(boolean isShareImg, final String url, final String title, final String content, final String head) {
        if (isShareImg) {
            ShareUtil.shareImg(mFragment.getActivity(), url, ShareUtil.WechatType);
        } else {
            String sHead = getHead(head);
            ShareUtil.App.toWechatFriend(mFragment.getActivity(), title, content, "", url, null);
        }
    }


    private String getHead(String head) {
        if (!TextUtils.isEmpty(head)) return head;
        UserInfo userInfo = UserLocalData.getUser(mFragment.getActivity());
        return userInfo.getHeadImg();

    }
    /**
     * 返回账号注销提示
     */
    private void getAccountDestroyHint() {
        RxHttp.getInstance().getCommonService().getRtnInfo()
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        if (!TextUtils.isEmpty(data.getSysValue())) {
                            mAccountDestroyHit = data.getSysValue();
                            openAccountDestroyDialog(data.getSysValue());
                        }
                    }
                });

    }

    private void openAccountDestroyDialog(String data) {  //注销确认弹窗
        if (TextUtils.isEmpty(data)) return;
        ClearSDdataDialog  mAccountDestroyDialog = new ClearSDdataDialog(mFragment.getActivity(), R.style.dialog, "提示", data, new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                destroyAccount();
            }
        });
        mAccountDestroyDialog.setOkTextAndColor(R.color.color_FF4848, "确认注销");
        mAccountDestroyDialog.setCancelTextAndColor(R.color.color_808080, "再想想");
        mAccountDestroyDialog.show();
    }

    /**
     * 账号注销
     */
    private void destroyAccount() {
        InputVerificationCodeDialog mInputVerificationCodeDialog = new InputVerificationCodeDialog((RxAppCompatActivity) mFragment.getActivity());
        mInputVerificationCodeDialog.show();

    }
    /**
     * 升级掌柜黑金的弹框
     */
    private void updateGrade2() {
        ShopkeeperUpgradeDialog2 upgradeDialog = new ShopkeeperUpgradeDialog2(mFragment.getActivity());
        upgradeDialog.setmOkListener(new ShopkeeperUpgradeDialog2.OnOkListener() {
            @Override
            public void onClick(View view) {
                    updateGradePresenter(mFragment, Integer.parseInt(C.UserType.operator));
            }
        });
        upgradeDialog.getWindow().setDimAmount(0.7f);
        upgradeDialog.show();

    }

    /**
     * 升级
     *  @param fragment
     * @param userType*/
    public void updateGradePresenter(Fragment fragment, int userType) {
        CommInterface.updateUserGrade((RxFragment) fragment, userType)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {


                    }
                })
                .subscribe(new DataObserver<UpdateInfoBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        super.onError(errorMsg, errCode);
                        showError(errCode, errorMsg);
                    }

                    @Override
                    protected void onDataListEmpty() {

                    }

                    @Override
                    protected void onSuccess(UpdateInfoBean data) {
                        onGradeSuccess(data);
                    }
                });
    }
    public void showError(String errorNo, String msg) {
        MyLog.i("test", "onFailure: " + this);
        if ("B1100007".equals(errorNo)
                || "B1100008".equals(errorNo)
                || "B1100009".equals(errorNo)
                || "B1100010".equals(errorNo)) {
            ViewShowUtils.showShortToast(mFragment.getActivity(), msg);
        }


    }
    public void onGradeSuccess(UpdateInfoBean info) {
        if (info != null) {
            UserInfo userInfo = UserLocalData.getUser();
            userInfo.setUserType(String.valueOf(info.getUserType()));
            userInfo.setMoreCoin(info.getMoreCoin());
            UserLocalData.setUser(mFragment.getActivity(), userInfo);
            EventBus.getDefault().post(new RefreshUserInfoEvent());
            EventBus.getDefault().post(new MessageEvent(EventBusAction.UPGRADE_SEHNGJI));
//            refreshUserInfo(userInfo);

                ShopkeeperUpgradeDialog3 shopkeeperUpgradeDialog3 = new ShopkeeperUpgradeDialog3(mFragment.getActivity(), 2);
                shopkeeperUpgradeDialog3.getWindow().setDimAmount(0.7f);
                shopkeeperUpgradeDialog3.show();

            } else {
                MyLog.d("test", "用户信息为空");
            }

        }

    }




