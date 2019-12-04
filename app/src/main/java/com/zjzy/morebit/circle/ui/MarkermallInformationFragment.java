package com.zjzy.morebit.circle.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.adapter.MarkermallInformationAdapter;
import com.zjzy.morebit.pojo.MarkermallInformation;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.UploadingOnclickUtils;
import com.zjzy.morebit.view.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author YangBoTian
 * @date   2019/9/2
 * @des
 *
 */
public class MarkermallInformationFragment extends BaseFragment{

   private List<MarkermallInformation> mList;
   private MarkermallInformationAdapter mAdapter;
  @BindView(R.id.mListView)
    RecyclerView mRecyclerView;

    public static void start(Context context, List<MarkermallInformation> list) {
        Bundle args = new Bundle();
        args.putSerializable("list",  (ArrayList)list);
        OpenFragmentUtils.goToSimpleFragment(context, MarkermallInformationFragment.class.getName(), args);
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
            mList = (List<MarkermallInformation>) bundle.getSerializable("list");
            if(mList==null){
                mList = new ArrayList<>();
            }
        }
    }

    private void initView() {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(R.string.mi_yuan_information);
        mAdapter = new MarkermallInformationAdapter(getActivity(),mList);
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.replace(mList);
        mAdapter.setOnClickListenter(new MarkermallInformationAdapter.OnClickListenter() {
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
