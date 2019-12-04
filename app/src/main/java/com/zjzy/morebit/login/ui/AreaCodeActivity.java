package com.zjzy.morebit.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.App;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.AreaCodeAdapter;
import com.zjzy.morebit.login.contract.AreaCodeContract;
import com.zjzy.morebit.login.presenter.AreaCodePresenter;
import com.zjzy.morebit.login.ui.areacode.AreaCodeIndexDecoration;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.zjzy.morebit.pojo.AreaCodeItem;
import com.zjzy.morebit.utils.AreaCodeUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.view.IndexView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class AreaCodeActivity extends MvpActivity<AreaCodePresenter> implements AreaCodeContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.index_view)
    IndexView indexView;
    public  static final int REQ_AREACODE = 1000;
    private AreaCodeAdapter mAreaCodeAdapter;
    private List<AreaCodeBean> mSelectCodeList = new ArrayList<>();
    private List<AreaCodeBean> mAllCodeList = new ArrayList<>();
    private List<AreaCodeItem> mCodeList;
    public static void actionStart(Activity context){
        Intent intent = new Intent(context,AreaCodeActivity.class);
        context.startActivityForResult(intent, REQ_AREACODE);
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_area_code;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarColor(R.color.color_F5F5F5)     //状态栏颜色，不写默认透明色
                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
                //  .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                //                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //软键盘自动弹出
                .init();
        initView();
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManagerWithSmoothScroller(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        AreaCodeIndexDecoration groupDecoration = new AreaCodeIndexDecoration(DensityUtil.dip2px(this,37), getResources().getColor(R.color.color_F5F5F5), DensityUtil.sp2px(this,18), getResources().getColor(R.color.color_333333));
        //实现index view显示
        recyclerView.addItemDecoration(groupDecoration);
        //实现分割线显示
        //recyclerView.addItemDecoration(new AreaCodeDividerDecoration(new ColorDrawable(ContextCompat.getColor(this, R.color.color_EEEEEE)), DensityUtil.dip2px(this,12), DensityUtil.dip2px(this,12)));


        mAreaCodeAdapter = new AreaCodeAdapter(this);
        mAreaCodeAdapter.setOnAdapterClickListener(new AreaCodeAdapter.OnAdapterClickListener() {
            @Override
            public void onItem(int position) {
                Intent data = new Intent();
                data.putExtra(C.Extras.COUNTRY, mAreaCodeAdapter.getDatas().get(position).data);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        recyclerView.setAdapter(mAreaCodeAdapter);


        if (!TextUtils.isEmpty(App.getACache().getAsString( C.sp.CACHE_AREA_CODE))) {
            String cacheData = App.getACache().getAsString( C.sp.CACHE_AREA_CODE);
            Gson gson = new Gson();
            List<AreaCodeBean> codeList = gson.fromJson(cacheData,new TypeToken<List<AreaCodeBean>>() {
            }.getType());
            initAreaCode(codeList);
        }

        indexView.setOnSelectedListener(new IndexView.OnSelectedListener() {
            @Override
            public void onSelected(String index) {
                selectedIndex(index);
            }
        });

        final EditText etSearch = (EditText) findViewById(R.id.edt_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                mSelectCodeList.clear();
                for (AreaCodeBean country : mAllCodeList) {
                    if (country.getCountry().contains(string.toLowerCase())){
                        mSelectCodeList.add(country);
                    }

                }
                mAreaCodeAdapter.updateData(AreaCodeUtil.getAreaCodeItemList(mSelectCodeList));
            }
        });

        mPresenter.getCountryList(this);
    }

    @Override
    public void showFinally() {

    }


    @OnClick({R.id.btn_back})
    public void onclick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void updateAreaCode(List<AreaCodeBean> data) {
           Gson gson = new Gson();
           String datajson = gson.toJson(data);
           App.getACache().put(C.sp.CACHE_AREA_CODE,datajson);
          initAreaCode(data);
    }

    private void initAreaCode(List<AreaCodeBean> data) {
        if(null == data){
            return;
        }
        if(data.size()> 0){
            mAllCodeList.clear();
            mAllCodeList.addAll(data);
            indexView.setIndexList(AreaCodeUtil.getIndexList(data));
            mCodeList = AreaCodeUtil.getAreaCodeItemList(data);
            mSelectCodeList.clear();
            mSelectCodeList.addAll(mAllCodeList);
            mAreaCodeAdapter.updateData(mCodeList);
        }

    }

    private void selectedIndex(String index) {
        int position = -1;
        for (int i = 0; i < mCodeList.size(); i++) {
            if (mCodeList.get(i).getGroupName().equals(index)) {
                position = i;
                break;
            }
        }

        if (-1 == position) {
            return;
        }

        recyclerView.smoothScrollToPosition(position);
    }
}
