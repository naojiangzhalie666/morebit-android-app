package com.zjzy.morebit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.MsgInfo;

/**
 * 系统消息详情
 * Created by Dell on 2016/3/22.
 */
public class SystemMsgDetailFragment extends BaseFragment {

    private TextView title,time,content;
    private MsgInfo msgInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initBundle();
        View view = inflater.inflate(R.layout.fragment_systemmsg_detail, container, false);
        inview (view);
        return view;
    }

    private void initBundle(){
        Bundle bundle = getArguments();
        if(bundle != null){
            msgInfo = (MsgInfo) bundle.getSerializable("data");
        }
    }
    /**
     * 初始化界面
     */
   public void inview (View view){
       title = (TextView)view.findViewById(R.id.title);
       time = (TextView)view.findViewById(R.id.time);
       content = (TextView)view.findViewById(R.id.content);
       initData();
   }

    /**
     * 初始化数据
     */
   private void initData(){
       if(msgInfo==null){
           return;
       }
       try {
//           title.setText("标题: " + msgInfo.getBranndName());
//           String getTime = msgInfo.getCreates_time();
//           if (getTime.length() > 10) {
//               getTime = getTime.substring(0, 10);
//           }
//           time.setText("时间: " + getTime);
//           content.setText(msgInfo.getContent());
       }catch (Exception e){
           e.printStackTrace();
       }
   }


}
