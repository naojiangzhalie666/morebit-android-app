/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.markermall.cat.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.markermall.cat.R;


/**
 * Extension of FrameLayout that assumes a measured (non-zero) width and sets the
 * height according to the provided aspect ratio.
 */
public class AspectRatioView extends FrameLayout {

    private final boolean mReverse;
    private float mAspectRatio = 0f;

    public AspectRatioView(Context context) {
        this(context, null, 0);
    }

    public AspectRatioView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.AspectRatioView, defStyle, 0);
        mAspectRatio = a.getFloat(R.styleable.AspectRatioView_aspectRatio, 0);
        mReverse = a.getBoolean(R.styleable.AspectRatioView_reverse, false);

        if (mAspectRatio == 0f) {
            throw new IllegalArgumentException(
                    "You must specify an aspect ratio when using AspectRatioView.");
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAspectRatio != 0) {
            if (!mReverse) {
                measureHeight(widthMeasureSpec);
            } else {
                measureWidth(heightMeasureSpec);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @SuppressLint("WrongCall")
    private void measureWidth(int heightMeasureSpec) {
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int width = (int) (height * mAspectRatio);
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @SuppressLint("WrongCall")
    private void measureHeight(int widthMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = (int) (width / mAspectRatio);
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    public float getAspectRatio() {
        return mAspectRatio;
    }

    public void setAspectRatio(float mAspectRatio) {
        this.mAspectRatio = mAspectRatio;
    }
}
