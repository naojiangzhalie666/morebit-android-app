package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.SearchArticleListActitivty;
import com.zjzy.morebit.circle.ui.VideoPlayerActivity;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.StudyRank;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.LoadImgUtils;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.StringUtils.getString;

/*
 * 技能课堂adapter
 * */
public class SkillAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Article> list = new ArrayList<>();
    private String sfinalShare, sfinalLike;
    private int type = 1001;
    private List<StudyRank> mdata=new ArrayList<>();

    public SkillAdapter(Context context) {
        this.context = context;

    }

    public void setData2(List<StudyRank> data) {
        if (data != null) {
            mdata.clear();
            mdata.addAll(data);
            notifyDataSetChanged();
        }
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == type) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.item_vip_skill_head, parent, false);
            return new ViewHolder2(inflate);
        }else{
            View inflate = LayoutInflater.from(context).inflate(R.layout.itme_skill, parent, false);
            ViewHolder holder = new ViewHolder(inflate);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == type) {
            ViewHolder2 holder2 = (ViewHolder2) holder;
            holder2.search_rl.setOnClickListener(new View.OnClickListener() {//进入商学院搜索
                @Override
                public void onClick(View v) {
                    SearchArticleListActitivty.start(context);
                }
            });

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            if ( holder2.rcy_title.getItemDecorationCount() == 0) {//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
                holder2.rcy_title.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(context, 6)));
            }
            holder2.rcy_title.setLayoutManager(gridLayoutManager);
            StudyTitleAdapter  titleAdapter = new StudyTitleAdapter(context);
            titleAdapter.setData(mdata);
            holder2.rcy_title.setAdapter(titleAdapter);
        }else{
            ViewHolder holder1 = (ViewHolder) holder;
            final Article article = list.get(position-1);
            if (!TextUtils.isEmpty(article.getImage())) {
                LoadImgUtils.loadingCornerBitmap(context, holder1.img, article.getImage(), 8);
            }
            holder1.tv_title.setText(article.getTitle());
            if (!TextUtils.isEmpty(article.getReleaseTime())) {
                holder1.tv_time.setText(DateTimeUtils.getShortTime2(article.getReleaseTime()) + "");
            } else {
                holder1.tv_time.setText("");
            }
            if (article.getType() == 2) {
                holder1.iv_video.setVisibility(View.VISIBLE);
            } else {
                holder1.iv_video.setVisibility(View.GONE);
            }
            int finalShare = article.getVirtualShare() + article.getShareNum();
            int finalLike = article.getPraiseNum() + article.getVirtualPraise();
            if (finalShare > 999) {
                sfinalShare = 999 + "+";
            } else {
                sfinalShare = finalShare + "";
            }

            if (finalLike > 999) {
                sfinalLike = 999 + "+";
            } else {
                sfinalLike = finalLike + "";
            }

            holder1.tv_dian.setText(sfinalShare + "分享·" + sfinalLike + "点赞");

            if (position == list.size() - 1) {
                holder1.line.setVisibility(View.GONE);
            } else {
                holder1.line.setVisibility(View.VISIBLE);
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
    }


    @Override
    public int getItemCount() {

        return list.size()+1;


    }

    @Override
    public int getItemViewType(int position) {
        //在第一个位置添加头
        if (position==0){
            return type;
        }
        return super.getItemViewType(position);
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


    public class ViewHolder2 extends RecyclerView.ViewHolder {


        private RelativeLayout search_rl;
        private RecyclerView rcy_title;

        public ViewHolder2(View itemView) {
            super(itemView);
              search_rl = itemView.findViewById(R.id.search_rl);
            //头部title列表
            rcy_title = itemView.findViewById(R.id.rcy_title);

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
