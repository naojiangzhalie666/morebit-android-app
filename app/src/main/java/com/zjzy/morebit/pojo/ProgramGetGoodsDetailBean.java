package com.zjzy.morebit.pojo;



import java.io.Serializable;
import java.util.Objects;

/**
 * 小程序 商品详情DTO  请求参数
 * @author CSR
 * @since 2019/2/28 0028 14:07
 */

public class ProgramGetGoodsDetailBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *  平台 1 京东   2 拼多多
     *
     */
    private Integer  type = 2;
    /**
     * 商品ID
     */
    private Long goodsId;

    private Integer searchType;

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public Integer getType() {
        return type;
    }

    public ProgramGetGoodsDetailBean setType(Integer type) {
        this.type = type;
        return this;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public ProgramGetGoodsDetailBean setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ProgramGetGoodsDetailBean that = (ProgramGetGoodsDetailBean) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(goodsId, that.goodsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, goodsId);
    }
}
