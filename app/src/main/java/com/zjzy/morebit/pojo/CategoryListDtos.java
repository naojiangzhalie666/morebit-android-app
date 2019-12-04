package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2019/6/14.
 */

public class CategoryListDtos implements Serializable {
    /**
     * id : 0
     * title :
     * type : 3
     * child : [{"id":0,"title":"","type":0}]
     */

    private int id;
    private String title;
    private String type;
    private List<CategoryListChildDtos> child;
    private boolean expanded;

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

    public List<CategoryListChildDtos> getChild() {
        return child;
    }

    public void setChild(List<CategoryListChildDtos> child) {
        this.child = child;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
