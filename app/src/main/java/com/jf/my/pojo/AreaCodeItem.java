package com.jf.my.pojo;

/**
 *  封装下数据，添加index字符串和是否是index的第一行元素的两个方法
 */
public class AreaCodeItem extends AreaDataItem<AreaCodeBean>{
    private boolean top;
    private String index;

    public AreaCodeItem(AreaCodeBean data, String index, boolean top) {
        super(data);
        this.index = index;
        this.top = top;
    }

    @Override
    public boolean isHead() {
        return top;
    }

    @Override
    public String getGroupName() {
        return index;
    }
}
