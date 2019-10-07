package com.jf.my.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:  蜜粉圈的品牌推荐
 * @Time: $date$ $time$
 **/
public class CircleBrand implements Serializable {
    private int id;
    private String picture;
    private int isShow;
    private String icon;
    private String name;
    private int width;
    private int height;
    private String userLabel;
    private String labelPicture;
    private int page;
    private int rows;
    private Object order;
    private String collectionId;
    private int isCollection;
    private List<String> pictureList;

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getLabelPicture() {
        return labelPicture;
    }

    public void setLabelPicture(String labelPicture) {
        this.labelPicture = labelPicture;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Object getOrder() {
        return order;
    }

    public void setOrder(Object order) {
        this.order = order;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public int getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(int isCollection) {
        this.isCollection = isCollection;
    }
}
