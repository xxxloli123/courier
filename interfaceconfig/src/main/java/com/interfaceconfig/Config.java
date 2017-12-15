package com.interfaceconfig;

public interface Config {
    String LOCAL_HOST = "http://192.168.0.105:8080/";
    String HOST = "http://www.zsh7.com/";
    // 登录
    String LOGIN = "slowlife/appuser/userlogin";
    // 注册
    String REGISTER = "slowlife/appuser/userregister";
    // 验证码.
    String SMS_CODE = "slowlife/share/getsmsmobile";

    /**
     * 检测更新
     */
    String UPDATE = "slowlife/share/getnewestappversion?type=androidCourier";

    /**
     * 推荐兑换余额
     * slowlife/appuser/recommendnumberexchangebalance
     */
    String RECOMMENDNUMBEREXCHANGEBALANCE = "slowlife/appuser/recommendnumberexchangebalance";

    /**
     * orderId:订单id;userId:用户id；type：Cancel 用户取消订单 Delete 用户删除订单
     * 删除/取消订单
     */
    String DELORDER = "slowlife/appuserorder/delorder";
    /**
     * 账户信息
     */
    String ACCOUNT = "slowlife/appuser/getuseraccount";
    /**
     *
     */
    String Order_Img = "slowlife/img/order/";
    /**
     * image
     */
    String IMG = HOST + "slowlife/img/order/";

    /**
     * 订单详情
     */
    String ORDER_DETAILS = "slowlife/appuserorder/queryorderid";


    /**
     * 绑定
     */
    String ORDERBIND = "slowlife/apppay/bindingaccount";
    /**
     * 提现
     */
    String CASH = "slowlife/apppay/showalipayapply";
    /**
     * 消息
     */
    String MESSAGE = "slowlife/share/getmessage";

    /**
     * 头像
     */
    String HEAD = LOCAL_HOST + "slowlife/img/userinfo/";

    /**
     * 重设密码
     */
    String SEARPWD = "slowlife/appuser/findpassword";

    /**
     * 改绑手机号
     */
    String MOTIFYPHONE = "slowlife/appuser/updatePhone";

    /**
     * 上传文件
     */
    String UPLOADFILE = "slowlife/appuser/uploaduserimgs";

    /**
     * 收货地址列表
     */
    String ADDRLIST = "slowlife/appuser/usergetaddresses";

    /**
     * 添加收货地址
     */
    String NEWADDR = "slowlife/appuser/useraddaddresses";

    /**
     * 修改收货地址
     */
    String EDITADDR = "slowlife/appuser/userupdateaddresses";

    /**
     * 删除收货地址
     */
    String DELADDR = "slowlife/appuser/userdeladdresses";


    /**
     * 添加订单
     */
    String ADDORDER = "slowlife/appuserorder/addorder";


    /**
     * 同城区域信息
     */
    String AREAINFO = "slowlife/appuserorder/createorderinformation";

    /**
     * 创建订单
     */
    String CREATEORDER = "slowlife/appuserorder/addorder";

    /**
     * 快递公司列表
     */
    String EXPRESSCOMPANY = "slowlife/appuserorder/getallexpresscompany";

    /**
     * 订单列表
     */
    String ORDERLIST = "slowlife/appuserorder/pagequeryorder";

    /**
     * 修改昵称
     */
    String MOTIFYNICKNAME = "slowlife/appuser/perfectuserinfo";


    /**
     * 天气
     */
    String WEATHER = "http://restapi.amap.com/v3/weather/weatherInfo";

    /**
     * 获取快递公司列表
     */
    String COMPANY = "slowlife/appuser/getallcompany";

    /**
     * 订单列表___快递员端
     */
    String ROBORDER = "slowlife/appuserorder/pagequeryorder";

    /**
     * 抢单
     */
    String BATTLE = "slowlife/appuserorder/postmanroborder";

    /**
     * 修改昵称
     */
    String NICKNAME = "slowlife/appuser/updatenickname";

    /**
     * 修改地址
     */
    String MOTIFYADDRESS = "slowlife/appuser/usergetaddresses";

    /**
     * 认证
     */
    String APPROVE = "slowlife/appuser/useridentityconfirm";

    /**
     * 订单详情
     */
    String ORDERDETAILS = "slowlife/appuserorder/queryorderid";

    /**
     * 快递员添加同城包裹
     */
    String ORDERTOTALCITYWIDE = "slowlife/appuserorder/ordertotalcitywide";

    /**
     * 快递员添加城际包裹
     */
    String ORDERTOTALINTERCITY = "slowlife/appuserorder/ordertotalintercity";

    /**
     * 分享
     */
    String SHARE = "/slowlife/app/recommend/receive.html?url=";

    /**
     * 下载链接
     */
    String DOWNLOAD = "slowlife/app/appupload.html";
    /**
     * 查询推荐
     */
    String RECOMMEND = "slowlife/appuser/recommend";

    /**
     * 确认送达 (订单完成)
     */
    String ORDERCOMPLETE = "slowlife/appuserorder/ordercomplete";

    /**
     * 提醒付款
     */
    String REMINDUSERPAYMENT = "slowlife/appuserorder/reminduserpayment";

    /**
     * 确认收款
     */
    String CONFIRMRECEIVABLES = "slowlife/apppay/confirmreceivables";

    /**
     * 获取评价
     */
    String EVALUATE = "slowlife/appuser/pagequeryevaluates";

    /**
     * 未付款已送达的订单短信提醒
     */
    String ALREADYDELIVERY = "slowlife/appuserorder/unpaybutcomplete";

    /**
     * 快递员取消订单
     */
    String CANCELORDER = "slowlife/appuserorder/cancelorder";

    /**
     *快递员注册时选择配送区域
     */
    String POSTSTREET = "slowlife/appuser/postmanregisterofstreet";

    /**
     *获取地区
     */
    String APPGETAREA = "slowlife/share/appgetarea";

    /**
     描述：快递员订单统计
     */
    String Get_Statistics = "slowlife/appuserorder/courierordercount";

    static class Url {
        public static String getUrl(String url) {
            return HOST + url;
        }
    }

}
