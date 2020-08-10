package com.zjzy.morebit.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

public class LimitedTimePageTitleView extends LinearLayout implements IPagerTitleView {

    private TextView mTimeTv;
    private TextView mTipTv;
    private int selectTipsColor=-1;
    private int selectTimeColor=-1;
    private int unSelectTipsColor=-1;
    private int unSelectTimeColor=-1;
    private int tipsSizeSp=11;
    private int timeSizeSp=19;
    private int selectTipsBg=-1;
    private Context mContext;
    public LimitedTimePageTitleView(Context context) {
        this(context, null);
    }

    public LimitedTimePageTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LimitedTimePageTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        setOrientation(VERTICAL);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.item_limited_time_title_layout, this, true);
        mTimeTv = findViewById(R.id.time_tv);
        mTipTv = findViewById(R.id.tips_tv);
        selectTipsColor = getResources().getColor(R.color.color_F05557);
        selectTimeColor =  getResources().getColor(R.color.white);
        unSelectTipsColor = getResources().getColor(R.color.color_FCDDDD);
        unSelectTimeColor = getResources().getColor(R.color.color_FCDDDD);
        setGravity(Gravity.CENTER);
        mTipTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,tipsSizeSp);
        mTimeTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,timeSizeSp);
        selectTipsBg=R.drawable.bg_ffffff_8dp;
    }

    @Override
    public void onSelected(int index, int totalCount) {
        mTimeTv.setTextColor(selectTimeColor);
        mTipTv.setTextColor(selectTipsColor);
        mTipTv.setBackgroundResource(selectTipsBg);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        mTimeTv.setTextColor(unSelectTimeColor);
        mTipTv.setTextColor(unSelectTipsColor);
        mTipTv.setBackground(null);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
    }

    public void setTimeText(String timeText){
        if (mTimeTv!=null){
            mTimeTv.setText(timeText);
        }
    }

    public void setTipsText(String tips){
        if (mTipTv!=null){
            mTipTv.setText(tips);
        }
    }
    public void setTimeUnselectColor(@ColorInt int unSelectColor){
        unSelectTimeColor=unSelectColor;
    }
    public void setTipsUnselectColor(@ColorInt int unSelectColor){
        unSelectTipsColor=unSelectColor;
    }
    public void setTimeSelectColor(@ColorInt int selectColor){
        selectTimeColor=selectColor;
    }
    public void setTipsSelectColor(@ColorInt int selectColor){
        selectTipsColor=selectColor;
    }

    /**
     * tips的背景
     * @param drawRes
     */
    public void setSelectTipsBg(@DrawableRes int drawRes){
        selectTipsBg=drawRes;
    }

    public void setTipsSizeSp(int sizeSp){
        this.tipsSizeSp=sizeSp;
        if (mTipTv!=null){
            mTipTv.setTextSize(tipsSizeSp);
        }
    }

    public void setTimeSizeSp(int sizeSp){
        this.timeSizeSp=sizeSp;
        if (mTimeTv!=null){
            mTimeTv.setTextSize(timeSizeSp);
        }
    }
    public void setTipsPadding(int left, int top, int right, int bottom){
        if (mTipTv!=null){
            mTipTv.setPadding(left,top,right,bottom);
        }
    }
}