package com.markermall.cat.login.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.markermall.cat.Module.common.Dialog.BuildQrcodeDialog;
import com.markermall.cat.R;
import com.markermall.cat.login.contract.ExclusiveWeChatContract;
import com.markermall.cat.login.presenter.ExclusiveWeChatPresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.mvp.base.frame.MvpFragment;
import com.markermall.cat.network.CallBackObserver;
import com.markermall.cat.pojo.TeamInfo;
import com.markermall.cat.utils.AppUtil;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.FileUtils;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.MyLog;
import com.markermall.cat.utils.OpenFragmentUtils;
import com.markermall.cat.utils.PageToUtil;
import com.markermall.cat.utils.ViewShowUtils;
import com.markermall.cat.utils.helper.ActivityLifeHelper;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

/**
 * Created by YangBoTian on 2018/8/7.
 * 注册专属微信号
 */

public class ExclusiveWeChatFragment extends MvpFragment<ExclusiveWeChatPresenter> implements ExclusiveWeChatContract.View {


    @BindView(R.id.text_qrcode)
    TextView mQrcode;
    @BindView(R.id.qrcode)
    ImageView mImgQrocde;

    private String mPictureName;
    private Bitmap mBitmap;
    private String mId;
    private String mWxCode;
    private Dialog buildQrcodeDialog;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(null != activity){
            ((LoginSinglePaneActivity)activity).setSwipeBackEnable(false);
        }
    }


    public static void start(Activity activity, String id) {
//        Intent intent = new Intent(activity, ExclusiveWeChatFragment.class);
//        intent.putExtra("id", id);
//        activity.startActivity(intent);

        Bundle extras = new Bundle();
        extras.putString(C.Extras.ID, id);
        extras.putBoolean(C.Extras.openFragment_isSysBar, true);
        extras.putBoolean(C.Extras.isBackPressed, false);
        OpenFragmentUtils.goToLoginSimpleFragment(activity, ExclusiveWeChatFragment.class.getName(), extras);
    }


    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.exclusive_wechat;
    }


    @Override
    protected void initData() {
        mPresenter.getServiceQrcode(this, mId);
    }

    @Override
    protected void initView(View view) {
        mId = getArguments().getString("id");
    }


    @OnClick(R.id.skip)
    public void skip() {
        gotoMain();
    }



    private void gotoMain() {
//        ActivityLifeHelper.getInstance().finishActivity(MainActivity.class);
        ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
    }

    @OnClick(R.id.copy_exclusive_wechat)
    public void copy() {
        AppUtil.coayText(getActivity(), mWxCode);
        Toast.makeText(getActivity(), "已复制到粘贴版", Toast.LENGTH_SHORT).show();
      //  PageToUtil.goToWeixin(getActivity());
    }

    @OnClick({R.id.qrcode,R.id.saveQRBtn})
    public void savaQrcode() {
        if (mBitmap != null) {
            buildQrcodeDialog = new BuildQrcodeDialog(getActivity(), R
                    .style.dialog, mBitmap, mPictureName);
            buildQrcodeDialog.show();
        }
    }

    @OnClick({R.id.openWxBtn})
    public void openWx(){
        if (!AppUtil.isWeixinAvilible(getActivity())) {
            ViewShowUtils.showShortToast(getActivity(), "请先安装微信客户端");
            return;
        }

        PageToUtil.goToWeixin(getActivity());
    }

    @Override
    public void showFinally() {

    }

    @Override
    public void showData(TeamInfo exclusiveWechat) {
        if (exclusiveWechat == null) {
            return;
        }

        if (!TextUtils.isEmpty(exclusiveWechat.getWxQrCode())) {
            mPictureName = FileUtils.getPictureName(exclusiveWechat.getWxQrCode());
            try {// ios 没有压缩图片 有可能内存溢出  mQrcode.setImageBitmap(resource);


                //设置图片圆角角度
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo);
                LoadImgUtils.getBitmapObservable(getActivity(), exclusiveWechat.getWxQrCode(), options)
                        .subscribe(new CallBackObserver<Bitmap>() {
                            @Override
                            public void onNext(@NonNull Bitmap bitmap) {
                                mBitmap = bitmap;
                                mImgQrocde.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onError(Throwable e) {
//                                                super.onError(e);
                                MyLog.i("test", "onLoadFailed: " + e.toString());
                            }
                        });
            } catch (Exception e) {
                LoadImgUtils.setImg(getActivity(), mImgQrocde, exclusiveWechat.getWxQrCode());
            }
        }else{
            mImgQrocde.setImageResource(R.drawable.logo);
        }
        mWxCode = exclusiveWechat.getWxNumber();
        mQrcode.setText(mWxCode);
    }

    @Override
    public void showFailureMessage(String errorMsg) {

    }




}
