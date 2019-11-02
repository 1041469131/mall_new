package com.zscat.mallplus.mbg.pms.vo;

import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;

/**
 * SKU的包装类
 * @Date: 2019/10/23
 * @Description
 */
public class PmsSkuStockVo extends PmsSkuStock{

    private PmsProductResult pmsProductResult;

    public PmsProductResult getPmsProductResult() {
        return pmsProductResult;
    }

    public void setPmsProductResult(PmsProductResult pmsProductResult) {
        this.pmsProductResult = pmsProductResult;
    }
}
