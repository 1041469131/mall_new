package com.zscat.mallplus.mbg.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("sys_matcher_statistics")
public class SysMatcherStatistics implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**搭配师id*/
    @TableField("matcher_id")
    @ApiModelProperty(value = "搭配师id")
    private Long matcherId;

    /**会员id*/
    @TableField("member_id")
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**累计销售额*/
    @TableField("total_sale_amount")
    @ApiModelProperty(value = "累计销售额")
    private BigDecimal totalSaleAmount;

    /**累计收益*/
    @TableField("total_profit")
    @ApiModelProperty(value = "累计收益")
    private BigDecimal totalProfit;

    /**未结算金额*/
    @TableField("unsettle_profit")
    @ApiModelProperty(value = "未结算金额")
    private BigDecimal unsettleProfit;

    /**商品佣金*/
    @TableField("product_commission")
    @ApiModelProperty(value = "商品佣金")
    private BigDecimal productCommission;

    /**未结算的商品分佣*/
    @TableField("product_unsettle_commission")
    @ApiModelProperty(value = "未结算的商品分佣")
    private BigDecimal productUnsettleCommission;

    /**已结算的商品分佣*/
    @TableField("product_settle_commission")
    @ApiModelProperty(value = "已结算的商品分佣")
    private BigDecimal productSettleCommission;

    /**已结算的订单金额（商品分佣部分）*/
    @TableField("product_settle_amount")
    @ApiModelProperty(value = "已结算的订单金额（商品分佣部分）")
    private BigDecimal productSettleAmount;

    /**未结算的订单金额（商品分佣部分）*/
    @TableField("product_unsettle_amount")
    @ApiModelProperty(value = "未结算的订单金额（商品分佣部分）")
    private BigDecimal productUnsettleAmount;

    /**邀请奖励*/
    @TableField("invite_commission")
    @ApiModelProperty(value = "邀请奖励")
    private BigDecimal inviteCommission;

    /**未算的订单金额（邀请奖励部分）*/
    @TableField("invite_unsettle_amount")
    @ApiModelProperty(value = "未算的订单金额（邀请奖励部分）")
    private BigDecimal inviteUnsettleAmount;

    /**已经算的订单金额（邀请奖励部分）*/
    @TableField("invite_settle_amount")
    @ApiModelProperty(value = "已经算的订单金额（邀请奖励部分）")
    private BigDecimal inviteSettleAmount;

    /**未结算的邀请奖励*/
    @TableField("invite_unsettle_commission")
    @ApiModelProperty(value = "未结算的邀请奖励")
    private BigDecimal inviteUnsettleCommission;

    /**已结算的邀请奖励*/
    @TableField("invite_settle_commission")
    @ApiModelProperty(value = "已结算的邀请奖励")
    private BigDecimal inviteSettleCommission;

    private Integer fanCount;

    /**邀请的搭配师数量*/
    @TableField("invite_count")
    @ApiModelProperty(value = "邀请的搭配师数量")
    private Integer inviteCount;

    /**更新时间*/
    @TableField("update_date")
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    /**更新时间*/
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;

    /**创建时间*/
    @TableField("create_date")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    /**创建时间*/
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatcherId() {
        return matcherId;
    }

    public void setMatcherId(Long matcherId) {
        this.matcherId = matcherId;
    }

    public BigDecimal getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public void setTotalSaleAmount(BigDecimal totalSaleAmount) {
        this.totalSaleAmount = totalSaleAmount;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getUnsettleProfit() {
        return unsettleProfit;
    }

    public void setUnsettleProfit(BigDecimal unsettleProfit) {
        this.unsettleProfit = unsettleProfit;
    }

    public BigDecimal getProductCommission() {
        return productCommission;
    }

    public void setProductCommission(BigDecimal productCommission) {
        this.productCommission = productCommission;
    }

    public BigDecimal getProductUnsettleCommission() {
        return productUnsettleCommission;
    }

    public void setProductUnsettleCommission(BigDecimal productUnsettleCommission) {
        this.productUnsettleCommission = productUnsettleCommission;
    }

    public BigDecimal getProductSettleCommission() {
        return productSettleCommission;
    }

    public void setProductSettleCommission(BigDecimal productSettleCommission) {
        this.productSettleCommission = productSettleCommission;
    }

    public BigDecimal getProductSettleAmount() {
        return productSettleAmount;
    }

    public void setProductSettleAmount(BigDecimal productSettleAmount) {
        this.productSettleAmount = productSettleAmount;
    }

    public BigDecimal getProductUnsettleAmount() {
        return productUnsettleAmount;
    }

    public void setProductUnsettleAmount(BigDecimal productUnsettleAmount) {
        this.productUnsettleAmount = productUnsettleAmount;
    }

    public BigDecimal getInviteCommission() {
        return inviteCommission;
    }

    public void setInviteCommission(BigDecimal inviteCommission) {
        this.inviteCommission = inviteCommission;
    }

    public BigDecimal getInviteUnsettleAmount() {
        return inviteUnsettleAmount;
    }

    public void setInviteUnsettleAmount(BigDecimal inviteUnsettleAmount) {
        this.inviteUnsettleAmount = inviteUnsettleAmount;
    }

    public BigDecimal getInviteSettleAmount() {
        return inviteSettleAmount;
    }

    public void setInviteSettleAmount(BigDecimal inviteSettleAmount) {
        this.inviteSettleAmount = inviteSettleAmount;
    }

    public BigDecimal getInviteUnsettleCommission() {
        return inviteUnsettleCommission;
    }

    public void setInviteUnsettleCommission(BigDecimal inviteUnsettleCommission) {
        this.inviteUnsettleCommission = inviteUnsettleCommission;
    }

    public BigDecimal getInviteSettleCommission() {
        return inviteSettleCommission;
    }

    public void setInviteSettleCommission(BigDecimal inviteSettleCommission) {
        this.inviteSettleCommission = inviteSettleCommission;
    }

    public Integer getFanCount() {
        return fanCount;
    }

    public void setFanCount(Integer fanCount) {
        this.fanCount = fanCount;
    }

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}