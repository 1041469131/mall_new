package com.zscat.mallplus.admin.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.assemble.MatchLibraryAssemble;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.manage.service.pms.IPmsProductMatchLibraryService;
import com.zscat.mallplus.manage.service.pms.IPmsProductService;
import com.zscat.mallplus.manage.service.pms.IPmsProductUserMatchLibraryService;
import com.zscat.mallplus.manage.service.pms.IPmsSkuStockService;
import com.zscat.mallplus.manage.utils.JsonUtil;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductMatchLibrary;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.vo.PmsProductMatchLibraryVo;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductResult;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.IdGeneratorUtil;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 搭配相关的controller
 * @Date: 2019/10/15
 * @Description
 */
@Slf4j
@RestController
@Api(tags = "PmsProductMatchController", description = "收藏管理搭配相关")
@RequestMapping("/pms/PmsProductMatch")
public class PmsProductMatchController {

    @Autowired
    private IPmsProductMatchLibraryService iPmsProductMatchLibraryService;

    @Autowired
    private IPmsProductUserMatchLibraryService iPmsProductUserMatchLibraryService;

    @Autowired
    private IPmsProductService iPmsProductService;

    @Autowired
    private IOmsOrderService omsOrderService;
    @Autowired
    private IOmsOrderReturnSaleService omsOrderReturnSaleService;
    @Autowired
    private IOmsOrderItemService omsOrderItemService;

    @IgnoreAuth
    @SysLog(MODULE = "pms", REMARK = "保存或者更新搭配库信息")
    @ApiOperation("保存或者更新搭配库信息")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public CommonResult<PmsProductMatchLibrary> saveOrUpdatePmsProductMatchLibrary(@ApiParam("搭配库") @RequestBody PmsProductMatchLibrary pmsProductMatchLibrary) {
        Long userId = UserUtils.getCurrentMember().getId();
        Long id = pmsProductMatchLibrary.getId();
        if(id == null){
            pmsProductMatchLibrary.setId(IdGeneratorUtil.getIdGeneratorUtil().nextId());
            pmsProductMatchLibrary.setOperateId(userId);
            pmsProductMatchLibrary.setUpdateTime(new Date());
            pmsProductMatchLibrary.setCreateTime(new Date());
        }else{
            pmsProductMatchLibrary.setUpdateTime(new Date());
        }
        if(StringUtils.isEmpty(pmsProductMatchLibrary.getCollectStatus())){
            pmsProductMatchLibrary.setCollectStatus(MagicConstant.COLLECT_STATUS_YES);
        }
        if(StringUtils.isEmpty(pmsProductMatchLibrary.getMatchType())){
            pmsProductMatchLibrary.setMatchType(MagicConstant.MATCH_TYPE_COMBIN);
        }
        if(StringUtils.isEmpty(pmsProductMatchLibrary.getMatchOwer())){
            pmsProductMatchLibrary.setMatchOwer(MagicConstant.MATCH_OWER_PERSON);
        }
        if(iPmsProductMatchLibraryService.saveOrUpdate(pmsProductMatchLibrary)){
            return new CommonResult<PmsProductMatchLibrary>().success(pmsProductMatchLibrary);
        }
        return  new CommonResult<PmsProductMatchLibrary>().failed("操作失败");
    }


    @IgnoreAuth
    @SysLog(MODULE = "pms", REMARK = "购物车提交")
    @ApiOperation("购物车提交")
    @RequestMapping(value = "/saveOrUpdate4User", method = RequestMethod.POST)
    public CommonResult<List<PmsProductUserMatchLibrary>> saveMatchLibrary4User(@ApiParam("用户搭配库") @RequestBody List<PmsProductUserMatchLibrary> pmsProductUserMatchLibrarys) {
        for (int i = 0; i < pmsProductUserMatchLibrarys.size(); i++) {
            PmsProductUserMatchLibrary pmsProductUserMatchLibrary=pmsProductUserMatchLibrarys.get(i);
            if (pmsProductUserMatchLibrary.getId() == null) {
                pmsProductUserMatchLibrary.setId(IdGeneratorUtil.getIdGeneratorUtil().nextId()+i);
                pmsProductUserMatchLibrary.setCreateTime(new Date());
            } else {
                if (StringUtils.isEmpty(pmsProductUserMatchLibrary.getSkuIds())) {
                    return new CommonResult<List<PmsProductUserMatchLibrary>>().failed("该用户推荐没有选择规格");
                }
                PmsProductUserMatchLibrary oldPmsProductUserMatchLibrary = iPmsProductUserMatchLibraryService
                  .getById(pmsProductUserMatchLibrary.getId());
                if (MagicConstant.RECOMMEND_TYPE_YES.equals(oldPmsProductUserMatchLibrary.getRecommendType())) {
                    return new CommonResult<List<PmsProductUserMatchLibrary>>().failed("该用户是已推荐的状态不能修改");
                }
            }
            pmsProductUserMatchLibrary.setUpdateTime(new Date());
            pmsProductUserMatchLibrary.setMatchUserId(UserUtils.getCurrentMember().getId());
            if (StringUtils.isEmpty(pmsProductUserMatchLibrary.getMatchType())) {
                pmsProductUserMatchLibrary.setMatchType(MagicConstant.MATCH_TYPE_COMBIN);
            }
            if (StringUtils.isEmpty(pmsProductUserMatchLibrary.getRecommendType())) {
                pmsProductUserMatchLibrary.setRecommendType(MagicConstant.RECOMMEND_TYPE_NO);
            }
        }
        iPmsProductUserMatchLibraryService.saveOrUpdateBatch(pmsProductUserMatchLibrarys);
        return new CommonResult<List<PmsProductUserMatchLibrary>>().success(pmsProductUserMatchLibrarys);
    }


    @IgnoreAuth
    @SysLog(MODULE = "pms", REMARK = "查询搭配库信息")
    @ApiOperation("查询（收藏）搭配库信息")
    @PostMapping(value = "/listMatchLibrary")
//    @PreAuthorize("hasAuthority('pms:PmsBrand:read')")
    public CommonResult<Page<PmsProductMatchLibraryVo>> listMatchLibrary(@ApiParam("条件")@RequestBody PmsProductQueryParam queryParam) {
        List<PmsProductMatchLibraryVo> pmsProductMatchLibraryVos = null;
        Page<PmsProductMatchLibrary> pmsProductMatchLibraryPage = iPmsProductMatchLibraryService.listByPage(queryParam);
        List<PmsProductMatchLibrary> pmsProductMatchLibraries =  pmsProductMatchLibraryPage.getRecords();
        if(!CollectionUtils.isEmpty(pmsProductMatchLibraries)){
            pmsProductMatchLibraryVos = new ArrayList<>();
            for(PmsProductMatchLibrary pmsProductMatchLibrary : pmsProductMatchLibraries){
                PmsProductMatchLibraryVo pmsProductMatchLibraryVo = new PmsProductMatchLibraryVo();
                String productIdstr = pmsProductMatchLibrary.getProductIds();
                if(!StringUtils.isEmpty(productIdstr)){
//                    String[] productArray = productIds.split(",");
//                    List<PmsProductResult> pmsProductResults = new ArrayList<>();
//                    for(String productId:productArray){
//                        PmsProductResult pmsProductResult = iPmsProductService.getUpdateInfo(Long.valueOf(productId));
//                        pmsProductResults.add(pmsProductResult);
//                    }
                    List<Long> productIds = Arrays.stream(productIdstr.split(",")).map(e -> Long.valueOf(e)).collect(Collectors.toList());
                    List<PmsProductResult> pmsProductResults = iPmsProductService.getProductResults(productIds);
                    pmsProductMatchLibraryVo.setPmsProductResults(pmsProductResults);
                    pmsProductMatchLibraryVo.setPmsProductMatchLibrary(pmsProductMatchLibrary);
                    pmsProductMatchLibraryVos.add(pmsProductMatchLibraryVo);
                }
            }
        }
        Page<PmsProductMatchLibraryVo> productMatchLibraryVoPage=new Page<>(pmsProductMatchLibraryPage.getCurrent(),pmsProductMatchLibraryPage.getSize(),pmsProductMatchLibraryPage.getTotal());
        productMatchLibraryVoPage.setRecords(pmsProductMatchLibraryVos);
        return  new CommonResult().success(productMatchLibraryVoPage);
    }

    @IgnoreAuth
    @SysLog(MODULE = "pms", REMARK = "删除我的搭配库")
    @ApiOperation("删除搭配库")
    @PostMapping(value = "/deleteMatchLibraryById")
    @PreAuthorize("hasAuthority('pms:PmsBrand:read')")
    public CommonResult deleteMatchLibraryById(@ApiParam("搭配库id") Long matchId) {
        iPmsProductMatchLibraryService.removeById(matchId);
        return new CommonResult().success("成功删除该搭配库");
    }

    @IgnoreAuth
    @SysLog(MODULE = "pms", REMARK = "删除用户搭配库")
    @ApiOperation("删除用户搭配库")
    @PostMapping(value = "/deleteUserMatchLibraryById")
    @PreAuthorize("hasAuthority('pms:PmsBrand:read')")
    public CommonResult deleteUserMatchLibraryById(@ApiParam("用户搭配库id") String matchIds) {
        String[] matchIdStrs = matchIds.split(",");
        List<Long> matchIdList = new ArrayList<>();
        for(String matchId:matchIdStrs){
            matchIdList.add(Long.valueOf(matchId));
        }
        iPmsProductUserMatchLibraryService.removeByIds(matchIdList);
        return new CommonResult().success("成功删除该搭配库");
    }

    @IgnoreAuth
    @SysLog(MODULE = "pms", REMARK = "修改用户搭配库推荐状态")
    @ApiOperation("修改用户搭配库推荐状态")
    @PostMapping(value = "/updateUserMatchLibraryStatus")
    @PreAuthorize("hasAuthority('pms:PmsBrand:read')")
    public CommonResult<PmsProductUserMatchLibrary> updateUserMatchLibraryStatus(@ApiParam("用户搭配库id组合格式{1,2,3}") String matchIdParam,
                                                                                 @ApiParam("推荐用户状态 0-未推荐 1-推荐")String recommType) {
        if(iPmsProductUserMatchLibraryService.saveProductUserMatch(matchIdParam,recommType)){
            return new CommonResult().success("操作成功");
        }
        return new CommonResult().failed("查询的用户搭配为空");
    }

    @IgnoreAuth
    @SysLog(MODULE = "pms", REMARK = "查询用户的搭配库")
    @ApiOperation("查询用户的搭配库")
    @PostMapping(value = "/listUserMatchLibaray")
    public CommonResult<List<PmsProductMatchLibraryVo>> listUserMatchLibaray(@RequestBody @ApiParam("查询参数") PmsProductQueryParam queryParam) {
        Long userId = UserUtils.getCurrentMember().getId();
        List<PmsProductUserMatchLibrary> pmsProductUserMatchLibraries = iPmsProductUserMatchLibraryService.list(new QueryWrapper<PmsProductUserMatchLibrary>().
                eq("match_user_id", userId).eq("user_id", queryParam.getMemberId()).eq("recommend_type",queryParam.getRecommendType()==null?1:queryParam.getRecommendType()).orderByDesc("update_time"));
        List<PmsProductMatchLibraryVo> pmsProductMatchLibraryVos = MatchLibraryAssemble.assembleUserMatchLibrary(pmsProductUserMatchLibraries);
        return  new CommonResult().success(pmsProductMatchLibraryVos);
    }


    @IgnoreAuth
    @SysLog(MODULE = "pms", REMARK = "用户商品订单状态")
    @ApiOperation("用户商品订单状态")
    @PostMapping(value = "/listOrderStatus")
    public CommonResult<Map<Long,Long>> listOrderStatus(@ApiParam("用户id") Long memberId) {
        //购买过的单品
        List<Long> orderIds4Pay = omsOrderService
          .list(new QueryWrapper<OmsOrder>().lambda().eq(OmsOrder::getMemberId, memberId).in(OmsOrder::getStatus, 1, 2, 3)).stream().map(OmsOrder::getId).collect(Collectors.toList());
        List<Long> productIds4Pay = CollectionUtils.isEmpty(orderIds4Pay)?new ArrayList<>():omsOrderItemService
          .list(new QueryWrapper<OmsOrderItem>().lambda().in(OmsOrderItem::getOrderId, orderIds4Pay))
          .stream().map(OmsOrderItem::getProductId).collect(
            Collectors.toList());

        //退货过的单品
        List<Long> orderIds4returnSale = omsOrderReturnSaleService
          .list(new QueryWrapper<OmsOrderReturnSale>().lambda().eq(OmsOrderReturnSale::getMemberId, memberId)
            .eq(OmsOrderReturnSale::getType, MagicConstant.RETURN_APPLY_TYPE_AFTER_SALE)).stream().map(OmsOrderReturnSale::getOrderId)
          .collect(Collectors.toList());
        List<Long> productIds4returnSale = CollectionUtils.isEmpty(orderIds4returnSale)?new ArrayList<>():omsOrderItemService
          .list(new QueryWrapper<OmsOrderItem>().lambda().in(OmsOrderItem::getOrderId, orderIds4returnSale))
          .stream().map(OmsOrderItem::getProductId).collect(
            Collectors.toList());

        Map<Long,Long> orderStatus=new LinkedHashMap<>();
        productIds4Pay.forEach(productIdPay->orderStatus.put(productIdPay,1L));
        productIds4returnSale.forEach(productIdReturnSale->orderStatus.put(productIdReturnSale,0L));
        return  new CommonResult().success(orderStatus);
    }

    @IgnoreAuth
    @SysLog(MODULE = "pms", REMARK = "将用户的搭配放到收藏搭配库")
    @ApiOperation("将用户的搭配放到收藏搭配库")
    @PostMapping(value = "/setUserMatch2Match")
    @PreAuthorize("hasAuthority('pms:PmsBrand:read')")
    public CommonResult<PmsProductUserMatchLibrary> setUserMatch2Match(@ApiParam("用户搭配id") @Param("matchId") Long matchId) {
        PmsProductUserMatchLibrary pmsProductUserMatchLibrary = iPmsProductUserMatchLibraryService.getById(matchId);
        PmsProductMatchLibrary pmsProductMatchLibrary = MatchLibraryAssemble.assembleMatchLibrary(pmsProductUserMatchLibrary);
        iPmsProductMatchLibraryService.save(pmsProductMatchLibrary);
        return  new CommonResult<PmsProductUserMatchLibrary>().success();
    }
}
