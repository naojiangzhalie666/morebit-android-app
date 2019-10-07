package com.markermall.cat.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2019/6/14.
 */

public class CategoryListChildDtos implements Serializable {
    /**
     * id : 0
     * title :
     * type : 3
     * child : [{"id":0,"title":"","type":0}]
     */

    private int id;
    private String title;
    private String type;
    private List<ImageInfo> opens;

    private boolean isSelect;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ImageInfo> getOpens() {
        return opens;
    }

    public void setOpens(List<ImageInfo> opens) {
        this.opens = opens;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
