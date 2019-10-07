package com.markermall.cat.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.markermall.cat.pojo.AreaCodeBean;
import com.markermall.cat.pojo.AreaCodeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 地区选择工具类
 **/
public class AreaCodeUtil {

    public static AreaCodeBean getDefaultCode(){
        return new AreaCodeBean("中国大陆","86",11,"Z");
    }



    public static List<String> getIndexList(@NonNull List<AreaCodeBean> list) {
        //填充groupName
        ArrayList<String> indexList = new ArrayList<>();
        String groupName;
        String oldName = null;
        for (int j = 0; j < list.size(); j++) {
            AreaCodeBean code = list.get(j);
             groupName = code.getPrefix();
            if (!TextUtils.equals(groupName, oldName)) {
                indexList.add(groupName);
            }
            oldName = groupName;
        }

        return indexList;
    }

    public static List<AreaCodeItem> getAreaCodeItemList(@NonNull List<AreaCodeBean> list) {
        List<AreaCodeItem> data = new ArrayList<>(list.size());
        String preIndex = null;
        String index;
        boolean top;
        for (int j = 0; j < list.size(); j++) {
            AreaCodeBean code = list.get(j);
            index = code.getPrefix();
            top = !TextUtils.equals(index, preIndex);
            data.add(new AreaCodeItem(code, index, top));
            preIndex = index;
        }

        return data;
    }
}
