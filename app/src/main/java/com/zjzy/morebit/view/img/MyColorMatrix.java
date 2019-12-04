package com.zjzy.morebit.view.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zjzy.morebit.R;

public class MyColorMatrix extends View {

    private Context context;
    private Paint mPaint;// 画笔
    private Bitmap bitmap;// 位图
    private float  mBitmapR;
    private float  mBitmapG;
    private float  mBitmapB;

    public MyColorMatrix(Context context) {
        this(context, null);
    }

    public MyColorMatrix(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制位图

        // 生成色彩矩阵
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                // 灰色
//                0.33F, 0.59F, 0.11F, 0, 0,
//                0.33F, 0.59F, 0.11F, 0, 0,
//                0.33F, 0.59F, 0.11F, 0, 0,
//                0, 0, 0, 1, 0,

                // 绿色
//                1, 0, 0, 0, 0,
//                0, 2F, 0, 0, 0,
//                0, 0, 1, 0, 0,
//                0, 0, 0, 1, 0,

//
//                // 绿色
//                2F, 0, 0, 0, 0,
//                0, 2F, 0, 0, 0,
//                0, 0, 1, 0, 0,
//                0, 0, 0, 1, 0,

                // 绿色
                1F, 0, 0, 0, mBitmapR, //红
                0, 1F, 0, 0, mBitmapG, //绿色
                0, 0, 1F, 0, mBitmapB,  // 蓝色
                0, 0, 0, 1, 0,
        });
        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        // 获取位图
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_back_min);
        canvas.drawBitmap(bitmap, 0, 0, mPaint);
    }

    public float getBitmapR() {
        return mBitmapR;
    }

    public void setBitmapR(float bitmapR) {
        mBitmapR = bitmapR;
    }

    public float getBitmapG() {
        return mBitmapG;
    }

    public void setBitmapG(float bitmapG) {
        mBitmapG = bitmapG;
    }

    public float getBitmapB() {
        return mBitmapB;
    }

    public void setBitmapB(float bitmapB) {
        mBitmapB = bitmapB;
    }
}