package com.zjzy.morebit.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShareMoneyAdapter;
import com.zjzy.morebit.goods.shopping.ui.ShareMoneyGetImgActivity;
import com.zjzy.morebit.goods.shopping.ui.dialog.ShareFriendsDialog;
import com.zjzy.morebit.main.model.ConfigModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.ShareMoenyPosterEvent;
import com.zjzy.morebit.pojo.goods.EditTemplateInfo;
import com.zjzy.morebit.pojo.goods.PddShareContent;
import com.zjzy.morebit.pojo.goods.ShareUrlListBaen;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.share.ShareMore;
import com.zjzy.morebit.utils.sys.RequestPermissionUtlis;
import com.zjzy.morebit.view.ConsCommissionRuleDialog;
import com.zjzy.morebit.view.goods.ShareMoneySwitchForPddTemplateView;
import com.zjzy.morebit.view.goods.ShareMoneySwitchTemplateView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 分享拼多多推广链接
 */

public class ShareMoneyForPddActivity extends BaseActivity implements View.OnClickListener {

    public static String goodsDataText = "goodsDataText";

    private LinearLayout btn_back;
    private RecyclerView mRecyclerView;
    private ShareMoneyAdapter mAdapter;
    private TextView tv_copy, incomeMoney;
    private EditText et_copy;
    private LinearLayout weixinCircle, weixinFriend;
    private ShopGoodInfo goodsInfo;

    private String promotionUrl;//推广链接
    private RelativeLayout rule_ly;
    private int ticknum = 1; //选择多少张

    private String mExtension = "";
    private String mPosterPicPath;
    private int mPosterPos;
    private ShareFriendsDialog mShareFriendsDialog;
    private int mBitmapPosition = -1;



    public static void start(Activity context, ShopGoodInfo info, String promotionUrl) {
        if (promotionUrl == null || info == null) return;
        Intent sharemoney = new Intent(context, ShareMoneyForPddActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable(C.Extras.GOODSBEAN, info);
        bundle1.putSerializable(C.Extras.PDDPROMOTIONDATA, promotionUrl);
        sharemoney.putExtras(bundle1);
        context.startActivityForResult(sharemoney, C.Result.shareMoneyToEditTemplateCoad);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_pdd_share_money);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            goodsInfo = (ShopGoodInfo) bundle.getSerializable(C.Extras.GOODSBEAN);
            promotionUrl = (String) bundle.getSerializable(C.Extras.PDDPROMOTIONDATA);

        }
        inview();
        getImg(0);

    }

    public void inview() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_F8F8F8); //设置标题栏颜色值
        }
        //设置页面头顶空出状态栏的高度
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        tv_copy = (TextView) findViewById(R.id.tv_copy);
        et_copy = (EditText) findViewById(R.id.et_copy);
        et_copy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //通知父控件不要干扰
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    //通知父控件不要干扰
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });


        weixinCircle = (LinearLayout) findViewById(R.id.weixinCircle);
        weixinFriend = (LinearLayout) findViewById(R.id.weixinFriend);

        btn_back.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
        weixinCircle.setOnClickListener(this);
        weixinFriend.setOnClickListener(this);
        findViewById(R.id.qqFriend).setOnClickListener(this);
        findViewById(R.id.qqRoom).setOnClickListener(this);
        findViewById(R.id.sinaWeibo).setOnClickListener(this);
        findViewById(R.id.makeGoodsPoster).setOnClickListener(this);
        findViewById(R.id.ll_more).setOnClickListener(this);

        //画廊
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器

        if (goodsInfo == null) return;
        if (goodsInfo.getAdImgUrl() != null && goodsInfo.getAdImgUrl().size() != 0) {
            if (goodsInfo.getAdImgUrl().size() != 0)
                goodsInfo.getAdImgUrl().get(0).isChecked = true;
            mAdapter = new ShareMoneyAdapter(this, goodsInfo.getAdImgUrl(), false);
            mRecyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new ShareMoneyAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position, boolean isChecked) {
                    ticknum = 0;
                    for (int i = 0; i < goodsInfo.getAdImgUrl().size(); i++) {
                        ImageInfo imageInfo = goodsInfo.getAdImgUrl().get(i);
                        if (imageInfo != null && imageInfo.isChecked) ticknum++;
                    }
                }
            });
            mAdapter.notifyDataSetChanged();
        }

        rule_ly = (RelativeLayout) findViewById(R.id.rule_ly);
        rule_ly.setOnClickListener(this);
        //代理商显示代理佣金
        incomeMoney = (TextView) findViewById(R.id.incomeMoney);

        UserInfo userInfo = UserLocalData.getUser(ShareMoneyForPddActivity.this);
//        if (C.UserType.member.equals(userInfo.getPartner())) { //消费者
//            rule_ly.setVisibility(View.GONE);
//        } else {
            rule_ly.setVisibility(View.VISIBLE);
            String discountsMoneyStr = MathUtils.getMuRatioComPrice(userInfo.getCalculationRate(), goodsInfo.getCommission());
//            String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(userInfo.getCalculationRate(), goodsInfo.getSubsidiesPrice());
//            String allDiscountsMoneyStr = MathUtils.getTotleSubSidies(discountsMoneyStr, getRatioSubside);
            incomeMoney.setText("您的奖励预计为: " + discountsMoneyStr + "元");
//        }
        ShareMoneySwitchForPddTemplateView viewSwitch = (ShareMoneySwitchForPddTemplateView) findViewById(R.id.view_swicht);
        viewSwitch.setAction(new MyAction.One<Integer>() {
            @Override
            public void invoke(Integer arg) {
                getTemplate();
            }
        });
        getTemplate();

    }


    @Override
    protected void onResume() {
        super.onResume();
//        String templateGson = (String) SharedPreferencesUtils.get(this, C.sp.editTemplate, "");
//        if (!TextUtils.isEmpty(templateGson)) {
//            try {
//                editTemplateInfo = (EditTemplateInfo) MyGsonUtils.jsonToBean(templateGson, EditTemplateInfo.class);
//            } catch (Exception e) {
//                editTemplateInfo = new EditTemplateInfo();
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_copy:
                if (TextUtils.isEmpty(et_copy.getText().toString())) {
                    return;
                }
                AppUtil.coayTextPutNative(this, et_copy.getText().toString());
                ViewShowUtils.showShortToast(this, R.string.coayTextSucceed);
                break;

            case R.id.weixinFriend:
                startShare(ShareUtil.WechatType);
                break;

            case R.id.weixinCircle:
                if (ticknum > 1) {
                    openShareFriendsDialog();
                    startShare(ShareUtil.WeMomentsType, true);
                } else {
                    startShare(ShareUtil.WeMomentsType);
                }
                break;
            case R.id.qqFriend:
                startShare(ShareUtil.QQType);
                break;
            case R.id.qqRoom:
                startShare(ShareUtil.QQZoneType);
                break;
            case R.id.sinaWeibo:
                if (!AppUtil.isWeiboClientAvailable(ShareMoneyForPddActivity.this)) {
                    ViewShowUtils.showShortToast(ShareMoneyForPddActivity.this, "请先安装微博客户端");
                    return;
                }
                startShare(ShareUtil.WeiboType);
                break;
            case R.id.ll_more:
                permission(ShareUtil.MoreType);
                break;
            case R.id.rule_ly: //奖励规则
                showRuleDialog();
                break;
            case R.id.makeGoodsPoster: //更换海报
                goodsInfo.setCouponUrl(promotionUrl);
                ShareMoneyGetImgActivity.start(this, mPosterPicPath, mPosterPos, goodsInfo);
                break;
            default:
                break;
        }
    }

    private void openShareFriendsDialog() {
        if (mShareFriendsDialog == null) {
            mShareFriendsDialog = new ShareFriendsDialog(this, new ShareFriendsDialog.OnCloseListener() {
                @Override
                public void onClick() {
                    if (!AppUtil.isWeixinAvilible(ShareMoneyForPddActivity.this)) {
                        ViewShowUtils.showShortToast(ShareMoneyForPddActivity.this, "请先安装微信客户端");
                        return;
                    }
                    //跳转微信
                    PageToUtil.goToWeixin(ShareMoneyForPddActivity.this);

                }
            });
        }
        mShareFriendsDialog.show();
    }


    /**
     * 弹出奖励规则窗口
     */
    private ConsCommissionRuleDialog consCommissionRuleDialog;

    private void showRuleDialog() {

        if (consCommissionRuleDialog == null) {
            LoadingView.showDialog(this);
            ConfigModel.getInstance().getConfigForKey((RxAppCompatActivity) this, C.SysConfig.WEB_SHARE_RULE)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            LoadingView.dismissDialog();
                        }
                    })
                    .subscribe(new DataObserver<HotKeywords>() {
                        @Override
                        public void onSuccess(HotKeywords data) {
                            String sysValue = data.getSysValue();
                            if (!TextUtils.isEmpty(sysValue)) {
                                consCommissionRuleDialog = new ConsCommissionRuleDialog(ShareMoneyForPddActivity.this, R.style.dialog, "规则说明", sysValue, null);
                                consCommissionRuleDialog.show();
                            }
                        }
                    });
        } else {
            consCommissionRuleDialog.show();
        }

    }


    public void startShare(final int type) {
        startShare(type, false);
    }

    public void startShare(final int type, boolean isDownload) {
        if (AppUtil.isFastClick(500)) return;
        if (goodsInfo == null || goodsInfo.getAdImgUrl().size() <= 0) {
            return;
        }
        if (ticknum == 0) {
            Toast.makeText(ShareMoneyForPddActivity.this, "请选择你要分享的图片", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        AppUtil.coayText(this, et_copy.getText().toString());
        for (int i = 0; i < goodsInfo.getAdImgUrl().size(); i++) {
            ImageInfo imageInfo = goodsInfo.getAdImgUrl().get(i);
            if (imageInfo != null && imageInfo.isChecked) {
                String picture = imageInfo.getPicture();
                arrayList.add(picture);
            }
        }
        shareImg(arrayList, type, isDownload);
    }

    public void permission(final int type) {
        RequestPermissionUtlis.requestOne(this, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                share(type);
            }

            @Override
            public void onError() {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void share(final int type) {

        if (goodsInfo == null || goodsInfo.getAdImgUrl().size() <= 0) {
            return;
        }
        try {
            AppUtil.coayText(this, et_copy.getText().toString());
            final ArrayList<File> mUrlArrayList = new ArrayList<>();
            if (ticknum == 0) {
                Toast.makeText(ShareMoneyForPddActivity.this, "请选择你要分享的图片", Toast.LENGTH_SHORT).show();
                return;
            }
            LoadingView.showDialog(this, "");
            for (int i = 0; i < goodsInfo.getAdImgUrl().size(); i++) {
                ImageInfo imageInfo = goodsInfo.getAdImgUrl().get(i);
                if (imageInfo != null && imageInfo.isChecked) {
                    String fileName = "";
                    fileName = FileUtils.getPictureName(imageInfo.getPicture());
                    GoodsUtil.saveImg(ShareMoneyForPddActivity.this, imageInfo.getPicture(), fileName,
                            new MyAction.OnResultTwo<File, Integer>() {
                                @Override
                                public void invoke(File arg, Integer arg1) {
                                    if (arg != null) {
                                        mUrlArrayList.add(arg);
                                    }

                                    if (ticknum == mUrlArrayList.size()) {
                                        ShareMoneyForPddActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                GoodsUtil.originalShareImage(ShareMoneyForPddActivity.this, mUrlArrayList, type);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ShareMoneyForPddActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == C.Result.shareMoneyToEditTemplateCoad && resultCode == RESULT_OK) {
            getTemplate();
        }
    }

    public void getTemplate() {
        setResult(RESULT_OK);
        LoadingView.showDialog(this, "请求中...");

        if (goodsInfo.getItemSource().equals("1")){//京东
            GoodsUtil.getGenerateForJD(this, goodsInfo)
                    .subscribe(new DataObserver<String>() {
                        @Override
                        protected void onSuccess(String data) {
                            String template = data;
                            et_copy.setText(template);

                        }
                    });
        }else{//拼多多
            GoodsUtil.getGenerateForPDD(this, goodsInfo)
                    .subscribe(new DataObserver<PddShareContent>() {
                        @Override
                        protected void onSuccess(PddShareContent data) {
                            String template = data.getTemplate();
                            et_copy.setText(template);

                        }
                    });
        }

    }


    /**
     * 生成分享海报二维码
     */
    private Observable<Bitmap> getShareEWMBitmap(String promotionUrl) {

        return GoodsUtil.getShareEWMBitmapObservable(ShareMoneyForPddActivity.this, promotionUrl);
    }



    ;

    public void getImg(final int pos) {

        RequestPermissionUtlis.requestOne(this, new MyAction.OnResult<String>() {

            @Override
            public void invoke(String arg) {
                int index = pos;
                if (goodsInfo == null || goodsInfo.getAdImgUrl().size() <= 0) {
                    return;
                }

                if (goodsInfo.getAdImgUrl().get(0).isPoster) {
                    index = index + 1;
                }
                if (index > goodsInfo.getAdImgUrl().size()) {
                    index = goodsInfo.getAdImgUrl().size();
                }
                String finalDownPath = goodsInfo.getAdImgUrl().get(index).getPicture();
                Observable<Bitmap> flowable1 = LoadImgUtils.getImgToBitmapObservable(ShareMoneyForPddActivity.this, finalDownPath);
                Observable<Bitmap> flowable2 = getShareEWMBitmap(promotionUrl);

                Observable.zip(flowable1, flowable2, new BiFunction<Bitmap, Bitmap, String>() {
                    @Override
                    public String apply(Bitmap bitmap, Bitmap ewmBitmap) throws Exception {
                        MyLog.threadName();
                        if (bitmap == null) {
                            ViewShowUtils.showShortToast(ShareMoneyForPddActivity.this, "生成失败");
                            return null;
                        }
                        return GoodsUtil.saveGoodsImg(ShareMoneyForPddActivity.this, goodsInfo, bitmap, ewmBitmap, mExtension);
                    }
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new CallBackObserver<String>() {
                            @Override
                            public void onNext(String picPath) {
                                mPosterPicPath = picPath;
                                MyLog.threadName();
                                updatePoster(picPath, pos);
                            }
                        });
            }

            @Override
            public void onError() {
                AppUtil.goSetting(ShareMoneyForPddActivity.this);
            }
        }, false, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});


    }


    /* 分享图片,
     *
     * @param picture
     */
    protected void shareImg(final List<String> picture, final int sharePlatform, boolean isDownload) {
        if (!isDownload) {
            LoadingView.showDialog(this, "");
        }
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
                                bitmap = LoadImgUtils.getImgBitmapOnIo1(ShareMoneyForPddActivity.this, s);
                                String pictureName = FileUtils.getPictureName(s);
                                img = GoodsUtil.saveBitmapToFile(ShareMoneyForPddActivity.this, bitmap, pictureName);
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
                                ViewShowUtils.showShortToast(ShareMoneyForPddActivity.this, ShareMoneyForPddActivity.this.getString(R.string.save_succeed));
                            } else {
                                GoodsUtil.sysShareImage(ShareMoneyForPddActivity.this, urlList, sharePlatform);
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

    public void updatePoster(String picPath, int finalIndex) {
        if (goodsInfo.getAdImgUrl() == null || goodsInfo.getAdImgUrl().size() == 0 || TextUtils.isEmpty(picPath)) {
            return;
        }
        int posterPos = -1;
        for (int i = 0; i < goodsInfo.getAdImgUrl().size(); i++) {
            ImageInfo imageInfo = goodsInfo.getAdImgUrl().get(i);
            if (imageInfo.isPoster) {
                posterPos = i;
            }
        }
        if (posterPos != -1) {
            goodsInfo.getAdImgUrl().remove(posterPos);
        }
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setPicture(picPath);
        imageInfo.isPoster = true;
        imageInfo.isChecked = true;
        // 生产的海报放在第一位,被生产的发在第2位
        // 生产的海报放在第一位,被生产的发在第2位
        ImageInfo imageInfo1 = goodsInfo.getAdImgUrl().get(finalIndex);
        if (imageInfo1 != null) {
            goodsInfo.getAdImgUrl().remove(finalIndex);
            imageInfo1.isChecked = false;
            goodsInfo.getAdImgUrl().add(0, imageInfo);
            goodsInfo.getAdImgUrl().add(1, imageInfo1);
            mAdapter.setData(goodsInfo.getAdImgUrl());
            mRecyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(ShareMoenyPosterEvent event) {
        mBitmapPosition = event.getBitmapPosition();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mBitmapPosition != -1) {
            getImg(mBitmapPosition);
            mBitmapPosition = -1;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
