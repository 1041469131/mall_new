package com.zscat.mallplus.mbg.pms.vo;

import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Date: 2020/4/19
 * @Description
 */
@ApiModel("分佣的扩展类")
public class PmsProductCommissionVo extends PmsProductCommission {

    @ApiModelProperty("商品ids，用逗号隔开")
    private String productIds;

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }
}
