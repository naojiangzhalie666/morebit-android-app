package com.markermall.cat.adapter.holder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markermall.cat.utils.ViewFinder;

import butterknife.ButterKnife;

/**
 * Created by YangBoTian on 2018/7/6.
 */


public   class SimpleViewHolder extends RecyclerView.ViewHolder {

    private ViewFinder mViewFinder;

    public SimpleViewHolder(LayoutInflater inflater, @LayoutRes int resId, ViewGroup parent) {
        this(inflater.inflate(resId, parent, false), false);
    }

    public SimpleViewHolder(Context context, @LayoutRes int resId, ViewGroup parent) {
        this(LayoutInflater.from(context), resId, parent);
    }

    public SimpleViewHolder(View itemView, @IdRes int... ids) {
        this(itemView, false, ids);
    }

    public SimpleViewHolder(View itemView, boolean lazy, @IdRes int... ids) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mViewFinder = createViewFinder(itemView, lazy, ids);
    }

    @NonNull
    protected ViewFinder createViewFinder(View itemView, boolean lazy, @IdRes int[] ids) {
        return new ViewFinder(itemView, lazy, ids);
    }

    public ViewFinder viewFinder() {
        return mViewFinder;
    }
}
