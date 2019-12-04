package com.zjzy.morebit.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.interfaces.EmptyView;


import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by YangBoTian on 2018/9/25.
 */

public class SimpleEmptyView extends FrameLayout implements EmptyView {

    private ImageView mIcon;
    private TextView mTitle;
   private  String mMessage;
    private State mState;

    enum State {
        ERROR, NORMAL
    }

    public SimpleEmptyView(Context context) {
        this(context, null);
    }

    public SimpleEmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.simple_empty_view,null);
        mIcon = (ImageView) view.findViewById(R.id.icon);
        mTitle = (TextView) view.findViewById(R.id.title);
        LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(view, params);
        mState = State.NORMAL;
    }

    @Override
    public void onError(Throwable tr) {
       // setIcon();
        setMessage("请检查您的网络状态是否正常，请稍后再试哦~");
        mState = State.ERROR;
    }

    @Override
    public void setVisibility(int visibility) {
        if (mState == State.NORMAL) {
            setMessage(mMessage);
        }
        if (mState == State.ERROR && visibility == VISIBLE) {
            mState = State.NORMAL;
        }
        super.setVisibility(visibility);
    }


    @Override
    public void setMessage(String message) {
        mMessage = message;
        if(mTitle!=null){
            mTitle.setText(message);
        }
    }

    @Override
    public void setIcon(@DrawableRes int drawableId) {
        if(mIcon!=null){
            mIcon.setImageResource(drawableId);
        }
    }

}
