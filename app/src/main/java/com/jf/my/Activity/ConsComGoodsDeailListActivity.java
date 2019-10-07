package com.jf.my.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.R;
import com.jf.my.fragment.AppFeedBackFragment;
import com.jf.my.fragment.GoodsFeedBackFragment;
import com.jf.my.utils.ActivityStyleUtil;
import com.jf.my.utils.OpenFragmentUtils;


/**
 * Created by fengrs on 2018/11/24 0011.
 * 官方推荐
 */

public class ConsComGoodsDeailListActivity extends BaseActivity implements View.OnClickListener{
    private TextView txt_head_title;
    private View status_bar;
    private Fragment fragment;
    private LinearLayout btn_back;
    private Bundle bundle;
    private String title="";
    private String fragmentName = "";
    private RelativeLayout rel_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBundle();
        setContentView(R.layout.activity_onefragmentdefault);
        inview();
    }

    @Override
    public boolean isSwipe() {
        return false;
    }

    public void onResume() {
        super.onResume();
    }
    public void onPause() {
        super.onPause();
    }

    private void initBundle(){
        bundle = getIntent().getExtras();
        if(bundle!=null){
            title = bundle.getString("title","");
            fragmentName = bundle.getString("fragmentName","");
        }
    }

   public  void inview(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
       }else{
           ActivityStyleUtil.initSystemBar(this, R.color.color_757575); //设置标题栏颜色值
       }
       txt_head_title=(TextView)findViewById(R.id.txt_head_title);
       txt_head_title.setText(title);
       btn_back = (LinearLayout) findViewById(R.id.btn_back);
       btn_back.setOnClickListener(this);
       btn_back.setVisibility(View.VISIBLE);
       rel_top = (RelativeLayout)findViewById(R.id.rel_top);
       showFragment(0);
   }

    /**
     * 设置标题
     * @param text
     */
   public void setTitleText(String text){
       txt_head_title.setText(text);
   }

    /**
     * 设置标题栏的背景颜色
     * @param color
     */
   public void setTitleBarColor(int color){
       rel_top.setBackgroundResource(color);
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    int currentFlagment = 0;
    public void showFragment(int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // 想要显示一个fragment,先隐藏所有fragment，防止重叠
        hideFragments(ft);
        switch (index) {
            case 0:
                // 如果fragment1已经存在则将其显示出来
                if (fragment != null) {
                    ft.show(fragment);
                    // 否则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
                }else {
                    fragment = OpenFragmentUtils.getFragment(fragmentName);
                    fragment.setArguments(bundle);
                    ft.add(R.id.con_page, fragment,"fragment");
                }
                break;
        }
        ft.commit();
        currentFlagment = index;
    }


    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (fragment != null)
            ft.hide(fragment);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ("GoodsFeedBackFragment".equals(fragmentName)) {
                ((GoodsFeedBackFragment) fragment).onActivityResult(requestCode, resultCode, data);
            }else if ("AppFeedBackFragment".equals(fragmentName)){
                ((AppFeedBackFragment) fragment).onActivityResult(requestCode, resultCode, data);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode==1){
            if (permissions[0].equals(Manifest.permission.CAMERA)&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //获取权限成功
            }else {//没有获得到权限
//                Toast.makeText(getActivity(),"您不给权限我就不好干事了啦",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
