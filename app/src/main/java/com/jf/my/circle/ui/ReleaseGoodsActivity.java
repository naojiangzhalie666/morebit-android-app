package com.jf.my.circle.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jf.my.App;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.Module.common.Activity.ImagePagerActivity;
import com.jf.my.Module.common.Dialog.ReleaseGoodsDialog;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.R;
import com.jf.my.circle.adapter.ExtendableListViewAdapter;
import com.jf.my.circle.adapter.ImageGridAdapter;
import com.jf.my.fragment.base.BaseFeedBackFragment;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.CategoryListChildDtos;
import com.jf.my.pojo.CategoryListDtos;
import com.jf.my.pojo.ReleaseManage;
import com.jf.my.pojo.ShopGoodInfo;
import com.jf.my.pojo.Subject;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.event.ReleaseManageEvent;
import com.jf.my.pojo.request.RequestReleaseCategory;
import com.jf.my.pojo.request.RequestReleaseGoods;
import com.jf.my.utils.C;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.MathUtils;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.ReadImgUtils;
import com.jf.my.utils.StringsUtils;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.utils.action.MyAction;
import com.jf.my.utils.fire.OssManage;
import com.jf.my.view.ToolbarHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;
import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2019/6/11.
 * 查看文案
 */

public class ReleaseGoodsActivity extends BaseActivity {
    public final static int SUBMIT = 1;//可提交审核
    public final static int NOT_SUBMIT = 2; //不可提交审核
    public final static String ADD = "toAddPic";
    private final static int IMAGE_MAX= 9;
    @BindView(R.id.gridView)
    GridView mGridView;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.img)
    ImageView mImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.good_mall_tag)
    ImageView good_mall_tag;
    @BindView(R.id.tv_shop_name)
    TextView tv_shop_name;
    @BindView(R.id.coupon)
    TextView coupon;
    @BindView(R.id.discount_price)
    TextView discount_price;
    @BindView(R.id.commission)
    TextView commission;
    @BindView(R.id.ll_return_cash)
    LinearLayout ll_return_cash;
    @BindView(R.id.et_reason)
    EditText et_reason;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    int mType;
    int mFrom ;
    private ImageGridAdapter gridAdapter;
    private List<String> urls = new ArrayList<>();
    private ExtendableListViewAdapter mExpandableAdapter;
    private List<RecyclerViewData> mDatas = new ArrayList<>();
    ;

    private List<ShopGoodInfo> mShopGoodInfoList;
    private ShopGoodInfo mShopGoodInfo;
    private ReleaseManage mReleaseManage;
    private List<CategoryListDtos> mCategoryListDtos = new ArrayList<>();
    private List<CategoryListDtos> mCategoryListDtosSelect = new ArrayList<>();
    private List<String> uploadUrls = new ArrayList<>();
    private int mUploadSize;  //上传图片的个人
    public static void start(Context activity, List<ShopGoodInfo> shopGoodInfoList, ReleaseManage releaseManage, int type, int from) {
        Intent intent = new Intent(activity, ReleaseGoodsActivity.class);
        intent.putExtra("shopGoodInfoList", (ArrayList) shopGoodInfoList);
        intent.putExtra("from",from);
        intent.putExtra("releaseManage", releaseManage);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_goods);
        setStatusBarWhite();
        initView();
    }

    private void initView() {
        mShopGoodInfoList = (ArrayList) getIntent().getSerializableExtra("shopGoodInfoList");
        mReleaseManage = (ReleaseManage) getIntent().getSerializableExtra("releaseManage");
        mType = getIntent().getIntExtra("type", 1);
        mFrom = getIntent().getIntExtra("from", 1);
        initToolbar();
        initGridView();
        initGoodsDetial();
        getShareRangCategory();
    }

    private void initToolbar() {
        if(mFrom==1){
            new ToolbarHelper(this).setToolbarAsUp(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveGoods();
                }
            }).setCustomTitle(R.string.release_goods).setCustomRightTitle(R.string.submit_goods, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInput()) {
                        submitDialog();
                    }
                }
            });
        } else {
            if (mType == SUBMIT) {
                new ToolbarHelper(this).setToolbarAsUp(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveGoods();
                    }
                }).setCustomTitle(R.string.check_des).setCustomRightTitle(R.string.submit_goods, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkInput()) {
                            submitDialog();
                        }
                    }
                });
            } else {
                new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(R.string.check_des);
            }
        }

    }

    private void submitDialog() {
        ReleaseGoodsDialog releaseGoodsDialog = new ReleaseGoodsDialog(this, R.style.dialog, "提交素材", "确定要提交吗？", ReleaseGoodsDialog.EDIT);
        releaseGoodsDialog.setmOkListener(new ReleaseGoodsDialog.OnOkListener() {
            @Override
            public void onClick(View view) {
                uploadUrls.clear();
                uploadPicToServer();
            }
        });
        releaseGoodsDialog.show();
    }


    private void getShareRangCategory() {
        RequestReleaseCategory requestReleaseCategory = new RequestReleaseCategory();
        requestReleaseCategory.setType(0);
        RxHttp.getInstance().getCommonService().getShareRangCategory(requestReleaseCategory)
                .compose(RxUtils.<BaseResponse<List<CategoryListDtos>>>switchSchedulers())
                .compose(this.<BaseResponse<List<CategoryListDtos>>>bindToLifecycle())
                .subscribe(new DataObserver<List<CategoryListDtos>>() {
                    @Override
                    protected void onSuccess(List<CategoryListDtos> data) {
                        initModule(data);
                    }
                });
    }

    private void initGoodsDetial() {

        if (mShopGoodInfoList == null || mShopGoodInfoList.size() <=0 || mShopGoodInfoList.get(0) == null) {
            return;
        }

        mShopGoodInfo = mShopGoodInfoList.get(0);
        title.setText(MathUtils.getTitle(mShopGoodInfo));

        if (!TextUtils.isEmpty(mShopGoodInfo.getPicture())) {
            LoadImgUtils.setImg(this, mImg,MathUtils.getPicture(mShopGoodInfo));
        }
        if (StringsUtils.isEmpty(mShopGoodInfo.getCouponPrice())) {
            ll_return_cash.setVisibility(View.GONE);
        } else {
            ll_return_cash.setVisibility(View.VISIBLE);
        }

        //判断是淘宝还是天猫的商品
        if (mShopGoodInfo.getShopType() == 2) {
            good_mall_tag.setImageResource(R.drawable.tianmao);
        } else {
            good_mall_tag.setImageResource(R.drawable.taobao);
        }

        coupon.setText(getString(R.string.yuan, MathUtils.getCouponPrice(mShopGoodInfo.getCouponPrice())));

        if (C.UserType.operator.equals(UserLocalData.getUser(this).getPartner()) || C.UserType.vipMember.equals(UserLocalData.getUser(this).getPartner())) {
//            commission.setVisibility(View.VISIBLE);
            commission.setText(getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(this).getCalculationRate(), mShopGoodInfo.getCommission())));
        } else {
            commission.setText(getString(R.string.upgrade_commission));
//            commission.setVisibility(View.GONE);
        }
        //店铺名称
        if (!TextUtils.isEmpty(mShopGoodInfo.getShopName())) {
            tv_shop_name.setText(mShopGoodInfo.getShopName());
        }
        discount_price.setText(getString(R.string.income, MathUtils.getVoucherPrice(mShopGoodInfo.getVoucherPrice())));
        price.setText(getString(R.string.income, MathUtils.getPrice(mShopGoodInfo.getPrice())));
        price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    private void initGridView() {

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = urls.get(position);
                if (ADD.equals(imgs.toString())) {  //加号
                    ReadImgUtils.callPermissionMultiplePic(ReleaseGoodsActivity.this,IMAGE_MAX+1-urls.size());
                } else {
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    if (urls != null) {
                        for (int i = 0; i < urls.size(); i++) {
                            if (!ADD.equals(urls.get(i))) {
                                imageUrls.add(urls.get(i));
                            }
                        }
                    }
                    Intent intent = new Intent(ReleaseGoodsActivity.this, ImagePagerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
                    bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    bundle.putBoolean(ImagePagerActivity.IS_NATIVE_IMG, true);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            }
        });
        if (mType == SUBMIT) {//可以提交
            if (mReleaseManage == null) {   //新建
                urls.add(ADD);
            } else {      //编辑
                if (mReleaseManage.getPicture() != null) {
                    String[] pictures = mReleaseManage.getPicture().split(",");
                    if (pictures != null) {
                        for (int i = 0; i < pictures.length; i++) {
                            urls.add(pictures[i]);
                        }
                    }
                }
                if(urls.size()<9){
                    urls.add(ADD);
                }
                et_reason.setText(mReleaseManage.getContent());
            }
            et_reason.setEnabled(true);
        } else {   //不可以提交
            if (mReleaseManage.getPicture() != null) {
                String[] pictures = mReleaseManage.getPicture().split(",");
                if (pictures != null) {
                    for (int i = 0; i < pictures.length; i++) {
                        urls.add(pictures[i]);
                    }
                }
            }
            et_reason.setText(mReleaseManage.getContent());
            et_reason.setEnabled(false);
        }

        gridAdapter = new ImageGridAdapter(this, urls,mType);
        mGridView.setAdapter(gridAdapter);

        gridAdapter.setOnItemClickListener(new BaseFeedBackFragment.OnItemDeleteImgClickListener() {
            @Override
            public void onImgDelete(int position) {
                if(mType==NOT_SUBMIT){
                 return;
                }
                if (urls != null) {
                    if (urls.size() > 1&&urls.size()<IMAGE_MAX) {
                        urls.remove(position);
                    } else if(urls.size()==IMAGE_MAX){
                        if(position == urls.size()-1){
                            urls.remove(position);
                            urls.add(ADD);
                        } else {
                            urls.remove(position);
                        }
                    }
                    gridAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initModule(List<CategoryListDtos> data) {
//        for(int i=0;i<data.size();i++){  //去掉没有2级的
//            if(data.get(i).getChild()!=null&&data.get(i).getChild().size()>0){
//                mCategoryListDtos.add(data.get(i));
//            }
//        }
        mCategoryListDtos.addAll(data);
//        if (mType == SUBMIT) {
            if (mReleaseManage == null) {   //新建
                MyLog.i("test","mReleaseManage: " +mReleaseManage);
                setModuleData(mCategoryListDtos);
            } else {   //编辑
                replaceModuleData(mCategoryListDtos, mReleaseManage.getOpens());
            }
//        }

        mExpandableAdapter = new ExtendableListViewAdapter(this, mDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mExpandableAdapter);

        mExpandableAdapter.setOnItemClickListener(new OnRecyclerViewListener.OnItemClickListener() {
            @Override
            public void onGroupItemClick(int position, int groupPosition, View view) {
                if (mType == NOT_SUBMIT) {
                    return;
                }

                MyLog.i("test", "position: " + position + "  groupPosition: " + groupPosition);
                if (mCategoryListDtos.get(groupPosition) instanceof CategoryListDtos) {
                    CategoryListDtos oneSubject =  mCategoryListDtos.get(groupPosition);
                    oneSubject.setExpanded(!oneSubject.isExpanded());
                    mExpandableAdapter.notifyDataSetChanged();
                    if(oneSubject.isExpanded()){
                        scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    }
                }

            }

            @Override
            public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
                if (mType == NOT_SUBMIT) {
                    return;
                }
                MyLog.i("test", "position: " + position + "  groupPosition: " + groupPosition + " childPosition: " + childPosition);
                if (mCategoryListDtos.get(groupPosition).getChild().get(childPosition)!=null) {
                    CategoryListChildDtos oneSubject =  mCategoryListDtos.get(groupPosition).getChild().get(childPosition);
                    oneSubject.setSelect(!oneSubject.isSelect());
                    mExpandableAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void replaceModuleData(List<CategoryListDtos> data, List<Subject> subjects) {
        if (subjects == null || data == null) {
            return;
        }
        //循环已选中的专题
        for (int i = 0; i < subjects.size(); i++) {
            //循环所有一级专题
            for (int j = 0; j < data.size(); j++) {
                //判断已选中的一级专题是否相同
                if (subjects.get(i).getId() == data.get(j).getId()) {
                    List<CategoryListChildDtos> childs = data.get(j).getChild();
                    if (childs != null&&childs.size()>0) {   //如果有二级
                        //相等循环所有二级专题，并和已选中的二级专题进行比较
                        for (int k = 0; k < childs.size(); k++) {
                            //比较,相等 进行初始赋值
                            if (subjects.get(i).getTwoLevelId() == childs.get(k).getId()) {
                                data.get(j).setExpanded(true);
                                childs.get(k).setSelect(true);
                            }
                        }
                    } else {  //如果没有二级
                        //比较,相等 进行初始赋值
//                        if (subjects.get(i).getId() == data.get(j).getId()) {
                            data.get(j).setExpanded(true);
//                        }
                    }

                }
            }
        }
        //默认展开二级列表状态
        for (int i = 0; i < data.size(); i++) {
//            if (data.get(i).getChild() != null&&data.get(i).getChild().size()>0) {
                mDatas.add(new RecyclerViewData(data.get(i), data.get(i).getChild(), data.get(i).isExpanded()));
//            }
        }
    }

    private void setModuleData(List<CategoryListDtos> data) {
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                List<CategoryListChildDtos> childSubjects = data.get(i).getChild();
                data.get(i).setExpanded(true);
                if (childSubjects != null && childSubjects.size() > 0) {
                    childSubjects.get(0).setSelect(true);
                    mCategoryListDtosSelect.add(data.get(i));
                }
                mDatas.add(new RecyclerViewData(data.get(i), childSubjects, true));
            } else {
//                if (data.get(i).getChild() != null&&data.get(i).getChild().size()>0) {
                    mDatas.add(new RecyclerViewData(data.get(i), data.get(i).getChild(), false));
//                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList == null || selectList.size() == 0) return;
                    for (LocalMedia localMedia : selectList) {
                        if (!TextUtils.isEmpty(localMedia.getCompressPath())) {
                            setGridImagePic(localMedia.getCompressPath());
                        } else {
                            if (!TextUtils.isEmpty(localMedia.getPath())) {
                                setGridImagePic(localMedia.getPath());
                            }
                        }
                    }

                    break;
            }
        }
    }

    private void setGridImagePic(String path) {
        if (urls != null) {
            if (urls.size() < IMAGE_MAX) {
                urls.add(urls.size() - 1, path);
            } else if (urls.size() == IMAGE_MAX) {
                urls.add(urls.size() - 1, path);
                //去掉加号
                urls.remove(urls.size() - 1);
            }
        }
        gridAdapter.notifyDataSetChanged();
    }

    /**
     * 检查输入
     *
     * @return
     */
    private boolean checkInput() {
        if (urls.size() < 2) {
            ViewShowUtils.showShortToast(this, "请至少上传一个图片");
            return false;
        }
        if (et_reason.getText().toString().equals("") || et_reason.getText().toString().length() < 10 || et_reason.getText().toString().length() > 300) {
            Toast.makeText(this, "请输入文案，最少10个字，最多300字", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mCategoryListDtos.size() == 0) {
            ViewShowUtils.showShortToast(this, "请选择发布专题");
            return false;
        }
        setSubjectListSelect();
        if (mCategoryListDtosSelect.size() == 0) {
            ViewShowUtils.showShortToast(this, "请选择发布专题");
            return false;
        }

        return true;
    }

    /**
     * 设置选中的专题列表
     */
    private void setSubjectListSelect() {
        MyLog.i("test","setSubjectListSelect");
        mCategoryListDtosSelect.clear();
        for (int i = 0; i < mCategoryListDtos.size(); i++) {
            if (mCategoryListDtos.get(i).getChild() != null&&mCategoryListDtos.get(i).getChild().size()>0) {
                List<CategoryListChildDtos> child = mCategoryListDtos.get(i).getChild();
                List<CategoryListChildDtos> childSelect = new ArrayList<>();
                for (int j = 0; j < child.size(); j++) {
                    if (child.get(j).isSelect()) {
                        childSelect.add(child.get(j));
                    }
                }
                if (childSelect.size() > 0) {
                    mCategoryListDtos.get(i).setChild(childSelect);
                    mCategoryListDtosSelect.add(mCategoryListDtos.get(i));
                }
            } else {
                if(mCategoryListDtos.get(i).isExpanded()){
                    mCategoryListDtosSelect.add(mCategoryListDtos.get(i));
                }
            }
        }
    }

    /**
     * 把图片提交到服务器
     */
    public void uploadPicToServer() {
        mUploadSize = 0;
        UserInfo userInfo = UserLocalData.getUser(this);
        if (userInfo == null) {
            ViewShowUtils.showShortToast(this, "数据异常");
            return;
        }
        LoadingView.showDialog(this, "提交中...");
        for(int i=0;i<urls.size();i++){
            if(!ADD.equals(urls.get(i))){
                mUploadSize++;
            }
        }
        for (int i = 0; i < urls.size(); i++) {
            String s = urls.get(i);
                uploadPic(s);
        }
    }

    /**
     * 提交反馈意见
     */
    public void submitGoods(List<String> uploadUrls) {

        RequestReleaseGoods request = new RequestReleaseGoods();
        StringBuffer stringBuffer = new StringBuffer();
        if (mShopGoodInfo != null) {
            if (mShopGoodInfo.getItemBanner() != null) {

                for (int i = 0; i < mShopGoodInfo.getItemBanner().size(); i++) {
                    stringBuffer.append(mShopGoodInfo.getItemBanner().get(i));
                    if (mShopGoodInfo.getItemBanner().size() - 1 > i) {
                        stringBuffer.append(",");
                    }
                }

            }
        }
        if(mReleaseManage!=null&&mReleaseManage.getId()>0){
            request.setId(mReleaseManage.getId()+"");
        }
        request.setIcon(UserLocalData.getUser().getHeadImg());
        request.setCategoryListDtos(mCategoryListDtosSelect);
        request.setContent(et_reason.getText().toString());
        request.setItemBanner(stringBuffer.toString());
        mShopGoodInfo.setItemBanner(null);
        request.setGoods(mShopGoodInfoList);
        request.setName(UserLocalData.getUser().getNickName());
        request.setPicture(getUploadUrls(uploadUrls));
        RxHttp.getInstance().getUsersService()
                //                .getSubmitAppFeedBack(picUrl, et_statistics.getText().toString().trim(), gid, keys,2,"")
                .submitReleaseGoods(request)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        ViewShowUtils.showShortToast(ReleaseGoodsActivity.this, "提交成功");
                        if(mFrom==0){
                            ReleaseManageEvent releaseManageEvent = new ReleaseManageEvent();
                            EventBus.getDefault().postSticky(releaseManageEvent);
                        }
                        finish();
                    }
                });
    }

    /**
     * 把本地url数组转换成以逗号隔开的字符串
     *
     * @param localUrls
     */
    private String getLocalPicUrl(List<String> localUrls) {
        StringBuffer picUrl = new StringBuffer();
        if (localUrls != null) {
            if(localUrls.size()==1){
                return null;
            }
            for (int i = 0; i < localUrls.size(); i++) {
                if (!ADD.equals(localUrls.get(i))) {
                    picUrl.append(localUrls.get(i) + ",");
                }
            }

            return picUrl.toString().substring(0, picUrl.toString().lastIndexOf(","));
        }
      return null;
    }

    /**
     * 把已上传到服务器的url地址以逗号隔开
     *
     * @param uploadUrls
     */
    private String getUploadUrls(List<String> uploadUrls) {
        StringBuffer picUrl = new StringBuffer();
        if (uploadUrls != null) {
            for (int i = 0; i < uploadUrls.size(); i++) {
                if(i==uploadUrls.size()-1){
                    picUrl.append(uploadUrls.get(i));
                } else {
                    picUrl.append(uploadUrls.get(i) + ",");
                }

            }
            MyLog.i("test","picUrl: " +picUrl.toString());
            return picUrl.toString();
        }
        return null;
    }
    private void uploadPic(String path) {
        if(ADD.equals(path)){
            return;
        }
        OssManage.newInstance().putFielToOss(path, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                String s = arg == null ? "" : arg;
                if (!uploadUrls.contains(s)) {
                    uploadUrls.add(s);
                }
                MyLog.i("test", "uploadUrls: " + uploadUrls.size() + "  size: " + mUploadSize + " arg: " + arg);
                if (uploadUrls.size() == mUploadSize) {
                    submitGoods(uploadUrls);
                }
            }

            @Override
            public void onError() {
                if (uploadUrls.size() == mUploadSize) {
                    ViewShowUtils.showShortToast(ReleaseGoodsActivity.this,"上传图片失败");
                }
            }
        });
    }


    private void saveGoods() {
        if(mFrom==0){  //只有在商品详情页进来才保存
            finish();
            return;
        }
        ReleaseGoodsDialog releaseGoodsDialog = new ReleaseGoodsDialog(this, R.style.dialog, "提示", "保留此次编辑吗？", ReleaseGoodsDialog.EDIT);
        releaseGoodsDialog.setmCancelListener(new ReleaseGoodsDialog.OnCancelListner() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        releaseGoodsDialog.setmOkListener(new ReleaseGoodsDialog.OnOkListener() {
            @Override
            public void onClick(View view) {
                if (mReleaseManage == null) {
                    mReleaseManage = new ReleaseManage();
                }
                setSubjectListSelect();
                List<Subject> subjectList = new ArrayList<Subject>();
                for (int i = 0; i < mCategoryListDtosSelect.size(); i++) {
                    List<CategoryListChildDtos> childs = mCategoryListDtosSelect.get(i).getChild();
                    if (childs != null) {
                        for (int j = 0; j < childs.size(); j++) {
                            Subject subject = new Subject();
                            subject.setId(mCategoryListDtosSelect.get(i).getId());
                            subject.setName(mCategoryListDtosSelect.get(i).getTitle());
                            subject.setTwoLevelId(childs.get(j).getId());
                            subject.setTwoLevelName(childs.get(j).getTitle());
                            subjectList.add(subject);
                        }

                    }
                }
                MyLog.i("test", "su: " + subjectList.size());
                mReleaseManage.setName(UserLocalData.getUser().getNickName());
                mReleaseManage.setIcon(UserLocalData.getUser().getHeadImg());
                mReleaseManage.setContent(et_reason.getText().toString());
                mReleaseManage.setPicture(getLocalPicUrl(urls));
                mReleaseManage.setOpens(subjectList);
                mReleaseManage.setGoods(mShopGoodInfoList);
                App.getACache().put(C.sp.RELEASE_GOODS+mShopGoodInfo.getItemSourceId()+UserLocalData.getUser().getInviteCode(), mReleaseManage);
                finish();
            }
        });
        releaseGoodsDialog.show();
    }

    @Override
    public void onBackPressed() {
        saveGoods();
    }


    /**
            * 获取点击事件
     */
    @CallSuper
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isShouldHideKeyBord(view, ev)) {
                hideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判定当前是否需要隐藏
     */
    protected boolean isShouldHideKeyBord(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
