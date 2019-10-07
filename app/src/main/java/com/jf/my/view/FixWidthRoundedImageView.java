package com.jf.my.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * @Author: wuchaowen
 * @Description: 宽度自适应
 **/
public class FixWidthRoundedImageView extends RoundedImageView {

    public FixWidthRoundedImageView(Context context) {
        super(context);
    }

    public FixWidthRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if(drawable != null){
            int width = drawable.getMinimumWidth();
            int height = drawable.getMinimumHeight();
            float scale = (float)width/height;
            int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);
            int widthMeasure = (int)(heightMeasure*scale);
            widthMeasureSpec =  MeasureSpec.makeMeasureSpec(widthMeasure, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
