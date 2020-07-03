package com.zscat.mallplus.mbg.oms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("分佣流水")
@TableName("oms_matcher_commission_item")
public class OmsMatcherCommissionItem implements Serializable {
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;
  /**
   * 搭配师id
   */
  @TableField("matcher_user_id")
  @ApiModelProperty(value = "搭配师id")
  private Long matcherUserId;

  /**
   * 订单id
   */
  @TableField("order_id")
  @ApiModelProperty(value = "订单id")
  private Long orderId;

  /**
   * 分佣类型 0佣金 1邀请
   */
  @TableField("profit_type")
  @ApiModelProperty(value = "分佣类型 0佣金 1邀请")
  private String profitType;

  /**
   * 状态
   */
  @TableField("status")
  @ApiModelProperty(value = "状态  1 可用 0 退款(不可用)")
  private String status;

  private Date updateDate;

  private Date createDate;

  /**
   * 分佣金额
   */
  @TableField("profit")
  @ApiModelProperty(value = "分佣金额")
  private BigDecimal profit;

  /**
   * 关联的明细
   */
  @TableField("order_item_id")
  @ApiModelProperty(value = "关联的明细")
  private Long orderItemId;

  /**
   * 产品id
   */
  @TableField("product_id")
  @ApiModelProperty(value = "产品id")
  private Long productId;

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

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public BigDecimal getProfit() {
    return profit;
  }

  public void setProfit(BigDecimal profit) {
    this.profit = profit;
  }

  public Long getOrderItemId() {
    return orderItemId;
  }

  public void setOrderItemId(Long orderItemId) {
    this.orderItemId = orderItemId;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName());
    sb.append(" [");
    sb.append("Hash = ").append(hashCode());
    sb.append(", id=").append(id);
    sb.append(", matcherUserId=").append(matcherUserId);
    sb.append(", orderId=").append(orderId);
    sb.append(", profitType=").append(profitType);
    sb.append(", status=").append(status);
    sb.append(", updateDate=").append(updateDate);
    sb.append(", createDate=").append(createDate);
    sb.append(", profit=").append(profit);
    sb.append(", orderItemId=").append(orderItemId);
    sb.append(", productId=").append(productId);
    sb.append(", serialVersionUID=").append(serialVersionUID);
    sb.append("]");
    return sb.toString();
  }
}