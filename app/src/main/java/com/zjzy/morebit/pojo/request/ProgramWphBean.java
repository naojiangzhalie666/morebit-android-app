package com.zjzy.morebit.pojo.request;


import java.io.Serializable;
import java.util.Objects;

/**
 * 小程序 关键字搜索 接口 请求参数
 *
 * @author CSR
 * @since 2019/2/28 0028 14:07
 */

public class ProgramWphBean implements Serializable {


    private Integer page = 1;
    private Integer rows = 20;
    private String order;
    private String keyword;
    private String fieldName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramWphBean that = (ProgramWphBean) o;
        return
                Objects.equals(page, that.page) &&
                Objects.equals(rows, that.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, rows);
    }
}
