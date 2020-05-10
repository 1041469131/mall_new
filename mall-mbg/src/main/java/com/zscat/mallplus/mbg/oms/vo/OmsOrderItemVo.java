package com.zscat.mallplus.mbg.oms.vo;

import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;

import java.util.List;

public class OmsOrderItemVo extends OmsOrderItem{

    private List<PmsProductCommission> pmsProductCommissions;

    public List<PmsProductCommission> getPmsProductCommissions() {
        return pmsProductCommissions;
    }

    public void setPmsProductCommissions(List<PmsProductCommission> pmsProductCommissions) {
        this.pmsProductCommissions = pmsProductCommissions;
    }
}
