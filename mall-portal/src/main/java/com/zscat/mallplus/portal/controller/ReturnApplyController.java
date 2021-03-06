package com.zscat.mallplus.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnApplyService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnReasonService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.utils.JsonUtil;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnApply;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnReason;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.vo.OmsReturnParam;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.IdGeneratorUtil;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import com.zscat.mallplus.portal.single.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 售后，退款退货controller
 */
@Slf4j
@Controller
@Api(tags = "ReturnApplyController", description = "退款售后")
@RequestMapping("/api/return")
public class ReturnApplyController extends ApiBaseAction {

  @Autowired
  private IOmsOrderReturnApplyService iOmsOrderReturnApplyService;
  @Autowired
  private IOmsOrderReturnReasonService iOmsOrderReturnReasonService;
  @Autowired
  private IUmsMemberService umsMemberService;
  @Autowired
  private IOmsOrderReturnSaleService iOmsOrderReturnSaleService;
  @Autowired
  private IOmsOrderService iOmsOrderService;
  @Autowired
  private IOmsOrderItemService omsOrderItemService;

  @ApiOperation("保存退款售后申请")
  @PostMapping(value = "/saveOrUpdate")
  @ResponseBody
  public CommonResult saveOmsOrderReturnApply(@RequestBody @ApiParam("退款售后申请参数") OmsReturnParam omsReturnParam) {
    try {
      //售后的列表
      OmsOrderReturnSale omsOrderReturnSale = omsReturnParam.getOmsOrderReturnSale();
      //订单id
      Long orderId = omsOrderReturnSale.getOrderId();
      OmsOrder omsOrder = iOmsOrderService.getById(orderId);
      Map<Long,OmsOrderItem> omsOrderItemMap = omsOrderItemService
        .list(new QueryWrapper<OmsOrderItem>().lambda().eq(OmsOrderItem::getOrderId, orderId)).stream().collect(
          Collectors.toMap(OmsOrderItem::getId, Function.identity()));
      if (omsOrder != null && omsOrder.getStatus().equals(MagicConstant.ORDER_STATUS_YET_SEND)) {
        omsOrderReturnSale.setType(MagicConstant.RETURN_APPLY_TYPE_AFTER_SALE);
      }
      omsOrderReturnSale.setUpdateTime(new Date());
      omsOrderReturnSale.setMemberId(UserUtils.getCurrentUmsMember().getId());
     // omsOrderReturnSale.setMemberUsername(UserUtils.getCurrentUmsMember().getUsername());
      if (omsOrderReturnSale.getId() == null) {
        omsOrderReturnSale.setCreateTime(new Date());
        omsOrderReturnSale.setId(IdGeneratorUtil.getIdGeneratorUtil().nextId());
      }
      omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_WAIT_DEAL);
      BigDecimal returnAmount = BigDecimal.ZERO;
      if (iOmsOrderReturnSaleService.saveOrUpdate(omsOrderReturnSale)) {
        iOmsOrderReturnApplyService.remove(new QueryWrapper<OmsOrderReturnApply>().eq("sale_id", omsOrderReturnSale.getId()));
        //售后对应的商品列表
        List<OmsOrderReturnApply> omsOrderReturnApplies = omsReturnParam.getOmsOrderReturnApplies();
        if (!CollectionUtils.isEmpty(omsOrderReturnApplies)) {
          omsOrderReturnApplies.forEach(omsOrderReturnApply->{
            omsOrderReturnApply.setSaleId(omsOrderReturnSale.getId());
            omsOrderReturnApply.setCreateTime(new Date());
            omsOrderReturnApply.setOrderId(orderId);
            omsOrderReturnApply.setOrderSn(omsOrder.getOrderSn());
            omsOrderReturnApply.setStatus(omsOrderReturnSale.getStatus());
          });
          returnAmount = dealOmsOrderReturnApply(omsOrderReturnApplies,omsOrderItemMap);
          iOmsOrderReturnApplyService.saveOrUpdateBatch(omsOrderReturnApplies);
        }
      }
      omsOrderReturnSale.setReturnAmount(returnAmount);
      iOmsOrderReturnSaleService.saveOrUpdate(omsOrderReturnSale);
      return new CommonResult<>().success("操作成功");
    } catch (Exception e) {
      log.error("保存订单退货申请：%s", e.getMessage(), e);
      return new CommonResult().failed("保存退款和退货申请异常：异常信息" + e.getMessage());
    }
  }

  @ApiOperation("查询退货原因列表")
  @PostMapping(value = "/listOmsOrderReturnReason")
  @ResponseBody
  public Object listOmsOrderReturnReason() {
    List<OmsOrderReturnReason> list = iOmsOrderReturnReasonService
      .list(new QueryWrapper<OmsOrderReturnReason>().eq("status", MagicConstant.RETURN_STATUS_ON));

    if (!CollectionUtils.isEmpty(list)) {
      return new CommonResult<>().success(list);
    }
    return new CommonResult().failed();
  }

  @ApiOperation("查询/退货申请列表")
  @PostMapping(value = "/listOmsOrderReturnApply")
  @ResponseBody
  public CommonResult<List<OmsReturnParam>> listOmsOrderReturnApply() {

    UmsMember umsMember = umsMemberService.getCurrentMember();
    List<OmsReturnParam> omsReturnParams = null;
    List<OmsOrderReturnSale> omsOrderReturnSales = iOmsOrderReturnSaleService
      .list(new QueryWrapper<OmsOrderReturnSale>().eq("member_id", umsMember.getId()));
    if (!CollectionUtils.isEmpty(omsOrderReturnSales)) {
      omsReturnParams = new ArrayList<>();
      for (OmsOrderReturnSale omsOrderReturnSale : omsOrderReturnSales) {
        OmsReturnParam omsReturnParam = new OmsReturnParam();
        omsReturnParam.setOmsOrderReturnSale(omsOrderReturnSale);
        List<OmsOrderReturnApply> omsOrderReturnApplies = iOmsOrderReturnApplyService
          .list(new QueryWrapper<OmsOrderReturnApply>().eq("sale_id", omsOrderReturnSale.getId()));
        if (!CollectionUtils.isEmpty(omsOrderReturnApplies)) {
          omsReturnParam.setOmsOrderReturnApplies(omsOrderReturnApplies);
        }
        omsReturnParams.add(omsReturnParam);
      }
    }
    if (!CollectionUtils.isEmpty(omsReturnParams)) {
      return new CommonResult<List<OmsReturnParam>>().success(omsReturnParams);
    }
    return new CommonResult().failed();
  }

  @ApiOperation("查询订单的售后状态")
  @PostMapping(value = "/querySaleStatus")
  @ResponseBody
  public Object queryReturnSaleDetail(@ApiParam("订单id") Long ordId) {
    try {
      OmsReturnParam omsReturnParam = new OmsReturnParam();
      OmsOrderReturnSale omsOrderReturnSale = iOmsOrderReturnSaleService
        .getOne(new QueryWrapper<OmsOrderReturnSale>().eq("order_id", ordId).eq("member_id", UserUtils.getCurrentUmsMember().getId()));
      if (omsOrderReturnSale != null) {
        List<OmsOrderReturnApply> omsOrderReturnApplyList = iOmsOrderReturnApplyService
          .list(new QueryWrapper<OmsOrderReturnApply>().eq("sale_id", omsOrderReturnSale.getId()));
        omsReturnParam.setOmsOrderReturnApplies(omsOrderReturnApplyList);
      }
      omsReturnParam.setOmsOrderReturnSale(omsOrderReturnSale);
      return new CommonResult<>().success(omsReturnParam);
    } catch (Exception e) {
      return new CommonResult().failed("查询订单的售后状态");
    }
  }

  @ApiOperation("修改退款/退货的接口的状态")
  @PostMapping(value = "/updateSaleStatus")
  @ResponseBody
  public Object updateSaleStatus(@ApiParam("售后id") Long saleId, @ApiParam("退款/退货状态") Integer status,
    @ApiParam("物流公司") String deliveryCompany,
    @ApiParam("物流单号") String deliverySn) {
    try {
      OmsOrderReturnSale omsOrderReturnSale = new OmsOrderReturnSale();
      omsOrderReturnSale.setId(saleId);
      if (!StringUtils.isEmpty(deliverySn)) {
        omsOrderReturnSale.setDeliverySn(deliverySn);
      }
      if (!StringUtils.isEmpty(deliveryCompany)) {
        omsOrderReturnSale.setDeliveryCompany(deliveryCompany);
      }
      omsOrderReturnSale.setStatus(status);
      iOmsOrderReturnSaleService.updateById(omsOrderReturnSale);
      return new CommonResult<>().success("修改退款/退货状态成功");
    } catch (Exception e) {
      return new CommonResult().failed("失败");
    }
  }


  /**
   * 处理退款退货申请
   */
  private BigDecimal dealOmsOrderReturnApply(List<OmsOrderReturnApply> entitys, Map<Long, OmsOrderItem> omsOrderItemMap) {
    BigDecimal returnAmountTotal = BigDecimal.ZERO;
    for (OmsOrderReturnApply omsOrderReturnApply : entitys) {
      OmsOrderItem omsOrderItem = omsOrderItemMap.get(omsOrderReturnApply.getOrderItemId());
      BigDecimal productRealPrice = omsOrderReturnApply.getProductPrice();
      if(omsOrderItem!=null){
        productRealPrice=omsOrderItem.getRealAmount();
        omsOrderReturnApply.setProductRealPrice(productRealPrice);
        omsOrderReturnApply.setProductPrice(omsOrderItem.getProductPrice());
      }
      BigDecimal returnAmount=productRealPrice.multiply(new BigDecimal(omsOrderReturnApply.getProductCount()));
      omsOrderReturnApply.setReturnAmount(returnAmount);
      returnAmountTotal = returnAmountTotal.add(returnAmount);
    }
    return returnAmountTotal;
  }

}
