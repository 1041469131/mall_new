package com.zscat.mallplus.admin.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.admin.po.OrderCommissionStatistics;
import com.zscat.mallplus.manage.service.logistics.IlogisticsService;
import com.zscat.mallplus.manage.service.oms.IOmsMatcherCommissionService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.manage.service.pms.IPmsBrandService;
import com.zscat.mallplus.manage.service.pms.IPmsProductService;
import com.zscat.mallplus.manage.service.sms.ISmsService;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.IUmsApplyMatcherService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.vo.OmsMoneyInfoParam;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderDeliveryParam;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderQueryParam;
import com.zscat.mallplus.mbg.oms.vo.OmsReceiverInfoParam;
import com.zscat.mallplus.mbg.oms.vo.OrderResult;
import com.zscat.mallplus.mbg.pms.entity.PmsBrand;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsApplyMatcher;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "OmsOrderController", description = "订单表管理")
@RequestMapping("/oms/OmsOrder")
public class OmsOrderController {

  @Autowired
  private IOmsOrderService omsOrderService;
  @Autowired
  private IUmsApplyMatcherService iUmsApplyMatcherService;
  @Autowired
  private ISysUserService iSysUserService;
  @Autowired
  private IlogisticsService ilogisticsService;
  @Autowired
  private IPmsProductService iPmsProductService;
  @Autowired
  private IPmsBrandService iPmsBrandService;
  @Autowired
  private IUmsMemberService umsMemberService;
  @Autowired
  private IOmsMatcherCommissionService omsMatcherCommissionService;
  @Autowired
  private ISmsService smsService;
  @SysLog(MODULE = "oms", REMARK = "根据条件查询所有订单表列表")
  @ApiOperation("根据条件查询所有订单表列表")
  @GetMapping(value = "/list")
  @PreAuthorize("hasAuthority('oms:OmsOrder:read')")
  public CommonResult<Page<OmsOrder>> getOmsOrderByPage(OmsOrder entity,
    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
  ) {
    try {
      IPage<OmsOrder> omsOrderIPage = omsOrderService
        .page(new Page<>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc("create_time"));
      return new CommonResult().success(omsOrderIPage);
    } catch (Exception e) {
      log.error("根据条件查询所有订单表列表：%s", e.getMessage(), e);
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "oms", REMARK = "根据条件查询所有订单表列表")
  @ApiOperation("根据条件查询所有订单表列表")
  @RequestMapping(value = "/pageList",method = RequestMethod.POST)
  @IgnoreAuth
  //@PreAuthorize("hasAuthority('oms:OmsOrder:read')")
  public CommonResult<Page<OrderResult>> getOrderByPage(@RequestBody @ApiParam("订单查询条件") OmsOrderQueryParam queryParam) {
    try {
      Page<OrderResult> orderResultPage = omsOrderService.listOmsOrderByPage(queryParam);
      return new CommonResult().success(orderResultPage);
    } catch (Exception e) {
      log.error("根据条件查询所有订单表列表：%s", e.getMessage(), e);
    }
    return new CommonResult().failed();
  }


  @SysLog(MODULE = "oms", REMARK = "保存订单表")
  @ApiOperation("保存订单表")
  @RequestMapping(value = "/create")
  @PreAuthorize("hasAuthority('oms:OmsOrder:create')")
  public Object saveOmsOrder(@RequestBody OmsOrder entity) {
    try {
      if (omsOrderService.save(entity)) {
        return new CommonResult().success();
      }
    } catch (Exception e) {
      log.error("保存订单表：%s", e.getMessage(), e);
      return new CommonResult().failed();
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "oms", REMARK = "更新订单表")
  @ApiOperation("更新订单表")
  @RequestMapping(value = "/update/{id}")
  @PreAuthorize("hasAuthority('oms:OmsOrder:update')")
  public Object updateOmsOrder(@RequestBody OmsOrder entity) {
    try {
      if (omsOrderService.updateById(entity)) {
        return new CommonResult().success();
      }
    } catch (Exception e) {
      log.error("更新订单表：%s", e.getMessage(), e);
      return new CommonResult().failed();
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "oms", REMARK = "删除订单表")
  @ApiOperation("删除订单表")
  @RequestMapping(value = "/delete/{id}")
  @PreAuthorize("hasAuthority('oms:OmsOrder:delete')")
  public Object deleteOmsOrder(@ApiParam("订单表id") @PathVariable Long id) {
    try {
      if (ValidatorUtils.empty(id)) {
        return new CommonResult().paramFailed("订单表id");
      }
      if (omsOrderService.removeById(id)) {
        return new CommonResult().success();
      }
    } catch (Exception e) {
      log.error("删除订单表：%s", e.getMessage(), e);
      return new CommonResult().failed();
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "oms", REMARK = "给订单表分配订单表")
  @ApiOperation("查询订单表明细")
  @RequestMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('oms:OmsOrder:read')")
  public CommonResult<OrderResult> getOmsOrderById(@ApiParam("订单表id") @PathVariable Long id) {
    try {
      if (ValidatorUtils.empty(id)) {
        return new CommonResult().paramFailed("订单表id");
      }
      OmsOrderQueryParam queryParam=new OmsOrderQueryParam();
      queryParam.setOrderId(id);
      Page<OrderResult> orderResultPage = omsOrderService.listOmsOrderByPage(queryParam);
      List<OrderResult> records = orderResultPage.getRecords();

      if(!CollectionUtils.isEmpty(records)){
        OrderResult orderResult = records.get(0);
        //设置邀请人
        if(orderResult.getSysUser()!=null) {
          UmsApplyMatcher umsApplyMatcher = iUmsApplyMatcherService
            .getOne(new QueryWrapper<UmsApplyMatcher>().eq("phone", orderResult.getSysUser().getPhone()));
          if(umsApplyMatcher!=null&&umsApplyMatcher.getAuditStatus().equals("1")) {
            String invitePhone = umsApplyMatcher.getInvitePhone();
            SysUser sysUser = iSysUserService.getOne(new QueryWrapper<SysUser>().eq("phone", invitePhone));
            orderResult.setInviteUser(sysUser);
          }
        }
        //设置品牌
        Long productId = orderResult.getOmsOrderItemList().get(0).getProductId();
        PmsProduct pmsProduct = iPmsProductService.getById(productId);
        PmsBrand pmsBrand = iPmsBrandService.getOne(new QueryWrapper<PmsBrand>().eq("id", pmsProduct.getBrandId()));
        orderResult.setPmsBrand(pmsBrand);
        return new CommonResult().success(orderResult);
      }
      return new CommonResult().success();

    } catch (Exception e) {
      log.error("查询订单表明细：%s", e.getMessage(), e);
      return new CommonResult().failed();
    }

  }

  @ApiOperation(value = "批量删除订单表")
  @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
  @ResponseBody
  @SysLog(MODULE = "pms", REMARK = "批量删除订单表")
  @PreAuthorize("hasAuthority('oms:OmsOrder:delete')")
  public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
    boolean count = omsOrderService.removeByIds(ids);
    if (count) {
      return new CommonResult().success(count);
    } else {
      return new CommonResult().failed();
    }
  }

  @SysLog(MODULE = "oms", REMARK = "批量发货")
  @ApiOperation("批量发货")
  @RequestMapping(value = "/update/delivery", method = RequestMethod.POST)
  @ResponseBody
  public Object delivery(@RequestBody List<OmsOrderDeliveryParam> deliveryParamList) {
    int count = omsOrderService.delivery(deliveryParamList);
    if (count > 0) {
      return new CommonResult().success(count);
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "oms", REMARK = "批量关闭订单")
  @ApiOperation("批量关闭订单")
  @RequestMapping(value = "/update/close", method = RequestMethod.POST)
  @ResponseBody
  public Object close(@RequestParam("ids") List<Long> ids, @RequestParam String note) {
    int count = omsOrderService.close(ids, note);
    if (count > 0) {
      return new CommonResult().success(count);
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "oms", REMARK = "修改收货人信息")
  @ApiOperation("修改收货人信息")
  @RequestMapping(value = "/update/receiverInfo", method = RequestMethod.POST)
  @ResponseBody
  public Object updateReceiverInfo(@RequestBody OmsReceiverInfoParam receiverInfoParam) {
    int count = omsOrderService.updateReceiverInfo(receiverInfoParam);
    if (count > 0) {
      return new CommonResult().success(count);
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "oms", REMARK = "修改订单费用信息")
  @ApiOperation("修改订单费用信息")
  @RequestMapping(value = "/update/moneyInfo", method = RequestMethod.POST)
  @ResponseBody
  public Object updateReceiverInfo(@RequestBody OmsMoneyInfoParam moneyInfoParam) {
    int count = omsOrderService.updateMoneyInfo(moneyInfoParam);
    if (count > 0) {
      return new CommonResult().success(count);
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "oms", REMARK = "备注订单")
  @ApiOperation("备注订单")
  @RequestMapping(value = "/update/note", method = RequestMethod.POST)
  @ResponseBody
  public Object updateNote(@RequestParam("id") Long id,
    @RequestParam("note") String note,
    @RequestParam("status") Integer status) {
    int count = omsOrderService.updateNote(id, note, status);
    if (count > 0) {
      return new CommonResult().success(count);
    }
    return new CommonResult().failed();
  }

  @IgnoreAuth
  @ApiOperation(value = "获取物流相关信息")
  @RequestMapping(value = "getLogisticsInfos", method = RequestMethod.GET)
  @ResponseBody
  public Object getLogisticsInfos(String no) {
    try {
      String respon = ilogisticsService.query(no);
      return new CommonResult().success(respon);
    }catch (Exception e){
      return new CommonResult().failed(e.getMessage());
    }
  }



  @ApiOperation(value = "首页首页统计")
  @RequestMapping(value = "getOrderStatistics", method = RequestMethod.GET)
  @ResponseBody
  public Object getOrderStatistics() {
    try {
      OrderCommissionStatistics orderCommissionStatistics=new OrderCommissionStatistics();
      Long matcherUserId = UserUtils.getCurrentMember().getId();
      List<UmsMember> list = umsMemberService.list(new QueryWrapper<UmsMember>().lambda().eq(UmsMember::getMatchUserId, matcherUserId));
      if(CollectionUtils.isEmpty(list)){
        return  new CommonResult().success(orderCommissionStatistics);
      }
      List<OmsOrder> omsOrders = omsOrderService.list(
        new QueryWrapper<OmsOrder>().lambda().in(OmsOrder::getMemberId, list.stream().map(UmsMember::getId).collect(Collectors.toList())));
      BigDecimal avgPayAmount=BigDecimal.ZERO;
      if(!CollectionUtils.isEmpty(omsOrders)){
        avgPayAmount = omsOrders.stream().map(OmsOrder::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
          .divide(BigDecimal.valueOf(omsOrders.size()), 2, BigDecimal.ROUND_HALF_UP);
      }
      Calendar calendar=Calendar.getInstance();
      calendar.add(Calendar.MONTH,-1);
      List<OmsMatcherCommission> omsMatcherCommissions = omsMatcherCommissionService
        .list(new QueryWrapper<OmsMatcherCommission>().lambda().eq(OmsMatcherCommission::getMatcherUserId, matcherUserId).gt(OmsMatcherCommission::getCreateDate,calendar.getTime()).in(OmsMatcherCommission::getStatus,"0","1","2"));
      BigDecimal profitAmount = omsMatcherCommissions.stream().map(OmsMatcherCommission::getProfit).reduce(BigDecimal.ZERO, BigDecimal::add);
      List<Long> orderIds = omsMatcherCommissions.stream().map(OmsMatcherCommission::getOrderId).collect(Collectors.toList());
      BigDecimal totalPayAmount=BigDecimal.ZERO;
      if(!CollectionUtils.isEmpty(orderIds)){

        List<OmsOrder> omsOrderList = omsOrderService.list(new QueryWrapper<OmsOrder>().lambda().in(OmsOrder::getId, orderIds));
        totalPayAmount=omsOrderList.stream().map(OmsOrder::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
      }
      orderCommissionStatistics.setAvgPayAmount(avgPayAmount);
      orderCommissionStatistics.setProfitAmount(profitAmount);
      orderCommissionStatistics.setTotalPayAmount(totalPayAmount);
      return new CommonResult().success(orderCommissionStatistics);
    }catch (Exception e){
      return new CommonResult().failed(e.getMessage());
    }
  }


}
