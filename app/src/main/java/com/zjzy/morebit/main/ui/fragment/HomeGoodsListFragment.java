package com.zjzy.morebit.main.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.zjzy.morebit.R;
import com.zjzy.morebit.main.contract.HomeGoodsListContract;
import com.zjzy.morebit.main.presenter.HomeGoodsListPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.goods.Child2;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengrs on 2018/12/14.
 * 备注:
 */

public class HomeGoodsListFragment extends MvpFragment<HomeGoodsListPresenter> implements HomeGoodsListContract.View  {

    public static HomeGoodsListFragment newInstance(String typeName, String title,
                                                   String cid, List<Child2> shopGoodInfos,
                                                   String keywords) {

        HomeGoodsListFragment shoppingAoLeFragment = new HomeGoodsListFragment();
        //MyLog.i("test","newInstance: " +shoppingAoLeFragment);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isHome", true);
        bundle.putString("TypeName", typeName);
        bundle.putString("title", title);
        bundle.putString("cid", cid);
        bundle.putString("keywords", keywords);
        bundle.putSerializable("FlData", (Serializable) shopGoodInfos);
        shoppingAoLeFragment.setArguments(bundle);
        return shoppingAoLeFragment;
    }


    @Override
    public void showFinally() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {

    }

    /**
     * 获取资源ID
     *
     * @return
     */
    @Override
    protected int getViewLayout() {
        return R.layout.fragment_home_goods_list;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }
}
