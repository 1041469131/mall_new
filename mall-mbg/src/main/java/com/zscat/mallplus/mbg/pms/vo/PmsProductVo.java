package com.zscat.mallplus.mbg.pms.vo;

import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Date: 2020/4/19
 * @Description
 */
@ApiModel("产品信息的扩展类")
public class PmsProductVo extends PmsProduct{

    @ApiModelProperty("产品佣金比例")
    private List<PmsProductCommission> pmsProductCommissions;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页的总数据")
    private Integer pageSize;

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
}
