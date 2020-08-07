package com.zjzy.morebit.address.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.address.AddressInfo;
import com.zjzy.morebit.pojo.address.AddressInfoList;
import com.zjzy.morebit.address.contract.ManageAddressContract;
import com.zjzy.morebit.address.presenter.ManageAddressPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 管理收货地址
 * Created by haiping.liu on 2019-12-05.
 */
public class ManageGoodsAddressActivity extends MvpActivity<ManageAddressPresenter> implements View.OnClickListener,ManageAddressContract.View {
    private static final String TAG = ManageGoodsAddressActivity.class.getSimpleName();

    private static final int REQUEST_ADDRESS_CODE =100;
    public static final int REQUEST_ADD_ADDRESS_CODE =101;
    public static final int REQUEST_UPDATE_ADDRESS_CODE =102;
    @BindView(R.id.txt_head_title)
    TextView headTitle;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

//    @BindView(R.id.swi)
//    ReUseListView mReUseListView;

    private ManageAddressAdapter manageAdressAdapter;

    View footerView;

    RelativeLayout rlManageAddAddress;

    private int fromOrderActivity;

    List<AddressInfo> mListAddressInfo = new ArrayList<AddressInfo>();
    public interface OnAdapterClickListener {
        public void onItem(int position);
    }

    /**
     * 收货地址管理页面
     * @param context
     */
    public static void addressStart(Activity context){
        Intent intent = new Intent(context, ManageGoodsAddressActivity.class);
        intent.putExtra(C.Extras.SELECTED_ADDRESS_FROM_CONFIRM_ORDER,1);
        context.startActivityForResult(intent, REQUEST_ADDRESS_CODE);
    }

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
    public void onDeleteSuccessful(Boolean isSuccess,int position) {

        mListAddressInfo.remove(position);
        manageAdressAdapter.notifyItemRemoved(position);
        manageAdressAdapter.notifyItemRangeChanged(position,mListAddressInfo.size()-position);
    }

    @Override
    public void onAddressListError() {
        ViewShowUtils.showShortToast(ManageGoodsAddressActivity.this, "收货地址删除失败");
//        mReUseListView.setVisibility(View.GONE);
    }

    @Override
    public void onAddressListSuccessful(AddressInfoList datas) {
        List<AddressInfo> list = datas.getList();
        if (list.size() == 0) {
            return;
        }
        mListAddressInfo.clear();


        mListAddressInfo.addAll(list);
        manageAdressAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);


//        mReUseListView.getSwipeList().setRefreshing(false);
//        mReUseListView.getListView().setNoMore(true);


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        }
        fromOrderActivity = getIntent().getIntExtra(C.Extras.SELECTED_ADDRESS_FROM_CONFIRM_ORDER,0);

        headTitle.setText("管理收货地址");
         manageAdressAdapter = new ManageAddressAdapter(ManageGoodsAddressActivity.this,mListAddressInfo);


        recyclerView.setLayoutManager(new LinearLayoutManager(ManageGoodsAddressActivity.this));
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(ManageGoodsAddressActivity.this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(ManageGoodsAddressActivity.this,LinearLayoutManager.VERTICAL));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        recyclerView.setAdapter(manageAdressAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_ADDRESS_CODE || requestCode == REQUEST_UPDATE_ADDRESS_CODE) {
            refreshData();
        }
    }

    private void refreshData(){
        swipeRefreshLayout.setRefreshing(true);
//        mReUseListView.getSwipeList().setRefreshing(true);
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
    public class ManageAddressAdapter extends RecyclerView.Adapter  {
        Context mContext;
        List<AddressInfo> mDatas;
        public ManageAddressAdapter(Context context,List<AddressInfo> datas) {
            mDatas = datas;
            mContext = context;

        }
        public void setData(List<AddressInfo> list){
            mDatas = list;
            notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            if (getItemViewType(position) == R.layout.item_goods_address){

                bindItemAddressInfoHolder((ItemAddressInfoHolder) holder,position);
            } else{
                bindFooderHolder((FooderHolder) holder,position);
            }


        }

        @Override
        public int getItemCount() {
            return mDatas.size()+1;
        }

        @Override
        public int getItemViewType(int position) {
            return (position == mDatas.size()) ? R.layout.footer_manage_goods_address : R.layout.item_goods_address;
        }

        private void bindFooderHolder(FooderHolder holder,final int position){
                holder.manageAddressBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddModifyAddressActivity.start(ManageGoodsAddressActivity.this,null, C.Address.ADD_TYPE);
                    }
                });
        }

        private void bindItemAddressInfoHolder(ItemAddressInfoHolder holder, final int position) {
            final AddressInfo item =  mDatas.get(position);
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
            if (item.getIsDefault()==1){
                holder.addressdefault.setVisibility(View.VISIBLE);
            }else{
                holder.addressdefault.setVisibility(View.GONE);
            }
            if (fromOrderActivity == 1){
                holder.rlMainItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent data = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(C.Extras.GOODS_ADDRESS_INFO, item);
                        data.putExtras(bundle);
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    }
                });
            }

            holder.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.deleteAddress(ManageGoodsAddressActivity.this,
                            item.getId().toString(),position);

                }
            });

            holder.llModifyAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddModifyAddressActivity.start(ManageGoodsAddressActivity.this,
                            mDatas.get(position),C.Address.UPDATE_TYPE);
                }
            });


        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == R.layout.item_goods_address){
                View root = LayoutInflater.from(mContext).inflate(R.layout.item_goods_address, parent, false);
                return new ItemAddressInfoHolder(root);
            }else{
                View root = LayoutInflater.from(mContext).inflate(R.layout.footer_manage_goods_address, parent, false);
                return new FooderHolder(root);
            }

        }

        class FooderHolder extends RecyclerView.ViewHolder {

            RelativeLayout manageAddressBtn;

            public FooderHolder(View itemView) {
                super(itemView);
                manageAddressBtn = (RelativeLayout) itemView.findViewById(R.id.rl_manage_add_address);

            }
        }

         class ItemAddressInfoHolder extends RecyclerView.ViewHolder {

            RelativeLayout rlMainItem;

            TextView txtUserName;

            TextView addressdefault;

            TextView txtAddressDetail;

            TextView txtAddressPhone;

            LinearLayout llModifyAddress;

            Button txtDelete;

            public ItemAddressInfoHolder(View itemView) {
                super(itemView);
                txtUserName = (TextView)itemView.findViewById(R.id.txt_user_name);
                addressdefault = (TextView)itemView.findViewById(R.id.address_default);
                txtAddressDetail = (TextView)itemView.findViewById(R.id.txt_goods_address);
                txtAddressPhone = (TextView)itemView.findViewById(R.id.txt_address_phone);
                llModifyAddress = (LinearLayout)itemView.findViewById(R.id.modify_address);
                txtDelete = (Button)itemView.findViewById(R.id.delete);
                rlMainItem = (RelativeLayout) itemView.findViewById(R.id.main_item);
            }


        }

    }
}
