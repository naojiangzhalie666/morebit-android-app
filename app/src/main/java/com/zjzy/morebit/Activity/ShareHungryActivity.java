package com.zjzy.morebit.Activity;

import android.Manifest;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.SdDirPath;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.purchase.PurchaseActivity;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.sys.RequestPermissionUtlis;

import java.io.File;
import java.util.ArrayList;

/*
*
* 饿了么分享
*
* */
public class ShareHungryActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title;
    private LinearLayout btn_back,weixinFriend,weixinCircle,sinaWeibo,qqFriend,qqRoom,ll_more;
    private String shareHundry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_hungry);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();
        initView();
    }

    private void initView() {

        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("饿了么");
        txt_head_title.setTextSize(18);
        txt_head_title.getPaint().setFakeBoldText(true);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        weixinFriend = (LinearLayout) findViewById(R.id.weixinFriend);
        weixinFriend.setOnClickListener(this);
        weixinCircle = (LinearLayout) findViewById(R.id.weixinCircle);
        weixinCircle.setOnClickListener(this);
        sinaWeibo = (LinearLayout) findViewById(R.id.sinaWeibo);
        sinaWeibo.setOnClickListener(this);
        qqFriend = (LinearLayout) findViewById(R.id.qqFriend);
        qqFriend.setOnClickListener(this);
        qqRoom = (LinearLayout) findViewById(R.id.qqRoom);
        qqRoom.setOnClickListener(this);
        ll_more = (LinearLayout) findViewById(R.id.ll_more);
        ll_more.setOnClickListener(this);
        Resources resources = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(resources, R.mipmap.pianyuanbaoyou);
        shareHundry = FileUtils.savePhoto(bmp, SdDirPath.IMAGE_CACHE_PATH, "appshare");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.weixinFriend://微信
                ShareUtil.Image.toWechatFriend(ShareHungryActivity.this, shareHundry, null);
                break;
            case R.id.weixinCircle://微信朋友圈
                ShareUtil.Image.toWechatMoments(ShareHungryActivity.this, shareHundry, null);
                break;
            case R.id.sinaWeibo://微博
                ShareUtil.Image.toSinaWeibo(ShareHungryActivity.this, shareHundry, null);
                break;
            case R.id.qqFriend://QQ
                ShareUtil.Image.toQQFriend(ShareHungryActivity.this, shareHundry, null);
                break;
            case R.id.qqRoom://QQ空间
                ShareUtil.Image.toQQRoom(ShareHungryActivity.this, shareHundry, null);
                break;
            case R.id.ll_more://更多
                permission(ShareUtil.MoreType);
                break;
        }
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
        final ArrayList<File> mUrlArrayList = new ArrayList<>();
        try {
                    String fileName = "";
                    fileName = FileUtils.getPictureName(shareHundry);
                    GoodsUtil.saveImg(ShareHungryActivity.this, shareHundry, fileName,
                            new MyAction.OnResultTwo<File, Integer>() {
                                @Override
                                public void invoke(File arg, Integer arg1) {
                                    if (arg != null) {
                                        mUrlArrayList.add(arg);
                                    }
                                                GoodsUtil.originalShareImage(ShareHungryActivity.this, mUrlArrayList, type);

                                    }
                                @Override
                                public void onError() {

                                }
                            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ShareHungryActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
        }
    }
}
