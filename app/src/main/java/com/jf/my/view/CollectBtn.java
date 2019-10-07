package com.jf.my.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.my.R;

/**
 - @Description: 收藏按钮
 - @Author:  wuchaowen
 - @Time:  2019/8/29 17:17
 **/
public class CollectBtn extends LinearLayout implements View.OnClickListener{
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private TextView collectTv;
    private TextView collectedTv;
    private boolean isCollect = false; //默认没有收藏


    public CollectBtn(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    public CollectBtn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(context);
    }

    public CollectBtn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(context);
    }


    public void initView(Context context){
        mLayoutInflater =  LayoutInflater.from(context);
        mLayoutInflater.inflate(R.layout.btn_view_collect, this, true);
        collectTv = findViewById(R.id.collectTv);
        collectedTv = findViewById(R.id.collectedTv);
    }


    @Override
    public void onClick(View v) {

    }

    public void setCollected(boolean isCollect){
       if(isCollect){
           collectedTv.setVisibility(View.VISIBLE);
           collectTv.setVisibility(View.GONE);
       }else{
           collectedTv.setVisibility(View.GONE);
           collectTv.setVisibility(View.VISIBLE);
       }
    }

}
