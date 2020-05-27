package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.VideoPlayerActivity;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.LoadImgUtils;

import java.util.List;

import static com.blankj.utilcode.util.StringUtils.getString;

/*
 * 技能课堂adapter
 * */
public class SkillClassAdapter extends RecyclerView.Adapter<SkillClassAdapter.ViewHolder> {
    private Context context;
    private List<Article> list;


    public SkillClassAdapter(Context context, List<Article> data) {
        this.context = context;
        this.list = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_skill, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Article article = list.get(position);
        if (!TextUtils.isEmpty(article.getImage())) {
            LoadImgUtils.loadingCornerBitmap(context, holder.img, article.getImage(),8);
        }
        holder.tv_title.setText(article.getTitle());
        int finalStudyNum = article.getStudyNum() + article.getVirtualStudy();
        holder.tv_study_num.setText(getString(R.string.college_article_already_study, finalStudyNum));
        if (!TextUtils.isEmpty(article.getReleaseTime())) {
            holder.tv_time.setText("日期："+ DateTimeUtils.ymdhmsToymd(article.getReleaseTime()) + "");
        } else {
            holder.tv_time.setText("");
        }
        if(article.getType()==2){
            holder.iv_video.setVisibility(View.VISIBLE);
        } else {
            holder.iv_video.setVisibility(View.GONE);
        }
        int finalShare = article.getVirtualShare() + article.getShareNum();
        holder.tv_share_sum.setText(finalShare+"");
        int finalLike = article.getPraiseNum() + article.getVirtualPraise();
        holder.tv_like_sum.setText(finalLike+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (article.getType() == 1) {
                        //文章类型
                        if (article != null && article.getAuthority() == 1) {
                            if (!TextUtils.isEmpty(article.getUrl())) {
                                ShowWebActivity.start((Activity) context, article.getUrl() + "?id=" + article.getId() + "&status=" + article.getStatus(), article.getTitle(), article);
                            }
                        }
                    }  else if(article.getType()==2){
                        VideoPlayerActivity.start(context,article);
                    }else if (article.getType() == 3) {
                        ShowWebActivity.start((Activity) context, article.getLinkUrl(), article.getTitle());
                    }
                }
        });
    }


    @Override
    public int getItemCount() {

        return list.size();


    }

    public void setData(List<Article> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0,data.size());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img,iv_video;
        private TextView tv_title,tv_study_num,tv_time,tv_share_sum,tv_like_sum;

        public ViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);//主图
            tv_title=itemView.findViewById(R.id.tv_title);//标题
            tv_study_num=itemView.findViewById(R.id.tv_study_num);//人数
            tv_time=itemView.findViewById(R.id.tv_time);//时间
            tv_share_sum=itemView.findViewById(R.id.tv_share_sum);//分享数
            tv_like_sum=itemView.findViewById(R.id.tv_like_sum);//收藏数
            iv_video=itemView.findViewById(R.id.iv_video);//视频
        }
    }

    public static interface OnAddClickListener {

        public void onShareClick();
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
}
