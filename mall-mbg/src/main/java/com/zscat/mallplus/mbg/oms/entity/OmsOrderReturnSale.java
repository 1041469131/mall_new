package com.zscat.mallplus.mbg.oms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@ApiModel("订单售后对象")
@Data
public class OmsOrderReturnSale implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
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

    /**退货实际金额*/
    @TableField("real_return_amount")
    @ApiModelProperty(value = "退货实际金额")
    private BigDecimal realReturnAmount;

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
    @ApiModelProperty(value = "售后的状态 0->待处理；1->退货中（退货待寄回）；2->已完成；3->已拒绝 4-已撤销 5-寄回退货（已寄回待验收）,6-已收货 7-等待退款 8-退款失败")
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
}