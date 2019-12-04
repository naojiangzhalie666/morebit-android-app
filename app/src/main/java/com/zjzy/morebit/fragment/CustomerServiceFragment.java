package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.requestbodybean.RequestInviteCodeBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import io.reactivex.annotations.NonNull;


/**
 * 专属客服-界面
 */
public class CustomerServiceFragment extends BaseFragment implements View.OnClickListener {

    private TextView copy_wxnum, mServiceWechat, mSaveQrcode;
    private ImageView mQrcode;
    private Bitmap mBitmap;
    private String localPath;
    private RelativeLayout mRlWxCode;
    private String mWxcode;

    private String mPictureName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_customer_service, container, false);
        inview(view);
        return view;
    }

    public void inview(View view) {
        ActivityStyleUtil.initSystemBar(getActivity(), R.color.color_757575); //设置标题栏颜色值
        copy_wxnum = (TextView) view.findViewById(R.id.copy_wxnum);
        mQrcode = (ImageView) view.findViewById(R.id.iv_qrcode);
        mSaveQrcode = (TextView) view.findViewById(R.id.save_qrcode);
        mServiceWechat = (TextView) view.findViewById(R.id.service_wechat);
        mRlWxCode = (RelativeLayout) view.findViewById(R.id.ll_wx_code);
        copy_wxnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mWxcode)) {
                    return;
                }
                AppUtil.coayTextPutNative((Activity) getActivity(), mWxcode);
                Toast.makeText(getActivity(), "已复制到粘贴版", Toast.LENGTH_SHORT).show();
            }
        });
        getQrcodePicture();
        mSaveQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(localPath)) {
                    if (mBitmap == null) {
                        Toast.makeText(getActivity(), "二维码还没有生成", Toast.LENGTH_SHORT).show();
                    } else {

                        Uri uri = GoodsUtil.saveBitmap(getActivity(), mBitmap, mPictureName);
                        if (uri != null) {
                            ViewShowUtils.showShortToast(getActivity(), "保存成功");
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "二维码已保存： " + localPath, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;

        }
    }


    public void getQrcodePicture() {

        RxHttp.getInstance().getUsersService().getServiceQrcode(new RequestInviteCodeBean().setWxShowType(2))
                .compose(RxUtils.<BaseResponse<TeamInfo>>switchSchedulers())
                .compose(this.<BaseResponse<TeamInfo>>bindToLifecycle())
                .subscribe(new DataObserver<TeamInfo>() {
                    @Override
                    protected void onSuccess(TeamInfo data) {

                        if (!TextUtils.isEmpty(data.getWxQrCode())) {
                            mPictureName = FileUtils.getPictureName(data.getWxQrCode());
                            try {// ios 没有压缩图片 有可能内存溢出  mQrcode.setImageBitmap(resource);


                                //                                //设置图片圆角角度
                                RequestOptions options = new RequestOptions()
                                        .placeholder(R.drawable.service_person_qrcode)
                                        .error(R.drawable.service_person_qrcode);
                                LoadImgUtils.getBitmapObservable(getActivity(), data.getWxQrCode(), options)
                                        .subscribe(new CallBackObserver<Bitmap>() {
                                            @Override
                                            public void onNext(@NonNull Bitmap bitmap) {
                                                mBitmap = bitmap;
                                                mQrcode.setImageBitmap(bitmap);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                            }
                                        });

                            } catch (Exception e) {
                                LoadImgUtils.setImg(getActivity(), mQrcode, data.getWxQrCode());
                            }
                        }
                        if (TextUtils.isEmpty(data.getWxNumber())) {
                            mRlWxCode.setVisibility(View.GONE);
                        } else {
                            mServiceWechat.setText(getString(R.string.exclusiveWechat, data.getWxNumber()));
                            mWxcode = data.getWxNumber();
                            mRlWxCode.setVisibility(View.VISIBLE);
                        }

                    }
                });


        //        RxHttp.getInstance().getUsersService().getServiceQrcode( UserLocalData.getUser(getActivity()).getInviteCode())
        //                .compose(RxUtils.<BaseResponse<ExclusiveWechat>>switchSchedulers())
        //                .compose(this.<BaseResponse<ExclusiveWechat>>bindToLifecycle())
        //                .subscribe(new DataObserver<ExclusiveWechat>() {
        //                    @Override
        //                    protected void onSuccess(ExclusiveWechat data) {
        //
        //                        if (!TextUtils.isEmpty(data.getWxQrCode())) {
        //                            mPictureName = FileUtils.getPictureName(data.getWxQrCode());
        //                            try {// ios 没有压缩图片 有可能内存溢出  mQrcode.setImageBitmap(resource);
        //
        //
        ////                                //设置图片圆角角度
        //                                RequestOptions options = new RequestOptions()
        //                                        .placeholder(R.drawable.service_person_qrcode)
        //                                        .error(R.drawable.service_person_qrcode);
        //                                LoadImgUtils.getBitmapObservable(getActivity(), data.getWxQrCode(), options )
        //                                        .subscribe(new CallBackObserver<Bitmap>() {
        //                                            @Override
        //                                            public void onNext(@NonNull Bitmap bitmap) {
        //                                                mBitmap = bitmap;
        //                                                mQrcode.setImageBitmap(bitmap);
        //                                            }
        //
        //                                            @Override
        //                                            public void onError(Throwable e) {
        //                                            }
        //                                        });
        //
        //                            } catch (Exception e) {
        //                                LoadImgUtils.setImg(getActivity(), mQrcode, data.getWxQrCode());
        //                            }
        //                        }
        //                        if (TextUtils.isEmpty(data.getWxNumber())) {
        //                            mRlWxCode.setVisibility(View.GONE);
        //                        } else {
        //                            mServiceWechat.setText(getString(R.string.exclusiveWechat, data.getWxNumber()));
        //                            mWxcode = data.getWxNumber();
        //                            mRlWxCode.setVisibility(View.VISIBLE);
        //                        }
        //
        //                    }
        //                });
    }
}
