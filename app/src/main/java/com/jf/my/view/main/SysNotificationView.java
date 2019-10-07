package com.jf.my.view.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.my.App;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.R;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.utils.C;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;

/**
 * Created by fengrs on 2019/4/17.
 * 备注:
 */

public class SysNotificationView {

    private View mSysNotificationTextView;
    private View mSysNotificationImgView;

    public void addSysNotificationView(final Activity activity, final RelativeLayout rl_urgency_notifi, final ImageInfo imageInfo) {
        if(rl_urgency_notifi == null)return;
        ArrayList<String> ids = (ArrayList<String>) App.getACache().getAsObject(UserLocalData.getUser().getPhone() + C.sp.SysNoticeDataIds);
        if (ids != null && ids.contains(imageInfo.getId() + ""))
            return;
        //0：图片 2: 文本
        if (imageInfo.getMediaType()  == 2) {
            if (mSysNotificationTextView == null) {
                mSysNotificationTextView = LayoutInflater.from(activity).inflate(R.layout.view_urgency_notification_text, null);
            }
            mSysNotificationTextView.findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BannerInitiateUtils.gotoAction(activity, imageInfo);
                }
            });
            mSysNotificationTextView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    putSysNoticeDataId(imageInfo);
                    rl_urgency_notifi.removeAllViews();
                }
            });
            TextView tv_title = mSysNotificationTextView.findViewById(R.id.title);
            tv_title.setText(imageInfo.getDesc());
            tv_title.setSelected(true);
            rl_urgency_notifi.removeAllViews();
            rl_urgency_notifi.addView(mSysNotificationTextView);
        } else if (imageInfo.getMediaType()  == 0) {
            if (mSysNotificationImgView == null) {
                mSysNotificationImgView = LayoutInflater.from(activity).inflate(R.layout.view_urgency_notification_img, null);
            }
            mSysNotificationImgView.findViewById(R.id.as_banner_make_money).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BannerInitiateUtils.gotoAction(activity, imageInfo);

                }
            });
            mSysNotificationImgView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    putSysNoticeDataId(imageInfo);
                    rl_urgency_notifi.removeAllViews();
                }
            });
            ImageView iv_icon = mSysNotificationImgView.findViewById(R.id.iv_icon);
            LoadImgUtils.loadingCornerTop(activity, iv_icon, imageInfo.getPicture(),3);
            rl_urgency_notifi.removeAllViews();
            rl_urgency_notifi.addView(mSysNotificationImgView);
        }
    }

    private void putSysNoticeDataId(ImageInfo imageInfo) {
        ArrayList<String> ids = (ArrayList<String>) App.getACache().getAsObject(UserLocalData.getUser().getPhone() + C.sp.SysNoticeDataIds);
        if (ids == null) {
            ids = new ArrayList<>();
        }
        ids.add(imageInfo.getId() + "");
        App.getACache().put(UserLocalData.getUser().getPhone() + C.sp.SysNoticeDataIds, ids);
    }

}
