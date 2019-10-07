package com.jf.my.utils.fire;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.jf.my.App;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.myInfo.OssCallbackBean;
import com.jf.my.pojo.myInfo.OssKeyBean;
import com.jf.my.utils.DateTimeUtils;
import com.jf.my.utils.MyGsonUtils;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.SharedPreferencesUtils;
import com.jf.my.utils.action.MyAction;

/**
 * Created by fengrs on 2018/12/27.
 * 备注:
 */

public class OssManage {

    private static OssManage sOssManage;
    private OSS mOss;
    private OssCallbackBean mCallbackBean;
    private OssKeyBean mOssKeyBean;

    public static OssManage newInstance() {
        if (sOssManage == null) {
            sOssManage = new OssManage();
        }
        return sOssManage;
    }

    /**
     * 初始化oss
     *
     * @return
     */
    public void initOss(final MyAction.OnResult<OSS> action) {
        if (mOssKeyBean != null) return;

        RxHttp.getInstance().getSysteService().getOssKey()
                .compose(RxUtils.<BaseResponse<OssKeyBean>>switchSchedulers())
                .subscribe(new DataObserver<OssKeyBean>() {

                    @Override
                    protected void onSuccess(final OssKeyBean data) {
                        mOssKeyBean = data;
                        OSSAuthCredentialsProvider credentialProvider = new OSSAuthCredentialsProvider(data.getAccessKeyId(), data.getAccessKeySecret(), data.getSecurityToken(), data.getExpiration());
                        ClientConfiguration conf = new ClientConfiguration();
                        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                        byte[] decode = Base64.decode(data.getCallback(), Base64.NO_WRAP);
                        String s = new String(decode);
                        MyLog.i("  Callback = " + s);
                        try {
                            mCallbackBean = (OssCallbackBean) MyGsonUtils.jsonToBean(s, OssCallbackBean.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (action != null)
                                action.onError();

                        }
//                        "http://oss-cn-shenzhen.aliyuncs.com"
                        mOss = new OSSClient(App.getAppContext(), data.getHost(), credentialProvider, conf);
                        if (action != null)
                            action.invoke(mOss);
                        saveOssTokenSp(data.getExpiration()); //保存token的生成时间,用于更新token的时间判断
                    }
                });

    }

    /**
     * 上传图片
     *
     * @return
     */
    public void putFielToOss(final String filePath, final MyAction.OnResult<String> action) {
        if(!TextUtils.isEmpty(filePath)&&filePath.contains("http")){
            action.invoke(filePath);
        } else {
            if (mCallbackBean == null || mOssKeyBean == null || mOss == null || tokenIsExpire()) {
                initOss(new MyAction.OnResult<OSS>() {
                    @Override
                    public void invoke(OSS arg) {
                        putImgToOss(filePath, action);
                    }

                    @Override
                    public void onError() {
                        App.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                action.onError();
                            }
                        });
                    }
                });
            } else {
                putImgToOss(filePath, action);
            }
        }


    }

    /**
     * 配置上传信息
     *
     * @param filePath
     * @param action
     */

    private void putImgToOss(String filePath, final MyAction.OnResult<String> action) {
        final String objectKey = DateTimeUtils.getTimePath() + "/jpg/" + System.currentTimeMillis() + ".jpg";
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(mOssKeyBean.getBucket(), objectKey, filePath);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");
        put.setMetadata(metadata);

        if (mCallbackBean != null) {
            final String callbackUrl = mCallbackBean.getCallbackUrl();
            final String callbackBody = mCallbackBean.getCallbackBody();
            put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                }
            });
        }
        OSSAsyncTask task = mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                final String url = mOss.presignPublicObjectURL(mOssKeyBean.getBucket(), objectKey);
                App.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (action != null)
                            action.invoke(url);
                    }
                });
                Log.d("PutObject", "url  =  " + url);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                App.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (action != null)
                            action.onError();
                    }
                });
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    /**
     * 判断oss的Token是否过期
     */
    private boolean tokenIsExpire(){
        boolean isExpire = false;
        String lastOssTime = (String) SharedPreferencesUtils.get(App.getAppContext(),"lastTime","");
        if(!TextUtils.isEmpty(lastOssTime)){
            // 取上次oss的时间,,实际测试有效时间只有6,7分钟,这里以5分钟更新一次token,防止上传失败.
            long currentOssTime = Long.valueOf(lastOssTime)+ 5 * 60 * 1000 ;
            long currentTime = System.currentTimeMillis();
            if(currentTime >= currentOssTime){
                //token已过期
                isExpire = true;
                mOssKeyBean = null;
                mOss = null;
            }
        }
        return isExpire;
    }



    /**
     * 保存oss的token 生成时间,用于后面判断token是否过期

     */
    private void saveOssTokenSp(String expire){
        /**
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            String currentTime = String.valueOf(df.parse(time).getTime()) ;
            SharedPreferencesUtils.put(App.getAppContext(),"lastTime",currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
         **/
        if(!TextUtils.isEmpty(expire)){
            //因为oss的token返回的过期时间有时30分钟,有时20分钟,不好判断,这里以oss的初始时间为准
            SharedPreferencesUtils.put(App.getAppContext(),"lastTime",System.currentTimeMillis()+"");
        }

    }
}
