package com.zscat.mallplus.mbg.oms.vo;

import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnApply;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xiang.li create date 2020/6/29 description
 */
@Data
@ApiModel("订单退货商品")
public class OmsOrderReturnApplyVO extends OmsOrderReturnApply {
  @ApiModelProperty("订单状态")
  private Integer OrderStatus;

  @ApiModelProperty(value = "类型 0-退款 1-售后")
  private Integer type;
}
