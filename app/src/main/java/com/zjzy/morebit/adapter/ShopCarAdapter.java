package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.NumberGoodsDetailsActivity;
import com.zjzy.morebit.Module.common.Dialog.GoodsDeteleDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.RequestIscheckShopcarBean;
import com.zjzy.morebit.pojo.ShopCarGoodsBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.request.RequestAddShopcarBean;
import com.zjzy.morebit.pojo.request.RequestDeleteShopcarBean;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/*
 * 购物车adapter
 * */
public class ShopCarAdapter extends RecyclerView.Adapter<ShopCarAdapter.ViewHolder> {
    private Context mContext;
    private List<ShopCarGoodsBean.CartListBean> list = new ArrayList<>();
    private boolean isAllCheck = false;
    private boolean isCheckBox=false;


    public ShopCarAdapter(Context context) {
        this.mContext = context;

    }

    public void isAll(boolean check) {
        this.isAllCheck = check;
    }


    public void checkbox(boolean check){
        this.isCheckBox = check;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_shopcar_goods, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ShopCarGoodsBean.CartListBean listBean = list.get(position);
//        Object tag = holder.iv_shoppingaole_bg.getTag();
//        if(tag==null || !tag.equals(listBean.getPicUrl())){
//            holder.iv_shoppingaole_bg.setTag(listBean.getPicUrl());
//        }
        LoadImgUtils.loadingCornerBitmap(mContext, holder.iv_shoppingaole_bg, listBean.getPicUrl(), 5);

        if (!TextUtils.isEmpty(listBean.getGoodsName())) {
            holder.tv_title.setText(listBean.getGoodsName() + "");
        }

        if (!TextUtils.isEmpty(listBean.getPrice())) {
            holder.tv_price.setText(listBean.getPrice() + "");
        }

        if (!TextUtils.isEmpty(listBean.getNumber())) {
            holder.goodsRule_numTv.setText("" + listBean.getNumber());
        }
        List<String> specifications = listBean.getSpecifications();
        if (specifications != null && specifications.size() > 0) {
            holder.tv_guige.setText("规格：" + specifications.get(0));
        }
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listBean.isChecked()) {//已勾选
                   holder.checkbox.setSelected(false);
                    listBean.setChecked(false);
                    onItemAddClick.onCheckNum(position,false);
//                    getCheck(listBean.getProductId(),"0");

                }else{//未勾选
                   holder.checkbox.setSelected(true);
                    listBean.setChecked(true);
                    onItemAddClick.onCheckNum(position,true);
//                    getCheck(listBean.getProductId(),"0");

                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberGoodsDetailsActivity.start((Activity) mContext, listBean.getGoodsSn());
            }
        });


        holder.shopcar_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsDeteleDialog goodsDeteleDialog = new GoodsDeteleDialog(mContext);
                goodsDeteleDialog.show();
                goodsDeteleDialog.setmOkListener(new GoodsDeteleDialog.OnOkListener() {
                    @Override
                    public void onClick(View view) {
                        getDelete(listBean.getProductId(), position);
                    }
                });
            }
        });

        if (listBean.isOnSale()) {
            holder.tv_shi.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.checkbox.setVisibility(View.GONE);
            holder.tv_shi.setVisibility(View.VISIBLE);
        }

        if (isAllCheck) {
            holder.checkbox.setSelected(true);
        } else {
            holder.checkbox.setSelected(false);
        }


        holder.goodsRule_minusRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf((String) holder.goodsRule_numTv.getText());
                if (count == 1) {
                    ViewShowUtils.showShortToast(mContext, "不能再减了哦");
                } else {
                    count--;
                    holder.goodsRule_numTv.setText((count) + "");
                    listBean.setNumber(String.valueOf(count));
                    getUpdata(listBean.getGoodsId(), listBean.getProductId(), listBean.getNumber(), listBean.getId());
                   onItemAddClick.onCheckNum(position,listBean.isChecked());
                }
            }
        });


        holder.goodsRule_addRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count2 = Integer.valueOf((String) holder.goodsRule_numTv.getText());

                if (count2 + 1 > 99) {
                    ViewShowUtils.showShortToast(mContext, "不能再加了。没有库存了哦");
                } else {
                    count2++;
                    holder.goodsRule_numTv.setText((count2) + "");
                    listBean.setNumber(String.valueOf(count2));
                    getUpdata(listBean.getGoodsId(), listBean.getProductId(), listBean.getNumber(), listBean.getId());
                   onItemAddClick.onCheckNum(position,listBean.isChecked());
                }

            }
        });
        if(list.size()==position+1){
            holder.view.setVisibility(View.VISIBLE);
        }else{
            holder.view.setVisibility(View.GONE);
        }


    }




    private void getCheck(String productId, String isCheck) {
        checkShopCar((RxAppCompatActivity) mContext, productId, isCheck)
                .subscribe(new DataObserver<String>(false) {
                    @Override
                    protected void onDataListEmpty() {

                    }

                    @Override
                    protected void onDataNull() {


                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {

                    }

                    @Override
                    protected void onSuccess(String data) {

                    }
                });
    }

    private void getDelete(String productId, final int position) {
        deleteShopCar((RxAppCompatActivity) mContext, productId)
                .subscribe(new DataObserver<String>(false) {
                    @Override
                    protected void onDataListEmpty() {

                    }

                    @Override
                    protected void onDataNull() {
                        ToastUtils.showShort("删除成功");
                        list.remove(position);
                        notifyDataSetChanged();
                      //  onItemAddClick.onShareClick(position,list.get(position).isChecked());
                        onItemAddClick.onShareClick(position);

                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {

                    }

                    @Override
                    protected void onSuccess(String data) {

                    }
                });
    }

    private void getUpdata(String goodsId, String productId, String number, String cartid) {
        updataShopCar((RxAppCompatActivity) mContext, goodsId, productId, number, cartid)
                .subscribe(new DataObserver<String>(false) {
                    @Override
                    protected void onDataListEmpty() {

                    }

                    @Override
                    protected void onDataNull() {

                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {

                    }

                    @Override
                    protected void onSuccess(String data) {

                    }
                });
    }


    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public void addData(List<ShopCarGoodsBean.CartListBean> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void setData(List<ShopCarGoodsBean.CartListBean> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_shoppingaole_bg;
        private TextView tv_title, tv_guige, tv_price, goodsRule_numTv, tv_shi;
        private RelativeLayout rl_tou;
        private ImageView shopcar_delete;
        private View view;
        private RelativeLayout goodsRule_minusRelative, goodsRule_addRelative;
        private ImageView checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            shopcar_delete = itemView.findViewById(R.id.shopcar_delete);//删除商品
            view = itemView.findViewById(R.id.view);
            iv_shoppingaole_bg = itemView.findViewById(R.id.iv_shoppingaole_bg);//主图
            tv_title = itemView.findViewById(R.id.tv_title);//标题
            tv_guige = itemView.findViewById(R.id.tv_guige);//规格
            tv_price = itemView.findViewById(R.id.tv_price);//价格
            goodsRule_minusRelative = itemView.findViewById(R.id.goodsRule_minusRelative);//减
            goodsRule_numTv = itemView.findViewById(R.id.goodsRule_numTv);//数量
            goodsRule_addRelative = itemView.findViewById(R.id.goodsRule_addRelative);//加
            checkbox = itemView.findViewById(R.id.checkbox);
            tv_shi = itemView.findViewById(R.id.tv_shi);

        }
    }


    public static interface OnAddClickListener {

        public void onShareClick(int position);
        public void onCheckNum(int position,boolean ischeck);
      //  public void onCheckNum2(int position);

    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }

    //添加购物车
    public Observable<BaseResponse<String>> updataShopCar(RxAppCompatActivity fragment, String goodsId, String productId, String number, String cartid) {
        RequestAddShopcarBean requestAddShopcarBean = new RequestAddShopcarBean();
        requestAddShopcarBean.setGoodsId(goodsId);
        requestAddShopcarBean.setProductId(productId);
        requestAddShopcarBean.setNumber(number);
        requestAddShopcarBean.setCartId(cartid);
        return RxHttp.getInstance().getSysteService().getShopCarUpdate(requestAddShopcarBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(fragment.<BaseResponse<String>>bindToLifecycle());
    }


    //删除购物车
    public Observable<BaseResponse<String>> deleteShopCar(RxAppCompatActivity fragment, String productId) {
        RequestDeleteShopcarBean deleteShopcarBean = new RequestDeleteShopcarBean();
        List<String> productIds = new ArrayList<>();
        productIds.add(productId);
        deleteShopcarBean.setProductIds(productIds);
        return RxHttp.getInstance().getSysteService().getShopCarDelete(deleteShopcarBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(fragment.<BaseResponse<String>>bindToLifecycle());
    }

    //勾选购物车
    public Observable<BaseResponse<String>> checkShopCar(RxAppCompatActivity fragment, String productId, String isCheck) {
        RequestIscheckShopcarBean ischeckShopcarBean = new RequestIscheckShopcarBean();
        List<String> productIds = new ArrayList<>();
        productIds.add(productId);
        ischeckShopcarBean.setProductIds(productIds);
        ischeckShopcarBean.setIsChecked(isCheck);
        return RxHttp.getInstance().getSysteService().getShopCarChecked(ischeckShopcarBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(fragment.<BaseResponse<String>>bindToLifecycle());
    }
}
