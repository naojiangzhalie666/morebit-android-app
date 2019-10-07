package com.markermall.cat.circle.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markermall.cat.Activity.ShowWebActivity;
import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.Module.common.Dialog.HintDialog;
import com.markermall.cat.R;
import com.markermall.cat.adapter.SimpleAdapter;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.circle.model.CircleModel;
import com.markermall.cat.circle.ui.VideoPlayerActivity;
import com.markermall.cat.pojo.Article;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.DateTimeUtils;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.LoginUtil;
import com.markermall.cat.utils.MyLog;
import com.markermall.cat.utils.UploadingOnclickUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class ArticleAdapter extends SimpleAdapter<Article, SimpleViewHolder> {
    public static  final  int TYPE_HOTIZONTAL =0;
    public static  final  int TYPE_VERTICAL =1;
    public static  final  int TYPE_RECOMMEND =2;
    private Context mContext;
    private HintDialog mDialog;
    CircleModel mCircleModel;
    //    MyPlatformActionListener mMyPlatformActionListener;
    private boolean isHomeRecommend;
    private RxAppCompatActivity mRxAppCompatActivity;
    private RxFragment mRxFragment;
    private int mType=0;
    private boolean isStudyView;  //已学Textview是否变色
    public ArticleAdapter(Context context, RxAppCompatActivity rxAppCompatActivity) {
        super(context);
        this.mContext = context;
        mRxAppCompatActivity = rxAppCompatActivity;
    }

    public ArticleAdapter(Context context, RxFragment rxFragment) {
        super(context);
        this.mContext = context;
        mRxFragment = rxFragment;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mType==TYPE_HOTIZONTAL){
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutorial_course_horizontal, parent, false));
        } else if(mType == TYPE_RECOMMEND){
            return new SimpleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutorial_course_recommend_horizontal, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutorial_course_vertical, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder simpleViewHolder, final int position) {
        final Article article = getItem(position);
        if (article == null) return;
        if(simpleViewHolder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) simpleViewHolder;
            //通用都有
            if (!TextUtils.isEmpty(article.getImage())) {
                LoadImgUtils.setImg(mContext, viewHolder.ivShow, article.getImage());
            }
            if (!TextUtils.isEmpty(article.getReleaseTime())) {
                viewHolder.tvTime.setText(DateTimeUtils.ymdhmsToymd(article.getReleaseTime()) + "");
            } else {
                viewHolder.tvTime.setText("");
            }
            if(article.getType()==2){
                viewHolder.iv_video.setVisibility(View.VISIBLE);
            } else {
                viewHolder.iv_video.setVisibility(View.GONE);
            }
            viewHolder.title.setText(article.getTitle());

            int finalStudyNum = article.getStudyNum() + article.getVirtualStudy();
            if(isStudyView){
                viewHolder.tvCount.setTextColor(ContextCompat.getColor(mContext,R.color.colcor_FFA61F));
                if(position<3){
                    viewHolder. iv_recommend.setVisibility(View.VISIBLE);
                } else {
                    viewHolder. iv_recommend.setVisibility(View.GONE);
                }
            } else {
                viewHolder.tvCount.setTextColor(ContextCompat.getColor(mContext,R.color.colcor_999999));
            }

            viewHolder.tvCount.setText(getString(R.string.college_article_already_study, finalStudyNum));

            int finalShare = article.getVirtualShare() + article.getShareNum();
            viewHolder.tv_share_sum.setText(finalShare+"");
            int finalLike = article.getPraiseNum() + article.getVirtualPraise();
            viewHolder.tv_like_sum.setText(finalLike+"");
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    SensorsDataUtil.getInstance().mifenClickTrack("","","商学院",position,"","","","",article.getUrl(),article.getTitle(),"");
                    if (LoginUtil.checkIsLogin((Activity) mContext)) {
                        sendStatistics(article, viewHolder.tvCount,UploadingOnclickUtils.TYPE_BROWS_ONCLICK);
                        if (article.getType() == 1) {
                            //文章类型
                            if (article != null && article.getAuthority() == 1) {
                                if (!TextUtils.isEmpty(article.getUrl())) {
                                    ShowWebActivity.start((Activity) mContext, article.getUrl() + "?id=" + article.getId() + "&status=" + article.getStatus(), article.getTitle(), article);
                                }
                            } else {
                                openDialog();
                            }
                        }  else if(article.getType()==2){
                            VideoPlayerActivity.start(mContext,article);
                        }else if (article.getType() == 3) {
                            ShowWebActivity.start((Activity) mContext, article.getLinkUrl(), article.getTitle());
                        }
                    }
                }
            });
        } else {  //推荐
            ImageView img = simpleViewHolder.viewFinder().view(R.id.img);
            TextView tv_time = simpleViewHolder.viewFinder().view(R.id.tv_time);
            TextView tv_title = simpleViewHolder.viewFinder().view(R.id.tv_title);
            ImageView iv_video = simpleViewHolder.viewFinder().view(R.id.iv_video);
            ImageView iv_recommend = simpleViewHolder.viewFinder().view(R.id.iv_recommend);
            //通用都有
            if (!TextUtils.isEmpty(article.getImage())) {
                LoadImgUtils.setImg(mContext, img, article.getImage());
            }
            if (!TextUtils.isEmpty(article.getReleaseTime())) {
                if(TYPE_RECOMMEND == mType){
                    tv_time.setText(article.getReleaseTime());
                } else {
                    tv_time.setText(DateTimeUtils.ymdhmsToymd(article.getReleaseTime()));
                }

            } else {
                tv_time.setText("");
            }
            if(position<3){
                iv_recommend.setVisibility(View.VISIBLE);
            } else {
                iv_recommend.setVisibility(View.GONE);
            }
            if(article.getType()==2){
                iv_video.setVisibility(View.VISIBLE);
            } else {
                iv_video.setVisibility(View.GONE);
            }
            tv_title.setText(article.getTitle());
            simpleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    SensorsDataUtil.getInstance().mifenClickTrack("","","商学院",position,"","","","",article.getUrl(),article.getTitle(),"");
                    if (LoginUtil.checkIsLogin((Activity) mContext)) {
                        sendStatistics(article, null,UploadingOnclickUtils.TYPE_BROWS_ONCLICK);
                        if (article.getType() == 1) {
                            //文章类型
                            if (article != null && article.getAuthority() == 1) {
                                if (!TextUtils.isEmpty(article.getUrl())) {
                                    ShowWebActivity.start((Activity) mContext, article.getUrl() + "?id=" + article.getId() + "&status=" + article.getStatus(), article.getTitle(), article);
                                }
                            } else {
                                openDialog();
                            }
                        }  else if(article.getType()==2){
                            VideoPlayerActivity.start(mContext,article);
                        } else if (article.getType() == 3) {
                            ShowWebActivity.start((Activity) mContext, article.getLinkUrl(), article.getTitle());
                        }
                    }
                }
            });
        }




    }




    private class ViewHolder extends SimpleViewHolder {

        ImageView ivShow;
        ImageView  iv_video;
        //        LinearLayout ll_share;
        TextView tvTime;
        TextView title;
        TextView tv_like_sum;
        TextView tv_share_sum;
        TextView tvCount;
        ImageView iv_recommend;

        public ViewHolder(View itemView) {
            super(itemView);
            ivShow = itemView.findViewById(R.id.img);
//            ll_share = itemView.findViewById(R.id.ll_share);
            tvTime = itemView.findViewById(R.id.tv_time);
            title = itemView.findViewById(R.id.tv_title);
            tvCount = itemView.findViewById(R.id.tv_study_num);
            tv_share_sum = itemView.findViewById(R.id.tv_share_sum);
            tv_like_sum = itemView.findViewById(R.id.tv_like_sum);
            iv_video = itemView.findViewById(R.id.iv_video);
            iv_recommend = itemView.findViewById(R.id.iv_recommend);
//            iv_recommend = itemView.findViewById(R.id.iv_recommend);
//            mCardViewArticle = itemView.findViewById(R.id.card_view_article);
//            ll_count_container = itemView.findViewById(R.id.ll_count_container);
        }
    }

    private void openDialog() {
        if (mDialog == null) {
            // 0是消费者，1是代理商，2是运营专员
            String toastMsg = getHintString();
            mDialog = new HintDialog(mContext, R.style.dialog, "提示", toastMsg);
        }
        String toastMsg = getHintString();
        mDialog.setContentText(toastMsg);
        mDialog.show();
    }

    @NonNull
    private String getHintString() {
        String toastMsg = "";
        if (C.UserType.member.equals(UserLocalData.getUser(mContext).getPartner())) {
            toastMsg = mContext.getString(R.string.dialog_article_vip_hint);
        } else if (C.UserType.vipMember.equals(UserLocalData.getUser(mContext).getPartner())) {
            toastMsg = mContext.getString(R.string.dialog_article_operator_hint);
        } else {
            toastMsg = mContext.getString(R.string.dialog_article_drm_hint); // 默认提示
        }
        return toastMsg;
    }

    private void sendStatistics(Article article, final TextView textView,int type) {
        MyLog.i("test", "text: " + textView);
        if (mCircleModel == null) {
            mCircleModel = new CircleModel();
        }
        if(textView!=null){
            try {
                Integer setCount = Integer.parseInt(textView.getText().toString());
                textView.setText(getString(R.string.college_article_already_study, setCount++));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        UploadingOnclickUtils.mp4Browse(article.getId(),type);
    }

    public void setType(int type) {
        mType = type;
    }

    public void setStudyView(boolean studyView) {
        isStudyView = studyView;
    }

    public void setHomeRecommend(boolean homeRecommend) {
        isHomeRecommend = homeRecommend;
    }
}
