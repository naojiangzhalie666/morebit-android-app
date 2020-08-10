package com.zjzy.morebit.utils;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zjzy.morebit.R;

/**
 *
 *
 *
 * @date :  2019/11/14 10:53
 * Summary:
 */
public class CountTimeView extends LinearLayout implements Runnable {
    public static final String TAG = CountTimeView.class.getSimpleName();
    private Context mContext;
    private TextView mHourTv;//时
    private TextView mMinTv;//分
    private TextView mSecondTv;//秒
    private long mTime = 0;
    private Handler mHandler;
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public CountTimeView(Context context) {
        this(context, null);
    }

    public CountTimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        mHandler = new Handler(Looper.getMainLooper());
        mHourTv = findViewById(R.id.hour_tv);
        mMinTv = findViewById(R.id.min_tv);
        mSecondTv = findViewById(R.id.second_tv);
    }


    @Override
    public void run() {
        mTime = mTime - 1;
        if (mTime <= 0) {
            notifyDataSetChanged();
        } else {
            mHandler.postDelayed(this, 1000);
        }
        long[] timeDuring = getTimeDuring(mTime);
        setTimeText(mHourTv, timeDuring[0] < 10 ? String.format("0%s", timeDuring[0]) : String.valueOf(timeDuring[0]));
        setTimeText(mMinTv, timeDuring[1] < 10 ? String.format("0%s", timeDuring[1]) : String.valueOf(timeDuring[1]));
        setTimeText(mSecondTv, timeDuring[2] < 10 ? String.format("0%s", timeDuring[2]) : String.valueOf(timeDuring[2]));
    }

    private void setTimeText(TextView textView, String cont) {
        textView.setText(cont);
    }

    /**
     * 倒计时
     *
     * @param time 秒
     */
    public void startLimitedTime(long time) {
        mTime = time;
        if (mTime > 0) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.post(this);
        }
    }

    /**
     * 距离秒杀还剩多少时间
     * @return
     */
    public long getLimitedTime(){
        return mTime;
    }

    public final void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public final void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public final void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public final void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    private long[] getTimeDuring(long second) {
        long hours = (second % (60 * 60 * 24)) / (60 * 60);
        long minutes = (second % (60 * 60)) / (60);
        long seconds = second % 60;
        return new long[]{hours, minutes, seconds};
    }

    /**
     *
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / (60);
        long seconds = mss % 60;
        return days + " 天 " + hours + ":" + minutes + ":"
                + seconds;
    }
}
