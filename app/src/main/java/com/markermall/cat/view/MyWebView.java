package com.markermall.cat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyWebView extends android.webkit.WebView {
 public MyWebView(Context context) {
     super(context);
 }

 public MyWebView(Context context, AttributeSet attrs) {
     super(context, attrs);
 }

 public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
     super(context, attrs, defStyleAttr);
 }
// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//     invalidate();
//     super.onMeasure(widthMeasureSpec, heightMeasureSpec);
// }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.d("touchevent", "touchevent"+super.onTouchEvent(ev));
//            return super.onTouchEvent(ev);
        return false;
    }
}
