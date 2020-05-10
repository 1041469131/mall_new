package com.zscat.mallplus.mbg.oms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("oms_matcher_commission")
public class OmsMatcherCommission implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**搭配师id*/
    @TableField("matcher_user_id")
    @ApiModelProperty(value = "搭配师id")
    private Long matcherUserId;

    /**订单id*/
    @TableField("order_id")
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**收益的类型 0-佣金 1-邀请*/
    @TableField("profit_type")
    @ApiModelProperty(value = "收益的类型 0-佣金 1-邀请")
    private String profitType;

    /**收益*/
    @TableField("profit")
    @ApiModelProperty(value = "收益")
    private BigDecimal profit;

    /**状态*/
    @TableField("status")
    @ApiModelProperty(value = "状态0-待结算 1-待结算（部分退款）2-已人工结算3-已人工结算（部分退款）4-不结算（全退款）")
    private String status;

    /**更新日期*/
    @TableField("update_date")
    @ApiModelProperty(value = "更新日期")
    private Date updateDate;

    /**更新时间（时间戳）*/
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间（时间戳）")
    private Long updateTime;

    /**创建日期*/
    @TableField("create_date")
    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    /**创建时间（时间戳）*/
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间（时间戳）")
    private Long createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatcherUserId() {
        return matcherUserId;
    }

    public void setMatcherUserId(Long matcherUserId) {
        this.matcherUserId = matcherUserId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProfitType() {
        return profitType;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}