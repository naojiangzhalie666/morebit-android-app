package com.markermall.cat.Module.push;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.markermall.cat.MainActivity;
import com.markermall.cat.pojo.JPushSkipBean;
import com.markermall.cat.pojo.PushMsgInfo;
import com.markermall.cat.utils.MyGsonUtils;
import com.markermall.cat.utils.MyLog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by fengrs on 2018/12/10.
 * 备注: 接受极光离线推送
 *
 */

public class JPushReceiveActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 华为离线推送
        Uri data = getIntent().getData();
        if (data != null) {
            String s = data.toString();
            MyLog.e("[MyReceiver", s);
            if (!TextUtils.isEmpty(s)) {
                try {
                    JPushSkipBean jPushSkipBean = (JPushSkipBean) MyGsonUtils.jsonToBean(s, JPushSkipBean.class);
                    PushMsgInfo extras = jPushSkipBean.getN_extras();
                    MyReceiver.startActivity(this, extras);
                } catch (Exception e) {
                    e.printStackTrace();
                    startMain();
                }
            } else {
                startMain();
            }
        } else {
            startMain();

        }
        this.finish();
    }

    private void startMain() {
        Intent intent = new Intent(JPushReceiveActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
