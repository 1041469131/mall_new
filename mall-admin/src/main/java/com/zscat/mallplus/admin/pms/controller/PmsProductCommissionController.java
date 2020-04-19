package com.zscat.mallplus.admin.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.pms.IPmsProductCommissionService;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import com.zscat.mallplus.mbg.pms.entity.PmsProductConsult;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@Api(tags = "PmsProductCommissionController", description = "商品佣金管理")
@RequestMapping("/pms/productCommission")
public class PmsProductCommissionController {

    @Autowired
    private IPmsProductCommissionService iPmsProductCommissionService;

    @SysLog(MODULE = "pms", REMARK = "保存分佣比例")
    @ApiOperation("保存分佣比例")
    @PostMapping(value = "/saveOrUpdateCommisssion")
//    @PreAuthorize("hasAuthority('pms:PmsProductConsult:read')")
    public Object saveOrUpdateCommisssion(@RequestBody PmsProductCommission pmsProductCommission) {
        if(pmsProductCommission.getId() == null){
            pmsProductCommission.setCreateDate(new Date());
            pmsProductCommission.setCreateTime(new Date().getTime());
        }
        pmsProductCommission.setUpdateDate(new Date());
        pmsProductCommission.setUpdateTime(new Date().getTime());
        iPmsProductCommissionService.saveOrUpdate(pmsProductCommission);
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品的分佣比例")
    @ApiOperation("查询商品的分佣比例")
    @GetMapping(value = "/queryCommission")
//    @PreAuthorize("hasAuthority('pms:PmsProductConsult:read')")
    public Object queryCommission(Long productId) {
        if(productId == null){
            return new CommonResult<>().failed("商品id为空");
        }
        PmsProductCommission pmsProductCommission = iPmsProductCommissionService.getOne(new QueryWrapper<PmsProductCommission>().eq("product_id",productId));
        return new CommonResult().success(pmsProductCommission);
    }
}
