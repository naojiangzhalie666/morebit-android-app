package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.GoodsDetailForJdActivity;
import com.zjzy.morebit.Activity.GoodsDetailForPddActivity;
import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ShareDownloadDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CircleCopyBean;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.MarkermallCircleItemInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.ShareUrlListBaen;
import com.zjzy.morebit.pojo.request.RequestCircleShareBean;
import com.zjzy.morebit.pojo.request.RequestCircleShareCountBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.DownloadManage;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.utils.share.ShareMore;
import com.zjzy.morebit.view.CircleShareDialog;
import com.zjzy.morebit.view.CopyTextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*
 * 每日好货商品内容
 * */
public class GoodsDialyAdapter extends RecyclerView.Adapter<GoodsDialyAdapter.ViewHolder> {
    private Context context;
    private List<MarkermallCircleInfo> list = new ArrayList<>();
    private GridLayoutManager manager;
    private CircleShareDialog shareDialog;
    private FileDownloadListener downloadListener;
    private String mExtension = "";
    private String mPosterPicPath;
    private ArrayList<String> arrayList;
    private int shopType;
    private String itemSourceId;
    private String taourl;
    private MarkermallCircleInfo circleInfo;
    private int circletype;
    private int mDownloadCount = 0;
    private List<String> mlist;
    private  ShopGoodInfo goodInfo;
    private List<String>  picture=new ArrayList<>();
    private Bitmap shareEWMBitmap;


    public GoodsDialyAdapter(Context context, int circletype) {
        this.context = context;
        this.circletype = circletype;
    }

    public void setData(List<MarkermallCircleInfo> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }


    public void addData(List<MarkermallCircleInfo> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_dialy_goods, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (list == null && list.size() == 0) return;
        circleInfo = list.get(position);
        if (!TextUtils.isEmpty(circleInfo.getIcon())) {
            LoadImgUtils.setImgCircle(context, holder.userIcon, circleInfo.getIcon());
        }
        if (!TextUtils.isEmpty(circleInfo.getContent())) {
            if (circletype == 0) {
                holder.content.setText(circleInfo.getContent() + "  ");
                holder.content.append(colorText("查看详情"));
            } else {
                holder.content.setText(circleInfo.getContent() + "  ");
            }

        }
        holder.tv_shart.setText(circleInfo.getShareCount() + "");
        if (!TextUtils.isEmpty(circleInfo.getCreateTime())) {
            holder.tv_time.setText(DateTimeUtils.getShortTime2(circleInfo.getCreateTime()) + "");
        }
        if (!TextUtils.isEmpty(circleInfo.getComment())) {
            if (circletype == 0) {
                holder.tv_comment.setText(circleInfo.getComment());
            } else {
                UserInfo usInfo = UserLocalData.getUser(context);

                if (usInfo == null || TextUtils.isEmpty(UserLocalData.getToken())) {
                    holder.tv_comment.setText(circleInfo.getComment() + "\n邀请ID:* * *");
                } else {
                    holder.tv_comment.setText(circleInfo.getComment() + "\n邀请ID:" + usInfo.getInviteCode());
                }

            }

        }
        if (!TextUtils.isEmpty(circleInfo.getName())) {
            holder.title.setText(circleInfo.getName());
        }
        arrayList = new ArrayList<>();
        List<MarkermallCircleItemInfo> items = circleInfo.getShareRangItems();
        for (int i = 0; i < items.size(); i++) {
            String picture = items.get(i).getPicture();
            arrayList.add(picture);
        }



        if (arrayList == null || arrayList.size() == 0)
            return;

        if (arrayList.size() == 4 || arrayList.size() == 2) {
            manager = new GridLayoutManager(context, 2);
        } else {
            manager = new GridLayoutManager(context, 3);
        }
        holder.rcy_photo.setLayoutManager(manager);
        //设置图标的间距
        if (holder.rcy_photo.getItemDecorationCount() == 0) {//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
            holder.rcy_photo.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(context, 6)));
        }
        PhotoDialyAdapter photoDialyAdapter = new PhotoDialyAdapter(context, arrayList, circleInfo, circletype);
        holder.rcy_photo.setAdapter(photoDialyAdapter);


        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<ShopGoodInfo> goods = list.get(position).getGoods();
                for (ShopGoodInfo info : goods) {
                    shopType = info.getShopType();
                    info.setGoodsId(Long.parseLong(info.getItemSourceId()));
                    getTao3(shopType, info, position);

                }
            }
        });


        holder.tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ShopGoodInfo> goods = list.get(position).getGoods();
                if (circletype==0){
                    for (ShopGoodInfo info : goods) {
                        shopType = info.getShopType();
                        switch (shopType) {
                            case 1://淘宝
                                getTao(shopType, info.getItemSourceId(), position);
                                break;
                            case 2://天猫
                                getTao(shopType, info.getItemSourceId(), position);

                                break;
                            case 3://pdd
                                getTao(shopType, info.getItemSourceId(), position);
                                break;
                            case 4://jd
                                getTao(shopType, info.getItemSourceId(), position);
                                break;
                            default:
                                break;

                        }
                    }
                }else{
                    AppUtil.coayTextPutNative((Activity) context, holder.tv_comment.getText().toString());
                    ViewShowUtils.showShortToast(context, context.getString(R.string.coayTextSucceed));
                    //跳转微信
                    PageToUtil.goToWeixin(context);
                }


            }
        });


        holder.tv_shart.setOnClickListener(new View.OnClickListener() {
            @Override //
            public void onClick(View view) {
                if (!LoginUtil.checkIsLogin((Activity) context)) return;
                if (TaobaoUtil.isAuth()) {   //淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) context);
                } else {
                    if (AppUtil.isFastClick(1000)) return;

                    List<ShopGoodInfo> goods = list.get(position).getGoods();
                    if (circletype==0){
                        for (ShopGoodInfo info : goods) {
                            goodInfo=new ShopGoodInfo();
                            goodInfo.setCouponPrice(info.getCouponPrice());
                            goodInfo.setVoucherPrice(info.getItemVoucherPrice());
                            goodInfo.setPrice(info.getItemprice());
                            goodInfo.setSaleMonth(info.getSaleMonth());
                            goodInfo.setShopType(info.getShopType());
                            goodInfo.setTitle(info.getItemTitle());
                            goodInfo.setItemPicture(info.getItemPicture());
                            goodInfo.setItemSourceId(info.getItemSourceId());
                            shopType = info.getShopType();
                            switch (shopType) {
                                case 1://淘宝
                                    getTao2(shopType, info.getItemSourceId(), position,goodInfo);
                                    break;
                                case 2://天猫
                                    getTao2(shopType, info.getItemSourceId(), position, goodInfo);

                                    break;
                                case 3://pdd
                                    getTao2(shopType, info.getItemSourceId(), position, goodInfo);
                                    break;
                                case 4://jd
                                    getTao2(shopType, info.getItemSourceId(), position, goodInfo);
                                    break;
                            }
                        }
                    }else{
                        picture.clear();
                        List<MarkermallCircleItemInfo> shareRangItems = list.get(position).getShareRangItems();
                        if (shareRangItems.size() == 0 || shareRangItems == null) return;
                        for (int i = 0; i < shareRangItems.size(); i++) {
                            picture.add(shareRangItems.get(i).getPicture());//取出图片
                        }
                        showChoosePicDialog((Activity) context, circleInfo, position,picture);
                    }


                }
            }
        });


    }

    private void getTao(final int shopType, String itemSourceId, final int position) {

        RequestCircleShareBean bean = new RequestCircleShareBean();
        bean.setItemId(itemSourceId);
        bean.setType(shopType);
        RxHttp.getInstance().getGoodsService()
                .getGoodsPurchaseLink(bean)
                .compose(RxUtils.<BaseResponse<CircleCopyBean>>switchSchedulers())

                .subscribe(new DataObserver<CircleCopyBean>() {
                    @Override
                    protected void onSuccess(CircleCopyBean data) {
                        if (data != null) {

                            String comment = list.get(position).getComment();
                            if (!TextUtils.isEmpty(comment)) {
                                String quStr = comment.substring(comment.indexOf("{") + 1, comment.indexOf("}"));
                                Log.e("sfsdfsd", quStr + "");
                                String replace = "";
                                if (!TextUtils.isEmpty(data.getTkl())) {
                                    replace = comment.replace(quStr, data.getTkl() + "");
                                } else {
                                    replace = comment.replace(quStr, data.getPurchaseLink() + "");
                                }
                                AppUtil.coayTextPutNative((Activity) context, replace);
                                ViewShowUtils.showShortToast(context, context.getString(R.string.coayTextSucceed));
                                //跳转微信
                                PageToUtil.goToWeixin(context);

                            }


                        }

                    }
                });
    }


    private void getTao2(final int shopType, String itemSourceId, final int position, final ShopGoodInfo goodInfo) {

        RequestCircleShareBean bean = new RequestCircleShareBean();
        bean.setItemId(itemSourceId);
        bean.setType(shopType);
        RxHttp.getInstance().getGoodsService()
                .getGoodsPurchaseLink(bean)
                .compose(RxUtils.<BaseResponse<CircleCopyBean>>switchSchedulers())

                .subscribe(new DataObserver<CircleCopyBean>() {
                    @Override
                    protected void onSuccess(CircleCopyBean data) {
                        if (data != null) {

                            String comment = list.get(position).getComment();
                            if (!TextUtils.isEmpty(comment)) {
                                String quStr = comment.substring(comment.indexOf("{") + 1, comment.indexOf("}"));
                                Log.e("sfsdfsd", quStr + "");
                                String replace = "";
                                if (!TextUtils.isEmpty(data.getTkl())) {
                                    replace = comment.replace(quStr, data.getTkl() + "");
                                    goodInfo.setCouponUrl(data.getTkl());
                                    getShareEWMBitmap(goodInfo,position);

                                } else {
                                    replace = comment.replace(quStr, data.getPurchaseLink() + "");
                                    shareEWMBitmap = GoodsUtil.getShareEWMBitmap((Activity) context, data.getPurchaseLink());
                                    if (shareEWMBitmap==null)return;
                                    Log.e("sfdf",shareEWMBitmap+"");
                                    getPoster(position,shareEWMBitmap);//第一张图生成海报
                                }
                                AppUtil.coayTextPutNative((Activity) context, replace);
                                ViewShowUtils.showShortToast(context, context.getString(R.string.coayTextSucceed));






                            }

                            }




                    }
                });
    }

    private void getTao3(final int shopType, final ShopGoodInfo info, final int position) {

        RequestCircleShareBean bean = new RequestCircleShareBean();
        bean.setItemId(info.getItemSourceId());
        bean.setType(shopType);
        RxHttp.getInstance().getGoodsService()
                .getGoodsPurchaseLink(bean)
                .compose(RxUtils.<BaseResponse<CircleCopyBean>>switchSchedulers())

                .subscribe(new DataObserver<CircleCopyBean>() {
                    @Override
                    protected void onSuccess(CircleCopyBean data) {

                        switch (shopType) {
                            case 1://淘宝
                                GoodsUtil.checkGoods((RxAppCompatActivity) context, info.getItemSourceId(), new MyAction.One<ShopGoodInfo>() {
                                    @Override
                                    public void invoke(ShopGoodInfo arg) {
                                        MyLog.i("test", "arg: " + arg);
                                        GoodsDetailActivity.start(context, arg);

                                    }
                                });
                                break;
                            case 2://天猫
                                GoodsUtil.checkGoods((RxAppCompatActivity) context, info.getItemSourceId(), new MyAction.One<ShopGoodInfo>() {
                                    @Override
                                    public void invoke(ShopGoodInfo arg) {
                                        MyLog.i("test", "arg: " + arg);
                                        GoodsDetailActivity.start(context, arg);
                                    }
                                });
                                break;
                            case 3://pdd
                                info.setItemSource("2");
                                GoodsDetailForPddActivity.start(context, info);
                                break;
                            case 4://jd
                                info.setItemSource("1");
                                GoodsDetailForJdActivity.start(context, info);
                                break;

                        }



                    }
                });
    }


    /**
     * 回去分享海报二维码
     * @param goodInfo
     * @param position
     */
    private void getShareEWMBitmap(ShopGoodInfo goodInfo, final int position) {


        ArrayList<ShopGoodInfo> shopGoodInfos = new ArrayList<>();
        shopGoodInfos.add(goodInfo);
        ShareMore.getShareGoodsUrlListObservable((RxAppCompatActivity) context, shopGoodInfos, goodInfo.getItemSourceId())
                .compose(RxUtils.<BaseResponse<ShareUrlListBaen>>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new DataObserver<ShareUrlListBaen>() {
                    @Override
                    protected void onSuccess(ShareUrlListBaen data) {
                        List<String> link = data.getLink();
                        if (link != null && link.size() != 0) {
                            shareEWMBitmap = GoodsUtil.getShareEWMBitmap((Activity) context, link.get(0));
                            if (shareEWMBitmap==null)return;
                            getPoster(position,shareEWMBitmap);//第一张图生成海报
                        }

                    }

                });


    }

    //生成海报
    private void getPoster(final int position, final Bitmap shareEWMBitmap) {
        picture.clear();
        List<MarkermallCircleItemInfo> shareRangItems = list.get(position).getShareRangItems();
        final List<String> poster = new ArrayList<>();

        if (shareRangItems.size() == 0 || shareRangItems == null) return;
        for (int i = 0; i < shareRangItems.size(); i++) {
            poster.add(shareRangItems.get(i).getPicture());//取出图片
        }
        if (poster.size() == 0 || poster == null) return;
        Glide.with(context)
                .asBitmap()
                .load(poster.get(0))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (resource!=null){
                            try {
                                poster.remove(0);
                                String s = GoodsUtil.saveGoodsImg((Activity) context, goodInfo, resource, shareEWMBitmap, "");
                                picture.add(s);
                                picture.addAll(poster);
                                showChoosePicDialog((Activity) context, circleInfo, position,picture);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });






    }





    private void showChoosePicDialog(Activity context, final MarkermallCircleInfo circleInfo, final int position, final List<String> picture) {
        shareDialog = new CircleShareDialog(context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getshareCount(list.get(position).getId());
                switch (v.getId()) {
                    case R.id.weixinFriend: //分享到好友
                        shareImg(ShareUtil.WechatType,picture);
                        break;
                    case R.id.weixinCircle: //分享到朋友圈
                        startSave(picture);
                        // shareImg(ShareUtil.WeMomentsType, circleInfo, position);
                        break;
                    case R.id.qqFriend: //分享到QQ
                        shareImg(ShareUtil.QQType, picture);
                        break;
                    case R.id.qqRoom: //分享到QQ空间
                        shareImg(ShareUtil.QQZoneType,picture);
                        break;
                    case R.id.plct: //批量存图
                        startSave(picture);
                        break;
                    default:
                        break;

                }

                shareDialog.dismiss();
            }
        });

        if (!shareDialog.isShowing()) {
            shareDialog.show();
        }
    }

    private void getshareCount(int id) {
        try {
            RequestCircleShareCountBean requestBean = new RequestCircleShareCountBean();
            requestBean.setId(id + "");
            String sign = EncryptUtlis.getSign2(requestBean);
            requestBean.setSign(sign);

            RxHttp.getInstance().getOrdersService()
                    .getMarkermallShareCount(requestBean)
                    .compose(RxUtils.<BaseResponse>switchSchedulers())
                    .subscribe(new Consumer<BaseResponse>() {
                        @Override
                        public void accept(BaseResponse response) throws Exception {
                            notifyDataSetChanged();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* 分享图片,
     *
     * @param picture
     */
    protected void shareImg(final int sharePlatform, final List<String> picture) {


        LoadingView.showDialog(context, "");
        Observable.just(picture)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, Map<String, File>>() {
                    @Override
                    public Map<String, File> apply(List<String> strings) throws Exception {
                        Map<String, File> map = new HashMap<>();
                        for (int i = 0; i < picture.size(); i++) {
                            final String s = picture.get(i);
                            if (TextUtils.isEmpty(s)) {
                                continue;
                            }
                            Bitmap bitmap = null;
                            File img = null;
                            try {
                                bitmap = LoadImgUtils.getImgBitmapOnIo1(context, s);
                                String pictureName = FileUtils.getPictureName(s);
                                img = GoodsUtil.saveBitmapToFile(context, bitmap, pictureName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (bitmap == null || img == null) continue;
                            map.put(s, img);
                        }
                        return map;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new CallBackObserver<Map<String, File>>() {
                    @Override
                    public void onNext(Map<String, File> map) {
                        ArrayList<File> urlList = new ArrayList();
                        for (String str : picture) {
                            if (!TextUtils.isEmpty(str)) {
                                File file = map.get(str);
                                if (file != null) {
                                    urlList.add(file);
                                }
                            }
                        }

                        if (urlList.size() != 0) {
                            if (sharePlatform == ShareUtil.WeMomentsType && urlList.size() > 1) {
                                ViewShowUtils.showShortToast(context, context.getString(R.string.save_succeed));
                            } else {
                                GoodsUtil.sysShareImage(context, urlList, sharePlatform);
                                LoadingView.dismissDialog();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });

    }


    private void startSave(List<String> picture) {


        if (AppUtil.isFastClick(500)) return;
        if (circleInfo == null) {
            return;
        }

        downloadListener = createLis();

        DownloadManage.getInstance().multitaskStart(picture, downloadListener);
    }

    private FileDownloadListener createLis() {
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
                //MyLog.i("test","completed: " +mDownloadCount);

                if (task.getListener() != downloadListener) {
                    return;
                }

                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), task.getPath(), task.getFilename(), null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mDownloadCount++;

                if (mDownloadCount == picture.size()) {
                    ToastUtils.showShort("图片已存到相册");
                    ShareDownloadDialog dialog = new ShareDownloadDialog(context, R.style.dialog);
                    dialog.show();
                    mDownloadCount = 0;
                }


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
                MyLog.i("test", "downloadListener: " + downloadListener);

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

    private Spanned colorText(String text) {
        return Html.fromHtml(String.format("<font color='#F05557'>%1$s</font>", text));
    }

    @Override
    public int getItemCount() {


        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private CopyTextView tv_shart, content, tv_time, tv_comment, tv_copy, title;
        private ImageView userIcon;
        private RecyclerView rcy_photo;


        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);//名字
            userIcon = itemView.findViewById(R.id.userIcon);//头像
            tv_shart = itemView.findViewById(R.id.tv_shart);//分享
            content = itemView.findViewById(R.id.content);//内容
            rcy_photo = itemView.findViewById(R.id.rcy_photo);//图片列表
            tv_time = itemView.findViewById(R.id.tv_time);//时间
            tv_comment = itemView.findViewById(R.id.tv_comment);//复制内容
            tv_copy = itemView.findViewById(R.id.tv_copy);//复制


        }
    }

    public static interface OnAddClickListener {

        public void onShareClick(int postion);
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }



}
