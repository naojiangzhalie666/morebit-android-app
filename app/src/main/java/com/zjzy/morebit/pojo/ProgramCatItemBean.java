package com.zjzy.morebit.pojo;



import java.io.Serializable;
import java.util.Objects;

/**
 * 小程序 关键字搜索 接口 请求参数
 * @author CSR
 * @since 2019/2/28 0028 14:07
 */

public class ProgramCatItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *  平台 1 京东   2 拼多多  3 苏宁
     *
     */
    private Integer  type = 2;
    /**
     *  类别
     *  1 美食  2 水果  3 百货 4 家纺   5 母婴  6 女装  7 美妆  8 鞋包
     */
    private Integer catId;

   private Integer page = 1 ;
   private Integer rows  = 10;
   private String eliteId="";


    public String getEliteId() {
        return eliteId;
    }

    public void setEliteId(String eliteId) {
        this.eliteId = eliteId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
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
                ProgramCatItemBean that = (ProgramCatItemBean) o;
                return Objects.equals(type, that.type) &&
                        Objects.equals(catId, that.catId) &&
                        Objects.equals(page, that.page) &&
                        Objects.equals(rows, that.rows) ;
        }

        @Override
        public int hashCode() {
                return Objects.hash(type, catId, page, rows);
        }
}
