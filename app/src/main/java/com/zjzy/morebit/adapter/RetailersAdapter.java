package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.GoodsDetailForJdActivity;
import com.zjzy.morebit.Activity.GoodsDetailForKoalaActivity;
import com.zjzy.morebit.Activity.GoodsDetailForPddActivity;
import com.zjzy.morebit.Activity.GoodsDetailForWphActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.VideoPlayerActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.CircleCopyBean;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.request.RequestCircleShareBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestItemSourceId;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/*
 * 订单列表
 * */
public class RetailersAdapter extends RecyclerView.Adapter<RetailersAdapter.ViewHolder> {
    private Context context;
    private List<ConsComGoodsInfo> list = new ArrayList<>();
    private int ordertype;
    private   ZLoadingDialog dialog;

    public RetailersAdapter(Context context, int order_type) {
        this.context = context;
        this.ordertype = order_type;

    }

    public void setData(List<ConsComGoodsInfo> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }


    public void addData(List<ConsComGoodsInfo> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_retailers, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ConsComGoodsInfo info = list.get(position);

        if (!TextUtils.isEmpty(info.getItemImg())) {
            LoadImgUtils.loadingCornerBitmap(context, holder.img_zhu, info.getItemImg());
        }
        if (!TextUtils.isEmpty(info.getItemName())) {
            holder.tv_title.setText(info.getItemName());
            holder.tv_title.getPaint().setFakeBoldText(true);
        }
        final int orderType = info.getType();
        switch (orderType) {
            case 1:
                holder.img_type.setBackgroundResource(R.drawable.tb_list_icon);
                //holder.tv_type.setText("淘宝");
                break;
            case 2:
                holder.img_type.setBackgroundResource(R.mipmap.jd_list_icon);
                //  holder.tv_type.setText("京东");
                break;
            case 4:
                holder.img_type.setBackgroundResource(R.mipmap.pdd_list_icon);
                //  holder.tv_type.setText("拼多多");
                break;
            case 5:
                holder.img_type.setBackgroundResource(R.mipmap.kl_order_icon);
                //  holder.tv_type.setText("考拉");
                break;
            case 6:
                holder.img_type.setBackgroundResource(R.mipmap.wph_order_icon);
                // holder.tv_type.setText("唯品会");
                break;
        }
        if (!TextUtils.isEmpty(info.getCreateTime())) {
            holder.tv_time.setText("下单时间：" + info.getCreateTime());
        }

        if (!TextUtils.isEmpty(info.getPaymentAmount())) {
            holder.tv_fu.setText(info.getPaymentAmount());
        }
        if (!TextUtils.isEmpty(info.getOrderSn())) {
            holder.tv_order.setText("订单号：" + info.getOrderSn());
            holder.tv_order.getPaint().setFakeBoldText(true);
        }

        if (!TextUtils.isEmpty(info.getCommission())) {
            holder.tv_fanyong.setText("赚 ¥" + info.getCommission());
        }

        int status = Integer.parseInt(info.getStatus());
        if (status == 4) {
            holder.tv_fanyong.setBackgroundResource(R.drawable.background_fffff_radius_2dp);
            holder.tv_fanyong.setTextColor(Color.parseColor("#999999"));
            holder.tv_content.setText("已失效");
            holder.tv_content.setTextColor(Color.parseColor("#999999"));
        } else if (status == 5) {
            holder.tv_fanyong.setBackgroundResource(R.drawable.background_ffebce_radius_2dp);
            holder.tv_fanyong.setTextColor(Color.parseColor("#975400"));
            holder.tv_content.setText("维权订单");
            holder.tv_content.setTextColor(Color.parseColor("#F05557"));
        } else if (status == 3) {
            holder.tv_fanyong.setBackgroundResource(R.drawable.background_ffebce_radius_2dp);
            holder.tv_fanyong.setTextColor(Color.parseColor("#975400"));
            holder.tv_content.setText("已到账");
            holder.tv_content.setTextColor(Color.parseColor("#F05557"));
        } else {
            holder.tv_fanyong.setBackgroundResource(R.drawable.background_ffebce_radius_2dp);
            holder.tv_fanyong.setTextColor(Color.parseColor("#975400"));
            holder.tv_content.setText("待返佣");
            holder.tv_content.setTextColor(Color.parseColor("#F05557"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AppUtil.coayText((Activity) context, info.getOrderSn());
//                ViewShowUtils.showShortToast(context, "已复制订单号，点击粘贴文案");
                switch (orderType) {
                    case 1://淘宝
                        loading();
                        getTao(info.getItemId());
                      //  getTao3(orderType, 1, info);

                        break;
                    case 2://京东
                        loading();
                        getTao3(orderType, 4, info);
                        break;

                    case 4://jd
                        loading();
                        getTao3(orderType, 3, info);
                        break;
                    case 5://考拉
                        getTao3(orderType, 5, info);
                        break;
                    case 6://唯品会
                        getTao3(orderType, 6, info);
                        break;

                }




            }
        });

        holder.img_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsUtil.getOrdeRule(context);
            }
        });

        if (list.size() != 0 && list.size() == position + 1) {
            holder.ll_bottom.setVisibility(View.VISIBLE);
        } else {
            holder.ll_bottom.setVisibility(View.GONE);
        }

    }
    private void loading(){
        dialog = new ZLoadingDialog(context);
        dialog.setLoadingBuilder(Z_TYPE.SINGLE_CIRCLE)//设置类型
                .setLoadingColor(Color.parseColor("#F05557"))//颜色
                .setHintText("Loading...")
                .show();
    }

    private void getTao(String itemId) {
        getBaseResponseObservable((BaseActivity) context, itemId)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new DataObserver<ShopGoodInfo>() {
                    @Override
                    protected void onSuccess(final ShopGoodInfo data) {
                      if (data!=null){
                        if (TextUtils.isEmpty(data.getItemPrice())||TextUtils.isEmpty(data.getItemVoucherPrice())){
                            ToastUtils.showShort("商品已下架");
                        }else{
                            GoodsDetailActivity.start(context, data);
                        }
                          dialog.dismiss();
                      }else{
                          ToastUtils.showShort("商品已下架");
                          dialog.dismiss();
                      }

                    }
                });
    }


    private void getTao3(final int i, final int shopType, final ConsComGoodsInfo info) {

        final RequestCircleShareBean bean = new RequestCircleShareBean();
        bean.setItemId(info.getItemId());
        bean.setType(shopType);
        RxHttp.getInstance().getGoodsService()
                .getGoodsPurchaseLink(bean)
                .compose(RxUtils.<BaseResponse<CircleCopyBean>>switchSchedulers())

                .subscribe(new DataObserver<CircleCopyBean>() {
                    @Override
                    protected void onSuccess(CircleCopyBean data) {

                        switch (i) {
                            case 1://淘宝
//                                GoodsUtil.checkGoods((RxAppCompatActivity) context, info.getItemId(), new MyAction.One<ShopGoodInfo>() {
//                                    @Override
//                                    public void invoke(ShopGoodInfo arg) {
//                                        MyLog.i("test", "arg: " + arg);
//
//
//                                    }
//                                });
                                break;
                            case 2://京东
                                ShopGoodInfo goodInfo2 = new ShopGoodInfo();
                                goodInfo2.setItemSource("1");
                                goodInfo2.setItemSourceId(info.getItemId());
                                goodInfo2.setGoodsId(Long.valueOf(info.getItemId()));
                                GoodsDetailForJdActivity.start(context, goodInfo2);
                                break;
                            case 4://jd
                                ShopGoodInfo goodInfo = new ShopGoodInfo();
                                goodInfo.setItemSource("2");
                                goodInfo.setItemSourceId(info.getItemId());
                                goodInfo.setGoodsId(Long.valueOf(info.getItemId()));
                                GoodsDetailForPddActivity.start(context, goodInfo);
                                break;
                            case 5://考拉
                                GoodsDetailForKoalaActivity.start(context, info.getItemId());
                                break;
                            case 6://唯品会
                                GoodsDetailForWphActivity.start(context, info.getItemId());
                                break;

                        }

                        dialog.dismiss();
                    }
                });
    }


    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_zhu, img_question, img_type;
        private TextView tv_fanyong, tv_fu, tv_order, tv_time, tv_content, tv_title;
        private LinearLayout ll_bottom;

        public ViewHolder(View itemView) {
            super(itemView);
            img_zhu = itemView.findViewById(R.id.img_zhu);//主图
            tv_fanyong = itemView.findViewById(R.id.tv_fanyong);//返佣
            tv_fu = itemView.findViewById(R.id.tv_fu);//付款金额
            tv_order = itemView.findViewById(R.id.tv_order);//订单号
            img_question = itemView.findViewById(R.id.img_question);//常见问题
            tv_time = itemView.findViewById(R.id.tv_time);//下单时间
            img_type = itemView.findViewById(R.id.img_type);//平台类型
            tv_content = itemView.findViewById(R.id.tv_content);//内容
            tv_title = itemView.findViewById(R.id.tv_title);//标题
            ll_bottom = itemView.findViewById(R.id.ll_bottom);
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


    //淘宝详情
    private Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservable(BaseActivity rxActivity, String itemId) {
        return RxHttp.getInstance().getCommonService().getDetailData(new RequestItemSourceId().setItemSourceId(itemId).setItemFrom("1"))
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }
}
