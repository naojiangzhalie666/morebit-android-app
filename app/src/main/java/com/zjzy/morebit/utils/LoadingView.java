package com.zjzy.morebit.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class LoadingView extends View {
    private RectF rectF;

    private Paint paint;

    private int radius = 90; //半径

    private int count = 0;

    private boolean run = false; //动画控制

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(radius * 2, radius * 2);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        rectF = new RectF(radius - 5, 30, radius + 5, 60);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(count * 36, radius, radius);
        for (int i = 0; i < 10; i++) {
            paint.setAlpha(255 - i * 20);
            canvas.drawRoundRect(rectF, 10, 10, paint);
            canvas.rotate(36, radius, radius);
        }
        count++;
        if (count > 9) {
            count = 0;
        }
        if (run) {
            postInvalidateDelayed(100);
        }
    }

    public void start() {
        if (!run) {
            postInvalidateDelayed(100);
            run = true;
        }
    }
    public void stop() {
        run = false;
    }
}
