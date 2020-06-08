package com.zscat.mallplus.admin.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.pms.IPmsProductService;
import com.zscat.mallplus.manage.service.pms.IPmsProductUserCollectService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserCollect;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiang.li create date 2020/5/28 description
 */
@Slf4j
@RestController
@Api(tags = "PmsProductUserCollectController", description = "单品收藏管理")
@RequestMapping("/pms/PmsProductUserCollect")
public class PmsProductUserCollectController {
  @Autowired
  private IPmsProductService pmsProductService;
  @Autowired
  private IPmsProductUserCollectService pmsProductUserCollectService;

  @SysLog(MODULE = "pms", REMARK = "根据条件查询收藏商品信息列表")
  @ApiOperation("根据条件查询收藏商品信息列表")
  @RequestMapping(value = "/listPmsProductByPage",method= RequestMethod.POST)
  public CommonResult<Page<PmsProductVo>> listPmsProductByPage(@ApiParam("产品信息的扩展类") @RequestBody PmsProductQueryParam queryParam) {
    queryParam.setDeleteStatus(0);
    Page<PmsProductVo> pmsProductList = pmsProductService.listPmsProductCollectByPage(queryParam);
    return new CommonResult().success(pmsProductList);
  }

  @SysLog(MODULE = "pms", REMARK = "收藏商品")
  @ApiOperation("收藏商品")
  @RequestMapping(value = "/add/{productId}",method= RequestMethod.POST)
  public CommonResult add(@ApiParam("产品Id") @PathVariable Long  productId) {
    Long matcherUserId = UserUtils.getCurrentMember().getId();
    PmsProductUserCollect pmsProductUserCollect = pmsProductUserCollectService.getOne(
      new QueryWrapper<PmsProductUserCollect>().lambda().eq(PmsProductUserCollect::getProductId, productId)
        .eq(PmsProductUserCollect::getMatchUserId, matcherUserId));
    if(pmsProductUserCollect==null) {
      pmsProductUserCollect=new PmsProductUserCollect();
      pmsProductUserCollect.setMatchUserId(matcherUserId);
      pmsProductUserCollect.setProductId(productId);
      pmsProductUserCollectService.save(pmsProductUserCollect);
    }
    return new CommonResult().success();
  }

  @SysLog(MODULE = "pms", REMARK = "收藏商品ids")
  @ApiOperation("收藏商品Ids")
  @RequestMapping(value = "/list/getIds",method= RequestMethod.POST)
  public CommonResult getIds() {
    Long matcherUserId = UserUtils.getCurrentMember().getId();
    Set<Long> ids = pmsProductUserCollectService.list(
      new QueryWrapper<PmsProductUserCollect>().lambda().eq(PmsProductUserCollect::getMatchUserId, matcherUserId)).stream()
      .map(PmsProductUserCollect::getProductId).collect(
        Collectors.toSet());
    return new CommonResult().success(ids);
  }


  @SysLog(MODULE = "pms", REMARK = "取消收藏商品")
  @ApiOperation("取消收藏商品")
  @RequestMapping(value = "/delete/{productId}",method= RequestMethod.POST)
  public CommonResult delete(@ApiParam("产品Id") @PathVariable Long  productId) {
    Long matcherUserId = UserUtils.getCurrentMember().getId();
    pmsProductUserCollectService.remove(
      new QueryWrapper<PmsProductUserCollect>().lambda().eq(PmsProductUserCollect::getProductId, productId)
        .eq(PmsProductUserCollect::getMatchUserId, matcherUserId));
    return new CommonResult().success();
  }

  @SysLog(MODULE = "pms", REMARK = "根据条件查询喜欢商品信息列表")
  @ApiOperation("根据条件喜欢商品信息列表")
  @RequestMapping(value = "/listLikePmsProductByPage",method= RequestMethod.POST)
  public CommonResult<Page<PmsProductVo>> listLikePmsProductByPage(@ApiParam("产品信息的扩展类") @RequestBody PmsProductQueryParam queryParam) {
    queryParam.setDeleteStatus(0);
    Page<PmsProductVo> pmsProductList = pmsProductService.listPmsProductCollectByPage(queryParam);
    return new CommonResult().success(pmsProductList);
  }

}
