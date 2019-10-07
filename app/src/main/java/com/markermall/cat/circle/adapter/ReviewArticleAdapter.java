package com.markermall.cat.circle.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markermall.cat.Activity.ShowWebActivity;
import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.Module.common.Dialog.HintDialog;
import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.Module.push.Logger;
import com.markermall.cat.R;
import com.markermall.cat.adapter.SimpleAdapter;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.circle.ui.RecommendListActivity;
import com.markermall.cat.player.RecyclerItemNormalHolder;
import com.markermall.cat.pojo.Article;
import com.markermall.cat.pojo.MessageEvent;
import com.markermall.cat.utils.AppUtil;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.LoginUtil;
import com.markermall.cat.utils.MyLog;
import com.markermall.cat.utils.ShareUtil;
import com.markermall.cat.utils.ViewShowUtils;
import com.markermall.cat.view.CommercialShareDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import javax.net.ssl.SSLHandshakeException;

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

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class ReviewArticleAdapter extends SimpleAdapter<Article, SimpleViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private HintDialog mDialog;
    MyPlatformActionListener mMyPlatformActionListener;
    private int type = RecommendListActivity.ARTICLE_RECOMMEND;
    private boolean isHomeRecommend;
    public ReviewArticleAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            return new RecyclerItemNormalHolder(mContext,LayoutInflater.from(parent.getContext()).inflate(R.layout.list_video_item_normal, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutorial, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder simpleViewHolder, final int position) {
        final Article article = getItem(position);

        if (article == null) return;
        if (simpleViewHolder instanceof RecyclerItemNormalHolder) {
            RecyclerItemNormalHolder recyclerItemViewHolder = (RecyclerItemNormalHolder) simpleViewHolder;
            recyclerItemViewHolder.setRecyclerBaseAdapter(this);
//            recyclerItemViewHolder.onBind(position, article, EventBusAction.CIRCLE_LIST_VIDEO_PLAY,type,isHomeRecommend);
            recyclerItemViewHolder.setOnItemClickListener(new RecyclerItemNormalHolder.OnItemClickListener() {
                @Override
                public void onAuthority(View v) {
                    if (LoginUtil.checkIsLogin((Activity) mContext)) {
                        openDialog();
                    }
                }

                @Override
                public void onShare(View v) {
                    MyLog.i("test", "onShare");
                    if (LoginUtil.checkIsLogin((Activity) mContext)) {
                        openShareDialog(article.getTitle(), article.getShareContent(), article.getImage(), article.getShareUrl());
                    }

                }
            });

        } else if (simpleViewHolder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) simpleViewHolder;

            //通用都有
            if (!TextUtils.isEmpty(article.getImage())) {
                LoadImgUtils.setImg(mContext, viewHolder.ivShow, article.getImage());
            }
            if (!TextUtils.isEmpty(article.getReleaseTime())) {
                viewHolder.tvTime.setText(article.getReleaseTime() + "");
            } else {
                viewHolder.tvTime.setText("");
            }
            viewHolder.title.setText(article.getTitle());
//            viewHolder.tvTime.setText(article.getReleaseTime() + "");
            if( isHomeRecommend){
                if(position<=2){
                    viewHolder.iv_recommend.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.iv_recommend.setVisibility(View.GONE);
                }
            }  else {
                viewHolder.iv_recommend.setVisibility(View.GONE);
            }


            String finalViewCount = article.getRealView() + article.getVirtualView() + "";
            if (TextUtils.isEmpty(finalViewCount)) {
                viewHolder.tvCount.setText("");
            } else {
                viewHolder.tvCount.setText(article.getRealView() + article.getVirtualView() + "");
            }
            if(type == RecommendListActivity.ARTICLE_REVIEW){
                viewHolder.ll_share.setVisibility(View.GONE);
                viewHolder.ll_count_container.setVisibility(View.GONE);
            } else {
                viewHolder.ll_share.setVisibility(View.VISIBLE);
                viewHolder.ll_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LoginUtil.checkIsLogin((Activity) mContext)) {
                            openShareDialog(article.getTitle(), article.getShareContent(), article.getImage(), article.getShareUrl());
                        }

                    }
                });
                viewHolder.ll_count_container.setVisibility(View.VISIBLE);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    SensorsDataUtil.getInstance().mifenClickTrack("","","商学院",position,"","","","",article.getUrl(),article.getTitle(),"");
                    if (LoginUtil.checkIsLogin((Activity) mContext)) {
//                        sendStatistics(article,EventBusAction.CIRCLE_LIST_VIDEO_PLAY);
                        if (article.getType() == 1) {
                            //文章类型
                            if (article != null && article.getAuthority() == 1) {
                                if (!TextUtils.isEmpty(article.getUrl())) {
                                    Logger.e("==TutorialAdapter 条目点击==" + article.getAuthority());
                                    article.setuType(type);
                                    ShowWebActivity.start((Activity) mContext, article.getUrl() + "?id=" + article.getId()+"&status="+article.getStatus(), article.getTitle(),article);
                                }
                            } else {
                                openDialog();
                            }
                        } else if(article.getType()==3){
                            ShowWebActivity.start((Activity) mContext, article.getLinkUrl(), article.getTitle());
                        }
                    }
                }
            });
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getType() == 2) {
            return 2;
        }
        return 1;
    }


    private class ViewHolder extends SimpleViewHolder {

        ImageView ivShow;
        LinearLayout ll_share;
        TextView tvTime;
        TextView title;
        TextView tvCount;
        CardView mCardViewArticle;
        ImageView iv_recommend;
        LinearLayout ll_count_container;
        public ViewHolder(View itemView) {
            super(itemView);
            ivShow = itemView.findViewById(R.id.iv_show);
            ll_share = itemView.findViewById(R.id.ll_share);
            tvTime = itemView.findViewById(R.id.tv_time);
            title = itemView.findViewById(R.id.title);
            tvCount = itemView.findViewById(R.id.tv_see_count);
            iv_recommend = itemView.findViewById(R.id.iv_recommend);
            mCardViewArticle = itemView.findViewById(R.id.card_view_article);
            ll_count_container = itemView.findViewById(R.id.ll_count_container);
        }
    }

    private void openDialog() {
        if (mDialog == null) {
            // 0是消费者，1是代理商，2是运营专员
            String toastMsg = getHintString();
            mDialog = new HintDialog(mContext, R.style.dialog, "提示", toastMsg);
        }
        String toastMsg = getHintString();
        mDialog.setContentText(toastMsg);
        mDialog.show();
    }

    @NonNull
    private String getHintString() {
        String toastMsg = "";
        if (C.UserType.member.equals(UserLocalData.getUser(mContext).getPartner())) {
            toastMsg = mContext.getString(R.string.dialog_article_vip_hint);
        } else if (C.UserType.vipMember.equals(UserLocalData.getUser(mContext).getPartner())) {
            toastMsg = mContext.getString(R.string.dialog_article_operator_hint);
        } else {
            toastMsg = mContext.getString(R.string.dialog_article_drm_hint); // 默认提示
        }
        return toastMsg;
    }

    private void openShareDialog(final String title, final String text, final String imageUrl, final String shareUrl) {

        CommercialShareDialog shareDialog = new CommercialShareDialog(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.weixinFriend: //分享到好友
                        if (!AppUtil.isWeixinAvilible(mContext)) {
                            ViewShowUtils.showShortToast(mContext, "请先安装微信客户端");
                            return;
                        }
                        LoadingView.showDialog(mContext);
                        toShareFriends(1, title, text, imageUrl, shareUrl);
                        break;
                    case R.id.weixinCircle: //分享到朋友圈
                        if (!AppUtil.isWeixinAvilible(mContext)) {
                            ViewShowUtils.showShortToast(mContext, "请先安装微信客户端");
                            return;
                        }
                        LoadingView.showDialog(mContext);
                        toShareFriends(2, title, text, imageUrl, shareUrl);
                        break;
                    case R.id.qqFriend: //分享到QQ
                        LoadingView.showDialog(mContext);
                        toShareFriends(3, title, text, imageUrl, shareUrl);
                        break;
                    case R.id.qqRoom: //分享到QQ空间
                        LoadingView.showDialog(mContext);
                        toShareFriends(4, title, text, imageUrl, shareUrl);
                        break;
                    case R.id.sinaWeibo: //分享到新浪微博
                        LoadingView.showDialog(mContext);
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

    private void sendStatistics(Article article, String action) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setAction(action);
        messageEvent.setId(article.getId());
        messageEvent.setMsg(article.getTwoLevel() + "");
        EventBus.getDefault().post(messageEvent);
    }
    /**
     * 海报分享
     *
     * @param type
     */
    private void toShareFriends(int type, String title, String text, String imageUrl, String shareUrl) {

        if (mMyPlatformActionListener == null) {
            mMyPlatformActionListener = new MyPlatformActionListener(mContext);
        }
        if (type == 1) {    //微信好友
            ShareUtil.App.toWechatFriend((Activity) mContext, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 2) {  //分享到朋友圈
            ShareUtil.App.toWechatMoments((Activity) mContext, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 3) {  //分享到qq好友
            ShareUtil.App.toQQFriend((Activity) mContext, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 4) {  //qq空间

            ShareUtil.App.toQQRoom((Activity) mContext, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 5) {  //新浪微博
            ShareUtil.App.toSinaWeibo((Activity) mContext, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
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
            ViewShowUtils.showShortToast(com.markermall.cat.App.getAppContext(), "分享成功");
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
            ViewShowUtils.showShortToast(com.markermall.cat.App.getAppContext(), "分享取消");
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setHomeRecommend(boolean homeRecommend) {
        isHomeRecommend = homeRecommend;
    }
}
