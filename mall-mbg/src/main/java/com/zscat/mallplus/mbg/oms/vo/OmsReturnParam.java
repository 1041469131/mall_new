package com.zscat.mallplus.mbg.oms.vo;

import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnApply;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("订单")
public class OmsReturnParam {

    /**
     * 售后的列表
     */
    @ApiModelProperty(value = "售后表")
    private OmsOrderReturnSale omsOrderReturnSale;

    /**
     * 售后的商品列表
     */
    @ApiModelProperty(value = "售后的商品列表")
    private List<OmsOrderReturnApply> omsOrderReturnApplies;

    public OmsOrderReturnSale getOmsOrderReturnSale() {
        return omsOrderReturnSale;
    }

    public void setOmsOrderReturnSale(OmsOrderReturnSale omsOrderReturnSale) {
        this.omsOrderReturnSale = omsOrderReturnSale;
    }

    public List<OmsOrderReturnApply> getOmsOrderReturnApplies() {
        return omsOrderReturnApplies;
    }

    public void setOmsOrderReturnApplies(List<OmsOrderReturnApply> omsOrderReturnApplies) {
        this.omsOrderReturnApplies = omsOrderReturnApplies;
    }
}
