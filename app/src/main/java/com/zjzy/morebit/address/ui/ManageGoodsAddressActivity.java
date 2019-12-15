package com.zjzy.morebit.address.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.address.AddressInfoList;
import com.zjzy.morebit.address.contract.ManageAddressContract;
import com.zjzy.morebit.address.presenter.ManageAddressPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.C;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 管理收货地址
 * Created by haiping.liu on 2019-12-05.
 */
public class ManageGoodsAddressActivity extends MvpActivity<ManageAddressPresenter> implements View.OnClickListener,ManageAddressContract.View {
    private static final String TAG = ManageGoodsAddressActivity.class.getSimpleName();



    @BindView(R.id.txt_head_title)
    TextView headTitle;

    @BindView(R.id.mListView)
    ReUseListView mReUseListView;

    private ManageAddressAdapter manageAdressAdapter;

    View footerView;

    RelativeLayout rlManageAddAddress;
    /**
     * 管理收货地址的页面
     * @param activity
     */
    public static void start(Activity activity) {
        //跳转到管理收货地址页面
        Intent it = new Intent(activity, ManageGoodsAddressActivity.class);

        activity.startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void onDeleteError() {

    }

    @Override
    public void onDeleteSuccessful(Boolean isSuccess) {

    }

    @Override
    public void onAddressListError() {
        mReUseListView.setVisibility(View.GONE);
    }

    @Override
    public void onAddressListSuccessful(AddressInfoList datas) {
        List<AddressInfo> list = datas.getList();
        if (list.size() == 0) {
            return;
        }

        manageAdressAdapter.clear();
        manageAdressAdapter.add(list);

        manageAdressAdapter.notifyDataSetChanged();

        mReUseListView.getSwipeList().setRefreshing(false);
        mReUseListView.getListView().setNoMore(true);


    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_manage_goods_address;
    }

    private void initView(){
        headTitle.setText("管理收货地址");
         manageAdressAdapter = new ManageAddressAdapter(ManageGoodsAddressActivity.this);
        mReUseListView.setLayoutManager(new LinearLayoutManager(ManageGoodsAddressActivity.this));
        mReUseListView.getListView().addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(ManageGoodsAddressActivity.this));
        mReUseListView.getListView().addItemDecoration(new DividerItemDecoration(ManageGoodsAddressActivity.this,LinearLayoutManager.VERTICAL));
        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });
        footerView = LayoutInflater.from(this).inflate(R.layout.footer_manage_goods_address, null);

        mReUseListView.setAdapterAndFootView(footerView,manageAdressAdapter);
        mReUseListView.getListView().setNoMore(true);
        rlManageAddAddress = (RelativeLayout)footerView.findViewById(R.id.rl_manage_add_address);
        rlManageAddAddress.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AddModifyAddressActivity.start(ManageGoodsAddressActivity.this,null, C.Address.ADD_TYPE);
            }
        });

    }
    private void refreshData(){
        mReUseListView.getSwipeList().setRefreshing(true);
        initData();
    }
    private void initData(){
        mPresenter.getAddressList(ManageGoodsAddressActivity.this);
    }
    @OnClick({R.id.btn_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 会员商品适配器
     */
    class ManageAddressAdapter extends SimpleAdapter<AddressInfo, SimpleViewHolder> {
        Context mContext;

        public ManageAddressAdapter(Context context) {
            super(context);
            mContext = context;

        }
        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
            if (holder instanceof ItemAddressInfoHolder){
                bindItemAddressInfoHolder((ItemAddressInfoHolder) holder,position);
            }

        }

        private void bindItemAddressInfoHolder(ItemAddressInfoHolder holder, int position) {
            final AddressInfo item =  getItem(position);
            String name = item.getName();
            if (!TextUtils.isEmpty(name)){
                holder.txtUserName.setText(name);
            }else{
                holder.txtUserName.setText("");
            }

            String phone = item.getTel();
            if (!TextUtils.isEmpty(name)){
                holder.txtAddressPhone.setText(phone);
            }else{
                holder.txtAddressPhone.setText("");
            }

            String provice = item.getProvince();
            String city = item.getCity();
            String distinct = item.getDistrict();
            String detail = item.getDetailAddress();

            String addressStr = provice + city + distinct + detail;
            if (!TextUtils.isEmpty(addressStr)){
                holder.txtAddressDetail.setText(addressStr);
            }else{
                holder.txtAddressDetail.setText("");
            }
            if (item.isDefault()){
                holder.addressdefault.setVisibility(View.VISIBLE);
            }else{
                holder.addressdefault.setVisibility(View.GONE);
            }

            //编辑地址
            holder.llModifyAddress.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    AddModifyAddressActivity.start(ManageGoodsAddressActivity.this,
                            item,C.Address.UPDATE_TYPE);
                }
            });
            //删除地址
            holder.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.deleteAddress(ManageGoodsAddressActivity.this,
                            item.getId().toString());
                }
            });

        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemAddressInfoHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_goods_address, parent, false));
        }

        public class ItemAddressInfoHolder extends SimpleViewHolder {
            @BindView(R.id.txt_user_name)
            TextView txtUserName;

            @BindView(R.id.address_default)
            TextView addressdefault;

            @BindView(R.id.txt_goods_address)
            TextView txtAddressDetail;

            @BindView(R.id.txt_address_phone)
            TextView txtAddressPhone;

            @BindView(R.id.modify_address)
            LinearLayout llModifyAddress;

            @BindView(R.id.delete)
            TextView txtDelete;

            public ItemAddressInfoHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
