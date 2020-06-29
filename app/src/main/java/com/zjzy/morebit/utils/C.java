package com.zjzy.morebit.utils;

import android.support.annotation.IntRange;

import com.zjzy.morebit.BuildConfig;

/**
 * SharePreference配置常量
 */
public class C {


    // api

    public static final String BASE_MOREBIT_DEV = "http://api.dev.morebit.com.cn";//开发
    public static final String BASE_MOREBIT_TEST = "http://api.test.morebit.com.cn";//测试
    public static final String BASE_MOREBIT_PROD = "https://api.morebit.com.cn";//正式
    public static final String BASE_HOST = BuildConfig.BASE_HOST;
    //本地
//    public static final String BASE_YUMIN = BuildConfig.BASE_HOST;

    private int serverType = PROD;
    public static final int PROD = 0;//正式
    public static final int TEST = 1;//测试
    public static final int DEV = 2;//开发

    /**
     * 设置服务ip切换环境
     *
     * @param serverType
     */
    public void setServerType(@IntRange(from = 0, to = 2) int serverType) {
        this.serverType = serverType;
    }


    public String getServerTypeName() {
        switch (serverType) {
            case PROD:
                return "正式环境";
            case TEST:
                return "测试环境";
            case DEV:
                return "开发";
        }
        return "正式";
    }
    private static C instance;
    public synchronized static C getInstance() {
        if (instance == null) {
            instance = new C();
        }
        return instance;
    }

    public String getGoodsIp() {
//        switch (serverType) {
//            case PROD:
//                return BASE_MOREBIT_PROD;
//            case TEST:
//                return BASE_MOREBIT_TEST;
//            case DEV:
//                return BASE_MOREBIT_DEV;
//        }
//        return BASE_MOREBIT_PROD;
        return BASE_HOST;
    }

    public static class UrlV2 {
        //用户模块调用
        //   public static final String USERS = BASE_MOREBIT;
        //商品模块调用
        //   public static final String GOODS = BASE_MOREBIT;
        //系统模块
        //    public static final String SYSTE = BASE_MOREBIT;
        //订单模块
        //   public static final String ORDERS = BASE_MOREBIT;
    }

    public static class Setting {
        public static final String appName = "morebit_";
        public static String user_agent = "";//手机型号
        public static String device_id = "";//设备号
        public static String app_version = "";//app版本号
        public static String gray = ""; //灰度控制参数


        public static final String descParms = "desc"; //倒序
        public static final String ascParms = "asc"; //升序
        public static final String itemIndex = "itemIndex"; //综合
        public static final String itemVoucherPrice = "itemVoucherPrice"; //券后价
        public static final String saleMonth = "saleMonth"; //销量
        public static final String commission = "commission"; //奖励
        public static final String coupon_select = "select"; //优惠卷
        public static final String coupon_un_select = "select"; //优惠卷

        public static final String sort_price = "price";//单价
        public static final String sort_commissionShare = "commissionShare";//佣金比例
        public static final String sort_inOrderCount30Days = "inOrderCount30Days";//销量


        public static final int deviceType = 1;
        public static final int os = 1;
    }

//    //订单类型
//    public static class OrderType{
//        //淘宝
//        public static final int ORDER_TYPE_TAOBAO = 1;
//        //优选
//        public static final int ORDER_TYPE_YOUXUAN = 10;
//        //京东
//        public static final int ORDER_TYPE_JD = 2;
//        //拼多多
//        public static final int ORDER_TYPE_PDD = 4;
//    }


    public static class Result {  //返回值
        public static final int shareMoneyToEditTemplateCoad = 1001;
    }

    /**
     * 更新或者增加地址。
     */
    public static class Address {
        public static final int ADD_TYPE = 0;
        public static final int UPDATE_TYPE = 1;
    }


    /**
     * 订单状态
     */
    public static class OrderStatus {
        /**
         * 未支付的订单状态
         */
        public static final int WAIT_PAY_ORDER_STATUS = 2;

        /**
         * 订单已支付（已支付，已发货，未确认收货状态）
         */
        public static final int SUCCESS_ORDER_STATUS = 1;
        /**
         * 已结算状态
         */
        public static final int SETTLE_ORDER_STATUS = 3;

        /**
         * 订单关闭（已经确认收货（或者系统自动确认收货））
         */
        public static final int CLOSE_ORDER_STATUS = 4;

        /**
         * 待收货状态
         */
        public static final int RECEIVE_GOODS_ORDER_STATUS = 6;
    }

    /**
     * 更新或者增加地址。
     */
    public static class UserGrade {

        /**
         * 会员
         */
        public static final int NUMBER = 0;
        /**
         * Vip会员
         */
        public static final int VIP_NUMBER = 1;
        /**
         * 团队长
         */
        public static final int SENIOR_LEADER = 2;

    }

    /**
     * 传值 extrar
     */
    public static class Extras {
        public static final String pushType = "pushType";
        public static final String fragmentData = "fragmentData";
        // 过时用下面滴
        public static final String openFragment_isSysBar = "openFragment_isSysBar"; // 打开fragment时候是否要导航栏
        //搜索关键字
        public static final String search_keyword = "keyWord";

        public static final String loginType = "loginType"; // 是否显示用户协议,判断是否注册

        //注册登录
        public static final String PHONE = "phone";  //
        public static final String OAUTH_WX = "OAUTH_WX";  //
        public static final String INVITATION_CODE = "invitationCode";  //
        public static final String ID = "id";
        public static final String isBackPressed = "isBackPressed";
        public static final String ewmText = "ewmText";
        public static final String AREACODE = "areaCode";

        // 招商中心
        public static final String order_type = "order_type"; // 0 查看订单  1, 输入订单
        public static final String order_sn = "order_sn"; //  订单号
        public static final String returncach_itme = "returncach_itme"; //  返现订单

        //商品
        public static final String TAOBAO = "TAOBAO"; // 淘宝Id
        public static final String GOODSBEAN = "GOODSBEAN"; // 一个商品
        public static final String VIDEOBEAN = "VIDEOBEAN"; // 一个视频id
        public static final String PAGEBEAN = "PAGEBEAN"; // 一个视频页数
        public static final String CIDBEAN = "CIDBEAN"; // 一个分类id
        public static final String TKLDATA = "TKLDATA"; // 淘口令数据
        public static final String PDDPROMOTIONDATA = "PDDPROMOTIONDATA"; // 拼多多数据
        public static final String ITEMVIDEOID = "ITEMVIDEOID"; // 视频地址
        public static final String WEBURL = "WEBURL"; // webview 地址
        public static final String WEBTITLE = "WEBTITLE"; // webview title
        public static final String TITLE = "TITLE"; //   title
        public static final String FLOORCHILD = "floorChild"; //   floorChild
        public static final String COUNTRY = "country"; //   country
        public static final String POSTER_URL = "POSTER_URL"; //   海报
        public static final String POSTER_POS = "POSTER_POS"; //   海报 选择位置

        public static final String GOODS_DETAIL_PDD = "GOODS_DETAIL_PDD"; //拼多多商品详情

        public static final String GOODS_CONTENTS = "GOODS_CONTENTS"; //   内容

        //会员商品订单
        public static final String GOODS_ORDER_INFO = "GOODS_ORDER_INFO";
        //收货地址
        public static final String GOODS_ADDRESS_INFO = "GOODS_ADDRESS_INFO";
        //增加或者更新地址
        public static final String TYPE_ADDRESS = "UPDATE_ADDRESS";
        //从订单确认页面跳转
        public static final String SELECTED_ADDRESS_FROM_CONFIRM_ORDER = "SELECT_ADDRESS_FROM_CONFIRM_ORDER";

        //是否显示 切换环境item
        public static final String KEY_SHOW_DEVELOPER_SETTING = "key_show_developer_setting";

        /**
         * 切换服务器类型
         */
        public static final  String KEY_SERVER_TYPE = "serverType";

        public static final String SEARCH_TYPE = "SEARCH_TYPE";

    }

    /**
     * 传值 extrar
     */
    public static class SceneTag {
        public static final int goTaobaoTag = 80590;

    }

    /**
     * 商品列表的type
     */
    public static class GoodsListType {
        public static final int CurrentRanking = 21; // 实时排行
        public static final int RealTimeRanking = 22; // 今日排行
        public static final int THREEGOODS = 23; // 三级分类
        public static final int NINEPINKAGE = 24; // 9.9包邮
        public static final int TypeActivity = 25; // 精选活动列表
        public static final int DiscountStores = 26; // 店铺优惠
        public static final int ForeShow_1 = 27; // 预告单  2-抢购中；3-隔夜单；1-即将开始
        public static final int ForeShow_2 = 28; // 预告单  2-抢购中；3-隔夜单；1-即将开始
        public static final int ForeShow_3 = 29; // 预告单  2-抢购中；3-隔夜单；1-即将开始
        public static final int BigShopList = 30; // 超值大牌
        public static final int officialrecomList = 31;//官方推荐页面列表
        public static final int WHAT_LIKE = 32; // 猜你喜欢
        public static final int MATERIAL = 33;// 物料id列表
        public static final int RANKING_NEWS = 34;// 新版排行榜
        public static final int DAYRECOMMEND = 1;//每日推荐
        public static final int JVHUASUAN = 2;//聚划算
        public static final int TAOQIANGGOU = 3;//淘抢购


    }

    /**
     * banner 跳转事件
     */
    public static class BannerIntentionType {


        public static final int JHS = 1;// 聚划算
        public static final int TQG = 2; // 淘抢购
        public static final int NINE = 3;//9.9
        public static final int PANICBUY = 4;//限时秒杀
        public static final int OFFICIALRECOM = 5;//官方推荐页面
        public static final int BrandSell = 6;//品牌特卖
        public static final int MakeMoney = 7;//赚钱计划
        public static final int TypeActivity = 8;// 专题列表
        public static final int Newcomers = 9;// 新人课堂
        public static final int OfficialNotice = 10;// 官方公告
        public static final int Collect = 11;// 收藏
        public static final int service = 12;// 专属客服
        public static final int AppFeed = 13;// 意见反馈
        public static final int AppAbout = 15;// 关于我们
        public static final int suggest = 16;// 投诉建议
        public static final int life = 17;// 投诉建议
        public static final int Ranking = 18;// 热门排行
        public static final int DAYRECOMMEND = 19;// 每日推荐
        public static final int Browsing = 20;// 足迹
        public static final int operator = 21;// 运营专员后台
        public static final int officialActivity = 22;// 22.官方活动

        public static final int BigShop = 24;//24：超值大牌
        public static final int ForeShow = 25;//25：预告单
        public static final int CIRCLE_REVIEW = 26;//0pen=2,商学院预览列表
        public static final int BRAND_LIST = 27;//
        public static final int THREE_GOODS = 28;//
        public static final int GOODS_BYBRAND = 29;//
        public static final int JD = 40;//京东
        public static final int PDD = 41;//拼多多
        public static final int NEW_PERSONAL = 42;//新人免单
        public static final int HUNGRY = 43;//饿了么
        public static final int MOUTH = 44;//口碑
        public static final int SHOPMALL = 45;//优选商城
        public static final int KOALA = 46;//考拉海购
        public static final int WPH = 47;//唯品会


    }

    /**
     * 商品列表的type
     */
    public static class more {
        public static final int jptjType = 1; // 精品推荐 type


    }

    /**
     * 发送验证码
     * 1：登录 2：修改密码 3：绑定微信 4：修改支付宝 5：修改手机号 6:注册 7: 注销 8:微信注册 已有账号立即绑定
     */
    public static class sendCodeType {
        public static final int LOGIN = 1;
        public static final int REVAMPPWD = 2;
        public static final int BINDWEIXIN = 3;
        public static final int REVAMPAIPAY = 4;
        public static final int REVAMPPHONE = 5;
        public static final int REGISTER = 6;
        public static final int logout = 7; //
        public static final int WEIXINREGISTER = 7; //自己用 绑定微信
        public static final int WEIXINBIND = 8;

    }

    /**
     * 发送验证码
     * 类型：0是消费者，1是代理商，2是运营专员
     */
    public static class UserType {
        //会员
        public static final String member = "0";
        //vip会员
        public static final String vipMember = "1";
        //团队长
        public static final String operator = "2";

        public static final String ORDERTYPE = "ORDERTYPE";
        public static final String TIMESKILL = "TIMESKILL";
        public static final String ICONNAME = "ICONNAME";


    }

    /**
     * 系统配置
     * 获取商家推广语:WEB_EXTENSION
     * 获取分享规则:WEB_SHARE_RULE
     * APP_SORT_BACKGROUND:分类背景图
     * RECEIVE_RED_PACKET:首页红包
     * SEARCH_DISCOVERY_ANDROID:搜索发现安卓
     * WEB_FANS_LIST: 粉丝列表
     */
    public static class SysConfig {
        public static String COMMISSION_PERCENT_VALUE;// 获取普通会员，VIP，运营商佣金比例 扥value

        public static String SELF_COMMISSION_PERCENT_VALUE;// 自营商品的获取普通会员，VIP，运营商佣金比例 扥value
        public static String NUMBER_COMMISSION_PERCENT_VALUE = "60";// 获取普通会员的分佣比例。
        public static final String RECEIVE_RED_PACKET = "RECEIVE_RED_PACKET";
        public static final String SEARCH_DISCOVERY_ANDROID = "SEARCH_DISCOVERY_ANDROID";
        public static final String WEB_FANS_LIST = "WEB_FANS_LIST";
        public static final String NEW_PEOPLE_BANNER = "NEW_PEOPLE_BANNER";// 新人课堂banner图
        public static final String COMPLAINT_SUGGESTION = "COMPLAINT_SUGGESTION ";// 投诉建议
        public static final String WEB_WALLET_RULE = "WEB_WALLET_RULE";// 收益模块的规则说明
        public static final String WEB_SHARE_RULE = "WEB_SHARE_RULE";// 商品分享规则
        public static final String WEB_WITHDRAW_ACCOUNT = "WEB_WITHDRAW_ACCOUNT";// 商品分享规则
        public static final String ITEM_SHARE_HOST = "ITEM_SHARE_HOST";// 商品分享规则
        public static final String TAOBAO_TO_GRANT_AUTHORIZATION = "TAOBAO_TO_GRANT_AUTHORIZATION";// 授权了解详情
        public static final String CUSTOMER_SERVICE_ADDRESS = "CUSTOMER_SERVICE_ADDRESS";// 客服系统地址H5
        public static final String COMMISSION_PERCENT = "COMMISSION_PERCENT";// 获取普通会员，VIP，运营商佣金比例

        public static final String SELF_COMMISSION_PERCENT = "SELF_COMMISSION_PERCENT";// 自营商品的获取普通会员，VIP，运营商佣金比例
    }

    /**
     * 展示位
     * 类型
     * official：官方推荐，
     * poster：推广海报，
     * start：启动页，
     * popup：首页弹窗，
     * personal：个人轮播，
     * welfare：福利津贴，
     * noticebar：标题轮播，
     * brandsale：品牌特卖，
     * taobao_v2：聚划算v2，
     * 0：首页轮播，1：推广海报，6：启动页，8：官方推荐，11：首页弹窗，12：个人轮播，13：福利津贴，14：标题轮播，16：品牌特卖，17：聚划算v2 18 赚钱计划轮播图
     * 19:我的工具 20:商学院 ,21：首页ICON配置,22是好单预告,32 首页悬浮窗
     */
    public static class UIShowType {
        public static final int HomeBanner = 0;
        public static final int Poster = 1;
        public static final int Start = 6;
        public static final int Official = 8;
        public static final int Popup = 11;
        public static final int Personal = 12;
        public static final int Welfare = 13;
        public static final int Noticebar = 14;
        public static final int Brandsale = 16;
        public static final int Taobao_v2 = 17;
        public static final int Makemoney = 18;
        public static final int myTool = 19;
        public static final int commercial = 20;
        public static final int HomeIcon = 21;
        public static final int HomeSecond = 22;
        public static final int PERSONAL_FUNCTION = 29;
        public static final int FLOAT_AD = 32;
    }

    /**
     * 协议配置
     * "3": "服务协议",
     * "4": "奖励说明",
     * "5": "奖励规则",
     * "8": "常见问题",
     * "9": "新人上路",
     * "10": "安卓更新公告",
     * "12": "分类菜单",
     * "14": "发现精彩",
     * "15": "隐私政策"
     */
    public static class ProtocolType {

        public static final int agreement = 3;
        public static final int awardShow = 4;
        public static final int rewardRules = 5;
        public static final int commonProblem = 8;
        public static final int comers = 9;
        public static final int notice = 10;
        public static final int categoryMenu = 12;
        public static final int wonderful = 14;
        public static final int privateProtocol = 15;

    }

    /**
     * 发送验证码
     * 类型：0是消费者，1是代理商，2是运营专员
     */
    public static class requestCode {
        // 自己用
        public static final String dataListEmpty = "dataListEmpty";//  listdata为null
        public static final String dataNull = "dataNull";//   data为null

        public final static String SUCCESS = "B00000";//成功
        public static final String B10005 = "B10005";//号码已经被注册
        public static final String B10010 = "B10010";//注册手机号已存在,跳转登录界面
        public static final String B10040 = "B10040";//B10011 该微信未注册,跳转到注册界面
        public static final String B10031 = "B10031";//  登录时未注册去登录
        public static final String B10051 = "B10051";//  微信号或手机号已注册
        public static final String B10019 = "B10019";//  重新登陆状态
        public static final String B10310 = "B10310";//  vip申请后,待审核
        public static final String B10003 = "B10003";//  密码不正确
        public static final String B10035 = "B10035";//  不存在的邀请码
        public static final String B1000004 = "B1000004"; //查询每日爆款失败
        public static final String B10301 = "B10301"; //不在提现时间
        public static final String B30401 = "B30401"; //天猫国际和飞猪的错误码
        public static final String B10358 = "B10358"; //此账号未绑定微信号
        public static final String B10011 = "B10011"; //该微信未注册,跳转到注册界面
        public static final String B10357 = "B10357"; //扫码来的用户，手机号为空
        public static final String B10009 = "B10009"; //此微信已绑定其他用户
        public static final String B800430 = "B800430"; //该手机号已有用户使用
        public static final String B1000007 = "B1000007"; //用户未注册，请先注册
        public static final String B30421 = "B30421"; //商品已下架，请走全局搜索

    }


    public static class ResponseCoad {
     /*
        //自定义
        100   调接口错误
        101   数据获取出错

        //接口
        10000 成功
        10001 参数错误
        10002 邀请码错误
        10003 验证码错误
        10004 此手机号已经注册
        10005 此微信已绑定其他账户
        10006 用户未注册
        10007 用户密码不能为空
        10008 用户密码错误
        10009 60秒内只能发送一次
        10010 微信用户数据信息错误
        90001 返利商品邀请人数不足
        */

        public static final int Coad100 = 100;


    }

    public static class syncTime {
        public static final String SERVER_TIME = "SERVER_TIME";
        public static final String CLIENT_TIME = "CLIENT_TIME";
    }

    //隐私授权
    public static class authPrivate {

        public static final String IS_AUTHED = "IS_AUTHED";
    }

    public static class sp {
        public static final String token = Setting.appName + "token";
        public static final String editTemplate = Setting.appName + "sp_editTemplate";
        public static final String SearchText = Setting.appName + "Search_Text";
        public static final String appStartImg = Setting.appName + "appStartImg";
        public static final String homeTopFlData = Setting.appName + "homeTopFlData";
        public static final String homeListData = Setting.appName + "homeListData";
        public static final String homelunboData = Setting.appName + "homelunboData";
        public static final String homeGtherData = Setting.appName + "homeGtherData";// 首页聚划算
        public static final String homeActivityData = Setting.appName + "homeActivityData_3";// 首页精选活动
        public static final String homeOfficialData = Setting.appName + "homeOfficialData";// 官方推荐图片
        public static final String homeRankingData = Setting.appName + "homeRankingData";// 官方推荐图片
        public static final String searchHotKey = Setting.appName + "searchHotKey";  // 历史搜索
        public static final String addressBook = Setting.appName + "addressBook"; // 联系人
        public static final String merchantExtension = Setting.appName + "merchantExtension";
        public static final String homeFenleiData = Setting.appName + "homeFenleiData"; // 首页分类
        public static final String homeCollectLike = Setting.appName + "homeCollectLike"; // 收藏猜您喜欢
        public static final String searechHotKeywords = Setting.appName + "searechHotKeywords1"; // 热门搜索
        public static final String homeCategoryBackground = Setting.appName + "homeCategoryBackground"; // 分类bg
        public static final String rebateCrderRule = Setting.appName + "rebateCrderRule"; // 获取返现订单规则
        public static final String homeRedPagckageRecord = Setting.appName + "homeRedPagckageRecord3"; // 首页红包
        public static final String homeFenleiCourseTiem = Setting.appName + "homeFenleiCourseTiem"; // 新手教程的时间
        public static final String version_Code = Setting.appName + "version_Code"; //app 版本
        public static final String isSwitchLogo = Setting.appName + "isSwitchLogo"; //是否切换了logo
        public static final String UIShow = Setting.appName + "UIShow"; //
        public static final String userInfo = Setting.appName + "sUserInfo"; // 用户缓存
        public static final String isShowSubmitWX = Setting.appName + "isShowSubmitWX"; //是否弹出提交微信
        public static final String SHARE_POSTER = Setting.appName + "share_poster"; //分享海报缓存
        public static final String SysNoticeDataIds = Setting.appName + "SysNoticeDataIds2"; // 首页系统通知 关闭过滴id
        public static final String isShortLink = Setting.appName + "isShortLink"; //  是否短链，1-短链；2-长链；0-默认值
        public static final String isOpenHotMsgPush = Setting.appName + "isOpenHotMsgPush"; //  是否热门消息推送
        public static final String isShowGuide = Setting.appName + "isShowGuide"; //  是否显示首页的新手引导
        public static final String isShowGuideCircle = Setting.appName + "isShowGuideCircle"; //  是否显示发圈的新手引导
        public static final String isShowGuideMine = Setting.appName + "isShowGuideMine"; //  是否显示个人中心新手引导
        public static final String isShowMsgNotication = Setting.appName + "isShowMsgNotication"; //  是否显示消息首页的通知提示
        public static final String CACHE_AREA_CODE = Setting.appName + "areacode"; //  地区列表
        public static final String SHARE_RANG_DATA = Setting.appName + "SHARE_RANG_DATA"; //  密粉圈title
        public static final String RELEASE_GOODS = Setting.appName + "RELEASE_GOODS"; //  商品发布管理缓存
        public static final String FLOOR_CACHE = Setting.appName + "floorCache"; //  模块管理
        public static final String TAOBAO_LINK_CACHE = Setting.appName + "taobao_link"; //  淘宝跳转缓存
        public static final String CLESE_RECOMMEND_GOODS = Setting.appName + "CLESE_RECOMMEND_GOODS"; //  关闭推荐商品
        public static final String RANKING_CATEGORY = Setting.appName + "RANKING_CATEGORY"; //  排行榜二级标题数据
        public static final String CIRCLE_SEARCH_HISTORY = Setting.appName + "circle_search_history";  // 发圈历史搜索
        public static final String SHARE_MOENY_IS_INVITECODE = Setting.appName + "SHARE_MOENY_IS_INVITECODE";  // 是否需要邀请码 0 否 1是
        public static final String SHARE_MOENY_IS_DOWNLOAD_URL = Setting.appName + "SHARE_MOENY_IS_DOWNLOAD_URL ";  // 是否需要下载链接 0 否 1是
        public static final String SHARE_SHORT_LINK_FOR_PDD = Setting.appName + "SHARE_SHORT_LINK_FOR_PDD"; //  抢购链接拼多多

        public static final String COLLEGE_SEARCH_HISTORY = Setting.appName + "college_search_history";  // 商学院历史搜索
        public static final String APPSTART_POPUPTYPE = Setting.appName + "APPSTART_POPUPTYPE";  //广告页面
        public static final String CIRCLE_LOAD_DATA_TYPE = Setting.appName + "CIRCLE_LOAD_DATA_TYPE";  //发圈加载数据type
        public static final String CIRCLE_SEARCH_NAME = Setting.appName + "CIRCLE_SEARCH_NAME ";  //发圈搜索内容
        public static final String CIRCLE_SEARCH_TITLE = Setting.appName + "CIRCLE_SEARCH_TITLE ";  //发圈 title
        public static final String DIALOG_USER_IS_UPGRADE = Setting.appName + "DIALOG_USER_IS_UPGRADE";  //是否已弹出用户升级提示
        public static final String DIALOG_USER_IS_UPGRADE_TIME = Setting.appName + "DIALOG_USER_IS_UPGRADE_TIME";  //用户升级dialog弹出的时间
        public static final String SAVED_DB_FOR_ADDRESS = Setting.appName + "SAVED_DB_FOR_ADDRESS";  //地址保存到db
        public static final String PDD_CATEGORY = Setting.appName + "PDD_CATEGORY"; //  拼多多二级标题数据
    }


    public static class requestType {
        public static final int initData = 0; // 第一次 ,刷新
        public static final int loadMore = 1;  // 加载更多

    }

    public static class Push {
        /**
         * "1":"首页","2":"分类","3":"收藏","4":"个人中心","5":"发圈","6":"奖励消息",
         * "7":"系统消息","8":"提现管理","9":"商品推送","10":"超级划算","11":"品牌爆款"}
         * 等于9的时候有一个字段goods_id是淘宝商品的ID
         * 等于9的时候有一个字段goods_id是淘宝商品的ID.   内容就是content字段当标题
         */
        public final static int homePage = 1;//首页
        public final static int fenlei = 2;//分类
        public final static int collect = 3;//收藏
        public final static int partner = 4;//我的
        public final static int circle = 5;//发圈
        public final static int awardMsg = 6;//奖励消息
        public final static int sysMsg = 7;//系统消息
        public final static int moenyMsg = 8;//提现消息
        public final static int goodsDetail = 9;//商品推送
        public final static int superWeigh = 10;//超级划算
        public final static int GO_H5 = 12;//跳转到h5
        public final static int earningsMsg = 13;//收益消息
        public final static int FEEDBACK_MSG = 14;//反馈回复消息
        public final static int FANS_MSG = 15;//粉丝消息
        public final static int SHARE_MSG_PASS = 16;//16 商品已上架
        public final static int SHARE_MSG_FAILURE = 17;// 商品未通过
        public final static int SHARE_MSG_UNSHELVE = 18;//商品已下架
        public final static int SHARE_MSG_VIP = 19;//vip会员页
        public final static String tag_everydayHotCommodity = "everydayHotCommodity";//热门推荐Tag
    }

    public static class Response {

    }


    public static class SearchStatistics {
        public final static String ADID = "adId";
        public final static String TYPE = "type";

        //商品领券分享模块
        public final static String ID_SHARE = "32";
        public final static String TYPE_share = "4";

        //搜索框下面 推荐关键词
        public final static String ID_HOT_KEY = "42";
        public final static String TYPE_HOT_KEY = "2";

        //外面复制了淘宝标题到APP搜索时，标题剪贴板自动识别后点击
        public final static String ID_COPY_SEARCH = "41";
        public final static String TYPE_COPY_SEARCH = "3";

        //在搜索输入框输入内容时
        public final static String ID_SEARCH = "40";
        public final static String TYPE_SEARCH = "3";

        //多点优选热门
        public final static String TYPE_HOME_HOT = "1";
    }

    public static class EnvType {
        public static final int DEVELOP = 1;//开发环境
        public static final int UAT = 2;//Uat环境
        public static final int PP = 3;//pp环境
        public static final int PRODUCT = 4;//正式环境
    }


    public static class ViewType {
        public static final int FLOOR_ONE = 1;
        public static final int FLOOR_TWO = 2;
        public static final int FLOOR_THREE = 3;
        public static final int FLOOR_FOUR = 4;
        public static final int FLOOR_FIVE = 5;
    }

    public static class PHONE {
        public static final int MIN_LENGTH = 6;
        public static final int MAX_LENGTH = 12;
        public static final int DEFAULT_LENGTH = 11;
    }

    public static class ConfigKey {
        //NOTICE_SHOW_MORE 公告区-是否显示更多入口 1：是 0：否
        public static final String NOTICE = "NOTICE_SHOW_MORE";
        //HOME_RECOMMENDED_TITLE: 首页推荐区title
        public static final String HOME_RECOMMENDED_TITLE = "HOME_RECOMMENDED_TITLE";
        //TODAY_RECOMMENDED_TITLE: 今日推荐title
        public static final String TODAY_RECOMMENDED_TITLE = "TODAY_RECOMMENDED_TITLE";
        //ITEM_DETAILS_RECOMMENDED_TITLE: 商品详情页推荐区title
        public static final String ITEM_DETAILS_RECOMMENDED_TITLE = "ITEM_DETAILS_RECOMMENDED_TITLE";
        //超级导航URL
        public static final String SUPER_NAVIGATION_URL = "SUPER_NAVIGATION_URL";
        //分类地址
        public static final String CATEGORY_URL = "CATEGORY_URL";
        //订单找回地址
        public static final String ORDER_TRACKING = "ORDER_TRACKING";
        //大数据开关
        public static final String ORDER_BIG_DATA_PUSH_SYSTEM = "BIG_DATA_SETTINGS";
        //提现时间
        public static final String WEB_WITHDRAW_TIME = "WEB_WITHDRAW_TIME";
    }

    public static class BigData {
        public static final int NORMAL_SEARCH = 0;
        public static final int SUPER_SEARCH = 1;
        public static final String AD_A = "A";
        public static final String AD_B = "B";
        public static final String AD_C = "C";
    }

    public static class mainPage {
        public static final int HOME = 0;
        public static final int SUPER_NAVIGATION = 1;
        //修改为会员页面
        public static final int NUMBER = 2;
        public static final int CIRCLE = 3;
        public static final int MINE = 4;

    }

    public static class OrderType {
        public static final int TAOBAO = 1;
        public static final int YUXUAN = 10;
        public static final int JD = 2;
        public static final int PDD = 4;
        public static final int KAOLA = 5;
        public static final int WPH = 6;


        public static final  int E_UPLIMIT_SORT_DOWN=0;//降序
        public static final  int E_UPLIMIT_SORT_UP=1;//升序

    }
}

