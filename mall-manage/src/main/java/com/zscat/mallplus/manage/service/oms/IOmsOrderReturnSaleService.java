package com.zscat.mallplus.manage.service.oms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.vo.OmsUpdateStatusParam;

/**
 * 售后表服务
 */
public interface IOmsOrderReturnSaleService extends IService<OmsOrderReturnSale> {

    /**
     * 修改申请状态
     */
    int updateStatus(Long id, OmsUpdateStatusParam statusParam);
}
