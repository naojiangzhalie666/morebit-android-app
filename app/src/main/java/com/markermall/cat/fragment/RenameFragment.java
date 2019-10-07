package com.markermall.cat.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.Module.common.Fragment.BaseFragment;
import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.R;
import com.markermall.cat.contact.EventBusAction;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.MessageEvent;
import com.markermall.cat.pojo.request.RequestNickNameBean;
import com.markermall.cat.utils.ActivityStyleUtil;
import com.markermall.cat.utils.ViewShowUtils;
import com.markermall.cat.utils.encrypt.EncryptUtlis;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Action;


/**
 * 修改昵称界面
 */
public class RenameFragment extends BaseFragment implements View.OnClickListener {

    private Bundle bundle;
    private EditText et_name;
    private ImageView clear;
    private String mGetName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_rename, container, false);
        inview(view);
        initViewData();
        return view;
    }

    public void inview(View view) {
        ActivityStyleUtil.initSystemBar(getActivity(), R.color.color_757575); //设置标题栏颜色值
        et_name = (EditText) view.findViewById(R.id.et_name);
        clear = (ImageView) view.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //判断是否显示删除按钮
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    if (clear.getVisibility() != View.GONE) {
                        clear.setVisibility(View.GONE);
                    }
                } else {
                    if (clear.getVisibility() != View.VISIBLE) {
                        clear.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        view.findViewById(R.id.submit).setOnClickListener(this);
    }

    /**
     * 初始化界面数据
     */
    private void initViewData() {
        bundle = getArguments();
        if (bundle != null) {
            mGetName = bundle.getString("UserName");
            if (!TextUtils.isEmpty(mGetName)) {
                et_name.setText(mGetName);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:
                et_name.setText("");
                break;
            case R.id.submit: //确认修改
                submit();
                break;
            default:
                break;

        }
    }

    /**
     * 保存昵称
     */
    private void submit() {
        if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
            ViewShowUtils.showShortToast(getActivity(), "请输入昵称");
            return;
        }
        if (et_name.getText().toString().trim().equals(mGetName)) {
            ViewShowUtils.showShortToast(getActivity(), "已经是当前昵称,不要重复提交");
            return;
        }
        uploadPic(et_name.getText().toString().trim());
    }


    /**
     * 修改用户信息
     *
     * @param userName
     */
    private void uploadPic(final String userName) {


        LoadingView.showDialog(getActivity(), "提交中...");

        RequestNickNameBean requestBean = new RequestNickNameBean();
        requestBean.setNickname(userName);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);

        RxHttp.getInstance().getUsersService()
//                .setNickName(userName, sign)
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

                    /**
                     * 成功回调
                     *
                     * @param data 结果
                     */
                    @Override
                    protected void onSuccess(String data) {
                        ViewShowUtils.showShortToast(getActivity(), "修改成功");
                        //更新个人数据
                        UserLocalData.getUser(getActivity()).setNickName(userName);
                        EventBus.getDefault().post(new MessageEvent(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA));
                        getActivity().finish();
                    }
                });


    }


}
