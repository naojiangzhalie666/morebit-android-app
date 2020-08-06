package com.zjzy.morebit.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.FansRemarkDialog;
import com.zjzy.morebit.Module.common.Dialog.QrcodeDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.MyTeamAdapter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.FansInfo;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.request.RequestFansInfoBean;
import com.zjzy.morebit.pojo.request.RequestRemarkBean;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.SoftInputUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.view.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2019/5/15.
 */

public class SearchFansActivity extends BaseActivity {
    @BindView(R.id.search_et)
    ClearEditText search_et;
    @BindView(R.id.search_rl)
    RelativeLayout search_rl;
    @BindView(R.id.listview)
    RecyclerView mRecyclerView;

    private QrcodeDialog mQrcodeDialog;
    private MyTeamAdapter myTeamAdapter;
    List<TeamInfo> listArray = new ArrayList<>();
    private String mSearchKey;
    private FansRemarkDialog mRemarkDialog;
    private LinearLayout dateNullView;
    private ImageView iv_back;
    public static void start(Context context, String search_key) {
        Intent intent = new Intent(context, SearchFansActivity.class);
        intent.putExtra("search_key", search_key);
        context.startActivity(intent);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        TeamInfo teamInfo = (TeamInfo) bundle.getSerializable("info");
                        FansInfo fansInfo = (FansInfo) bundle.getSerializable("fansInfo");
                        mQrcodeDialog.refreshData(teamInfo, fansInfo);
                    }
                    break;
                case 1:
                    Bundle bundle1= msg.getData();
                    if(bundle1!=null) {
                        String remark = bundle1.getString("remark");
                        int position = bundle1.getInt("position");
                        mRemarkDialog.setRemarkAnPosition(remark,position);
                        break;
                    }
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fans);
        ImmersionBar.with(this).statusBarColor(R.color.transparent).fitsSystemWindows(false).statusBarDarkFont(true, 0.2f).init();
        mSearchKey = getIntent().getStringExtra("search_key");
        initView();
    }

    private void initView() {
        iv_back= (ImageView) findViewById(R.id.iv_back);
        search_rl.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ECECEC));
        search_et.setHintTextColor(ContextCompat.getColor(this, R.color.color_999999));
        search_et.setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        dateNullView= (LinearLayout) findViewById(R.id.dateNullView);
        myTeamAdapter = new MyTeamAdapter(this, listArray);
        myTeamAdapter.setUserInfo(UserLocalData.getUser(this));
        myTeamAdapter.setOnAdapterClickListener(new MyTeamAdapter.OnAdapterClickListener() {
            @Override
            public void onItem(int position) {
                findFansDetail(listArray.get(position), listArray.get(position).getId() + "");

            }

            @Override
            public void onRemark(int position) {
                openRemarkDialog(listArray.get(position).getRemark(),position);
            }
        });
        mRecyclerView.setAdapter(myTeamAdapter);
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                MyLog.i("test", "onEditorAction: " + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchUser(search_et.getText().toString().trim(),false);
                    SoftInputUtil.hideSoftInputAt(search_et);
                    return true;
                }
                return false;
            }
        });
        search_et.setText(mSearchKey);
        search_et.setHint("输入粉丝手机号/用户名/备注");
//        searchUser(mSearchKey,true);
    }

    private void searchUser(String phoneOrActivationCode,boolean isFrist) {
        if (TextUtils.isEmpty(phoneOrActivationCode)) {
            ViewShowUtils.showShortToast(this, "输入粉丝手机号/用户名/备注");
            return;
        }

        RequestFansInfoBean requestFansInfoBean = new RequestFansInfoBean();
        requestFansInfoBean.setPhoneOrActivationCode(phoneOrActivationCode);
        RxHttp.getInstance().getCommonService().searchUser(requestFansInfoBean)
                .compose(RxUtils.<BaseResponse<List<TeamInfo>>>switchSchedulers())
                .subscribe(new DataObserver<List<TeamInfo>>() {
                    @Override
                    protected void onSuccess(List<TeamInfo> data) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        dateNullView.setVisibility(View.GONE);
                        listArray.clear();
                        listArray.addAll(data);
                        myTeamAdapter.setData(listArray);
                        myTeamAdapter.notifyDataSetChanged();

                    }

                    @Override
                    protected void onDataListEmpty() {
                        super.onDataListEmpty();
                        mRecyclerView.setVisibility(View.GONE);
                        dateNullView.setVisibility(View.VISIBLE);

                    }
                });


    }

    @OnClick({R.id.search, R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                searchUser(search_et.getText().toString().trim(),true);
                break;
            case R.id.iv_back:
                finish();
                isMethodManager(iv_back);
                break;
        }
    }

    private void findFansDetail(final TeamInfo info, String userId) {
        RequestFansInfoBean requestFansInfoBean = new RequestFansInfoBean();
        requestFansInfoBean.setId(userId);
        RxHttp.getInstance().getCommonService().getFansDetail(requestFansInfoBean)
                .compose(RxUtils.<BaseResponse<FansInfo>>switchSchedulers())
                .subscribe(new DataObserver<FansInfo>() {
                    @Override
                    protected void onSuccess(FansInfo data) {
                        openQrcodeDialog(info, data);
                    }
                });
    }

    private void openQrcodeDialog(final TeamInfo info, final FansInfo fansInfo) {  //退出确认弹窗
        if (mQrcodeDialog == null) {
            mQrcodeDialog = new QrcodeDialog(this);
        }
        mQrcodeDialog.show();
        Message message = new Message();
        message.what = 0;
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        bundle.putSerializable("fansInfo", fansInfo);
        message.setData(bundle);
        handler.sendMessage(message);
    }


    private void openRemarkDialog(String remark, final int position) {  //退出确认弹窗
        if (mRemarkDialog == null) {
            mRemarkDialog = new FansRemarkDialog(this);
            mRemarkDialog.setOnClickListener(new FansRemarkDialog.OnClickListener() {
                @Override
                public void onClick(View v, String remark,int position) {
                    updateRemark(remark,position);
                    mRemarkDialog.dismiss();
                }
            });
        }
        mRemarkDialog.show();
        Message message = new Message();
        message.what=1;
        Bundle bundle = new Bundle();
        bundle.putString("remark",remark);
        bundle.putInt("position",position);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void updateRemark(String remark,int position) {
        if(TextUtils.isEmpty(remark)){
            return;
        }
        RequestRemarkBean bean = new RequestRemarkBean();
        bean.setRemark(remark);
        bean.setRemark(listArray.get(position).getId()+"");
        RxHttp.getInstance().getCommonService().updateRemark(bean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {

                    }
                });
    }
}
