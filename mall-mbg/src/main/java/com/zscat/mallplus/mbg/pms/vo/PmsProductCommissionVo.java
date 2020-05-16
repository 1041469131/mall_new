package com.zscat.mallplus.mbg.pms.vo;

import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import io.micrometer.core.instrument.util.JsonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2020/4/19
 * @Description
 */
@ApiModel("分佣的扩展类")
public class PmsProductCommissionVo extends PmsProductCommission {

    @ApiModelProperty("商品ids，用逗号隔开")
    private String productIds;

    @ApiModelProperty("分佣比例字符串")
    private List<PmsProductCommission> PmsProductCommissions;

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public List<PmsProductCommission> getPmsProductCommissions() {
        return PmsProductCommissions;
    }

    public void setPmsProductCommissions(List<PmsProductCommission> pmsProductCommissions) {
        PmsProductCommissions = pmsProductCommissions;
    }
}
