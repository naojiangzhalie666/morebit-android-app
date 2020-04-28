package com.zjzy.morebit.Module.common.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zjzy.morebit.R;

public class ProgressDialog extends Dialog {

    private AnimationDrawable animationDrawable;

    public ProgressDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Fullscreen);
        setOwnerActivity((Activity) context);
        setContentView(R.layout.progress_dialog);
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
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);

    }
}
