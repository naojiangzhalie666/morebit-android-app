package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.PurchaseDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.VideoPlayerActivity;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.purchase.PurchaseActivity;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.UploadingOnclickUtils;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.StringUtils.getString;

/*
 * 技能课堂adapter
 * */
public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.ViewHolder> {
    private Context context;
    private List<Article> list = new ArrayList<>();
    private String sfinalShare,sfinalLike;

    public SkillAdapter(Context context) {
        this.context = context;

    }

    public void setData(List<Article> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
           notifyDataSetChanged();
        }
    }
    public void addData(List<Article> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
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
            LoadImgUtils.loadingCornerBitmap(context, holder.img, article.getImage(), 8);
        }
        holder.tv_title.setText(article.getTitle());
        if (!TextUtils.isEmpty(article.getReleaseTime())) {
            holder.tv_time.setText(DateTimeUtils.getShortTime2(article.getReleaseTime()) + "");
        } else {
            holder.tv_time.setText("");
        }
        if (article.getType() == 2) {
            holder.iv_video.setVisibility(View.VISIBLE);
        } else {
            holder.iv_video.setVisibility(View.GONE);
        }
        int finalShare = article.getVirtualShare() + article.getShareNum();
        int finalLike = article.getPraiseNum() + article.getVirtualPraise();
        if (finalShare > 999) {
            sfinalShare = 999 + "+";
        }else{
            sfinalShare=finalShare+"";
        }

        if (finalLike > 999) {
            sfinalLike = 999 + "+";
        }else{
            sfinalLike=finalLike+"";
        }

        holder.tv_dian.setText(sfinalShare + "分享·" + sfinalLike +"点赞");

        if (position == list.size() - 1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
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
                } else if (article.getType() == 2) {
                    VideoPlayerActivity.start(context, article);
                } else if (article.getType() == 3) {
                    ShowWebActivity.start((Activity) context, article.getLinkUrl(), article.getTitle());
                }
            }
        });
    }


    @Override
    public int getItemCount() {

        return list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img, iv_video;
        private TextView tv_title, tv_time, tv_dian;
        private View line;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);//主图
            tv_title = itemView.findViewById(R.id.tv_title);//标题
            tv_time = itemView.findViewById(R.id.tv_time);//时间
            tv_dian = itemView.findViewById(R.id.tv_dian);//分享数
            iv_video = itemView.findViewById(R.id.iv_video);//视频
            line = itemView.findViewById(R.id.line);
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
