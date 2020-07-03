package com.zscat.mallplus.mbg.oms.vo;

import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author xiang.li create date 2020/6/29 description
 */
@Data
@ApiModel("详情")
public class OmsReturnSaleDetail {

  @ApiModelProperty("订单退货商品")
  private List<OmsOrderReturnApplyVO> omsOrderReturnApplyVOs;

  @ApiModelProperty("订单")
  private OmsOrder omsOrder;

  @ApiModelProperty("订单退款")
  private OmsOrderReturnSale omsOrderReturnSale;


}
