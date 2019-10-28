package com.zscat.mallplus.mbg.utils.constant;

/**
 * 魔法书常量类
 */
public class MagicConstant {

    /**角色状态 0-禁用 1-启用*/
    public final static Integer ROLE_STATUS_OFF = 0;
    public final static Integer ROLE_STATUS_ON = 1;

    /**后台用户权限表 0-禁用 1-启用*/
    public final static Integer PERMISSION_OFF = 0;
    public final static Integer PERMISSION_ON = 1;

    /*******订单的状态 0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单 6-退款失败***/
    public final static Integer ORDER_STATUS_WAIT_PAY = 0;
    public final static Integer ORDER_STATUS_WAIT_SEND = 1;
    public final static Integer ORDER_STATUS_YET_SEND = 2;
    public final static Integer ORDER_STATUS_YET_DONE = 3;
    public final static Integer ORDER_STATUS_YET_SHUTDOWN = 4;
    public final static Integer ORDER_STATUS_INVALID_ORDER = 5;
    public final static Integer ORDER_STATUS_INVALID_REFUND_FAILD = 6;


    /*******是否是父订单 0-是 1-表示否***/
    public final static String IS_PARENT = "0";
    public final static String IS_NOT_PARENT = "1";

    /*******售后的状态 0->待处理；1->退货中；2->已完成；3->已拒绝 4-已撤销 5-寄回退货,6-已收货 7-等待退款 8-退款失败***/
    public final static Integer RETURN_STATUS_WAIT_DEAL = 0;
    public final static Integer RETURN_STATUS_REFUNDING = 1;
    public final static Integer RETURN_STATUS_FINISHED = 2;
    public final static Integer RETURN_STATUS_REFUSE = 3;
    public final static Integer RETURN_STATUS_CANCEL = 4;
    public final static Integer RETURN_STATUS_WAITSEND = 5;
    public final static Integer RETURN_STATUS_RECEIVED = 6;
    public final static Integer RETURN_STATUS_REFUND = 7;
    public final static Integer RETURN_STATUS_REFUND_FAILD = 8;

    /*******退货原因状态 0->不启用；1->启用***/
    public final static Integer RETURN_STATUS_OFF = 0;
    public final static Integer RETURN_STATUS_ON = 1;

    /*****订单来源：0->PC订单；1->app订单********/
    public final static Integer SOURCE_TYPE_PC = 0;
    public final static Integer SOURCE_TYPE_APP = 1;


    /****0->正常订单；1->秒杀订单*****/
    public final static Integer ORDER_TYPE_NORMAL = 0;
    public final static Integer ORDER_TYPE_SECKILL = 1;

    /***确认状态0->未确认；1->已确认*******/
    public final static Integer CONFIRM_NOT = 0;
    public final static Integer CONFIRM_YET = 1;

    /******删除状态 0-未删除 1-删除*********/
    public final static Integer DELETE_NOT = 0;
    public final static Integer DELETE_YET = 1;

    /*****交易方向 0-进 1-出**********/
    public final static Integer DIRECT_IN = 0;
    public final static Integer DIRECT_OUT = 1;

    /*****帐号启用状态:0->禁用；1->启用**********/
    public final static Integer ACCOUNT_STATUS_OFF = 0;
    public final static Integer ACCOUNT_STATUS_ON = 1;

    /****是否是默认地址 1-是 0-否*****/
    public final static Integer DEFAULT_STATUS_YES = 1;
    public final static Integer DEFAULT_STATUS_NO = 0;

    /****上架状态 0->下架；1->上架*****/
    public final static Integer PUBLISH_STATUS_DOWN = 0;
    public final static Integer PUBLISH_STATUS_UP = 1;

    /****审核状态：0->未审核；1->审核通过****/
    public final static Integer VERIFY_STATUS_UNVERIFY = 0;
    public final static Integer VERIFY_STATUS_VERIFYED = 1;

    /****收藏的状态 0-为收藏 1-已收藏****/
    public final static String COLLECT_STATUS_NO = "0";
    public final static String COLLECT_STATUS_YES = "1";

    /****搭配的系列 0-组合 1-系列****/
    public final static String MATCH_TYPE_COMBIN = "0";
    public final static String MATCH_TYPE_SERIES = "1";

    /****搭配库的归属类型 0-个人 1-公司****/
    public final static String MATCH_OWER_PERSON = "0";
    public final static String MATCH_OWER_COMPANY = "1";

    /****推荐的类别 0-未推荐 1-推荐****/
    public final static String RECOMMEND_TYPE_NO = "0";
    public final static String RECOMMEND_TYPE_YES = "1";

    /****1 商品 2 文章 3 搭配*****/
    public final static Integer FAVOR_TYPE_PRODUCT = 1;
    public final static Integer FAVOR_TYPE_ARTICLE = 2;
    public final static Integer FAVOR_TYPE_MATCH_LIBRARY = 3;


    /****喜欢的类型 0-不喜欢 1-喜欢*****/
    public final static String FAVOR_TYPE_DISLIKE = "0";
    public final static String FAVOR_TYPE_LIKE = "1";

    /****促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购*****/
    public final static Integer PROMOTION_TYPE_SALES = 1;
    public final static Integer PROMOTION_TYPE_MEMBER = 2;
    public final static Integer PROMOTION_TYPE_LADDER = 3;
    public final static Integer PROMOTION_TYPE_FULL_REDUCTION = 4;
    public final static Integer PROMOTION_TYPE_TIME_LIMIT = 5;


    /*******售后的类型 0-退款 1-售后***/
    public final static Integer RETURN_APPLY_TYPE_REFUND = 0;
    public final static Integer RETURN_APPLY_TYPE_AFTER_SALE = 1;

    /****购物车中选中商品的类别 1 商品详情 2 勾选购物车 3全部购物车的商品*****/
    public final static String CART_PRODUCT_TYPE_DETAIL = "1";
    public final static String CART_PRODUCT_TYPE_CHECKED = "2";
    public final static String CART_PRODUCT_TYPE_ALLCHECKED = "3";

    /*******交易流水的状态 0-失败 1-成功 ***/
    public final static Integer OMS_TRADE_STATUS_FAILD = 0;
    public final static Integer OMS_TRADE_STATUS_SUCCESS = 1;

}
