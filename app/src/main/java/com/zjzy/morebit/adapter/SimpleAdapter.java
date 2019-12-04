package com.zjzy.morebit.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.util.DiffUtil;


import com.zjzy.morebit.adapter.holder.SimpleViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by YangBoTian on 2018/7/7.
 */

public abstract class SimpleAdapter<T, VH extends SimpleViewHolder> extends BaseAdapter<VH> {

    private Context mContext;
    private Resources mResources;
    private List<T> mItems;

    public SimpleAdapter(Context context) {
        this(context, null);
    }

    public SimpleAdapter(Context context, Collection<T> items) {
        super(context);
        mContext = context;
        mResources = context.getResources();
        if (items != null) {
            mItems = new ArrayList<>(items);
        } else {
            mItems = new ArrayList<>();
        }
    }

    public List<T> getItems() {
        return mItems;
    }

    public void update(@NonNull final List<T> newItems) {
        mItems.addAll(newItems);
    }

    public void replace(List<T> items) {
        mItems = new ArrayList<>(items);
    }

    public void add(List<T> items) {
        mItems.addAll(items);
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    public void clear() {
        mItems.clear();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Context getContext() {
        return mContext;
    }

    public Resources getResources() {
        return mResources;
    }

    public String getString(@StringRes int resId, Object... formatArgs) {
        return mResources.getString(resId, formatArgs);
    }

    public static class MyCallback<T> extends DiffUtil.Callback {
        protected List<T> mOldItems;
        protected final List<T> mNewItems;

        public MyCallback(SimpleAdapter<T, ? extends SimpleViewHolder> adapter, List<T> newItems) {
            mOldItems = adapter.mItems;
            mNewItems = newItems;
        }

        @Override
        public int getOldListSize() {
            return mOldItems.size();
        }

        @Override
        public int getNewListSize() {
            return mNewItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            T newItem = mNewItems.get(newItemPosition);
            T oldItem = mOldItems.get(oldItemPosition);
            return newItem.equals(oldItem);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            T newItem = mNewItems.get(newItemPosition);
            T oldItem = mOldItems.get(oldItemPosition);
            return newItem.equals(oldItem);
        }

        List<T> getNewItems() {
            return mNewItems;
        }
    }
}
