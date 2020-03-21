package com.zjzy.morebit.pojo;


/**
 * 小程序 关键字搜索 接口 请求参数
 * @author CSR
 * @since 2019/2/28 0028 14:07
 */

public class ProgramSearchKeywordBean  {
    private static final long serialVersionUID = 1L;
    /**
     *  平台 1 京东   2 拼多多 3 苏宁
     *
     */
    private Integer  type;
    /**
     * 搜索内容
     */
    private String keyword;

    /**
     * 页码，默认1
     */
    private Integer page = 1;

    /**
     * 条数，默认10
     */
    private Integer rows = 10;


}
