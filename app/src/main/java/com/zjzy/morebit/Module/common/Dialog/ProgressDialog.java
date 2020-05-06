package com.zjzy.morebit.Module.common.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.R;

public class ProgressDialog extends Dialog {

    private AnimationDrawable animationDrawable;
    private Context context;

    public ProgressDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Fullscreen);
         setOwnerActivity((Activity) context);
        setContentView(R.layout.progress_dialog);
        this.context=context;

    }

    public ProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

    }

    protected ProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);//设置全屏
        ImageView progress_image = findViewById(R.id.progress_image);
      //  ImageView tv_content = findViewById(R.id.tv_content);
       // progress_image.setImageDrawable(R.drawable.new_progress1);
        animationDrawable = (AnimationDrawable) progress_image.getDrawable(); //帧动画的初始化
        progress_image.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
     //   progress_image.setImageDrawable(animationDrawable); //将动画设置在ImageView上


    }





    /**
     * 开始帧动画
     */
    @Override
    protected void onStart() {
        animationDrawable.start();
        super.onStart();
    }

    /**
     * 停止帧动画
     */
    @Override
    protected void onStop() {
        animationDrawable.stop();
        super.onStop();
    }

    @Override
    public void show() {
        super.show();




    }
}
