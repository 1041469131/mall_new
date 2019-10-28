package com.zscat.mallplus.mbg.oms.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OmsOrderTrade implements Serializable {

    private Long id;

    /**订单id*/
    @ApiModelProperty(value = "订单id")
    private Long ordId;

    /**订单编号*/
    @ApiModelProperty(value = "订单编号")
    private String orderCn;

    /**售后的id*/
    @ApiModelProperty(value = "售后的id")
    private Long returnApplyId;

    /**父订单号*/
    @ApiModelProperty(value = "父订单号")
    private Long supplyId;

    /**交易的金额*/
    @ApiModelProperty(value = "交易的金额")
    private BigDecimal amount;

    /**交易的方向 0-进 1-出*/
    @ApiModelProperty(value = "交易的方向 0-进 1-出")
    private Integer direct;

    /**微信返回的字段*/
    @ApiModelProperty(value = "微信返回的字段")
    private String prepayId;

    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /*****交易流水的状态 0-失败 1-成功****/
    @ApiModelProperty(value = "交易流水的状态 0-失败 1-成功")
    private Integer status;

    /*****返回的消息****/
    @ApiModelProperty(value = "返回的消息")
    private String responseMsg;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrdId() {
        return ordId;
    }

    public void setOrdId(Long ordId) {
        this.ordId = ordId;
    }

    public String getOrderCn() {
        return orderCn;
    }

    public void setOrderCn(String orderCn) {
        this.orderCn = orderCn;
    }

    public Long getReturnApplyId() {
        return returnApplyId;
    }

    public void setReturnApplyId(Long returnApplyId) {
        this.returnApplyId = returnApplyId;
    }

    public Long getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Long supplyId) {
        this.supplyId = supplyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getDirect() {
        return direct;
    }

    public void setDirect(Integer direct) {
        this.direct = direct;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}