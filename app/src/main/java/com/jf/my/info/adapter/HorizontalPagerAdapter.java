package com.jf.my.info.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jf.my.Module.common.Activity.ImagePagerActivity;
import com.jf.my.R;
import com.jf.my.utils.LoadImgUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by YangBoTian on 2018/12/26.
 */
public class HorizontalPagerAdapter extends PagerAdapter {


    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<String> mList;

    public HorizontalPagerAdapter(final Context context, List<String> list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;

        view = mLayoutInflater.inflate(R.layout.item_poster, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ImagePagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) mList);
                bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                bundle.putString(ImagePagerActivity.TAOBAO_ID, "get_gther_poster");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        if (mList != null) {
            LoadImgUtils.setImgPath(mContext, img, mList.get(position));
        }
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}
