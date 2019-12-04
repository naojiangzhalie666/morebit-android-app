package com.zjzy.morebit.view.guideview.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.cpiz.android.bubbleview.BubbleStyle;
import com.cpiz.android.bubbleview.BubbleTextView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.view.guideview.Component;


public class GuideCircle implements Component {
    private Component.onCloseListener mOnCloseListener;
    @Override public View getView(LayoutInflater inflater) {

        FrameLayout ll = (FrameLayout) inflater.inflate(R.layout.view_guide_common_other, null);
        BubbleTextView btv = ll.findViewById(R.id.bubbleTv);
        btv.setText(ll.getContext().getResources().getString(R.string.guide_circle));
        btv.setFillColor(ll.getContext().getResources().getColor(R.color.color_ff000000));
        btv.setArrowDirection(BubbleStyle.ArrowDirection.Down);
        btv.setArrowPosPolicy(BubbleStyle.ArrowPosPolicy.SelfCenter);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(null != mOnCloseListener){
                    mOnCloseListener.onDismiss();
                }
            }
        });
        return ll;
    }

    @Override public int getAnchor() {
        return Component.ANCHOR_TOP;
    }

    @Override public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override public int getXOffset() {
        return 0;
    }

    @Override public int getYOffset() {
        return -5;
    }

    @Override
    public void closeGuide() {

    }

    public void setmOnCloseListener(onCloseListener mOnCloseListener) {
        this.mOnCloseListener = mOnCloseListener;
    }
}
