package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.SdDirPath;
import com.zjzy.morebit.fragment.CircleDayHotFragment;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.MarkermallCircleItemInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.CopyPropertiesUtil;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.DensityUtil;
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
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.CollectBtn;
import com.zjzy.morebit.view.CommercialShareDialog;
import com.zjzy.morebit.view.FixRecyclerView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 */
public class MarkermallCircleAdapter extends BaseMYShareAdapter<MarkermallCircleAdapter.CommonViewHolder> {
    public static final int DISPLAY_CIRCLE_INFO = 0; //显示发圈
    public static final int DISPLAY_RECOMMOND = 1; //1/显示为你推荐
    private final int mLoadType;
    private LayoutInflater mInflater;

    private List<MarkermallCircleInfo> mDataList = new ArrayList<>();
    private int imgPadding = 5;
    private int imgWidth = 0;
    private int mWindowWidth3 = 0;
    public static int mShareCountId = 0;
    private TextView mTvShart;
    CommercialShareDialog shareDialog;
    private FileDownloadListener downloadListener;
    private int mDownloadSize;
    private int mCurDownloadSize;
    private String mMainTitle, mSubTitle;
    private MyAction.Two<MarkermallCircleInfo,Integer> mCollectAction;
    private MyAction.Two<MarkermallCircleInfo,Integer> mbrandCollectAction;
    CircleBrandsAdapter circleBrandsAdapter;
    public MarkermallCircleAdapter(Context context, int loadType) {
        super();
        WindowManager wm1 = ((Activity) context).getWindowManager();
        int mWindowWidth = wm1.getDefaultDisplay().getWidth();
        mWindowWidth3 = mWindowWidth ;
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.circle_padding);
        int listPaddingSizeLeft = context.getResources().getDimensionPixelSize(R.dimen.circle_list_padding_left);
        int listPaddingSizeRight = context.getResources().getDimensionPixelSize(R.dimen.circle_list_padding_right);
        int i = mWindowWidth - dimensionPixelSize * 2 - listPaddingSizeLeft - listPaddingSizeRight - imgPadding * 4;
        imgWidth = i / 3;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mLoadType = loadType;
        initDownload();
    }

    private void initDownload() {
//        FileDownloader.setup(mContext);
        downloadListener = createLis();
//        mQueueSet = new FileDownloadQueueSet(downloadListener);
//        mTasks = new ArrayList<>();
    }


    public List<MarkermallCircleInfo> getDatas(){
        return mDataList;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        try {
            if(getItemViewType(position) == DISPLAY_RECOMMOND){
                BrandViewHolder brandViewHolder = (BrandViewHolder) holder;
                brandViewHolder.bind(brandViewHolder, position, mDataList.get(position));
            }else{
                PostViewHolder postViewHolder = (PostViewHolder) holder;
                postViewHolder.bind(postViewHolder, position, mDataList.get(position));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == DISPLAY_RECOMMOND){
            return new BrandViewHolder(mInflater.inflate(R.layout.item_circle_brands_rv, parent, false));
        }
        return new PostViewHolder(mInflater.inflate(R.layout.item_circle_day_hot_adapter, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if(mDataList.get(position).isShowCircleBrand()){
            return DISPLAY_RECOMMOND;
        }
        return DISPLAY_CIRCLE_INFO;
    }

    public void setData(List<MarkermallCircleInfo> data) {
        mDataList.clear();
        if (data != null) {
            mDataList.addAll(data);
        }
    }

    public void updateItem(MarkermallCircleInfo item, int pos){
        if(null != mDataList){
            Iterator<MarkermallCircleInfo> iterator = mDataList.iterator();
            for (int i = 0; i < mDataList.size(); i++) {
                MarkermallCircleInfo eachItem =  mDataList.get(i);
                if(eachItem.getId() == item.getId()){
                    mDataList.set(i,item);
                    notifyItemChanged(i,item);
                }
            }

        }

    }

    public void updateBrandItem(MarkermallCircleInfo item, int pos){
        if(null != circleBrandsAdapter){
           List<MarkermallCircleInfo> items =  item.getCircleBrands();
            circleBrandsAdapter.replaceData(items);
            if(pos <= (items.size()-1)){
                circleBrandsAdapter.notifyItemChanged(pos,items.get(pos));
            }
        }

    }

    public void removeItem(MarkermallCircleInfo item, int pos){
        if(null != mDataList){
            int cur = 0;
            Iterator<MarkermallCircleInfo> iterator = mDataList.iterator();
            while (iterator.hasNext()){
                MarkermallCircleInfo eachItem = iterator.next();
                if(eachItem.getId() == item.getId()){
                    iterator.remove();
                    notifyItemRemoved(cur);
                }
                cur++;
            }
        }

    }

    public void insertItem(MarkermallCircleInfo item, int pos){
        if(null != mDataList){
            mDataList.add(pos,item);
        }
        notifyItemInserted(pos);
    }

    public void clearData() {
        mDataList.clear();
    }


    public class CommonViewHolder extends RecyclerView.ViewHolder{

        public CommonViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class BrandViewHolder extends CommonViewHolder{
        @BindView(R.id.brandRv)
        RecyclerView brandRv;
        public BrandViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final BrandViewHolder holder, final int position, final MarkermallCircleInfo item) {
            if(null != item && item.isShowCircleBrand() && null != item.getCircleBrands() && item.getCircleBrands().size()>0){
                circleBrandsAdapter = new CircleBrandsAdapter(mContext);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                brandRv.setLayoutManager(linearLayoutManager);
                brandRv.setAdapter(circleBrandsAdapter);
                circleBrandsAdapter.setData(item.getCircleBrands());
                circleBrandsAdapter.setmCollectAction(new MyAction.Two<MarkermallCircleInfo, Integer>() {
                    @Override
                    public void invoke(MarkermallCircleInfo childItem, Integer position) {
                        if (!LoginUtil.checkIsLogin((Activity) mContext)) return;
                         if(null != mbrandCollectAction){
                             mbrandCollectAction.invoke(item,position);
                         }
                    }
                });
            }

        }
    }

    public class PostViewHolder extends CommonViewHolder {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.mListViewGoods)
        FixRecyclerView mListViewGoods;

        @BindView(R.id.mListView11)
        FixRecyclerView mListView;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_shart)
        TextView tv_shart;
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.userIcon)
        ImageView userIcon;
        @BindView(R.id.tv_role)
        TextView tv_role;
        @BindView(R.id.tv_capy)
        TextView tv_capy;
        @BindView(R.id.tv_comment)
        TextView tv_comment;
        @BindView(R.id.rl_comment)
        RelativeLayout rl_comment;
        @BindView(R.id.collectBtn)
        CollectBtn collectBtn;
//        @BindView(R.id.labelIv)
//        ImageView labelIv;
//        @BindView(R.id.labelTv)
//        TextView labelTv;
//        @BindView(R.id.tabLayout)
//        LinearLayout tabLayout;
        private boolean isRefresh = false;
        MarkermallCirleItmeAdapter cirleDayHotItmeAdapter;
        MarkermallCirleItmeGoodsAdapter cirleDayHotItmeGoodsAdapter;
        int tv_shart_width = 0;
        int collect_width = 0;
        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final PostViewHolder holder, final int position, final MarkermallCircleInfo item) {
            LoadImgUtils.setImgCircle(mContext, userIcon, item.getIcon(), R.drawable.circle_default_head);
            if (!TextUtils.isEmpty(item.getUserLabel())) {
                tv_role.setText(item.getUserLabel());
            } else {
                tv_role.setText("分享达人");
            }


//            LoadImgUtils.setImg(mContext, iv_role_bg, item.getLabelPicture(), R.drawable.icon_darenbiaoqian);


            title.setText(item.getName());
            tv_time.setText(DateTimeUtils.getShortTime(item.getCreateTime()));
            tv_content.setText(item.getContent());
            tv_shart.setText(MathUtils.getSales(item.getShareCount() + ""));
            if (TextUtils.isEmpty(item.getComment())) {
                rl_comment.setVisibility(View.GONE);
            } else {
                rl_comment.setVisibility(View.VISIBLE);
                tv_comment.setText(item.getComment());
            }

            //计算title的最大宽度，因为后面跟着收藏按钮
            tv_shart_width = 0;
            tv_shart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tv_shart.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    tv_shart_width = tv_shart.getMeasuredWidth();
                }
            });

//            if(1 == item.getIsRecommend()){
//                tabLayout.setVisibility(View.VISIBLE);
//                if(!TextUtils.isEmpty(item.getLabelPic())){
//                    labelIv.setVisibility(View.VISIBLE);
//                    LoadImgUtils.setImg(mContext, labelIv, item.getLabelPic(), false);
//                }else{
//                    labelIv.setVisibility(View.GONE);
//                }
//                if(!TextUtils.isEmpty(item.getLabelName())){
//                    labelTv.setText(item.getLabelName());
//                }
//            }else{
//                tabLayout.setVisibility(View.GONE);
//            }
            title.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    title.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int screenWdith = ScreenUtils.getScreenWidth();
                    if(screenWdith > tv_shart_width){
                        int maxTitleWidth = screenWdith - tv_shart_width- DensityUtil.dip2px(mContext,38+30+95); //30是左右pading,90是收藏按钮的dp，38是头像的
                        title.setMaxWidth(maxTitleWidth);
                    }

                }
            });

            if(1 == item.getIsCollection() || !TextUtils.isEmpty(item.getCollectionId())){
                collectBtn.setCollected(true);
            }else{
                collectBtn.setCollected(false);
            }

            collectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!LoginUtil.checkIsLogin((Activity) mContext)) return;
                    if(null != mCollectAction){

                        mCollectAction.invoke(item,position);
                    }
                }
            });


            tv_capy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.coayTextPutNative((Activity) mContext, item.getComment());
                    ViewShowUtils.showShortToast(mContext, mContext.getString(R.string.coayTextSucceed));
                }
            });
            tv_shart.setOnClickListener(new View.OnClickListener() {
                @Override //
                public void onClick(View view) {
                    if (!LoginUtil.checkIsLogin((Activity) mContext)) return;
                    if (TaobaoUtil.isAuth()) {   //淘宝授权
                        TaobaoUtil.getAllianceAppKey((BaseActivity) mContext);
                    } else {
                        if (AppUtil.isFastClick(1000)) return;
                        mTvShart = tv_shart;
                        AppUtil.coayText((Activity) mContext, item.getContent());
                        if ((item.getGoods() == null || item.getGoods().size() == 0) &&
                                (item.getShareRangItems() == null || item.getShareRangItems().size() == 0)&&(item.getPicture()==null||item.getPicture().size()==0)) {
                            ViewShowUtils.showShortToast(mContext, "文案复制成功");
                            return;
                        }
//                        if (mLoadType == CircleDayHotFragment.TypeImg) {
//                            //TODO
//                            share(item, mLoadType);
//                        } else {
//                        SensorsDataUtil.getInstance().mifenClickTrack(mMainTitle,mSubTitle,"发圈",position,"","","","","","","",1,"");
                        showChoosePicDialog((Activity) mContext, item);
//                        }
                    }
                }
            });

            GridLayoutManager mgr = new GridLayoutManager(mContext, 3);
            if (!isRefresh) {
                isRefresh = true;
                mListView.addItemDecoration(new SpaceItemDecoration(imgPadding));
            }
             boolean isOneImg = false;
            if(item.getShareRangItems()==null||item.getShareRangItems().size()==0){
                isOneImg = item.getPicture() == null || item.getPicture().size() == 1;
            } else {
                isOneImg = item.getShareRangItems().size()==1;
            }


            if (holder.cirleDayHotItmeAdapter == null) {

                holder.cirleDayHotItmeAdapter = new MarkermallCirleItmeAdapter(mContext, item, mLoadType, imgWidth, isOneImg, mWindowWidth3);

                mListView.setLayoutManager(mgr);
                setGridManager(isOneImg, mgr);
                mListView.setHasFixedSize(true);
                mListView.setAdapter(cirleDayHotItmeAdapter);
            } else {
                holder.cirleDayHotItmeAdapter.seIsOneImg(isOneImg);
                holder.cirleDayHotItmeAdapter.seData(item);
                GridLayoutManager layoutManager = (GridLayoutManager) mListView.getLayoutManager();
                setGridManager(isOneImg, layoutManager);
            }
            if (holder.cirleDayHotItmeGoodsAdapter == null) {
                holder.cirleDayHotItmeGoodsAdapter = new MarkermallCirleItmeGoodsAdapter(mContext, item);
                mListViewGoods.setLayoutManager(new LinearLayoutManager(mContext));
                mListViewGoods.setAdapter(cirleDayHotItmeGoodsAdapter);
                cirleDayHotItmeGoodsAdapter.setUpdataShareTextAction(new MyAction.One<String>() {
                    @Override
                    public void invoke(String arg) {
                        mTvShart = holder.tv_shart;
                        AppUtil.coayText((Activity) mContext, item.getContent());
                    }
                });
            } else {
                holder.cirleDayHotItmeGoodsAdapter.seData(item);
                holder.cirleDayHotItmeGoodsAdapter.setUpdataShareTextAction(new MyAction.One<String>() {
                    @Override
                    public void invoke(String arg) {
                        mTvShart = holder.tv_shart;
                        AppUtil.coayText((Activity) mContext, item.getContent());
                    }
                });
            }
        }

        private void setGridManager(final boolean isOneImg, GridLayoutManager layoutManager) {
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    MyLog.d("MarkermallCirleItmeAdapter", "isOneImg " + isOneImg + "  position  " + position);
                    if (isOneImg) {
                        return 3;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    private void share(final MarkermallCircleInfo item, int type, int sharePlatform) {
        switch (type) {
            case CircleDayHotFragment.TypeImg:
            case CircleDayHotFragment.TypeCommodityImg:
                List<String> pictures = new ArrayList<>();

                if (item.getShareRangItems() == null || item.getShareRangItems().size() == 0) {
                    if(item.getPicture()==null||item.getPicture().size()==0){
                        List<ShopGoodInfo> osgData1 = GoodsUtil.CirleInfoToGoodInfo(item);
                        if (osgData1 == null) {
                            ViewShowUtils.showShortToast(mContext, R.string.goodsShertError);
                            return;
                        }
                        for (ShopGoodInfo info : osgData1) {
                            String picture1 = info.getPicture();
                            if (!TextUtils.isEmpty(picture1) && info.getIsExpire() == 0) {
                                pictures.add(picture1);
                            }
                        }
                    } else {
                        for (String picture : item.getPicture()) {

//                            if (!TextUtils.isEmpty(picture) && info.getIsExpire() == 0) {
                                pictures.add(picture);
//                            }
                        }
                    }

                } else {
                    //过滤视频图片
                    for (MarkermallCircleItemInfo markermallCircleItemInfo : item.getShareRangItems()) {
                        if (StringsUtils.isEmpty(markermallCircleItemInfo.getItemVideoid()) && markermallCircleItemInfo.getIsExpire() == 0) {
                            pictures.add(markermallCircleItemInfo.getPicture());
                        }

                    }
                }
                shareImg(item, pictures, sharePlatform);
                break;
            case CircleDayHotFragment.TypeCommodity:

//                List<ShopGoodInfo> osgData = GoodsUtil.CirleInfoToGoodInfo(item);
                List<ShopGoodInfo> osgData = addGoods(item);
                ;
                if (osgData == null) {
                    ViewShowUtils.showShortToast(mContext, R.string.goodsShertError);
                    return;
                }
                shareGoods(item, osgData, sharePlatform);
                break;
            case CircleDayHotFragment.TYPE_SHARE_VIDEO:
//                List<ShopGoodInfo> data = GoodsUtil.CirleInfoToGoodInfo(item);
                openApp(sharePlatform);
                break;
        }
    }

    private List<ShopGoodInfo> addGoods(MarkermallCircleInfo item) {
        List<ShopGoodInfo> goodsList = new ArrayList<>();
        List<MarkermallCircleItemInfo> markermallList = item.getShareRangItems();
        if (markermallList != null) {
            for (int i = 0; i < markermallList.size(); i++) {
                MarkermallCircleItemInfo markermallCircleItemInfo = markermallList.get(i);
                //只有商品分享才能分享二维码海报
                if (StringsUtils.isEmpty(markermallCircleItemInfo.getItemVideoid()) && !TextUtils.isEmpty(markermallCircleItemInfo.getItemSourceId())) {
                    ShopGoodInfo shopGood = new ShopGoodInfo();
                    try {
                        CopyPropertiesUtil.copyProperties(markermallList.get(i), shopGood);
                        goodsList.add(shopGood);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        goodsList.addAll(item.getGoods());
        return goodsList;

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
        mContext.startActivity(intent);
    }

    private void openQQ() {
        if (!AppUtil.isQQClientAvailable(mContext)) {
            ViewShowUtils.showShortToast(mContext, "请先安装微信客户端");
            return;
        }
//        ComponentName componet = new ComponentName("com.tencent.mobileqq");
//        Intent intent = new Intent();
//        intent.setComponent(componet);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
        mContext.startActivity(intent);
//        mContext.startActivity(intent);
    }

    private void openWechat() {
        if (!AppUtil.isWeixinAvilible(mContext)) {
            ViewShowUtils.showShortToast(mContext, "请先安装微信客户端");
            return;
        }
        Intent lan = mContext.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(lan.getComponent());
        mContext.startActivity(intent);
    }


    /**
     * 分享dialog
     */
    protected void showChoosePicDialog(final Activity activity, final MarkermallCircleInfo item) {

        final int type = getShareDialogType(item);
        showChoosePicDialog(type, new MyAction.One<Integer>() {
            @Override
            public void invoke(Integer which) {
                if (type == TYPE_GOODS_IMAGE_VIDEO) {
                    if (which == 0) {
                        openShareDialog(item, CircleDayHotFragment.TypeCommodity);
                    } else if (which == 1) {
                        openShareDialog(item, CircleDayHotFragment.TypeImg);
                    } else if (which == 2) {
//                    openShareDialog(item,CircleDayHotFragment.TYPE_SHARE_VIDEO);
                        download(item, CircleDayHotFragment.TYPE_SHARE_VIDEO);
                    }
                } else if (type == TYPE_GOODS_IMAGE) {
                    if (which == 0) {
                        openShareDialog(item, CircleDayHotFragment.TypeCommodity);
                    } else if (which == 1) {
                        openShareDialog(item, CircleDayHotFragment.TypeImg);
                    }
                } else if (type == TYPE_VIDEO) {
                    if (which == 0) {
                        download(item, CircleDayHotFragment.TYPE_SHARE_VIDEO);
                    }
                } else if (type == TYPE_IMAGE) {
                    openShareDialog(item, CircleDayHotFragment.TypeImg);
                } else if (type == TYPE_VIDEO_IMAGE) {
                    if (which == 0) {
                        openShareDialog(item, CircleDayHotFragment.TypeImg);
                    } else if (which == 1) {
                        download(item, CircleDayHotFragment.TYPE_SHARE_VIDEO);
                    }
                }

            }
        });
    }

    private int getShareDialogType(MarkermallCircleInfo item) {
        boolean isExitVideo = false;
        boolean isExitGoods = false;
        boolean isExitImage = false;
        if (item.getGoods() != null && item.getGoods().size() > 0) {
            isExitGoods = true;
        }
        if (item.getShareRangItems() != null) {
            for (MarkermallCircleItemInfo markermallCircleItemInfo : item.getShareRangItems()) {
                if (!StringsUtils.isEmpty(markermallCircleItemInfo.getItemVideoid())) {
                    isExitVideo = true;
                    continue;
                } else if (!TextUtils.isEmpty(markermallCircleItemInfo.getItemSourceId())) {
                    isExitGoods = true;
                    continue;
                } else {
                    isExitImage = true;
                }
            }
        }
        if (isExitGoods && isExitVideo && isExitImage) {
            //商品，图片,视频
            return TYPE_GOODS_IMAGE_VIDEO;
        } else if (isExitGoods && isExitVideo && !isExitImage) {
            //商品，图片,视频
            return TYPE_GOODS_IMAGE_VIDEO;
        } else if (!isExitGoods && isExitVideo && isExitImage) {
            //图片,视频
            return TYPE_VIDEO_IMAGE;
        } else if (!isExitGoods && isExitVideo && !isExitImage) {
            //纯视频
            return TYPE_VIDEO;
        } else if (!isExitGoods && !isExitVideo && isExitImage) {
            //纯图片
            return TYPE_IMAGE;
        } else if (isExitGoods && !isExitVideo) {
            //商品，图片
            return TYPE_GOODS_IMAGE;
        } else {
            //商品，图片
            return TYPE_GOODS_IMAGE;
        }
    }

    public void updataShareText() {
        if (mTvShart != null) {
            try {
                CharSequence text = mTvShart.getText();
                if (!TextUtils.isEmpty(text)) {
                    Integer count = Integer.valueOf((String) text);
                    int i = count + 1;
                    mTvShart.setText(i + "");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            mTvShart = null;
        }
    }

    private void openShareDialog(final MarkermallCircleInfo item, final int type) {
        shareDialog = new CommercialShareDialog(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.weixinFriend: //分享到好友
                        share(item, type, ShareUtil.WechatType);
                        break;
                    case R.id.weixinCircle: //分享到朋友圈
                        share(item, type, ShareUtil.WeMomentsType);
                        break;
                    case R.id.qqFriend: //分享到QQ
                        share(item, type, ShareUtil.QQType);
                        break;
                    case R.id.qqRoom: //分享到QQ空间
                        share(item, type, ShareUtil.QQZoneType);
                        break;
                    case R.id.sinaWeibo: //分享到新浪微博
                        share(item, type, ShareUtil.WeiboType);
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

    private void download(MarkermallCircleInfo item, int sharePlatform) {
        if (item == null) {
            return;
        }
        LoadingView.showDialog(mContext, mContext.getString(R.string.downloading_local));
        if (item.getShareRangItems() != null) {
            //先获取视频总数量
            for (int i = 0; i < item.getShareRangItems().size(); i++) {
                MarkermallCircleItemInfo circleItemInfo = item.getShareRangItems().get(i);
                if (circleItemInfo != null) {
                    String videoUrl = circleItemInfo.getItemVideoid();
                    if (!StringsUtils.isEmpty(videoUrl)) {
                        mDownloadSize++;
                    }
                }
            }
            //开始下载
            for (int i = 0; i < item.getShareRangItems().size(); i++) {
                MarkermallCircleItemInfo circleItemInfo = item.getShareRangItems().get(i);
                if (circleItemInfo != null) {
                    String videoUrl = circleItemInfo.getItemVideoid();
                    List<String> videoUrlList = new ArrayList<>();
                    if (!StringsUtils.isEmpty(videoUrl)) {
                        videoUrlList.add(videoUrl);
                        String fileName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.lastIndexOf("."));
                        String suffix = videoUrl.substring(videoUrl.lastIndexOf(".") + 1, videoUrl.length());
                        DownloadManage.getInstance().start(videoUrl, SdDirPath.IMAGE_CACHE_PATH + fileName + "." + suffix, downloadListener);
//                       mTasks.add(FileDownloader.getImpl().create(videoUrl).setPath(SdDirPath.IMAGE_CACHE_PATH+fileName+"."+suffix).setTag(i + 1));
                    }
                }

            }
        }
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
                if (task.getListener() != downloadListener) {
                    return;
                }
                mCurDownloadSize++;
                if (mCurDownloadSize == mDownloadSize) {
                    openShareDialog(null, CircleDayHotFragment.TYPE_SHARE_VIDEO);
                    LoadingView.dismissDialog();
                }
                DownloadManage.getInstance().refreshGallery(mContext, task.getPath());

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

    public void setTitle(String mMainTitle, String mSubTitle) {
        this.mMainTitle = mMainTitle;
        this.mSubTitle = mSubTitle;
    }

    public void setMbrandCollectAction(MyAction.Two<MarkermallCircleInfo, Integer> mbrandCollectAction) {
        this.mbrandCollectAction = mbrandCollectAction;
    }

    public void setmCollectAction(MyAction.Two<MarkermallCircleInfo, Integer> mCollectAction) {
        this.mCollectAction = mCollectAction;
    }
}
