package com.jf.my.pojo;

/**
 * Created by Administrator on 2017/12/7.
 */

public class WelfareOrderInfo {

    /**
     * original_order_no : 1234567891
     * title : 1
     * taobao : 546833342832
     * start_time : 1525881600
     * end_time : 1526054400
     * create_time : 2018-05-12 00:00:00
     * free_price : 35.00
     * picture : beijing.aliyuncs.com/picture/20180510/152592191693838.png
     */

    //订单编号
    private String original_order_no="";
    //商品标题
    private String title="";
    //淘宝id
    private String taobao="";
    //免单开始时间
    private long start_time;
    //免单结束日期
    private long end_time;
    //下单时间
    private String create_time="";
    //下单时间
    private String free_price="";
    // 商品图片
    private String picture="";
    // 免单金额
    private String payment_amount="";
    // 订到是失效时间
    private long CreateTime;
    private String cupon_url="";//  优惠地址 获取淘口令
    private String tb_reserve_price="";//
    private int user_type;// 天猫
    private int is_review;//  0 3 show
    private  int is_order_sn;//  是否提交订单号, 1 不倒及时, 不立即购买
    private  String order_sn="";//  上次订单号
    private  String settlement_time ="";//  上次订单号

    public String getSettlement_time() {
        return settlement_time;
    }

    public void setSettlement_time(String settlement_time) {
        this.settlement_time = settlement_time;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public int getIs_review() {
        return is_review;
    }

    public void setIs_review(int is_review) {
        this.is_review = is_review;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getTb_reserve_price() {
        return tb_reserve_price;
    }

    public void setTb_reserve_price(String tb_reserve_price) {
        this.tb_reserve_price = tb_reserve_price;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getIs_order_sn() {
        return is_order_sn;
    }

    public void setIs_order_sn(int is_order_sn) {
        this.is_order_sn = is_order_sn;
    }

    public String getCupon_url() {
        return cupon_url;
    }

    public void setCupon_url(String cupon_url) {
        this.cupon_url = cupon_url;
    }


    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }


    public String getOriginal_order_no() {
        return original_order_no;
    }

    public void setOriginal_order_no(String original_order_no) {
        this.original_order_no = original_order_no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaobao() {
        return taobao;
    }

    public void setTaobao(String taobao) {
        this.taobao = taobao;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFree_price() {
        return free_price;
    }

    public void setFree_price(String free_price) {
        this.free_price = free_price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    /**
     * order_sn : 656565454767676769
     * name : 【订单结算】潮牌苹果X手机壳潮男磨砂防摔创意iphone6个性硅胶全包7plus女8软
     * picture : https://gd1.alicdn.com/imgextra/i1/2626818564/TB2pvo3dlfH8KJjy1XbXXbLdXXa_!!2626818564.jpg
     * total_nums : 1
     * taobao_price : 25.00
     * pay_amount : 100.00
     * settlement_amount : 100.00
     * income_amount : 20.00
     * commission : 20.00
     * pid : mm_116307482_19348228_217376089
     * order_type : 1
     * deal_all : 3
     * status : 1
     * settlement_status : 1
     * creates_time : 1526457480
     * settlement_time : 1524626720
     * join_time : 1524626720
     * toabao_id : null
     * token : zhitu_yGMptkq0ERNe
     */
//
//    private String order_sn;
//    private String name;
//    private String picture;
//    private int total_nums;
//    private String taobao_price;//淘宝商品单价 25.00
//    private String pay_amount;//付款金额 100.00
//    private String settlement_amount;//结算金额 100.00
//    private String income_amount;//收入消费佣金 20.00
//    private String commission;
//    private String pid;
//    private String order_type;//1: 淘宝  2:天猫
//    private int deal_all;
//    private int status;//1:订单结算  2：订单付款 - 预估 3:订单失效
//    private int settlement_status;//是否已经结算 0 or 1
//    private int creates_time;//订单创建时间
//    private int settlement_time;//结算时间
//    private int join_time;
//    private Object toabao_id;
//    private String token;
//
//    public String getOrderSn() {
//        return order_sn;
//    }
//
//    public void setOrderSn(String order_sn) {
//        this.order_sn = order_sn;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPicture() {
//        return picture;
//    }
//
//    public void setPicture(String picture) {
//        this.picture = picture;
//    }
//
//    public int getTotal_nums() {
//        return total_nums;
//    }
//
//    public void setTotal_nums(int total_nums) {
//        this.total_nums = total_nums;
//    }
//
//    public String getTaobao_price() {
//        return taobao_price;
//    }
//
//    public void setTaobao_price(String taobao_price) {
//        this.taobao_price = taobao_price;
//    }
//
//    public String getPay_amount() {
//        return pay_amount;
//    }
//
//    public void setPay_amount(String pay_amount) {
//        this.pay_amount = pay_amount;
//    }
//
//    public String getSettlement_amount() {
//        return settlement_amount;
//    }
//
//    public void setSettlement_amount(String settlement_amount) {
//        this.settlement_amount = settlement_amount;
//    }
//
//    public String getIncome_amount() {
//        return income_amount;
//    }
//
//    public void setIncome_amount(String income_amount) {
//        this.income_amount = income_amount;
//    }
//
//    public String getCommission() {
//        return commission;
//    }
//
//    public void setCommission(String commission) {
//        this.commission = commission;
//    }
//
//    public String getPid() {
//        return pid;
//    }
//
//    public void setPid(String pid) {
//        this.pid = pid;
//    }
//
//    public String getOrderType() {
//        return order_type;
//    }
//
//    public void setOrderType(String order_type) {
//        this.order_type = order_type;
//    }
//
//    public int getDeal_all() {
//        return deal_all;
//    }
//
//    public void setDeal_all(int deal_all) {
//        this.deal_all = deal_all;
//    }
//
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public int getSettlement_status() {
//        return settlement_status;
//    }
//
//    public void setSettlement_status(int settlement_status) {
//        this.settlement_status = settlement_status;
//    }
//
//    public int getCreates_time() {
//        return creates_time;
//    }
//
//    public void setCreates_time(int creates_time) {
//        this.creates_time = creates_time;
//    }
//
//    public int getSettlementTime() {
//        return settlement_time;
//    }
//
//    public void setSettlementTime(int settlement_time) {
//        this.settlement_time = settlement_time;
//    }
//
//    public int getJoin_time() {
//        return join_time;
//    }
//
//    public void setJoin_time(int join_time) {
//        this.join_time = join_time;
//    }
//
//    public Object getToabao_id() {
//        return toabao_id;
//    }
//
//    public void setToabao_id(Object toabao_id) {
//        this.toabao_id = toabao_id;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
}
