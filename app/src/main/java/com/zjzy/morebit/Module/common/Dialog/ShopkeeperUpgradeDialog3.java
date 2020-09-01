package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.GoodsUtil;

/*
*
* 升级成功弹框
* */
public class ShopkeeperUpgradeDialog3 extends Dialog {
    private TextView btn_ok,title,tv2,tv3;
    private Context mContext;
    private ImageView img;
    private int type=1;
    public ShopkeeperUpgradeDialog3(Context context,int type) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.type=type;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upgrade_shopkeeper3);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView() {
        img=findViewById(R.id.img);
        tv3=findViewById(R.id.tv3);
        tv2=findViewById(R.id.tv2);
        title=findViewById(R.id.title);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsUtil.getVipH5(mContext);
                    dismiss();

            }
        });


        if (type==1){
            img.setImageResource(R.mipmap.vip_bg_icon);
            title.setText("恭喜您升级成功");
            tv2.setText("将获得掌柜");
            tv3.setText("尊享权益");
        }else{
            img.setImageResource(R.mipmap.group_bg_icon);
            title.setText("领取成功");
            tv2.setText("您将尊享掌柜");
            tv3.setText("权益");
        }




    }




}
