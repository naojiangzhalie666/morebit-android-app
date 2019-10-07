package com.jf.my.goods.shopping.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.my.R;

/**
 * Created by fengrs on 2019/5/18.
 * 备注: 推荐指数
 */

public class RecommendIndexView extends RelativeLayout {

    private TextView mTv_index;

    public RecommendIndexView(Context context) {
        this(context, null);
    }

    public RecommendIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_recommend_index, null);
        mTv_index = inflate.findViewById(R.id.tv_index);
        this.addView(inflate);

    }
    public void setText(String srt){
        if (srt == null)return;
        mTv_index.setText(srt);

    }
    public void switchGridlayout(){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        setLayoutParams(layoutParams);
        mTv_index.setBackgroundResource(R.drawable.bg_shopping_recommend_index_2);

    }
}
