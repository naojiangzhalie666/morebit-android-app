package com.zjzy.morebit.goodsvideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.blankj.utilcode.util.ToastUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.VideoShareDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.SdDirPath;
import com.zjzy.morebit.fragment.CircleDayHotFragment;
import com.zjzy.morebit.goodsvideo.VideoActivity;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.MarkermallCircleItemInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.TKLBean;

import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DownloadManage;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.WechatUtil;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.CommercialShareDialog;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/*
*
* 抖货视频adapter
* */
public class VideoDouAdapter extends RecyclerView.Adapter<VideoDouAdapter.ViewHolder> {
    private Context context;
    private List<ShopGoodInfo> data;
    private TKLBean mTKLBean;
    private CommercialShareDialog shareDialog;
    private FileDownloadListener downloadListener  = createLis();
    private String path,template ;
    List<ImageInfo> indexbannerdataArray = new ArrayList<>();

    public VideoDouAdapter(Context context,List<ShopGoodInfo> data) {
        this.context = context;
        this.data=data;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View inflate = LayoutInflater.from(context).inflate(R.layout.item_videodou, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ShopGoodInfo mGoodsInfo=data.get(position);

       // mGoodsInfo.setPrice(String.valueOf(MathUtils.sum(Double.valueOf(mGoodsInfo.getItemPrice()),Double.valueOf(mGoodsInfo.getCouponMoney()))));

//        indexbannerdataArray.clear();
//        ImageInfo imageInfo = new ImageInfo();
//        imageInfo.setThumb(mGoodsInfo.getItemPic());
//        indexbannerdataArray.add(imageInfo);
//        mGoodsInfo.setAdImgUrl(indexbannerdataArray);
//        if (mGoodsInfo.getColler()==0){
//            holder.img_collect.setImageResource(R.mipmap.video_unselect);
//        }else{
//            holder.img_collect.setImageResource(R.mipmap.video_select);
//        }
        LoadImgUtils.loadingCornerTop(context, holder.first_img,mGoodsInfo.getItemVideoPic(),0);
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//监听视频是否渲染
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                            holder.first_img.setVisibility(View.GONE);
                        }

                        return true;
                    }
                });
            }
        });
                //监听视频播放完的代码
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.videoView.isPlaying()) {
                    holder.videoView.pause();
                    holder.img_stop.setVisibility(View.VISIBLE);
                } else {
                    holder.videoView.start();
                    holder.img_stop.setVisibility(View.GONE);
                }
            }
        });
       /* LoadImgUtils.loadingCornerBitmap(context, holder.iv_head, mGoodsInfo.getItemPic());
        holder.tv_title.setText(mGoodsInfo.getItemTitle());
        holder.tv_price.setText(mGoodsInfo.getCouponMoney() + "元劵");
        holder.tv_num.setText("销量：" + mGoodsInfo.getItemSale());
        String itemPrice = mGoodsInfo.getItemPrice();
        holder.tv_coupon_price.setText(mGoodsInfo.getItemVoucherPrice() + "");
        UserInfo userInfo1 = UserLocalData.getUser();
        if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
            holder.tv_subsidy.setText("登录赚佣金");
        } else {
            if (C.UserType.operator.equals(UserLocalData.getUser(context).getPartner())
                    || C.UserType.vipMember.equals(UserLocalData.getUser(context).getPartner())) {
                holder.tv_subsidy.setText("预估收益" + MathUtils.getMuRatioComPrice(UserLocalData.getUser(context).getCalculationRate(), mGoodsInfo.getTkMoney() + "") + "元");
            }
        }


        holder.tv_buy.setOnClickListener(new View.OnClickListener() {//购买
            @Override
            public void onClick(View v) {
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) context);
                } else {
                    if (isGoodsLose(mGoodsInfo)) return;

                    if (mTKLBean == null) {
                        LoadingView.showDialog(context, "");
                        GoodsUtil.getTaoKouLing((RxAppCompatActivity) context, mGoodsInfo, new MyAction.OnResult<TKLBean>() {
                            @Override
                            public void invoke(TKLBean arg) {
                                mTKLBean = arg;
                            }

                            @Override
                            public void onError() {
                            }
                        });
                    } else {
                        ShareMoneyActivity.start((Activity) context, mGoodsInfo, mTKLBean);
                    }
                }
            }
        });

        holder.tv_share.setOnClickListener(new View.OnClickListener() {//分享
            @Override
            public void onClick(View v) {
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) context);
                } else {
                    GoodsUtil.getCouponInfo((RxAppCompatActivity) context, mGoodsInfo);
                }

            }
        });*/

        holder.ll_share.setOnClickListener(new View.OnClickListener() {//分享
            @Override
            public void onClick(View v) {
                getTemplate(mGoodsInfo);
            }
        });

        holder.ll_xia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download(mGoodsInfo.getItemVideo());
            }
        });


    }



    private void mShare(final String itemVideo) {
        if (!LoginUtil.checkIsLogin((Activity) context)) {
            return;
        }
        if (TaobaoUtil.isAuth()) {//淘宝授权
            TaobaoUtil.getAllianceAppKey((BaseActivity) context);
            return;
        }
          shareDialog = new CommercialShareDialog(context, new View.OnClickListener() {//分享
            @Override
            public void onClick(View v) {
                if (path!=null){
                    switch (v.getId()) {
                        case R.id.weixinFriend: //分享到好友
                            AppUtil.coayTextPutNative((Activity) context,template);
                            ShareUtil.shareWxFile(context,new File(path));
                            break;
                        case R.id.weixinCircle: //分享到朋友圈
                            AppUtil.coayTextPutNative((Activity) context,template);
                            openWechat();
                            break;
                        case R.id.qqFriend: //分享到QQ
                        case R.id.qqRoom: //分享到QQ空间
                            AppUtil.coayTextPutNative((Activity) context,template);
                            openQQ();
                            break;
                        case R.id.sinaWeibo: //分享到新浪微博
                            openWeibo();
                            break;
                        default:
                            break;
                    }
                shareDialog.dismiss();
                }
            }

          });


        if (!shareDialog.isShowing()) {
            shareDialog.show();
        }
    }
    private void openApp(int sharePlatform) {
        switch (sharePlatform) {
            case ShareUtil.WechatType: //分享到微信好友
            case ShareUtil.WeMomentsType: //分享到朋友圈
                openWechat();
                break;
            case ShareUtil.QQType: //分享到QQ
            case ShareUtil.QQZoneType: //分享到QQ空间
                openQQ();
                break;
            case ShareUtil.WeiboType: //分享到新浪微博
                openWeibo();
                break;
            default:
                break;

        }

    }

    private void openWeibo() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://sendweibo?content=" + URLEncoder.encode("")));
        context.startActivity(intent);
    }

    private void openQQ() {
        if (!AppUtil.isQQClientAvailable(context)) {
            ViewShowUtils.showShortToast(context, "请先安装QQ客户端");
            return;
        }
//        ComponentName componet = new ComponentName("com.tencent.mobileqq");
//        Intent intent = new Intent();
//        intent.setComponent(componet);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
        context.startActivity(intent);
//        mContext.startActivity(intent);
    }

    private void openWechat() {
        if (!AppUtil.isWeixinAvilible(context)) {
            ViewShowUtils.showShortToast(context, "请先安装微信客户端");
            return;
        }
        Intent lan = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(lan.getComponent());
        context.startActivity(intent);
    }

    /**
     * 商品是否过期
     *
     * @return
     * @param mGoodsInfo
     */
    private boolean isGoodsLose(ShopGoodInfo mGoodsInfo) {
        if (!LoginUtil.checkIsLogin((Activity) context)) {
            return true;
        }
        if (AppUtil.isFastClick(200)) {
            return true;
        }
        if (mGoodsInfo == null) {
            return true;
        }
        if (TextUtils.isEmpty(mGoodsInfo.getPrice())) {
            ViewShowUtils.showLongToast(context, "商品已经过期，请联系管理员哦");
            return true;
        }
        return false;
    }
    public void getTemplate(final ShopGoodInfo mGoodsInfo) {

        LoadingView.showDialog(context, "请求中...");
        GoodsUtil.getGetTkObservable((RxAppCompatActivity) context, mGoodsInfo)
                .subscribe(new DataObserver<TKLBean>() {
                    @Override
                    protected void onSuccess(TKLBean data) {
                         template = data.getTemplate();
                        Log.e("ssss",template+"");
                        VideoShareDialog videoShareDialog = new VideoShareDialog(context, template, new VideoShareDialog.OnOkListener() {
                            @Override
                            public void onClick(View view) {
                                mShare(mGoodsInfo.getItemVideo());
                            }
                        });
                        download(mGoodsInfo.getItemVideo());
                        videoShareDialog.show();
                    }
                });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView closs, tv_title, tv_price, tv_subsidy, tv_num, tv_coupon_price, tv_buy, tv_share;
        private VideoView videoView;
        private ImageView iv_head, img_stop,img_share,img_collect,img_xia,first_img;
        private RelativeLayout r1;
        private LinearLayout ll_share,ll_xia,ll_collect;
        public ViewHolder(View itemView) {
            super(itemView);
            closs = (TextView) itemView.findViewById(R.id.closs);
            videoView = (VideoView) itemView.findViewById(R.id.videoView);//视频
            iv_head = (ImageView) itemView.findViewById(R.id.iv_head);//主图
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);//标题
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);//优惠券
            tv_subsidy = (TextView) itemView.findViewById(R.id.tv_subsidy);//预估收益
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);//销量
            tv_coupon_price = (TextView) itemView.findViewById(R.id.tv_coupon_price);//商品价格
            tv_buy = (TextView) itemView.findViewById(R.id.tv_buy);//立即购买
            tv_share = (TextView) itemView.findViewById(R.id.tv_share);//分享

            img_stop = (ImageView) itemView.findViewById(R.id.img_stop);
            r1 = (RelativeLayout) itemView.findViewById(R.id.r1);
            img_collect= itemView.findViewById(R.id.img_collect);//收藏
            img_share= itemView.findViewById(R.id.img_share);//分享
            img_xia= itemView.findViewById(R.id.img_xia);//下载
            first_img=itemView.findViewById(R.id.first_img);//视频第一帧图片
            ll_share=itemView.findViewById(R.id.ll_share);
            ll_xia=itemView.findViewById(R.id.ll_xia);
            ll_collect=itemView.findViewById(R.id.ll_collect);
        }
    }
    private void download(String videoUrl) {
        if (videoUrl == null) {
            return;
        }
        LoadingView.showDialog(context, context.getString(R.string.downloading_local));
        if (videoUrl != null) {
            //先获取视频总数量
            if (!StringsUtils.isEmpty(videoUrl) && videoUrl.contains("/")) {
                String fileName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.lastIndexOf("."));
                String suffix = videoUrl.substring(videoUrl.lastIndexOf(".") + 1, videoUrl.length());
                DownloadManage.getInstance().start(videoUrl, SdDirPath.IMAGE_CACHE_PATH + fileName + "." + suffix, downloadListener);
//                       mTasks.add(FileDownloader.getImpl().create(videoUrl).setPath(SdDirPath.IMAGE_CACHE_PATH+fileName+"."+suffix).setTag(i + 1));
            }
        }

    }
        private FileDownloadListener createLis () {
            return new FileDownloadListener() {

                @Override
                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    // 之所以加这句判断，是因为有些异步任务在pause以后，会持续回调pause回来，而有些任务在pause之前已经完成，
                    // 但是通知消息还在线程池中还未回调回来，这里可以优化
                    // 后面所有在回调中加这句都是这个原因
                    if (task.getListener() != downloadListener) {
                        return;
                    }
                }

                @Override
                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    if (task.getListener() != downloadListener) {
                        return;
                    }


                }



                @Override
                protected void completed(BaseDownloadTask task) {
                    if (task.getListener() != downloadListener) {
                        return;
                    }
                    LoadingView.dismissDialog();
                    DownloadManage.getInstance().refreshGallery(context, task.getPath());
                     path = task.getPath();
                    ToastUtils.showLong("视频已保存到本地");

                }

                @Override
                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    if (task.getListener() != downloadListener) {
                        return;
                    }
                }

                @Override
                protected void error(BaseDownloadTask task, Throwable e) {
                    MyLog.i("test", "error: " + e.toString());
                    if (task.getListener() != downloadListener) {
                        return;
                    }
                    LoadingView.dismissDialog();

                }

                @Override
                protected void warn(BaseDownloadTask task) {
                    MyLog.i("test", "warn: ");
                    if (task.getListener() != downloadListener) {
                        return;
                    }

                }
            };
        }


    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

}
