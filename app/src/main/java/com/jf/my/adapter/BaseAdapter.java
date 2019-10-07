package com.jf.my.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;


import com.jf.my.adapter.holder.SimpleViewHolder;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.ViewFinder;

import java.util.List;

/**
 * Created by YangBoTian on 2018/7/6.
 */

abstract class BaseAdapter<VH extends SimpleViewHolder> extends RecyclerView.Adapter<VH> {
    private ViewFinder mCurrentViewFinder;
    private LayoutInflater mLayoutInflater;

    public BaseAdapter(Context context) {
        this(LayoutInflater.from(context));
    }

    public BaseAdapter(LayoutInflater layoutInflater) {
        mLayoutInflater = layoutInflater;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = onCreateView(mLayoutInflater, parent, viewType);
        return (VH) new SimpleViewHolder(view);
    }

    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return null;
    }

    @CallSuper
    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        update(holder);
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    protected void update(SimpleViewHolder holder) {
        mCurrentViewFinder = holder.viewFinder();
    }


    ////////////////////////////////////////////////////
    //         Delegate Method For ViewFinder         //
    ////////////////////////////////////////////////////


    public ViewFinder setText(@IdRes int viewId, CharSequence value) {
        return mCurrentViewFinder.setText(viewId, value);
    }

    public ViewFinder setText(@IdRes int viewId, @StringRes int strId) {
        return mCurrentViewFinder.setText(viewId, strId);
    }

    public ViewFinder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        return mCurrentViewFinder.setImageResource(viewId, imageResId);
    }

    public ViewFinder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        return mCurrentViewFinder.setBackgroundColor(viewId, color);
    }

    public ViewFinder setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        return mCurrentViewFinder.setBackgroundRes(viewId, backgroundRes);
    }

    public ViewFinder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        return mCurrentViewFinder.setTextColor(viewId, textColor);
    }

    public ViewFinder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        return mCurrentViewFinder.setImageDrawable(viewId, drawable);
    }

    public ViewFinder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        return mCurrentViewFinder.setImageBitmap(viewId, bitmap);
    }

    public ViewFinder setAlpha(@IdRes int viewId, float value) {
        return mCurrentViewFinder.setAlpha(viewId, value);
    }

    public ViewFinder setVisible(@IdRes int viewId, boolean visible) {
        return mCurrentViewFinder.setVisible(viewId, visible);
    }

    public ViewFinder linkify(@IdRes int viewId) {
        return mCurrentViewFinder.linkify(viewId);
    }

    public ViewFinder setTypeface(@IdRes int viewId, Typeface typeface) {
        return mCurrentViewFinder.setTypeface(viewId, typeface);
    }

    public ViewFinder setTypeface(Typeface typeface, int... viewIds) {
        return mCurrentViewFinder.setTypeface(typeface, viewIds);
    }

    public ViewFinder setProgress(@IdRes int viewId, int progress) {
        return mCurrentViewFinder.setProgress(viewId, progress);
    }

    public ViewFinder setProgress(@IdRes int viewId, int progress, int max) {
        return mCurrentViewFinder.setProgress(viewId, progress, max);
    }

    public ViewFinder setMax(@IdRes int viewId, int max) {
        return mCurrentViewFinder.setMax(viewId, max);
    }

    public ViewFinder setRating(@IdRes int viewId, float rating) {
        return mCurrentViewFinder.setRating(viewId, rating);
    }

    public ViewFinder setRating(@IdRes int viewId, float rating, int max) {
        return mCurrentViewFinder.setRating(viewId, rating, max);
    }

    public ViewFinder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        return mCurrentViewFinder.setOnClickListener(viewId, listener);
    }

    public ViewFinder setOnTouchListener(@IdRes int viewId, View.OnTouchListener listener) {
        return mCurrentViewFinder.setOnTouchListener(viewId, listener);
    }

    public ViewFinder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        return mCurrentViewFinder.setOnLongClickListener(viewId, listener);
    }

    public ViewFinder setOnItemClickListener(@IdRes int viewId, AdapterView.OnItemClickListener listener) {
        return mCurrentViewFinder.setOnItemClickListener(viewId, listener);
    }

    public ViewFinder setOnItemLongClickListener(@IdRes int viewId, AdapterView.OnItemLongClickListener listener) {
        return mCurrentViewFinder.setOnItemLongClickListener(viewId, listener);
    }

    public ViewFinder setOnItemSelectedClickListener(@IdRes int viewId, AdapterView.OnItemSelectedListener listener) {
        return mCurrentViewFinder.setOnItemSelectedClickListener(viewId, listener);
    }

    public ViewFinder setOnCheckedChangeListener(@IdRes int viewId, CompoundButton.OnCheckedChangeListener listener) {
        return mCurrentViewFinder.setOnCheckedChangeListener(viewId, listener);
    }

    public ViewFinder setTag(@IdRes int viewId, Object tag) {
        return mCurrentViewFinder.setTag(viewId, tag);
    }

    public ViewFinder setTag(@IdRes int viewId, int key, Object tag) {
        return mCurrentViewFinder.setTag(viewId, key, tag);
    }

    public ViewFinder setChecked(@IdRes int viewId, boolean checked) {
        return mCurrentViewFinder.setChecked(viewId, checked);
    }

    public ViewFinder setAdapter(@IdRes int viewId, Adapter adapter) {
        return mCurrentViewFinder.setAdapter(viewId, adapter);
    }

    public <T extends View> T view(@IdRes int viewId) {
        return mCurrentViewFinder.view(viewId);
    }

    public <T extends View> T view0(int index) {
        return mCurrentViewFinder.view0(index);
    }
}

