package com.zscat.mallplus.mbg.pms.vo;

import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductMatchLibrary;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 *
 * @Date: 2019/10/15
 * @Description
 */
@ApiModel("搭配库中的数据信息")
public class PmsProductMatchLibraryVo {

    @ApiModelProperty("商品信息")
    private List<PmsProduct> pmsProducts;

    @ApiModelProperty("搭配库信息")
    private PmsProductMatchLibrary pmsProductMatchLibrary;

    @ApiModelProperty("用户搭配库信息")
    private PmsProductUserMatchLibrary pmsProductUserMatchLibrary;

    @ApiModelProperty("sku相关属性的查询")
    private List<PmsSkuStockVo> pmsSkuStockVos;


    public List<PmsProduct> getPmsProducts() {
        return pmsProducts;
    }

    public void setPmsProducts(List<PmsProduct> pmsProducts) {
        this.pmsProducts = pmsProducts;
    }

    public PmsProductMatchLibrary getPmsProductMatchLibrary() {
        return pmsProductMatchLibrary;
    }

    public void setPmsProductMatchLibrary(PmsProductMatchLibrary pmsProductMatchLibrary) {
        this.pmsProductMatchLibrary = pmsProductMatchLibrary;
    }

    public PmsProductUserMatchLibrary getPmsProductUserMatchLibrary() {
        return pmsProductUserMatchLibrary;
    }

    public void setPmsProductUserMatchLibrary(PmsProductUserMatchLibrary pmsProductUserMatchLibrary) {
        this.pmsProductUserMatchLibrary = pmsProductUserMatchLibrary;
    }

    public List<PmsSkuStockVo> getPmsSkuStockVos() {
        return pmsSkuStockVos;
    }

    public void setPmsSkuStockVos(List<PmsSkuStockVo> pmsSkuStockVos) {
        this.pmsSkuStockVos = pmsSkuStockVos;
    }
}
