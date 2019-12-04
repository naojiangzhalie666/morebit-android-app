package com.zjzy.morebit.utils;

import android.support.v4.view.ViewPager;

import static com.zjzy.morebit.utils.SwipeDirectionDetector.Direction.LEFT;
import static com.zjzy.morebit.utils.SwipeDirectionDetector.Direction.NO_DIRECTION;
import static com.zjzy.morebit.utils.SwipeDirectionDetector.Direction.RIGHT;

/**
 * @Author: wuchaowen
 * @Description: Viewpager滑动事件
 **/
public class SwipeDirectionDetector implements ViewPager.OnPageChangeListener {
    private Direction direction;
    private int lastState;

    public enum Direction {
        LEFT,
        RIGHT,
        NO_DIRECTION
    }

    @Override
    public void onPageScrolled(int position, float offset, int positionOffsetPixels) {
        boolean leftStarted = direction == LEFT && offset > 0.5;
        boolean rightStarted = direction == RIGHT && offset < 0.5;
        if (lastState == ViewPager.SCROLL_STATE_DRAGGING && (leftStarted || rightStarted)) {
            direction = NO_DIRECTION;
        }
        initDirection(offset);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        lastState = state;
    }

    private void initDirection(float offset) {
        if (direction == NO_DIRECTION && offset > 0) {
            if (offset > 0.5) {
                direction = LEFT;
            } else {
                direction = RIGHT;
            }
        }
        if (offset == 0) {
            direction = NO_DIRECTION;
        }
    }

    public Direction getDirection() {
        return direction;
    }
}
