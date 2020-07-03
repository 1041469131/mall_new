package com.zscat.mallplus.mbg.oms.vo;

import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnApply;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author xiang.li create date 2020/6/29 description
 */
@Data
public class OmsOrderReturnSaleVO {

  @ApiModelProperty("订单退款")
  private OmsOrderReturnSale omsOrderReturnSale;

  @ApiModelProperty("订单退款商品详情")
  private List<OmsOrderReturnApply> omsOrderReturnApplyList;


}
