package com.zscat.mallplus.mbg.pms.vo;

import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Date: 2020/4/19
 * @Description
 */
@ApiModel("产品信息的扩展类")
public class PmsProductVo extends PmsProduct{

    @ApiModelProperty("产品佣金比例")
    private List<PmsProductCommission> pmsProductCommissions;

    @ApiModelProperty("商品sku")
    private List<PmsSkuStock> pmsSkuStocks ;

    @ApiModelProperty("起始价格区间")
    private BigDecimal startPrice;

    @ApiModelProperty("结束价格区间")
    private BigDecimal endPrice;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页的总数据")
    private Integer pageSize;

    @ApiModelProperty("'喜欢的类型 0-不喜欢 1-喜欢'")
    private  String  favoriteType ;

    public String getFavoriteType() {
        return favoriteType;
    }

    public void setFavoriteType(String favoriteType) {
        this.favoriteType = favoriteType;
    }

    public List<PmsProductCommission> getPmsProductCommissions() {
        return pmsProductCommissions;
    }

    public void setPmsProductCommissions(List<PmsProductCommission> pmsProductCommissions) {
        this.pmsProductCommissions = pmsProductCommissions;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<PmsSkuStock> getPmsSkuStocks() {
        return pmsSkuStocks;
    }

    public PmsProductVo setPmsSkuStocks(List<PmsSkuStock> pmsSkuStocks) {
        this.pmsSkuStocks = pmsSkuStocks;
        return this;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public PmsProductVo setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
        return this;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public PmsProductVo setEndPrice(BigDecimal endPrice) {
        this.endPrice = endPrice;
        return this;
    }
}
