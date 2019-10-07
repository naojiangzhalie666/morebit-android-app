package com.markermall.cat.utils.netWork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.stetho.common.LogUtil;

/**
 * Created by JerryHo on 2018/6/22
 * Description: 网络连接状态的监听器。通过注册broadcast实现的
 */
public class NetWatchdog {

    private static final String TAG = NetWatchdog.class.getSimpleName();

    private Context mContext;
    //网络变化监听
    private NetChangeListener mNetChangeListener;
    //广播过滤器，监听网络变化
    private IntentFilter mNetIntentFilter = new IntentFilter();
    boolean isUnmReceiver;

    /**
     * 网络变化监听事件
     */
    public interface NetChangeListener {
        /**
         * wifi变为4G
         */
        void onWifiTo4G();

        /**
         * 4G变为wifi
         */
        void on4GToWifi();

        /**
         * 网络断开
         */
        void onNetDisconnected();
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取手机的连接服务管理器，这里是连接管理器类
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobileNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            NetworkInfo.State wifiState = NetworkInfo.State.UNKNOWN;
            NetworkInfo.State mobileState = NetworkInfo.State.UNKNOWN;

            if (wifiNetworkInfo != null) {
                wifiState = wifiNetworkInfo.getState();
            }
            if (mobileNetworkInfo != null) {
                mobileState = mobileNetworkInfo.getState();
            }

            if (NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
                LogUtil.d(TAG, "onWifiTo4G()");
                if (mNetChangeListener != null) {
                    mNetChangeListener.onWifiTo4G();
                }
            } else if (NetworkInfo.State.CONNECTED == wifiState && NetworkInfo.State.CONNECTED != mobileState) {
                if (mNetChangeListener != null) {
                    mNetChangeListener.on4GToWifi();
                }
            } else if (NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED != mobileState) {
                if (mNetChangeListener != null) {
                    mNetChangeListener.onNetDisconnected();
                }
            }
        }
    };


    public NetWatchdog(Context context) {
        mContext = context;
        mNetIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    /**
     * 设置网络变化监听
     *
     * @param l 监听事件
     */
    public void setNetChangeListener(NetChangeListener l) {
        mNetChangeListener = l;
    }

    /**
     * 开始监听
     */
    public void startWatch() {
        try {
            if (mContext!=null)
            mContext.registerReceiver(mReceiver, mNetIntentFilter);
        } catch (Exception e) {
            LogUtil.e("---------"+e.toString());
        }
    }

    /**
     * 结束监听
     */
    public void stopWatch() {
        try {
            if (mContext!=null&&mReceiver!=null&&!isUnmReceiver){
                isUnmReceiver=true;
                mContext.unregisterReceiver(mReceiver);
            }
        } catch (Exception e) {
            LogUtil.e("---------"+e.toString());
        }
    }


    /**
     * 静态方法获取是否有网络连接
     *
     * @param context 上下文
     * @return 是否连接
     */
    public static boolean hasNet(Context context) {
        //获取手机的连接服务管理器，这里是连接管理器类
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        NetworkInfo.State wifiState = NetworkInfo.State.UNKNOWN;
        NetworkInfo.State mobileState = NetworkInfo.State.UNKNOWN;

        if (wifiNetworkInfo != null) {
            wifiState = wifiNetworkInfo.getState();
        }
        if (mobileNetworkInfo != null) {
            mobileState = mobileNetworkInfo.getState();
        }

        if (NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED != mobileState) {
            return false;
        }

        return true;
    }

    /**
     * 静态判断是不是4G网络
     *
     * @param context 上下文
     * @return 是否是4G
     */
    public static boolean is4GConnected(Context context) {
        //获取手机的连接服务管理器，这里是连接管理器类
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mobileNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        NetworkInfo.State mobileState = NetworkInfo.State.UNKNOWN;

        if (mobileNetworkInfo != null) {
            mobileState = mobileNetworkInfo.getState();
        }

        return NetworkInfo.State.CONNECTED == mobileState;
    }

}
