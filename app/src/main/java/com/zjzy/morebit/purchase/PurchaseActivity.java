package com.zjzy.morebit.purchase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.CircleDayHotFragment;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.purchase.adapter.ProductAdapter;
import com.zjzy.morebit.purchase.adapter.PurchseAdapter;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.view.CommercialShareDialog;
import com.zjzy.morebit.view.FixRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.zjzy.morebit.utils.C.requestType.initData;

/*
*
* 0元购
* */
public class PurchaseActivity extends BaseActivity implements View.OnClickListener {


    private TextView txt_head_title,tv_rule;
    private RecyclerView rl_list,rcy_product;
    private PurchseAdapter adapter;
    private ProductAdapter padapter;
    private ImageView share;
    CommercialShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        initView();
        initData();
    }

    private void initData() {
        txt_head_title.setText("新人0元购");

        
    }

    private void initView() {
        txt_head_title= (TextView) findViewById(R.id.txt_head_title);
        rl_list= (FixRecyclerView) findViewById(R.id.rl_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter=new PurchseAdapter(this);
        rl_list.setLayoutManager(manager);
        rl_list.setAdapter(adapter);

        rcy_product= (FixRecyclerView) findViewById(R.id.rcy_product);
        padapter=new ProductAdapter(this);
        rcy_product.setLayoutManager(manager);
        rcy_product.setAdapter(padapter);


        share = (ImageView) findViewById(R.id.share);//分享
        share.setOnClickListener(this);

        tv_rule= (TextView) findViewById(R.id.tv_rule);//活动规则
        tv_rule.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share:
                shareDialog = new CommercialShareDialog(this, new View.OnClickListener() {//分享
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.weixinFriend: //分享到好友
                               // mshare(item, type, ShareUtil.WechatType);
                                break;
                            case R.id.weixinCircle: //分享到朋友圈
                              //  mshare(item, type, ShareUtil.WeMomentsType);
                                break;
                            case R.id.qqFriend: //分享到QQ
                               // mshare(item, type, ShareUtil.QQType);
                                break;
                            case R.id.qqRoom: //分享到QQ空间
                               // mshare(item, type, ShareUtil.QQZoneType);
                                break;
                            case R.id.sinaWeibo: //分享到新浪微博
                               // mshare(item, type, ShareUtil.WeiboType);
                                break;
                            default:
                                break;

                        }

                        shareDialog.dismiss();
                    }
                });

                if (!shareDialog.isShowing()) {
                    shareDialog.show();
                }
                break;
            case R.id.tv_rule:
                break;
        }
    }

    private void mshare(final ShopGoodInfo item, int type, int sharePlatform) {
        switch (type) {
            case CircleDayHotFragment.TypeCommodityImg:
                List<String> pictures = new ArrayList<>();
                if (!TextUtils.isEmpty(item.getPicture())) {
                    pictures.add(item.getPicture());
                }
         //       shareImg(mDatas, pictures, sharePlatform);
//                shareImg(mDatas, pictures);
                break;
            case CircleDayHotFragment.TypeCommodity:
                List<ShopGoodInfo> osgData = new ArrayList<>();
                osgData.add(item);
//                shareGoods(mDatas, osgData);
               // shareGoods(mDatas, osgData, sharePlatform);
                break;
        }
    }
}
