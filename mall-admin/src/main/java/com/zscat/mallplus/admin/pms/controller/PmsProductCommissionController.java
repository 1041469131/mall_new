package com.zscat.mallplus.admin.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.manage.service.pms.IPmsProductCommissionService;
import com.zscat.mallplus.manage.utils.JsonUtil;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderItemVo;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import com.zscat.mallplus.mbg.pms.entity.PmsProductConsult;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductCommissionMapper;
import com.zscat.mallplus.mbg.pms.vo.PmsProductCommissionVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "PmsProductCommissionController", description = "商品佣金管理")
@RequestMapping("/pms/productCommission")
public class PmsProductCommissionController {

    @Autowired
    private IPmsProductCommissionService iPmsProductCommissionService;

    @Autowired
    private IOmsOrderItemService iOmsOrderItemService;

    @Autowired
    private PmsProductCommissionMapper pmsProductCommissionMapper;

    @SysLog(MODULE = "pms", REMARK = "保存分佣比例")
    @ApiOperation("保存分佣比例")
    @PostMapping(value = "/saveOrUpdateCommisssion")
    public Object saveOrUpdateCommisssion(@RequestBody List<PmsProductCommission> pmsProductCommissions) {
        if(!CollectionUtils.isEmpty(pmsProductCommissions)){
            pmsProductCommissions.forEach(pmsProductCommission -> {
                if (pmsProductCommission.getId() == null) {
                    pmsProductCommission.setCreateDate(new Date());
                    pmsProductCommission.setCreateTime(System.currentTimeMillis());
                }
                pmsProductCommission.setUpdateDate(new Date());
                pmsProductCommission.setUpdateTime(System.currentTimeMillis());
            });
            iPmsProductCommissionService.saveOrUpdateBatch(pmsProductCommissions);
        }
        return new CommonResult().success();
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品的分佣比例")
    @ApiOperation("查询商品的分佣比例")
    @GetMapping(value = "/queryCommission")
//    @PreAuthorize("hasAuthority('pms:PmsProductConsult:read')")
    public Object queryCommission(Long productId) {
        if(productId == null){
            return new CommonResult<>().failed("商品id为空");
        }
        List<PmsProductCommission> pmsProductCommission = iPmsProductCommissionService.list(new QueryWrapper<PmsProductCommission>().eq("product_id",productId));
        return new CommonResult().success(pmsProductCommission);
    }

    @SysLog(MODULE = "pms", REMARK = "批量修改分佣比例")
    @ApiOperation("批量修改分佣比例")
    @PostMapping(value = "/batchUpdateCommission")
//    @PreAuthorize("hasAuthority('pms:PmsProductConsult:read')")
    public Object batchUpdateCommission(@RequestBody PmsProductCommissionVo pmsProductCommissionVo) {

        if(StringUtils.isEmpty(pmsProductCommissionVo.getProductIds())){
            return new CommonResult<>().failed("批量数据的商品id不能为空");
        }

        iPmsProductCommissionService.batchUpdateCommission(pmsProductCommissionVo);

        return new CommonResult().success();
    }

    @SysLog(MODULE = "pms", REMARK = "查询收益比例")
    @ApiOperation("查询收益比例")
    @PostMapping(value = "/queryPrfitProportion")
//    @PreAuthorize("hasAuthority('pms:PmsProductConsult:read')")
    public Object queryPrfitProportion(Long omsOrderId) {
        List<OmsOrderItemVo> omsOrderItemVos = iOmsOrderItemService.queryPrfitProportion(omsOrderId);
        return new CommonResult().success();
    }
}
