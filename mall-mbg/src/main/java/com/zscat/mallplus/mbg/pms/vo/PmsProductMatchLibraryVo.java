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
    private List<PmsProductResult> pmsProductResults;

    @ApiModelProperty("搭配库信息")
    private PmsProductMatchLibrary pmsProductMatchLibrary;

    @ApiModelProperty("用户搭配库信息")
    private PmsProductUserMatchLibrary pmsProductUserMatchLibrary;

    @ApiModelProperty("sku相关属性的查询")
    private List<PmsSkuStockVo> pmsSkuStockVos;

    @ApiModelProperty("喜欢的类型 0-不喜欢 1-喜欢")
    private String userMatchLibaryFavorType;

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

    public List<PmsProductResult> getPmsProductResults() {
        return pmsProductResults;
    }

    public void setPmsProductResults(List<PmsProductResult> pmsProductResults) {
        this.pmsProductResults = pmsProductResults;
    }

    public String getUserMatchLibaryFavorType() {
        return userMatchLibaryFavorType;
    }

    public void setUserMatchLibaryFavorType(String userMatchLibaryFavorType) {
        this.userMatchLibaryFavorType = userMatchLibaryFavorType;
    }
}
