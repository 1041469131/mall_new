package com.zscat.mallplus.mbg.oms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OmsOrderReturnSale implements Serializable {
    private Long id;

    /**订单id*/
    @TableField("order_id")
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**收货地址表id*/
    @TableField("company_address_id")
    @ApiModelProperty(value = "收货地址表id")
    private Long companyAddressId;

    /**订单编号*/
    @TableField("order_sn")
    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    /**用户id*/
    @TableField("member_id")
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**用户名*/
    @TableField("member_username")
    @ApiModelProperty(value = "用户名")
    private String memberUsername;

    /**退货金额*/
    @TableField("return_amount")
    @ApiModelProperty(value = "退货金额")
    private BigDecimal returnAmount;

    /**退货人姓名*/
    @TableField("return_name")
    @ApiModelProperty(value = "退货人姓名")
    private String returnName;

    /**退货人电话*/
    @TableField("return_phone")
    @ApiModelProperty(value = "退货人电话")
    private String returnPhone;

    /**申请状态：0->待处理；1->退货中；2->已完成；3->已拒绝;4-已撤销，5-寄回退款退货，6-已收货*/
    @TableField("status")
    @ApiModelProperty(value = "申请状态：0->待处理；1->退货中；2->已完成；3->已拒绝;4-已撤销，5-寄回退款退货，6-已收货")
    private Integer status;

    /**原因*/
    @TableField("reason")
    @ApiModelProperty(value = "原因")
    private String reason;

    /**描述*/
    @TableField("description")
    @ApiModelProperty(value = "描述")
    private String description;

    /**凭证图片，以逗号隔开*/
    @TableField("proof_pics")
    @ApiModelProperty(value = "凭证图片，以逗号隔开")
    private String proofPics;

    /**处理备注*/
    @TableField("handle_note")
    @ApiModelProperty(value = "处理备注")
    private String handleNote;

    /**处理人*/
    @TableField("handle_man")
    @ApiModelProperty(value = "处理人")
    private String handleMan;

    /**收货人姓名*/
    @TableField("receive_man")
    @ApiModelProperty(value = "收货人姓名")
    private String receiveMan;

    /**收货备注*/
    @TableField("receive_note")
    @ApiModelProperty(value = "收货备注")
    private String receiveNote;

    /**物流公司*/
    @TableField("delivery_company")
    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;

    /**物流单号*/
    @TableField("delivery_sn")
    @ApiModelProperty(value = "物流单号")
    private String deliverySn;

    /**更新时间*/
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**创建时间*/
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**收货时间*/
    @TableField("receive_time")
    @ApiModelProperty(value = "收货时间")
    private Date receiveTime;

    /**类型 0-退款 1-售后*/
    @TableField("type")
    @ApiModelProperty(value = "类型 0-退款 1-售后")
    private Integer type;

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

    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
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

    public String getHandleNote() {
        return handleNote;
    }

    public void setHandleNote(String handleNote) {
        this.handleNote = handleNote;
    }

    public String getHandleMan() {
        return handleMan;
    }

    public void setHandleMan(String handleMan) {
        this.handleMan = handleMan;
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

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", companyAddressId=").append(companyAddressId);
        sb.append(", orderSn=").append(orderSn);
        sb.append(", memberId=").append(memberId);
        sb.append(", memberUsername=").append(memberUsername);
        sb.append(", returnAmount=").append(returnAmount);
        sb.append(", returnName=").append(returnName);
        sb.append(", returnPhone=").append(returnPhone);
        sb.append(", status=").append(status);
        sb.append(", reason=").append(reason);
        sb.append(", description=").append(description);
        sb.append(", proofPics=").append(proofPics);
        sb.append(", handleNote=").append(handleNote);
        sb.append(", handleMan=").append(handleMan);
        sb.append(", receiveMan=").append(receiveMan);
        sb.append(", receiveNote=").append(receiveNote);
        sb.append(", deliveryCompany=").append(deliveryCompany);
        sb.append(", deliverySn=").append(deliverySn);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", receiveTime=").append(receiveTime);
        sb.append(", type=").append(type);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}