package com.jf.my.circle.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.R;
import com.jf.my.adapter.SimpleAdapter;
import com.jf.my.adapter.holder.SimpleViewHolder;
import com.jf.my.circle.adapter.MiYuanInformationAdapter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.MiyuanInformation;
import com.jf.my.pojo.UserFeedback;
import com.jf.my.pojo.request.RequestReplyBean;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.view.ToolbarHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author YangBoTian
 * @date 2019/9/5
 * @des
 */
public class OpinionpReplyFragment extends BaseFragment {


    @BindView(R.id.icon_head1)
    RoundedImageView icon_head1;
    @BindView(R.id.icon_head3)
    RoundedImageView icon_head3;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_reply)
    TextView tv_reply;
    @BindView(R.id.rl_reply)
    RelativeLayout rl_reply;
    @BindView(R.id.mListView)
    RecyclerView mRecyclerView;

    private int mFeedbackId;
   private OpinionpReplyAdapter mAdapter;

    public static void start(Context context, int feedbackId) {
        Bundle args = new Bundle();
        args.putInt("feedbackId",  feedbackId);
        OpenFragmentUtils.goToSimpleFragment(context, OpinionpReplyFragment.class.getName(), args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_opinion_reply, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(R.string.suggestion_feedback);
        Bundle bundle = getArguments();
        if(bundle!=null){
            mFeedbackId = bundle.getInt("feedbackId");
        }
//        UserFeedback userFeedback = new UserFeedback();
//        userFeedback.setFeedbackInfo("请问在哪里可以下载这样的图片啊？？");
//        userFeedback.setFeedbackPicture("http://miyuan-static.oss-cn-shenzhen.aliyuncs.com/zhitu-api/1553146859306.jpg");
//       userFeedback.setReplyContent("亲，长按图片可以保存哦～");
//       setData(userFeedback);
        mAdapter = new OpinionpReplyAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        getUserFeedback();

    }

    private void getUserFeedback() {
        RequestReplyBean requestReplyBean = new RequestReplyBean();
        requestReplyBean.setId(mFeedbackId);
        RxHttp.getInstance().getSysteService()
                .getUserFeedback(requestReplyBean)
                .compose(RxUtils.<BaseResponse<UserFeedback>>switchSchedulers())
                .compose(this.<BaseResponse<UserFeedback>>bindToLifecycle())
                .subscribe(new DataObserver<UserFeedback>() {
                    @Override
                    protected void onSuccess(UserFeedback data) {
                        setData(data);
                    }
                });
    }

    private void setData(UserFeedback data) {
        if(TextUtils.isEmpty(data.getReplyContent())){
            rl_reply.setVisibility(View.GONE);
        } else {
            tv_reply.setText(data.getReplyContent());
            rl_reply.setVisibility(View.VISIBLE);
        }

        LoadImgUtils.setImgHead(getActivity(),icon_head1, UserLocalData.getUser().getHeadImg());
//        LoadImgUtils.setImgHead(getActivity(),icon_head3, UserLocalData.getUser().getHeadImg());
        String[] temp = data.getFeedbackPicture().split(",");
        List<String> picturs = new ArrayList<>();
        if(picturs!=null){
            for(int i=0;i<temp.length;i++){
                if(!TextUtils.isEmpty(temp[i])){
                    picturs.add(temp[i]);
                }
            }
            mAdapter.replace(picturs);
            mAdapter.notifyDataSetChanged();
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }

        tv_content.setText(data.getFeedbackInfo());

    }


    public class OpinionpReplyAdapter extends SimpleAdapter<String, SimpleViewHolder> {
        public OpinionpReplyAdapter(Context context) {
            super(context);

        }

        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, final int position) {
            String url = getItem(position);
            RoundedImageView icon_head = holder.viewFinder().view(R.id.icon_head);
            ImageView img = holder.viewFinder().view(R.id.img);
            LoadImgUtils.setImgHead(getActivity(),icon_head, UserLocalData.getUser().getHeadImg());
            LoadImgUtils.setImg(getActivity(),img, url);
        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_opinion_reply_recycle, parent, false);
        }
    }
}
