package com.jf.my.pojo.grenndao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fengrs on 2018/12/25.
 * 备注: 商品图片滴缓存
 */


@Entity
public class GoodsImgHistory {
    @Id(autoincrement = false)
    private String itemSourceId = ""; //商品ID
    private String content = ""; //解析内容
    private int type ; //解析内容
    private long time; //插入时间
    @Generated(hash = 928868360)
    public GoodsImgHistory(String itemSourceId, String content, int type,
            long time) {
        this.itemSourceId = itemSourceId;
        this.content = content;
        this.type = type;
        this.time = time;
    }

    @Generated(hash = 1353767888)
    public GoodsImgHistory() {
    }

    public String getItemSourceId() {
        return this.itemSourceId;
    }
    public void setItemSourceId(String itemSourceId) {
        this.itemSourceId = itemSourceId;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
