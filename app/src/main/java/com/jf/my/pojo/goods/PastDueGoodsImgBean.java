package com.jf.my.pojo.goods;

import java.util.List;

/**
 * Created by fengrs on 2018/12/25.
 * 备注: 图片详情上传
 */

public class PastDueGoodsImgBean {
    private List<PastDueGoodsImgDataBean> list;
    private int size;

    public List<PastDueGoodsImgDataBean> getList() {
        return list;
    }

    public void setList(List<PastDueGoodsImgDataBean> list) {
        this.list = list;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
