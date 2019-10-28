package com.zscat.mallplus.mbg.oms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("oms_order_return_sale")
public class OmsOrderReturnSale implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**订单id*/
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**收货地址表id*/
    @ApiModelProperty(value = "收货地址表id")
    private Long companyAddressId;

    /**订单编号*/
    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    /**用户id*/
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**会员用户名*/
    @ApiModelProperty("会员用户名")
    private String memberUsername;

    /**退货金额*/
    @ApiModelProperty(value = "退货金额")
    private BigDecimal returnAmount;

    /**退货人姓名*/
    @ApiModelProperty(value = "退货人姓名")
    private String returnName;

    /**退货人电话*/
    @ApiModelProperty(value = "退货人电话")
    private String returnPhone;

    /**申请状态：0->待处理；1->退货中；2->已完成；3->已拒绝*/
    @ApiModelProperty(value = "申请状态：0->待处理；1->退货中；2->已完成；3->已拒绝")
    private Integer status;

    /**原因*/
    @ApiModelProperty(value = "原因")
    private String reason;

    /**描述*/
    @ApiModelProperty(value = "描述")
    private String description;

    /**凭证图片，以逗号隔开*/
    @ApiModelProperty(value = "凭证图片，以逗号隔开")
    private String proofPics;

    /**收货人姓名*/
    @ApiModelProperty(value = "收货人姓名")
    private String receiveMan;

    /**收货备注*/
    @ApiModelProperty(value = "收货备注")
    private String receiveNote;

    /**物流公司*/
    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;

    /**物流单号*/
    @ApiModelProperty(value = "物流单号")
    private String deliverySn;

    /**更新时间*/
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "类型 0-退款 1-售后")
    private Integer type;

    @ApiModelProperty(value = "处理人")
    private String handleMan;

    @ApiModelProperty(value = "处理备注")
    private String handleNote;

    @ApiModelProperty(value = "收货时间")
    private Date receiveTime;


    private static final long serialVersionUID = 1L;

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

    public Long getCompanyAddressId() {
        return companyAddressId;
    }

    public void setCompanyAddressId(Long companyAddressId) {
        this.companyAddressId = companyAddressId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getReturnName() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName;
    }

    public String getReturnPhone() {
        return returnPhone;
    }

    public void setReturnPhone(String returnPhone) {
        this.returnPhone = returnPhone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProofPics() {
        return proofPics;
    }

    public void setProofPics(String proofPics) {
        this.proofPics = proofPics;
    }

    public String getReceiveMan() {
        return receiveMan;
    }

    public void setReceiveMan(String receiveMan) {
        this.receiveMan = receiveMan;
    }

    public String getReceiveNote() {
        return receiveNote;
    }

    public void setReceiveNote(String receiveNote) {
        this.receiveNote = receiveNote;
    }

    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }

    public String getDeliverySn() {
        return deliverySn;
    }

    public void setDeliverySn(String deliverySn) {
        this.deliverySn = deliverySn;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public String getHandleMan() {
        return handleMan;
    }

    public void setHandleMan(String handleMan) {
        this.handleMan = handleMan;
    }

    public String getHandleNote() {
        return handleNote;
    }

    public void setHandleNote(String handleNote) {
        this.handleNote = handleNote;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }
}