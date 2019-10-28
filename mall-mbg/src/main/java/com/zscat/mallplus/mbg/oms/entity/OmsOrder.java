package com.zscat.mallplus.mbg.oms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Data
@TableName("oms_order")
public class OmsOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    List<OmsOrderItem> orderItemList ;
    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("member_id")
    @ApiModelProperty("会员id")
    private Long memberId;

    @TableField("coupon_id")
    private Long couponId;

    /**
     * 订单编号
     */
    @TableField("order_sn")
    @ApiModelProperty("订单编号")
    private String orderSn;

    /**
     * 提交时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 用户帐号
     */
    @TableField("member_username")
    private String memberUsername;

    /**
     * 订单总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 应付金额（实际支付金额）
     */
    @TableField("pay_amount")
    private BigDecimal payAmount;

    /**
     * 运费金额
     */
    @TableField("freight_amount")
    private BigDecimal freightAmount;

    /**
     * 促销优化金额（促销价、满减、阶梯价）
     */
    @TableField("promotion_amount")
    private BigDecimal promotionAmount;

    /**
     * 积分抵扣金额
     */
    @TableField("integration_amount")
    private BigDecimal integrationAmount;

    /**
     * 优惠券抵扣金额
     */
    @TableField("coupon_amount")
    private BigDecimal couponAmount;

    /**
     * 管理员后台调整订单使用的折扣金额
     */
    @TableField("discount_amount")
    private BigDecimal discountAmount;

    /**
     * 支付方式：0->未支付；1->支付宝；2->微信
     */
    @TableField("pay_type")
    @ApiModelProperty("支付方式：0->未支付；1->支付宝；2->微信")
    private Integer payType;

    /**
     * 订单来源：0->PC订单；1->app订单
     */
    @TableField("source_type")
    @ApiModelProperty("订单来源：0->PC订单；1->app订单")
    private Integer sourceType;

    /**
     * 订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
     */
    @ApiModelProperty("订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;

    /**
     * 订单类型：0->正常订单；1->秒杀订单
     */
    @TableField("order_type")
    private Integer orderType;

    /**
     * 物流公司(配送方式)
     */
    @TableField("delivery_company")
    @ApiModelProperty("物流公司(配送方式)")
    private String deliveryCompany;

    /**
     * 物流单号
     */
    @TableField("delivery_sn")
    @ApiModelProperty("物流单号")
    private String deliverySn;

    /**
     * 自动确认时间（天）
     */
    @TableField("auto_confirm_day")
    private Integer autoConfirmDay;

    /**
     * 可以获得的积分
     */
    @ApiModelProperty("可以获得的积分")
    private Integer integration;

    /**
     * 可以活动的成长值
     */
    @ApiModelProperty("可以活动的成长值")
    private Integer growth;

    /**
     * 活动信息
     */
    @TableField("promotion_info")
    @ApiModelProperty("活动信息")
    private String promotionInfo;

    /**
     * 发票类型：0->不开发票；1->电子发票；2->纸质发票
     */
    @TableField("bill_type")
    @ApiModelProperty("发票类型：0->不开发票；1->电子发票；2->纸质发票")
    private Integer billType;

    /**
     * 发票抬头
     */
    @TableField("bill_header")
    private String billHeader;

    /**
     * 发票内容
     */
    @TableField("bill_content")
    private String billContent;

    /**
     * 收票人电话
     */
    @TableField("bill_receiver_phone")
    private String billReceiverPhone;

    /**
     * 收票人邮箱
     */
    @TableField("bill_receiver_email")
    private String billReceiverEmail;

    /**
     * 收货人姓名
     */
    @TableField("receiver_name")
    @ApiModelProperty("收货人姓名")
    private String receiverName;

    /**
     * 收货人电话
     */
    @TableField("receiver_phone")
    @ApiModelProperty("收货人电话")
    private String receiverPhone;

    /**
     * 收货人邮编
     */
    @TableField("receiver_post_code")
    @ApiModelProperty("收货人邮编")
    private String receiverPostCode;

    /**
     * 省份/直辖市
     */
    @TableField("receiver_province")
    @ApiModelProperty("省份/直辖市")
    private String receiverProvince;

    /**
     * 城市
     */
    @TableField("receiver_city")
    @ApiModelProperty("城市")
    private String receiverCity;

    /**
     * 区
     */
    @TableField("receiver_region")
    @ApiModelProperty("区")
    private String receiverRegion;

    /**
     * 详细地址
     */
    @TableField("receiver_detail_address")
    @ApiModelProperty("详细地址")
    private String receiverDetailAddress;

    /**
     * 订单备注
     */
    @ApiModelProperty("订单备注")
    private String note;

    /**
     * 确认收货状态：0->未确认；1->已确认
     */
    @TableField("confirm_status")
    @ApiModelProperty("确认收货状态：0->未确认；1->已确认")
    private Integer confirmStatus;

    /**
     * 删除状态：0->未删除；1->已删除
     */
    @TableField("delete_status")
    @ApiModelProperty("删除状态：0->未删除；1->已删除")
    private Integer deleteStatus;

    /**
     * 下单时使用的积分
     */
    @TableField("use_integration")
    @ApiModelProperty("下单时使用的积分")
    private Integer useIntegration;

    /**
     * 支付时间
     */
    @TableField("payment_time")
    @ApiModelProperty("支付时间")
    private Date paymentTime;

    /**
     * 发货时间
     */
    @TableField("delivery_time")
    @ApiModelProperty("发货时间")
    private Date deliveryTime;

    /**
     * 确认收货时间
     */
    @TableField("receive_time")
    @ApiModelProperty("确认收货时间")
    private Date receiveTime;

    /**
     * 评价时间
     */
    @TableField("comment_time")
    @ApiModelProperty("评价时间")
    private Date commentTime;

    /**
     * 修改时间
     */
    @TableField("modify_time")
    @ApiModelProperty("修改时间")
    private Date modifyTime;

    @TableField("prepay_id")
    private String prepayId;

    @TableField("supply_id")
    @ApiModelProperty("父订单号")
    private Long supplyId;

    @TableField("goods_id")
    private Long goodsId;

    @TableField("goods_name")
    private String goodsName;

    @TableField("school_id")
    private Long schoolId;

    @TableField("transaction_id")
    @ApiModelProperty("微信支付订单编号")
    private String transaction_id;



}
