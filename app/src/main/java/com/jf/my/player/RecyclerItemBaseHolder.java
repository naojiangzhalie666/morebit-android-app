package com.jf.my.player;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jf.my.adapter.holder.SimpleViewHolder;


/**
 * Created by shuyu on 2016/12/3.
 */

public class RecyclerItemBaseHolder extends SimpleViewHolder {

    RecyclerView.Adapter recyclerBaseAdapter;

    public RecyclerItemBaseHolder(View itemView) {
        super(itemView);
    }

    public RecyclerView.Adapter getRecyclerBaseAdapter() {
        return recyclerBaseAdapter;
    }

    public void setRecyclerBaseAdapter(RecyclerView.Adapter recyclerBaseAdapter) {
        this.recyclerBaseAdapter = recyclerBaseAdapter;
    }
}
