package com.zjzy.morebit.pojo.UI;

/**
 * Created by fengrs on 2018/6/22.
 */

public class BaseTitleTabBean {
    public  String name ="";
    public  String activity_id ="";//:精选活动ID
    public  String where=""; // 查找数据的字段
    public  String order=""; // 排序
    public  boolean isSelect;// 是否有选择
    public  boolean isSwitchItme ;// 切换Itme 样式

    public  String categoryId ="";//分类id
    public  String userId ="";//店铺Id
    public  String material ;//物料id


    public BaseTitleTabBean(boolean isSwitchItme,String activity_id) {
        this.isSwitchItme = isSwitchItme;
        this.activity_id = activity_id;
    }
    public BaseTitleTabBean(boolean isSwitchItme ) {
        this.isSwitchItme = isSwitchItme;
    }

    public BaseTitleTabBean(String name) {
        this.name = name;
    }

    public BaseTitleTabBean(String name, boolean isSelect,String where) {
        this.name = name;
        this.isSelect = isSelect;
        this.where = where;
    }
       public BaseTitleTabBean(String name, boolean isSelect,String where,String activity_id) {
        this.name = name;
        this.isSelect = isSelect;
        this.where = where;
        this.activity_id = activity_id;
    }

}
