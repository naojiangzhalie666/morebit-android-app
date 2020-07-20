package com.zjzy.morebit.Activity;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gyf.barlibrary.ImmersionBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.request.AppletsBean;
import com.zjzy.morebit.pojo.request.RequestActivityLinkBean;
import com.zjzy.morebit.pojo.request.RequestAppletsBean;
import com.zjzy.morebit.pojo.request.WxCodeBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.DownloadManage;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ShareUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.zjzy.morebit.utils.C.requestType.initData;
//小程序分享
public class AppletsActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title;
    private LinearLayout btn_back,weixinFriend,weixinCircle,save,applets,qqRoom,ll_more;
    private ImageView img1,img2,imgone,imgtwo;
    private RoundedImageView tv_tou;
    private TextView tv,tv_name;
    private Bitmap bitmap1,bitmap2;
    private  String  shareHundry;
    private FileDownloadListener downloadListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applets);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();
        initView();
       initmData();
    }

    private void initmData() {
        UserInfo user = UserLocalData.getUser(this);
        Log.e("qwer",user.getNickName());
        //LoadingView.showDialog(this, "请求中...");
        RequestAppletsBean bean = new RequestAppletsBean();
        bean.setInviteCode(user.getInviteCode());
        RxHttp.getInstance().getGoodsService()
                .shareMiniProgramInfo(bean)
                .compose(RxUtils.<BaseResponse<AppletsBean>>switchSchedulers())
                .compose(this.<BaseResponse<AppletsBean>>bindToLifecycle())
                .subscribe(new DataObserver<AppletsBean>() {
                    @Override
                    protected void onSuccess(final AppletsBean data) {

                        String backgroundImgUrl = data.getBackgroundImgUrl();



                        if (TextUtils.isEmpty(data.getBackgroundImgUrl())) return;
                        Glide.with(AppletsActivity.this)
                                .asBitmap()
                                .load(backgroundImgUrl)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        bitmap1=resource;
                                        Log.e("qwer","bitmap1"+bitmap1);
                                        img1.setImageBitmap(resource);
                                        imgone.setImageBitmap(resource);
                                        imgVIew(resource,data.getQrCodeUrl());
                                    }
                                });
                        //  LoadImgUtils.setViewBackground(ShareHungryActivity.this, share_img, activityLink);



                        if (TextUtils.isEmpty(data.getShareDesc()))return;
                        tv.setText(data.getShareDesc());




                      //  LoadingView.dismissDialog();
                    }
                });

    }

    private void imgVIew(final Bitmap resource2, String data) {
        Glide.with(AppletsActivity.this)
                .asBitmap()
                .load(data)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmap2=resource;
                        Log.e("qwer","bitmap2"+bitmap2);
                        img2.setImageBitmap(resource);
                        imgtwo.setImageBitmap(resource);

                        imgView2(resource2,resource);
                    }
                });


    }

    private void imgView2(Bitmap resource2, Bitmap resource) {
        Log.e("qwer","bitmap2"+resource2+"bitmap1"+resource);
        if (resource2!=null&&resource!=null){
            try {
                Log.e("qwer","bitmap1"+resource);
                shareHundry = GoodsUtil.saveAppletsGoodsImg(AppletsActivity.this, resource2,resource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        imgone= (ImageView) findViewById(R.id.imgone);
        imgtwo= (ImageView) findViewById(R.id.imgtwo);
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("小程序分享");
        txt_head_title.setTextSize(18);
        txt_head_title.getPaint().setFakeBoldText(true);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        weixinFriend = (LinearLayout) findViewById(R.id.weixinFriend);
        weixinFriend.setOnClickListener(this);
        weixinCircle = (LinearLayout) findViewById(R.id.weixinCircle);
        weixinCircle.setOnClickListener(this);
        save = (LinearLayout) findViewById(R.id.save);
        save.setOnClickListener(this);
        applets = (LinearLayout) findViewById(R.id.applets);
        applets.setOnClickListener(this);
        img1= (ImageView) findViewById(R.id.img1);
        img2= (ImageView) findViewById(R.id.img2);
        tv= (TextView) findViewById(R.id.tv);
        tv_tou= (RoundedImageView) findViewById(R.id.tv_tou);
        tv_name= (TextView) findViewById(R.id.tv_name);
        UserInfo user = UserLocalData.getUser(this);

        String sss=user.getNickName();
        Log.e("qwer1","sss"+sss);
        tv_name.setText(sss+"");
        LoadImgUtils.setImgCircle(this, tv_tou, user.getHeadImg(), R.drawable.head_icon);


    }


    @Override
    public void onClick(View v) {
        Log.e("yyyy","s"+shareHundry);

                switch (v.getId()){
                    case R.id.weixinFriend://微信
                        if (shareHundry!=null){
                            ShareUtil.Image.toWechatFriend(AppletsActivity.this, shareHundry, null);

                        }else{
                            ToastUtils.showShort("图片生成失败");
                        }
                        break;
                    case R.id.weixinCircle://微信朋友圈
                        if (shareHundry!=null){
                            ShareUtil.Image.toWechatMoments(AppletsActivity.this, shareHundry, null);

                        }else{
                            ToastUtils.showShort("图片生成失败");
                        }
                        break;
                    case R.id.applets://打开小程序
                        GoodsUtil.jumpApplets(this);
                        break;
                    case R.id.save://保存图片
                        if (shareHundry!=null){
                            startSave();
                        }else{
                            ToastUtils.showShort("图片生成失败");
                        }

                        break;

                }
            }






    public void startSave() {
        downloadListener =createLis();
     List<String> arrayList=new ArrayList<>();
     if (shareHundry!=null){
         arrayList.add(shareHundry);
     }


        //DownloadManage.getInstance().start(arrayList, SdDirPath.IMAGE_CACHE_PATH, downloadListener);

        DownloadManage.getInstance().multitaskStart(arrayList,/*SdDirPath.IMAGE_CACHE_PATH+fileName+"."+suffix,*/downloadListener);

        // shareImg(arrayList, type, isDownload);
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
//                type =DOWNLOADING ;
//                btn_setting.setVisibility(View.GONE);
//                MyLog.i("test","progress.getProgress()+1: " +progress.getProgress()+1);
//                progressPb.setProgress(progressPb.getProgress() + 1);
//                progressTv.setText("progress: " + progressPb.getProgress());
//                progressInfoTv.append((int)task.getTag() + " | ");


            }



            @Override
            protected void completed(BaseDownloadTask task) {
                //MyLog.i("test","completed: " +mDownloadCount);
                if (task.getListener() != downloadListener) {
                    return;
                }
                // GoodsUtil.updataImgToTK(mContext,new File(task.getPath()),task.getFilename());
                try {
                    MediaStore.Images.Media.insertImage(AppletsActivity.this.getContentResolver(), task.getPath(), task.getFilename(), null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                ToastUtils.showShort("图片已存到相册");

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
    }

