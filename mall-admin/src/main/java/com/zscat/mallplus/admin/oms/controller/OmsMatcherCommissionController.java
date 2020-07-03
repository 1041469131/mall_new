package com.zscat.mallplus.admin.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.oms.IOmsMatcherCommissionItemService;
import com.zscat.mallplus.manage.service.oms.IOmsMatcherCommissionService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommissionItem;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommissionItemVo;
import com.zscat.mallplus.mbg.oms.vo.OmsMatcherCommissionVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "OmsMatcherCommissionController", description = "订单佣金管理")
@RequestMapping("/oms/matcherCommission")
public class OmsMatcherCommissionController {
  @Autowired
  private IOmsMatcherCommissionService iOmsMatcherCommissionService;
  @Autowired
  private IOmsMatcherCommissionItemService omsMatcherCommissionItemService;
  @Autowired
  private IOmsOrderItemService omsOrderItemService;

  @ApiOperation("分页查询佣金订单")
  @RequestMapping(value = "/pageOmsMathcerCommissions", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult<Page<OmsMatcherCommissionVo>> pageOmsMathcerCommissions(@RequestBody OmsMatcherCommissionVo omsMatcherCommissionVo) {
    Page<OmsMatcherCommissionVo> omsMatcherCommissionVoPage = iOmsMatcherCommissionService
      .pageOmsMathcerCommissions(omsMatcherCommissionVo);
    return new CommonResult().success(omsMatcherCommissionVoPage);
  }

  @ApiOperation("查询佣金订单列表")
  @RequestMapping(value = "/listOmsMathcerCommissions", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult<List<OmsMatcherCommissionVo>> listOmsMathcerCommissions(@RequestBody OmsMatcherCommissionVo omsMatcherCommissionVo) {
    List<OmsMatcherCommissionVo> omsMatcherCommissionVoList = iOmsMatcherCommissionService
      .listOmsMathcerCommissions(omsMatcherCommissionVo);
    return new CommonResult().success(omsMatcherCommissionVoList);
  }

  @ApiOperation("结算")
  @RequestMapping(value = "/updateSettleStatus", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult<Void> updateSettleStatus(@RequestBody List<OmsMatcherCommission> omsMatcherCommissions) {
    if (CollectionUtils.isEmpty(omsMatcherCommissions)) {
      return new CommonResult<Void>().failed("订单id为空");
    }
    iOmsMatcherCommissionService.updateSettleStatus(omsMatcherCommissions);
    return new CommonResult<Void>().success();
  }

  @ApiOperation("查询佣金订单列表")
  @RequestMapping(value = "/listOmsMathcerCommissionsItems", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult<List<OmsMatcherCommissionItemVo>> listOmsMathcerCommissionsItems(@ApiParam @RequestBody OmsMatcherCommissionItem omsMatcherCommissionItem) {
    List<OmsMatcherCommissionItem> omsMatcherCommissionVoList = omsMatcherCommissionItemService.list(new QueryWrapper<>(omsMatcherCommissionItem));
    List<OmsMatcherCommissionItemVo> collect = omsMatcherCommissionVoList.stream().map(omsItem -> {
      OmsMatcherCommissionItemVo o = new OmsMatcherCommissionItemVo();
      BeanUtils.copyProperties(omsItem, o);
      o.setOmsOrderItem(omsOrderItemService.getById(o.getOmsOrderItem()));
      return o;
    }).collect(Collectors.toList());
    return new CommonResult().success(collect);
  }

}
