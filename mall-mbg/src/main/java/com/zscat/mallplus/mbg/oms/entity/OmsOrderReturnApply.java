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

/**
 * <p>
 * 订单退货申请
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@TableName("oms_order_return_apply")
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

    /**
     * 售后id
     */
    @TableField("sale_id")
    @ApiModelProperty("售后id")
    private Long saleId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductAttr() {
        return productAttr;
    }

    public void setProductAttr(String productAttr) {
        this.productAttr = productAttr;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getProductRealPrice() {
        return productRealPrice;
    }

    public void setProductRealPrice(BigDecimal productRealPrice) {
        this.productRealPrice = productRealPrice;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }
}
