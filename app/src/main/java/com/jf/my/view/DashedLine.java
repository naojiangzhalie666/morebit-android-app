package com.jf.my.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.jf.my.R;

/**
 * 画虚线
 */
class DashedLine extends View {

    public DashedLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.top_head));
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, 900);
        PathEffect effects = new DashPathEffect(new float[]{4, 4, 4, 4}, 2);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}