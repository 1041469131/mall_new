package com.zscat.mallplus.manage.service.oms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderSaleParam;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderReturnSaleVO;

import java.util.Map;

/**
 * 售后表服务
 */
public interface IOmsOrderReturnSaleService extends IService<OmsOrderReturnSale> {

    /**
     * 修改申请状态
     */
    int updateStatus(OmsOrderReturnSale returnSale);

    /**
     * 分页查询
     * @param queryParam
     * @return
     */
    Page<OmsOrderReturnSale> listByPage(OmsOrderSaleParam queryParam);

    Page<OmsOrderReturnSaleVO> listVoByPage(OmsOrderSaleParam queryParam);
}
