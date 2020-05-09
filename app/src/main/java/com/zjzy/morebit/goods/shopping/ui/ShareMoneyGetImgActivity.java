package com.zjzy.morebit.goods.shopping.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Activity.ImagePagerActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShareMoneyAdapter;
import com.zjzy.morebit.contact.SdDirPath;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.event.ShareMoenyPosterEvent;
import com.zjzy.morebit.pojo.goods.ShareUrlListBaen;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.share.ShareMore;
import com.zjzy.morebit.view.FixWidthRoundedImageView;
import com.zjzy.morebit.view.ToolbarHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author fengrs
 * @date 2019/8/31
 */
public class ShareMoneyGetImgActivity extends BaseActivity {
    @BindView(R.id.id_recyclerview_horizontal)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_poter_view)
    FixWidthRoundedImageView mIvPoterView;
    private ShopGoodInfo mGoodInfo;
    private ShareMoneyAdapter mAdapter;
    private Bitmap mEwmBitmap;

    private String mPosterPicPath;
    private String mBitmapPicPath;
    private int mPosterPos;
    private Bitmap mBitmap;

    public static void start(Activity activity, String posterPicPath, int posterPos, ShopGoodInfo goodsInfo) {
        Intent intent = new Intent(activity, ShareMoneyGetImgActivity.class);
        intent.putExtra(C.Extras.POSTER_URL, posterPicPath);
        intent.putExtra(C.Extras.POSTER_POS, posterPos);
        intent.putExtra(C.Extras.GOODSBEAN, goodsInfo);
        activity.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_moeny_get_img);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_757575); //设置标题栏颜色值
        }
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mGoodInfo = (ShopGoodInfo) getIntent().getSerializableExtra(C.Extras.GOODSBEAN);
        if (mGoodInfo == null||mGoodInfo.getAdImgUrl() == null||mGoodInfo.getAdImgUrl().size() == 0) {
            return;
        }

        String posterUrl = "";
        for (int i = 0; i < mGoodInfo.getAdImgUrl().size(); i++) {
            ImageInfo imageInfo = mGoodInfo.getAdImgUrl().get(i);
            if (i == 0&& imageInfo.isPoster){
                posterUrl = imageInfo.getPicture();
            }
            imageInfo.isChecked = false;
        }
        if (!TextUtils.isEmpty(posterUrl)) {
            mPosterPicPath = posterUrl;
            mGoodInfo.getAdImgUrl().remove(0);
        } else {
            mPosterPicPath = getIntent().getStringExtra(C.Extras.POSTER_URL);
        }
        if (!TextUtils.isEmpty(mPosterPicPath)) {
            LoadImgUtils.setImg(this, mIvPoterView, mPosterPicPath);
        }
        mPosterPos = getIntent().getIntExtra(C.Extras.POSTER_POS, 0);
        getShareEWMBitmap();

        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.change_img));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if (mGoodInfo.getAdImgUrl() != null && mGoodInfo.getAdImgUrl().size() != 0) {
            if (mGoodInfo.getAdImgUrl().size() != 0)
                mGoodInfo.getAdImgUrl().get(mPosterPos).isChecked = true;
            mAdapter = new ShareMoneyAdapter(this, mGoodInfo.getAdImgUrl(), true);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new ShareMoneyAdapter.OnRecyclerViewItemClickListener() {

                @Override
                public void onItemClick(View view, final int position, boolean isChecked) {
                    if (!isChecked) return;
                    final String finalDownPath = mGoodInfo.getAdImgUrl().get(position).getPicture();
                    Observable<Bitmap> flowable1 = LoadImgUtils.getImgToBitmapObservable(ShareMoneyGetImgActivity.this, finalDownPath);
                    flowable1.flatMap(new Function<Bitmap, ObservableSource<Bitmap>>() {
                        @Override
                        public ObservableSource<Bitmap> apply(final Bitmap bitmap) throws Exception {
                            return Observable.create(new ObservableOnSubscribe<Bitmap>() {
                                @Override
                                public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                                    //设置布局数据
                                    View view = GoodsUtil.getGoodsPosterView(ShareMoneyGetImgActivity.this, mGoodInfo, bitmap, mEwmBitmap);
                                    emitter.onNext(GoodsUtil.getViewBitmap(view));
                                }
                            }).compose(RxUtils.<Bitmap>switchSchedulers());
                        }
                    })
                            .compose(ShareMoneyGetImgActivity.this.<Bitmap>bindToLifecycle())
                            .subscribe(new CallBackObserver<Bitmap>() {
                                @Override
                                public void onNext(Bitmap bitmapPath) {
                                    mBitmapPicPath = finalDownPath;
                                    mBitmap = bitmapPath;
                                    mIvPoterView.setImageBitmap(bitmapPath);
//                                    LoadImgUtils.setImg(ShareMoneyGetImgActivity.this, mIvPoterView, bitmapPath);
                                    EventBus.getDefault().post(new ShareMoenyPosterEvent(position));
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });


                }
            });
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 回去分享海报二维码
     */
    private void getShareEWMBitmap() {
        ArrayList<ShopGoodInfo> shopGoodInfos = new ArrayList<>();
        shopGoodInfos.add(mGoodInfo);
        ShareMore.getShareGoodsUrlListObservable(this, shopGoodInfos, mGoodInfo.getItemSourceId())
                .observeOn(Schedulers.io())
                .flatMap(new Function<BaseResponse<ShareUrlListBaen>, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(BaseResponse<ShareUrlListBaen> baseResponse) throws Exception {
                        MyLog.threadName();
                        ShareUrlListBaen data = baseResponse.getData();
                        if (data != null) {
                            List<String> link = data.getLink();
                            if (link != null && link.size() != 0) {
                                return GoodsUtil.getShareEWMBitmapObservable(ShareMoneyGetImgActivity.this, link.get(0));
                            }
                        }
                        return null;
                    }
                })
                .subscribe(new CallBackObserver<Bitmap>() {
                    @Override
                    public void onNext(Bitmap bitmap) {
                        mEwmBitmap = bitmap;
                    }
                });
    }


    @OnClick({R.id.tv_save_img, R.id.iv_poter_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_poter_view:
                final ArrayList<String> arrayList = new ArrayList<String>();
                if (mBitmap != null && !TextUtils.isEmpty(mBitmapPicPath)) {
                    getBitmapObserver(new CallBackObserver<File>() {
                        @Override
                        public void onNext(File s) {
                            arrayList.add(s.getPath());
                            ImagePagerActivity.startActivity(ShareMoneyGetImgActivity.this, arrayList, 0);
                        }
                    });
                } else {
                    arrayList.add(mPosterPicPath);
                    ImagePagerActivity.startActivity(ShareMoneyGetImgActivity.this, arrayList, 0);
                }
                break;

            case R.id.tv_save_img:
                if (mBitmap != null && !TextUtils.isEmpty(mBitmapPicPath)) {
                    getBitmapObserver(new CallBackObserver<File>() {
                        @Override
                        public void onNext(File file) {
                            String path = file.getPath();
                            saveBitmap(path);
                        }
                    });
                } else {
                    saveBitmap(mPosterPicPath);
                }

                break;

            default:
                break;
        }

    }

    public void saveBitmap(String path) {
        if (TextUtils.isEmpty(path)) {
            ViewShowUtils.showLongToast(this, "保存图片失败");
            return;
        }
        String bitName = FileUtils.getPictureName(path);
        LoadingView.showDialog(ShareMoneyGetImgActivity.this, "");
        GoodsUtil.saveImg(ShareMoneyGetImgActivity.this, path, bitName, new MyAction.OnResultTwo<File, Integer>() {
            @Override
            public void invoke(File arg, Integer arg1) {
                LoadingView.dismissDialog();
                if (arg == null) {
                    ViewShowUtils.showShortToast(ShareMoneyGetImgActivity.this, "保存图片失败");
                    return;
                }
                if (arg1 == 1) {
                    ViewShowUtils.showShortToast(ShareMoneyGetImgActivity.this, R.string.save_img_succeed);
                } else {
                    String dir = SdDirPath.IMAGE_CACHE_PATH;
                    String string = ShareMoneyGetImgActivity.this.getResources().getString(R.string.img_save_to, dir);
                    ViewShowUtils.showShortToast(ShareMoneyGetImgActivity.this, string);
                }
            }

            @Override
            public void onError() {
                LoadingView.dismissDialog();
            }
        });
    }

    public void getBitmapObserver(CallBackObserver observer) {
        if (observer == null) return;
        LoadingView.showDialog(this);
        Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(final ObservableEmitter<File> emitter) throws Exception {
                String bitmappictureName = FileUtils.interceptingPictureName(mBitmapPicPath);
                File bitmapFile = GoodsUtil.saveBitmapToFile(ShareMoneyGetImgActivity.this, mBitmap, "goodqr_" + bitmappictureName, false);
                emitter.onNext(bitmapFile);
            }
        }).compose(RxUtils.<File>switchSchedulers())
                .compose(this.<File>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(observer);
    }

    @Override
    protected void onDestroy() {
        mEwmBitmap = null;
        mBitmap = null;
        super.onDestroy();

    }

}
