package com.zjzy.morebit.goods.shopping.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.MyLog;

/**
 * Created by fengrs on 2019/5/16.
 * 备注:
 */

public class ForeshowItemTiemView extends android.support.v7.widget.AppCompatTextView {



    public ForeshowItemTiemView(Context context) {
        this(context, null);
    }

    public ForeshowItemTiemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        setLayoutParams(layoutParams);
        setGravity(Gravity.CENTER);
        setMaxEms(1);
        setEllipsize(TextUtils.TruncateAt.END);
        setTextColor(ContextCompat.getColor(context, R.color.white));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
    }

    public void setTimerData(final Context context, final ShopGoodInfo info) {

        int countDown = info.getCountDown();
        if (countDown == 0) {
            setBackgroundResource(R.drawable.bg_shopping_foreshow_tiem_1);
            setText(context.getString(R.string.zero_time)+ context.getString(R.string.open_dot));
            info.isCountDownEnd = true;
        } else if (countDown <= 60) {
            setBackgroundResource(R.drawable.bg_shopping_foreshow_tiem_2);
            String hms = DateTimeUtils.getCountTimeByLong(countDown * 60 * 1000);
            MyLog.d("CountDownTimer  onTick  "+hms);
            if (!TextUtils.isEmpty(hms))
                setText(hms + context.getString(R.string.open_later));
        } else {
            setBackgroundResource(R.drawable.bg_shopping_foreshow_tiem_1);
            info.setCountDownStr(info.getCountDownStr() == null ? "" : info.getCountDownStr());
            setText(info.getCountDownStr() + context.getString(R.string.open_dot));
        }
    }

}
