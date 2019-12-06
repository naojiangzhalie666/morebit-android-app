package com.zjzy.morebit.Module.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.LocalData.PushMsgLocalData;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.SinglePaneActivity;
import com.zjzy.morebit.circle.ui.ReleaseManageActivity;
import com.zjzy.morebit.info.ui.fragment.EarningsFragment;
import com.zjzy.morebit.info.ui.fragment.MsgEarningsFragment;
import com.zjzy.morebit.info.ui.fragment.MsgFansFragment;
import com.zjzy.morebit.info.ui.fragment.MsgFeedbackFragment;
import com.zjzy.morebit.info.ui.fragment.MsgSysFragment;
import com.zjzy.morebit.main.ui.CollectFragment2;
import com.zjzy.morebit.pojo.PushMsgInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";

    //	public  static
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);
                //接受到信息
                try {
                    String getMsgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
                    String getTitle = bundle.getString(JPushInterface.EXTRA_TITLE);
                    String getContent = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                    PushMsgInfo pushMsgInfo = JSON.parseObject(getContent, PushMsgInfo.class);
                    pushMsgInfo.setMsgId(getMsgId);
                    List<PushMsgInfo> getArrMsg = PushMsgLocalData.getPushList(context);
                    if (getArrMsg == null) {
                        getArrMsg = new ArrayList<>();
                    }
                    getArrMsg.add(pushMsgInfo);
                    PushMsgLocalData.setPushList(context, JSON.toJSONString(getArrMsg));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
                String getMsgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
                try {
                    List<PushMsgInfo> getArrMsg = PushMsgLocalData.getPushList(context);
                    if (getArrMsg != null) {
                        for (int i = 0; i < getArrMsg.size(); i++) {
                            PushMsgInfo pushMsgInfo = getArrMsg.get(i);
                            if (getMsgId.equals(pushMsgInfo.getMsgId())) {
                                startActivity(context, pushMsgInfo);
                                //删除这条消息
                                getArrMsg.remove(i);
                                PushMsgLocalData.setPushList(context, JSON.toJSONString(getArrMsg));
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    public static void startActivity(Context context, PushMsgInfo pushMsgInfo) {


        int pushType = Integer.valueOf(pushMsgInfo.getPush_type());
        MyLog.i("test","pushType: " +pushType);
        Intent it = null;
        Bundle itBundle = new Bundle();
        switch (pushType) {
            case C.Push.collect://收藏
                it = new Intent(context, SinglePaneActivity.class);
                it.putExtra("fragment",  CollectFragment2.class.getName());
                it.putExtras(itBundle);
                break;
            case C.Push.homePage://首页
            case C.Push.fenlei://分类
            case C.Push.partner://我的
            case C.Push.circle://发圈
                it = new Intent(context, MainActivity.class);
                break;
            case C.Push.moenyMsg://提现消息
                it = new Intent(context, SinglePaneActivity.class);
                it.putExtra(C.Extras.openFragment_isSysBar, true);
                it.putExtra("fragment",  EarningsFragment.class.getName());
                it.putExtras(itBundle);

                break;

            case C.Push.sysMsg://系统消息
                MyLog.i("test","系统");
                it = new Intent(context, SinglePaneActivity.class);
                it.putExtra("fragment",  MsgSysFragment.class.getName());
                it.putExtras(itBundle);
                break;
            case C.Push.awardMsg://奖励消息
            case C.Push.earningsMsg://收益
                it = new Intent(context, SinglePaneActivity.class);
                it.putExtra("fragment",  MsgEarningsFragment.class.getName());
                it.putExtras(itBundle);
                break;
            case C.Push.goodsDetail://商品推送
                it = new Intent(context, GoodsDetailActivity.class);
                ShopGoodInfo info = new ShopGoodInfo();
                info.setTitle(pushMsgInfo.getContent());
                info.setTaobao(pushMsgInfo.getGoods_id());
                itBundle.putSerializable(C.Extras.GOODSBEAN, info);
                break;
            case C.Push.GO_H5:

                //跳转到网页
                it = new Intent(context, ShowWebActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("title", pushMsgInfo.getTitle());
                bundle2.putString("url", pushMsgInfo.getSchedule_id());
                it.putExtras(bundle2);
                break;
            case C.Push.FEEDBACK_MSG:        //回复反馈消息
                MyLog.i("test","系统");
                it = new Intent(context, SinglePaneActivity.class);
                it.putExtra("fragment",  MsgFeedbackFragment.class.getName());
                it.putExtras(itBundle);
                break;
            case C.Push.FANS_MSG:        //粉丝消息
                it = new Intent(context, SinglePaneActivity.class);
                it.putExtra("fragment",  MsgFansFragment.class.getName());
                it.putExtras(itBundle);
                break;
            case C.Push.SHARE_MSG_PASS:
            case C.Push.SHARE_MSG_FAILURE:
            case C.Push.SHARE_MSG_UNSHELVE:
                it = new Intent(context, ReleaseManageActivity.class);
                it.putExtra("pushType",pushType);
                it.putExtras(itBundle);
            default:
                break;
        }

        itBundle.putInt(C.Extras.pushType, pushType);
        it.putExtras(itBundle);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(it);
    }
    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to goTaobaoTag
    private void processCustomMessage(Context context, Bundle bundle) {
//		if (goTaobaoTag.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(goTaobaoTag.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(goTaobaoTag.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(goTaobaoTag.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//		}
    }
}
