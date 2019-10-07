package com.jf.my.view.guideview.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.cpiz.android.bubbleview.BubbleStyle;
import com.cpiz.android.bubbleview.BubbleTextView;
import com.jf.my.R;
import com.jf.my.view.guideview.Component;


public class GuideService implements Component {
    private onCloseListener mOnCloseListener;
    private int arrowDelta = 0;
    private boolean isLeft = false;
    private int position = 0;

    public GuideService(int arrowDelta, boolean isLeft, int position) {
        this.arrowDelta = arrowDelta;
        this.isLeft = isLeft;
        this.position = position;
    }

    @Override public View getView(LayoutInflater inflater) {

        FrameLayout ll = (FrameLayout) inflater.inflate(R.layout.view_guide_common_other, null);
        BubbleTextView btv = ll.findViewById(R.id.bubbleTv);
        btv.setText(ll.getContext().getResources().getString(R.string.guide_mine_service));
        btv.setFillColor(ll.getContext().getResources().getColor(R.color.color_ff000000));
        if(isLeft){
            btv.setArrowDirection(BubbleStyle.ArrowDirection.Down);
            btv.setArrowPosPolicy(BubbleStyle.ArrowPosPolicy.SelfBegin);
            if(position %2 == 0){
                if(this.arrowDelta !=0){
                    btv.setArrowPosDelta(this.arrowDelta+50f);
                }
            }else{
                if(this.arrowDelta !=0){
                    btv.setArrowPosDelta(this.arrowDelta-10f);
                }
            }

        }else{
            btv.setArrowDirection(BubbleStyle.ArrowDirection.Down);
            btv.setArrowPosPolicy(BubbleStyle.ArrowPosPolicy.SelfEnd);
            if(this.arrowDelta !=0){
                if(position ==4 || position ==8 || position  == 12 || position == 16){
                    btv.setArrowPosDelta(this.arrowDelta-60f);
                }else{
                    btv.setArrowPosDelta(this.arrowDelta+55f);
                }

            }
        }

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
        if(isLeft){
            return Component.FIT_START;
        }
        return Component.FIT_END;
    }

    @Override public int getXOffset() {
        if(isLeft){
            if(position %2 == 0){
                return -20;
            }
            return 0;
        }
        if(position ==4 || position ==8 || position  == 12 || position == 16){
            return -20;
        }
        return 20;
    }

    @Override public int getYOffset() {
        return -10;
    }

    @Override
    public void closeGuide() {

    }


    public void setmOnCloseListener(onCloseListener mOnCloseListener) {
        this.mOnCloseListener = mOnCloseListener;
    }
}
