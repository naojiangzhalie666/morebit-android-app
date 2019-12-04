package com.zjzy.morebit.info.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.request.RequestNickNameBean;
import com.zjzy.morebit.pojo.request.RequestUpdateHeadPortraitBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.ReadImgUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.utils.fire.OssManage;
import com.zjzy.morebit.view.ToolbarHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Action;

/**
 * @author fengrs
 * @date 2019/8/30
 */
public class SettingMineInfoActivity extends BaseActivity {

    @BindView(R.id.userIcon)
    ImageView mUserIcon;
    @BindView(R.id.rl_head)
    RelativeLayout mRlHead;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.rl_nickname)
    RelativeLayout mRlNickname;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout mRlSex;
    @BindView(R.id.tv_age)
    TextView mTvAge;
    @BindView(R.id.rl_age)
    RelativeLayout mRlAge;
    private TimePickerView mPvTime;
    private OptionsPickerView<String> mPvCustomOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_mine_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_757575); //设置标题栏颜色值
        }
        EventBus.getDefault().register(this);
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.mine_info));
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        UserInfo user = UserLocalData.getUser();
        if (user.getHeadImg() == null || "".equals(user.getHeadImg())) {
            mUserIcon.setImageResource(R.drawable.head_icon); //显示图片
        } else {
            LoadImgUtils.setImgCircle(this, mUserIcon, user.getHeadImg());
        }
        if (user.getNickName() != null && !"".equals(user.getNickName())) {
            mTvNickname.setText(user.getNickName());
        }
        if (user.getSex() != 0) {
            mTvSex.setText(user.getSex() == 1 ? "男" : "女");
        }
        if (!TextUtils.isEmpty(user.getBirthDate())) {
            mTvAge.setText(user.getBirthDate());
        }
    }


    @OnClick({R.id.rl_head, R.id.rl_nickname, R.id.rl_sex, R.id.rl_age})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_head:
                ReadImgUtils.callPermissionOfEnableCrop(this);
                break;
            case R.id.rl_nickname:
                openDialogUserName();
                break;
            case R.id.rl_sex:
                openDialogUsersex();
                break;
            case R.id.rl_age:
                openDialogUserAge();
                break;
        }
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        if (event.getAction().equals(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA)) { //更新个人信息
            initView();
        }
    }


    private void openDialogUserName() {  //打开修改名称
        Bundle bundle6 = new Bundle();
        bundle6.putString("title", "昵称");
        bundle6.putString("fragmentName", "RenameFragment");
        bundle6.putString("UserName", UserLocalData.getUser().getNickName());
        PageToUtil.goToUserInfoSimpleFragment(this, bundle6);
    }


    private void openDialogUserAge() {
        if (mPvTime == null) {
            //默认设置false ，内部实现将DecorView 作为它的父控件。

            Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            //startDate.set(2013,1,1);
            Calendar endDate = Calendar.getInstance();
            //endDate.set(2020,1,1);

            //正确设置方式 原因：注意事项有说明
            startDate.set(1900, 1, 1);
            endDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE));

            mPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    MyLog.i("pvTime", "onTimeSelect");
                    String ymd = DateTimeUtils.getYmd(date);
                    updataBirthDate(ymd);
                }
            })
                    .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                    .setRangDate(startDate, endDate)//起始终止年月日设定
                    .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .setLabel("", "", "", "", "", "")
                    .build();

            Dialog mDialog = mPvTime.getDialog();
            if (mDialog != null) {

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM);

                params.leftMargin = 0;
                params.rightMargin = 0;
                mPvTime.getDialogContainerLayout().setLayoutParams(params);

                Window dialogWindow = mDialog.getWindow();
                if (dialogWindow != null) {
                    dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                    dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                    dialogWindow.setDimAmount(0.1f);
                }
            }

        }

        mPvTime.show();
    }

    private void updataBirthDate(final String set) {

        RequestNickNameBean requestBean = new RequestNickNameBean();
        requestBean.setBirthDate(set);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);
        RxHttp.getInstance().getUsersService()
                .setNickname(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
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
                        ViewShowUtils.showShortToast(SettingMineInfoActivity.this, "修改成功");
                        //更新个人数据
                        UserLocalData.getUser().setBirthDate(set);
                        if (!TextUtils.isEmpty(set)) {
                            mTvAge.setText(set);
                        }
                    }
                });
    }


    private void updataSex(final int sex) {
        if (sex == 0) return;
        RequestNickNameBean requestBean = new RequestNickNameBean();
        requestBean.setSex(sex+"");
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);
        RxHttp.getInstance().getUsersService()
                .setNickname(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
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
                        ViewShowUtils.showShortToast(SettingMineInfoActivity.this, "修改成功");
                        //更新个人数据
                        UserLocalData.getUser().setSex(sex);
                        mTvSex.setText(sex == 1 ? "男" : "女");

                    }
                });
    }


    private void openDialogUsersex() {
        if (mPvCustomOptions == null) {
            mPvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    MyLog.d(options1);
                    updataSex(options1 + 1);
                }
            })
                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(20)
                    .build();
            ArrayList<String> cardItem = new ArrayList<>();
            cardItem.add("男");
            cardItem.add("女");
            mPvCustomOptions.setPicker(cardItem);//添加数据
        }
        mPvCustomOptions.show();

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
                    LocalMedia localMedia = selectList.get(0);
                    String compressPath = localMedia.getCompressPath();
                    if (!TextUtils.isEmpty(compressPath)) {

                        uploadPicOnly(compressPath);
                    } else {
                        if (!TextUtils.isEmpty(localMedia.getPath())) {
                            uploadPicOnly(localMedia.getPath());
                        }
                    }

                    break;
            }
        }
    }

    /**
     * 上传图片到服务器
     *
     * @param
     */
    private void uploadPicOnly(String path) {
        LoadingView.showDialog(this, "提交中...");
        OssManage.newInstance().putFielToOss(path, new MyAction.OnResult<String>() {
            @Override
            public void invoke(final String arg) {
                if (!TextUtils.isEmpty(arg)) {
                    setHead(arg);
                }
            }

            @Override
            public void onError() {
                LoadingView.dismissDialog();
            }
        });

    }

    /**
     * 修改头像
     *
     * @param picPath
     */
    private void setHead(final String picPath) {
        LoadingView.showDialog(this, "提交中...");
        RequestUpdateHeadPortraitBean requestBean = new RequestUpdateHeadPortraitBean();
        requestBean.setHeadImg(picPath);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);
        RxHttp.getInstance().getUsersService()
                .setHead(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        super.onError(errorMsg, errCode);
                        LoadingView.dismissDialog();
                    }

                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        ViewShowUtils.showShortToast(SettingMineInfoActivity.this, "修改成功");
                        //更新个人数据
                        UserLocalData.getUser(SettingMineInfoActivity.this).setHeadImg(picPath);
                        UserLocalData.isPartner = true;
                        LoadImgUtils.setImgCircle(SettingMineInfoActivity.this, mUserIcon, picPath);
                        EventBus.getDefault().post(new MessageEvent(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
