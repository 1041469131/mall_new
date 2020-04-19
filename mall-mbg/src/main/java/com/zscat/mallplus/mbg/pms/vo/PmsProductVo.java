package com.zscat.mallplus.mbg.pms.vo;

import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Date: 2020/4/19
 * @Description
 */
@ApiModel("产品信息的扩展类")
public class PmsProductVo extends PmsProduct{

    @ApiModelProperty("产品佣金比例")
    private PmsProductCommission pmsProductCommission;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页的总数据")
    private Integer pageSize;

    public PmsProductCommission getPmsProductCommission() {
        return pmsProductCommission;
    }

    public void setPmsProductCommission(PmsProductCommission pmsProductCommission) {
        this.pmsProductCommission = pmsProductCommission;
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
}
