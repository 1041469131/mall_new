package com.zscat.mallplus.mbg.oms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * @author xiang.li create date 2020/6/28 description
 */
@Data
@ApiModel("售后查询参数")
public class OmsOrderSaleParam {

  @ApiModelProperty(value = "订单编号")
  private String orderSn;

  @ApiModelProperty("开始时间")
  private Date startDate;

  @ApiModelProperty("结束时间")
  private Date endDate;

  @ApiModelProperty("产品名称")
  private String productName;

  @ApiModelProperty(value = "申请状态：0->待处理；1->退货中；2->已完成；3->已拒绝;4-已撤销，5-寄回退款退货，6-已收货")
  private Integer status;

  @ApiModelProperty(value = "类型 0-退款 1-售后")
  private Integer type;

  @ApiModelProperty(value = "物流单号")
  private String deliverySn;

  @ApiModelProperty("页码")
  private Integer pageNum=1;

  @ApiModelProperty("页的总数据")
  private Integer pageSize=5;

  @ApiModelProperty("售后编号")
  private Long id;


}
