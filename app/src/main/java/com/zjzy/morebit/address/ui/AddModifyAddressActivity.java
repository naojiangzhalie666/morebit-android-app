package com.zjzy.morebit.address.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.db.manager.AddressDictManager;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;
import com.zjzy.morebit.Activity.ChannelWebActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.address.contract.AddOrModifyAddressContract;
import com.zjzy.morebit.address.presenter.AddOrModifyAddressPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by haiping.liu on 2019-12-14.
 */
public class AddModifyAddressActivity extends MvpActivity<AddOrModifyAddressPresenter> implements View.OnClickListener,
        OnAddressSelectedListener, AddressSelector.OnDialogCloseListener, AddressSelector.onSelectorAreaPositionListener ,
        AddOrModifyAddressContract.View {

    private static  final String TAG = AddModifyAddressActivity.class.getSimpleName();
    @BindView(R.id.txt_head_title)
    TextView headTitle;

    @BindView(R.id.btn_back)
    LinearLayout btnBack;

    @BindView(R.id.btn_save)
    TextView btnSave;

    @BindView(R.id.edt_address_user_name)
    EditText userName;

    @BindView(R.id.address_editPhone)
    EditText phone;



    @BindView(R.id.txt_provice_city_dist)
    TextView proviceCityDist;

    @BindView(R.id.edt_add_address_detail)
    EditText editDetail;


    @BindView(R.id.rl_add_address)
    RelativeLayout rlAddAddress;

    @BindView(R.id.switch_address_default)
    SwitchCompat swtichAddressDefault;

    private BottomDialog dialog;

    private AddressInfo mAddressInfo;

    private int type;

    private String mProvice;

    private String mCity;

    private String mDistinct;
    private int mAddressId;
    private String mName;
    private String mPhone;
    private String mDetail;
    private int mDefaultFlag = 0;


    private String provinceCode;
    private String cityCode;
    private String countyCode;
    private String streetCode;
    private int provincePosition;
    private int cityPosition;
    private int countyPosition;
    private int streetPosition;

    private int mAddressPosition;


    /**
     * 更新或者增加地址的页面
     * @param activity
     * @param info
     * @param type
     */
    public static void start(Activity activity, AddressInfo info,int type) {
        //跳转到网页
        Intent it = new Intent(activity, AddModifyAddressActivity.class);

            Bundle bundle = new Bundle();
            bundle.putInt(C.Extras.TYPE_ADDRESS,type);
            if (info != null){
                bundle.putSerializable(C.Extras.GOODS_ADDRESS_INFO,info);
            }
            it.putExtras(bundle);
            switch (type){
                case C.Address.UPDATE_TYPE:
                    activity.startActivityForResult(it,ManageGoodsAddressActivity.REQUEST_UPDATE_ADDRESS_CODE);
                    break;
                case C.Address.ADD_TYPE:
                    activity.startActivityForResult(it,ManageGoodsAddressActivity.REQUEST_ADD_ADDRESS_CODE);
                    break;
                    default:
                        break;
            }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }


    @Override
    public void onAddSuccessful(Boolean isSuccess) {
        if (isSuccess){
            ViewShowUtils.showShortToast(AddModifyAddressActivity.this, "收货地址添加成功");
        }else{
            ViewShowUtils.showShortToast(AddModifyAddressActivity.this, "添加失败");
        }
        setResult(Activity.RESULT_OK);
        finish();

    }

    @Override
    public void onUpdateSuccessful(Boolean isSuccess) {
        if (isSuccess){
            ViewShowUtils.showShortToast(AddModifyAddressActivity.this, "收货地址更新成功");
        }else{
            ViewShowUtils.showShortToast(AddModifyAddressActivity.this, "更新地址失败");
        }

        setResult(Activity.RESULT_OK);
        finish();
    }

     @Override
    public void onUpdateError() {
         ViewShowUtils.showShortToast(AddModifyAddressActivity.this, "更新失败");
    }
     @Override
    public void onAddError() {
         ViewShowUtils.showShortToast(AddModifyAddressActivity.this, "添加失败");
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_add_modify_address;
    }

    private void initView(){
        type = getIntent().getIntExtra(C.Extras.TYPE_ADDRESS,0);
        if (type == C.Address.UPDATE_TYPE){
            mAddressPosition = getIntent().getExtras().getInt("addressPosition");
        }


        swtichAddressDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mDefaultFlag = 1;
                    swtichAddressDefault.setChecked(true);
                }else{
                    mDefaultFlag = 0;
                    swtichAddressDefault.setChecked(false);
                }
            }
        });
        if (type == C.Address.UPDATE_TYPE){
            headTitle.setText("更新收货地址");
        }else{
            headTitle.setText("添加收货地址");
        }


    };

    /**
     * 初始化数据
     */
    private void initData(){


        if (type == C.Address.UPDATE_TYPE){
            mAddressInfo = (AddressInfo) getIntent().getSerializableExtra(C.Extras.GOODS_ADDRESS_INFO);
            if (mAddressInfo == null){
                MyLog.e(TAG,"更新地址，没有传递地址信息");
                return;
            }
            mDefaultFlag = mAddressInfo.getIsDefault();
            if (mDefaultFlag == 1 ){
                swtichAddressDefault.setChecked(true);
            }else{
                swtichAddressDefault.setChecked(false);
            }
            userName.setText(mAddressInfo.getName());
            phone.setText(mAddressInfo.getTel());
            mAddressId = mAddressInfo.getId();
            mName = mAddressInfo.getName();
            mPhone = mAddressInfo.getTel();
            mProvice = mAddressInfo.getProvince();
            mCity = mAddressInfo.getCity();
            mDistinct = mAddressInfo.getDistrict();
            mDetail = mAddressInfo.getDetailAddress();
            proviceCityDist.setText(mAddressInfo.getProvince()+mAddressInfo.getCity()+mAddressInfo.getDistrict());
            editDetail.setText(mAddressInfo.getDetailAddress());

        }else{
            mDefaultFlag= (swtichAddressDefault.isChecked()? 1:0);
        }
//        AddressSelector selector = new AddressSelector(this);
//        AddressDictManager addressDictManager = selector.getAddressDictManager();
//        addressDictManager.deleteAll();
//        if (!UserLocalData.getSavedRegionFlag()){
//            mPresenter.getAllRegion(this);
//        }

    }
    @OnClick({R.id.btn_save,R.id.btn_back,R.id.rl_select_area})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                //返回
                finish();
                break;
            case R.id.btn_save:
                if (type == C.Address.UPDATE_TYPE){
                    mAddressInfo = createAddressInfo();
                    if (mAddressInfo != null){
                        mAddressInfo.setId(mAddressId);
                        mPresenter.updateAddress(AddModifyAddressActivity.this,mAddressInfo);
                    }
                }else{
                    mAddressInfo = createAddressInfo();
                    if (mAddressInfo != null){
                        mPresenter.addAddress(AddModifyAddressActivity.this,mAddressInfo);
                    }
                }

                break;
            case R.id.rl_select_area:
                if (dialog != null) {
                    dialog.show();
                } else {
                    dialog = new BottomDialog(this);
                    dialog.setOnAddressSelectedListener(this);
                    dialog.setDialogDismisListener(this);
                    dialog.setTextSize(14);//设置字体的大小
                    dialog.setIndicatorBackgroundColor(R.color.color_FD4601);//设置指示器的颜色
                    dialog.setTextSelectedColor(R.color.color_FD4601);//设置字体获得焦点的颜色
                    dialog.setTextUnSelectedColor(R.color.color_333333);//设置字体没有获得焦点的颜色
                    dialog.setSelectorAreaPositionListener(this);
                    dialog.show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 检查地址信息
     */
    private boolean checkInfo(){
        boolean checkFlag = false;
        mName = userName.getText().toString();
        if (TextUtils.isEmpty(mName)){
            ViewShowUtils.showShortToast(AddModifyAddressActivity.this, "请输入收货人");
            return checkFlag;
        }
        mPhone = phone.getText().toString();
        if (TextUtils.isEmpty(mPhone)){
            ViewShowUtils.showShortToast(AddModifyAddressActivity.this, "请输入手机号");
            return checkFlag;
        }
        if (TextUtils.isEmpty(mProvice)&&TextUtils.isEmpty(mCity)&&TextUtils.isEmpty(mDistinct)){
            ViewShowUtils.showShortToast(AddModifyAddressActivity.this, "请选择所在地区");
            return checkFlag;
        }
        mDetail = editDetail.getText().toString();
        if (TextUtils.isEmpty(mDetail)){
            ViewShowUtils.showShortToast(AddModifyAddressActivity.this, "请输入详细地址");
            return checkFlag;
        }
        checkFlag = true;
        return checkFlag;
    }

    /**
     * 创建地址信息
     */
    private AddressInfo createAddressInfo(){
        if (!checkInfo()){
            return null;
        }
            AddressInfo info = new AddressInfo();

            info.setName(mName);
            info.setTel(mPhone);
            info.setProvince(mProvice);
            info.setCity(mCity);
            info.setDistrict(mDistinct);
            info.setDetailAddress(mDetail);
            info.setIsDefault(mDefaultFlag);
            return info;
    }

    private void AddressSelectorCreate(){

    }

    @Override
    public void dialogclose() {
        if(dialog!=null){
            dialog.dismiss();
        }
    }

    @Override
    public void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition) {
        this.provincePosition = provincePosition;
        this.cityPosition = cityPosition;
        this.countyPosition = countyPosition;
        this.streetPosition = streetPosition;
        MyLog.d("数据", "省份位置=" + provincePosition);
        MyLog.d("数据", "城市位置=" + cityPosition);
        MyLog.d("数据", "乡镇位置=" + countyPosition);
        MyLog.d("数据", "街道位置=" + streetPosition);
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String provinceCode = (province == null ? "" : province.code);
        String cityCode = (city == null ? "" : city.code);
        String countyCode = (county == null ? "" : county.code);
        String streetCode = (street == null ? "" : street.code);
        MyLog.d("数据", "省份id=" + provinceCode);
        MyLog.d("数据", "城市id=" + cityCode);
        MyLog.d("数据", "乡镇id=" + countyCode);
        MyLog.d("数据", "街道id=" + streetCode);
         mProvice = (province == null ? "" : province.name);
         mCity = (city == null ? "" : city.name);
         mDistinct = (county == null ? "" : county.name);

        String s = (province == null ? "" : province.name) + (city == null ? "" : city.name) + (county == null ? "" : county.name) +
                (street == null ? "" : street.name);
        proviceCityDist.setText(s);
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
