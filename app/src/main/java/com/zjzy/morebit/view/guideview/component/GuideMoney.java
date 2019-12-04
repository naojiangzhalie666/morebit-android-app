package com.zjzy.morebit.view.guideview.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.cpiz.android.bubbleview.BubbleStyle;
import com.cpiz.android.bubbleview.BubbleTextView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.view.guideview.Component;


public class GuideMoney implements Component {
    private onCloseListener mOnCloseListener;
    @Override public View getView(LayoutInflater inflater) {

        FrameLayout ll = (FrameLayout) inflater.inflate(R.layout.view_guide_common, null);
        BubbleTextView btv = ll.findViewById(R.id.bubbleTv);
        btv.setText(ll.getContext().getResources().getString(R.string.guide_mine_money));
        btv.setFillColor(ll.getContext().getResources().getColor(R.color.color_ff000000));
        btv.setArrowDirection(BubbleStyle.ArrowDirection.Up);
        btv.setArrowPosPolicy(BubbleStyle.ArrowPosPolicy.SelfBegin);
        btv.setArrowPosDelta(80.0f);
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
        return Component.ANCHOR_BOTTOM;
    }

    @Override public int getFitPosition() {
        return Component.FIT_START;
    }

    @Override public int getXOffset() {
        return 0;
    }

    @Override public int getYOffset() {
        return 5;
    }

    @Override
    public void closeGuide() {

    }


    public void setmOnCloseListener(onCloseListener mOnCloseListener) {
        this.mOnCloseListener = mOnCloseListener;
    }
}
