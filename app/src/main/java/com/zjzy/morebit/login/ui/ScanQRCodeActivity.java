package com.zjzy.morebit.login.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.login.myZxing.CaptureFragment;
import com.zjzy.morebit.login.myZxing.CodeUtils;
import com.zjzy.morebit.login.myZxing.QRCodeDecoder;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LogUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 二维码扫描
 */
public class ScanQRCodeActivity extends BaseActivity {

    @BindView(R.id.tv_photo)
    TextView mTvPhoto;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv_flashlight)
    TextView mTvFlashlight;
    private CaptureFragment captureFragment;
    private String ewmTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ActivityStyleUtil.initLucencySystemBar(this);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面

//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
//        SurfaceHolder holder = surfaceView.getHolder();
//        holder.setFixedSize(1080,1920);
        CodeUtils.setFragmentArgs(captureFragment, R.layout.view_my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

    }


    public static boolean isOpen = false;


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            getInviteCode(result);

        }

        @Override
        public void onAnalyzeFailed() {
            ViewShowUtils.showShortToast(ScanQRCodeActivity.this, "识别失败,请重试");
        }
    };


    private void prasePhoto(final String path) {
        Observable.just(path)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        String s1 = QRCodeDecoder.syncDecodeQRCode(path);
                        return Observable.just(s1);

                    }
                })
                .compose(RxUtils.<String>switchSchedulers())
                .subscribe(new CallBackObserver<String>() {
                    @Override
                    public void onNext(String s) {
//                        ViewShowUtils.showShortToast(ScanQRCodeActivity.this, s);
                        getInviteCode(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ViewShowUtils.showShortToast(ScanQRCodeActivity.this, "识别失败, 请重试哦");
                        LogUtils.Log("ScanQRCodeActivity", "onError  " + e.getMessage());
                    }
                });
    }

    private void getInviteCode(String s) {
        try {
            LogUtils.Log("ScanQRCodeActivity", "InviteCode  " + s);
            if (!TextUtils.isEmpty(s)) {
                Map<String, String> stringStringMap = AppUtil.URLRequest(s);
                int isError = 0;
                for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                    String key = entry.getKey().toString();
                    String value = entry.getValue().toString();
                    if (key.contains("invite_code") || key.contains("invite") || key.contains("inviteCode")) {
                        if (!TextUtils.isEmpty(value)) {
                            isError = 1;
                            Intent intent = new Intent();
                            intent.putExtra(C.Extras.ewmText, value);
                            setResult(LoginEditInviteFragment.REQ_QR_CODE, intent);
                            ScanQRCodeActivity.this.finish();
                            return;
                        }
                    }
                }
                if (isError == 0) {
                    ViewShowUtils.showShortToast(ScanQRCodeActivity.this, "识别失败,请重试!");
                }

//                String[] split = s.split("inviteCode=");
//                String s1 = split[1];
//                String[] split1 = s1.split("&");
//                ewmTest = split1[0];
//                if (!TextUtils.isEmpty(split1[0])) {
//                    Intent intent = new Intent();
//                    intent.putExtra(C.Extras.ewmText, split1[0]);
//
//                    setResult(LoginEditInviteFragment.REQ_QR_CODE, intent);
//                    ScanQRCodeActivity.this.finish();
//                } else {
//                    ViewShowUtils.showShortToast(ScanQRCodeActivity.this, "识别失败,请重试!");
//                }
            } else {
                ViewShowUtils.showShortToast(ScanQRCodeActivity.this, "识别失败, 请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ViewShowUtils.showShortToast(ScanQRCodeActivity.this, "识别失败, 请重试哦");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300) {
            if (data != null) {
                Uri uri = data.getData();
                prasePhoto(ImageUtil.getImageAbsolutePath(this, uri));
            }
//            if (data != null) {
//                Uri uri = data.getData();
//                try {
//                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
//                        @Override
//                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//                            Toast.makeText(ScanQRCodeActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void onAnalyzeFailed() {
//                            Toast.makeText(ScanQRCodeActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }


    @OnClick({R.id.tv_photo, R.id.tv_flashlight, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_photo:
                if (AppUtil.isFastClick(500)) {
                    return;
                }
//                //调用系统图库，选择图片
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                //返回结果和标识
                startActivityForResult(intent, 300);
                CodeUtils.isLightEnable(false);
                mTvFlashlight.setSelected(false);
                isOpen = false;
                break;
            case R.id.tv_flashlight:
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                    mTvFlashlight.setSelected(true);
                } else {
                    CodeUtils.isLightEnable(false);
                    mTvFlashlight.setSelected(false);
                    isOpen = false;
                }
                break;
            case R.id.iv_back:
                finish();
//                getInviteCode("http://a.app.qq.com/o/simple.jsp?pkgname=com.zjzy.morebit&inviteCode=ljxnbq&invite=ljxnbq&token=zhitu_uSVnYLWLeGpk");
                break;
        }
    }



}
