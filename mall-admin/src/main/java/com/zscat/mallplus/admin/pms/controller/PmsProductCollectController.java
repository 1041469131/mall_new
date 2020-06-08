package com.zscat.mallplus.admin.pms.controller;

import static com.zscat.mallplus.manage.assemble.MatchLibraryAssemble.assembleSingleUserMatchLibrary;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.assemble.MatchLibraryAssemble;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.manage.service.pms.IPmsProductService;
import com.zscat.mallplus.manage.service.pms.IPmsProductUserCollectService;
import com.zscat.mallplus.manage.service.pms.IPmsProductUserMatchLibraryService;
import com.zscat.mallplus.manage.service.ums.IUmsCollectService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductMatchLibrary;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserCollect;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.vo.PmsProductHistoryVO;
import com.zscat.mallplus.mbg.pms.vo.PmsProductMatchLibraryVo;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductResult;
import com.zscat.mallplus.mbg.pms.vo.PmsProductVo;
import com.zscat.mallplus.mbg.ums.entity.UmsCollect;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiang.li create date 2020/5/28 description
 */
@Slf4j
@RestController
@Api(tags = "PmsProductCollectController", description = "收藏管理")
@RequestMapping("/pms/PmsProductCollect")
public class PmsProductCollectController {
  @Autowired
  private IPmsProductService pmsProductService;
  @Autowired
  private IPmsProductUserCollectService pmsProductUserCollectService;
  @Autowired
  private IPmsProductUserMatchLibraryService pmsProductUserMatchLibraryService;
  @Autowired
  private IUmsCollectService umsCollectService;
  @Autowired
  private IOmsOrderService omsOrderService;
  @Autowired
  private IOmsOrderReturnSaleService omsOrderReturnSaleService;
  @Autowired
  private IOmsOrderItemService omsOrderItemService;
  @Autowired
  private IPmsProductService iPmsProductService;
  @SysLog(MODULE = "pms", REMARK = "根据条件查询收藏商品信息列表")
  @ApiOperation("根据条件查询收藏商品信息列表")
  @RequestMapping(value = "/listPmsProductByPage", method = RequestMethod.POST)
  public CommonResult<Page<PmsProductVo>> listPmsProductByPage(@ApiParam("产品信息的扩展类") @RequestBody PmsProductQueryParam queryParam) {
    queryParam.setDeleteStatus(0);
    Page<PmsProductVo> pmsProductList = pmsProductService.listPmsProductCollectByPage(queryParam);
    return new CommonResult().success(pmsProductList);
  }

  @IgnoreAuth
  @SysLog(MODULE = "pms", REMARK = "查询用户喜欢的搭配库信息")
  @ApiOperation("查询用户喜欢的搭配库信息")
  @PostMapping(value = "/listLikeMatchLibrary")
//  @PreAuthorize("hasAuthority('pms:PmsBrand:read')")
  public CommonResult<Page<PmsProductMatchLibraryVo>> listMatchLibrary(@ApiParam("产品信息的扩展类") @RequestBody PmsProductQueryParam queryParam) {
    Page<PmsProductUserMatchLibrary> pmsProductUserMatchLibraryPage = pmsProductUserMatchLibraryService.listByPage(queryParam);
    List<PmsProductMatchLibraryVo> pmsProductMatchLibraryVos = pmsProductUserMatchLibraryPage.getRecords().stream().map(pmsProductUserMatchLibrary->{
      PmsProductMatchLibraryVo pmsProductMatchLibraryVo=new PmsProductMatchLibraryVo();
      pmsProductMatchLibraryVo.setPmsProductUserMatchLibrary(pmsProductUserMatchLibrary);
      String productIds = pmsProductUserMatchLibrary.getProductIds();
      if(!StringUtils.isEmpty(productIds)) {
        List<PmsProductResult> pmsProductResults= Arrays.stream(productIds.split(",")).map(productId->iPmsProductService.getUpdateInfo(Long.valueOf(productId))).collect(Collectors.toList());
        pmsProductMatchLibraryVo.setPmsProductResults(pmsProductResults);
      }
      return pmsProductMatchLibraryVo;
    }).collect(Collectors.toList());
    return new CommonResult().success(new Page<PmsProductMatchLibraryVo>(pmsProductUserMatchLibraryPage.getCurrent(),pmsProductUserMatchLibraryPage.getSize(),pmsProductUserMatchLibraryPage.getTotal()).setRecords(pmsProductMatchLibraryVos));
  }

  @IgnoreAuth
  @SysLog(MODULE = "pms", REMARK = "历史单品")
  @ApiOperation("历史单品")
  @PostMapping(value = "/listProductHistory/{memeberId}")
  public CommonResult<PmsProductHistoryVO> listProductHistory(
    @ApiParam("会员Id") @PathVariable Long memeberId) {
    Map<String, Set<Long>> favorTypeMap = umsCollectService
      .list(new QueryWrapper<UmsCollect>().lambda().eq(UmsCollect::getMemberId, memeberId).eq(UmsCollect::getType, 1))
      .stream().collect(
        Collectors.groupingBy(UmsCollect::getFavorType, Collectors.mapping(UmsCollect::getAssemblyId, Collectors.toSet())));
    //喜欢的单品
    Set<Long> productIds4like = favorTypeMap.get(MagicConstant.FAVOR_TYPE_LIKE);
    List<PmsProduct> pmsProducts4like = CollectionUtils.isEmpty(productIds4like)?new ArrayList<>():(List<PmsProduct>) pmsProductService.listByIds(productIds4like);
    //无感的单品
    Set<Long> productIds4dislike = favorTypeMap.get(MagicConstant.FAVOR_TYPE_DISLIKE);
    List<PmsProduct> pmsProducts4dislike =CollectionUtils.isEmpty(productIds4dislike)?new ArrayList<>(): (List<PmsProduct>) pmsProductService.listByIds(productIds4dislike);
    //购买过的单品
    List<Long> orderIds4Pay = omsOrderService
      .list(new QueryWrapper<OmsOrder>().lambda().eq(OmsOrder::getMemberId, memeberId)
        .in(OmsOrder::getStatus, 1, 2, 3)).stream().map(OmsOrder::getId).collect(Collectors.toList());
    List<PmsProduct> pmsProducts4Pay = getPmsProducts(orderIds4Pay);
    //退货过的单品
    List<Long> orderIds4returnSale = omsOrderReturnSaleService
      .list(new QueryWrapper<OmsOrderReturnSale>().lambda().eq(OmsOrderReturnSale::getMemberId, memeberId)
        .eq(OmsOrderReturnSale::getType, MagicConstant.RETURN_APPLY_TYPE_AFTER_SALE)).stream().map(OmsOrderReturnSale::getOrderId)
      .collect(Collectors.toList());
    List<PmsProduct> pmsProducts4returnSale = getPmsProducts(orderIds4returnSale);

    PmsProductHistoryVO pmsProductHistoryVO=new PmsProductHistoryVO();
    pmsProductHistoryVO.setPmsProducts4like(pmsProducts4like);
    pmsProductHistoryVO.setPmsProducts4dislike(pmsProducts4dislike);
    pmsProductHistoryVO.setPmsProducts4Pay(pmsProducts4Pay);
    pmsProductHistoryVO.setPmsProducts4returnSale(pmsProducts4returnSale);
    return new CommonResult().success(pmsProductHistoryVO);
  }

  private List<PmsProduct> getPmsProducts(List<Long> orderIds4Pay) {
    if(CollectionUtils.isEmpty(orderIds4Pay)){
      return new ArrayList<>();
    }
    List<Long> productIds4Pay = omsOrderItemService
      .list(new QueryWrapper<OmsOrderItem>().lambda().in(OmsOrderItem::getOrderId, orderIds4Pay))
      .stream().map(OmsOrderItem::getProductId).collect(
        Collectors.toList());
    return (List<PmsProduct>) pmsProductService.listByIds(productIds4Pay);
  }


}
