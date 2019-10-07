package com.jf.my.circle.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.my.Activity.ShowWebActivity;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.R;
import com.jf.my.circle.CollegeListActivity;
import com.jf.my.circle.adapter.MiYuanInformationAdapter;
import com.jf.my.contact.EventBusAction;
import com.jf.my.main.ui.NoticeActivity;
import com.jf.my.pojo.MessageEvent;
import com.jf.my.pojo.MiyuanInformation;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.utils.UI.BannerInitiateUtils;
import com.jf.my.utils.UploadingOnclickUtils;
import com.jf.my.view.ToolbarHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author YangBoTian
 * @date   2019/9/2
 * @des
 *
 */
public class MiYuanInformationFragment extends BaseFragment{

   private List<MiyuanInformation> mList;
   private MiYuanInformationAdapter mAdapter;
  @BindView(R.id.mListView)
    RecyclerView mRecyclerView;

    public static void start(Context context, List<MiyuanInformation> list) {
        Bundle args = new Bundle();
        args.putSerializable("list",  (ArrayList)list);
        OpenFragmentUtils.goToSimpleFragment(context, MiYuanInformationFragment.class.getName(), args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi_yuan_information, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBundle();
        initView();
    }

    private void initBundle() {
        Bundle bundle  = getArguments();
        if(bundle!=null){
            mList = (List<MiyuanInformation>) bundle.getSerializable("list");
            if(mList==null){
                mList = new ArrayList<>();
            }
        }
    }

    private void initView() {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(R.string.mi_yuan_information);
        mAdapter = new MiYuanInformationAdapter(getActivity(),mList);
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.replace(mList);
        mAdapter.setOnClickListenter(new MiYuanInformationAdapter.OnClickListenter() {
            @Override
            public void onClick(View v, int position) {
                if(mAdapter.getItem(position).getOpen()==3){
                    UploadingOnclickUtils.updateThemeClicks(mAdapter.getItem(position).getId(),1);
                    ShowWebActivity.start(getActivity(), mAdapter.getItem(position).getUrl(), mAdapter.getItem(position).getTitle());
                } else if(mAdapter.getItem(position).getOpen()==9) {
                    BannerInitiateUtils.goToArticle(getActivity(), mAdapter.getItem(position).getUrl(), mAdapter.getItem(position).getTitle());
                    UploadingOnclickUtils.mp4Browse(mAdapter.getItem(position).getId(),1);
                }

               mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.notifyDataSetChanged();
    }

}
