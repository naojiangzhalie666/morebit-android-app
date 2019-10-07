package com.markermall.cat.main.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markermall.cat.Module.common.Fragment.BaseFragment;
import com.markermall.cat.R;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.OpenFragmentUtils;
import com.markermall.cat.utils.UI.ActivityUtils;
import com.markermall.cat.view.ToolbarHelper;

/**
 * Created by fengrs on 2018/9/11.
 * 备注: 商品列表
 */


public class GoodNewsFramgent extends BaseFragment {
    private ShoppingListFragment2 mCircleDayHotFragment;


    public static void start(Activity activity, ImageInfo imageInfo, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(ShoppingListFragment2.Type, type);
        bundle.putSerializable(ShoppingListFragment2.IMAGE_INFO, imageInfo);
        OpenFragmentUtils.goToComplaintOrderFragment(activity, GoodNewsFramgent.class.getName(), bundle);
    }

    public static void start(Activity activity, ImageInfo imageInfo) {
        start(activity, imageInfo, C.GoodsListType.TypeActivity);
    }


    public static void startGoodsByBrand(Activity activity, ImageInfo imageInfo) {
        start(activity, imageInfo, ShoppingListFragment2.TYPEGOODSBYBRAND);
    }

    public static void startTiemSale(Activity activity, ImageInfo imageInfo) {
        start(activity, imageInfo, C.GoodsListType.DAYRECOMMEND);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_goods_news, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    /**
     * 初始化界面
     */
    private void initView() {
        ImageInfo imageInfo = (ImageInfo) getArguments().getSerializable(ShoppingListFragment2.IMAGE_INFO);
        if (getArguments() == null || imageInfo == null ) {
            return;
        }
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(imageInfo.getTitle());
            showComFragment();

    }


    private void showComFragment() {
        // Create the fragment
        if (mCircleDayHotFragment == null) {
            mCircleDayHotFragment = ShoppingListFragment2.newInstance(getArguments());
            ActivityUtils.replaceFragmentToActivity(
                    getActivity().getSupportFragmentManager(), mCircleDayHotFragment, R.id.fl_view);
        }
    }


}
