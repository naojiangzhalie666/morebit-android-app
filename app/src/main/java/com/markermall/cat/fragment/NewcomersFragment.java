package com.markermall.cat.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.markermall.cat.Activity.ShowWebActivity;
import com.markermall.cat.Module.common.Fragment.BaseFragment;
import com.markermall.cat.R;
import com.markermall.cat.adapter.NewComersAdapter;
import com.markermall.cat.main.model.ConfigModel;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.HotKeywords;
import com.markermall.cat.pojo.ProtocolRuleBean;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.LoginUtil;
import com.markermall.cat.utils.OpenFragmentUtils;
import com.markermall.cat.view.FixListView;
import com.markermall.cat.view.ToolbarHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 新手上路
 */
public class NewcomersFragment extends BaseFragment implements View.OnClickListener {

    private WebView webview;
    private List<ProtocolRuleBean> arrNewComers = new ArrayList<>(); //分类信息
    private FixListView list_lv;
    private NewComersAdapter ncomesAdapter;
    private ImageView banner;
    private String mTitle;

    public static void start(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "新人课堂");
        OpenFragmentUtils.goToSimpleFragment(activity, NewcomersFragment.class.getName(), bundle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_newcomers, container, false);
        inview(view);
        getNewComersPic();//获取新手上路图片
        getListData();//获取新人课堂
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString("title");
            new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(mTitle);
        }
    }

    public void inview(View view) {
        banner = (ImageView) view.findViewById(R.id.banner);
        list_lv = (FixListView) view.findViewById(R.id.list_lv);
        list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowWebActivity.start(getActivity(), arrNewComers.get(i).getHtmlUrl(),arrNewComers.get(i).getTitle());

//                Intent it = new Intent(getActivity(), ShowWebActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("title", arrNewComers.get(i).getTitle());
//                bundle.putString("url", arrNewComers.get(i).getHtmlUrl());
//                it.putExtras(bundle);
//                startActivity(it);
            }
        });


    }


    /**
     * 获取新人课堂列表
     */
    public void getListData() {
        LoginUtil.getSystemStaticPage((RxAppCompatActivity) getActivity(), C.ProtocolType.comers)
                .subscribe(new DataObserver<List<ProtocolRuleBean>>() {
                    @Override
                    protected void onSuccess(List<ProtocolRuleBean> data) {
                        arrNewComers.clear();
                        arrNewComers.addAll(data);
                        ncomesAdapter = new NewComersAdapter(getActivity(), arrNewComers);
                        list_lv.setAdapter(ncomesAdapter);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    public void getNewComersPic() {
        ConfigModel.getInstance().getConfigForKey((RxAppCompatActivity) getActivity(), C.SysConfig.NEW_PEOPLE_BANNER)
                .subscribe(new DataObserver<HotKeywords>() {

                    @Override
                    protected void onSuccess(HotKeywords data) {
                        String sysValue = data.getSysValue();
                        if (!TextUtils.isEmpty(sysValue)) {
                            LoadImgUtils.setImg(getActivity(), banner, sysValue);
                        }
                    }
                });
    }
}
