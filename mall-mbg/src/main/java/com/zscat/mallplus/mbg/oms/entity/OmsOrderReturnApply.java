package com.zscat.mallplus.mbg.oms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 订单退货申请
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@TableName("oms_order_return_apply")
@Data
public class OmsOrderReturnApply implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 订单id
     */
    @TableField("order_id")
    @ApiModelProperty("订单id")
    private Long orderId;

    /**
     * 退货商品id
     */
    @TableField("product_id")
    @ApiModelProperty("退货商品id")
    private Long productId;

    /**
     * 订单编号
     */
    @TableField("order_sn")
    @ApiModelProperty("订单编号")
    private String orderSn;

    /**
     * 申请时间
     */
    @TableField("create_time")
    @ApiModelProperty("申请时间")
    private Date createTime;

    /**
     * 退款金额
     */
    @TableField("return_amount")
    @ApiModelProperty("退款金额")
    private BigDecimal returnAmount;

    /**
     * 商品图片
     */
    @TableField("product_pic")
    @ApiModelProperty("商品图片")
    private String productPic;

    /**
     * 商品名称
     */
    @TableField("product_name")
    @ApiModelProperty("商品名称")
    private String productName;

    /**
     * 商品品牌
     */
    @TableField("product_brand")
    @ApiModelProperty("商品品牌")
    private String productBrand;


    /**
     * 货号
     */
    @TableField("product_sn")
    @ApiModelProperty(value = "货号")
    private String productSn;

    /**
     * 商品销售属性：颜色：红色；尺码：xl;
     */
    @TableField("product_attr")
    @ApiModelProperty("商品销售属性：颜色：红色；尺码：xl")
    private String productAttr;

    /**
     * 退货数量
     */
    @TableField("product_count")
    @ApiModelProperty("退货数量")
    private Integer productCount;

    /**
     * 商品单价
     */
    @TableField("product_price")
    @ApiModelProperty("商品单价")
    private BigDecimal productPrice;
    /**
     * 商品实际支付单价
     */
    @TableField("product_real_price")
    @ApiModelProperty("商品实际支付单价")
    private BigDecimal productRealPrice;

    @TableField("order_item_id")
    @ApiModelProperty("订单明细")
    private Long orderItemId;

    @TableField("status")
    @ApiModelProperty("单品退款状态 ：0->待处理；1->退货中；2->已完成；3->已拒绝;4-已撤销，5-寄回退款退货，6-已收货")
    private Integer status;
    /**
     * 售后id
     */
    @TableField("sale_id")
    @ApiModelProperty("售后id")
    private Long saleId;
}
