package com.zjzy.morebit.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.GoodsDetailForJdActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ShareDownloadDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CircleCopyBean;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.MarkermallCircleItemInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.ShareUrlListBaen;
import com.zjzy.morebit.pojo.request.RequestCircleShareBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.DownloadManage;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.share.ShareMore;
import com.zjzy.morebit.utils.sys.RequestPermissionUtlis;
import com.zjzy.morebit.view.CircleShareDialog;
import com.zjzy.morebit.view.CopyTextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*
 * 每日好货商品内容
 * */
public class GoodsDialyAdapter extends RecyclerView.Adapter<GoodsDialyAdapter.ViewHolder> {
    private Context context;
    private List<MarkermallCircleInfo> list = new ArrayList<>();
    private  GridLayoutManager manager;
    private    CircleShareDialog   shareDialog;
    private FileDownloadListener downloadListener;
    private String mExtension = "";
    private String mPosterPicPath;
    private   ArrayList<String> arrayList;
    private  int shopType;
    private  String itemSourceId;
    private String taourl;
    private    MarkermallCircleInfo circleInfo;
    private  int circletype;


    public GoodsDialyAdapter(Context context, int circletype) {
        this.context = context;
        this.circletype=circletype;
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
        if (list==null&&list.size()==0)return;
         circleInfo = list.get(position);
        if (!TextUtils.isEmpty(circleInfo.getIcon())) {
            LoadImgUtils.setImgCircle(context, holder.userIcon, circleInfo.getIcon());
        }
        if (!TextUtils.isEmpty(circleInfo.getContent())) {
            if (circletype==0){
                holder.content.setText(circleInfo.getContent()+"  ");
                holder.content.append(colorText("查看详情"));
            }else{
                holder.content.setText(circleInfo.getContent()+"  ");
            }

        }
        holder.tv_shart.setText(circleInfo.getShareCount() + "");
        if (!TextUtils.isEmpty(circleInfo.getCreateTime())) {
            holder.tv_time.setText(DateTimeUtils.getShortTime2(circleInfo.getCreateTime()) + "");
        }
        if (!TextUtils.isEmpty(circleInfo.getComment())) {
            if (circletype==0){
                holder.tv_comment.setText(circleInfo.getComment());
            }else{
                UserInfo usInfo = UserLocalData.getUser(context);

                if (usInfo == null || TextUtils.isEmpty(UserLocalData.getToken())) {
                    holder.tv_comment.setText(circleInfo.getComment()+"\n邀请ID:* * *");
                } else {
                    holder.tv_comment.setText(circleInfo.getComment()+"\n邀请ID:"+usInfo.getInviteCode());
                }

            }

        }
        if (!TextUtils.isEmpty(circleInfo.getName())){
            holder.title.setText(circleInfo.getName());
        }
        final List<ShopGoodInfo> goods = circleInfo.getGoods();

          arrayList = new ArrayList<>();
        List<MarkermallCircleItemInfo> items = circleInfo.getShareRangItems();
        for (int i=0;i<items.size();i++){
            String picture = items.get(i).getPicture();
            arrayList.add(picture);
        }
        if (arrayList==null||arrayList.size()==0)
            return;

            if (arrayList.size()==4 ||arrayList.size()==2){
                manager=new GridLayoutManager(context,2);
            }else{
                manager=new GridLayoutManager(context,3);
            }


        holder.rcy_photo.setLayoutManager(manager);
        //设置图标的间距
        if (holder.rcy_photo.getItemDecorationCount() == 0) {//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
            holder.rcy_photo.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(context, 6)));
        }
        PhotoDialyAdapter photoDialyAdapter=new PhotoDialyAdapter(context,arrayList,circleInfo);
        holder.rcy_photo.setAdapter(photoDialyAdapter);

        for (ShopGoodInfo info:goods){
           shopType =info.getShopType();
           itemSourceId = info.getItemSourceId();
            info.setGoodsId(Long.parseLong(itemSourceId));
           // getImg(0,arrayList,info);

        }



        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (ShopGoodInfo info : goods) {
                    switch (shopType){
                        case 1://淘宝
                            GoodsUtil.checkGoods((RxAppCompatActivity) context, itemSourceId, new MyAction.One<ShopGoodInfo>() {
                                @Override
                                public void invoke(ShopGoodInfo arg) {
                                    MyLog.i("test", "arg: " + arg);
                                    GoodsDetailActivity.start(context, arg);

                                }
                            });
                            break;
                        case 2://天猫
                            GoodsUtil.checkGoods((RxAppCompatActivity) context, itemSourceId, new MyAction.One<ShopGoodInfo>() {
                                @Override
                                public void invoke(ShopGoodInfo arg) {
                                    MyLog.i("test", "arg: " + arg);
                                    GoodsDetailActivity.start(context, arg);
                                }
                            });
                            break;
                        case 3://pdd
                            info.setItemSource("2");
                            GoodsDetailForJdActivity.start(context,info);
                            break;
                        case 4://jd
                            info.setItemSource("1");
                            GoodsDetailForJdActivity.start(context,info);
                            break;

                    }
                }
            }
        });



        holder.tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (shopType){
                    case 1://淘宝
                        getTao(shopType,itemSourceId);
                        break;
                    case 2://天猫
                        getTao(shopType,itemSourceId);

                        break;
                    case 3://pdd
                        getTao(shopType,itemSourceId);
                        break;
                    case 4://jd
                        getTao(shopType,itemSourceId);
                        break;

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
                  //  mTvShart = tv_shart;
                    AppUtil.coayText((Activity) context, circleInfo.getContent());
                    if ((circleInfo.getGoods() == null || circleInfo.getGoods().size() == 0) &&
                            (circleInfo.getShareRangItems() == null || circleInfo.getShareRangItems().size() == 0)&&(circleInfo.getPicture()==null||circleInfo.getPicture().size()==0)) {
                        ViewShowUtils.showShortToast(context, "文案复制成功");
                        return;
                    }
//                        if (mLoadType == CircleDayHotFragment.TypeImg) {
//                            //TODO
//                            share(item, mLoadType);
//                        } else {
//                        SensorsDataUtil.getInstance().mifenClickTrack(mMainTitle,mSubTitle,"发圈",position,"","","","","","","",1,"");
                    showChoosePicDialog((Activity) context, circleInfo);
//                        }
                }
            }
        });





    }

    private void getTao(int shopType, String itemSourceId) {

        RequestCircleShareBean bean = new RequestCircleShareBean();
        bean.setItemId(itemSourceId);
        bean.setType(shopType);
        RxHttp.getInstance().getGoodsService()
                .getGoodsPurchaseLink(bean)
                .compose(RxUtils.<BaseResponse<CircleCopyBean>>switchSchedulers())

                .subscribe(new DataObserver<CircleCopyBean>() {
                    @Override
                    protected void onSuccess(CircleCopyBean data) {
                        if (data!=null){
                            String comment = circleInfo.getComment();
                            String s = comment.replaceAll("\\{[^}]*\\}", data.getTkl());

                            AppUtil.coayTextPutNative((Activity) context,s);
                            ViewShowUtils.showShortToast(context, context.getString(R.string.coayTextSucceed));
                            //跳转微信
                            PageToUtil.goToWeixin(context);


                        }

                    }
                });
    }

    private void getImg(final int pos, final ArrayList<String> arrayList, final ShopGoodInfo goods) {
        RequestPermissionUtlis.requestOne((Activity) context, new MyAction.OnResult<String>() {

            @Override
            public void invoke(String arg) {
                final int index = pos;
                if (arrayList == null || arrayList.size() <= 0) {
                    return;
                }
//                if (TextUtils.isEmpty(taourl)) {
//                    ViewShowUtils.showShortToast(context, "淘口令不能为空");
//                    return;
//                }

                String finalDownPath =arrayList.get(0);
                Observable<Bitmap> flowable1 = LoadImgUtils.getImgToBitmapObservable(context, finalDownPath);
                Observable<Bitmap> flowable2 = getShareEWMBitmap(goods);

                Observable.zip(flowable1, flowable2, new BiFunction<Bitmap, Bitmap, String>() {
                    @Override
                    public String apply(Bitmap bitmap, Bitmap ewmBitmap) throws Exception {
                        MyLog.threadName();
                        if (bitmap == null) {
                            ViewShowUtils.showShortToast(context, "生成失败");
                            return null;
                        }
                        return GoodsUtil.saveGoodsImg((Activity) context, goods, bitmap, ewmBitmap, mExtension);
                    }
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new CallBackObserver<String>() {
                            @Override
                            public void onNext(String picPath) {
                                mPosterPicPath = picPath;
                                MyLog.threadName();
                                updatePoster(picPath, pos,arrayList);
                            }
                        });
            }

            @Override
            public void onError() {
                AppUtil.goSetting((Activity) context);
            }
        }, false, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});



    }

    public void updatePoster(String picPath, int finalIndex, ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.size() == 0 || TextUtils.isEmpty(picPath)) {
            return;
        }

       arrayList.remove(0);
        arrayList.add(0,picPath);

    }



    private void showChoosePicDialog(Activity context, final MarkermallCircleInfo circleInfo) {
            shareDialog = new CircleShareDialog(context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.weixinFriend: //分享到好友
                       // share(item, type, ShareUtil.WechatType);
                        break;
                    case R.id.weixinCircle: //分享到朋友圈
                       // share(item, type, ShareUtil.WeMomentsType);
                        break;
                    case R.id.qqFriend: //分享到QQ
                       // share(item, type, ShareUtil.QQType);
                        break;
                    case R.id.qqRoom: //分享到QQ空间
                       // share(item, type, ShareUtil.QQZoneType);
                        break;
                    case R.id.plct: //批量存图
                       // share(item, type, ShareUtil.WeiboType);
                        startSave(circleInfo.getShareRangItems());
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

    private void startSave(List<MarkermallCircleItemInfo> circleInfo) {


        if (AppUtil.isFastClick(500)) return;
        if (circleInfo == null ) {
            return;
        }

        downloadListener =createLis();


        DownloadManage.getInstance().multitaskStart(arrayList,downloadListener);
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

                ToastUtils.showShort("图片已存到相册");
                ShareDownloadDialog dialog=new ShareDownloadDialog(context,R.style.dialog);
                dialog.show();
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                MyLog.i("test","error: "+e.toString());

                if (task.getListener() != downloadListener) {
                    return;
                }
                MyLog.i("test","downloadListener: "+downloadListener);

            }

            @Override
            protected void warn(BaseDownloadTask task) {
                MyLog.i("test","warn: ");
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


        private CopyTextView tv_shart, content, tv_time, tv_comment, tv_copy,title;
        private ImageView userIcon;
        private RecyclerView rcy_photo;




        public ViewHolder(View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);//名字
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



    /**
     * 回去分享海报二维码
     * @param goods
     */
    private Observable<Bitmap> getShareEWMBitmap(ShopGoodInfo goods) {
        ArrayList<ShopGoodInfo> shopGoodInfos = new ArrayList<>();
        shopGoodInfos.add(goods);
        return ShareMore.getShareGoodsUrlListObservable((RxAppCompatActivity) context, shopGoodInfos, goods.getItemSourceId())
                .observeOn(Schedulers.io())
                .flatMap(new Function<BaseResponse<ShareUrlListBaen>, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(BaseResponse<ShareUrlListBaen> baseResponse) throws Exception {
                        MyLog.threadName();
                        ShareUrlListBaen data = baseResponse.getData();
                        if (data != null) {
                            mExtension = data.getExtension();
                            List<String> link = data.getLink();
                            if (link != null && link.size() != 0) {
                                return GoodsUtil.getShareEWMBitmapObservable((Activity) context, link.get(0));

                            }
                        }
                        return null;
                    }
                });





    }
}
