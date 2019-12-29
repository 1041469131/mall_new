package com.zscat.mallplus.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.assemble.MatchLibraryAssemble;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnReasonService;
import com.zscat.mallplus.manage.service.pms.IPmsProductService;
import com.zscat.mallplus.manage.service.pms.IPmsProductUserMatchLibraryService;
import com.zscat.mallplus.manage.service.pms.IPmsSkuStockService;
import com.zscat.mallplus.manage.service.ums.IUmsCollectService;
import com.zscat.mallplus.manage.utils.JsonUtil;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnReason;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import com.zscat.mallplus.mbg.pms.vo.PmsProductMatchLibraryVo;
import com.zscat.mallplus.mbg.pms.vo.PmsProductResult;
import com.zscat.mallplus.mbg.pms.vo.PmsSkuStockVo;
import com.zscat.mallplus.mbg.ums.entity.UmsCollect;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.IdGeneratorUtil;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import com.zscat.mallplus.portal.single.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户喜欢/收藏
 * @Date: 2019/10/20
 * @Description
 */
@Slf4j
@Controller
@Api(tags = "UmsCollectController", description = "喜欢/收藏管理")
@RequestMapping("/api/collect")
public class UmsCollectController extends ApiBaseAction {

    @Autowired
    private IUmsCollectService iUmsCollectService;

    @Autowired
    private IPmsProductUserMatchLibraryService iPmsProductUserMatchLibraryService;

    @Autowired
    private IPmsSkuStockService iPmsSkuStockService;

    @Autowired
    private IPmsProductService iPmsProductService;

    @Autowired
    private IOmsOrderReturnReasonService iOmsOrderReturnReasonService;

    @ApiOperation("保存用户收藏")
    @PostMapping(value = "/saveOrUpdate")
    @ResponseBody
    public CommonResult<UmsCollect> saveOrUpdateCollect(@ApiParam("用户收藏对象") UmsCollect umsCollect) {
        UmsCollect umsCollectOld = iUmsCollectService.getOne(new QueryWrapper<UmsCollect>().eq("assembly_id", umsCollect.getAssemblyId()).
                eq("member_id", UserUtils.getCurrentUmsMember().getId()));
        umsCollect.setUpdateTime(new Date());
        if(umsCollectOld == null){
            umsCollect.setCreateTime(new Date());
            umsCollect.setId(IdGeneratorUtil.getIdGeneratorUtil().nextId());
            umsCollect.setMemberId(UserUtils.getCurrentUmsMember().getId());
        }else{
            umsCollect.setId(umsCollectOld.getId());
        }

        if(iUmsCollectService.saveOrUpdate(umsCollect)){
            return new CommonResult<>().success("操作成功");
        }
        return new CommonResult().failed("操作失败");
    }

    @ApiOperation("查询我喜欢的页面列表")
    @PostMapping(value = "/listMyFavors")
    @ResponseBody
    public CommonResult listMyFavors(@ApiParam("1 商品 2 文章 3 搭配")String type) {

        List<UmsCollect> umsCollects = iUmsCollectService.list(new QueryWrapper<UmsCollect>().eq("type", type).
                eq("member_id",UserUtils.getCurrentUmsMember().getId()).eq("favor_type", MagicConstant.FAVOR_TYPE_LIKE));
        if(!CollectionUtils.isEmpty(umsCollects)){
            List<Long> assemblyIds = new ArrayList<>();
            for(UmsCollect umsCollect:umsCollects){
                assemblyIds.add(umsCollect.getAssemblyId());
            }
            if(String.valueOf(MagicConstant.FAVOR_TYPE_PRODUCT).equals(type)){//商品
                List<PmsSkuStock> pmsSkuStocks = (List<PmsSkuStock>)iPmsSkuStockService.listByIds(assemblyIds);
                List<PmsSkuStockVo> pmsSkuStockVos = null;
                if(!CollectionUtils.isEmpty(pmsSkuStocks)){
                    pmsSkuStockVos = new ArrayList<>();
                    for(PmsSkuStock pmsSkuStock:pmsSkuStocks ){
                        PmsSkuStockVo pmsSkuStockVo = new PmsSkuStockVo();
                        BeanUtils.copyProperties(pmsSkuStock, pmsSkuStockVo);
                        PmsProductResult pmsProductResult = iPmsProductService.getUpdateInfo(pmsSkuStock.getProductId());
                        pmsSkuStockVo.setPmsProductResult(pmsProductResult);
                        pmsSkuStockVos.add(pmsSkuStockVo);
                    }
                }
                return new CommonResult().success(pmsSkuStockVos);
            }
            if(String.valueOf(MagicConstant.FAVOR_TYPE_MATCH_LIBRARY).equals(type)){//搭配
                List<PmsProductUserMatchLibrary> pmsProductUserMatchLibraries = (List<PmsProductUserMatchLibrary>)iPmsProductUserMatchLibraryService.listByIds(assemblyIds);
                List<PmsProductMatchLibraryVo> pmsProductMatchLibraryVos = MatchLibraryAssemble.assembleUserMatchLibrary(pmsProductUserMatchLibraries);
                return new CommonResult().success(pmsProductMatchLibraryVos);
            }
        }
        return new CommonResult().success(umsCollects);
    }

    @ApiOperation("查询收藏不喜欢的原因")
    @RequestMapping(value = "/getReasons4Collect",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getReasons4Collect(){
        List<OmsOrderReturnReason> reasons = iOmsOrderReturnReasonService.list(new QueryWrapper<OmsOrderReturnReason>().eq("type","1"));
        return new CommonResult().success(reasons);
    }

}
