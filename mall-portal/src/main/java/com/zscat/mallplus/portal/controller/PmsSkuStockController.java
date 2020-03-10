package com.zscat.mallplus.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.pms.IPmsSkuStockService;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "PmsSkuStockController", description = "小程序端库存管理controller")
@RequestMapping("/api/pmsSku")
public class PmsSkuStockController {

    @Autowired
    private IPmsSkuStockService iPmsSkuStockService;


    @IgnoreAuth
    @ApiOperation("根据商品id查询sku列表")
    @GetMapping(value = "/queryPmsSkuStocksByProductId")
    public CommonResult<List<PmsSkuStock>> queryPmsSkuStocksByProductId(Long productId) {
        List<PmsSkuStock> pmsSkuStocks = iPmsSkuStockService.list(new QueryWrapper<PmsSkuStock>().eq("product_id",productId).gt("stock",0));
        return new CommonResult<>().success(pmsSkuStocks);
    }
}
