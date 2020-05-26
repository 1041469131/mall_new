package com.zscat.mallplus.mbg.oms.vo;

import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * @author xiang.li create date 2020/5/25 description
 */
@ApiModel("订单返回值对象")
public class OrderResult extends OmsOrder {

  @ApiModelProperty("搭配师")
  private SysUser sysUser;

  @ApiModelProperty("会员")
  private UmsMember umsMember;

  @ApiModelProperty("售后状态")
  private OmsOrderReturnSale omsOrderReturnSale;

  @ApiModelProperty("订单商品")
  private List<OmsOrderItem> omsOrderItemList;
  @ApiModelProperty("商品sku")
  private List<PmsSkuStock>  pmsSkuStockList;

  public SysUser getSysUser() {
    return sysUser;
  }

  public OrderResult setSysUser(SysUser sysUser) {
    this.sysUser = sysUser;
    return this;
  }

  public UmsMember getUmsMember() {
    return umsMember;
  }

  public OrderResult setUmsMember(UmsMember umsMember) {
    this.umsMember = umsMember;
    return this;
  }

  public OmsOrderReturnSale getOmsOrderReturnSale() {
    return omsOrderReturnSale;
  }

  public OrderResult setOmsOrderReturnSale(OmsOrderReturnSale omsOrderReturnSale) {
    this.omsOrderReturnSale = omsOrderReturnSale;
    return this;
  }

  public List<OmsOrderItem> getOmsOrderItemList() {
    return omsOrderItemList;
  }

  public OrderResult setOmsOrderItemList(List<OmsOrderItem> omsOrderItemList) {
    this.omsOrderItemList = omsOrderItemList;
    return this;
  }

  public List<PmsSkuStock> getPmsSkuStockList() {
    return pmsSkuStockList;
  }

  public OrderResult setPmsSkuStockList(List<PmsSkuStock> pmsSkuStockList) {
    this.pmsSkuStockList = pmsSkuStockList;
    return this;
  }
}
